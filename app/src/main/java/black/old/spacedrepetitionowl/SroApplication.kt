package black.old.spacedrepetitionowl

import android.app.Application
import android.content.res.Resources
import com.facebook.stetho.Stetho

class SroApplication : Application() {
    lateinit var res: Resources

    override fun onCreate() {
        super.onCreate()
        // Required initialization logic here!
        Stetho.initializeWithDefaults(this)
        res = resources
    }

}