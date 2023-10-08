package com.asifddlks.starwarscharacterexplorer

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class StarWarsApplication : Application() {
    companion object {
        const val TAG = "StarWarsApplication"
    }
}
