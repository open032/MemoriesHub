package lex.neuron.memorieshub.ui.title.deletedir

import android.content.ContentValues.TAG
import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import lex.neuron.memorieshub.data.RoomDao
import lex.neuron.memorieshub.data.entity.DirEntity
import java.util.*

class DeleteDirViewModel @ViewModelInject constructor(
    private val dao: RoomDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {
    private val id = state.get<Int>("id")
    private var dirId = state.get<Int>("dirId") ?: id ?: -5
        set(value) {
            field = value
            state.set("dirId", value)
        }



    fun deleteDir() = viewModelScope.launch {
        Log.e(TAG, "deleteDirId: $dirId" )
        dao.deleteDirById(dirId)
    }
}
