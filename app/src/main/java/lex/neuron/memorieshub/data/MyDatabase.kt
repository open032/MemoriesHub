package lex.neuron.memorieshub.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import lex.neuron.memorieshub.data.entity.DirEntity
import lex.neuron.memorieshub.data.entity.MemoEntity
import lex.neuron.memorieshub.data.entity.TitleEntity
import lex.neuron.memorieshub.di.ApplicationScope
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [TitleEntity::class, MemoEntity::class, DirEntity::class], version = 1)
abstract class MyDatabase : RoomDatabase() {

    abstract fun titleCardDao(): RoomDao
//    abstract fun titleCardDaoTwo(): MemoDao


    class Callback @Inject constructor(
        private val database: Provider<MyDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().titleCardDao()

            applicationScope.launch {
                dao.insertDir(DirEntity("Main"))
                dao.insertDir(DirEntity("Geology"))
                dao.insertDir(DirEntity("Chemistry"))
                dao.insertDir(DirEntity("Something"))

                dao.insertTe(TitleEntity(1, "Interesting"))
                dao.insertTe(TitleEntity(1, "Good"))
                dao.insertTe(TitleEntity(3, "Alkane"))
                dao.insertTe(TitleEntity(2, "Geology Time"))
                dao.insertTe(TitleEntity(4, "Coffee"))

                dao.insertMemo(MemoEntity(1, "Hock", "room" ))
                dao.insertMemo(MemoEntity(2, "Temp", "To day" ))
                dao.insertMemo(MemoEntity(3, "Methane", "CH4" ))
                dao.insertMemo(MemoEntity(3, "Ethane", "C2H6" ))
                dao.insertMemo(MemoEntity(4, "Mesozoic", "Start 240 Millions of years ago" ))
                dao.insertMemo(MemoEntity(5, "Hard", "Drink" ))
            }
        }
    }
}