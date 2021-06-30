package lex.neuron.memorieshub.ui.firebase.crud.dir

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import lex.neuron.memorieshub.data.entity.DirEntity
import lex.neuron.memorieshub.util.AUTH
import lex.neuron.memorieshub.util.DIR

class OnlyDirDelete {
    fun deleteDir(dirEntity: DirEntity) {

        val dirId = dirEntity.id
        val uid = AUTH.currentUser?.uid.toString()

        val database: DatabaseReference =
            FirebaseDatabase.getInstance().getReference(DIR).child(uid)
                .child("dir $dirId")
        database.removeValue()
    }
}