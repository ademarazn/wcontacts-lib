package com.ademarazn.wcontacts;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ademarazn.wcontacts.adapters.WAdapter;
import com.ademarazn.wcontacts.adapters.holders.WContactViewHolder;
import com.ademarazn.wcontacts.lettertiles.LetterTileDrawable;
import com.ademarazn.wcontactslibrary.WContactsLibrary;
import com.ademarazn.wcontactslibrary.listeners.WContactsListener;
import com.ademarazn.wcontactslibrary.model.WContact;
import com.ademarazn.wcontactslibrary.model.WData;
import com.ademarazn.wcontactslibrary.util.Permissions;
import com.ademarazn.wcontactslibrary.util.WContactUtils;

import java.util.List;

/**
 * @author ademarazn
 * @since 02/01/2019
 */
public class MainActivity extends ListActivity implements WContactsListener {
    private static final String TAG = "MainActivity";

    private static final int READ_CONTACTS_REQUEST_CODE = WContactsLibrary.READ_CONTACTS_REQUEST_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadContacts();
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                WContactUtils.openContact(MainActivity.this, (WContact) adapterView.getItemAtPosition(i));

                return true;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READ_CONTACTS_REQUEST_CODE:
                loadContacts();
                break;
        }
    }

    private void loadContacts() {
        WContactsLibrary.getWContacts(MainActivity.this, this);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        final List<WData> data = ((WContact) l.getItemAtPosition(position)).getWDataList();
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        mDialogBuilder.setAdapter(new WAdapter<WData>(data) {
            @Override
            public View getView(int i, View view, ViewGroup parent) {
                if (view == null) {
                    view = new TextView(getApplicationContext());
                    ((TextView) view).setTextSize(20);
                }

                ((TextView) view).setText(getItem(i).getDescription());

                return view;
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                data.get(position).sendIntent(MainActivity.this);
            }
        });
        mDialogBuilder.show();
    }

    @Override
    public void onSuccess(@NonNull List<WContact> wContacts) {
        Log.d(TAG, "onSuccess() called");
        if (getListAdapter() == null) {
            setListAdapter(new WAdapter<WContact>(wContacts) {
                @Override
                public View getView(int i, View view, ViewGroup parent) {
                    WContactViewHolder holder;

                    if (view == null) {
                        view = LayoutInflater.from(MainActivity.this)
                                .inflate(R.layout.list_item, parent, false);
                        holder = new WContactViewHolder(view);
                        view.setTag(holder);
                    } else {
                        holder = (WContactViewHolder) view.getTag();
                    }

                    WContact wContact = getItem(i);
                    holder.tvName.setText(wContact.getName());
                    holder.tvNumber.setText(wContact.getNumber());

                    if (wContact.hasPhoto()) {
                        holder.ivPhoto.setImageURI(wContact.getPhotoThumbUri());
                    } else {
                        holder.ivPhoto.setImageDrawable(new LetterTileDrawable(getResources())
                                .setLetterAndColorFromContactDetails(wContact.getName(), wContact.getId().toString()));
                    }

                    return view;
                }
            });
        }

        getWindow().setBackgroundDrawableResource(android.R.color.background_light);
    }

    @Override
    public void onFailure(@NonNull Exception exception) {
        Log.w(TAG, "onFailure: ", exception);
        Permissions.requestReadContacts(this, READ_CONTACTS_REQUEST_CODE);
    }
}
