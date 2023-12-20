package com.dityapra.todoapp.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.dityapra.todoapp.R
import com.dityapra.todoapp.notification.NotificationWorker
import com.dityapra.todoapp.utils.NOTIFICATION_CHANNEL_ID
import java.util.concurrent.TimeUnit

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            val prefNotification =
                findPreference<SwitchPreference>(getString(R.string.pref_key_notify))
            prefNotification?.setOnPreferenceChangeListener { _, newValue ->
                val channelName = getString(R.string.notify_channel_name)
                val wManager = WorkManager.getInstance(requireContext())
                if (newValue as Boolean) {
                    val data = Data.Builder()
                        .putString(NOTIFICATION_CHANNEL_ID, channelName)
                        .build()
                    val periodicWorkRequest = PeriodicWorkRequest.Builder(
                        NotificationWorker::class.java,
                        1, TimeUnit.DAYS
                    )
                        .setInputData(data)
                        .build()
                    wManager.enqueueUniquePeriodicWork(
                        "daily_notification",
                        ExistingPeriodicWorkPolicy.UPDATE,
                        periodicWorkRequest
                    )
                } else {
                    wManager.cancelUniqueWork("daily_notification")
                }
                true
            }
        }
        private fun updateTheme(mode: Int): Boolean {
            AppCompatDelegate.setDefaultNightMode(mode)
            requireActivity().recreate()
            return true
        }
    }
}