package com.example.appcar.util

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.appcar.data.model.Response
import com.google.gson.Gson
import java.io.IOException
import java.nio.charset.StandardCharsets

/**
 * Created by KO Huyn on 29/10/2021.
 */

@Throws(IOException::class)
fun loadJSONFromAsset(context: Context, jsonFileName: String): String {
    val manager = context.assets
    val inputStream = manager.open(jsonFileName)
    val size = inputStream.available()
    val buffer = ByteArray(size)
    inputStream.read(buffer)
    inputStream.close()
    return String(buffer, StandardCharsets.UTF_8)
}

fun Fragment.getRepo(): Response? {
    val json = loadJSONFromAsset(requireContext(), "notice_board.json")
    return try {
        Gson().fromJson(json, Response::class.java)
    } catch (e: Exception) {
        null
    }
}