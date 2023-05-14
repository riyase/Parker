package com.keepqueue.sparepark

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.keepqueue.sparepark.data.Prefs
import com.keepqueue.sparepark.data.response.Result
import com.google.android.material.navigation.NavigationView
import com.keepqueue.sparepark.ui.login.LoginActivity
import com.keepqueue.sparepark.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: com.keepqueue.sparepark.MainActivityViewModel
    lateinit var loading: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loading = ProgressDialog(this)
        loading.setMessage("Wait...")
        viewModel = ViewModelProvider(this)[com.keepqueue.sparepark.MainActivityViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        setUpDrawer(drawerLayout, navView)
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home/*, R.id.nav_owner, R.id.nav_bookings, R.id.maps*/
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        //navView.setupWithNavController(navController)
        binding.appBarMain.contentMain.bnvHome.setupWithNavController(navController)

        viewModel.logoutResult.observe(this) { result ->
            when(result) {
                is Result.Success -> {
                    Prefs.setLoggedIn(this@MainActivity, false, -1, "")
                    loading.dismiss()
                    setDrawerMenuVisibility(binding.navView)
                }
                is Result.Error -> {
                    loading.dismiss()
                    result.message?.let { message ->
                        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                    }
                }
                else -> loading.show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setDrawerMenuVisibility(binding.navView)
    }

    private fun setUpDrawer(drawer: DrawerLayout, navView: NavigationView) {

        setDrawerMenuVisibility(navView)

        navView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { menuItem ->
            val id = menuItem.itemId
            //it's possible to do more actions on several items, if there is a large amount of items I prefer switch(){case} instead of if()
            if (id == R.id.nav_login) {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
            } else if (id == R.id.nav_logout) {
                showSignOut()
            }
            //This is for maintaining the behavior of the Navigation view
            //onNavDestinationSelected(menuItem, navController)
            //This is for closing the drawer after acting on it
            drawer.closeDrawer(GravityCompat.START)
            true
        })
    }

    private fun setDrawerMenuVisibility(navView: NavigationView) {
        val isLoggedIn = Prefs.isLoggedIn(this)
        if (isLoggedIn) {
            binding.appBarMain.contentMain.bnvHome.visibility = View.VISIBLE
            navView.menu.findItem(R.id.nav_login).isVisible = false
            navView.menu.findItem(R.id.nav_logout).isVisible = true
        } else {
            binding.appBarMain.contentMain.bnvHome.visibility = View.GONE
            navView.menu.findItem(R.id.nav_login).isVisible = true
            navView.menu.findItem(R.id.nav_logout).isVisible = false
        }
    }

    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        //menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.action_signout) {
            val adb: AlertDialog.Builder = AlertDialog.Builder(this)
            adb.setTitle("SignOut")
            adb.setMessage("Are you sure to Sign Out?")

            adb.setPositiveButton("Sign Out", DialogInterface.OnClickListener { dialog, which ->
                //Toast.makeText(this@MainActivity, "Ok clicked!", Toast.LENGTH_SHORT).show()
                viewModel.logout()
            })
            adb.setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, which -> {
                    Toast.makeText(this@MainActivity, "Cancel clicked!", Toast.LENGTH_SHORT).show()
                } })
            adb.show()
        }
        return super.onOptionsItemSelected(item)
    }*/

    private fun showSignOut() {
        val adb: AlertDialog.Builder = AlertDialog.Builder(this)
        adb.setTitle("SignOut")
        adb.setMessage("Are you sure to Sign Out?")

        adb.setPositiveButton("Sign Out", DialogInterface.OnClickListener { dialog, which ->
            //Toast.makeText(this@MainActivity, "Ok clicked!", Toast.LENGTH_SHORT).show()
            viewModel.logout()
        })
        adb.setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog, which -> {
                Toast.makeText(this@MainActivity, "Cancel clicked!", Toast.LENGTH_SHORT).show()
            } })
        adb.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}