package black.old.spacedrepetitionowl

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import black.old.spacedrepetitionowl.dummy.DummyContent

class MainActivity : AppCompatActivity(), SubjectFragment.OnListFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onListFragmentInteraction(item: DummyContent.DummyItem?) {

    }
}
