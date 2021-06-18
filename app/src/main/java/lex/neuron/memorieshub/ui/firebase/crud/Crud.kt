package lex.neuron.memorieshub.ui.firebase.crud


// class Crud {
//
// /** Dir **/
//
// /*futeDir(dirEntity: DirEntity, id: Long) {
// val dir = DirFirebase(
// id, dirEntity.name,
// dirEntity.createdDateFormatted,
// )
//
// val uid = AUTH.currentUser?.uid.toString()
//
// val database: DatabaseReference =
// FirebaseDatabase.getInstance().getReference(DIR).child(uid)
// database.child("dir $id").setValue(dir)
// }
//
// fun updateDir(updateDirEntity: DirEntity) {
// val dir = DirFirebase(
// updateDirEntity.id.toLong(),
// updateDirEntity.name,
// updateDirEntity.createdDateFormatted
// )
//
// val uid = AUTH.currentUser?.uid.toString()
//
// val database: DatabaseReference =
// FirebaseDatabase.getInstance().getReference(DIR).child(uid)
// database.child("dir ${dir.id}").setValue(dir)
// }
//
// fun deleteDir(dirEntity: DirEntity) {
// val dirId = dirEntity.id
// val uid = AUTH.currentUser?.uid.toString()
//
// val database: DatabaseReference =
// FirebaseDatabase.getInstance().getReference(DIR).child(uid)
// .child("dir $dirId")
// database.removeValue()
// }
//
//
// /** Title **/
//
// fun createTitle(newTitle: TitleEntity, idTitle: Long, idDir: Int) {
// val title = TitleFirebase(
// idTitle, newTitle.name,
// newTitle.createdDateFormatted,
// idDir
// )
//
// val uid = AUTH.currentUser?.uid.toString()
//
// val database: DatabaseReference =
// FirebaseDatabase.getInstance().getReference(TITLE).child(uid)
// database.child("dir $idDir, title $idTitle").setValue(title)
// }
//
// fun updateTitle(titleEntity: TitleEntity) {
// val idDir = titleEntity.dirList
// val idTitle = titleEntity.id
//
// val title = TitleFirebase(
// titleEntity.id.toLong(),
// titleEntity.name,
// titleEntity.createdDateFormatted,
// titleEntity.dirList
// )
//
// val uid = AUTH.currentUser?.uid.toString()
//
// val database: DatabaseReference =
// FirebaseDatabase.getInstance().getReference(TITLE).child(uid)
// database.child("dir $idDir, title $idTitle").setValue(title)
// }
//
// fun deleteTitle(titleEntity: TitleEntity, list: MutableList<Int>) {
// Log.e(TAG, "deleteTitle   ${titleEntity.id} ----- : $list")
//
// val idDir = titleEntity.dirList
// val idTitle = titleEntity.id
//
// val uid = AUTH.currentUser?.uid.toString()
// val database: DatabaseReference =
// FirebaseDatabase.getInstance().getReference(TITLE).child(uid)
// .child("dir $idDir, title $idTitle")
//
// database.removeValue()
//
// val size = list.size - 1
// for (i in 0..size) {
// val db: DatabaseReference =
// FirebaseDatabase.getInstance().getReference(MEMO_TWO_COLUMNS).child(uid)
// .child("title $idTitle, memo ${list[i]}")
// db.removeValue()
// }
// }
//
// /** Memo Two Columns **/
//
// fun createMemoTwoColumn(memoEntity: MemoEntity, idMemo: Long, idTitle: Int) {
// val memo = MemoTwoColumnsFirebase(
// idMemo, memoEntity.title,
// memoEntity.description,
// memoEntity.createdDateFormatted,
// idTitle
// )
//
// val uid = AUTH.currentUser?.uid.toString()
//
// val database: DatabaseReference =
// FirebaseDatabase.getInstance().getReference(MEMO_TWO_COLUMNS).child(uid)
// database.child("title $idTitle, memo $idMemo").setValue(memo)
// }
//
// fun updateMemoTwoColumns(updateMemoEntity: MemoEntity) {
// val idTitle = updateMemoEntity.titleList
// val idMemo = updateMemoEntity.id
//
// val memo = MemoTwoColumnsFirebase(
// updateMemoEntity.id.toLong(),
// updateMemoEntity.title,
// updateMemoEntity.description,
// updateMemoEntity.createdDateFormatted,
// updateMemoEntity.titleList
// )
//
// val uid = AUTH.currentUser?.uid.toString()
//
// val database: DatabaseReference =
// FirebaseDatabase.getInstance().getReference(MEMO_TWO_COLUMNS).child(uid)
// database.child("title $idTitle, memo $idMemo").setValue(memo)
// }
//
// fun deleteMemoTwoColumns(memo: MemoEntity) {
// val idTitle = memo.titleList
// val idMemo = memo.id
//
// val uid = AUTH.currentUser?.uid.toString()
// val database: DatabaseReference =
// FirebaseDatabase.getInstance().getReference(MEMO_TWO_COLUMNS).child(uid)
// .child("title $idTitle, memo $idMemo")
// database.removeValue()
//
// }
// */
// private fun getDataFromDB() {
// val vListener: ValueEventListener = object : ValueEventListener {
// override fun onDataChange(dataSnapshot: DataSnapshot) {
// for (ds in dataSnapshot.children) {
// Log.e(TAG, "onDataChange: ${ds}")
// /*
// val user: User = ds.getValue(User::class.java)!!
// listData.add(user.name)
// listTemp.add(user)
// }
// }
//
// override fun onCancelled(databaseError: DatabaseError) {}
// }
// //        mDatabase.addValueEventListener(vListener)
// }
//
//
// }