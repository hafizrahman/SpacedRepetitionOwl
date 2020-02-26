package black.old.spacedrepetitionowl.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import black.old.spacedrepetitionowl.models.Subject
import black.old.spacedrepetitionowl.repositories.SroRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val sroRepository = SroRepository(application)

    // TODO: This part needs some logic added to generate and insert Reminders based on Subject date
    fun insertSubject(subject: Subject) = viewModelScope.launch {
        sroRepository.insertSubject(subject)
    }

    fun getSubjects(): LiveData<List<Subject>>? {
        return sroRepository.getSubjects()
    }
}