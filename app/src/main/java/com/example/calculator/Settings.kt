package com.example.calculator

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class Settings : AppCompatActivity() {
    @SuppressLint("UseSwitchCompatOrMaterialCode")

    private fun isUsingNightModeResources(): Boolean {
        return when (resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> false
            else -> false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val actionBar=supportActionBar

        actionBar!!.title="Settings"
        actionBar.setDisplayHomeAsUpEnabled(true)

        val switch=findViewById<Switch>(R.id.modeSwitch)

        switch.isChecked=!isUsingNightModeResources()

        switch.setOnCheckedChangeListener{ _, isChecked->
            if(isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

            }
        }
    }

    fun sendPrecision(view: View) {
        val precision=findViewById<EditText>(R.id.precision).text.toString()
        println("current precision  " + precision)

        var intent = Intent(this@Settings, MainActivity::class.java)
        intent.putExtra("precision", precision)
        startActivity(intent)
    }
}
