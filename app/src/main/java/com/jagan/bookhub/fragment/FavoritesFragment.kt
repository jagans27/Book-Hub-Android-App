package com.jagan.bookhub.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.jagan.bookhub.R
import com.jagan.bookhub.adapter.FavouriteRecyclerAdapter
import com.jagan.bookhub.database.BookDatabase
import com.jagan.bookhub.database.BookEntity

class FavoritesFragment : Fragment() {

    lateinit var recyclerFavourite : RecyclerView

    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: FavouriteRecyclerAdapter

    var dbBookList = listOf<BookEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Toast.makeText(activity as Context,"In Favourites Fragment",Toast.LENGTH_SHORT).show()
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favorites, container, false)

        recyclerFavourite = view.findViewById(R.id.recyclerFavourite)


        layoutManager = GridLayoutManager(activity as Context,2)
        dbBookList = RetrieveFavourites(activity as Context).execute().get()

        println("The BOOKS ==> ${dbBookList.toString()}")

        if(activity !=null)
        {
            recyclerAdapter = FavouriteRecyclerAdapter(activity as Context,dbBookList)
            recyclerFavourite.adapter = recyclerAdapter
            recyclerFavourite.layoutManager = layoutManager
        }


        return view
    }
}

class RetrieveFavourites(val context: Context) : AsyncTask<Void,Void,List<BookEntity>>()
{
    override fun doInBackground(vararg p0: Void?): List<BookEntity> {
        println("---> BEFORE RETRIVE")

        val db = Room.databaseBuilder(context,BookDatabase::class.java,"bookS-db").build()
        println("RETRIVE ==> ${db.toString()} +++ ${db.bookDao().getAllBooks()}")
        return db.bookDao().getAllBooks()
    }

}



















