package com.centralelille.sequence1.data

import android.app.LauncherActivity
import android.content.ClipData
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

    private const val POST_API_URL = "http://tomnab.fr/todo-api/users/2/lists"
    private val gson = Gson()

    /**
     * To fetch the access token
     */
    fun getAccessToken(onSuccess: (String) -> Unit) {
        TODO()
    }

    /**
     * Return the items of the list whose ID is given
     *
     * @param ID
     * @param onSuccess
     */
    suspend fun getItemList(pseudo: String, itemLabel: String): List<ItemToDo> =
        withContext(Dispatchers.IO) {
            val json = getCall(pseudo, itemLabel)
            val itemsResponse = gson.fromJson(json, ItemsResponse::class.java)
            itemsResponse.itemsToDo
        }

    /**
     * Note : withContext gives the thread to use
     *
     * @param pseudo
     * @return
     */
    suspend fun getListUser(pseudo: String): List<ListeToDo> = withContext(Dispatchers.IO) {
        val json = getCall(pseudo, null)
        val listesResponse = gson.fromJson(json, ListesResponse::class.java)
        listesResponse.listesToDo
    }

    suspend fun postList(pseudo: String, listLabel: String) = withContext(Dispatchers.IO) {
        val json = postCall(pseudo, listLabel, null)
        val newListe = gson.fromJson(json, ListeToDo::class.java)
        newListe
    }


    suspend fun postItem(pseudo: String, listLabel: String, itemLabel: String) =
        withContext(Dispatchers.IO) {
            val json = postCall(pseudo, listLabel, itemLabel)
            val newItem = gson.fromJson(json, ItemToDo::class.java)
            newItem
        }

    private fun getCall(pseudo: String, itemLabel: String?): String? {
        var urlConnection: HttpURLConnection? = null
        // Buffer used to read the API response
        var reader: BufferedReader? = null

        try {
            // TODO("use pseudo or ID in URL")
            urlConnection = URL(POST_API_URL).openConnection() as HttpURLConnection
            urlConnection.requestMethod = "GET"
            urlConnection.connect()

            reader = urlConnection.inputStream?.bufferedReader()
            return reader?.readText()
        } finally {
            urlConnection?.disconnect()
            reader?.close()
        }
    }

    private fun postCall(ID: String, newListName: String, itemLabel: String?): String? {
        var urlConnection: HttpURLConnection? = null
        var reader: BufferedReader? = null

        try {
            // TODO("change URL")
            urlConnection = URL(POST_API_URL).openConnection() as HttpURLConnection
            urlConnection.requestMethod = "POST"
            urlConnection.connect()

            reader = urlConnection.inputStream?.bufferedReader()
            return reader?.readText()
        } finally {
            urlConnection?.disconnect()
            reader?.close()
        }
    }

}