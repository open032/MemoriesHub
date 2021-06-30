package lex.neuron.memorieshub.ui.titles.addedmemo

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import lex.neuron.memorieshub.data.RoomDao
import lex.neuron.memorieshub.data.entity.MemoEntity
import lex.neuron.memorieshub.permission.internet.TAG
import lex.neuron.memorieshub.ui.firebase.crud.memotwocolumns.MemoTwoColumnsCreate
import lex.neuron.memorieshub.ui.firebase.crud.memotwocolumns.MemoTwoColumnsUpdate

class AddEditMemoViewModel @ViewModelInject constructor(
    private val dao: RoomDao,
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

    fun onSaveClick(fabTitle: String, fabDesc: String, sendLaterNet: Boolean) {
        if (desc.isEmpty()) {
            createMemo(fabTitle, fabDesc, sendLaterNet)
        } else {
            changeMemo(fabTitle, fabDesc, sendLaterNet)
        }
    }

    private fun changeMemo(fabTitle: String, fabDesc: String, sendLaterNet: Boolean) =
        viewModelScope.launch {
            val memoEntity: MemoEntity = dao.getMemoById(id)
            val updateMemo: MemoEntity = memoEntity.copy(
                titleList = idTl, title = fabTitle, sendNetCreateUpdate = true,
                description = fabDesc, id = id
            )
            /*Log.d(TAG, "updateMemo: $updateMemoEntity")
           Log.d(TAG, "updateMemo: $sendLaterNet")*/
            Log.e(TAG, "updateMemo: $sendLaterNet")
            dao.updateMemo(updateMemo)

            // if has internet
            if (!sendLaterNet) {
                Log.e(TAG, "!sendLaterNet: $sendLaterNet")
                checkForCompliance()
                delay(200)
                val crud = MemoTwoColumnsUpdate()
                crud.updateMemoTwoColumns(updateMemo)
            }
            addEditTitleEventChannel.send(AddEditMemoEvent.NavigationBack)

//        updateMemo(updateMemoEntity, sendLaterNet)
        }


    private fun createMemo(fabTitle: String, fabDesc: String, sendLaterNet: Boolean) =
        viewModelScope.launch {
            val newMemo = MemoEntity(
                titleList = id, title = fabTitle, description = fabDesc,
                sendNetCreateUpdate = sendLaterNet
            )
            val idMemo = dao.insertMemo(newMemo)
            Log.d(TAG, "createMemo: $newMemo")

            if (!sendLaterNet) {
                checkForCompliance()
                delay(200)
                val crud = MemoTwoColumnsCreate()
                crud.createMemoTwoColumn(newMemo, idMemo, id)
                Log.d(TAG, "createMemo: $newMemo, $sendLaterNet")

            }
            addEditTitleEventChannel.send(AddEditMemoEvent.NavigationBack)
        }

    private fun checkForCompliance() = viewModelScope.launch {
        dao.getMemoByBool(bol = true).collect { value ->
            val size = value.size - 1
            for (i in 0..size) {
                Log.d(TAG, "checkForComplianceMemo: $value")

                val crud = MemoTwoColumnsCreate()
                crud.createMemoTwoColumn(value[i], value[i].id.toLong(), value[i].titleList)
                val memo = MemoEntity(
                    titleList = value[i].titleList, title = value[i].title,
                    sendNetCreateUpdate = false,
                    description = value[i].description,
                    created = value[i].created, id = value[i].id
                )
                dao.updateMemo(memo)
            }
        }
    }

    private fun updateMemo(updateMemoEntity: MemoEntity, sendLaterNet: Boolean) =
        viewModelScope.launch {
            /*Log.d(TAG, "updateMemo: $updateMemoEntity")
            Log.d(TAG, "updateMemo: $sendLaterNet")*/
            Log.e(TAG, "updateMemo: $sendLaterNet")
            dao.updateMemo(updateMemoEntity)

            if (!sendLaterNet) {
                checkForCompliance()
                delay(200)
                val crud = MemoTwoColumnsUpdate()
                crud.updateMemoTwoColumns(updateMemoEntity)
            }
            addEditTitleEventChannel.send(AddEditMemoEvent.NavigationBack)
        }

    sealed class AddEditMemoEvent {
        object NavigationBack : AddEditMemoEvent()
    }
}