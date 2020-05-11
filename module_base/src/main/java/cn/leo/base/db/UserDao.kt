package cn.leo.base.db

import androidx.lifecycle.LiveData
import androidx.room.*
import cn.leo.base.db.bean.User

/**
 * @author : ling luo
 * @date : 2019-12-03
 */

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): LiveData<List<User>>

    @Query("SELECT * FROM user WHERE id = :id")
    //fun findUserById(id: Long): LiveData<User>
    fun findUserById(id: Long): User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg users: User)

    @Update
    fun update(vararg users: User): Int

    @Delete
    fun del(vararg user: User): Int

    @Query("DELETE FROM user")
    fun delAll()
}