package com.jagan.bookhub.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.jagan.bookhub.R
import com.jagan.bookhub.database.BookDatabase
import com.jagan.bookhub.database.BookEntity
import com.jagan.bookhub.model.Book
import com.jagan.bookhub.util.ConnectionManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_description.*
import kotlinx.android.synthetic.main.recycler_dashboard_single_row.*
import org.json.JSONObject
import java.lang.Exception

class DescriptionActivity : AppCompatActivity() {
    var bookId: String? = "100"

    lateinit var btnATF: Button
    lateinit var txtBookDesc: TextView
    lateinit var tvaboutBook: TextView
    lateinit var txtBookRatings: TextView
    lateinit var txtBookPrices: TextView
    lateinit var txtBookAuthors: TextView
    lateinit var txtBookNames: TextView
    lateinit var imgBookImages: ImageView

    lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        btnATF = findViewById(R.id.btnATF)
        txtBookDesc = findViewById(R.id.txtBookDesc)
        tvaboutBook = findViewById(R.id.tvaboutBook)
        txtBookRatings = findViewById(R.id.txtBookRatings)
        txtBookPrices = findViewById(R.id.txtBookPrices)
        txtBookAuthors = findViewById(R.id.txtBookAuthors)
        txtBookNames = findViewById(R.id.txtBookNames)
        imgBookImages = findViewById(R.id.imgBookImages)
        progressBar = findViewById(R.id.progressBar)
        progressLayout = findViewById(R.id.progressLayout)
        progressBar.visibility = View.VISIBLE
        progressLayout.visibility = View.VISIBLE

        if (intent != null) {
            bookId = intent.getStringExtra("book_id")
        } else {
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "Some unexpected error occurred !!!",
                Toast.LENGTH_LONG
            ).show()
        }

        if (bookId.equals("100")) {

            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "Some unexpected error occurred !!!",
                Toast.LENGTH_LONG
            ).show()

        }

        val queue = Volley.newRequestQueue(this@DescriptionActivity)
        val url = "http://13.235.250.119/v1/book/get_book/"

        val jsonParams = JSONObject()
        jsonParams.put("book_id", bookId)

        println("Came here before")

        if (ConnectionManager().checkConnectivity(this@DescriptionActivity)) {

            val jsonRequest =
                object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {
                    // success
                    progressBar.visibility = View.GONE
                    progressLayout.visibility = View.GONE
                    println("came to success")
                    try {
                        val success = it.getBoolean("success")
                        if (success) {
                            val bookJSONObject = it.getJSONObject("book_data")
                            println("Came here success")

                            val bookImageUrl = bookJSONObject.getString("image")
                            Picasso.get().load(bookJSONObject.getString("image")).into(imgBookImages)
                            txtBookNames.text = bookJSONObject.getString("name")
                            txtBookAuthors.text = bookJSONObject.getString("author")
                            txtBookPrices.text = bookJSONObject.getString("price")
                            txtBookRatings.text = bookJSONObject.getString("rating")
                            txtBookDesc.text = bookJSONObject.getString("description")

                            val bookEntity = BookEntity(
                                bookId?.toInt() as Int,
                                txtBookNames.text.toString(),
                                txtBookAuthors.text.toString(),
                                txtBookPrices.text.toString(),
                                txtBookRatings.text.toString(),
                                txtBookDesc.text.toString(),
                                bookImageUrl
                            )

                            val checkFav = DBAsyncTask(applicationContext,bookEntity,1).execute()
                            val isFav = checkFav.get()

                            if(isFav)
                            {
                                btnATF.text = "Remove from Favourites"
                                val favColor = ContextCompat.getColor(applicationContext,
                                    R.color.teal_200
                                )
                                btnATF.setBackgroundColor(favColor)
                            }else{
                                btnATF.text = "Add to Favourites"
                                val favColor = ContextCompat.getColor(applicationContext,
                                    R.color.colorFav
                                )
                                btnATF.setBackgroundColor(favColor)
                            }

                            btnATF.setOnClickListener {
                                if(!DBAsyncTask(applicationContext,bookEntity,1).execute().get())
                                {
                                    val async = DBAsyncTask(applicationContext,bookEntity,2).execute()
                                    val result = async.get()

                                    if(result)
                                    {
                                        Toast.makeText(this@DescriptionActivity,"Book added to favourites",Toast.LENGTH_SHORT).show()
                                        btnATF.text = "Remove to Favourites"
                                        val favColor = ContextCompat.getColor(applicationContext,
                                            R.color.teal_200
                                        )
                                        btnATF.setBackgroundColor(favColor)
                                    }else{
                                        Toast.makeText(this@DescriptionActivity,"Some error occurred !",Toast.LENGTH_SHORT).show()
                                    }

                                }else{
                                    val async = DBAsyncTask(applicationContext,bookEntity,3).execute()
                                    val result  = async.get()
                                    if(result)
                                    {
                                        Toast.makeText(this@DescriptionActivity,"Book removed from favourites",Toast.LENGTH_SHORT).show()
                                        btnATF.text = "Add to Favourites"
                                        val favColor = ContextCompat.getColor(applicationContext,
                                            R.color.colorFav
                                        )

                                        btnATF.setBackgroundColor(favColor)
                                    }else{
                                        Toast.makeText(this@DescriptionActivity,"Some error occurred !",Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }

                        } else {
                            Toast.makeText(
                                this@DescriptionActivity,
                                "Some error occurred !!!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@DescriptionActivity,
                            "Some error occurred !!!",
                            Toast.LENGTH_LONG
                        ).show()
                        println("====> ${e.toString()}")
                    }
                },
                    Response.ErrorListener {
                        Toast.makeText(
                            this@DescriptionActivity,
                            "Volley error occurred !!!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                ) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "ebadc263dd07df"
                        return headers
                    }
                }
            queue.add(jsonRequest)
        }else{
                val dialog = AlertDialog.Builder(this@DescriptionActivity)
                dialog.setTitle("Error")
                dialog.setMessage("Internet not Connection Found")
                dialog.setPositiveButton("Open Settings") {
                        text, listener ->
                    val setting = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(setting)
                    this@DescriptionActivity.finish()
                }
                dialog.setNegativeButton("Cancel") {
                        text, listenr ->
                    ActivityCompat.finishAffinity(this@DescriptionActivity)
                }
                dialog.create()
                dialog.show()
            }
    }

    class DBAsyncTask(val context: Context,val bookEntity: BookEntity,val mode:Int) : AsyncTask<Void,Void,Boolean>()
    {
        val db = Room.databaseBuilder(context,BookDatabase::class.java,"books-db").build()
        override fun doInBackground(vararg p0: Void?): Boolean {

            when(mode){
                1 ->{
                    // Check DB if the book is favourite or not
                    val book : BookEntity? = db.bookDao().getBookById(bookEntity.book_id.toString())
                    db.close()
                    return book != null
                }
                2->{
                    // Save the book into DB as favourite
                    db.bookDao().insertBook(bookEntity)
                    db.close()
                    return true
                }
                3->{
                    // Remove the favourite book
                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true
                }
            }
            return false
        }

    }

}