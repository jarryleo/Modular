package cn.leo.base.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author : ling luo
 * @date : 2019-09-03
 */
@Entity
public class UserDetailInfo {
    @Id(autoincrement = true)
    private Long id;

    @Property
    @Unique
    private String userId;

    @Property
    private String nickname;

    @Property
    private String phone;

    @Property
    private int sex;

    @Property
    private String address;

    @Property
    private long channel;

    @Property
    private String email;

    @Property
    private String imei;

    @Property
    private String ip;
    @Property
    private String remarks;
    @Property
    private String status;
    @Property
    private long version;
    @Property
    private long birthday;
    @Property
    private String headImage;
    @Property
    private String qrCode;

    @Property
    private String invitationCode;

    @Generated(hash = 2082403140)
    public UserDetailInfo(Long id, String userId, String nickname, String phone,
            int sex, String address, long channel, String email, String imei,
            String ip, String remarks, String status, long version, long birthday,
            String headImage, String qrCode, String invitationCode) {
        this.id = id;
        this.userId = userId;
        this.nickname = nickname;
        this.phone = phone;
        this.sex = sex;
        this.address = address;
        this.channel = channel;
        this.email = email;
        this.imei = imei;
        this.ip = ip;
        this.remarks = remarks;
        this.status = status;
        this.version = version;
        this.birthday = birthday;
        this.headImage = headImage;
        this.qrCode = qrCode;
        this.invitationCode = invitationCode;
    }

    @Generated(hash = 1776232472)
    public UserDetailInfo() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getSex() {
        return this.sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getChannel() {
        return this.channel;
    }

    public void setChannel(long channel) {
        this.channel = channel;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImei() {
        return this.imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getVersion() {
        return this.version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public long getBirthday() {
        return this.birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public String getHeadImage() {
        return this.headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getQrCode() {
        return this.qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getInvitationCode() {
        return this.invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }
    
}
