package lex.neuron.memorieshub.ui.title.dir

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
import lex.neuron.memorieshub.data.MyDatabase
import lex.neuron.memorieshub.data.RoomDao
import lex.neuron.memorieshub.data.entity.DirEntity
import lex.neuron.memorieshub.data.entity.TitleEntity
import lex.neuron.memorieshub.di.AppModule

class DirViewModel @ViewModelInject constructor(
    private val dao: RoomDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {
    private val eventChannel = Channel<DirEvent>()

    private val id = state.get<Int>("id")
    var dirId = state.get<Int>("dirId") ?: id ?: 1
            set(value) {
                field = value
                state.set("dirId", value)
            }


    val dir = dao.getDir().asLiveData()
    val dirEvent = eventChannel.receiveAsFlow()


    fun onSwiped(dirEntity: DirEntity) = viewModelScope.launch {
        dao.deleteDir(dirEntity)
    }
    fun onClick(dirEntity: DirEntity) = viewModelScope.launch {
        eventChannel.send(DirEvent.NavigateToTitleList(dirEntity.id))
    }

    fun onLongClick(dirEntity: DirEntity) = viewModelScope.launch {
        eventChannel.send(DirEvent.NavigateToEditTitleDir(dirEntity.id, dirEntity.name))
    }

    fun addNewItem() = viewModelScope.launch {
        eventChannel.send(DirEvent.NavigateToAddDir)
    }

    /*fun onSwiped(dirEntity: DirEntity) = viewModelScope.launch {
        dao.deleteDir(dirEntity)
    }
*/
    sealed class DirEvent {
        object NavigateToAddDir : DirEvent()
        data class NavigateToTitleList(val id: Int) : DirEvent()
        data class NavigateToEditTitleDir(val id: Int, val name: String) : DirEvent()
    }
}
