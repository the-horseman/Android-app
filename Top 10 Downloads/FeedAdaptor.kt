package com.example.second

import android.content.Context
import android.content.Intent
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class FeedAdaptor(context: Context,private val resource: Int, private val applications: List<FeedEntry>) : ArrayAdapter<FeedEntry>(context, resource) {
    private val TAG = "FeedAdaptor"
    private val inflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return applications.size
        Log.d(TAG ,"getCount() called")

    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = inflater.inflate(resource, parent ,  false)
        val  tvName: TextView = view.findViewById(R.id.tvName)
        val  tvArtist: TextView = view.findViewById(R.id.tvArtist)
        val  tvNumber: TextView = view.findViewById(R.id.tvNumber)
        var c=0

        val currentApp = applications[position]

        tvNumber.text = currentApp.number.toString()
        tvName.text = currentApp.name
        tvArtist.text = currentApp.artist

        return view
    }

}
