package com.stu.navigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.stu.android.ActivityRequired
import com.stu.navigation.databinding.ActivityMainBinding
import com.stu.navigation.navigation.NavComponentRouter
import com.stu.navigation.navigation.RouterHolder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), RouterHolder {

    @Inject
    lateinit var navComponentRouterFactory: NavComponentRouter.Factory

    @Inject
    lateinit var destinationsProvider: DestinationsProvider

    @Inject
    lateinit var activityRequiredSet: Set<@JvmSuppressWildcards ActivityRequired>

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val navComponentRouter by lazy(LazyThreadSafetyMode.NONE) {
        navComponentRouterFactory.create(R.id.fragmentContainer)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (savedInstanceState == null)
            navComponentRouter.launchMain(destinationsProvider.provideStartDestinationId())

        activityRequiredSet.forEach {
            it.onCreated(this)
        }

        val menu = binding.bottomNavigationView.menu
        destinationsProvider.provideMainTabs().forEach { tab ->
            val menuItem = menu.add(0, tab.destinationId, Menu.NONE, tab.title)
            menuItem.setIcon(tab.iconRes)
        }
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as  NavHostFragment
        val navController = navHost.navController
        val graph = navController.navInflater.inflate(destinationsProvider.provideNavigationGraphId())
        graph.setStartDestination(navComponentRouter.getCurrentStartDestination() ?: destinationsProvider.provideStartDestinationId())
        navController.graph = graph
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return (navComponentRouter.onNavigateUp()) || super.onSupportNavigateUp()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        navComponentRouter.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        navComponentRouter.onRestoreInstanceState(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        activityRequiredSet.forEach { it.onStarted() }
    }

    override fun onStop() {
        super.onStop()
        activityRequiredSet.forEach { it.onStopped() }
    }

    override fun onDestroy() {
        super.onDestroy()
        activityRequiredSet.forEach { it.onDestroyed() }
    }

    override fun requireRouter(): NavComponentRouter {
        return navComponentRouter
    }

}