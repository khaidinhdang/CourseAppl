package com.example.courseapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.courseapp.model.Notes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CalendarViewModel : ViewModel() {
    private val mAuth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()
    private val noteRef = db.getReference("notes")

    private val _notes = MutableLiveData<List<Notes>>()
    val notes: LiveData<List<Notes>> = _notes

    init {
        fetchNotes()
    }

    fun addNote(note: Notes) {
        note.timestamp = System.currentTimeMillis()
        val userId = mAuth.currentUser?.uid
        userId?.let { uid ->
            noteRef.child(uid).push().setValue(note)
                .addOnSuccessListener {
                    fetchNotes()
                }.addOnFailureListener {

                }
        }
    }

    fun updateNote(note: Notes) {
        note.timestamp = System.currentTimeMillis()
        val userId = mAuth.currentUser?.uid
        userId?.let { uid ->
            noteRef.child(uid).child(note.id).setValue(note)
                .addOnSuccessListener {
                    fetchNotes()
                }.addOnFailureListener {

                }
        }
    }

    fun fetchNotes() {
        val userId = mAuth.currentUser?.uid
        userId?.let { uid ->
            noteRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val notesList = mutableListOf<Notes>()
                    snapshot.children.forEach { noteSnapshot ->
                        val note = noteSnapshot.getValue(Notes::class.java)
                        note?.let {
                            notesList.add(note)
                        }
                    }
                    _notes.value = notesList.sortedByDescending { it.timestamp }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }

    fun deleteNote(noteUid: String) {
        val userId = mAuth.currentUser?.uid
        userId?.let { uid ->
            noteRef.child(uid).child(noteUid).removeValue()
                .addOnSuccessListener {
                    fetchNotes()
                }.addOnFailureListener {

                }
        }
    }
}
