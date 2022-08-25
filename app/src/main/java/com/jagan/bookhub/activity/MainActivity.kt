package com.jagan.bookhub.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.jagan.bookhub.R
import com.jagan.bookhub.fragment.AboutFragment
import com.jagan.bookhub.fragment.DashBoardFragment
import com.jagan.bookhub.fragment.FavoritesFragment
import com.jagan.bookhub.fragment.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var previousMenuItem : MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Toast.makeText(this@MainActivity, "Hello Welcome Buddy", Toast.LENGTH_LONG).show()
        setUpToolBar()

        setDashboard()

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer

        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        // item in navigation drawer

        navigationView.setNavigationItemSelectedListener {

            if(previousMenuItem != null){
                previousMenuItem?.isChecked = false
            }

            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it

            when (it.itemId) {
                R.id.dashboard -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, DashBoardFragment()).addToBackStack("Dashboard")
                        .commit()
                    supportActionBar?.title = "Dashboard"
                    drawerLayout.closeDrawers()

                }
                R.id.favorites -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, FavoritesFragment())
                        .addToBackStack("Favorites")
                        .commit()
                    supportActionBar?.title = "Favorites"

                    drawerLayout.closeDrawers()

                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, ProfileFragment())
                        .addToBackStack("Profile")
                        .commit()
                    supportActionBar?.title = "Profile"

                    drawerLayout.closeDrawers()

                }
                R.id.about -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, AboutFragment())
                        .addToBackStack("About")
                        .commit()
                    supportActionBar?.title = "About"

                    drawerLayout.closeDrawers()

                }
            }

            return@setNavigationItemSelectedListener true
        }
    }

    fun setUpToolBar() {
        setSupportActionBar(toolBar)
        supportActionBar?.title = "Book Hub"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navigationView.setCheckedItem(R.id.dashboard)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        return super.onOptionsItemSelected(item)
    }

    fun setDashboard() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, DashBoardFragment()).addToBackStack("Dashboard")
            .commit()
        supportActionBar?.title = "Dashboard"
    }

    override fun onBackPressed() {

        val frag = supportFragmentManager.findFragmentById(R.id.frameLayout)

        when (frag) {
            !is DashBoardFragment -> setDashboard()
            else -> super.onBackPressed()
        }

    }

}

