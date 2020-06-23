package com.centralelille.sequence1.data

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * To manipulate the current user in the whole application
 * (We do not need a DataProvider instance to call its methods because it is a singleton)
 *
 * Note : a method with the key word "suspend", suspend the method that call it.
 * It is useful in order to avoid callbacks.
 * And this method does not block the thread !
 */
object DataProvider {
    
    private const val POST_API_URL = "http://tomnab.fr/todo-api/"
    private var HASH_CODE = "3f42b18b7f71498b166d1662848a5bec"
    private val gson = Gson()

    /**
     * To fetch the access token
     */
    suspend fun setAccessToken(pseudo: String, password: String) {
        Log.d("DataProvider", "setAccessToken")
        val json = getHashCall(pseudo, password)
        HASH_CODE = gson.fromJson(json, AccessToken::class.java).hash
    }

    /**
     * Return the items of the list whose ID is given
     *
     * @param pseudo
     * @param itemLabel
     */
    suspend fun getItemList(pseudo: String, itemLabel: String): List<ItemToDo> =
        withContext(Dispatchers.IO) {
            Log.d("DataProvider", "getListUser")
            val json = getCall("lists/469/items")
            val itemsResponse = gson.fromJson(json, ItemsResponse::class.java)
            itemsResponse.itemsToDo
        }

    /**
     * Return the lists of the user whose pseudo is given
     *
     * Note : withContext gives the thread to use
     * @param pseudo
     */
    suspend fun getListUser(pseudo: String): List<ListToDo> = withContext(Dispatchers.IO) {
        Log.d("DataProvider", "getListUser")
        val json = getCall("users/2/lists")
        val listsResponse = gson.fromJson(json, ListResponse::class.java)
        listsResponse.lists
    }

    /**
     * Create a new list
     *
     * @param pseudo
     * @param listLabel
     */
    suspend fun postList(pseudo: String, listLabel: String) = withContext(Dispatchers.IO) {
        Log.d("DataProvider", "getListUser")
        val json = postCall(pseudo, listLabel, null)
        val newListe = gson.fromJson(json, ListToDo::class.java)
        newListe
    }

    suspend fun postItem(pseudo: String, listLabel: String, itemLabel: String) =
        withContext(Dispatchers.IO) {
            val json = postCall(pseudo, listLabel, itemLabel)
            val newItem = gson.fromJson(json, ItemToDo::class.java)
            newItem
        }

    /**
     * Method used to call the API for hash codes
     */
    private fun getHashCall(pseudo: String, password: String): String? {
        Log.d("DataProvider", "getHashCode")
        var urlConnection: HttpURLConnection? = null
        // Buffer used to read the API response
        var reader: BufferedReader? = null

        try {
            urlConnection =
                URL(POST_API_URL + "authenticate?user=$pseudo&password=$password").openConnection() as HttpURLConnection
            urlConnection.requestMethod = "POST"
            urlConnection.connect()

            reader = urlConnection.inputStream?.bufferedReader()
            return reader?.readText()
        } finally {
            urlConnection?.disconnect()
            reader?.close()
        }
    }

    /**
     * Method used to call the API for every GET request
     */
    private fun getCall(endOfUrl: String): String? {
        Log.d("DataProvider", "getCall")
        var urlConnection: HttpURLConnection? = null
        // Buffer used to read the API response
        var reader: BufferedReader? = null

        try {
            urlConnection = URL(POST_API_URL + endOfUrl).openConnection() as HttpURLConnection
            urlConnection.requestMethod = "GET"
            // Headers
            urlConnection.setRequestProperty("hash", HASH_CODE)
            urlConnection.connect()

            reader = urlConnection.inputStream?.bufferedReader()
            return reader?.readText()
        } finally {
            urlConnection?.disconnect()
            reader?.close()
        }
    }

    /**
     * Method used to call the API for every POST request
     */
    private fun postCall(endOfUrl: String): String? {
        var urlConnection: HttpURLConnection? = null
        var reader: BufferedReader? = null

        try {
            // TODO("change URL")
            urlConnection = URL(POST_API_URL + endOfUrl).openConnection() as HttpURLConnection
            urlConnection.requestMethod = "POST"
            urlConnection.setRequestProperty("hash", HASH_CODE)
            urlConnection.connect()

            reader = urlConnection.inputStream?.bufferedReader()
            return reader?.readText()
        } finally {
            urlConnection?.disconnect()
            reader?.close()
        }
    }

}