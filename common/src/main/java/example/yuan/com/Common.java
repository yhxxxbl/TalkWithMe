package example.yuan.com;

/**
 * Created by Administrator on 2018/7/9/009.
 */

public class Common {
    /**
     * 一些不可变的参数
     * 通常用于一些配置
     */
    public interface Constance{
        //手机号的正则，11位手机号
        String REGEX_MOBLIE="[1][3,4,5,7,8][0-9]{9}$";
        //基础的网络请求地址
        String API_URI="http://192.168.0.103:8080/api/";
    }
}
