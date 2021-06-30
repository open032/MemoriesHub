package lex.neuron.memorieshub.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import lex.neuron.memorieshub.data.entity.DeleteEntity
import lex.neuron.memorieshub.data.entity.DirEntity
import lex.neuron.memorieshub.data.entity.MemoEntity
import lex.neuron.memorieshub.data.entity.TitleEntity
import lex.neuron.memorieshub.data.netnotwork.NetEntity

@Dao
interface RoomDao {

    // ********** Insert **********

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemo(memoEntity: MemoEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTe(titleEntity: TitleEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDir(dirEntity: DirEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDelete(deleteEntity: DeleteEntity): Long

    /*@Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNet(netEntity: NetEntity): Long*/


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

    @Query("SELECT * FROM memo_table WHERE sendNetCreateUpdate = :bol")
    fun getMemoByBool(bol: Boolean): Flow<List<MemoEntity>>


    // TitleEntity
    @Query("SELECT * FROM title_table")
    fun getTe(): Flow<List<TitleEntity>>

    @Query("SELECT * FROM title_table WHERE id = :id")
    suspend fun getTeById(id: Int): TitleEntity

    @Query("SELECT * FROM title_table WHERE dirList = :dl")
    fun getDirByTe(dl: Int): Flow<List<TitleEntity>>

    @Query("SELECT * FROM title_table WHERE sendNetCreateUpdate = :bol")
    fun getTitleByBool(bol: Boolean): Flow<List<TitleEntity>>

    // Dir
    @Query("SELECT * FROM dir_table")
    fun getDir(): Flow<List<DirEntity>>

    @Query("SELECT * FROM dir_table WHERE id = :id")
    suspend fun getDirById(id: Int): DirEntity

    @Query("SELECT * FROM title_table WHERE dirList = :dir")
    fun getDirTitle(dir: Int): Flow<List<TitleEntity>>

    @Query("SELECT * FROM dir_table WHERE sendNetCreateUpdate = :bol")
    fun getDirByBool(bol: Boolean): Flow<List<DirEntity>>


    // DeleteEntity
    @Query("SELECT * FROM delete_table")
    fun getDelete(): Flow<List<DeleteEntity>>

    @Query("SELECT * FROM delete_table WHERE name = :name")
    fun getDeleteByName(name: String): Flow<List<DeleteEntity>>
   /* // Net
    @Query("SELECT * FROM net_table")
    fun getNet(): Flow<List<NetEntity>>

    @Query("SELECT * FROM net_table WHERE id = :id")
    suspend fun getNetById(id: Int): NetEntity*/
}
