package ru.zuma.materialdesigntest

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_main.*
import kotlinx.coroutines.experimental.*
import ru.zuma.materialdesigntest.db.PREF_COOKIES
import ru.zuma.materialdesigntest.rest.CommiApi
import ru.zuma.materialdesigntest.rest.model.Product
import ru.zuma.materialdesigntest.rest.unwrapCall

class MainActivity : AppCompatActivity() {

    companion object {
        val REQ_AUTH = 1
    }

    // Product view
    private val productList = ArrayList<Product>()
    private lateinit var productAdapter: ProductAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    // Left menu
    private lateinit var mDrawerToggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout

    var categoryItems = ArrayList<CategoriesItem>()

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
            loadPageData()
            //tvCookie.text = "Cookie:" + cookie
        }

        // Product view
        // -----------------------------------------------------------
        //productList.add(Product(1234, "Техника", "Смартфон", 12_500f, 3f, false, 0))
        //productList.add(Product(2345, "Игры", "Кукла", 1_300f, 0f, false, 0))
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
        categoryItems.add(CategoriesItem("Косметика", R.drawable.ic_cosmetics))
        categoryItems.add(CategoriesItem("Хобби и спорт", R.drawable.ic_sports))
        categoryItems.add(CategoriesItem("Товары для дома", R.drawable.ic_home_products))
        categoryItems.add(CategoriesItem("Техника", R.drawable.ic_technics))
        categoryItems.add(CategoriesItem("Зоотовары и питомцы", R.drawable.ic_pet_supplies))
        categoryItems.add(CategoriesItem("Развлечения", R.drawable.ic_entertainment))
        categoryItems.add(CategoriesItem("Одежда и обувь", R.drawable.ic_clothes))
        categoryItems.add(CategoriesItem("Книги", R.drawable.ic_books))
        categoryItems.add(CategoriesItem("Растения", R.drawable.ic_plants))
        categoryItems.add(CategoriesItem("Авто и мото", R.drawable.ic_car_products))
        categoryItems.add(CategoriesItem("Детские товары", R.drawable.ic_child_products))
        categoryItems.add(CategoriesItem("Канцелярские товары", R.drawable.ic_stationery))
        categoryItems.add(CategoriesItem("Аптеки", R.drawable.ic_medicine_products))
        categoryItems.add(CategoriesItem("Ювелирные изделия", R.drawable.ic_jewelry))
        categoryItems.add(CategoriesItem("Услугм", R.drawable.ic_services))

        // DrawerLayout
        drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)

        val adapter = CategoriesAdapter(this, categoryItems)
        navList.adapter = adapter

        val drawerToggle = object : ActionBarDrawerToggle(this, drawerLayout,
                tbMain, R.string.drawer_open, R.string.drawer_close) {
            override fun onDrawerClosed(view: View) {
                super.onDrawerClosed(view)
                supportActionBar!!.show()
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
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

        btSearch.setOnClickListener {
            //TODO Replace toast with search logic
            toast("Search button clicked", Toast.LENGTH_SHORT)
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
            loadPageData()
        } else {
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.tb_help -> return true// TODO tb_help item clicked logic
            R.id.tb_shopping_cart -> return true// TODO tb_shopping_cart item clicked logic
            R.id.tb_liked -> return true// TODO tb_liked item clicked logic
            R.id.tb_personal_page -> return true// TODO tb_personal_page item clicked logic
            else -> return true
        }
    }

    private fun loadPageData() {
        launchPrintThrowable {
            val productsHits = CommiApi.instance
                    .getProductsHits("technics")
                    .unwrapCall()

            if (productsHits == null) {
                toastUI("Контент не был загружен с сервера")
                return@launchPrintThrowable
            }

            if (productsHits.products == null) {
                toastUI("Сервер не вернул нужный контент")
                return@launchPrintThrowable
            }

            runOnUiThread {
                productList.addAll(productsHits.products!!)
                productAdapter.notifyDataSetChanged()
            }

            val imageLoadDeferreds = ArrayList<Deferred<Any>>()

            productsHits.products!!.forEach {
                val nextDeferred = async {
                    val iconId = it.iconId ?: return@async
                    val responseBody = CommiApi.instance.getImage(iconId).unwrapCall()

                    if (responseBody == null) {
                        toastUI("Ошибка загрузки изображения")
                        return@async
                    }

                    if (responseBody.contentType()?.type() == "image") {
                        val bytes = responseBody.bytes()
                        it.icon = BitmapDrawable(
                                BitmapFactory.decodeByteArray(
                                        bytes, 0, bytes.size
                                )
                        )
                    } else {
                        Log.e(javaClass.simpleName, "Invalid content type. Required 'image'")
                        toastUI("Сервер вернул не изображение")
                    }
                }
                imageLoadDeferreds.add(nextDeferred)
            }

            imageLoadDeferreds.forEach {
                it.await()
            }

            runOnUiThread {
                productAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, text, duration).show()
    }

    private fun toastUI(text: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
        runOnUiThread {
            Toast.makeText(this, text, duration).show()
        }
    }
}

fun launchPrintThrowable(runnable: suspend CoroutineScope.() -> Unit): Job {
    return launch {
        try {
            runnable()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}