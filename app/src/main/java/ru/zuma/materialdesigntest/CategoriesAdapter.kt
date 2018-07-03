package ru.zuma.materialdesigntest

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.content.Context
import android.view.View
import android.widget.ImageView


/**
 * Created by Stephan on 27.06.2018.
 */
internal class CategoriesAdapter(var context: Context, var items: ArrayList<CategoriesItem>) : BaseAdapter() {

    override fun getCount(): Int {
        return items.size

    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View

        if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.categories_item, null)
        } else {
            view = convertView
        }

        val titleView = view.findViewById(R.id.title) as TextView
        val iconView = view.findViewById(R.id.icon) as ImageView

        titleView.text = items[position].mTitle
        iconView.setImageResource(items[position].mIcon)

        return view
    }
}