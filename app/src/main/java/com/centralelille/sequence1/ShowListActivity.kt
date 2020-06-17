package com.centralelille.sequence1

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.centralelille.sequence1.adapters.ItemAdapter
import com.centralelille.sequence1.data.DataProvider
import com.centralelille.sequence1.data.ItemToDo
import kotlinx.coroutines.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ShowListActivity : AppCompatActivity(), View.OnClickListener, ItemAdapter.OnItemListener {

    private val adapter = newAdapter()

    private lateinit var refOkBtn: Button
    private lateinit var refTxtNewItem: EditText
    private lateinit var refRecycler: RecyclerView

    private lateinit var pseudoRecu: String
    private lateinit var listeRecue: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_list)

        val bundleData: Bundle = this.intent.extras

        pseudoRecu = bundleData.getString("pseudo")
        listeRecue = bundleData.getString("titre")

        refOkBtn = findViewById(R.id.buttonNewItem)
        refRecycler = findViewById(R.id.listOfItem)
        refTxtNewItem = findViewById(R.id.editTextItem)

        // ClickListener associated to the button to create and save the lists
        refOkBtn.setOnClickListener(this)

        loadItemsList(refRecycler)
    }

    /* By default included in the Main thread */
    private val activityScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main
    )

    private fun loadItemsList(list: RecyclerView) {
        activityScope.launch(Dispatchers.Main) {
            val listeItemToDo = DataProvider.getItemList(pseudoRecu, listeRecue)
            adapter.showData(listeItemToDo)
        }
    }


    override fun onClick(v: View?) {
        val newLabel = refTxtNewItem.text.toString()

        activityScope.launch(Dispatchers.Main) {
            val newItem = DataProvider.postItem(pseudoRecu, listeRecue, newLabel)
            adapter.addItem(newItem)
        }
    }

    private fun newAdapter(): ItemAdapter {
        return ItemAdapter(onItemListener = this)
    }

    /**
     * Quand un item est cliqué l'utilisateur peut l'éditer
     *
     * @param item
     */
    override fun onItemClicked(item: ItemToDo) {
        Toast.makeText(this, item.label, Toast.LENGTH_LONG).show()
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        // Destroy all the Coroutines that have been launched
        activityScope.cancel()
        super.onDestroy()
    }
}