package lex.neuron.memorieshub.ui.firebase.crud.title

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import lex.neuron.memorieshub.data.entity.TitleEntity
import lex.neuron.memorieshub.util.AUTH
import lex.neuron.memorieshub.util.TITLE

class OnlyTitleDelete {
    fun deleteTitle(titleEntity: TitleEntity) {
        Log.d(TAG, "deleteTitle: Firebase")
        val dirId = titleEntity.dirList
        val titleId = titleEntity.id


        val uid = AUTH.currentUser?.uid.toString()
        val database: DatabaseReference =
            FirebaseDatabase.getInstance().getReference(TITLE).child(uid)
                .child("dir $dirId, title $titleId")

        database.removeValue()
    }
}