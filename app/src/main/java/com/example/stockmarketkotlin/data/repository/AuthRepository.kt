package com.example.stockmarketkotlin.data.repository

import android.app.Activity
import com.example.stockmarketkotlin.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun createUserWithPhone(
        phone: String,
        activity: Activity
    ): Flow<Resource<String>>

    fun signInWithCredential(
        otp: String
    ): Flow<Resource<String>>
}