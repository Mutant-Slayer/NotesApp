package com.example.notesapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class NotesDetailsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var database: NotesDatabase
    private var notes: Notes? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_details)

        val notesDesc = findViewById<TextView>(R.id.etNoteDetails)
        val deleteButton = findViewById<Button>(R.id.btnDeleteNote)
        val done = findViewById<Button>(R.id.btnSaveNote)
        val back = findViewById<Button>(R.id.btnBackPress)

        database = NotesDatabase.getDatabase(this)
        notes = intent.extras?.getSerializable("notes", Notes::class.java)
        notesDesc.text = notes?.desc ?: ""

        if (notes != null) {
            deleteButton.visibility = View.VISIBLE
            deleteButton.setOnClickListener(this)
        }
        done.setOnClickListener(this)
        back.setOnClickListener(this)

    }

    override fun onClick(view: View?) {

        when (view?.id) {
            R.id.btnSaveNote -> {
                val newDesc = findViewById<EditText>(R.id.etNoteDetails)
                val newTitle = findViewById<EditText>(R.id.etNoteTitle)
                if (notes == null) {
                    GlobalScope.launch {
                        database.notesDao().insertNotes(
                            Notes(
                                0,
                                newTitle.text.toString(),
                                newDesc.text.toString(),
                                Date()
                            )
                        )
                    }
                } else {
                    GlobalScope.launch {
                        database.notesDao()
                            .updateNotes(
                                Notes(
                                    notes?.id ?: 687848,
                                    newTitle.text.toString(),
                                    newDesc.text.toString(),
                                    notes?.createdAt ?: Date()
                                )
                            )
                    }
                }
                finish()
            }

            R.id.btnBackPress -> {
                val iNext = Intent(this, MainActivity::class.java)
                startActivity(iNext)
            }

            R.id.btnDeleteNote -> {
                GlobalScope.launch {
                    database.notesDao()
                        .deleteNotes(notes!!)
                }
                val iNext = Intent(this, MainActivity::class.java)
                startActivity(iNext)
            }
        }
    }
}