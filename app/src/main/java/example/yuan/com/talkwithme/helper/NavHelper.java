package example.yuan.com.talkwithme.helper;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;



/**
 * 解决对Fragment的调度与复用
 * 达到最优的Fragment切换
 * Created by Administrator on 2018/7/1/001.
 */

public class NavHelper<T> {
    private final SparseArray<Tab<T>> tabs = new SparseArray<>();//所有的Tab集合

    //用于初始化的必须参数
    private final Context context;
    private final int containerId;
    private final FragmentManager fragmentManager;
    private final OnTabChangedListener<T> listener;

    private Tab<T> currentTab;//当前的被选中的Tab

    public NavHelper(Context context, int containerId,
                     FragmentManager fragmentManager,
                     OnTabChangedListener<T> listener) {
        this.context = context;
        this.containerId = containerId;
        this.fragmentManager = fragmentManager;
        this.listener = listener;
    }

    public NavHelper<T> add(int menuId, Tab<T> tab) {
        tabs.put(menuId, tab);
        return this;
    }

    /**
     * 获取当前的显示的Tab
     *
     * @return
     */
    public Tab<T> getCurrentTab() {
        return currentTab;
    }

    /**
     * 执行点击菜单的操作
     *
     * @param menuId 菜单ID
     * @return 是否能处理点击
     */
    public boolean performClickMenu(int menuId) {
        //集合中寻找点击菜单对应的Tab，如果有就进行处理
        Tab<T> tab = tabs.get(menuId);
        if (tab != null) {
            doSelect(tab);
            return true;
        }

        return false;
    }

    /**
     * Tab选择操作的处理方法
     *
     * @param tab
     */
    private void doSelect(Tab<T> tab) {
        Tab<T> oldTab = null;
        if (currentTab != null) {
            oldTab = currentTab;
            if (oldTab == tab) {
                //如果当前Tab就是点击的Tab，则不做任何操作
                return;
            }
        }
        //赋值并调用切换方法
        currentTab = tab;
        doTabChanged(currentTab, oldTab);
    }

    /**
     * 进行真实的Fragment调度操作
     * @param newTab
     * @param oldTab
     */
    private void doTabChanged(Tab<T> newTab, Tab<T> oldTab) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (oldTab != null) {
            if (oldTab.fragment != null) {
                //从界面移除，但还存在于缓存空间中
                ft.detach(oldTab.fragment);
            }
        }
        if (newTab.fragment == null) {
            //首次新建
            Fragment fragment = Fragment.instantiate(context, newTab.clx.getName(), null);
            //将fragment缓存起来
            newTab.fragment = fragment;
            //提交到FragmentManager中
            ft.add(containerId, fragment, newTab.clx.getName());
        }else {
            ft.attach(newTab.fragment);
        }
        ft.commit();
        //通知回调
        notifyTabSelect(newTab,oldTab);
    }

    /**
     * 回调监听器
     * @param newTab
     * @param oldTab
     */
    public void notifyTabSelect(Tab<T> newTab, Tab<T> oldTab){
        if (listener!=null){
            listener.onTabChanged(newTab,oldTab);
        }
    }
    /**
     * 所有的Tabj基础属性
     * @param <T>
     */
    public static class Tab<T>{
        public Tab(Class<?> clx, T extra) {
            this.clx = clx;
            this.extra = extra;
        }

        //Fragment需要的class信息
        public Class<?> clx;
        //额外的字段，使用泛型T，用户根据需求使用
        public T extra;
         Fragment fragment;
    }

    /**
     * 定义事件处理完成后的回调接口
     * @param <T>
     */

    public interface OnTabChangedListener<T>{
        void onTabChanged(Tab<T> newTab,Tab<T> oldTab);
    }
}
