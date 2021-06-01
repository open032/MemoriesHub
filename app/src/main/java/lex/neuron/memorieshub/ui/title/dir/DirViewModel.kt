package lex.neuron.memorieshub.ui.title.dir

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import lex.neuron.memorieshub.data.RoomDao

class DirViewModel @ViewModelInject constructor(
    val memoDao: RoomDao
) : ViewModel() {

    val memo = memoDao.getDir().asLiveData()

}
