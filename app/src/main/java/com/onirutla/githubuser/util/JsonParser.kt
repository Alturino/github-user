package com.onirutla.githubuser.util

import android.content.Context
import com.onirutla.githubuser.data.Response
import com.squareup.moshi.Moshi
import java.io.IOException

object JsonParser {
    fun readJson(context: Context, fileName: String): Response? {
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(Response::class.java)
        val response: Response?
        try {
            val json = context.assets.open(fileName).bufferedReader().use { it.readText() }
            response = adapter.fromJson(json)
        } catch (e: IOException) {
            return null
        }
        return response
    }
}
