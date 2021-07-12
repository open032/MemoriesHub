package lex.neuron.memorieshub.ui.titles.dir

import android.content.ContentValues.TAG
import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import lex.neuron.memorieshub.data.RoomDao
import lex.neuron.memorieshub.data.entity.DeleteEntity
import lex.neuron.memorieshub.data.entity.DirEntity
import lex.neuron.memorieshub.data.entity.MemoEntity
import lex.neuron.memorieshub.data.entity.TitleEntity
import lex.neuron.memorieshub.ui.firebase.crud.dir.DirCreate
import lex.neuron.memorieshub.ui.firebase.crud.dir.DirDelete
import lex.neuron.memorieshub.ui.firebase.crud.dir.MemoForDelete
import lex.neuron.memorieshub.ui.firebase.crud.dir.OnlyDirDelete
import lex.neuron.memorieshub.ui.firebase.crud.memotwocolumns.MemoTwoColumnsDelete
import lex.neuron.memorieshub.ui.firebase.crud.title.OnlyTitleDelete
import lex.neuron.memorieshub.util.AUTH

class DirViewModel @ViewModelInject constructor(
    private val dao: RoomDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {
    private val eventChannel = Channel<DirEvent>()
    private var listTitle: MutableList<Int> = ArrayList()
    private var listMemo: MutableList<MemoForDelete> = ArrayList()




    private val id = state.get<Int>("id")
    var dirId = state.get<Int>("dirId") ?: id ?: 1
        set(value) {
            field = value
            state.set("dirId", value)
        }

    val dir = dao.getDir().asLiveData()
    val dirEvent = eventChannel.receiveAsFlow()

    /* fun showDir() = viewModelScope.launch {
         dao.getDir().collect { value ->
             Log.e(TAG, "showDir: $value")
         }
     }*/

    // List to remove nested elements
    fun titleList(dir: DirEntity) = viewModelScope.launch {
        dao.getDirByTe(dir.id).collect { value ->
            val size = value.size - 1
            for (i in 0..size) {
                memoList(value[i].id)
                Log.e(TAG, "titleList: ${value[i].dirList}")
                listTitle.add(value[i].id)
                val dirEntity = DeleteEntity("title", value[i].id, value[i].dirList)
                dao.insertDelete(dirEntity)
            }
        }
    }

    private fun memoList(id: Int) = viewModelScope.launch {
        Log.e(TAG, "memoList: $id")
        dao.getTeMemo(id).collect { value ->
            val size = value.size - 1
            for (i in 0..size) {
                listMemo.add(MemoForDelete(id, value[i].id))
                val deleteEntity = DeleteEntity("memo", value[i].id, value[i].titleList)
                dao.insertDelete(deleteEntity)
            }
        }
    }

    fun deleteItem(dir: DirEntity, sendLaterNet: Boolean) = viewModelScope.launch {
        Log.d(TAG, "onSwipedDir: $dir")
        Log.d(TAG, "onSwipedDir: $sendLaterNet")
        delay(150)
        dao.deleteDir(dir)

        if (!sendLaterNet) {
            Log.d(TAG, "!sendLaterNetDir: ")
            pendingDeletionDir()
            delay(200)
            pendingDeletionTitle()
            delay(200)
            pendingDeletionMemo()
            delay(200)
            val crud = DirDelete()
            crud.deleteDir(dir, listTitle, listMemo)
        }
        if (sendLaterNet) {
            Log.d(TAG, "sendLaterNet: ")
            delay(200)
            val deleteEntity = DeleteEntity("dir", dir.id)
            dao.insertDelete(deleteEntity)
        }
    }

    private fun pendingDeletionMemo() = viewModelScope.launch {
        dao.getDeleteByName("memo").collect { value ->
            val size = value.size - 1
            for (i in 0..size) {
                Log.d(lex.neuron.memorieshub.permission.internet.TAG, "pendingDeletion: $value")
                val crud = MemoTwoColumnsDelete()
                val memo = MemoEntity(
                    value[i].secondId, "", false,
                    false,true, "", 0, value[i].id
                )
                crud.deleteMemoTwoColumns(memo)

            }
        }
    }

    private fun pendingDeletionTitle() = viewModelScope.launch {
        Log.d(TAG, "pendingDeletionTitle: ")
        dao.getDeleteByName("title").collect { value ->
            val size = value.size - 1
            for (i in 0..size) {
                Log.d(TAG, "pendingDeletionTitle: $value")
                val crud = OnlyTitleDelete()
                val title = TitleEntity(
                    dirList = value[i].secondId, "", false,
                    false, 0, value[i].id
                )
                crud.deleteTitle(title)
            }
        }
    }

    private fun pendingDeletionDir() = viewModelScope.launch {
        dao.getDeleteByName("dir").collect { value ->
            val size = value.size - 1
            for (i in 0..size) {
                Log.d(TAG, "pendingDeletionDir: $value")
                val crud = OnlyDirDelete()
                val dir = DirEntity(
                    name = "", false, false, 0, value[i].id
                )
                crud.deleteDir(dir)
            }
        }
    }

    fun onClick(dirEntity: DirEntity) = viewModelScope.launch {
        eventChannel.send(DirEvent.NavigateToTitleList(dirEntity.id))

    }

    fun renameItem(dirEntity: DirEntity) = viewModelScope.launch {
        eventChannel.send(DirEvent.NavigateToEditTitleDir(dirEntity.id, dirEntity.name))
    }

    fun addNewItem() = viewModelScope.launch {

        Log.d(TAG, "addNewItem: ")
//        checkForCompliance()
        eventChannel.send(DirEvent.NavigateToAddDir)
    }


    fun authSignOut() = viewModelScope.launch {
        AUTH.signOut()
        eventChannel.send(DirEvent.NavigateToLogIn)
    }

    fun deleteAllRoom() = viewModelScope.launch {
        dao.deleteAllMemo()
        dao.deleteAllTitle()
        dao.deleteAllDir()
    }

    /*private fun checkForCompliance() = viewModelScope.launch {
        dao.getDirByBool(bol = false).collect { value ->
            val size = value.size - 1
            for (i in 0..size) {
                Log.e(lex.neuron.memorieshub.permission.internet.TAG, "checkForCompliance: $value")
                Log.d(lex.neuron.memorieshub.permission.internet.TAG, "AddEditDirViewModel checkForCompliance: $value")
                val crud = DirCreate()
                crud.createDir(value[i], value[i].id.toLong())
                val dir = DirEntity(name = value[i].name, hasNet = true,
                    created = value[i].created, id = value[i].id )
                dao.updateDir(dir)
            }
        }
    }*/


    sealed class DirEvent {
        object NavigateToAddDir : DirEvent()
        object NavigateToLogIn : DirEvent()
        data class NavigateToTitleList(val id: Int) : DirEvent()
        data class NavigateToEditTitleDir(val id: Int, val name: String) : DirEvent()
    }
}
