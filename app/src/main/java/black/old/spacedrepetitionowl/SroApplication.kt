package black.old.spacedrepetitionowl

import android.app.Application
import com.facebook.stetho.Stetho

class SroApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Required initialization logic here!
        Stetho.initializeWithDefaults(this)
    }
}