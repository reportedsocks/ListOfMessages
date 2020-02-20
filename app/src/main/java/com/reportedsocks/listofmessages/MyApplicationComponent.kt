package com.reportedsocks.listofmessages

import android.app.Application
import com.reportedsocks.listofmessages.data.di.RetrofitModule
import com.reportedsocks.listofmessages.data.di.ViewModelModule
import com.reportedsocks.listofmessages.ui.main.MainFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component( modules = [RetrofitModule::class, ViewModelModule::class] )
interface ApplicationComponent{
    fun inject(fragment: MainFragment)

}

class MyApplicationComponent: Application() {
    val appComponent = DaggerApplicationComponent.create()
}