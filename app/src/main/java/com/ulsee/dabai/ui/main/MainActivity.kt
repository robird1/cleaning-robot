package com.ulsee.dabai.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.ulsee.dabai.data.LoginDataSource
import com.ulsee.dabai.data.LoginRepository
import com.ulsee.dabai.ui.login.LoginActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.ulsee.dabai.R
import com.ulsee.dabai.ui.robot_local.CreateMapActivity
import com.ulsee.dabai.ui.robot_local.LocalMapListActivity

class MainActivity : AppCompatActivity() {

    public var projectID: Int = 0
    private lateinit var loginRepository: LoginRepository
    private lateinit var toolbar: Toolbar
    private lateinit var drawerToggle: ActionBarDrawerToggle
    lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        projectID = intent.getIntExtra("project-id", 0)

        loginRepository = LoginRepository(
            dataSource = LoginDataSource(
                url = "https://120.78.217.167:5200/v1/"
            )
        )

        initNavigation()
    }

    fun initNavigation() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        val bottomNavView: BottomNavigationView = findViewById(R.id.nav_view_bottom)

        val navController = findNavController(R.id.nav_host_fragment)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view_drawer)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_robot, R.id.navigation_map, R.id.navigation_task
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        bottomNavView.setupWithNavController(navController)

        navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_create_map -> startActivity(Intent(this, CreateMapActivity::class.java))
                R.id.nav_robot_map -> startActivity(Intent(this, LocalMapListActivity::class.java))
                R.id.nav_logout -> logout()
            }
            return@setNavigationItemSelectedListener false
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun logout() {
        GlobalScope.launch {
            loginRepository.logout()
            finish()
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }
    }
}