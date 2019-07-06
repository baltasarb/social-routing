package ps.g49.socialroutingclient.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class ImageDownloader : AsyncTask<URL, Unit, List<Bitmap>>() {

    override fun doInBackground(vararg params: URL): List<Bitmap> {
        if (params.isEmpty())
            return listOf()

        var connection: HttpURLConnection? = null
        val bitmaps: MutableList<Bitmap> = mutableListOf()

        params.forEach {
            try {
                // Initialize a new http url connection
                connection = it.openConnection() as HttpURLConnection
                // Connect the http url connection
                connection!!.connect()
                // Get the input stream from http url connection
                val inputStream = connection!!.inputStream
                // Initialize a new BufferedInputStream from InputStream
                val bitmap = BitmapFactory.decodeStream(inputStream)
                // Add the bitmap to list
                bitmaps.add(bitmap)
            } catch (io: IOException) {
                io.printStackTrace()
            } finally {
                // Disconnect the http url connection
                connection!!.disconnect()
            }
        }
        // Return bitmap list
        return bitmaps
    }

}