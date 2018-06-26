package ru.zuma.materialdesigntest

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.*
import ru.zuma.materialdesigntest.db.PREF_COOKIES
import ru.zuma.materialdesigntest.rest.Product

class MainActivity : AppCompatActivity() {

    companion object {
        val REQ_AUTH = 1
    }

    val productList = ArrayList<Product>()
    lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cookie = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(PREF_COOKIES, "")

        if (cookie.equals("")) {
            val intent = Intent(this, ChooseAuthActivity::class.java)
            startActivityForResult(intent, REQ_AUTH)
        }

        productList.add(Product(1234, "Техника", "Смартфон", 12_500f, 3f))
        productList.add(Product(2345, "Игры", "Кукла", 1_300f, 0f))
        productAdapter = ProductAdapter(this, productList)

        lvMain.adapter = productAdapter
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
