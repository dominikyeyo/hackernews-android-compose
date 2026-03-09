package com.diego.hackernewsapp.presentation.post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diego.hackernewsapp.domain.model.Post

@Composable
fun PostItem(
    post: Post,
    onClick: (Post) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {

        Card(
            modifier = modifier
                .fillMaxWidth()
                .clickable { onClick(post) },

            shape = RoundedCornerShape(0.dp),

            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    text = post.title,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "${post.author} - ${post.createdAt}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}