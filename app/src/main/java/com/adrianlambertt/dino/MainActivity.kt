package com.adrianlambertt.dino

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.adrianlambertt.dino.hydration.addSip
import com.adrianlambertt.dino.hydration.getHydrationState
import com.adrianlambertt.dino.hydration.hydrationDataStore
import com.adrianlambertt.dino.hydration.minusSip
import com.adrianlambertt.dino.views.WaterBottleView
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Log.d("Permissions", "Notification permission granted")
            } else {
                Log.d("Permissions", "Notification permission denied")
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {

        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
            val waterBottle = findViewById<WaterBottleView>(R.id.waterBottle)
            val hydrationPercent = findViewById<TextView>(R.id.hydrationPercent)
            val hydrationCount = findViewById<TextView>(R.id.hydrationCount)

            hydrationDataStore.data.collect { prefs ->
                val hydration = applicationContext.getHydrationState()
                val currentSips = hydration.currentSips
                val goalSips = hydration.goalSips
                val percent = currentSips.toFloat() / goalSips
                val percentText = (percent * 100).toInt()
                waterBottle.setProgress(percent)
                hydrationPercent.text = "$percentText%"
                hydrationCount.text = "$currentSips out of $goalSips sips"
            }
        }

        configurePermissions()
        resetDinoWisdom()
        configureSettingsButton()
        configureHydrationButtons()
        configureDinoWisdomRefresh()
    }

    private fun configurePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun configureSettingsButton() {
        val button = findViewById<ImageButton>(R.id.settingsButton)
        button.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun configureHydrationButtons() {
        val buttonAdd = findViewById<Button>(R.id.hydrationAddSip)
        buttonAdd.setOnClickListener {
            SoundPlayer.playPlusDropSound(this)
            lifecycleScope.launch {
                applicationContext.addSip()
            }
        }

        val buttonMinus = findViewById<Button>(R.id.hydrationMinusSip)
        buttonMinus.setOnClickListener {
            SoundPlayer.playMinusDropSound(this)
            lifecycleScope.launch {
                applicationContext.minusSip()
            }
        }
    }

    private fun configureDinoWisdomRefresh() {
        val button = findViewById<Button>(R.id.dinoWisdomRefresh)
        button.setOnClickListener {
            resetDinoWisdom()
        }
    }

    private fun resetDinoWisdom() {
        val fadeText01 = findViewById<TextView>(R.id.fadeText01)
        fadeText01.text = getTextOptions()
        fadeInAnimation(3000L, 500L, fadeText01)
    }

    private fun fadeInAnimation(duration: Long, startOffset: Long, view: TextView) {
        val fadeInAnimation = AlphaAnimation(0f, 1f).apply {
            this.duration = duration
            this.startOffset = startOffset
            fillAfter = true
        }
        view.startAnimation(fadeInAnimation)
    }

    private fun getTextOptions(): String {
        val wisdom = listOf(
            "Dino thinks you look good today!",
            "I eat my veggies, that's how I'm so strong!",
            "Hydration is as important as sleeping",
            "Make sure to go outside, vitamin D does wonders",
            "Take time to yourself, life flies by",

            "Even T-Rex rest — so should you",
            "Small steps still move you forward",
            "Stretch your legs, future fossil",
            "Kindness makes you unstoppable",
            "Progress beats perfection",
            "Drink water, roar louder",
            "You survived extinction — this will be easy",
            "A calm mind makes a mighty dino",
            "Go at your own prehistoric pace",
            "The best days start with good habits"
        )

        return wisdom.random()
    }
}