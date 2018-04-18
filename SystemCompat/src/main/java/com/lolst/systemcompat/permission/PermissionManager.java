package com.lolst.systemcompat.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

/**
 * App权限管理类
 * Created by log on 2018/3/2.
 */

public class PermissionManager {

    // sd卡
    public static final int REQUEST_CODE_EXTERNAL_STORAGE = 0x800;
    // 定位
    public static final int REQUEST_CODE_LOCATION = 0x801;
    // 联系人
    public static final int REQUEST_CODE_CONTACTS = 0x802;
    // 摄像头
    public static final int REQUEST_CODE_CAMERA = 0x803;
    // 录音
    public static final int REQUEST_CODE_MICROPHONE = 0x804;
    // 未知app来源
    public static final int REQUEST_CODE_UNKNOWN_APP_SORCE = 0x805;

    // 读写SD卡权限
    public static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    // 定位权限
    public static String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS
    };

    // 访问手机联系人权限
    public static String[] PERMISSIONS_CONTACTS = {
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.READ_CONTACTS
    };

    // 摄像头权限
    public static String[] PERMISSIONS_CAMERA = {
            Manifest.permission.CAMERA
    };

    // 录音权限
    public static String[] PERMISSIONS_MICROPHONE = {
            Manifest.permission.RECORD_AUDIO
    };

    public static boolean hasPermissions(Activity activity, String per) {
        boolean result = true;
        if (Build.VERSION.SDK_INT >= 23) {
            int permission = ActivityCompat.checkSelfPermission(activity, per);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                result = false;
            }
        }

        return result;
    }

    public static void requestPermissions(Activity activity, String[] pers, int requestCode) {
        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(activity, pers, requestCode);
        }
    }

    public static boolean hasStoragePermissions(Activity activity) {
        return hasPermissions(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public static void requestStoragePermissions(Activity activity) {
        requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_CODE_EXTERNAL_STORAGE);
    }

    public static void verifyStoragePermissions(Activity activity) {
        if (!hasStoragePermissions(activity)) {
            requestStoragePermissions(activity);
        }
    }

    public static boolean hasLocationPermissions(Activity activity) {
        return hasPermissions(activity, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    public static void requestLocationPermissions(Activity activity) {
        requestPermissions(activity, PERMISSIONS_LOCATION, REQUEST_CODE_LOCATION);
    }

    public static void verifyLocationPermissions(Activity activity) {
        if (!hasLocationPermissions(activity)) {
            requestLocationPermissions(activity);
        }
    }

    public static void verifyContactsPermissions(Activity activity) {
        if (/*!hasPermissions(activity, Manifest.permission.WRITE_CONTACTS)
                ||!hasPermissions(activity, Manifest.permission.GET_ACCOUNTS)
                || */!hasPermissions(activity, Manifest.permission.READ_CONTACTS)) {
            requestPermissions(activity, PERMISSIONS_CONTACTS, REQUEST_CODE_CONTACTS);
        }
    }

    public static boolean hasCameraPermissions(Activity activity) {
        return hasPermissions(activity, Manifest.permission.CAMERA);
    }

    public static void requestCameraPermissions(Activity activity) {
        requestPermissions(activity, PERMISSIONS_CAMERA, REQUEST_CODE_CAMERA);
    }

    public static void verifyCameraPermissions(Activity activity) {
        if (!hasCameraPermissions(activity)) {
            requestCameraPermissions(activity);
        }
    }

    public static void verifyMicPermissions(Activity activity) {
        if (!hasPermissions(activity, Manifest.permission.RECORD_AUDIO)) {
            requestPermissions(activity, PERMISSIONS_MICROPHONE, REQUEST_CODE_MICROPHONE);
        }
    }

    /**
     * 是否能安装应用（适配Android 8.0默认关闭了未知来源应用权限）
     *
     * @param context
     * @return
     */
    public static boolean hasInstallPermission(Context context) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            return context.getPackageManager().canRequestPackageInstalls();
        }
        return true;
    }

    /**
     * 跳转到本应用的授权列表
     * @param activity
     */
    private void startInstallPermissionSettingActivity(Activity activity) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            //注意这个是8.0新API
            // 注意：当通过Intent 跳转到未知应用授权列表的时候，一定要加上包名，这样就能直接跳转到你的app下，不然只能跳转到列表。
            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:" + activity.getPackageName()));
            activity.startActivityForResult(intent, REQUEST_CODE_UNKNOWN_APP_SORCE);
        }
    }

}
