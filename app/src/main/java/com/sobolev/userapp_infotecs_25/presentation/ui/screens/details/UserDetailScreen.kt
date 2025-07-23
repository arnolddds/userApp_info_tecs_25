package com.sobolev.userapp_infotecs_25.presentation.ui.screens.details

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MarkunreadMailbox
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch
import com.sobolev.userapp_infotecs_25.R
import com.sobolev.userapp_infotecs_25.presentation.ui.theme.Blue100
import com.sobolev.userapp_infotecs_25.presentation.ui.theme.Grey300

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailsScreen(
    userId: Int,
    onFinished: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UserDetailViewModel = hiltViewModel(
        creationCallback = { factory: UserDetailViewModel.Factory ->
            factory.create(userId)
        }
    )
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsState()
    val currentState = state.value

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(currentState) {
        if (currentState is UserDetailState.Error) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = currentState.message,
                    actionLabel = "OK"
                )
            }
        }
    }

    when (currentState) {
        is UserDetailState.Checking -> {
            Scaffold(
                modifier = modifier,
                topBar = {
                    TopAppBar(
                        title = { Title(title = stringResource(R.string.user_details)) },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    viewModel.processCommand(UserDetailCommand.Back)
                                }
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(R.string.back)
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.White,
                            actionIconContentColor = MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = currentState.user.picture.large,
                            contentDescription = stringResource(R.string.user_avatar),
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(Blue100),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(Modifier.width(16.dp))

                        Column {
                            Text(
                                text = currentState.user.fullName,
                                style = MaterialTheme.typography.headlineSmall,
                                color = Grey300
                            )
                            Text(
                                text = "@${currentState.user.login.username}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = stringResource(
                                    R.string.age_years,
                                    currentState.user.dob.toIntOrNull() ?: 0
                                ),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    SectionTitle(stringResource(R.string.contact_info))

                    ClickableInfoItem(
                        icon = Icons.Default.Email,
                        label = stringResource(R.string.email),
                        value = currentState.user.email
                    ) {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = "mailto:${currentState.user.email}".toUri()
                        }
                        try {
                            context.startActivity(intent)
                        } catch (e: ActivityNotFoundException) {
                            Toast.makeText(context, "No email app found", Toast.LENGTH_SHORT).show()
                        }
                    }

                    ClickableInfoItem(
                        icon = Icons.Default.Phone,
                        label = stringResource(R.string.phone),
                        value = currentState.user.phone
                    ) {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = "tel:${currentState.user.phone}".toUri()
                        }
                        try {
                            context.startActivity(intent)
                        } catch (e: ActivityNotFoundException) {
                            Toast.makeText(context, "No phone app found", Toast.LENGTH_SHORT).show()
                        }
                    }

                    SectionTitle(stringResource(R.string.address))

                    val fullAddress = "${currentState.user.location.street.number} ${currentState.user.location.street.name}, " +
                            "${currentState.user.location.city}, ${currentState.user.location.state}, ${currentState.user.location.country}"

                    ClickableInfoItem(
                        icon = Icons.Default.LocationOn,
                        label = stringResource(R.string.address),
                        value = fullAddress
                    ) {
                        val geoUri = "geo:0,0?q=${Uri.encode(fullAddress)}"
                        val intent = Intent(Intent.ACTION_VIEW, geoUri.toUri())
                        try {
                            context.startActivity(intent)
                        } catch (e: ActivityNotFoundException) {
                            Toast.makeText(context, "No card app found", Toast.LENGTH_SHORT).show()
                        }
                    }

                    InfoItem(Icons.Default.Lock, stringResource(R.string.city), currentState.user.location.city)
                    InfoItem(
                        Icons.Default.Home,
                        stringResource(R.string.state_country),
                        "${currentState.user.location.state}, ${currentState.user.location.country}"
                    )
                    InfoItem(
                        Icons.Default.MarkunreadMailbox,
                        stringResource(R.string.postcode),
                        currentState.user.location.postcode
                    )

                    SectionTitle(stringResource(R.string.additional_info))
                    InfoItem(Icons.Default.Person, stringResource(R.string.gender), currentState.user.gender)
                    InfoItem(Icons.Default.Face, stringResource(R.string.nationality), currentState.user.nat)
                    InfoItem(Icons.Default.HowToReg, stringResource(R.string.registered), currentState.user.registered.date)
                }
            }
        }

        UserDetailState.Finished -> {
            LaunchedEffect(key1 = Unit) {
                onFinished()
            }
        }

        UserDetailState.Initial -> {}

        is UserDetailState.Error -> {
            Scaffold(
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                topBar = {
                    TopAppBar(
                        title = { Title(title = stringResource(R.string.error)) },
                        navigationIcon = {
                            IconButton(onClick = {
                                viewModel.processCommand(UserDetailCommand.Back)
                            }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(R.string.back)
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                    )
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.failed_to_load_user),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}



@Composable
private fun Title(
    modifier: Modifier = Modifier,
    title: String
) {
    Text(
        text = title,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = 16.sp,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
private fun InfoItem(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, style = MaterialTheme.typography.labelSmall)
            Text(text = value, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun ClickableInfoItem(
    icon: ImageVector,
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, style = MaterialTheme.typography.labelSmall)
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline
                ),
                modifier = Modifier.clickable { onClick() }
            )
        }
    }
}

