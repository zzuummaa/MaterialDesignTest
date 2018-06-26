package ru.zuma.materialdesigntest

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import ru.zuma.materialdesigntest.db.PREF_COOKIES
import ru.zuma.materialdesigntest.rest.Product

class MainActivity : AppCompatActivity() {

    companion object {
        val REQ_AUTH = 1
    }

    private val productList = ArrayList<Product>()
    private lateinit var productAdapter: ProductAdapter

    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cookie = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(PREF_COOKIES, "")

        if (cookie.equals("")) {
            val intent = Intent(this, ChooseAuthActivity::class.java)
            startActivityForResult(intent, REQ_AUTH)
        } else {
            tvCookie.text = "Cookie:" + cookie
        }

        productList.add(Product(1234, "Техника", "Смартфон", 12_500f, 3f))
        productList.add(Product(2345, "Игры", "Кукла", 1_300f, 0f))
        productAdapter = ProductAdapter(productList)

        viewManager = LinearLayoutManager(this)

        //lvMain.adapter = productAdapter
        rvMain.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = productAdapter
        }
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
