package com.jagan.bookhub.fragment

import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Request.Method.GET
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.jagan.bookhub.R
import com.jagan.bookhub.adapter.DashboardRecyclerAdapter
import com.jagan.bookhub.model.Book
import com.jagan.bookhub.util.ConnectionManager
import kotlinx.android.synthetic.main.fragment_dashboard.*
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Method
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap


class DashBoardFragment : Fragment() {

    lateinit var recyclerDashBoard: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager

    lateinit var recyclerAdapter: DashboardRecyclerAdapter

    // for progress bar
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout

    val bookInfoList = arrayListOf<Book>()

    val ratingComparator = Comparator<Book> { book1, book2 ->
        if(book1.bookRating.compareTo(book2.bookRating,true)==0){
            book1.bookName.compareTo(book2.bookName,true)
        }else{
            book1.bookRating.compareTo(book2.bookRating,true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        progressBar = view.findViewById(R.id.progressBar)
        progressLayout = view.findViewById(R.id.progressLayout)

        progressLayout.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE

        setHasOptionsMenu(true)

        recyclerDashBoard = view.findViewById(R.id.dashboardRecyclerView)

        layoutManager = LinearLayoutManager(activity)

        // ebadc263dd07df token for api

        val queue = Volley.newRequestQueue(activity as Context)

        val url = "http://13.235.250.119/v1/book/fetch_books/"

        if (ConnectionManager().checkConnectivity(activity as Context)) {

            try {
                progressLayout.visibility = View.GONE
                progressBar.visibility = View.GONE

            val jsonObjects = object : JsonObjectRequest(
                Request.Method.GET, url, null,
                Response.Listener {
                    //response

                    val success = it.getBoolean("success")
                    if (success) {
                            val data = it.getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val bookJsonObject = data.getJSONObject(i)
                                val bookObject = Book(
                                    bookJsonObject.getString("book_id"),
                                    bookJsonObject.getString("name"),
                                    bookJsonObject.getString("author"),
                                    bookJsonObject.getString("rating"),
                                    bookJsonObject.getString("price"),
                                    bookJsonObject.getString("image")
                                )
                                bookInfoList.add(bookObject)
                                //adapter
                                recyclerAdapter = DashboardRecyclerAdapter(activity as Context, bookInfoList)

                                recyclerDashBoard.adapter = recyclerAdapter

                                recyclerDashBoard.layoutManager = layoutManager

/*                                recyclerDashBoard.addItemDecoration(
                                    DividerItemDecoration(
                                        recyclerDashBoard.context,
                                        (layoutManager as LinearLayoutManager).orientation
                                    )
                                )*/
                            }


                    } else {
                        Toast.makeText(
                            activity as Context,
                            "Hello Welcome Buddy",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }

                    //println("Response - $it")

                }, Response.ErrorListener {
                    //error
                    //println("Error - $it")
                    if(activity != null) {
                        Toast.makeText(activity as Context, "Volly Error !!!!!", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "ebadc263dd07df"
                    return headers
                }
            }

            queue.add(jsonObjects)
                }catch (e:JSONException){
                Toast.makeText(activity as Context,"Some error has occurred !!!!!",Toast.LENGTH_LONG).show()
            }
        }else{
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet not Connection Found")
            dialog.setPositiveButton("Open Settings") {
                    text, listener ->
                val setting = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(setting)
                activity?.finish()
            }
            dialog.setNegativeButton("Cancel") {
                    text, listenr ->
                    ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_dashboard,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item?.itemId

        if(id==R.id.action_sort){
            Collections.sort(bookInfoList,ratingComparator)
            bookInfoList.reverse()
        }
        recyclerAdapter.notifyDataSetChanged()

        return super.onOptionsItemSelected(item)
    }

}