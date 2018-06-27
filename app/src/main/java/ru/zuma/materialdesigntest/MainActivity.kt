package ru.zuma.materialdesigntest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_main.*
import ru.zuma.materialdesigntest.db.PREF_COOKIES
import ru.zuma.materialdesigntest.rest.Product


class MainActivity : AppCompatActivity() {

    companion object {
        val REQ_AUTH = 1
    }

    // Product view
    private val productList = ArrayList<Product>()
    private lateinit var productAdapter: ProductAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    // Left menu
    lateinit var mDrawerList: ListView
    private lateinit var mDrawerToggle: ActionBarDrawerToggle
    private lateinit var mDrawerLayout: DrawerLayout

    var mNavItems = ArrayList<CategoriesItem>()

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

        // Product view
        // -----------------------------------------------------------
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
        // -----------------------------------------------------------

        // Left menu
        // -----------------------------------------------------------
        mNavItems.add(CategoriesItem("Косметика", R.drawable.ic_cosmetics))
        mNavItems.add(CategoriesItem("Хобби и спорт", R.drawable.ic_sports))
        mNavItems.add(CategoriesItem("Товары для дома", R.drawable.ic_home_products))
        mNavItems.add(CategoriesItem("Техника", R.drawable.ic_technics))
        mNavItems.add(CategoriesItem("Зоотовары и питомцы", R.drawable.ic_pet_supplies))
        mNavItems.add(CategoriesItem("Развлечения", R.drawable.ic_entertainment))
        mNavItems.add(CategoriesItem("Одежда и обувь", R.drawable.ic_clothes))
        mNavItems.add(CategoriesItem("Книги", R.drawable.ic_books))
        mNavItems.add(CategoriesItem("Растения", R.drawable.ic_plants))
        mNavItems.add(CategoriesItem("Авто и мото", R.drawable.ic_car_products))
        mNavItems.add(CategoriesItem("Детские товары", R.drawable.ic_child_products))
        mNavItems.add(CategoriesItem("Канцелярские товары", R.drawable.ic_stationery))
        mNavItems.add(CategoriesItem("Аптеки", R.drawable.ic_medicine_products))
        mNavItems.add(CategoriesItem("Ювелирные изделия", R.drawable.ic_jewelry))
        mNavItems.add(CategoriesItem("Услугм", R.drawable.ic_services))

        // DrawerLayout
        mDrawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)

        // Populate the Navigtion Drawer with options
        mDrawerList = findViewById(R.id.navList)
        val adapter = CategoriesAdapter(this, mNavItems)
        mDrawerList.adapter = adapter
        // -----------------------------------------------------------
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
