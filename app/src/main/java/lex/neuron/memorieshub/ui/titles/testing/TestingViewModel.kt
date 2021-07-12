package lex.neuron.memorieshub.ui.titles.testing

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
import lex.neuron.memorieshub.permission.internet.TAG
import lex.neuron.memorieshub.ui.titles.title.TitleViewModel

class TestingViewModel @ViewModelInject constructor(
    private val dao: RoomDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {
    private val eventChannel = Channel<TestingEvent>()
    var listTesting: MutableList<TestingList> = ArrayList()
    var listRight: MutableList<TestingList> = ArrayList()

    private val id = state.get<Int>("id")
    private var titleId = state.get<Int>("titleId") ?: id ?: 1
        set(value) {
            field = value
            state.set("titleId", value)
        }
    val tId = titleId.toString().toInt()

    val getTeMemo = dao.getTeMemo(tId).asLiveData()

    val testingEvent = eventChannel.receiveAsFlow()

    fun leftList(memo: List<MemoEntity>): MutableList<TestingList> {
//        Log.d(TAG, "leftList1: $memo")
        for (i in 0..memo.size - 1) {
            listTesting.add(TestingList(memo[i].title, memo[i].id))
        }
        return listTesting
    }

    fun rightList(memo: List<MemoEntity>): MutableList<TestingList> {
        for (i in 0..memo.size - 1) {
            listRight.add(TestingList(memo[i].description, memo[i].id))
        }
        return listRight
    }

    fun pair(left: Int, right: Int): Boolean {
        if (left == right) {
//            Log.d(TAG, "pair: true")
            return true
        } else {
//            Log.d(TAG, "pair: false")
            return false
        }
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
