package com.ademarazn.wcontactslibrary.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.ademarazn.wcontactslibrary.model.WData;

public class WDataUtils {
    public static void sendWhatsAppIntent(@NonNull Activity activity, @NonNull WData wContactData) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(parseDataUri(wContactData.getId()), wContactData.getMimeType());
        i.setPackage("com.whatsapp");
        activity.startActivity(i);
    }

    private static Uri parseDataUri(@NonNull Long id) {
        return Uri.parse(String.format("content://com.android.contacts/data/%s", id));
    }
}
