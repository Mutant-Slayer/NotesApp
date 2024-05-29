package com.example.notesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var database: NotesDatabase
    private lateinit var notesList: ArrayList<Notes>
    private lateinit var adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        adapter = MyAdapter({ onNoteClick(it) }) {
            onNoteDelete(it)
        }

        database = NotesDatabase.getDatabase(this)

        val addButton = findViewById<FloatingActionButton>(R.id.add_fab)
        addButton.setOnClickListener(this)

        CoroutineScope(Dispatchers.IO).launch {
            getData()
            withContext(Dispatchers.Main) {
                Log.d("notes", notesList.toString())
                val recyclerView = findViewById<RecyclerView>(R.id.recycler)
                recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                recyclerView.adapter = adapter
                adapter.notes = notesList
            }
        }
    }

    override fun onClick(view: View?) {

        when (view?.id) {
            R.id.add_fab -> {
                val iNext = Intent(this, NotesDetailsActivity::class.java)
                startActivityForResult(iNext, 1)
            }
        }
    }

    private suspend fun getData() {
        database.notesDao().getAllNotes().apply {
            Log.d("Cheezy", this.toString())
            notesList = ArrayList(this)
        }
    }

    private fun onNoteClick(id: Int) {
        val iNext = Intent(this, NotesDetailsActivity::class.java)
        iNext.putExtra("notes", notesList[id])
        startActivity(iNext)
    }

    private fun onNoteDelete(id: Int) {
        GlobalScope.launch {
            database.notesDao().deleteNotes(notesList[id])
            getData()
            withContext(Dispatchers.Main) {
                adapter.notes = notesList
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            GlobalScope.launch {
                getData()
                withContext(Dispatchers.Main) {
                    adapter.notes = notesList
                }
            }
        }
    }
}

