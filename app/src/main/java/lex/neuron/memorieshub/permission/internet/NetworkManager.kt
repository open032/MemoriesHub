package lex.neuron.memorieshub.permission.internet

import android.content.Context
import android.net.ConnectivityManager
import java.io.IOException
import java.net.InetAddress

class NetworkManager {
    private var bol = false

    @get:Throws(InterruptedException::class, IOException::class)
    val isConnected: Boolean
        get() {
            val command = "ping -c 1 google.com"
            return Runtime.getRuntime().exec(command).waitFor() == 0
        }

    //You can replace it with your name
    val isInternetAvailable: Boolean
        get() = try {
            val ipAddr: InetAddress = InetAddress.getByName("http://www.google.com/")
            //You can replace it with your name
            !ipAddr.equals("")
        } catch (e: Exception) {
            false
        }

    companion object {
        // wifi check
        fun isNetworkAvailable(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var networkInfo = cm.activeNetworkInfo
            return if (networkInfo != null && networkInfo.isConnected) {
                true
            } else {
                false
            }
        }
    }
}