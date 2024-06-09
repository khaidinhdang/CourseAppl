package com.example.courseapp.ViewModelimport androidx.lifecycle.LiveDataimport androidx.lifecycle.MutableLiveDataimport androidx.lifecycle.ViewModelimport com.example.courseapp.model.Courseimport com.example.courseapp.model.Userimport com.google.firebase.auth.FirebaseAuthimport com.google.firebase.database.*data class CourseWithUser(    val course: Course,    val user: User)class HomeViewModel : ViewModel() {    private val database = FirebaseDatabase.getInstance()    private val auth = FirebaseAuth.getInstance()    private val courseRef = database.getReference("course")    private val userRef = database.getReference("user")    private val _courseAndUsers = MutableLiveData<List<CourseWithUser>>()    val courseAndUsers: LiveData<List<CourseWithUser>> = _courseAndUsers    private val _searchResults = MutableLiveData<List<CourseWithUser>?>()    val searchResults: LiveData<List<CourseWithUser>?> = _searchResults    private val _userCourses = MutableLiveData<List<CourseWithUser>?>()    val userCourses: LiveData<List<CourseWithUser>?> = _userCourses    private val _filteredCourses = MutableLiveData<List<CourseWithUser>>()    val filteredCourses: LiveData<List<CourseWithUser>> get() = _filteredCourses    init {        fetchCourseAndUsers()        fetchCoursesByUserId()    }    private fun fetchCourseAndUsers() {        val courseList = mutableListOf<CourseWithUser>()        courseRef.addListenerForSingleValueEvent(object : ValueEventListener {            override fun onDataChange(snapshot: DataSnapshot) {                for (courseSnapshot in snapshot.children) {                    val course = courseSnapshot.getValue(Course::class.java)                    course?.let {                        fetchUserForCourse(it) { user ->                            val courseWithUser = CourseWithUser(it, user)                            courseList.add(courseWithUser)                            _courseAndUsers.postValue(courseList)                        }                    }                }            }            override fun onCancelled(error: DatabaseError) {                // Handle errors            }        })    }    private fun fetchUserForCourse(course: Course, onResult: (User) -> Unit) {        userRef.child(course.userId).addListenerForSingleValueEvent(object : ValueEventListener {            override fun onDataChange(snapshot: DataSnapshot) {                val user = snapshot.getValue(User::class.java)                user?.let(onResult)            }            override fun onCancelled(error: DatabaseError) {                // Handle errors            }        })    }    fun searchCourses(query: String) {        val filteredCourses = _courseAndUsers.value?.filter {            it.course.title.contains(query, ignoreCase = true) ||                    it.user.userName.contains(query, ignoreCase = true)        }        _searchResults.postValue(filteredCourses)    }    fun fetchCoursesByUserId() {        val uid = auth.currentUser?.uid        uid?.let { userId ->            courseRef.orderByChild("userId").equalTo(userId)                .addListenerForSingleValueEvent(object : ValueEventListener {                    override fun onDataChange(snapshot: DataSnapshot) {                        val courseList = mutableListOf<CourseWithUser>()                        snapshot.children.forEach { courseSnapshot ->                            val course = courseSnapshot.getValue(Course::class.java)                            course?.let {                                fetchUserForCourse(it) { user ->                                    val courseWithUser = CourseWithUser(it, user)                                    courseList.add(courseWithUser)                                    _userCourses.postValue(courseList)                                }                            }                        }                    }                    override fun onCancelled(error: DatabaseError) {                        // Handle errors                    }                })        }    }    fun deleteCourse(courseId: String) {        courseRef.child(courseId).removeValue().addOnCompleteListener { task ->            if (task.isSuccessful) {                // Remove the course from the local LiveData                _courseAndUsers.value = _courseAndUsers.value?.filterNot { it.course.courseId == courseId }                _userCourses.value = _userCourses.value?.filterNot { it.course.courseId == courseId }            } else {                // Handle errors            }        }    }    fun updateCourse(course: Course) {        courseRef.child(course.courseId).setValue(course).addOnCompleteListener { task ->            if (task.isSuccessful) {                // Update the course in the local LiveData                _courseAndUsers.value = _courseAndUsers.value?.map {                    if (it.course.courseId == course.courseId) CourseWithUser(course, it.user) else it                }                _userCourses.value = _userCourses.value?.map {                    if (it.course.courseId == course.courseId) CourseWithUser(course, it.user) else it                }            } else {                // Handle errors            }        }    }    fun filterCoursesByCategory(categoryId: String) {        _filteredCourses.value = _courseAndUsers.value?.filter { it.course.categoryId == categoryId }    }    fun clearFilteredCourses() {        _filteredCourses.value = _courseAndUsers.value    }}