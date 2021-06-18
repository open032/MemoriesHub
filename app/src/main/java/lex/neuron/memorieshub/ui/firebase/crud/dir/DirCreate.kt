package lex.neuron.memorieshub.ui.firebase.crud.dir

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import lex.neuron.memorieshub.data.entity.DirEntity
import lex.neuron.memorieshub.data.fireentity.DirFirebase
import lex.neuron.memorieshub.util.AUTH
import lex.neuron.memorieshub.util.DIR

class DirCreate {

    fun createDir(dirEntity: DirEntity, id: Long) {
        val dir = DirFirebase(
            id, dirEntity.name,
            dirEntity.createdDateFormatted,
        )

        val uid = AUTH.currentUser?.uid.toString()

        val database: DatabaseReference =
            FirebaseDatabase.getInstance().getReference(DIR).child(uid)
        database.child("dir $id").setValue(dir)
    }
}