package lex.neuron.memorieshub.ui.title.memo

import android.content.ContentValues.TAG
import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import lex.neuron.memorieshub.data.RoomDao
import lex.neuron.memorieshub.data.entity.MemoEntity

class MemoViewModel @ViewModelInject constructor(
    private val memoDao: RoomDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    private val eventChannel = Channel<MemoEvent>()

    private val id = state.get<Int>("id")
    private var tcId = state.get<Int>("tcId") ?: id ?: "void tcId"
        set(value) {
            field = value
            state.set("tcId", value)
        }
    val convertTcId: Int = tcId.toString().toInt()


    val memo = memoDao.getTlMemo(convertTcId).asLiveData()
    val memoEvent = eventChannel.receiveAsFlow()

    fun onSwipe(memo: MemoEntity) = viewModelScope.launch {
        memoDao.deleteMemo(memo)
    }

    fun addNewMemo() = viewModelScope.launch {
        eventChannel.send(MemoEvent.NavigateToAddScreen(convertTcId))
    }

    fun longClick(memoEntity: MemoEntity) = viewModelScope.launch {
        Log.e(TAG, "longClick: ")
        eventChannel.send(
            MemoEvent.NavigateToEditScreen(
                memoEntity.id,
                memoEntity.title, memoEntity.description, memoEntity.titleList
            )
        )
    }

    sealed class MemoEvent {
        data class NavigateToAddScreen(val id: Int) : MemoEvent()
        data class NavigateToEditScreen(
            val id: Int,
            val titleMemo: String,
            val description: String,
            val idMemo: Int
        ) : MemoEvent()
    }
}