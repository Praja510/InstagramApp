package com.muhammadprajadinata.instagramapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_edit_profil.*

class EditProfilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profil)

        buttonLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            val pergi = Intent(this@EditProfilActivity, LoginActivity::class.java)
            startActivity(pergi)
            finish()
        }


    }
}