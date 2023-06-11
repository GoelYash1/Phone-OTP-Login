package com.example.stockmarketkotlin.presentation.onboardingScreen.otpScreen

import android.app.Activity
import android.content.Context
import android.text.TextUtils
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.stockmarketkotlin.Home
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OTPScreen(context: Context, mainNavController: NavHostController, verificationID: String) {
    val otp = remember {
        mutableStateOf("")
    }
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance();
    val message = remember {
        mutableStateOf("")
    }
    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val verificationID = navBackStackEntry?.arguments?.getString("verificationID") ?: ""
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
                value = otp.value,
                onValueChange = {otp.value = it},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                placeholder = { Text(text = "Enter OTP")}
            )
            Spacer(modifier = Modifier.height(25.dp))
            Button(
                onClick = {
                    if (TextUtils.isEmpty(otp.value.toString())) {
                        Toast.makeText(context, "Please enter otp..", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
                            verificationID, otp.value
                        )
                        signInWithPhoneAuthCredential(
                            credential,
                            mAuth,
                            context as Activity,
                            context,
                            message,
                            mainNavController
                        )
                    }
                }
            ) {
                Text(text = "Verify OTP")
            }
        }
    }
}

private fun signInWithPhoneAuthCredential(
    credential: PhoneAuthCredential,
    auth: FirebaseAuth,
    activity: Activity,
    context: Context,
    message: MutableState<String>,
    mainNavController: NavHostController
) {
    auth.signInWithCredential(credential)
        .addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                message.value = "Verification successful"
                Toast.makeText(context, "Verification successful..", Toast.LENGTH_SHORT).show()
                mainNavController.navigate(Home.route)
            } else {
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(
                        context,
                        "Verification failed.." + (task.exception as FirebaseAuthInvalidCredentialsException).message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
}