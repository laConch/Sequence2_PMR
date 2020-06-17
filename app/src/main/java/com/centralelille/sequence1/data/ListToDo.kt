package com.centralelille.sequence1.data

/**
 * Represents a list and is created according to the Json.
 *
 * @property id
 * @property label
 */
data class ListeToDo(var id: String, var label: String)

/**
 * Represents a list of lists and is created according to the Json.
 *
 * @property listesToDo
 */
data class ListesResponse(val listesToDo: MutableList<ListeToDo>)