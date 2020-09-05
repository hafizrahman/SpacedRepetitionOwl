package black.old.spacedrepetitionowl

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import black.old.spacedrepetitionowl.viewmodels.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_subject_url_bottom_dialog.view.*

class SubjectURLBottomDialogFragment : BottomSheetDialogFragment() {
    val args: SubjectURLBottomDialogFragmentArgs by navArgs()
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(
            R.layout.fragment_subject_url_bottom_dialog,
            container,
            false)

        // The ViewModel is already created on the Activity level (inside MainActivity.kt),
        // so here we are using the Activity's context
        mainViewModel = ViewModelProvider(activity!!).get(MainViewModel::class.java)

        view.subject_url_bottom_dialog_url.setText(args.subjectUrl)

        view.subject_url_bottom_dialog_open.setOnClickListener { view ->
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(args.subjectUrl)
            startActivity(intent)
        }

        return view
    }

    override fun onCancel(dialog: DialogInterface) {
        // Save the URL if the URL is changed
        if(view?.subject_url_bottom_dialog_url?.text.toString() != args.subjectUrl) {
            Log.d("hafiz", "lets save the URL here")
            mainViewModel.updateSubjectUrl(
                args.subjectId,
                view?.subject_url_bottom_dialog_url?.text.toString()
            )
        }
        super.onCancel(dialog)
    }
}