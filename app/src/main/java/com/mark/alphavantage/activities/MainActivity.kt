package com.mark.alphavantage.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mark.alphavantage.R
import com.mark.alphavantage.fragments.main.MainFragment


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startMainFragment()
    }

    private fun startMainFragment() {
        val fragment: Fragment = MainFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment, fragment::class.java.simpleName)
            .commit()
    }
}