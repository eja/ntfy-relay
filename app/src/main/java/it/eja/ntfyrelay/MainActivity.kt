// Copyright (C) 2024 by Ubaldo Porcheddu <ubaldo@eja.it>

package it.eja.ntfyrelay

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.core.app.NotificationManagerCompat
import android.widget.Button
import android.widget.Toast;

class MainActivity : ComponentActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout)

        val enabled = isNotificationListenerEnabled(this)
        if (!enabled) {
            val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
            startActivity(intent)
        }

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        val url = sharedPreferences.getString("URL", "")
        val urlInput = findViewById<EditText>(R.id.url_input)
        urlInput.setText(url)

        val submitButton = findViewById<Button>(R.id.submit_button)
        submitButton.setOnClickListener {
            val urlString = urlInput.text.toString()
            val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("URL", urlString)
            editor.apply()
            Toast.makeText(this, "Value saved successfully", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isNotificationListenerEnabled(context: Context): Boolean {
        val packageName = context.packageName
        val enabledListeners = NotificationManagerCompat.getEnabledListenerPackages(context)
        return enabledListeners.contains(packageName)
    }
}