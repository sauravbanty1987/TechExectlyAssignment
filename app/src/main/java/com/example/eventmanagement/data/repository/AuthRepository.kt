package com.example.eventmanagement.data.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepository(private val auth: FirebaseAuth = FirebaseAuth.getInstance()) {
    suspend fun login(e: String, p: String) = auth.signInWithEmailAndPassword(e, p).await();
    suspend fun signup(e: String, p: String) = auth.createUserWithEmailAndPassword(e, p).await();
    suspend fun reset(e: String) = auth.sendPasswordResetEmail(e).await();
    fun logout() = auth.signOut()
}
