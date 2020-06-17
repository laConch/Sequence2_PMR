package com.centralelille.sequence1.data

/**
 * Represents an item and is created according to the Json
 *
 * @property id
 * @property label
 * @property url
 * @property checked
 */
data class ItemToDo(var id: String, var label: String, var url: String, var checked: Boolean)

/**
 * Represents a list of items and is created according to the Json.
 *
 * @property itemsToDo
 */
data class ItemsResponse(val itemsToDo: MutableList<ItemToDo>)