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
    private lateinit var _binding: FragmentScoreBinding
    private val _viewModel: ScoreViewModel by viewModels()

    private var _finalScoreDialog: AlertDialog? = null
    private var _interstitialAd: InterstitialAd? = null

    private var _ignoreTopThreshold = 0f
    private var _dragThreshold = 0f
    private var _tapThreshold = 0f
    private var _initialY = 0f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate<FragmentScoreBinding?>(
            inflater,
            R.layout.fragment_score,
            container,
            false
        ).apply {
            scoreViewModel = _viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        initLayout()

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        _finalScoreDialog?.dismiss()
    }

    private fun initLayout() {
        ViewCompat.setOnApplyWindowInsetsListener(_binding.textviewHomeTitle) { v, insets ->
            val orientation = requireContext().resources.configuration.orientation
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                val bars = insets.getInsets(WindowInsetsCompat.Type.displayCutout())
                v.updatePadding(
                    left = bars.left,
                    top = bars.top,
                    right = bars.right,
                    bottom = bars.bottom
                )
            }
            WindowInsetsCompat.CONSUMED
        }
        _dragThreshold = ViewConfiguration.get(requireContext()).scaledVerticalScrollFactor
        _tapThreshold = ViewConfiguration.get(requireContext()).scaledTouchSlop.toFloat()
        _ignoreTopThreshold = getFivePercentOfScreen()
        DebugUtils.reportDebug("Tap: $_tapThreshold and Drag: $_dragThreshold")
    }

    private fun getFivePercentOfScreen() = resources.displayMetrics.heightPixels.toFloat()*0.05f

    @SuppressLint("ClickableViewAccessibility")
    private fun initListeners() {
        _binding.apply {
            viewLeftHalf.setOnTouchListener { _, event ->
                handleTouch(event,
                    onDecrement = { _viewModel.decrementTeam1() },
                    onIncrement = { _viewModel.addPointTeam1() }
                )
            }

            viewRightHalf.setOnTouchListener { _, event ->
                handleTouch(event,
                    onDecrement = { _viewModel.decrementTeam2() },
                    onIncrement = { _viewModel.addPointTeam2() }
                )
            }

            fabReload.setOnClickListener {
                if (_viewModel.showAds()) showAdvertisement()
                _viewModel.restartCounters()
            }

            fabSettings.setOnClickListener {
                val bottomSheetDialog = BottomSheetDialog(requireContext())
                val bottomSheetBinding = BottomsheetSettingsBinding.inflate(layoutInflater).apply {
                    val settingList = _viewModel.settingList.map { setting -> setting.copy() }
                    val settingsAdapter = SettingsAdapter()
                    val isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
                    val spanCount = if (isLandscape) 2 else 1
                    recyclerSettings.layoutManager = GridLayoutManager(requireContext(), spanCount)
                    recyclerSettings.adapter = settingsAdapter
                    buttonSave.setOnClickListener {
                        _viewModel.saveSettings(settingsAdapter.currentList)
                        bottomSheetDialog.dismiss()
                    }

                    settingsAdapter.submitList(settingList)
                }
                bottomSheetDialog.apply {
                    setContentView(bottomSheetBinding.root)
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                }.show()
            }
        }

        _viewModel.finishingGame.observe(viewLifecycleOwner) { finishingGame ->
            if (finishingGame) {
                disableCounters()
                showFinalScoreDialog()
            }
        }

        _viewModel.showAdvertisement.observe(viewLifecycleOwner) { showAdvertisement ->
            if (showAdvertisement) initAds()
        }
    }

    private fun disableCounters() {
        _binding.apply {
            viewLeftHalf.isEnabled = false
            viewRightHalf.isEnabled = false
        }
    }

    private fun showFinalScoreDialog() {
        val binding = DialogFinalScoreBinding.inflate(layoutInflater).apply {
            scoreViewModel = _viewModel
        }
        _finalScoreDialog = MaterialAlertDialogBuilder(requireContext(), R.style.ThemeOverlay_App_FullWidthActionsDialog)
            .setTitle(R.string.match_score)
            .setCancelable(false)
            .setView(binding.root)
            .setNeutralButton(R.string.new_game){ dialog, _ ->
                if (_viewModel.showAds() && _viewModel.isLongGame) showAdvertisement()
                _viewModel.restartCounters()
                enableCounters()
                _viewModel.gameFinished()
                dialog.dismiss()
            }.show()
    }

    private fun enableCounters() {
        _binding.apply {
            viewLeftHalf.isEnabled = true
            viewRightHalf.isEnabled = true
        }
    }

    private fun handleTouch(
        event: MotionEvent,
        onDecrement: () -> Unit,
        onIncrement: () -> Unit
    ): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                _initialY = event.y
                return true
            }

            MotionEvent.ACTION_UP -> {
                val deltaY = event.y - _initialY
                if (deltaY > _dragThreshold && _initialY > _ignoreTopThreshold) {
                    onDecrement()
                } else if(abs(deltaY) <= _tapThreshold){
                    onIncrement()
                }
                DebugUtils.reportDebug("Initial: $_initialY and final ${event.y} and delta: $deltaY")
                return true
            }

            else -> return false
        }
    }

    private fun initAds() {
        MobileAds.initialize(requireContext())
        loadAdvertisement()
    }

    private fun loadAdvertisement() {
        DebugUtils.reportDebug("Loading Advertisement")
        val adRequest = AdRequest.Builder().build()
        val interstitialAdLoadCallback = object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                _interstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                _interstitialAd = interstitialAd
            }
        }

        InterstitialAd.load(
            requireContext(),
            BuildConfig.INTERSTITIAL_AD_UNIT_ID,
            adRequest,
            interstitialAdLoadCallback
        )
    }

    private fun showAdvertisement() {
        _interstitialAd?.show(requireActivity())
            ?: DebugUtils.reportDebug("Advertisement not loaded")
        loadAdvertisement()
    }
}