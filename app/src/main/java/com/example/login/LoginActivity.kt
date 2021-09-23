package com.example.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    companion object{
        private const val RC_SIGN=123
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        createloginui()
    }
    fun createloginui(){
        val providers = arrayListOf<AuthUI.IdpConfig>(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.FacebookBuilder().build()
        )
        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .setTheme(R.style.FirebaseUI_DefaultMaterialTheme).build(), RC_SIGN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode== RC_SIGN){
            var response=IdpResponse.fromResultIntent(data)
            if (resultCode== Activity.RESULT_OK){
                val user=FirebaseAuth.getInstance().currentUser
                //  Log.d("USER_DATA", user?.displayName.toString())
                startActivity(Intent(this,MainActivity::class.java))
            }
            else{
                if (response==null){
                    finish()
                    return
                }
                if (response?.error?.errorCode==ErrorCodes.NO_NETWORK){
                    return
                }
                if (response?.error?.errorCode==ErrorCodes.UNKNOWN_ERROR){
                    Toast.makeText(this,response?.error?.errorCode.toString(),Toast.LENGTH_SHORT).show()
                    return
                }


            }
        }
    }
}