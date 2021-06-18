package lex.neuron.memorieshub.ui.titles.memo

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
import lex.neuron.memorieshub.ui.firebase.crud.memotwocolumns.MemoTwoColumnsDelete

class MemoViewModel @ViewModelInject constructor(
    private val dao: RoomDao,
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


    val memo = dao.getTeMemo(convertTcId).asLiveData()
    val memoEvent = eventChannel.receiveAsFlow()

    fun onSwipe(memo: MemoEntity) = viewModelScope.launch {
        dao.deleteMemo(memo)
        val crud = MemoTwoColumnsDelete()
        crud.deleteMemoTwoColumns(memo)
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
                memoEntity.title, memoEntity.description, memoEntity.titleList
            )
        )
    }

    sealed class MemoEvent {
        object NavigateBack : MemoEvent()
        data class NavigateToAddScreen(val id: Int) : MemoEvent()
        data class NavigateToEditScreen(
            val id: Int,
            val titleMemo: String,
            val description: String,
            val idMemo: Int
        ) : MemoEvent()
    }
}