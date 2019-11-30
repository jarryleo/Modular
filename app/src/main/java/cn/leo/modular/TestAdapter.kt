package cn.leo.modular

import cn.leo.base.base.SuperAdapter
import cn.leo.base.bean.WechatUserBean

/**
 * @author : ling luo
 * @date : 2019-11-30
 */
class TestAdapter : SuperAdapter<WechatUserBean>() {
    override fun getItemLayout(position: Int): Int {
        return R.layout.item_test
    }

    override fun bindData(helper: ItemHelper, data: WechatUserBean) {
        helper.setText(R.id.tvName, data.nickname)
    }
}