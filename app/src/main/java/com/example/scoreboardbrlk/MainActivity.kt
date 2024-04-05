package com.example.scoreboardbrlk

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private var counterTeam1 = 0
    private var counterTeam2 = 0
    private lateinit var textViewCounterTeam1: TextView
    private lateinit var textViewCounterTeam2: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.light_blue)

        findViewById<FloatingActionButton>(R.id.fab_reload).setOnClickListener {
            restartCounters()
        }
        textViewCounterTeam1 = findViewById(R.id.textview_counter_team1)
        textViewCounterTeam2 = findViewById(R.id.textview_counter_team2)
        findViewById<LinearLayout>(R.id.team1).setOnClickListener {
            counterTeam1+=1
            textViewCounterTeam1.text = counterTeam1.toString()
        }
        findViewById<LinearLayout>(R.id.team2).setOnClickListener {
            counterTeam2+=1
            textViewCounterTeam2.text = counterTeam2.toString()
            Log.i("Hello World", "Hello World")
        }
    }

    private fun restartCounters() {
        counterTeam1=0
        counterTeam2=0
        textViewCounterTeam1.text="0"
        textViewCounterTeam2.text="0"
    }
}
