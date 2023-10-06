package com.example.ui.mainactivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.MyApplication
import com.example.base.UiState
import com.example.ui.generic.showLoading
import com.example.ui.theme.KNetworkingExample
import com.example.ui.userScreen.ListOfUsers

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KNetworkingExample {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val mainViewModel: MainViewModel =
                        viewModel(factory = (application as MyApplication).provideSpecificViewModelFactory())

                    val uiState=mainViewModel.uiState.collectAsState().value

                    when(uiState){
                        is UiState.Error->{
                            showLoading()
                        }

                        is UiState.Loading->{

                        }

                        is UiState.Success->{
//                            ListOfUsers(uiState.data)
                            Text(text = uiState.data.results[0].phone ?: "")
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KNetworkingExample {
        Greeting("Android")
    }
}