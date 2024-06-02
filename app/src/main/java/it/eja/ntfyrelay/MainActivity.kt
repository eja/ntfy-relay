// Copyright (C) 2024 by Ubaldo Porcheddu <ubaldo@eja.it>

package it.eja.ntfyrelay

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.core.app.NotificationManagerCompat
import android.widget.Switch
import android.widget.Toast
import android.view.inputmethod.EditorInfo


class MainActivity : ComponentActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val enabled = isNotificationListenerEnabled(this)
        if (!enabled) {
            val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
            startActivity(intent)
        }

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        val url = sharedPreferences.getString("URL", "")
        val urlInput = findViewById<EditText>(R.id.url_input)
        urlInput.setText(url)
        urlInput?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                val editor = sharedPreferences.edit()
                editor.putString("URL", urlInput?.text.toString().trim())
                editor.apply()
                Toast.makeText(this, "URL Updated", Toast.LENGTH_SHORT).show()
                true
            } else {
                false
            }
        }


        val active = sharedPreferences.getBoolean("ACTIVE", false)
        val switchButton=findViewById<Switch>(R.id.switch1)
        switchButton.isChecked=active
        switchButton.setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPreferences.edit()
            editor.putBoolean("ACTIVE", isChecked)
            editor.apply()
        }
    }

    private fun isNotificationListenerEnabled(context: Context): Boolean {
        val packageName = context.packageName
        val enabledListeners = NotificationManagerCompat.getEnabledListenerPackages(context)
        return enabledListeners.contains(packageName)
    }
}