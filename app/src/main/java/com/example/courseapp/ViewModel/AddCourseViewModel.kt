package com.example.courseapp.ViewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.courseapp.model.Course
import com.example.courseapp.model.Lesson
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.storage
import java.util.UUID

class AddCourseViewModel : ViewModel() {
    private val db = FirebaseDatabase.getInstance()
    private val courseRef = db.getReference("course")

    private val storageRef = Firebase.storage.reference
    private val imageRef = storageRef.child("course/${UUID.randomUUID()}.jpg")

    private val _isPosted = MutableLiveData<Boolean>()
    val isPosted: LiveData<Boolean> = _isPosted

    fun saveData(
        title: String,
        content: String,
        classes: String,
        duration: String,
        chapter: String,
        userId: String,
        imageUrl: String,
        price: String,
        categoryId:String,
        lessons: List<Lesson>
    ) {
        val courseId = UUID.randomUUID().toString() // Tạo một courseId duy nhất
        val courseData = Course(title, content, classes, duration, chapter, imageUrl, userId, courseId, price, categoryId, lessons)
        courseRef.child(courseId).setValue(courseData)
            .addOnSuccessListener {
                _isPosted.postValue(true)
            }.addOnFailureListener {
                // Xử lý lỗi khi lưu dữ liệu
                Log.e("AddCourseViewModel", "Error saving data: $it")
                _isPosted.postValue(false)
            }
    }

    fun saveImage(
        title: String,
        content: String,
        classes: String,
        duration: String,
        chapter: String,
        userId: String,
        imageUri: Uri,
        price: String,
        categoryId:String,
        lessons: List<Lesson>
    ) {
        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener { _ ->
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                saveData(title, content, classes, duration, chapter, userId, uri.toString(), price, categoryId, lessons)
            }
        }.addOnFailureListener { e ->
            // Xử lý lỗi khi tải ảnh lên
            Log.e("AddCourseViewModel", "Error uploading image: $e")
            _isPosted.postValue(false)
        }
    }
}
