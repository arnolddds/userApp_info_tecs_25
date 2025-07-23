package com.sobolev.userapp_infotecs_25.presentation.ui.screens.users

import com.sobolev.userapp_infotecs_25.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.sobolev.userapp_infotecs_25.domain.entities.User
import com.sobolev.userapp_infotecs_25.presentation.ui.theme.Blue100
import com.sobolev.userapp_infotecs_25.presentation.ui.theme.UserColors
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersScreen(
    modifier: Modifier = Modifier,
    onUserClick: (User) -> Unit,
    viewModel: UsersViewModel = hiltViewModel()
) {
    val state by viewModel.screenState.collectAsState()

    val refreshState = rememberPullToRefreshState()
    val isRefreshing = state.isLoading

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(state.error) {
        state.error?.let { errorMessage ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = "$errorMessage Try restarting the application " +
                            "or check your internet connection.",
                    actionLabel = "ОК"
                )
            }
        }
    }

    PullToRefreshBox(
        state = refreshState,
        isRefreshing = isRefreshing,
        onRefresh = {
            viewModel.refresh()
        }
    ) {
        Scaffold(
            modifier = modifier,
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.all_users),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 24.dp),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White,
                        actionIconContentColor = MaterialTheme.colorScheme.onBackground
                    )
                )
            }
        ) { innerPadding ->

            LazyColumn(
                contentPadding = innerPadding
            ) {
                item {
                    Spacer(modifier = modifier.height(12.dp))
                }
                itemsIndexed(
                    items = state.allUsers,
                    key = { _, user -> user.id }
                ) { index, user ->
                    UserCard(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        user = user,
                        backgroundColor = UserColors[index % UserColors.size],
                        onUserClick = { onUserClick(user) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}



@Composable
private fun UserCard(
    modifier: Modifier = Modifier,
    user: User,
    backgroundColor: Color,
    onUserClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onUserClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .background(backgroundColor)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = user.picture.large,
                contentDescription = "User avatar",
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(Blue100),
                contentScale = ContentScale.Crop
            )


            Spacer(modifier = Modifier.width(24.dp))


            Column(
                modifier = Modifier
                    .weight(1f)
            ) {

                Text(
                    text = user.fullName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                InfoItem(
                    Icons.Default.LocationOn,
                    "${user.location.street.number} ${user.location.street.name}, ${user.location.city}"
                )

                Spacer(modifier = Modifier.height(4.dp))

                InfoItem(
                    Icons.Default.Phone,
                    user.phone
                )
            }

        }
    }
}

@Composable
private fun InfoItem(
    icon: ImageVector,
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
            contentDescription = value,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp)
        )
        Spacer(Modifier.width(8.dp))
        Column {
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

        }
    }
}