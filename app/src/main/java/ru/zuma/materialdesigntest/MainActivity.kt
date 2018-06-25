package ru.zuma.materialdesigntest

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.*
import ru.zuma.materialdesigntest.db.PREF_COOKIES

class MainActivity : AppCompatActivity() {

    companion object {
        val REQ_AUTH = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // if !auth
        val intent = Intent(this, ChooseAuthActivity::class.java)
        startActivityForResult(intent, REQ_AUTH)
        //end
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            tvCookie.text = "Cookie:" + PreferenceManager.getDefaultSharedPreferences(this)
                    .getString(PREF_COOKIES, "")
        } else {
            finish()
        }
    }
}
