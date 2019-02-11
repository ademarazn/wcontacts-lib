package com.ademarazn.wcontactslibrary.util;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ademarazn.wcontactslibrary.exceptions.WDataMimeTypeException;
import com.ademarazn.wcontactslibrary.model.WContact;
import com.ademarazn.wcontactslibrary.model.WData;

@SuppressWarnings("unused")
public class WContactUtils {
    private static final String TAG = "WContactUtils";

    /* WhatsApp MimeTypes */
    private static final String PROFILE_MIMETYPE = "vnd.android.cursor.item/vnd.com.whatsapp.profile";
    private static final String VOIP_CALL_MIMETYPE = "vnd.android.cursor.item/vnd.com.whatsapp.voip.call";
    private static final String VIDEO_CALL_MIMETYPE = "vnd.android.cursor.item/vnd.com.whatsapp.video.call";

    public static void openContact(@NonNull Context context, @NonNull WContact wContact) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, wContact.getLookupUri()));
    }

    public static Boolean hasProfileWData(WContact wContact) {
        try {
            return startConversation(null, wContact, Boolean.FALSE);
        } catch (WDataMimeTypeException e) {
            Log.d(TAG, "hasProfileWData: " + e);
        }

        return Boolean.FALSE;
    }

    public static void startConversation(@NonNull Context context, WContact wContact) throws WDataMimeTypeException {
        startConversation(context, wContact, Boolean.TRUE);
    }

    private static Boolean startConversation(Context context, WContact wContact, Boolean sendIntent) throws WDataMimeTypeException {
        WData profileWData = findDataByMimeType(wContact, PROFILE_MIMETYPE);

        if (profileWData != null) {
            if (sendIntent == Boolean.TRUE) {
                profileWData.sendIntent(context);
            }

            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    public static Boolean hasVoiceCallWData(WContact wContact) {
        try {
            return makeVoiceCall(null, wContact, Boolean.FALSE);
        } catch (WDataMimeTypeException e) {
            Log.d(TAG, "hasVoiceCallWData: " + e);
        }

        return Boolean.FALSE;
    }

    public static void makeVoiceCall(@NonNull Context context, WContact wContact) throws WDataMimeTypeException {
        makeVoiceCall(context, wContact, Boolean.TRUE);
    }

    private static Boolean makeVoiceCall(Context context, WContact wContact, Boolean sendIntent) throws WDataMimeTypeException {
        WData voiceWData = findDataByMimeType(wContact, VOIP_CALL_MIMETYPE);

        if (voiceWData != null) {
            if (sendIntent == Boolean.TRUE) {
                voiceWData.sendIntent(context);
            }

            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    public static Boolean hasVideoCallWData(WContact wContact) {
        try {
            return makeVideoCall(null, wContact, Boolean.FALSE);
        } catch (WDataMimeTypeException e) {
            Log.d(TAG, "hasVideoCallWData: " + e);
        }

        return Boolean.FALSE;
    }

    public static void makeVideoCall(@NonNull Context context, WContact wContact) throws WDataMimeTypeException {
        makeVideoCall(context, wContact, Boolean.TRUE);
    }

    private static Boolean makeVideoCall(Context context, WContact wContact, Boolean sendIntent) throws WDataMimeTypeException {
        WData videoWData = findDataByMimeType(wContact, VIDEO_CALL_MIMETYPE);

        if (videoWData != null) {
            videoWData.sendIntent(context);

            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    private static WData findDataByMimeType(WContact wContact, @NonNull String mimeType) throws WDataMimeTypeException {
        if (wContact != null && wContact.getWDataList() != null) {
            for (WData wData : wContact.getWDataList()) {
                if (wData != null && wData.getMimeType().equals(mimeType)) {
                    return wData;
                }
            }
        }

        throw new WDataMimeTypeException(mimeType);
    }
}
