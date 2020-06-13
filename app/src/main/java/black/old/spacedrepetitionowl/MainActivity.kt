package black.old.spacedrepetitionowl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import black.old.spacedrepetitionowl.dummy.DummyContent
import black.old.spacedrepetitionowl.viewmodels.MainViewModel
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), SubjectFragment.OnListFragmentInteractionListener {
    lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration


    override fun onCreate(savedInstanceState: Bundle?) {
        lateinit var mainViewModel: MainViewModel
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById<DrawerLayout>(R.id.main_drawer_layout)
        val navView = findViewById<NavigationView>(R.id.main_navigation_view)
        val sroToolbar = findViewById<Toolbar>(R.id.main_toolbar)
        val navController = findNavController(R.id.nav_host_fragment)

        // Setup ALL the thing
        navView.setupWithNavController(navController)
        setSupportActionBar(sroToolbar)

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.subjectFragment, R.id.subjectTimelineFragment, R.id.aboutFragment, R.id.settingsFragment),
            drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)


        // create ViewModel at the Activity level.
        // the fragment (SubjectFragment in this case) can then re-use this ViewModel by
        // setting the ViewModel() parameter to getActivity()
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // Setup notification channel
        NotificationHelper.createNotificationChannel(
            this,
            NotificationManagerCompat.IMPORTANCE_HIGH,
            false,
            //getString(R.string.app_name),
            "SRO CHANNEL NAME IS HERE",
            "Spaced Repetition Owl notification channel."
        )
    }

    override fun onListFragmentInteraction(item: DummyContent.DummyItem?) {
        // TODO: Actually do something onListFragmentInteraction
    }

    // Necessary to make the hamburger icon show the drawer when tapped.
    // Thanks Carson at https://stackoverflow.com/a/54447011 for the hint.
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        // need to use appBarConfiguration as parameter here so that the custom top level
        // destinations are obeyed, and for the drawer to show up correctly when hamburger icon
        // is tapped on the non-home top level destinations.
        // See also:
        // https://stackoverflow.com/q/62102479/884836
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
