package example.yuan.com.factory.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import example.yuan.com.factory.Factory;
import example.yuan.com.factory.model.api.account.AccountRspModel;
import example.yuan.com.factory.model.db.User;
import example.yuan.com.factory.model.db.User_Table;

/**
 * Created by Administrator on 2018/7/10/010.
 * 好像是回掉接口不好使或者调用setbind无效
 */

public class Account {

    private static final String KEY_PUSH_ID="KEY_PUSH_ID";
    private static final String KEY_IS_BIND="KEY_IS_BIND";
    private static final String KEY_TOKEN="KEY_TOKEN";
    private static final String KEY_USER_ID="KEY_USER_ID";
    private static final String KEY_ACCOUNT="KEY_ACCOUNT";
    private static String pushId;  //设备的推送ID
    private static boolean isBind;  //设备的绑定状态
    private static String token;//登陆状态的Token，用来接口请求
    private static String userId;//登陆的用户ID
    private static String account;//登陆的账户

    /**
     * 将pushID存储起来，持久化
     * @param context  CONTEXT
     */
    private static void save(Context context){
        SharedPreferences sp=context.getSharedPreferences(Account.class.getName(),Context.MODE_PRIVATE);
        sp.edit()
                .putString(KEY_PUSH_ID,pushId)
                .putBoolean(KEY_IS_BIND,isBind)
                .putString(KEY_TOKEN, token)
                .putString(KEY_USER_ID, userId)
                .putString(KEY_ACCOUNT, account)
                .apply();
    }

    /**
     * 进行数据加载
     * @param context
     */

    public static void load(Context context){
        SharedPreferences sp=context.getSharedPreferences(Account.class.getName(),Context.MODE_PRIVATE);
        pushId=sp.getString(KEY_PUSH_ID,"");
        isBind=sp.getBoolean(KEY_IS_BIND,false);
        token = sp.getString(KEY_TOKEN, "");
        userId = sp.getString(KEY_USER_ID, "");
        account = sp.getString(KEY_ACCOUNT, "");

    }
    public static String getPushId(){
        //获取pushId
        return pushId;
    }
    /**
     * 设置并存储设备的ID
     * @param pushId 设备的推送ID
     */
    public static void setPushId(String pushId){
        Account.pushId =pushId;
        Account.save(Factory.app());
    }
    /**
     * 是否已经绑定了服务器
     * @return
     */
    public static boolean isBind(){
        return isBind;
    }
    /**
     * 设备的绑定状态
     * @param isBind 设备是否绑定
     */
    public static void setBind(boolean isBind){
        Account.isBind=isBind;
        Account.save(Factory.app());
    }

    /**
     * 账户的登录状态的判断
     * @return true为已经登陆
     */
    public static boolean isLogin(){
        //用户ID和Token不为空
        return !TextUtils.isEmpty(userId)
                &&!TextUtils.isEmpty(token);
    }

    /**
     * 是否已经完善了用户信息
     * @return True为完成了
     */
    public static boolean isComplete(){
        // 首先保证登录成功
        if (isLogin()) {
            User self = getUser();
            return !TextUtils.isEmpty(self.getDesc())
                    && !TextUtils.isEmpty(self.getPortrait())
                    && self.getSex() != 0;
        }
        // 未登录返回信息不完全
        return false;
    }
    /**
     * 保存我自己的信息到持久化XML中
     * @param model
     */
    public static void login(AccountRspModel model){
        //存储当前登录的token，用户id，方便从数据库中查询我的信息
        Account.token=model.getToken();
        Account.account=model.getAccount();
        Account.userId=model.getUser().getId();
        save(Factory.app());
    }

    /**
     * 获取当前登录的用户信息
     * @return  user
     */
    public static User getUser(){
        //如果为空则返回一个user
        //如果不为空则从数据库查询
        return  TextUtils.isEmpty(userId) ?new User() : SQLite.select()
                .from(User.class).where(User_Table.id.eq(userId))
                .querySingle();
    }

    /**
     * 获取当前登录的Token
     * @return Token
     */
    public static String getToken() {
        return token;
    }
}
