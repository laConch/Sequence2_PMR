package com.centralelille.sequence1.data

/**
 * Represents a list and is created according to the Json.
 *
 * @property id
 * @property label
 */
data class ListToDo(var id: String, var label: String)

/**
 * Represents a list of lists and is created according to the Json.
 *
 * @property lists : List of ListToDO
 */
data class ListResponse(
    val version: String,
    var success: Boolean,
    var status: String,
    var lists: List<ListToDo>
)