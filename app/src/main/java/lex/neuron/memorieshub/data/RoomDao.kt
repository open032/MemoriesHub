package lex.neuron.memorieshub.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import lex.neuron.memorieshub.data.entity.DirEntity
import lex.neuron.memorieshub.data.entity.MemoEntity
import lex.neuron.memorieshub.data.entity.TitleEntity

@Dao
interface RoomDao {

    @Query("SELECT * FROM title_table")
    fun getTitle(): Flow<List<TitleEntity>>

    @Query("SELECT * FROM title_table WHERE id = :id")
    suspend fun getById(id: Int): TitleEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(titleEntity: TitleEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemo(memoEntity: MemoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDir(dirEntity: DirEntity)

    @Update
    suspend fun update(titleEntity: TitleEntity)

    @Update
    suspend fun updateMemo(memoEntity: MemoEntity)

    @Delete
    suspend fun delete(titleEntity: TitleEntity)

    @Delete
    suspend fun deleteMemo(memoEntity: MemoEntity)



    @Query("SELECT * FROM memo_table WHERE id = :id")
    suspend fun getByMemoId(id: Int): MemoEntity


    @Query("SELECT * FROM memo_table")
    fun getMemo(): Flow<List<MemoEntity>>

    @Query("SELECT * FROM dir_table")
    fun getDir(): Flow<List<DirEntity>>

    @Query("SELECT * FROM dir_table WHERE id = :id")
    suspend fun getDirId(id: Int): DirEntity

    @Query("SELECT * FROM memo_table WHERE titleList = :tc")
    fun getTlMemo(tc: Int): Flow<List<MemoEntity>>



}
