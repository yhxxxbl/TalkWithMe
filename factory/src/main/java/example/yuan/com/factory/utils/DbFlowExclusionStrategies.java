package example.yuan.com.factory.utils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

/**
 * 设置DbFlow框架的过滤器
 * 框架本身封装好了一些信息、操作等
 * 我们的Json不是全都需要解析，所以设置一个过滤器
 */

public class DbFlowExclusionStrategies implements ExclusionStrategy {
    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        //应该被跳过的字段
        //只要是属于DbFlow的字段全部过滤
        return f.getDeclaredClass().equals(ModelAdapter.class);
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        //应该被跳过的类

        return false;
    }
}
