package com.example.ui.userScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.UserResponse

@Composable
fun ListOfUsers(usersList: List<UserResponse.Users>) {
LazyColumn(content = {
    items(usersList){user->
        UserProfileCard(user)
    }
})
}

@Composable
fun UserProfileCard(
user:UserResponse.Users
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(user.avatar)
                    .crossfade(true).build(),
                contentDescription = "News Thumbnail",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                    .background(
                        color = MaterialTheme.colorScheme.onSurface,
                        MaterialTheme.shapes.medium
                    )
            )
            Column(
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                Text(
                    text = "${user.firstName} ${user.lastName}",
                    style = TextStyle(fontSize = 20.sp)
                )
                Text(
                    text = "Phone : ${user.email}.",
                    style = TextStyle(fontSize = 16.sp)
                )

                Text(
                    text = "Country: ${user.id}",
                    style = TextStyle(fontSize = 16.sp)
                )
            }
        }
    }
}

@Preview
@Composable
fun ProfileCard() {
//
//    UserProfileCard(UserResponse.Users(name = "Satyajit das"))
//
}