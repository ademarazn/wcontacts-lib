package com.ademarazn.wcontactslibrary.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * Permission utilities class, used to manage app permissions.
 * @author ademarazn
 * @since 01/30/2019
 */
public class Permissions {
    public static final String READ_CONTACTS = Manifest.permission.READ_CONTACTS;
    private static final int PERMISSION_GRANTED = PackageManager.PERMISSION_GRANTED;

    @TargetApi(Build.VERSION_CODES.M)
    public static void requestReadContacts(Activity act, int code) {
        act.requestPermissions(new String[]{READ_CONTACTS}, code);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean checkReadContacts(Context context) {
        return context.checkSelfPermission(READ_CONTACTS) == PERMISSION_GRANTED;
    }
}
