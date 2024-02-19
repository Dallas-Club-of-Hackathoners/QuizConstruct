package com.stu.navigation.navigation

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.stu.android.ARG_SCREEN
import com.stu.navigation.DestinationsProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * Stack and tabs navigation based on Navigation Component library
 */
class NavComponentRouter @AssistedInject constructor(
    @Assisted @IdRes private val fragmentContainerId: Int,
    private val destinationsProvider: DestinationsProvider,
    private val activity: FragmentActivity,
) {

    private var currentStartDestination: Int? = null

    fun onNavigateUp(): Boolean {
        // ideally you should check whether there are back handlers or not
        // and if so -> call this:
        //   return getRootNavController().navigateUp()
        pop()
        return true
    }

    fun onSaveInstanceState(bundle: Bundle) {
        currentStartDestination?.let {
            bundle.putInt(KEY_START_DESTINATION, it)
        }
    }

    fun onRestoreInstanceState(bundle: Bundle) {
        currentStartDestination = bundle.getInt(KEY_START_DESTINATION, 0)
    }

    fun launchMain(@IdRes initialDestinationId: Int) {
        currentStartDestination?.let {
            getRootNavController().navigate(
                resId = initialDestinationId,
                args = null,
                navOptions {
                    popUpTo(it) {
                        inclusive = true
                    }
                }
            )
        } ?: restoreRoot()

        currentStartDestination = initialDestinationId
    }

    fun launch(@IdRes destinationId: Int, args: Parcelable? = null) {
        if (args == null) {
            getRootNavController().navigate(resId = destinationId)
        } else {
            getRootNavController().navigate(
                resId = destinationId,
                args = Bundle().apply {
                    putParcelable(ARG_SCREEN, args)
                }
            )
        }
    }

    fun pop() {
        activity.onBackPressedDispatcher.onBackPressed()
    }

    fun getCurrentStartDestination(): Int? {
        return currentStartDestination
    }

    private fun restoreRoot() {
        val graph = getRootNavController().navInflater.inflate(destinationsProvider.provideNavigationGraphId())
        graph.setStartDestination(destinationsProvider.provideStartDestinationId())
        getRootNavController().graph = graph
    }

    private fun getRootNavController(): NavController {
        val fragmentManager = activity.supportFragmentManager
        val navHost = fragmentManager.findFragmentById(fragmentContainerId) as NavHostFragment
        return navHost.navController
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @IdRes fragmentContainerId: Int,
        ): NavComponentRouter
    }

    private companion object {
        const val KEY_START_DESTINATION = "startDestination"
    }
}