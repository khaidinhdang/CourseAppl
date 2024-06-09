package com.example.courseapp.screens.settingimport android.Manifestimport android.content.pm.PackageManagerimport android.net.Uriimport android.os.Buildimport androidx.activity.compose.rememberLauncherForActivityResultimport androidx.activity.result.contract.ActivityResultContractsimport androidx.compose.foundation.Imageimport androidx.compose.foundation.backgroundimport androidx.compose.foundation.clickableimport androidx.compose.foundation.layout.Arrangementimport androidx.compose.foundation.layout.Columnimport androidx.compose.foundation.layout.Spacerimport androidx.compose.foundation.layout.fillMaxSizeimport androidx.compose.foundation.layout.fillMaxWidthimport androidx.compose.foundation.layout.heightimport androidx.compose.foundation.layout.paddingimport androidx.compose.foundation.layout.sizeimport androidx.compose.foundation.shape.CircleShapeimport androidx.compose.foundation.shape.RoundedCornerShapeimport androidx.compose.material.AppBarDefaultsimport androidx.compose.material.IconButtonimport androidx.compose.material.Textimport androidx.compose.material.TextFieldimport androidx.compose.material.TextFieldDefaultsimport androidx.compose.material.TopAppBarimport androidx.compose.material.icons.Iconsimport androidx.compose.material.icons.filled.ArrowBackimport androidx.compose.material.icons.filled.MoreVertimport androidx.compose.material.icons.outlined.Emailimport androidx.compose.material.icons.outlined.Personimport androidx.compose.material3.ButtonDefaultsimport androidx.compose.material3.Iconimport androidx.compose.runtime.Composableimport androidx.compose.runtime.LaunchedEffectimport androidx.compose.runtime.getValueimport androidx.compose.runtime.livedata.observeAsStateimport androidx.compose.runtime.mutableStateOfimport androidx.compose.runtime.rememberimport androidx.compose.runtime.setValueimport androidx.compose.ui.Alignmentimport androidx.compose.ui.Modifierimport androidx.compose.ui.draw.clipimport androidx.compose.ui.graphics.Colorimport androidx.compose.ui.layout.ContentScaleimport androidx.compose.ui.platform.LocalContextimport androidx.compose.ui.text.font.FontWeightimport androidx.compose.ui.text.style.TextAlignimport androidx.compose.ui.unit.dpimport androidx.compose.ui.unit.spimport androidx.core.content.ContextCompatimport androidx.lifecycle.viewmodel.compose.viewModelimport androidx.navigation.NavHostControllerimport coil.compose.rememberAsyncImagePainterimport com.example.courseapp.navigation.Routesimport com.example.courseapp.utils.SharePrefimport com.example.courseapp.ViewModel.AuthViewModel@Composablefun ProfileScreen(navHostController: NavHostController) {    val authViewModel: AuthViewModel = viewModel()    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)    val updateSuccess by authViewModel.updateSuccess.observeAsState(false)    val context = LocalContext.current    var email by remember { mutableStateOf("") }    var userName by remember { mutableStateOf("") }    var name by remember { mutableStateOf("") }    var bio by remember { mutableStateOf("") }    var imageUri by remember { mutableStateOf<Uri?>(null) }    val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {        Manifest.permission.READ_MEDIA_IMAGES    } else {        Manifest.permission.READ_EXTERNAL_STORAGE    }    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->        imageUri = uri    }    val permissionLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->        if (isGranted) {            launcher.launch("image/*")        }    }    LaunchedEffect(updateSuccess) {        if (updateSuccess) {            navHostController.navigate(Routes.BottomNav.routes) {                popUpTo(Routes.Home.routes) {                    inclusive = true                }            }        }    }    LaunchedEffect(firebaseUser) {        if (firebaseUser == null) {            navHostController.navigate(Routes.BottomNav.routes) {                popUpTo(navHostController.graph.startDestinationId)                launchSingleTop = true            }        }        else {            name = SharePref.getName(context)            email = SharePref.getEmail(context)            bio = SharePref.getBio(context)            userName = SharePref.getUserName(context)            imageUri = Uri.parse(SharePref.getImage(context))        }    }    Column {        TopAppBar(            title = { Text(text = "Edit Profile", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },            backgroundColor = Color.White,            contentColor = Color.Black,            elevation = AppBarDefaults.TopAppBarElevation,            navigationIcon = {                IconButton(onClick = { navHostController.popBackStack() }) {                    androidx.compose.material.Icon(Icons.Default.ArrowBack, contentDescription = "Back")                }            },            actions = {                IconButton(onClick = { /* TODO: Handle more options */ }) {                    androidx.compose.material.Icon(Icons.Default.MoreVert, contentDescription = "More")                }            }        )        Column(            horizontalAlignment = Alignment.Start,            verticalArrangement = Arrangement.Top,            modifier = Modifier                .fillMaxSize()                .background(Color.White)                .padding(25.dp)        ) {            Column(                horizontalAlignment = Alignment.CenterHorizontally,                verticalArrangement = Arrangement.Center,                modifier = Modifier.fillMaxWidth()            ) {                Image(                    painter = if (imageUri == null)                        rememberAsyncImagePainter(model = SharePref.getImage(context))                    else rememberAsyncImagePainter(model = imageUri),                    contentDescription = null,                    modifier = Modifier                        .size(100.dp)                        .clip(CircleShape)                        .background(Color.LightGray)                        .clickable {                            val isGranted = ContextCompat.checkSelfPermission(                                context,                                permissionToRequest                            ) == PackageManager.PERMISSION_GRANTED                            if (isGranted) {                                launcher.launch("image/*")                            } else {                                permissionLauncher.launch(permissionToRequest)                            }                        },                    contentScale = ContentScale.Crop                )            }            Spacer(modifier = Modifier.height(35.dp))            TextFieldWithLabel("UserName", userName) { userName = it }            Spacer(modifier = Modifier.height(10.dp))            TextFieldWithLabel("Name", name) { name = it }            Spacer(modifier = Modifier.height(10.dp))//            TextFieldWithLabel("Bio", bio) { bio = it }//            Spacer(modifier = Modifier.height(10.dp))            Column(                modifier = Modifier.fillMaxWidth(),                horizontalAlignment = Alignment.CenterHorizontally,                verticalArrangement = Arrangement.spacedBy(20.dp)            ) {                Column(                    modifier = Modifier                        .fillMaxSize(),                    horizontalAlignment = Alignment.CenterHorizontally,                ) {                    Spacer(modifier = Modifier.height(20.dp))                    androidx.compose.material3.Button(                        onClick = {                            authViewModel.updateUser(name, email, bio, userName, imageUri, context)                        },                        modifier = Modifier                            .size(366.dp, 56.dp)                            .background(                                color = Color(0xFFFF8450),                                shape = RoundedCornerShape(20.dp)                            ),                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),                        shape = RoundedCornerShape(20.dp)                    ) {                        Text(                            text = "Save Changes",                            fontWeight = FontWeight.SemiBold,                            fontSize = 16.sp,                            color = Color.White                        )                    }                    Spacer(modifier = Modifier.height(20.dp))                }            }        }    }}@Composablefun TextFieldWithLabel(label: String, value: String, onValueChange: (String) -> Unit) {    Column(        horizontalAlignment = Alignment.Start,        verticalArrangement = Arrangement.Top    ) {        Text(            text = label,            fontWeight = FontWeight.Bold,            fontSize = 15.sp,            textAlign = TextAlign.Left        )        Spacer(modifier = Modifier.height(10.dp))        TextField(            value = value,            onValueChange = onValueChange,            label = {                Text(                    text = "Enter your $label",                    fontWeight = FontWeight.SemiBold,                    fontSize = 14.sp,                    color = Color(0xFFB2B9CA)                )            },            leadingIcon = {                Icon(                    imageVector = when (label) {                        "Email" -> Icons.Outlined.Email                        else -> Icons.Outlined.Person                    },                    contentDescription = null                )            },            colors = TextFieldDefaults.textFieldColors(                unfocusedIndicatorColor = Color.Transparent,                backgroundColor = Color(0xFFFBFBFD),                cursorColor = Color(0xFFB2B9CA)            ),            modifier = Modifier.size(366.dp, 56.dp)        )    }    Spacer(modifier = Modifier.height(10.dp))}