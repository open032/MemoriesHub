package lex.neuron.memorieshub.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import lex.neuron.memorieshub.data.entity.DeleteEntity
import lex.neuron.memorieshub.data.entity.DirEntity
import lex.neuron.memorieshub.data.entity.MemoEntity
import lex.neuron.memorieshub.data.entity.TitleEntity
import lex.neuron.memorieshub.data.netnotwork.NetEntity
import lex.neuron.memorieshub.di.ApplicationScope
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [TitleEntity::class, MemoEntity::class, DirEntity::class, DeleteEntity::class], version = 1)
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
                dao.insertDir(DirEntity("Main", true))
                dao.insertDir(DirEntity("Geology", true))
                dao.insertDir(DirEntity("Chemistry", true))
                dao.insertDir(DirEntity("Something", true))

                dao.insertTe(TitleEntity(1, "Interesting", true))
                dao.insertTe(TitleEntity(1, "Good", true))
                dao.insertTe(TitleEntity(3, "Alkane", true))
                dao.insertTe(TitleEntity(2, "Geology Time", true))
                dao.insertTe(TitleEntity(4, "Coffee", true))

                dao.insertMemo(MemoEntity(1, "Hock", true, true, true, "room" ))
                dao.insertMemo(MemoEntity(2, "Temp", true, true, true, "To day" ))
                dao.insertMemo(MemoEntity(3, "Methane", true, true, true,"CH4" ))
                dao.insertMemo(MemoEntity(3, "Ethane",  true, true,true,"C2H6" ))
                dao.insertMemo(MemoEntity(4, "Mesozoic",  true, true,true,"Start 240 Millions of years ago" ))
                dao.insertMemo(MemoEntity(5, "Hard",  true, true,true,"Drink" ))
            }
        }
    }
}