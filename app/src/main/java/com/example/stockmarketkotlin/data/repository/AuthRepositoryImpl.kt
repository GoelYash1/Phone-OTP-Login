package com.example.stockmarketkotlin.data.repository

import android.app.Activity
import android.util.Log
import com.example.stockmarketkotlin.util.Resource
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth_db: FirebaseAuth
) : AuthRepository{
    private lateinit var onVerificationCode: String
    override fun createUserWithPhone(phone: String,activity: Activity): Flow<Resource<String>> =  callbackFlow {
        trySend(Resource.Loading())

        val onVerificationCallback =
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {

                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    trySend(Resource.Error(p0))
                }

                override fun onCodeSent(
                    verificationCode: String,
                    p1: PhoneAuthProvider.ForceResendingToken
                ) {
                    super.onCodeSent(verificationCode, p1)
                    onVerificationCode = verificationCode
                    Log.d("On Verification Code value", "On Verification Code is --------> $onVerificationCode")
                    trySend(Resource.Success("OTP Sent Successfully"))
                }

            }
        val options = PhoneAuthOptions.newBuilder(auth_db)
            .setPhoneNumber(phone)
            .setTimeout(60L,TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(onVerificationCallback)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        awaitClose{
            close()
        }
    }

    override fun signInWithCredential(otp: String): Flow<Resource<String>> = callbackFlow{
        trySend(Resource.Loading())
        val credential = PhoneAuthProvider.getCredential(onVerificationCode,otp)
        auth_db.signInWithCredential(credential)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    trySend(Resource.Success("OTP Verified"))
                }
            }
            .addOnFailureListener{
                trySend(Resource.Error(it))
            }
        awaitClose {
            close()
        }
    }
}
