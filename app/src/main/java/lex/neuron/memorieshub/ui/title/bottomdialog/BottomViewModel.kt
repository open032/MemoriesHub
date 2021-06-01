package lex.neuron.memorieshub.ui.title.bottomdialog

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import lex.neuron.memorieshub.data.RoomDao

class BottomViewModel @ViewModelInject constructor(
    val memoDao: RoomDao
) : ViewModel() {

    val memo = memoDao.getDir().asLiveData()

}
