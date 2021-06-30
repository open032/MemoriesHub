package lex.neuron.memorieshub.ui.titles.title

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
import lex.neuron.memorieshub.data.entity.DeleteEntity
import lex.neuron.memorieshub.data.entity.MemoEntity
import lex.neuron.memorieshub.data.entity.TitleEntity
import lex.neuron.memorieshub.ui.firebase.crud.memotwocolumns.MemoTwoColumnsDelete
import lex.neuron.memorieshub.ui.firebase.crud.title.OnlyTitleDelete
import lex.neuron.memorieshub.ui.firebase.crud.title.TitleDelete

class TitleViewModel @ViewModelInject constructor(
    private val dao: RoomDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {
    private val eventChannel = Channel<TitleEvent>()
    private var list: MutableList<Int> = ArrayList()

    private val id = state.get<Int>("id")
    private var dirId = state.get<Int>("dirId") ?: id ?: 1
        set(value) {
            field = value
            state.set("dirId", value)
        }
    val convertDirId = dirId.toString().toInt()
    val title = dao.getDirByTe(convertDirId).asLiveData()

    val titleEvent = eventChannel.receiveAsFlow()

    fun memoList(titleEntity: TitleEntity) = viewModelScope.launch {
        dao.getTeMemo(titleEntity.id).collect { value ->
            val size = value.size - 1
            for (i in 0..size) {
                list.add(value[i].id)
                val deleteEntity = DeleteEntity("memo", value[i].id, value[i].titleList)
                dao.insertDelete(deleteEntity)
            }
        }
    }

    fun onSwiped(title: TitleEntity, sendLaterNet: Boolean) = viewModelScope.launch {
        Log.d(TAG, "onSwipedTitle: $title")
        Log.d(TAG, "onSwipedTitle: $sendLaterNet")
        delay(150)
        dao.deleteTe(title)

        if (!sendLaterNet) {
            pendingDeletionTitle()
            delay(200)
            pendingDeletionMemo()
            delay(200)
            val crud = TitleDelete()
            crud.deleteTitle(title, list)

        }
        if (sendLaterNet) {
            delay(200)
            val deleteEntity = DeleteEntity("title", title.id, title.dirList)
            dao.insertDelete(deleteEntity)
        }
    }

    private fun pendingDeletionTitle() = viewModelScope.launch {
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

    private fun pendingDeletionMemo() = viewModelScope.launch {
        dao.getDeleteByName("memo").collect { value ->
            val size = value.size - 1
            for (i in 0..size) {
                Log.d(lex.neuron.memorieshub.permission.internet.TAG, "pendingDeletion: $value")
                val crud = MemoTwoColumnsDelete()
                val memo = MemoEntity(
                    value[i].secondId, "", false,
                    false, "", 0, value[i].id
                )
                crud.deleteMemoTwoColumns(memo)

            }
        }
    }

    fun onAddNewTitleClick() = viewModelScope.launch {
        eventChannel.send(TitleEvent.NavigateToAddScreen(dirId))
    }

    fun onLongTitleSelected(titleEntity: TitleEntity) = viewModelScope.launch {
        eventChannel.send(TitleEvent.NavigateToEditTitleScreen(titleEntity.id, titleEntity.name))

    }

    fun onTitleSelected(titleEntity: TitleEntity) = viewModelScope.launch {
        eventChannel.send(TitleEvent.NavigateToAnotherList(titleEntity.id, titleEntity.name))
        Log.e(TAG, "onTitleSelected: ${titleEntity.id}")
    }

    fun arrowBack() = viewModelScope.launch {
        eventChannel.send(TitleEvent.NavigateBack)
    }


    sealed class TitleEvent {
        object NavigateBack : TitleEvent()
        object NavigateToBottomSheet : TitleEvent()
        data class NavigateToAddScreen(val id: Int) : TitleEvent()
        data class NavigateToEditTitleScreen(val id: Int, val name: String) : TitleEvent()
        data class NavigateToAnotherList(val id: Int, val name: String) : TitleEvent()
    }
}
