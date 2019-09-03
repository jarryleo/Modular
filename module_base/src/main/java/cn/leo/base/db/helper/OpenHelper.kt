package cn.leo.base.db.helper

import android.content.Context
import cn.leo.base.db.DaoMaster
import cn.leo.base.db.UserDetailInfoDao
import com.github.yuweiguocn.library.greendao.MigrationHelper
import org.greenrobot.greendao.database.Database

/**
 * @author : ling luo
 * @date : 2019-09-03
 * 数据库升级帮助类
 */
class OpenHelper(private val context: Context, private val dbName: String) :
    DaoMaster.DevOpenHelper(context, dbName) {

    override fun onUpgrade(db: Database?, oldVersion: Int, newVersion: Int) {
        MigrationHelper.migrate(db,object :MigrationHelper.ReCreateAllTableListener{
            override fun onCreateAllTables(db: Database?, ifNotExists: Boolean) {
                DaoMaster.createAllTables(db, ifNotExists)
            }

            override fun onDropAllTables(db: Database?, ifExists: Boolean) {
                DaoMaster.dropAllTables(db, ifExists)
            }
        },
            //这里写上所有数据库表dao类
            UserDetailInfoDao::class.java
        )
    }

}