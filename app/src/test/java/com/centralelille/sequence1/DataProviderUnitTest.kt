package com.centralelille.sequence1

import com.centralelille.sequence1.adapters.ListAdapter
import com.centralelille.sequence1.data.ListResponse
import com.centralelille.sequence1.data.ListToDo
import com.google.gson.Gson
import org.junit.Test

import org.junit.Assert.*

class DataProviderUnitTest {

    data class TestModel(
        val id: Int,
        val description: String
    )

    var gson = Gson()

    @Test
    fun example_isCorrect() {
        var jsonString = gson.toJson(TestModel(1, "Test"))
        assertEquals("""{"id":1,"description":"Test"}""", jsonString)
    }

    @Test
    fun serialization_isCorrect() {
        var jsonString = gson.toJson(
            ListResponse(
                version = "1", success = true, status = "200", lists = listOf(
                    ListToDo("1", "l1"), ListToDo("2", "l2")
                )
            )
        )
        assertEquals(
            """{"version":1,"success":true,"status":200,"lists":[{"id":"1","label":"l1"},{"id":"2","label":"l2"}]}""",
            jsonString
        )
    }
    @Test
    fun deserialization_isCorrect() {
        var listResponse = gson.fromJson("""{"version":1,"success":true,"status":200,"lists":[{"id":"1","label":"l1"},{"id":"2","label":"l2"}]}""", ListResponse::class.java)
        assertEquals(ListResponse(
            version = "1",
            success = true,
            status = "200",
            lists = listOf(
                ListToDo("1", "l1"), ListToDo("2", "l2")
            )),
            listResponse
        )
    }


}
