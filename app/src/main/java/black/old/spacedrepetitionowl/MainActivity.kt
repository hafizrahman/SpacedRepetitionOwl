package black.old.spacedrepetitionowl

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
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

        // Color the status bar
        // see: https://stackoverflow.com/a/54686103
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
    }

    override fun onListFragmentInteraction(item: DummyContent.DummyItem?) {
        // TODO: Actually do something onListFragmentInteraction
    }

    // Necessary to make the hamburger icon show the drawer when tapped.
    // Thanks Carson at https://stackoverflow.com/a/54447011 for the hint.

    // It's also used to handle up button pressing in the Subject Note Edit fragment.
    // In this use case, if the app see note changes then user presses back or up button, it has to
    // ask whether user wants to save or not. The back button action is handled in
    // SubjectNoteEditFragment.kt, however that does not affect Up button, so we need to add
    // that here. See also https://stackoverflow.com/a/55930024
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)

        // Handle Up button on Subject Note Edit fragment.
        // We already have the algorithm for this in SubjectNoteEditFragment's back button
        // callback, so here we just need to call that.
        if(navController.currentDestination?.id == R.id.subjectNoteEditFragment) {
            onBackPressedDispatcher.onBackPressed() // See https://stackoverflow.com/a/55905155
            // Returning true here because the actual going back/up action is already handled in
            // the back button callback as well.
            return true
        }

        // need to use appBarConfiguration as parameter here so that the custom top level
        // destinations are obeyed, and for the drawer to show up correctly when hamburger icon
        // is tapped on the non-home top level destinations.
        // See also:
        // https://stackoverflow.com/q/62102479/884836
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }



}
