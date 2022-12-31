package com.devwarex.kabeertask

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.devwarex.kabeertask.api.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(
    R.layout.activity_main
) {
    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.IO).launch {
            val response = ApiClient.create().logIn()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val i = Intent(this@MainActivity, EmployeeActivity::class.java)
                        .apply {
                            putExtra("USER_TOKEN", body.Token ?: "")
                        }
                    delay(1200)
                    startActivity(i)
                    finish()
                }
            } else {
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.field_login_message),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
