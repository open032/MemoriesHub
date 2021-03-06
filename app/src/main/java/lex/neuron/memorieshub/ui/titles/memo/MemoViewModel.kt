package lex.neuron.memorieshub.ui.titles.memo

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import lex.neuron.memorieshub.data.RoomDao
import lex.neuron.memorieshub.data.entity.DeleteEntity
import lex.neuron.memorieshub.data.entity.MemoEntity
import lex.neuron.memorieshub.ui.firebase.crud.memotwocolumns.MemoTwoColumnsDelete
import javax.inject.Inject

@HiltViewModel
class MemoViewModel @Inject constructor(
    private val dao: RoomDao,
    private val state: SavedStateHandle
) : ViewModel() {

    private val eventChannel = Channel<MemoEvent>()

    private val id = state.get<Int>("id")
    private val name = state.get<String>("name")
    private var tcId = state.get<Int>("tcId") ?: id ?: "void tcId"
        set(value) {
            field = value
            state.set("tcId", value)
        }

    var titleName = state.get<String>("titleName") ?: name ?: "void"
        set(value) {
            field = value
            state.set("titleName", value)
        }
    val convertTcId: Int = tcId.toString().toInt()


    val memo = dao.getTeMemo(convertTcId).asLiveData()
    val memoEvent = eventChannel.receiveAsFlow()

    fun onSwipe(memo: MemoEntity, sendLaterNet: Boolean) = viewModelScope.launch {
        Log.d(TAG, "onSwipeMemoDel: $memo")
        Log.d(TAG, "onSwipeMemoDel: $sendLaterNet")

        dao.deleteMemo(memo)

        if (!sendLaterNet) {
            pendingDeletion()
            delay(200)
            val crud = MemoTwoColumnsDelete()
            crud.deleteMemoTwoColumns(memo)
        }
        if (sendLaterNet) {
            val deleteEntity = DeleteEntity("memo", memo.id, memo.titleList)
            dao.insertDelete(deleteEntity)
        }
    }

    private fun pendingDeletion() = viewModelScope.launch {
        dao.getDeleteByName("memo").collect { value ->
            val size = value.size - 1
            for (i in 0..size) {
                Log.d(TAG, "pendingDeletion: $value")
                val crud = MemoTwoColumnsDelete()
                val memo = MemoEntity(
                    value[i].secondId, "", false,
                    false, true, "", 0, value[i].id
                )
                crud.deleteMemoTwoColumns(memo)

            }
        }
    }

    fun arrowBack() = viewModelScope.launch {
        eventChannel.send(MemoEvent.NavigateBack)
    }

    fun addNewMemo() = viewModelScope.launch {
        eventChannel.send(MemoEvent.NavigateToAddScreen(convertTcId))
    }

    fun longClick(memoEntity: MemoEntity) = viewModelScope.launch {
        eventChannel.send(
            MemoEvent.NavigateToEditScreen(
                memoEntity.id,
                memoEntity.title, memoEntity.testable, memoEntity.description, memoEntity.titleList
            )
        )
    }

    fun checkMemo() = viewModelScope.launch {
        dao.deleteAllMemoFirebase()
    }

    sealed class MemoEvent {
        object NavigateBack : MemoEvent()
        data class NavigateToAddScreen(val id: Int) : MemoEvent()
        data class NavigateToEditScreen(
            val id: Int,
            val titleMemo: String,
            val testable: Boolean,
            val description: String,
            val idMemo: Int
        ) : MemoEvent()
    }
}