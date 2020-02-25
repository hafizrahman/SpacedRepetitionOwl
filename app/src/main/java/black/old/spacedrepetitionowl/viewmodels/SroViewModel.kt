package black.old.spacedrepetitionowl.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import black.old.spacedrepetitionowl.models.Subject
import black.old.spacedrepetitionowl.repositories.SroRepository
import kotlinx.coroutines.launch

class SroViewModel(application: Application) : AndroidViewModel(application) {
    val sroRepository = SroRepository(application)

    fun insertSubject(subject: Subject) = viewModelScope.launch {
        sroRepository.insertSubject(subject)
    }

}