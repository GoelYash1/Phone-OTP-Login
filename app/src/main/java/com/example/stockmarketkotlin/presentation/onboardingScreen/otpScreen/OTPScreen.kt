package com.example.stockmarketkotlin.presentation.onboardingScreen.otpScreen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.stockmarketkotlin.Home
import com.example.stockmarketkotlin.util.Resource
import com.example.stockmarketkotlin.viewModels.AuthViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OTPScreen(context: Context, mainNavController: NavHostController, viewModel: AuthViewModel = hiltViewModel()) {
    var otp by remember { mutableStateOf("")}
    val scope = rememberCoroutineScope()
    var isCircularLoading by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .background(Color.Gray.copy(alpha = 0.1f), RoundedCornerShape(10.dp))
                .padding(25.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Stock Market",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 32.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Login",
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(25.dp))
            Text(
                text = "OTP",
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Left
            )
            Spacer(modifier = Modifier.height(5.dp))
            TextField(
                value = otp,
                onValueChange = {otp = it},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = { Text(text = "Enter OTP")}
            )
            Spacer(modifier = Modifier.height(25.dp))
            Button(
                onClick = {
                    scope.launch(Dispatchers.Main) {
                        viewModel.signInWithCredential(
                            otp
                        ).collect{
                            when(it){
                                is Resource.Loading ->{
                                    isCircularLoading = true
                                }
                                is Resource.Error ->{
                                    isCircularLoading = false
                                    Toast.makeText(context,"Error->${it.error}",Toast.LENGTH_SHORT).show()
                                }
                                is Resource.Success->{
                                    Toast.makeText(context,"Verification Complete",Toast.LENGTH_SHORT).show()
                                    isCircularLoading = false
                                    mainNavController.navigate(Home.route)
                                }
                            }
                        }
                    }
                }
            ) {
                Text(text = "Verify OTP")
            }
        }
    }
    if (isCircularLoading)
    {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            CircularProgressIndicator()
        }
    }
}