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
    private val dao: RoomDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    private val mId = state.get<Int>("id")
    private val name = state.get<String>("name")

    var titleID = state.get<Int>("titleID") ?: mId ?: "void id"
        set(value) {
            field = value
            state.set("titleID", value)
        }
    var titleName = state.get<String>("titleName") ?: name ?: "void name"
        set(value) {
            field = value
            state.set("titleName", value)
        }
    val id = titleID.toString().toInt()

    fun onSaveClick(name: String) {
        if(titleName.isEmpty()) {
            createTitle(name)
        } else {
            changeTitle(id, name)
        }
    }

      private val addEditTitleEventChannel = Channel<AddEditTitleEvent>()
      val addEditTitleEvent = addEditTitleEventChannel.receiveAsFlow()

    private fun changeTitle(titleID: Int, etName: String) = viewModelScope.launch {

        val titleEntity: TitleEntity = dao.getTeById(titleID)
        val updateTitleEntity: TitleEntity = titleEntity.copy(name = etName)

        updateTitle(updateTitleEntity)
    }

    private fun createTitle(etName: String) = viewModelScope.launch {
        val newTitle = TitleEntity(dirList = id, name = etName)
        dao.insertTe(newTitle)
        addEditTitleEventChannel.send(AddEditTitleEvent.NavigateBack)
        Log.e(TAG, "createTitle: $id", )
    }

    private fun updateTitle(updateTitleEntity: TitleEntity) = viewModelScope.launch {
        dao.updateTe(updateTitleEntity)
        addEditTitleEventChannel.send(AddEditTitleEvent.NavigateBack)
    }


    sealed class AddEditTitleEvent {
        object NavigateBack : AddEditTitleEvent()
    }
}
