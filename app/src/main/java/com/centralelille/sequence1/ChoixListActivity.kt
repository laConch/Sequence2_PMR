package com.centralelille.sequence1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.centralelille.sequence1.adapters.ListAdapter
import com.centralelille.sequence1.data.DataProvider
import com.centralelille.sequence1.data.ListToDo
import kotlinx.coroutines.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ChoixListActivity : AppCompatActivity(), View.OnClickListener, ListAdapter.OnListListener {

    private val adapter = newAdapter()

    private lateinit var refOkBtn: Button
    private lateinit var refTxtNewList: EditText
    private lateinit var refRecycler: RecyclerView

    private lateinit var pseudoRecu: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choix_list)

        val bundlePseudo: Bundle = this.intent.extras

        pseudoRecu = bundlePseudo.getString("pseudo")

        refOkBtn = findViewById(R.id.buttonNewList)
        refRecycler = findViewById(R.id.listOfList)
        refTxtNewList = findViewById(R.id.editText)

        refRecycler.adapter = adapter
        refRecycler.layoutManager = LinearLayoutManager(this)

        // Click listener associated to the button to create and save the lists
        refOkBtn.setOnClickListener(this)

        loadListsUser(refRecycler)
    }

    /* By default included in the Main thread */
    private val activityScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main
    )

    private fun loadListsUser(list: RecyclerView) {
        activityScope.launch(Dispatchers.Main) {
            val listeListeToDo = DataProvider.getListUser(pseudoRecu)
            adapter.showData(listeListeToDo)
        }
    }

    override fun onClick(v: View?) {
        val newListTitle = refTxtNewList.text.toString()

        activityScope.launch(Dispatchers.Main) {
            val newList = DataProvider.postList(pseudoRecu, newListTitle)
            adapter.addList(newList)
        }
    }

    private fun newAdapter(): ListAdapter {
        return ListAdapter(onListListener = this)
    }

    /**
     * Quand une liste est cliquée on change d'activité pout aller sur l'activité ShowListActivity
     *
     * @param list
     */
    override fun onListClicked(list: ListToDo) {
        Log.d("ChoixListActivity", "onListClicked $list")
        Toast.makeText(this, list.label, Toast.LENGTH_LONG).show()

        val listLabel = list.label
        val bundleData = Bundle()
        bundleData.putString("pseudo", pseudoRecu)
        bundleData.putString("titre", listLabel)

        val afficherShowListActivity = Intent(this, ShowListActivity::class.java)
        afficherShowListActivity.putExtras(bundleData)
        startActivity(afficherShowListActivity)
    }

    override fun onDestroy() {
        // Destroy all the Coroutines that have been launched
        activityScope.cancel()
        super.onDestroy()
    }
}
