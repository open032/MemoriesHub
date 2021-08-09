package lex.neuron.memorieshub.ui.titles.dir

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import lex.neuron.memorieshub.data.RoomDao
import lex.neuron.memorieshub.data.entity.*
import lex.neuron.memorieshub.ui.firebase.crud.dir.DirDelete
import lex.neuron.memorieshub.ui.firebase.crud.dir.MemoForDelete
import lex.neuron.memorieshub.ui.firebase.crud.dir.OnlyDirDelete
import lex.neuron.memorieshub.ui.firebase.crud.memotwocolumns.MemoTwoColumnsDelete
import lex.neuron.memorieshub.ui.firebase.crud.title.OnlyTitleDelete
import lex.neuron.memorieshub.util.AUTH
import lex.neuron.memorieshub.util.DIR
import lex.neuron.memorieshub.util.MEMO_TWO_COLUMNS
import lex.neuron.memorieshub.util.TITLE
import javax.inject.Inject

@HiltViewModel
class DirViewModel @Inject constructor(
    private val dao: RoomDao,
) : ViewModel() {
    private val eventChannel = Channel<DirEvent>()
    private var listTitle: MutableList<Int> = ArrayList()
    private var listMemo: MutableList<MemoForDelete> = ArrayList()

    val dir = dao.getDir().asLiveData()
    val dirEvent = eventChannel.receiveAsFlow()


    fun showList() = viewModelScope.launch {
        dao.getDir()
    }

    // List to remove nested elements
    fun titleList(dir: DirEntity) = viewModelScope.launch {
        dao.getDirByTe(dir.id).collect { value ->
            val size = value.size - 1
            for (i in 0..size) {
                memoList(value[i].id)
                Log.e(TAG, "titleList: ${value[i].dirList}")
                listTitle.add(value[i].id)
                val dirEntity = DeleteEntity("title", value[i].id, value[i].dirList)
                dao.insertDelete(dirEntity)
            }
        }
    }

    private fun memoList(id: Int) = viewModelScope.launch {
        Log.e(TAG, "memoList: $id")
        dao.getTeMemo(id).collect { value ->
            val size = value.size - 1
            for (i in 0..size) {
                listMemo.add(MemoForDelete(id, value[i].id))
                val deleteEntity = DeleteEntity("memo", value[i].id, value[i].titleList)
                dao.insertDelete(deleteEntity)
            }
        }
    }

    fun deleteItem(dir: DirEntity, sendLaterNet: Boolean) = viewModelScope.launch {
        Log.d(TAG, "onSwipedDir: $dir")
        Log.d(TAG, "onSwipedDir: $sendLaterNet")
        delay(150)
        dao.deleteDir(dir)

        if (!sendLaterNet) {
            Log.d(TAG, "!sendLaterNetDir: ")
            pendingDeletionDir()
            delay(200)
            pendingDeletionTitle()
            delay(200)
            pendingDeletionMemo()
            delay(200)
            val crud = DirDelete()
            crud.deleteDir(dir, listTitle, listMemo)
        }
        if (sendLaterNet) {
            Log.d(TAG, "sendLaterNet: ")
            delay(200)
            val deleteEntity = DeleteEntity("dir", dir.id)
            dao.insertDelete(deleteEntity)
        }
    }

    private fun pendingDeletionMemo() = viewModelScope.launch {
        dao.getDeleteByName("memo").collect { value ->
            val size = value.size - 1
            for (i in 0..size) {
                Log.d(TAG, "pendingDeletion: $value")
                val crud = MemoTwoColumnsDelete()
                val memo = MemoEntity(
                    value[i].secondId, "", false,
                    false, true, "", 0, value[i].id
                )
                crud.deleteMemoTwoColumns(memo)

            }
        }
    }

    private fun pendingDeletionTitle() = viewModelScope.launch {
        Log.d(TAG, "pendingDeletionTitle: ")
        dao.getDeleteByName("title").collect { value ->
            val size = value.size - 1
            for (i in 0..size) {
                Log.d(TAG, "pendingDeletionTitle: $value")
                val crud = OnlyTitleDelete()
                val title = TitleEntity(
                    dirList = value[i].secondId, "", false,
                    false, 0, value[i].id
                )
                crud.deleteTitle(title)
            }
        }
    }

    private fun pendingDeletionDir() = viewModelScope.launch {
        dao.getDeleteByName("dir").collect { value ->
            val size = value.size - 1
            for (i in 0..size) {
                Log.d(TAG, "pendingDeletionDir: $value")
                val crud = OnlyDirDelete()
                val dir = DirEntity(
                    name = "", false, false, 0, value[i].id
                )
                crud.deleteDir(dir)
            }
        }
    }

    fun onClick(dirEntity: DirEntity) = viewModelScope.launch {
        checkTitle()
        eventChannel.send(DirEvent.NavigateToTitleList(dirEntity.id, dirEntity.name))

    }

    private fun checkTitle() = viewModelScope.launch {
        dao.getTeFirebase().collect() { value ->
            if (value.isEmpty()) {
                Log.d(TAG, "onClick: empty TitleFromFirebase")
            } else {
                val size = value.size - 1
                for (i in 0..size) {
                    var name = " "
                    name = if (value[i].name == "") {
                        " "
                    } else {
                        value[i].name
                    }

                    val newTitle = TitleEntity(
                        dirList = value[i].dirList,
                        name = name,
                        sendNetCreateUpdate = false,
                        sendNetDelete = false,
                        created = 0,
                        id = value[i].id
                    )

                    createTitle(newTitle)
                }
            }
        }
    }

    fun renameItem(dirEntity: DirEntity) = viewModelScope.launch {
        eventChannel.send(DirEvent.NavigateToEditTitleDir(dirEntity.id, dirEntity.name))
    }

    fun addNewItem() = viewModelScope.launch {

        Log.d(TAG, "addNewItem: ")
        eventChannel.send(DirEvent.NavigateToAddDir)
    }


    fun authSignOut() = viewModelScope.launch {
        AUTH.signOut()
        eventChannel.send(DirEvent.NavigateToLogIn)
    }


    fun deleteAllRoom(sendLaterNet: Boolean) = viewModelScope.launch {
        if (!sendLaterNet) {
            dao.deleteAllMemo()
            dao.deleteAllTitle()
            dao.deleteAllDir()
            Log.e(TAG, "if net: ${Thread.currentThread().name}")
        }
    }


    fun createMemoFromFirebase() {
        val uid = AUTH.currentUser?.uid.toString()
        var ref = FirebaseDatabase.getInstance().getReference(MEMO_TWO_COLUMNS).child(uid)
        val menuListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds: DataSnapshot in dataSnapshot.children) {
                    var title = " "
                    var description = " "
                    title = if (ds.child("name").value.toString() == "") {
                        " "
                    } else {
                        ds.child("name").value.toString()
                    }
                    description = if (ds.child("description").value.toString() == "") {
                        " "
                    } else {
                        ds.child("description").value.toString()
                    }
                    val newMemo = MemoFromFirebase(
                        titleList = ds.child("idParent").value.toString().toInt(),
                        title = title,
                        testable = ds.child("testable").value.toString().toBoolean(),
                        sendNetCreateUpdate = false,
                        sendNetDelete = false,
                        description = description,
                        created = 0,
                        id = ds.child("id").value.toString().toInt()
                    )
                    Log.d(TAG, "onDataChange: $newMemo")


                    createMemoFromFirebase(newMemo)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        ref.addListenerForSingleValueEvent(menuListener)
    }

    fun createTitleFromFirebase() {
        val uid = AUTH.currentUser?.uid.toString()
        var ref = FirebaseDatabase.getInstance().getReference(TITLE).child(uid)
        val menuListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds: DataSnapshot in dataSnapshot.children) {
                    Log.d(TAG, "* * * * * ${ds.child("idParent")} ")

                    var name = " "
                    name = if (ds.child("name").value.toString() == "") {
                        " "
                    } else {
                        ds.child("name").value.toString()
                    }

                    val newTitle = TitleFromFirebase(
                        dirList = ds.child("idParent").value.toString().toInt(),
                        name = name,
                        sendNetCreateUpdate = false,
                        sendNetDelete = false,
                        created = 0,
                        id = ds.child("id").value.toString().toInt()
                    )
                    createNewTitleFromFirebase(newTitle)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        }
        ref.addListenerForSingleValueEvent(menuListener)
    }


    fun createDir() {
        val uid = AUTH.currentUser?.uid.toString()
        var ref = FirebaseDatabase.getInstance().getReference(DIR).child(uid)
        val menuListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds: DataSnapshot in dataSnapshot.children) {


                    var name = " "
                    name = if (ds.child("name").value.toString() == "") {
                        " "
                    } else {
                        ds.child("name").value.toString()
                    }

                    val newNameDir = DirEntity(
                        name = name,
                        sendNetCreateUpdate = false,
                        sendNetDelete = false,
                        created = 0,
                        id = ds.child("id").value.toString().toInt()
                    )
                    createDir(newNameDir)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        ref.addListenerForSingleValueEvent(menuListener)
    }

    fun createTitle(newTitle: TitleEntity) = viewModelScope.launch {
        val id = dao.insertTe(newTitle)
    }

    private fun createNewTitleFromFirebase(newTitle: TitleFromFirebase) = viewModelScope.launch {
        val id = dao.insertTeFromFirebase(newTitle)
    }

    private fun createMemoFromFirebase(newMemo: MemoFromFirebase) = viewModelScope.launch {
        val id = dao.insertMemoFromFirebase(newMemo)
    }

    fun createDir(newDir: DirEntity) = viewModelScope.launch {
        val id = dao.insertDir(newDir)
    }

    sealed class DirEvent {
        object NavigateToAddDir : DirEvent()
        object NavigateToLogIn : DirEvent()
        data class NavigateToTitleList(val id: Int, val name: String) : DirEvent()
        data class NavigateToEditTitleDir(val id: Int, val name: String) : DirEvent()
    }
}
