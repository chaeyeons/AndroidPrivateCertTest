package com.example.myapplication

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.core.RetrofitClient
import com.example.myapplication.model.AuthRequest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AuthScreen(context = baseContext)
                }
            }
        }
    }
}

@Composable
fun AuthScreen(context: Context) {
    var accessToken by remember { mutableStateOf<String?>(null) }
    var refreshToken by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = {
            scope.launch {
                try {
                    val response = RetrofitClient.create(context).getToken(
                        AuthRequest("dejay", "emwpdl123!")
                    )
                    if (response.isSuccessful) {
                        response.body()?.let {
                            accessToken = it.access
                            refreshToken = it.refresh
                        }
                    } else {
                        errorMessage = "Error: ${response.code()}"
                    }
                } catch (e: Exception) {
                    errorMessage = "Exception: ${e.message}"
                }
            }
        }) {
            Text("Fetch Token")
        }

        accessToken?.let {
            BasicText("Access Token: $it")
        }
        refreshToken?.let {
            BasicText("Refresh Token: $it")
        }
        errorMessage?.let {
            BasicText("Error: $it")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAuthScreen() {
//    AuthScreen()
}
