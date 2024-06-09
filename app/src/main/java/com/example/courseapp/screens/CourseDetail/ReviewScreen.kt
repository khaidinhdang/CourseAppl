package com.example.courseapp.screens.CourseDetailimport androidx.compose.animation.fadeOutimport androidx.compose.foundation.Imageimport androidx.compose.foundation.backgroundimport androidx.compose.foundation.borderimport androidx.compose.foundation.clickableimport androidx.compose.foundation.layout.*import androidx.compose.foundation.lazy.LazyColumnimport androidx.compose.foundation.lazy.LazyRowimport androidx.compose.foundation.lazy.itemsimport androidx.compose.foundation.shape.CircleShapeimport androidx.compose.foundation.shape.RoundedCornerShapeimport androidx.compose.foundation.text.BasicTextimport androidx.compose.material.*import androidx.compose.material.icons.Iconsimport androidx.compose.material.icons.filled.Addimport androidx.compose.material.icons.filled.MoreVertimport androidx.compose.material.icons.filled.Starimport androidx.compose.material.icons.outlined.Starimport androidx.compose.material.icons.twotone.Starimport androidx.compose.runtime.*import androidx.compose.runtime.collectAsStateimport androidx.compose.ui.Alignmentimport androidx.compose.ui.Modifierimport androidx.compose.ui.draw.clipimport androidx.compose.ui.graphics.Brushimport androidx.compose.ui.graphics.Colorimport androidx.compose.ui.layout.ContentScaleimport androidx.compose.ui.text.font.FontWeightimport androidx.compose.ui.text.input.TextFieldValueimport androidx.compose.ui.unit.dpimport androidx.compose.ui.unit.spimport androidx.lifecycle.viewmodel.compose.viewModelimport coil.compose.rememberAsyncImagePainterimport com.example.courseapp.ViewModel.ReviewViewModelimport com.example.courseapp.model.Commentimport com.example.courseapp.model.Ratingimport com.example.courseapp.ui.theme.black80import com.example.courseapp.ui.theme.gray80import com.example.courseapp.ui.theme.orange80import com.example.courseapp.ui.theme.trackColorimport java.text.SimpleDateFormatimport java.util.Dateimport java.util.Localeimport androidx.compose.material3.Buttonimport androidx.compose.material3.ButtonDefaultsimport androidx.compose.material3.ExperimentalMaterial3Apiimport androidx.compose.material3.TextFieldimport androidx.compose.material3.TextFieldDefaultsimport androidx.compose.ui.window.Dialogimport com.composables.ui.Menuimport com.composables.ui.MenuButtonimport com.composables.ui.MenuContentimport com.composables.ui.MenuItemimport com.example.courseapp.ui.theme.white80import java.util.UUID@Composablefun ReviewScreen(courseId: String, reviewViewModel: ReviewViewModel = viewModel()) {    LaunchedEffect(courseId) {        reviewViewModel.setCourseId(courseId)    }    val comments by reviewViewModel.comments.collectAsState()    val ratings by reviewViewModel.ratings.collectAsState()    val gradientColors = listOf(        Color(0xFFFFAC71),        Color(0xFFFF8450)    )    val gradientBrush = Brush.horizontalGradient(gradientColors)    var showDialog by remember { mutableStateOf(false) }    var editingComment by remember { mutableStateOf<Comment?>(null) }    Column {        DisplayRatingAndReviews(comments = comments, ratings = ratings)        Spacer(modifier = Modifier.height(16.dp))        Row(            horizontalArrangement = Arrangement.SpaceBetween,            verticalAlignment = Alignment.CenterVertically,            modifier = Modifier.fillMaxWidth()        ) {            Text(                "Comments",                fontSize = 20.sp,                fontWeight = FontWeight.Bold,                color = black80            )            IconButton(onClick = { showDialog = true }) {                Icon(Icons.Default.Add, tint = black80, contentDescription = "Add")            }        }        if (showDialog || editingComment != null) {            AddOrEditCommentDialog(                onDismiss = {                    showDialog = false                    editingComment = null                },                onSubmit = { commentText, rating ->                    val currentUser = reviewViewModel.currentUser                    val userId = currentUser?.uid ?: ""                    val id = UUID.randomUUID().toString()                    val newComment = Comment(                        text = commentText,                        rating = rating,                        courseId = courseId,                        userId = userId,                        id = id,                        timestamp = System.currentTimeMillis()                    )                    if (editingComment == null) {                        reviewViewModel.addComment(newComment, courseId)                    } else {                        reviewViewModel.updateComment(editingComment!!.id, newComment, courseId)                    }                    showDialog = false                    editingComment = null                },                initialComment = editingComment            )        }        Spacer(modifier = Modifier.height(16.dp))        DisplayComments(comments = comments, onEdit = { comment ->            editingComment = comment            showDialog = true        }, onDelete = { commentId ->            reviewViewModel.deleteComment(commentId, courseId)        })    }}@Composableprivate fun DisplayRatingAndReviews(comments: List<Comment>, ratings: List<Rating>) {    // Calculate average rating    val averageRating = if (ratings.isNotEmpty()) {        val totalScore = ratings.sumOf { it.score * it.count }        val totalCount = ratings.sumOf { it.count }        totalScore.toFloat() / totalCount    } else {        0f    }    Row(        modifier = Modifier.fillMaxWidth(),        horizontalArrangement = Arrangement.SpaceBetween,        verticalAlignment = Alignment.CenterVertically,    ) {        Column {            Row(                horizontalArrangement = Arrangement.Center,                verticalAlignment = Alignment.CenterVertically            ) {                Text(                    text = "%.1f".format(averageRating),                    fontSize = 30.sp,                    fontWeight = FontWeight.Bold                )                Icon(                    Icons.Default.Star,                    contentDescription = null,                    tint = orange80                )            }            Text(                text = "${comments.size} Reviews",                fontSize = 14.sp,                fontWeight = FontWeight.Normal,                color = gray80            )        }        Column(            verticalArrangement = Arrangement.spacedBy(10.dp)        ) {            ratings.forEach { rating ->                DisplayRatingBar(rating = rating)            }        }    }}@Composableprivate fun DisplayRatingBar(rating: Rating) {    Row(        horizontalArrangement = Arrangement.Center,        verticalAlignment = Alignment.CenterVertically,    ) {        Text(            text = "${rating.score}",            color = gray80        )        Spacer(modifier = Modifier.padding(end = 8.dp))        LinearProgressIndicator(            modifier = Modifier                .size(240.dp, 8.dp)                .clip(shape = RoundedCornerShape(5.dp)),            progress = rating.progress.toFloat(),            backgroundColor = trackColor,            color = orange80,        )    }}@Composableprivate fun DisplayComments(    comments: List<Comment>,    onEdit: (Comment) -> Unit,    onDelete: (String) -> Unit) {    if (comments.isNotEmpty()) {        LazyColumn {            items(comments) { comment ->                CommentItem(comment = comment, onEdit = onEdit, onDelete = onDelete)            }        }    } else {        Text("No comments available")    }}@Composableprivate fun CommentItem(    comment: Comment,    onEdit: (Comment) -> Unit,    onDelete: (String) -> Unit) {    val user = comment.user    Row(        horizontalArrangement = Arrangement.SpaceBetween,        verticalAlignment = Alignment.CenterVertically,        modifier = Modifier.fillMaxWidth()    ) {        user?.let { currentUser ->            Image(                painter = rememberAsyncImagePainter(model = currentUser.imageUrl),                contentDescription = null,                modifier = Modifier                    .clip(shape = CircleShape)                    .size(38.dp),                contentScale = ContentScale.Crop            )            Spacer(modifier = Modifier.padding(end = 12.dp))            Column {                val options = listOf("Update", "Delete")                var selected by remember { mutableStateOf(0) }                Row(                    horizontalArrangement = Arrangement.SpaceBetween,                    verticalAlignment = Alignment.CenterVertically,                    modifier = Modifier.fillMaxWidth()                ) {                    Text(                        text = currentUser.userName,                        fontSize = 16.sp,                        fontWeight = FontWeight.SemiBold,                        color = black80                    )                    Column {                        Menu(Modifier.align(Alignment.End)) {                            MenuButton {                                Row(                                    verticalAlignment = Alignment.CenterVertically,                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)                                ) {                                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)                                    Spacer(Modifier.width(4.dp))                                }                            }                            MenuContent(                                modifier = Modifier                                    .width(70.dp)                                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(10.dp))                                    .background(Color.White)                                    .padding(4.dp),                                hideTransition = fadeOut()                            ) {                                options.forEachIndexed { index, option ->                                    MenuItem(                                        modifier = Modifier.clip(RoundedCornerShape(4.dp)),                                        onClick = {                                            if (index == 0) {                                                onEdit(comment)                                            } else {                                                onDelete(comment.id)                                            }                                        }                                    ) {                                        BasicText(option, modifier = Modifier                                            .fillMaxWidth()                                            .padding(vertical = 8.dp, horizontal = 4.dp))                                    }                                }                            }                        }                    }                }                LazyRow(                    modifier = Modifier.fillMaxWidth(),                    verticalAlignment = Alignment.CenterVertically,                    horizontalArrangement = Arrangement.spacedBy(5.dp)                ) {                    items(comment.rating) {                        Icon(                            Icons.Outlined.Star,                            contentDescription = null,                            tint = orange80                        )                    }                    item {                        Text(                            text = "${formatTimestamp(comment.timestamp)}",                            color = black80                        )                    }                }            }        }    }    Spacer(modifier = Modifier.height(12.dp))    Text(        text = comment.text,        color = black80    )    Divider(        color = gray80,        thickness = 1.dp,        modifier = Modifier.padding(vertical = 8.dp)    )}fun formatTimestamp(timestamp: Long): String {    val sdf = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())    val date = Date(timestamp)    return sdf.format(date)}@OptIn(ExperimentalMaterial3Api::class)@Composableprivate fun AddOrEditCommentDialog(    onDismiss: () -> Unit,    onSubmit: (String, Int) -> Unit,    initialComment: Comment? = null) {    var newCommentText by remember { mutableStateOf(TextFieldValue(initialComment?.text ?: "")) }    var newRating by remember { mutableStateOf(initialComment?.rating ?: 5) }    Dialog(        onDismissRequest = onDismiss,    ) {        Surface(            shape = RoundedCornerShape(20.dp),            color = white80,        ) {            LazyColumn(contentPadding = PaddingValues(12.dp)) {                item {                    Row(                        Modifier.fillMaxSize(),                        horizontalArrangement = Arrangement.Center                    ) {                        Text(                            text = if (initialComment == null) "Add Comment" else "Edit Comment",                            fontSize = 18.sp,                            fontWeight = FontWeight.Bold                        )                    }                }                item {                    Spacer(modifier = Modifier.height(16.dp))                    Column(                        modifier = Modifier                            .fillMaxWidth()                            .padding(16.dp)                    ) {                        TextField(                            value = newCommentText,                            onValueChange = { newCommentText = it },                            shape = RoundedCornerShape(10.dp),                            label = { Text("Comment") },                            modifier = Modifier.fillMaxWidth(),                            colors = TextFieldDefaults.textFieldColors(                                unfocusedIndicatorColor = Color.Transparent,                                containerColor = Color.White,                            )                        )                        Row(                            verticalAlignment = Alignment.CenterVertically,                            horizontalArrangement = Arrangement.spacedBy(8.dp),                            modifier = Modifier.padding(vertical = 8.dp)                        ) {                            Text("Rating: ", style = MaterialTheme.typography.body1)                            Row {                                (1..5).forEach { star ->                                    Icon(                                        imageVector = if (star <= newRating) Icons.Filled.Star else Icons.TwoTone.Star,                                        contentDescription = null,                                        tint = Color(0xFFFFA000),                                        modifier = Modifier                                            .size(24.dp)                                            .clickable { newRating = star }                                    )                                }                            }                        }                    }                }                item {                    Row(                        horizontalArrangement = Arrangement.End,                        modifier = Modifier.fillMaxWidth()                    ) {                        Button(                            onClick = {                                onSubmit(newCommentText.text, newRating)                            },                            Modifier.padding(horizontal = 12.dp),                            colors = ButtonDefaults.buttonColors(                                containerColor = Color.Black,                                contentColor = Color.Black                            )                        ) {                            Text(if (initialComment == null) "Add" else "Update", color = white80)                        }                        TextButton(onClick = onDismiss) {                            Text("Cancel", color = black80)                        }                    }                }            }        }    }}