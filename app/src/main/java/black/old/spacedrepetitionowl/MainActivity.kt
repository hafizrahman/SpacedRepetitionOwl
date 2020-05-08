package black.old.spacedrepetitionowl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import black.old.spacedrepetitionowl.dummy.DummyContent
import black.old.spacedrepetitionowl.viewmodels.MainViewModel

class MainActivity : AppCompatActivity(), SubjectFragment.OnListFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        lateinit var mainViewModel: MainViewModel
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // create ViewModel at the Activity level.
        // the fragment (SubjectFragment in this case) can then re-use this ViewModel by
        // setting the ViewModel() parameter to getActivity()
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onListFragmentInteraction(item: DummyContent.DummyItem?) {
        // TODO: Actually do something onListFragmentInteraction
    }
}
