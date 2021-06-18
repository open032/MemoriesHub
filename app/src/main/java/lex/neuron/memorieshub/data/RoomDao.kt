package lex.neuron.memorieshub.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import lex.neuron.memorieshub.data.entity.DirEntity
import lex.neuron.memorieshub.data.entity.MemoEntity
import lex.neuron.memorieshub.data.entity.TitleEntity

@Dao
interface RoomDao {

    // ********** Insert **********

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemo(memoEntity: MemoEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTe(titleEntity: TitleEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDir(dirEntity: DirEntity): Long


    // ********** Update **********

    @Update
    suspend fun updateMemo(memoEntity: MemoEntity)

    @Update
    suspend fun updateTe(titleEntity: TitleEntity)

    @Update
    suspend fun updateDir(dirEntity: DirEntity)


    // ********** Delete **********


    @Delete
    suspend fun deleteMemo(memoEntity: MemoEntity)

    @Delete
    suspend fun deleteTe(titleEntity: TitleEntity)

    @Delete
    suspend fun deleteDir(dirEntity: DirEntity)


    // ********** Query **********


    // Memo
    @Query("SELECT * FROM memo_table")
    fun getMemo(): Flow<List<MemoEntity>>

    @Query("SELECT * FROM memo_table WHERE id = :id")
    suspend fun getMemoById(id: Int): MemoEntity

    @Query("SELECT * FROM memo_table WHERE titleList = :tl")
    fun getTeMemo(tl: Int): Flow<List<MemoEntity>>


    // TitleEntity
    @Query("SELECT * FROM title_table")
    fun getTe(): Flow<List<TitleEntity>>

    @Query("SELECT * FROM title_table WHERE id = :id")
    suspend fun getTeById(id: Int): TitleEntity

    @Query("SELECT * FROM title_table WHERE dirList = :dl")
    fun getDirByTe(dl: Int): Flow<List<TitleEntity>>


    // Dir
    @Query("SELECT * FROM dir_table")
    fun getDir(): Flow<List<DirEntity>>

    @Query("SELECT * FROM dir_table WHERE id = :id")
    suspend fun getDirById(id: Int): DirEntity

    @Query("SELECT * FROM title_table WHERE dirList = :dir")
    fun getDirTitle(dir: Int): Flow<List<TitleEntity>>
}
