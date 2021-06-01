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
//            val daoTwo = database.get().titleCardDaoTwo()

            applicationScope.launch {
                dao.insert(TitleEntity("Nika loves coconut"))
                dao.insert(TitleEntity("Churchill loves Core"))
                dao.insert(TitleEntity("Sasha loves Coffee"))
                dao.insert(TitleEntity("1"))
                dao.insert(TitleEntity("2"))
                dao.insert(TitleEntity("3"))
                dao.insert(TitleEntity("4"))

                dao.insertMemo(MemoEntity(1, "Database", "desc" ))
                dao.insertMemo(MemoEntity(1, "Who", "I am" ))
                dao.insertMemo(MemoEntity(2, "No", "Yes" ))

                dao.insertDir(DirEntity("Dir One"))
                dao.insertDir(DirEntity("Dir Two"))
                dao.insertDir(DirEntity("Dir Three"))
            }
        }
    }
}