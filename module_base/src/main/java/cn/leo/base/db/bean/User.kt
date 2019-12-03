package cn.leo.base.db.bean

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author : ling luo
 * @date : 2019-12-03
 */
@Entity(tableName = "user")
data class User(
    @PrimaryKey
    val id: Long,

    val name: String,

    val sex: Int = 0
)