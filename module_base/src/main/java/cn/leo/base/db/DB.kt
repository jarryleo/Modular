package cn.leo.base.db

import androidx.room.Database
import androidx.room.RoomDatabase
import cn.leo.base.db.bean.User

/**
 * @author : ling luo
 * @date : 2019-12-03
 */
@Database(entities = [User::class], version = 1)
abstract class DB : RoomDatabase() {
    abstract fun userDao(): UserDao
}