package black.old.spacedrepetitionowl

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import black.old.spacedrepetitionowl.models.Subject
import kotlinx.android.synthetic.main.fragment_subject.view.*
import black.old.spacedrepetitionowl.SubjectFragment.OnListFragmentInteractionListener

class SubjectRecyclerViewAdapter(
    val subjects: List<Subject>,
    val listener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<SubjectRecyclerViewAdapter.ViewHolder>() {
    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as Subject
            // TODO("This still uses the DummyItem, replace it with actual stuff")
            //listener?.onListFragmentInteraction(item)
        }

    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val contentView: TextView = view.content
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_subject, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = subjects.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentSubject = subjects[position]
        // TODO("Fill in data into the items inside fragment_subject.xml")
        holder.contentView.text = currentSubject.content
        with(holder.view) {
            // TODO("Set click listener here?")

        }
    }
}