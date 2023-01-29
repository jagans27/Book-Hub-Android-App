package com.jagan.bookhub.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.jagan.bookhub.R
import com.jagan.bookhub.activity.DescriptionActivity
import com.jagan.bookhub.database.BookEntity
import com.jagan.bookhub.model.Book
import com.squareup.picasso.Picasso
import org.w3c.dom.Text

//initialise class for data and context
class DashboardRecyclerAdapter(val context: Context, val itemList: ArrayList<Book>) :
    RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>() {

    class DashboardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txtBookName: TextView = view.findViewById(R.id.txtBookName)
        var txtBookRating: TextView = view.findViewById(R.id.txtBookRating)
        var txtBookAuthor: TextView = view.findViewById(R.id.txtBookAuthor)
        var imgBookImage: ImageView = view.findViewById(R.id.imgBookImage)
        var txtBookCost: TextView = view.findViewById(R.id.txtBookCost)
        var rlContent: LinearLayout = view.findViewById(R.id.bookDivRow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_dashboard_single_row, parent, false)
        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val book = itemList[position]
        holder.txtBookName.text = book.bookName
        holder.txtBookAuthor.text = book.bookAuthor
        holder.txtBookCost.text = book.bookPrice
        holder.txtBookRating.text = book.bookRating
        Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover)
            .into(holder.imgBookImage)

        holder.rlContent.setOnClickListener {
            val intent = Intent(context, DescriptionActivity::class.java)
            intent.putExtra("book_id", book.bookId)
            context.startActivity(intent)

            Toast.makeText(context, "Clicked on ${holder.txtBookName.text}!", Toast.LENGTH_SHORT)
                .show()
        }

    }
}