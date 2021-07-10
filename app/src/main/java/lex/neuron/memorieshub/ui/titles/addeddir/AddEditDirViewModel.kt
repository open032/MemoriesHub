package lex.neuron.memorieshub.ui.titles.addeddir

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import lex.neuron.memorieshub.data.RoomDao
import lex.neuron.memorieshub.data.entity.DirEntity
import lex.neuron.memorieshub.permission.internet.TAG
import lex.neuron.memorieshub.ui.firebase.crud.dir.DirCreate
import lex.neuron.memorieshub.ui.firebase.crud.dir.DirUpdate
import lex.neuron.memorieshub.ui.firebase.crud.read.AccountData
import lex.neuron.memorieshub.ui.firebase.crud.read.ReadFirebase

class AddEditDirViewModel @ViewModelInject constructor(
    private val dao: RoomDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    private val mId = state.get<Int>("id")
    private val name = state.get<String>("name")
    private var sizeNet: Int = 0

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

    fun onSaveClick(name: String, sendLaterNet: Boolean) {
        if (mId != -1) {
            changeDir(dirID as Int, name, sendLaterNet)
        } else {
            createDir(name, sendLaterNet)
        }
    }

    private val addEditEventChannel = Channel<AddEditEvent>()
    val addEditEvent = addEditEventChannel.receiveAsFlow()

    private fun changeDir(dirID: Int, etName: String, sendLaterNet: Boolean) =
        viewModelScope.launch {

            val dirEntity: DirEntity = dao.getDirById(dirID)
            val updateDirEntity: DirEntity =
                dirEntity.copy(name = etName, sendNetCreateUpdate = sendLaterNet)
            Log.d(TAG, "changeDir: $updateDirEntity")
            dao.updateDir(updateDirEntity)

            if (!sendLaterNet) {
                checkForCompliance()
                delay(200)
                val crud = DirUpdate()
                crud.updateDir(updateDirEntity)
//                Log.e(TAG, "changeDir: $updateDirEntity")
            }
            addEditEventChannel.send(AddEditEvent.NavigateBack)
        }

    private fun createDir(etName: String, sendLaterNet: Boolean) = viewModelScope.launch {
        Log.e(TAG, "createDir: ######### $sendLaterNet")
        val newNameDir = DirEntity(name = etName, sendNetCreateUpdate = sendLaterNet)
        val id = dao.insertDir(newNameDir)
        Log.d(TAG, "AddEditDirViewModel createDir: hasNet = $sendLaterNet")

        // when has internet
        if (!sendLaterNet) {
            checkForCompliance()
            delay(200)
            val crud = DirCreate()
            crud.createDir(newNameDir, id)
            Log.d(TAG, "AddEditDirViewModel createDir2: if hasNet ")
        }
        addEditEventChannel.send(AddEditEvent.NavigateBack)
    }

    private fun checkForCompliance() = viewModelScope.launch {
        dao.getDirByBool(bol = true).collect { value ->
            val size = value.size - 1
            for (i in 0..size) {
//                Log.e(TAG, "checkForCompliance: $value")
                Log.d(TAG, "AddEditDirViewModel checkForCompliance: $value")
                val crud = DirCreate()
                crud.createDir(value[i], value[i].id.toLong())
                val dir = DirEntity(
                    name = value[i].name, sendNetCreateUpdate = false,
                    created = value[i].created, id = value[i].id
                )
                dao.updateDir(dir)
            }
        }
    }

    fun showLog() = viewModelScope.launch {
        val crud = AccountData()
        crud.getAccountData()
    }

    sealed class AddEditEvent {
        object NavigateBack : AddEditEvent()
    }
}