package com.example.androiddomain
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.androiddomain.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener{
    private lateinit var binding: ActivityMainBinding
    var actionBarDrawerToggle : ActionBarDrawerToggle ? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        actionBarDrawerToggle = ActionBarDrawerToggle(this, binding.drawerLayout,binding.toolbar,R.string.open, R.string.close)

        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle!!)
        actionBarDrawerToggle!!.syncState()
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        binding.navigationView.setNavigationItemSelectedListener (this)


        val navController = findNavController(R.id.fragment)

        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.rateUs -> {
                Toast.makeText(this, "Rate Us", Toast.LENGTH_SHORT).show()
            }
            R.id.termsCondition -> {
                Toast.makeText(this, "termsCondition", Toast.LENGTH_SHORT).show()
            }
            R.id.privacyPolicy -> {
                Toast.makeText(this, "privacyPolicy", Toast.LENGTH_SHORT).show()
            }
            R.id.devloper -> {
                Toast.makeText(this, "devloper", Toast.LENGTH_SHORT).show()
            }
            R.id.favourite -> {
                Toast.makeText(this, "favourite", Toast.LENGTH_SHORT).show()
            }
            R.id.shareApp -> {
                Toast.makeText(this, "shareApp", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if(actionBarDrawerToggle!!.onOptionsItemSelected(item)){
            true
        }else
            super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.close()
        }
    }
}