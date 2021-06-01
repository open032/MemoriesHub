package lex.neuron.memorieshub.ui.title.title

import android.content.ContentValues.TAG
import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import lex.neuron.memorieshub.data.RoomDao
import lex.neuron.memorieshub.data.entity.DirEntity
import lex.neuron.memorieshub.data.entity.TitleEntity

class TitleViewModel @ViewModelInject constructor(
    private val dao: RoomDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    private val eventChannel = Channel<TitleEvent>()

    val title = dao.getTitle().asLiveData()

    val titleEvent = eventChannel.receiveAsFlow()

    fun onSwiped(title: TitleEntity) = viewModelScope.launch {
        dao.delete(title)
    }

    fun onMenuClick() = viewModelScope.launch {
        eventChannel.send(TitleEvent.NavigateToBottomSheet)
    }

    fun onAddNewTitleClick() = viewModelScope.launch {
        eventChannel.send(TitleEvent.NavigateToAddScreen)
    }

    fun onLongTitleSelected(titleEntity: TitleEntity) = viewModelScope.launch {
        eventChannel.send(TitleEvent.NavigateToEditTitleScreen(titleEntity.id, titleEntity.name))

    }

    fun onTitleSelected(titleEntity: TitleEntity) = viewModelScope.launch {
        eventChannel.send(TitleEvent.NavigateToAnotherList(titleEntity.id, titleEntity.name))
    }

    sealed class TitleEvent {
        object NavigateToAddScreen : TitleEvent()
        object NavigateToBottomSheet : TitleEvent()
        data class NavigateToEditTitleScreen(val id: Int, val name: String) : TitleEvent()
        data class NavigateToAnotherList(val id: Int, val name: String) : TitleEvent()
    }
}
