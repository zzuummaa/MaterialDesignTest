package ru.zuma.materialdesigntest

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_main.*
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
    lateinit var drawerList: ListView
    private lateinit var mDrawerToggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout

    var mNavItems = ArrayList<CategoriesItem>()

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cookie = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(PREF_COOKIES, "")

        if (cookie.equals("")) {
            val intent = Intent(this, ChooseAuthActivity::class.java)
            startActivityForResult(intent, REQ_AUTH)
        } else {
            //tvCookie.text = "Cookie:" + cookie
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
        drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)

        // Populate the Navigtion Drawer with options
        drawerList = findViewById(R.id.navList)
        val adapter = CategoriesAdapter(this, mNavItems)
        drawerList.adapter = adapter

        val drawerToggle = object : ActionBarDrawerToggle(this, drawerLayout,
                tbMain, R.string.drawer_open, R.string.drawer_close) {
            override fun onDrawerClosed(view: View) {
                super.onDrawerClosed(view)
                toast("onDrawerClosed")
                supportActionBar!!.show()
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                toast("onDrawerOpened")
                supportActionBar!!.hide()
            }
        }
        drawerLayout.addDrawerListener(drawerToggle)
        // -----------------------------------------------------------

        // Toolbar
        setSupportActionBar(tbMain)
        tbMain.setNavigationOnClickListener {
            drawerLayout.openDrawer(Gravity.LEFT)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_toolbar_items, menu)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
//            tvCookie.text = "Cookie:" + PreferenceManager.getDefaultSharedPreferences(this)
//                    .getString(PREF_COOKIES, "")
        } else {
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.tb_help -> return true// TODO tb_help item clicked
            R.id.tb_shopping_cart -> return true// TODO tb_shopping_cart item clicked
            R.id.tb_liked -> return true// TODO tb_liked item clicked
            R.id.tb_personal_page -> return true// TODO tb_personal_page item clicked
            R.id.tb_search -> return true// TODO tb_search item clicked
            else -> return true
        }
    }

    private fun toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, text, duration).show()
    }
}