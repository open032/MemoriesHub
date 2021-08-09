package lex.neuron.memorieshub.ui.titles.testing

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import lex.neuron.memorieshub.data.RoomDao
import javax.inject.Inject

@HiltViewModel
class TestingViewModel @Inject constructor(
    dao: RoomDao,
    private val state: SavedStateHandle
) : ViewModel() {
    private val eventChannel = Channel<TestingEvent>()

    private val id = state.get<Int>("id")
    private var titleId = state.get<Int>("titleId") ?: id ?: 1
        set(value) {
            field = value
            state.set("titleId", value)
        }
    val tId = titleId.toString().toInt()

    val getTeMemo = dao.getTeMemo(tId).asLiveData()

    val testingEvent = eventChannel.receiveAsFlow()


    fun pair(left: Int, right: Int): Boolean {
        return left == right
        return false
    }

    fun backStack(listLeftIt: MutableList<TestingList>) = viewModelScope.launch {
        if (listLeftIt.size == 0) {
            eventChannel.send(TestingEvent.NavigateBack)
        }
    }

    fun randomList(size: Int): MutableList<Int> {
        var listRandom: MutableList<Int> = ArrayList()
        var randomFirst = (0..size - 1).random()

        listRandom.add(randomFirst)


        while (listRandom.size != size) {
            var random = (0..size - 1).random()

            var bol = true
            for (i in 0..listRandom.size - 1) {
                if (random == listRandom[i]) {
                    bol = false
                }
            }
            if (bol) {
                listRandom.add(random)
            }
        }
        return listRandom
    }

    sealed class TestingEvent {
        object NavigateBack : TestingEvent()
    }

}
