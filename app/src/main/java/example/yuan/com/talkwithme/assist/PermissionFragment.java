package example.yuan.com.talkwithme.assist;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import example.yuan.com.common.app.Application;
import example.yuan.com.common.widget.GalleryView;
import example.yuan.com.talkwithme.R;
import example.yuan.com.talkwithme.fragments.media.GalleryFragment;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 权限申请弹出
 */
public class PermissionFragment extends BottomSheetDialogFragment
        implements EasyPermissions.PermissionCallbacks {
    private static final int RC = 0x000;

    public PermissionFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new GalleryFragment.TransBottomSheetDialog(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       //获取布局中的控件
        View root = inflater.inflate(R.layout.fragment_permission, container, false);
        //绑定按钮控件，点击时进行申请权限
        root.findViewById(R.id.btn_submit)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestPermission();
                    }
                });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        //在界面显示的时候进行刷新
        refreshState(getView());
    }

    /**
     * 刷新布局中的图片状态
     *
     * @param root 根布局
     */
    private void refreshState(View root) {
        if (root==null)
            return;
        Context context = getContext();
        root.findViewById(R.id.img_state_permission_network)
                .setVisibility(haveNetworkPermission(context) ? View.VISIBLE : View.GONE);
        root.findViewById(R.id.img_state_permission_record_audio)
                .setVisibility(haveRecordPermission(context) ? View.VISIBLE : View.GONE);
        root.findViewById(R.id.img_state_permission_write)
                .setVisibility(haveWritekPermission(context) ? View.VISIBLE : View.GONE);
        root.findViewById(R.id.img_state_permission_read)
                .setVisibility(haveReadkPermission(context) ? View.VISIBLE : View.GONE);

    }

    /**
     * @param context
     * @return true代表有权限 反之没有
     */
    private static boolean haveNetworkPermission(Context context) {
        //需要检查的权限：网络权限
        String[] permission = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE
        };
        return EasyPermissions.hasPermissions(context, permission);
    }

    private static boolean haveWritekPermission(Context context) {
        //需要检查的权限:读取权限
        String[] permission = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
        };
        return EasyPermissions.hasPermissions(context, permission);
    }

    private static boolean haveReadkPermission(Context context) {
        //需要检查的权限:写入权限
        String[] permission = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        };
        return EasyPermissions.hasPermissions(context, permission);
    }

    private static boolean haveRecordPermission(Context context) {
        //需要检查的权限：录音权限
        String[] permission = new String[]{
                Manifest.permission.RECORD_AUDIO,
        };
        return EasyPermissions.hasPermissions(context, permission);
    }

    //私有的show方法
    private static void show(FragmentManager manager) {
        //diaoyong BottomSheetDialogFragment以及准备好的显示方法
        new PermissionFragment()
                .show(manager, PackageManager.class.getName());
    }

    //是否具有所有权限
    public static boolean isHaveAll(Context context, FragmentManager fragmentManager) {
        boolean HaveAll = haveNetworkPermission(context)
                && haveWritekPermission(context)
                && haveReadkPermission(context)
                && haveRecordPermission(context);
        if (!HaveAll) {
            show(fragmentManager);
        }
        return HaveAll;
    }


    //申请权限的方法
    @AfterPermissionGranted(RC)
    private void requestPermission() {
        String[] permission = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        };

        if (EasyPermissions.hasPermissions(getContext(), permission)) {
            Application.showToast(R.string.label_permission_submit);
            //Fragment可以调用getView获得根布局，前提是在onCreateView后调用
            refreshState(getView());
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.title_assist_permissions), RC, permission);
        }
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog
                    .Builder(this)
                    .build()
                    .show();

        }
    }

    /**
     * 权限申请时回调的方法，在这个方法中把对应的权限申请状态交给框架
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //传递对应的参数，并告知接受权限的处理者是我自己
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }
}
