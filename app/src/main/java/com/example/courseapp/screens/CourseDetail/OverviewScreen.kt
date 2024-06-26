package com.example.courseapp.screens.CourseDetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.courseapp.ViewModel.CourseWithUser
import com.example.courseapp.model.Course
import com.example.courseapp.navigation.Routes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OverviewScreen(
    course: Course,
    courseWithUser: CourseWithUser,
    navHostController: NavHostController
) {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed))
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            if (course.price > "") {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = course.image),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth().height(200.dp)
                    )
                    Text(
                        text = "Payment Details",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Title: ${course.title}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Price: ${course.price}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {
                            // Handle payment logic here
                            scope.launch {
                                bottomSheetScaffoldState.bottomSheetState.collapse()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "Proceed to Payment", fontSize = 16.sp, color = Color.White)
                    }
                }
            }
        },
        sheetPeekHeight = 0.dp // Ensure the sheet is fully hidden when not in use
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CourseInfoCard(color = Color(0x33ffc22a), value = course.chapter, label = "Chapters", textColor = Color(0xffFFC22A))
                    CourseInfoCard(color = Color(0x33fb693c), value = course.classes, label = "Classes", textColor = Color(0xffFF5050))
                    CourseInfoCard(color = Color(0x332dd186), value = course.duration, label = "Duration", textColor = Color(0xff2DD186))
                }
            }
            item {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "About Course",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = course.content,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFC3C2C2)
                    )
                }
            }
            item {
                val gradientColors = listOf(
                    Color(0xFFFFAC71),
                    Color(0xFFFF8450)
                )
                val gradientBrush = Brush.horizontalGradient(gradientColors)
                Spacer(modifier = Modifier.height(10.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Button(
                        onClick = {
                            if (course.price > "") {
                                scope.launch {
                                    bottomSheetScaffoldState.bottomSheetState.expand()
                                }
                            } else {
                                navHostController.navigate(Routes.Lesson.routes + "/${course.courseId}")
                            }
                        },
                        modifier = Modifier
                            .size(200.dp, 50.dp)
                            .fillMaxSize()
                            .background(brush = gradientBrush, shape = RoundedCornerShape(10.dp)),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(10.dp),
                    ) {
                        Text(
                            text = if (course.price > "") "Pay ${course.price}" else "Learn Now",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CourseInfoCard(color: Color, value: String, label: String, textColor: Color) {
    Card(
        colors = CardDefaults.cardColors(containerColor = color),
        modifier = Modifier
            .size(108.dp, 67.dp)
            .clip(shape = RoundedCornerShape(10.dp))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = textColor
            )
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}
