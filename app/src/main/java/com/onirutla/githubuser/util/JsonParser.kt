package com.onirutla.githubuser.util

import android.content.Context
import com.onirutla.githubuser.data.Response
import com.onirutla.githubuser.data.UserItem
import com.squareup.moshi.Moshi
import java.io.IOException

object JsonParser {
    fun readJson(context: Context, fileName: String): List<UserItem>? {
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(Response::class.java)
        val userItems: List<UserItem>?
        try {
            val json = context.assets.open(fileName).bufferedReader().use { it.readText() }
            userItems = adapter.fromJson(json)?.users
        } catch (e: IOException) {
            return null
        }
        return userItems
    }
}
