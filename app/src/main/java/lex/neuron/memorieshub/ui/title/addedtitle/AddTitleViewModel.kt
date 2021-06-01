package lex.neuron.memorieshub.ui.title.addedtitle

import android.content.ContentValues.TAG
import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import lex.neuron.memorieshub.data.RoomDao
import lex.neuron.memorieshub.data.entity.TitleEntity

class AddTitleViewModel @ViewModelInject constructor(
    private val roomDao: RoomDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    private val id = state.get<Int>("id")
    private val name = state.get<String>("name")

    var titleID = state.get<Int>("titleID") ?: id ?: "void id"
        set(value) {
            field = value
            state.set("titleID", value)
        }
    var titleName = state.get<String>("titleName") ?: name ?: "void name"
        set(value) {
            field = value
            state.set("titleName", value)
        }

    fun onSaveClick(name: String) {
        if (id != -1) {
            changeTitle(titleID as Int, name)
        } else {
            createTitle(name)
        }
    }

      private val addEditTitleEventChannel = Channel<AddEditTitleEvent>()
      val addEditTitleEvent = addEditTitleEventChannel.receiveAsFlow()

    private fun changeTitle(titleID: Int, etName: String) = viewModelScope.launch {

        val titleEntity: TitleEntity = roomDao.getById(titleID)
        val updateTitleEntity: TitleEntity = titleEntity.copy(name = etName)

        updateTitle(updateTitleEntity)
    }

    private fun createTitle(etName: String) = viewModelScope.launch {
        val newTitle = TitleEntity(name = etName)
        roomDao.insert(newTitle)
        addEditTitleEventChannel.send(AddEditTitleEvent.NavigateBack)
    }

    private fun updateTitle(updateTitleEntity: TitleEntity) = viewModelScope.launch {
        roomDao.update(updateTitleEntity)
        addEditTitleEventChannel.send(AddEditTitleEvent.NavigateBack)
    }


    sealed class AddEditTitleEvent {
        object NavigateBack : AddEditTitleEvent()
    }
}
