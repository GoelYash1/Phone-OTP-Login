package com.example.stockmarketkotlin.presentation.onboardingScreen.phoneNumberScreen

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.stockmarketkotlin.util.Resource
import com.example.stockmarketkotlin.viewModels.AuthViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneNumberScreen(context: Context, viewModel: AuthViewModel = hiltViewModel(), mainNavController: NavHostController) {
    val phoneNumber = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var isCircularLoading by remember { mutableStateOf(false) }
    var verificationID by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize(),
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
            Spacer(modifier = Modifier.height(25.dp))
            Text(
                text = "Phone Number",
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(5.dp))
            TextField(
                value = phoneNumber.value,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { phoneNumber.value = it },
                placeholder = { Text(text = "Enter Phone Number") },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(25.dp))
            Button(
                onClick = {
                    if (phoneNumber.value.isBlank()) {
                        Toast.makeText(context, "Please enter your phone number..", Toast.LENGTH_SHORT).show()
                    } else {
                        val number = "+91${phoneNumber.value}"
                        scope.launch(Dispatchers.Main) {
                            viewModel.createUserWithPhone(number, context as Activity).collect { result ->
                                when (result) {
                                    is Resource.Success -> {
                                        isCircularLoading = false
                                        Toast.makeText(context, "OTP has been generated", Toast.LENGTH_SHORT).show()
                                        verificationID = result.data.toString()
                                        Log.d(TAG,"verificationID --------------> $verificationID")
                                        mainNavController.navigate("otp")
                                    }
                                    is Resource.Error -> {
                                        isCircularLoading = false
                                        Log.d(TAG, result.error.toString())
                                        Toast.makeText(context,"Some Error in generating the verification code",Toast.LENGTH_SHORT).show()
                                    }
                                    is Resource.Loading -> {
                                        isCircularLoading = true
                                    }
                                }
                            }
                        }
                    }
                }
            ) {
                Text(text = "Request OTP")
            }
        }
    }
    if (isCircularLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            CircularProgressIndicator()
        }
    }
}

