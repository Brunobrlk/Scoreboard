package com.bgbrlk.scoreboardbrlk.ui.score

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bgbrlk.scoreboardbrlk.BuildConfig
import com.bgbrlk.scoreboardbrlk.R
import com.bgbrlk.scoreboardbrlk.databinding.BottomsheetSettingsBinding
import com.bgbrlk.scoreboardbrlk.databinding.DialogFinalScoreBinding
import com.bgbrlk.scoreboardbrlk.databinding.FragmentScoreBinding
import com.bgbrlk.scoreboardbrlk.helpers.Constants.FIVE_PERCENT
import com.bgbrlk.scoreboardbrlk.helpers.DebugUtils
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class ScoreFragment : Fragment() {
    private lateinit var binding: FragmentScoreBinding
    private val viewModel: ScoreViewModel by viewModels()

    private var finalScoreDialog: AlertDialog? = null
    private var interstitialAd: InterstitialAd? = null

    private var ignoreTopThreshold = 0f
    private var dragThreshold = 0f
    private var tapThreshold = 0f
    private var initialY = 0f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding =
            DataBindingUtil
                .inflate<FragmentScoreBinding?>(
                    inflater,
                    R.layout.fragment_score,
                    container,
                    false,
                ).apply {
                    scoreViewModel = viewModel
                    lifecycleOwner = viewLifecycleOwner
                }
        initLayout()

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        finalScoreDialog?.dismiss()
    }

    private fun initLayout() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.textviewHomeTitle) { v, insets ->
            val orientation = requireContext().resources.configuration.orientation
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                val bars = insets.getInsets(WindowInsetsCompat.Type.displayCutout())
                v.updatePadding(
                    left = bars.left,
                    top = bars.top,
                    right = bars.right,
                    bottom = bars.bottom,
                )
            }
            WindowInsetsCompat.CONSUMED
        }
        dragThreshold = ViewConfiguration.get(requireContext()).scaledVerticalScrollFactor
        tapThreshold = ViewConfiguration.get(requireContext()).scaledTouchSlop.toFloat()
        ignoreTopThreshold = getFivePercentOfScreen()
        DebugUtils.reportDebug("Tap: $tapThreshold and Drag: $dragThreshold")
    }

    private fun getFivePercentOfScreen() = resources.displayMetrics.heightPixels.toFloat() * FIVE_PERCENT

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    private fun initListeners() {
        binding.apply {
            viewLeftHalf.setOnTouchListener { _, event ->
                handleTouch(
                    event,
                    onDecrement = { viewModel.decrementTeam1() },
                    onIncrement = { viewModel.addPointTeam1() },
                )
            }

            viewRightHalf.setOnTouchListener { _, event ->
                handleTouch(
                    event,
                    onDecrement = { viewModel.decrementTeam2() },
                    onIncrement = { viewModel.addPointTeam2() },
                )
            }

            fabReload.setOnClickListener {
                if (viewModel.showAds()) showAd()
                viewModel.restartCounters()
            }

            fabSettings.setOnClickListener {
                val bottomSheetDialog = BottomSheetDialog(requireContext())
                val bottomSheetBinding =
                    BottomsheetSettingsBinding.inflate(layoutInflater).apply {
                        val settingList = viewModel.settingList.map { setting -> setting.copy() }
                        val settingsAdapter = SettingsAdapter()
                        val isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
                        val spanCount = if (isLandscape) 2 else 1
                        textviewVersion.text = "App Version: ${BuildConfig.VERSION_NAME}"
                        recyclerSettings.layoutManager = GridLayoutManager(requireContext(), spanCount)
                        recyclerSettings.adapter = settingsAdapter
                        buttonSave.setOnClickListener {
                            viewModel.saveSettings(settingsAdapter.currentList)
                            bottomSheetDialog.dismiss()
                        }

                        settingsAdapter.submitList(settingList)
                    }
                bottomSheetDialog
                    .apply {
                        setContentView(bottomSheetBinding.root)
                        behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }.show()
            }
        }

        viewModel.finishingGame.observe(viewLifecycleOwner) { finishingGame ->
            if (finishingGame) {
                disableCounters()
                showFinalScoreDialog()
            }
        }

        viewModel.showAdvertisement.observe(viewLifecycleOwner) { showAdvertisement ->
            if (showAdvertisement) initAds()
        }
    }

    private fun showFinalScoreDialog() {
        val binding =
            DialogFinalScoreBinding.inflate(layoutInflater).apply {
                scoreViewModel = viewModel
            }
        finalScoreDialog =
            MaterialAlertDialogBuilder(requireContext(), R.style.ThemeOverlay_App_FullWidthActionsDialog)
                .setTitle(R.string.match_score)
                .setCancelable(false)
                .setView(binding.root)
                .setNeutralButton(R.string.new_game) { dialog, _ ->
                    if (viewModel.showAds() && viewModel.isLongGame) showAd()
                    viewModel.restartCounters()
                    enableCounters()
                    viewModel.gameFinished()
                    dialog.dismiss()
                }.show()
    }

    private fun enableCounters() {
        binding.viewLeftHalf.isEnabled = true
        binding.viewRightHalf.isEnabled = true
    }

    private fun disableCounters() {
        binding.viewLeftHalf.isEnabled = false
        binding.viewRightHalf.isEnabled = false
    }

    private fun handleTouch(
        event: MotionEvent,
        onDecrement: () -> Unit,
        onIncrement: () -> Unit,
    ): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                initialY = event.y
                return true
            }

            MotionEvent.ACTION_UP -> {
                val deltaY = event.y - initialY
                if (deltaY > dragThreshold && initialY > ignoreTopThreshold) {
                    onDecrement()
                } else if (abs(deltaY) <= tapThreshold) {
                    onIncrement()
                }
                DebugUtils.reportDebug("Initial: $initialY and final ${event.y} and delta: $deltaY")
                return true
            }

            else -> return false
        }
    }

    private fun initAds() {
        MobileAds.initialize(requireContext())
        loadNewAd()
    }

    private fun loadNewAd() {
        DebugUtils.reportDebug("Loading Advertisement")
        val adRequest = AdRequest.Builder().build()
        val interstitialAdLoadCallback =
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    interstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    this@ScoreFragment.interstitialAd = interstitialAd
                }
            }

        InterstitialAd.load(
            requireContext(),
            BuildConfig.INTERSTITIAL_AD_UNIT_ID,
            adRequest,
            interstitialAdLoadCallback,
        )
    }

    private fun showAd() {
        interstitialAd?.show(requireActivity())
            ?: DebugUtils.reportDebug("Advertisement not loaded")
        loadNewAd()
    }
}
