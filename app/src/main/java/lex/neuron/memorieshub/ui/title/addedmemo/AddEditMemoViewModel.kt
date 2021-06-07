package lex.neuron.memorieshub.ui.title.addedmemo

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
import lex.neuron.memorieshub.data.entity.MemoEntity

class AddEditMemoViewModel @ViewModelInject constructor(
    private val roomDao: RoomDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {
    private val mId = state.get<Int>("id")
    private val mTitle = state.get<String>("titleMemo")
    private val mDesc = state.get<String>("description")
    private val mIdMemo = state.get<Int>("idmemo")

    private val addEditTitleEventChannel = Channel<AddEditMemoEvent>()
    val addEditMemoEvent = addEditTitleEventChannel.receiveAsFlow()

    var idMemo = state.get<Int>("memoId") ?: mId ?: "void memoId"
        set(value) {
            field = value
            state.set("memoId", value)
        }
    var titleMemo = state.get<String>("nameMemo") ?: mTitle ?: "void nameMemo"
        set(value) {
            field = value
            state.set("nameMemo", value)
        }
    var descriptionMemo = state.get<String>("descriptionMemo") ?: mDesc ?: "void desc"
        set(value) {
            field = value
            state.set("descriptionMemo", value)
        }
    var tIdMemo = state.get<Int>("tIdMemo") ?: mIdMemo ?: "void tIdMemo"
        set(value) {
            field = value
            state.set("tIdMemo", value)
        }
    val id = idMemo.toString().toInt()
    val title = titleMemo.toString()
    val desc = descriptionMemo.toString()
    val idTl = tIdMemo.toString().toInt()

    fun onSaveClick(fabTitle: String, fabDesc: String) {
        if (desc.isEmpty()) {
            createMemo(fabTitle, fabDesc)
        } else {
            changeMemo(fabTitle, fabDesc)
        }
    }

    private fun changeMemo(fabTitle: String, fabDesc: String) = viewModelScope.launch {
        val memoEntity: MemoEntity = roomDao.getMemoById(id)
        val updateMemoEntity: MemoEntity = memoEntity.copy(titleList = idTl, title = fabTitle,
            description = fabDesc, id = id)
        updateMemo(updateMemoEntity)
    }


    private fun createMemo(fabTitle: String, fabDesc: String) = viewModelScope.launch {
        val newMemo =
            MemoEntity(titleList = id, title = fabTitle, description = fabDesc)
        roomDao.insertMemo(newMemo)
        addEditTitleEventChannel.send(AddEditMemoEvent.NavigationBack)
    }

    private fun updateMemo(updateMemoEntity: MemoEntity) = viewModelScope.launch {
        roomDao.updateMemo(updateMemoEntity)
        addEditTitleEventChannel.send(AddEditMemoEvent.NavigationBack)
    }

    sealed class AddEditMemoEvent {
        object NavigationBack : AddEditMemoEvent()
    }
}