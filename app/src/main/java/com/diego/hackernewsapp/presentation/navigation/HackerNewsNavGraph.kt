package com.diego.hackernewsapp.presentation.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.diego.hackernewsapp.presentation.post.PostsScreen
import com.diego.hackernewsapp.presentation.webview.WebViewScreen

@Composable
fun HackerNewsNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "posts"
    ) {
        composable("posts") {
            PostsScreen(
                onPostClick = { post ->
                    val encodedUrl = Uri.encode(post.url)
                    navController.navigate("webview/$encodedUrl")
                }
            )
        }

        composable(
            route = "webview/{url}",
            arguments = listOf(
                navArgument("url") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val encodedUrl = backStackEntry.arguments?.getString("url").orEmpty()
            val decodedUrl = Uri.decode(encodedUrl)

            WebViewScreen(
                url = decodedUrl,
                onBack = { navController.popBackStack() }
            )
        }
    }
}