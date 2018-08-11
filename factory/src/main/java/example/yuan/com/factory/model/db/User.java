package example.yuan.com.factory.model.db;

import com.google.gson.annotations.Expose;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by Administrator on 2018/7/9/009.
 */
@Table(database =AppDatabase.class)
public class User extends BaseModel {
    public static final int SEX_MAN=1;
    public static final int SEX_WOMAN=2;

    @PrimaryKey//主键
    private String id;
    @Column
    private String name;
    @Column
    private String phone;
    @Column
    private String desc;
    @Column
    private String portrait;
    @Column
    private int sex=0;
    @Column
    private String alias;//对别的用户的备注信息，写入数据库
    @Column
    private int follows;//用户粉丝的数量
    @Column
    private int following;//用户关注人的数量
    @Column
    private boolean isFollow;
    @Column
    private Date modiftAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getFollows() {
        return follows;
    }

    public void setFollows(int follows) {
        this.follows = follows;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public Date getModiftAt() {
        return modiftAt;
    }

    public void setModiftAt(Date modiftAt) {
        this.modiftAt = modiftAt;
    }
}
