package com.ademarazn.wcontactslibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ademarazn.wcontactslibrary.exceptions.PermissionDeniedException;
import com.ademarazn.wcontactslibrary.model.WContact;
import com.ademarazn.wcontactslibrary.listeners.WContactsListener;
import com.ademarazn.wcontactslibrary.model.WData;
import com.ademarazn.wcontactslibrary.util.Permissions;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the Library main class, used to retrieve the WhatsApp contacts as {@link WContact} objects.
 *
 * @author ademarazn
 * @since 01/29/2019
 */
public class WContactsLibrary {
    private static final String TAG = "WContactsLibrary";

    /* Permissions Request Code */
    public static final int READ_CONTACTS_REQUEST_CODE = 33;

    private static final String WHATSAPP_PACKAGE_NAME = "com.whatsapp";
    private static final String WHATSAPP_NET_DOMAIN = "@s.whatsapp.net";

    /* WContacts 'Main' Cursor params */
    private static final Uri CURSOR_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    @SuppressLint("InlinedApi")
    private static final String[] PROJECTION = {
            ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
            ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI,
            ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY
    };
    private static final String SELECTION = ContactsContract.RawContacts.ACCOUNT_TYPE + "=?";
    private static final String[] SELECTION_ARGS = {WHATSAPP_PACKAGE_NAME};
    private static final String SORT_ORDER = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;

    private static final int CONTACT_ID_INDEX = 0;
    private static final int CONTACT_DISPLAY_NAME_INDEX = 1;
    private static final int CONTACT_NUMBER_INDEX = 2;
    private static final int CONTACT_PHOTO_URI_INDEX = 3;
    private static final int CONTACT_PHOTO_THUMBNAIL_URI_INDEX = 4;
    private static final int CONTACT_LOOKUP_KEY_INDEX = 5;

    /* WhatsApp Number Cursor params */
    private static final Uri NUMBER_CURSOR_URI = ContactsContract.Data.CONTENT_URI;
    @SuppressLint("InlinedApi")
    private static final String[] NUMBER_PROJECTION = {ContactsContract.Data.DATA1};
    private static final String NUMBER_SELECTION = ContactsContract.RawContacts.ACCOUNT_TYPE + "=? AND " +
            ContactsContract.Data.RAW_CONTACT_ID + "=? AND " + ContactsContract.Data.DATA1 + " LIKE ?";
    private static String[] numberSelectionArgs = {WHATSAPP_PACKAGE_NAME, null, "%" + WHATSAPP_NET_DOMAIN};
    private static final int NUMBER_CONTACT_ID_ARG_INDEX = 1;
    private static final String NUMBER_SORT_ORDER = null;

    private static final int NUMBER_DATA1_INDEX = 0;

    /* WData Cursor params */
    private static final Uri WDATA_CURSOR_URI = ContactsContract.Data.CONTENT_URI;
    @SuppressLint("InlinedApi")
    private static final String[] WDATA_PROJECTION = {ContactsContract.Data._ID, ContactsContract.Data.DATA3,
            ContactsContract.Data.MIMETYPE};
    private static final String WDATA_SELECTION = ContactsContract.RawContacts.ACCOUNT_TYPE + "=? AND " +
            ContactsContract.Data.RAW_CONTACT_ID + "=? AND " + ContactsContract.Data.DATA1 + " LIKE ?";
    private static String[] wDataSelectionArgs = {WHATSAPP_PACKAGE_NAME, null, "%" + WHATSAPP_NET_DOMAIN};
    private static final int WDATA_CONTACT_ID_ARG_INDEX = 1;
    private static final String WDATA_SORT_ORDER = null;

    private static final int WDATA_ID_INDEX = 0;
    private static final int WDATA_DATA3_INDEX = 1;
    private static final int WDATA_MIMETYPE_INDEX = 2;

    private WContactsLibrary() {
    }

    /**
     * This method is used to retrieve all contacts on the device, which are WhatsApp contacts,
     * through a {@link WContactsListener}.
     * <h2>Example:</h2><pre><code>
     * WContactsLibrary.getWContacts(context, new WContactsListener() {
     *    {@literal @}Override
     *     public void onSuccess(@NonNull{@literal List<WContact>} wContacts) {
     *         for (WContact wContact : wContacts) {
     *             System.out.println(wContact);
     *         }
     *     }
     *    {@literal @}Override
     *     public void onFailure(@NonNull Exception exception) {
     *         exception.printStackTrace();
     *     }
     * });</code></pre>
     *
     * @param context  {@link Context} instance used for query purposes.
     * @param listener Listener used to retrieve a list of {@link WContact}.
     */
    public static synchronized void getWContacts(@NonNull final Context context, @NonNull final WContactsListener listener) {
        new GetWContactsTask(listener).execute(context);
    }

    private static WContact parseWContact(@NonNull Context context, @NonNull Cursor c) {
        return new WContact.Builder(c.getLong(CONTACT_ID_INDEX))
                .setName(c.getString(CONTACT_DISPLAY_NAME_INDEX))
                .setNumber(getWhatsAppNumber(context, c.getString(CONTACT_ID_INDEX), c.getString(CONTACT_NUMBER_INDEX)))
                .setPhotoUri(c.getString(CONTACT_PHOTO_URI_INDEX))
                .setPhotoThumbUri(c.getString(CONTACT_PHOTO_THUMBNAIL_URI_INDEX))
                .setLookupUri(c.getString(CONTACT_LOOKUP_KEY_INDEX))
                .addAllWData(getWData(context, c.getLong(CONTACT_ID_INDEX)))
                .build();
    }

    @NonNull
    private static String getWhatsAppNumber(@NonNull Context context, @NonNull final String contactId, @NonNull String defaultValue) {
        numberSelectionArgs[NUMBER_CONTACT_ID_ARG_INDEX] = contactId;
        Cursor c = query(context, NUMBER_CURSOR_URI, NUMBER_PROJECTION, NUMBER_SELECTION, numberSelectionArgs, NUMBER_SORT_ORDER);

        if (c != null && c.moveToFirst()) {
            String number = c.getString(NUMBER_DATA1_INDEX).replace(WHATSAPP_NET_DOMAIN, "");
            c.close();

            return number;
        }

        return defaultValue;
    }

    private static Cursor query(@NonNull Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (Permissions.checkReadContacts(context) == Boolean.TRUE) {
            return context.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
        }

        return null;
    }

    private static List<WData> getWData(@NonNull Context context, @NonNull Long contactId) {
        wDataSelectionArgs[WDATA_CONTACT_ID_ARG_INDEX] = contactId.toString();

        List<WData> data = new ArrayList<>();
        Cursor c = context.getContentResolver().query(WDATA_CURSOR_URI, WDATA_PROJECTION, WDATA_SELECTION, wDataSelectionArgs, WDATA_SORT_ORDER);

        if (c != null) {
            while (c.moveToNext()) {
                data.add(parseWData(c));
            }

            c.close();
        }

        return data;
    }

    private static WData parseWData(@NonNull Cursor c) {
        return new WData.Builder(c.getLong(WDATA_ID_INDEX))
                .description(c.getString(WDATA_DATA3_INDEX))
                .mimeType(c.getString(WDATA_MIMETYPE_INDEX))
                .build();
    }

    private static class GetWContactsTask extends AsyncTask<Context, Void, List<WContact>> {
        private final WContactsListener mListener;

        GetWContactsTask(WContactsListener mListener) {
            this.mListener = mListener;
        }

        @Override
        protected List<WContact> doInBackground(Context... context) {
            Log.d(TAG, "getWContacts: doInBackground");

            if (Permissions.checkReadContacts(context[0]) == Boolean.FALSE) {
                return null;
            }

            List<WContact> wContacts = new ArrayList<>();
            Cursor c = query(context[0], CURSOR_URI, PROJECTION, SELECTION, SELECTION_ARGS, SORT_ORDER);

            if (c != null) {
                while (c.moveToNext()) {
                    wContacts.add(parseWContact(context[0], c));
                }

                c.close();
            }

            return wContacts;
        }

        @Override
        protected void onPostExecute(List<WContact> wContacts) {
            super.onPostExecute(wContacts);

            if (wContacts != null) {
                Log.d(TAG, "getWContacts: onSuccess");
                mListener.onSuccess(wContacts);
            } else {
                Log.w(TAG, "getWContacts: onFailure");
                mListener.onFailure(new PermissionDeniedException(Permissions.READ_CONTACTS));
            }
        }
    }
}
