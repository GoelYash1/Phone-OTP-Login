package com.example.stockmarketkotlin

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.stockmarketkotlin.presentation.homeScreen.HomeScreen
import com.example.stockmarketkotlin.presentation.onboardingScreen.otpScreen.OTPScreen
import com.example.stockmarketkotlin.presentation.onboardingScreen.phoneNumberScreen.PhoneNumberScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val mainNavController = rememberNavController()
            MyApp(LocalContext.current,mainNavController)
        }
    }
}

@Composable
fun MyApp(context: Context, mainNavController: NavHostController){
    NavHost(
        navController = mainNavController,
        startDestination = "phone"
    ){
        composable("phone"){
            PhoneNumberScreen(context,mainNavController)
        }
        composable("otp/{verificationID}") { backStackEntry ->
            val verificationID =
                backStackEntry.arguments?.getString("verificationID") ?: ""
            OTPScreen(context, mainNavController, verificationID)
        }
        composable(Home.route){
            HomeScreen(mainNavController)
        }
    }
}