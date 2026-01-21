package com.adrianlambertt.dino

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.adrianlambertt.dino.hydration.setGoalSips

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.settings_activity)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }

        val headerLayout = findViewById<View>(R.id.headerLayout)
        ViewCompat.setOnApplyWindowInsetsListener(headerLayout) { view, insets ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            view.setPadding(
                view.paddingLeft,
                statusBarHeight + dpToPx(16),
                view.paddingRight,
                view.paddingBottom
            )
            insets
        }

        configureSettingsBackButton()
    }

    fun configureSettingsBackButton() {
        val button = findViewById<ImageButton>(R.id.settingsBackButton)
        button.setOnClickListener{
            finish()
        }
    }

    fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)


            val goalPref = findPreference<EditTextPreference>("goal_sips")!!

            goalPref.setOnPreferenceChangeListener { _, newValue ->
                val goal = newValue.toString().toIntOrNull() ?: return@setOnPreferenceChangeListener false

                viewLifecycleOwner.lifecycleScope.launch {
                    requireContext().setGoalSips(goal)
                }
                true
            }
        }
    }
}