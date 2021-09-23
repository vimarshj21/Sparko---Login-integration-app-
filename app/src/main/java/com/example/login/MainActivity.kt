package com.example.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.login.databinding.ActivityMainBinding
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    val auth = FirebaseAuth.getInstance().currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        if (auth != null) {
            createUI()
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            // printKeyHash()
        }

        btn_logout.setOnClickListener {
            AuthUI.getInstance().signOut(this).addOnSuccessListener {
                startActivity(Intent(this, LoginActivity::class.java))

                Toast.makeText(this, "Successfully logged out", Toast.LENGTH_SHORT).show()
            }
        }


    }
    override fun onResume() {
        super.onResume()
        if(auth!=null&& intent!= null){

            createUI()
        }
        else{
            startActivity(Intent(this,LoginActivity::class.java))
            this.finish()

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    fun createUI() {

        val list = auth?.providerData
        var providerData: String = ""
        list?.let {
            for (provider in list) {
                providerData = providerData + " " + provider.providerId
            }
        }

        auth?.let {
            txtName.text = auth.displayName
            txtEmail.text = auth.email
            txtPhone.text = auth.phoneNumber
            txtProvider.text = providerData
            Glide
                .with(this)
                .load(auth.photoUrl)
                .fitCenter()
                .placeholder(R.drawable.image)
                .into(profile_image)


        }
        /*  fun printKeyHash() {
              try {
                  val info = packageManager.getPackageInfo(
                      "com.example.socialsitelogin",
                      PackageManager.GET_SIGNATURES
                  )
                  for (signature in info.signatures) {
                      val md = MessageDigest.getInstance("SHA")
                      md.update(signature.toByteArray())
                      Log.e("KEYHASH", Base64.encodeToString(md.digest(), Base64.DEFAULT))
                  }
              } catch (e: PackageManager.NameNotFoundException) {
                  Log.d(TAG, "printHashKey()", e)
              } catch (e: NoSuchAlgorithmException) {
                  Log.d(TAG, "printHashKey()", e)
              }
          } */

    }
}