package com.example.stockmarketkotlin

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.stockmarketkotlin.presentation.homeScreen.HomeScreen
import com.example.stockmarketkotlin.presentation.onboardingScreen.otpScreen.OTPScreen
import com.example.stockmarketkotlin.presentation.onboardingScreen.phoneNumberScreen.PhoneNumberScreen
import com.example.stockmarketkotlin.viewModels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val mainNavController = rememberNavController()
            MyApp(this,mainNavController)
        }
    }
}

@Composable
fun MyApp(context: Context, mainNavController: NavHostController){
    val authViewModel: AuthViewModel = hiltViewModel()
    NavHost(
        navController = mainNavController,
        startDestination = "phone"
    ){
        composable("phone"){
            PhoneNumberScreen(context,authViewModel,mainNavController)
        }
        composable("otp") {
            OTPScreen(context, mainNavController,authViewModel)
        }
        composable(Home.route){
            HomeScreen(mainNavController)
        }
    }
}