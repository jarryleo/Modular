package cn.leo.modular

import cn.leo.base.model.BaseModel

/**
 * @author : ling luo
 * @date : 2019-12-06
 */
class TestModel : BaseModel() {

    fun setTitle(title: String) = async { title }

}