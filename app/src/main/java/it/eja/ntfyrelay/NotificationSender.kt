// Copyright (C) 2024 by Ubaldo Porcheddu <ubaldo@eja.it>

package it.eja.ntfyrelay

import java.net.HttpURLConnection
import java.net.URL

class NotificationSender {
    fun sendNotification(urlString: String, title: String, message: String) {
        if (urlString != "" && message != "") {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection

            connection.requestMethod = "POST"
            connection.setRequestProperty("X-Title", title)
            connection.doOutput = true

            val outputStream = connection.outputStream
            outputStream.write(message.toByteArray())
            outputStream.flush()
            outputStream.close()

            val responseCode = connection.responseCode
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw RuntimeException("Unexpected response code: $responseCode")
            }
        }
    }
}