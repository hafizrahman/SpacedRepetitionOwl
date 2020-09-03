package black.old.spacedrepetitionowl

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_subject_note_edit.view.*
import kotlinx.android.synthetic.main.fragment_subject_url_bottom_dialog.view.*

/**
 * A simple [Fragment] subclass.
 * Use the [SubjectNoteEditFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SubjectNoteEditFragment : Fragment() {
    val args: SubjectNoteEditFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(
            R.layout.fragment_subject_note_edit,
            container,
            false)

        view.subject_note_edit.setText(args.subjectNote)

        return view
    }
}