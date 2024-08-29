package com.example.scoreboardbrlk.ui.score

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.databinding.DataBindingUtil
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.scoreboardbrlk.R
import com.example.scoreboardbrlk.databinding.FragmentScoreBinding
import com.example.scoreboardbrlk.domain.Setting
import com.google.android.material.bottomsheet.BottomSheetDialog

class ScoreFragment : Fragment(),  SaveClickInterface {
    private lateinit var _binding: FragmentScoreBinding
    private lateinit var _viewModel: ScoreViewModel
    private val Context._dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private val dragThreshold = 200f
    private var initialY=0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val viewModelFactory = ScoreViewModelFactory(AppDatastoreRepository(requireContext()))
        _viewModel = ViewModelProvider(this, viewModelFactory)[ScoreViewModel::class.java]
        _binding = DataBindingUtil.inflate<FragmentScoreBinding?>(inflater, R.layout.fragment_score, container, false).apply {
            scoreViewModel = _viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        ViewCompat.setOnApplyWindowInsetsListener(_binding.textviewHomeTitle){ v, insets ->
            val orientation = requireContext().resources.configuration.orientation
            if(orientation == Configuration.ORIENTATION_PORTRAIT){
                val bars = insets.getInsets(WindowInsetsCompat.Type.displayCutout())
                v.updatePadding(left=bars.left, top=bars.top, right = bars.right, bottom = bars.bottom)
            }
            WindowInsetsCompat.CONSUMED
        }

//        _viewModel.accessReleased.observe(viewLifecycleOwner){ accessReleased ->
//            val isPremiumKey = intPreferencesKey("is_premium")
//            Toast.makeText(requireContext(), "Acesso premium desbloqueado", Toast.LENGTH_SHORT).show()
//        }

        _binding.apply {
            fabSettings.setOnClickListener {
                val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetTheme)
                val sheetView = layoutInflater.inflate(R.layout.custom_settings_layout, it.findViewById(R.id.bottomsheet_settings_layout))
                sheetView.apply {
                    val settingsAdapter = SettingsAdapter(_viewModel.settingList.toList())
                    findViewById<RecyclerView>(R.id.recycler_settings).adapter = settingsAdapter
                    findViewById<Button>(R.id.button_save).setOnClickListener {
                        _viewModel.saveSettings(settingsAdapter.getList())
                        bottomSheetDialog.dismiss()
                    }
                }
                bottomSheetDialog.setOnDismissListener {
                    Toast.makeText(requireContext(), "dismissed", Toast.LENGTH_SHORT).show()
                }
                bottomSheetDialog.setContentView(sheetView)
                bottomSheetDialog.show()
            }

            leftHalf.setOnTouchListener { v, event ->
                when(event.action){
                    MotionEvent.ACTION_DOWN -> {
                        initialY = event.y
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        val deltaY = event.y - initialY
                        Log.d("test", "Initial: $initialY and final ${event.y} and delta: $deltaY")
                        if (initialY>=100f && dragThreshold<deltaY){
                            _viewModel.decrementTeam1()
                        } else if (initialY==event.y){
                            _viewModel.addPointTeam1()
                        }
                        initialY=0f
                        true
                    }
                    else -> false
                }
            }
            rightHalf.setOnTouchListener { _, event ->
                when(event.action){
                    MotionEvent.ACTION_DOWN -> {
                        initialY = event.y
                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        val deltaY = event.y - initialY
                        Log.d("test", "Initial: $initialY and final ${event.y} and delta: $deltaY")
                        if (initialY>=100f && dragThreshold<deltaY){
                            _viewModel.decrementTeam2()
                        } else if (initialY==event.y){
                            _viewModel.addPointTeam2()
                        }
                        initialY=0f
                        true
                    }
                    else -> false
                }
            }
        }
        return _binding.root
    }

    override fun onSaveClick(settingList: List<Setting>) {
        _viewModel.saveSettings(settingList)
    }
}