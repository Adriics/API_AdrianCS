package org.example.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.runtime.LaunchedEffect
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        var posts by remember { mutableStateOf<List<Post>>(emptyList()) }
        var isLoading by remember { mutableStateOf(false) }
        var error by remember { mutableStateOf<String?>(null) }
        val greeting = remember { Greeting() }
        val scope = rememberCoroutineScope()

        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = { showContent = !showContent }) {
                Text(if (showContent) "Amagar Posts" else "Mostrar Posts")
            }

            AnimatedVisibility(showContent) {
                LaunchedEffect(showContent) {
                    isLoading = true
                    error = null
                    try {
                        val jsonString = greeting.greeting()
                        posts = greeting.parsePosts(jsonString)
                        if (posts.isEmpty()) {
                            error = "No s'han trobat posts"
                        }
                    } catch (e: Exception) {
                        error = e.message ?: "S'ha produït un error desconegut"
                    } finally {
                        isLoading = false
                    }
                }

                Column(
                    Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when {
                        isLoading -> {
                            CircularProgressIndicator()
                            Text("Carregant posts...", Modifier.padding(top = 8.dp))
                        }
                        error != null -> {
                            Text(error!!, color = MaterialTheme.colors.error)
                            Button(
                                onClick = {
                                    scope.launch {
                                        isLoading = true
                                        error = null
                                        try {
                                            val jsonString = greeting.greeting()
                                            posts = greeting.parsePosts(jsonString)
                                            if (posts.isEmpty()) {
                                                error = "No s'han trobat posts"
                                            }
                                        } catch (e: Exception) {
                                            error = e.message ?: "S'ha produït un error desconegut"
                                        } finally {
                                            isLoading = false
                                        }
                                    }
                                },
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Text("Reintentar")
                            }
                        }
                        posts.isNotEmpty() -> {
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(posts) { post ->
                                    PostItem(post)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PostItem(post: Post) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = post.title,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = post.body,
                style = MaterialTheme.typography.body1
            )
        }
    }
}

