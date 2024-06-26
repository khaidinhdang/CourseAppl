package com.example.courseapp.ViewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.courseapp.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser

class AuthViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    val firebaseUser: LiveData<FirebaseUser?> = authRepository.firebaseUser
    val error: LiveData<String> = authRepository.error
    val updateSuccess: LiveData<Boolean> = authRepository.updateSuccess
    val userRole: LiveData<String> = authRepository.userRole

    fun login(email: String, password: String, context: Context) {
        authRepository.login(email, password, context)
    }

    fun register(
        email: String,
        password: String,
        name: String,
        bio: String,
        userName: String,
        imageUri: Uri,
        role: String,
        context: Context
    ) {
        authRepository.register(email, password, name, bio, userName, imageUri, role, context)
    }

    fun promoteToAdmin(uid: String) {
        authRepository.promoteToAdmin(uid)
    }

    fun forgot(email: String) {
        authRepository.forgot(email)
    }

    fun logout() {
        authRepository.logout()
    }

    fun updateUser(
        name: String,
        email: String,
        bio: String,
        userName: String,
        imageUri: Uri?,
        context: Context
    ) {
        authRepository.updateUser(name, email, bio, userName, imageUri, context)
    }

    fun checkAdminPrivileges(context: Context): Boolean {
        return authRepository.checkAdminPrivileges(context)
    }

    fun fetchUserRole() {
        authRepository.fetchUserRole()
    }
}
