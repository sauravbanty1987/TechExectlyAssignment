package com.example.eventmanagement.data.repository

import android.util.Log
import com.example.eventmanagement.data.model.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class EventRepository(
    private val db: FirebaseFirestore =
        FirebaseFirestore.getInstance("techexactly"),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    private fun c() =
        db.collection("users")
            .document(auth.currentUser?.uid ?: error("Not signed in"))
            .collection("events")

    fun observe() = callbackFlow<List<Event>> {
        val r = c()
            .orderBy("dateTime", Query.Direction.DESCENDING)
            .addSnapshotListener { snap, e ->

                if (e != null) {
                    close(e)
                } else {
                    trySend(
                        snap?.documents?.mapNotNull {
                            it.toObject(Event::class.java)?.copy(id = it.id)
                        } ?: emptyList()
                    )
                }
            }

        awaitClose {
            r.remove()
        }
    }

    suspend fun add(e: Event) {
        try {
            val r = c().document()

            Log.d("FIRESTORE", "Starting write: ${r.id}")

            r.set(e.copy(id = r.id)).await()

            Log.d("FIRESTORE", "SUCCESS: ${r.id}")

        } catch (ex: Exception) {
            Log.e("FIRESTORE", "ERROR: ${ex.javaClass.simpleName}", ex)
            throw ex
        }
    }

    suspend fun update(e: Event) {
        c().document(e.id).set(e).await()
    }

    suspend fun delete(id: String) {
        c().document(id).delete().await()
    }
}
