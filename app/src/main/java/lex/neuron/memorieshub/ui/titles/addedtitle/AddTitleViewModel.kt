package lex.neuron.memorieshub.ui.titles.addedtitle

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import lex.neuron.memorieshub.data.RoomDao
import lex.neuron.memorieshub.data.entity.TitleEntity
import lex.neuron.memorieshub.ui.firebase.crud.title.TitleCreate
import lex.neuron.memorieshub.ui.firebase.crud.title.TitleUpdate
import javax.inject.Inject

@HiltViewModel
class AddTitleViewModel @Inject constructor(
    private val dao: RoomDao,
    private val state: SavedStateHandle
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

    fun onSaveClick(name: String, sendLaterNet: Boolean) {
        if (titleName.isEmpty()) {
            createTitle(name, sendLaterNet)
        } else {
            changeTitle(id, name, sendLaterNet)
        }
    }

    private val addEditTitleEventChannel = Channel<AddEditTitleEvent>()
    val addEditTitleEvent = addEditTitleEventChannel.receiveAsFlow()

    private fun changeTitle(titleID: Int, etName: String, sendLaterNet: Boolean) =
        viewModelScope.launch {
            Log.d(TAG, "changeTitle: $sendLaterNet")
            val titleEntity: TitleEntity = dao.getTeById(titleID)
            val updateTitle: TitleEntity =
                titleEntity.copy(name = etName, sendNetCreateUpdate = sendLaterNet)
            dao.updateTe(updateTitle)
            Log.d(TAG, "changeTitle: $updateTitle")

            if (!sendLaterNet) {
                Log.d(TAG, "!sendLaterNet: $sendLaterNet")
                checkForCompliance()
                delay(200)
                val crud = TitleUpdate()
                crud.updateTitle(updateTitle)
            }
            addEditTitleEventChannel.send(AddEditTitleEvent.NavigateBack)
        }

    private fun createTitle(etName: String, sendLaterNet: Boolean) = viewModelScope.launch {
        Log.d(TAG, "createTitleTitle: $sendLaterNet")
        Log.d(TAG, "createTitleTitle: $etName")
        val newTitle = TitleEntity(dirList = id, name = if (etName.isEmpty()) " " else etName,
            sendNetCreateUpdate = sendLaterNet)
        val idTitle = dao.insertTe(newTitle)

        if (!sendLaterNet) {
            checkForCompliance()
            delay(200)
            val crud = TitleCreate()
            crud.createTitle(newTitle, idTitle, id)
        }
        addEditTitleEventChannel.send(AddEditTitleEvent.NavigateBack)
    }

    private fun checkForCompliance() = viewModelScope.launch {
        dao.getTitleByBool(bol = true).collect { value ->
            val size = value.size - 1
            for (i in 0..size) {
                Log.d(TAG, "checkForComplianceTitle: $value")

                val crud = TitleCreate()
                crud.createTitle(value[i], value[i].id.toLong(), value[i].dirList)
                val title = TitleEntity(
                    dirList = value[i].dirList, name = value[i].name,
                    sendNetCreateUpdate = false, created = value[i].created,
                    id = value[i].id
                )
                dao.updateTe(title)
            }
        }
    }

    sealed class AddEditTitleEvent {
        object NavigateBack : AddEditTitleEvent()
    }
}
