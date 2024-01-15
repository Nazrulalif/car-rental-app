package com.example.rental_mobil

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rental_mobil.Admin.LoginFragment
import com.example.rental_mobil.Customer.AboutActivity
import com.example.rental_mobil.Customer.ContactActivity
import com.example.rental_mobil.Customer.CarActivity
import com.example.rental_mobil.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var b: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.hide()

        b.btnLoginPelanggan.setOnClickListener {
            val dialogFragment = com.example.rental_mobil.Customer.LoginFragment()
            dialogFragment.show(supportFragmentManager, "LoginPelangganFragment")
        }

        b.btnLoginAdmin.setOnClickListener {
            val dialogFragment = LoginFragment()
            dialogFragment.show(supportFragmentManager, "LoginAdminFragment")
        }

        b.btnCars.setOnClickListener {
            startActivity(Intent(this, CarActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
        }

        b.btnAbout.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
        }

        b.btnContact.setOnClickListener {
            startActivity(Intent(this, ContactActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
        }
    }
}