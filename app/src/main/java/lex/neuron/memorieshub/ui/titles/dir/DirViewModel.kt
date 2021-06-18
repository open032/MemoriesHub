package lex.neuron.memorieshub.ui.titles.dir

import android.content.ContentValues.TAG
import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import lex.neuron.memorieshub.data.RoomDao
import lex.neuron.memorieshub.data.entity.DirEntity
import lex.neuron.memorieshub.data.entity.TitleEntity
import lex.neuron.memorieshub.ui.firebase.crud.dir.DirDelete
import lex.neuron.memorieshub.ui.firebase.crud.dir.MemoForDelete

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

    fun titleList(dir: DirEntity) = viewModelScope.launch {
        dao.getDirByTe(dir.id).collect { value ->
            val size = value.size - 1
            for (i in 0..size) {
                memoList(value[i].id)
                Log.e(TAG, "titleList: ${value[i].dirList}")
                listTitle.add(value[i].id)
            }
        }
    }

    private fun memoList(id: Int) = viewModelScope.launch {
        Log.e(TAG, "memoList: $id", )
        dao.getTeMemo(id).collect { value ->
            val size = value.size - 1
            for (i in 0..size) {
                listMemo.add(MemoForDelete(id, value[i].id))
            }
        }
    }

    fun onSwiped(dirEntity: DirEntity) = viewModelScope.launch {
        delay(150)
        dao.deleteDir(dirEntity)
        val crud = DirDelete()
        crud.deleteDir(dirEntity, listTitle, listMemo)
    }

    fun onClick(dirEntity: DirEntity) = viewModelScope.launch {
        eventChannel.send(DirEvent.NavigateToTitleList(dirEntity.id))

        /*dao.getDirTitle(dirEntity.id).collect { value ->
            val size = value.size
            for (i in 0..size - 1) {
                Log.e(TAG, "Item Title: ${value[i].id}")
            }
//            Log.e(TAG, "onClickAddDir: $value")
//            Log.e(TAG, "onClickAddDir: ${value.get(value.size - 1)}")

        }*/
    }

    fun onLongClick(dirEntity: DirEntity) = viewModelScope.launch {
        eventChannel.send(DirEvent.NavigateToEditTitleDir(dirEntity.id, dirEntity.name))
    }

    fun addNewItem() = viewModelScope.launch {
        eventChannel.send(DirEvent.NavigateToAddDir)
    }



    sealed class DirEvent {
        object NavigateToAddDir : DirEvent()
        data class NavigateToTitleList(val id: Int) : DirEvent()
        data class NavigateToEditTitleDir(val id: Int, val name: String) : DirEvent()
    }
}
