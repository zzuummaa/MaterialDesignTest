package ru.zuma.materialdesigntest

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import ru.zuma.materialdesigntest.rest.model.Product
import android.view.LayoutInflater
import android.widget.*

class ProductAdapter(private val products: List<Product>) :
        RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ProductAdapter.ViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_main_product_item, parent, false)
        // set the view's size, margins, paddings and layout parameters

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        val p = products[position]
        val view = holder.view

        view.findViewById<TextView>(R.id.tvProductID).text = "№ ${p.id}"
        view.findViewById<TextView>(R.id.tvProductCategory).text = p.category
        view.findViewById<TextView>(R.id.tvProductName).text = p.name
        view.findViewById<TextView>(R.id.tvPrice).text = "${p.price} руб."
        p.icon?.let {
            view.findViewById<ImageView>(R.id.icon).setImageDrawable(it)
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = products.size
}