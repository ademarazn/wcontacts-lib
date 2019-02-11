package com.ademarazn.wcontactslibrary.listeners;

import android.support.annotation.NonNull;

import com.ademarazn.wcontactslibrary.model.WContact;

import java.util.List;

/**
 * @author ademarazn
 * @since 01/30/2019
 */
public interface WContactsListener {
    void onSuccess(@NonNull List<WContact> wContacts);

    void onFailure(@NonNull Exception exception);
}
