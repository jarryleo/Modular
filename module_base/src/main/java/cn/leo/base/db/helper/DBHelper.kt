package cn.leo.base.db.helper

import android.app.Application
import android.database.sqlite.SQLiteDatabase
import cn.leo.base.db.DaoMaster
import cn.leo.base.db.DaoSession

/**
 * @author : ling luo
 * @date : 2019-09-03
 */

object DBHelper {
    const val DB_NAME = "db_short_video"
    private lateinit var daoMasterHelper: DaoMaster.DevOpenHelper
    private lateinit var daoMaster: DaoMaster
    private lateinit var db: SQLiteDatabase
    private lateinit var daoSession: DaoSession
    fun initGreenDao(application: Application) {
        daoMasterHelper = OpenHelper(application, DB_NAME)
        db = daoMasterHelper.writableDatabase
        daoMaster = DaoMaster(db)
        daoSession = daoMaster.newSession()
    }

    fun getSession():DaoSession{
        return  daoSession
    }

    fun close(){
        daoSession.clear()
    }
}