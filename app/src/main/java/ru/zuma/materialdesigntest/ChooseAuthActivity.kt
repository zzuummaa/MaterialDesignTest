package ru.zuma.materialdesigntest

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_choose_auth.*

class ChooseAuthActivity : AppCompatActivity() {

    companion object {
        val REQ_LOGIN = 2
        val REQ_REGISTER = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_auth)

        btLogin.setOnClickListener {
            intent = Intent(this, LoginActivity::class.java)
            startActivityForResult(intent, REQ_LOGIN)
        }

        btRegister.setOnClickListener {
            intent = Intent(this, RegisterActivity::class.java)
            startActivityForResult(intent, REQ_REGISTER)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        setResult(resultCode)
        finish()
    }
}
