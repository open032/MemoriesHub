package lex.neuron.memorieshub.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import lex.neuron.memorieshub.data.entity.*
import lex.neuron.memorieshub.di.ApplicationScope
import lex.neuron.memorieshub.ui.firebase.crud.dir.DirCreate
import lex.neuron.memorieshub.ui.firebase.crud.memotwocolumns.MemoTwoColumnsCreate
import lex.neuron.memorieshub.ui.firebase.crud.title.TitleCreate
import javax.inject.Inject
import javax.inject.Provider

@Database(
    entities = [TitleEntity::class, MemoEntity::class, DirEntity::class, DeleteEntity::class,
        TitleFromFirebase::class, MemoFromFirebase::class], version = 1
)
abstract class MyDatabase : RoomDatabase() {


    var INSTANCE: MyDatabase? = null
    abstract fun titleCardDao(): RoomDao


    class Callback @Inject constructor(
        private val database: Provider<MyDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().titleCardDao()

            applicationScope.launch {
                val chemistry = DirEntity("Chemistry", true, false,0, 1)
                dao.insertDir(chemistry)

                val alkanes = TitleEntity(1, "Alkane", false, false,0, 1)
                dao.insertTe(alkanes)

                val methane = (MemoEntity(1, "Methane", true, false, false, "CH4", 0, 1))
                val ethane = (MemoEntity(1, "Ethane", true, false, false, "C2H6", 0, 2))
                val propane = (MemoEntity(1, "Propane", true, false, false, "C3H8", 0, 3))
                val butane = (MemoEntity(1, "Butane", true, false, false, "C4H10", 0, 4))
                val pentane = (MemoEntity(1, "Pentane", true, false, false, "C5H12", 0, 5))
                val hexane = (MemoEntity(1, "Hexane", true, false, false, "C6H14", 0, 6))
                val heptane = (MemoEntity(1, "Heptane", true, false, false, "C7H16", 0,7))
                val octane = (MemoEntity(1, "Octane", true, false, false, "C8H18", 0, 8))
                val nonane = (MemoEntity(1, "Nonane", true, false, false, "C9H20", 0, 9))
                val decane = (MemoEntity(1, "Decane", true, false, false, "C10H22", 0, 10))

                val memoAlkanes = (MemoEntity(1, "Memo", false, false, false,
                    "СnH2n+2\nmethane ethane propane butane\npenta - 5\nhexa - 6\n" +
                            "hepta - 7\nocta - 8\nnona - 9\ndeca - 10", 0, 11))

                dao.insertMemo(methane)
                dao.insertMemo(ethane)
                dao.insertMemo(propane)
                dao.insertMemo(butane)
                dao.insertMemo(pentane)
                dao.insertMemo(hexane)
                dao.insertMemo(heptane)
                dao.insertMemo(octane)
                dao.insertMemo(nonane)
                dao.insertMemo(decane)
                dao.insertMemo(memoAlkanes)


                val dirCreateInFirebase = DirCreate()
                val titleCreateInFirebase = TitleCreate()
                val memoCreateInFirebase = MemoTwoColumnsCreate()
                dirCreateInFirebase.createDir(chemistry, chemistry.id.toLong())
                titleCreateInFirebase.createTitle(alkanes, alkanes.dirList.toLong(), alkanes.id)
                memoCreateInFirebase.createMemoTwoColumn(methane, methane.id.toLong(), methane.titleList)
                memoCreateInFirebase.createMemoTwoColumn(ethane, ethane.id.toLong(), ethane.titleList)
                memoCreateInFirebase.createMemoTwoColumn(propane, propane.id.toLong(), propane.titleList)
                memoCreateInFirebase.createMemoTwoColumn(butane, butane.id.toLong(), butane.titleList)
                memoCreateInFirebase.createMemoTwoColumn(pentane, pentane.id.toLong(), pentane.titleList)
                memoCreateInFirebase.createMemoTwoColumn(hexane, hexane.id.toLong(), hexane.titleList)
                memoCreateInFirebase.createMemoTwoColumn(heptane, heptane.id.toLong(), heptane.titleList)
                memoCreateInFirebase.createMemoTwoColumn(octane, octane.id.toLong(), octane.titleList)
                memoCreateInFirebase.createMemoTwoColumn(nonane, nonane.id.toLong(), nonane.titleList)
                memoCreateInFirebase.createMemoTwoColumn(decane, decane.id.toLong(), decane.titleList)
                memoCreateInFirebase.createMemoTwoColumn(memoAlkanes, memoAlkanes.id.toLong(), memoAlkanes.titleList)

            }
        }
    }
}