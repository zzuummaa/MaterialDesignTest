package ru.zuma.materialdesigntest

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import ru.zuma.materialdesigntest.rest.Product
import android.view.LayoutInflater
import android.widget.*


/**
 * Created by Stephan on 26.06.2018.
 */

class ProductAdapter(val context: Context, val products: List<Product>) : BaseAdapter() {

    var lInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    // кол-во элементов
    override fun getCount(): Int {
        return products.size
    }

    // элемент по позиции
    override fun getItem(position: Int): Any {
        return products[position]
    }

    // id по позиции
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // пункт списка
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // используем созданные, но не используемые view
//        var view: View? = convertView
//        if (view == null) {
//            view = lInflater.inflate(R.layout.activity_main_product_item, parent, false)
//        }

//        val p = getProduct(position)
//
//        // заполняем View в пункте списка данными из товаров
//        (view!!.findViewById(R.id.tvProductID) as TextView).text = "№ ${p.id}"
//        (view.findViewById<View>(R.id.tvProductCategory) as TextView).text = "${p.category} руб."
//        (view.findViewById<View>(R.id.tvProductName) as TextView).text = p.name
//        (view.findViewById<View>(R.id.tvPrice) as TextView).text = "${p.price} руб."
//
//        markStars(view.findViewById<View>(R.id.llStars) as LinearLayout, p.stars!!.toInt())
//
//        return view
//    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View? = convertView
        if (view == null) {
            view = lInflater.inflate(R.layout.activity_main_product_item, parent, false)
        }

        val p = getProduct(position)

        (view!!.findViewById(R.id.tvProductID) as TextView).text = "№ ${p.id}"
        (view.findViewById<View>(R.id.tvProductCategory) as TextView).text = p.category
        (view.findViewById<View>(R.id.tvProductName) as TextView).text = p.name
        (view.findViewById<View>(R.id.tvPrice) as TextView).text = "${p.price} руб."

        val stars = p.stars?.toInt()
        if (stars != null) {
            markStars(view.findViewById<View>(R.id.llStars) as LinearLayout, stars)
        }

        return view!!
    }

    private inline fun markStars(llStars: LinearLayout, stars: Int) {

        if (stars > 5 || stars < 0) {
            Log.e(javaClass.simpleName, "Invalid stars count: $stars")
            return
        }

        for (i in 0..(stars-1)) {
            llStars.getChildAt(i)?.visibility = View.VISIBLE
            llStars.getChildAt(i + 5)?.visibility = View.GONE
        }

        for (i in (stars)..4) {
            llStars.getChildAt(i)?.visibility = View.GONE
            llStars.getChildAt(i + 5)?.visibility = View.VISIBLE
        }
    }

    // товар по позиции
    fun getProduct(position: Int): Product {
        return getItem(position) as Product
    }

}