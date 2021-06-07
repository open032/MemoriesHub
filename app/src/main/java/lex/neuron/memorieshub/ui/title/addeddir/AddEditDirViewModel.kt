package lex.neuron.memorieshub.ui.title.addeddir

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import lex.neuron.memorieshub.data.RoomDao
import lex.neuron.memorieshub.data.entity.DirEntity

class AddEditDirViewModel @ViewModelInject constructor(
    private val dao: RoomDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    private val mId = state.get<Int>("id")
    private val name = state.get<String>("name")

    var dirID = state.get<Int>("dirID") ?: mId ?: "void id"
        set(value) {
            field = value
            state.set("dirID", value)
        }
    var dirName = state.get<String>("dirName") ?: name ?: "void name"
        set(value) {
            field = value
            state.set("dirName", value)
        }
    val id = dirID.toString().toInt()

    fun onSaveClick(name: String) {
        if (mId != -1) {
            changeDir(dirID as Int, name)
        } else {
            createDir(name)
        }
    }

    private val addEditEventChannel = Channel<AddEditEvent>()
    val addEditEvent = addEditEventChannel.receiveAsFlow()

    private fun changeDir(dirID: Int, etName: String) = viewModelScope.launch {

        val dirEntity: DirEntity = dao.getDirById(dirID)
        val updateDirEntity: DirEntity = dirEntity.copy(name = etName)

        updateDir(updateDirEntity)
    }

    private fun createDir(etName: String) = viewModelScope.launch {
        val newNameDir = DirEntity(name = etName)
        dao.insertDir(newNameDir)
        addEditEventChannel.send(AddEditEvent.NavigateBack)
    }

    private fun updateDir(updateDirEntity: DirEntity) = viewModelScope.launch {
        dao.updateDir(updateDirEntity)
        addEditEventChannel.send(AddEditEvent.NavigateBack)
    }


    sealed class AddEditEvent {
        object NavigateBack : AddEditEvent()
    }
}