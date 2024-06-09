package com.example.courseapp.screens.Calendarimport androidx.compose.foundation.layout.Arrangementimport androidx.compose.foundation.layout.PaddingValuesimport androidx.compose.foundation.layout.Rowimport androidx.compose.foundation.layout.Spacerimport androidx.compose.foundation.layout.fillMaxSizeimport androidx.compose.foundation.layout.heightimport androidx.compose.foundation.layout.paddingimport androidx.compose.foundation.lazy.LazyColumnimport androidx.compose.foundation.shape.RoundedCornerShapeimport androidx.compose.foundation.text.KeyboardActionsimport androidx.compose.foundation.text.KeyboardOptionsimport androidx.compose.material3.Buttonimport androidx.compose.material3.ButtonDefaultsimport androidx.compose.material3.ExperimentalMaterial3Apiimport androidx.compose.material3.Surfaceimport androidx.compose.material3.Textimport androidx.compose.material3.TextFieldimport androidx.compose.material3.TextFieldDefaultsimport androidx.compose.runtime.Composableimport androidx.compose.runtime.getValueimport androidx.compose.runtime.mutableStateOfimport androidx.compose.runtime.saveable.rememberSaveableimport androidx.compose.runtime.setValueimport androidx.compose.ui.ExperimentalComposeUiApiimport androidx.compose.ui.Modifierimport androidx.compose.ui.graphics.Colorimport androidx.compose.ui.platform.LocalSoftwareKeyboardControllerimport androidx.compose.ui.text.font.FontWeightimport androidx.compose.ui.text.input.ImeActionimport androidx.compose.ui.unit.dpimport androidx.compose.ui.unit.spimport androidx.compose.ui.window.Dialogimport androidx.lifecycle.viewmodel.compose.viewModelimport com.example.courseapp.ViewModel.CalendarViewModelimport com.example.courseapp.model.Notesimport java.util.UUID@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)@Composablefun DialogScreen(    note: Notes?,    onDismissed: () -> Unit,    onSave: (Notes) -> Unit) {    val calendarViewModel: CalendarViewModel = viewModel()    var title by rememberSaveable {        mutableStateOf(note?.title ?: "")    }    var content by rememberSaveable {        mutableStateOf(note?.description ?: "")    }    val keyboardController = LocalSoftwareKeyboardController.current    Dialog(onDismissRequest = { onDismissed() }) {        Surface(            shape = RoundedCornerShape(12.dp),            color = Color(0xFFFBFBFD),        ) {            LazyColumn(contentPadding = PaddingValues(12.dp)) {                item {                    Row(                        Modifier.fillMaxSize(),                        horizontalArrangement = Arrangement.Center                    ) {                        Text(                            text = if (note == null) "Add Task" else "Update Task",                            fontSize = 18.sp,                            fontWeight = FontWeight.Bold                        )                    }                }                item {                    Spacer(modifier = Modifier.height(16.dp))                    TextField(                        value = title,                        onValueChange = {                            title = it                        },                        label = { Text(text = "Task Title") },                        shape = RoundedCornerShape(10.dp),                        keyboardOptions = KeyboardOptions(                            imeAction = ImeAction.Done                        ),                        keyboardActions = KeyboardActions(                            onDone = {                                keyboardController?.hide()                            }                        ),                        colors = TextFieldDefaults.textFieldColors(                            unfocusedIndicatorColor = Color.Transparent,                            containerColor = Color.White,                        )                    )                }                item {                    Spacer(modifier = Modifier.height(16.dp))                    TextField(                        value = content,                        onValueChange = { content = it },                        label = { Text(text = "Task Text") },                        shape = RoundedCornerShape(10.dp),                        keyboardOptions = KeyboardOptions(                            imeAction = ImeAction.Done                        ),                        keyboardActions = KeyboardActions(                            onDone = {                                keyboardController?.hide()                            }                        ),                        colors = TextFieldDefaults.textFieldColors(                            unfocusedIndicatorColor = Color.Transparent,                            containerColor = Color.White                        )                    )                }                item {                    Spacer(modifier = Modifier.height(16.dp))                    Row(                        Modifier.fillMaxSize(),                        horizontalArrangement = Arrangement.Center                    ) {                        Button(                            onClick = {                                if (title.isNotBlank() && content.isNotBlank()) {                                    val newNote = note?.copy(                                        title = title,                                        description = content                                    ) ?: Notes(                                        id = UUID.randomUUID().toString(),                                        title = title,                                        description = content                                    )                                    onSave(newNote)                                    onDismissed()                                }                            },                            Modifier.padding(horizontal = 12.dp),                            colors = ButtonDefaults.buttonColors(                                containerColor = Color.Black,                                contentColor = Color.Black                            )                        ) {                            Text(text = if (note == null) "Save Task" else "Update Task", color = Color.White)                        }                    }                }            }        }    }}