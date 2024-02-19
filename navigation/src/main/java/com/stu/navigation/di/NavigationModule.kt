package com.stu.navigation.di

import com.stu.android.ActivityRequired
import com.stu.navigation.GlobalNavComponentRouter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
class NavigationModule {

    @Provides
    @IntoSet
    fun provideRouterAsActivityRequired(
        router: GlobalNavComponentRouter,
    ): ActivityRequired {
        return router
    }

}