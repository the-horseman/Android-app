package com.example.second

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AbsListView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.loader.content.AsyncTaskLoader
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.security.AccessControlContext
import java.security.cert.CertPath
import kotlin.properties.Delegates

class FeedEntry {
    var number =0
    var name : String = ""
    var artist : String = ""
    var releasedate : String = ""
    var summary : String = ""
    var imageURL : String = ""
    override fun toString(): String {
        return  """
            Number = $number
            Name = $name
            Artist = $artist
            Release Date = $releasedate
            Image URL = $imageURL
        """.trimIndent()
    }
}
class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate called")
        val downloadData = DownloadData(this, xmlListView )
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=10/xml")
        Log.d(TAG, "onCreate finished")
    }
    companion object {
        private class DownloadData(context: Context, listView: ListView) : AsyncTask<String, Void, String>() {
            private val TAG = "DownloadData"

            var propContext : Context by Delegates.notNull()
            var propListView : ListView by Delegates.notNull()

            init {
                propContext = context
                propListView = listView
            }

            override fun onPostExecute(result: String) {
                super.onPostExecute(result)
//                Log.d(TAG, "onPostExecute : parameter is $result")
                val parseApplication = ParseApplication()
                parseApplication.parse(result)

                val arrayAdapter = ArrayAdapter<FeedEntry>(propContext, R.layout.list_item, parseApplication.applications)
                propListView.adapter = arrayAdapter
            }

            override fun doInBackground(vararg url: String?): String {
                Log.d(TAG, "doInBackground starts with ${url[0]}")
                val rssFeed = downloadXML(url[0])
                if (rssFeed.isEmpty()) {
                    Log.e(TAG, "DoInBackground : Error")
                }
                return rssFeed
            }
            private fun downloadXML(urlPath: String?): String {
                return URL(urlPath).readText()

            }
        }
    }
}
