package com.ademarazn.wcontactslibrary.model;

import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * WContact class used to create object instances of this type with WhatsApp contact data.
 *
 * @author ademarazn
 * @see Builder
 * @since 01/29/2019
 */
public class WContact {
    private Long id;
    private String name;
    private String number;
    private Uri photoUri;
    private Uri photoThumbUri;
    private Uri lookupUri;
    private List<WData> wDataList;

    private WContact(@NonNull WContact.Builder builder) {
        id = builder.id;
        name = builder.name;
        number = builder.number;
        photoUri = builder.photoUri;
        photoThumbUri = builder.photoThumbUri;
        lookupUri = builder.lookupUri;
        wDataList = builder.wDataList;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public Uri getPhotoUri() {
        return photoUri;
    }

    public Uri getPhotoThumbUri() {
        return photoThumbUri;
    }

    public boolean hasPhoto() {
        return photoUri != null;
    }

    public Uri getLookupUri() {
        return lookupUri;
    }

    public List<WData> getWDataList() {
        return wDataList;
    }

    @Override
    public String toString() {
        return "WContact{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", photoUri=" + photoUri +
                ", photoThumbUri=" + photoThumbUri +
                ", lookupUri=" + lookupUri +
                ", data=" + Arrays.toString(wDataList.toArray()) +
                '}';
    }

    /**
     * Builder class that is used to build a {@link WContact} instance.
     */
    public static class Builder {
        private final Long id;
        private String name;
        private String number;
        private Uri photoUri;
        private Uri photoThumbUri;
        private Uri lookupUri;
        private List<WData> wDataList;

        /**
         * Constructs a new {@link WContact.Builder} with the given id.
         *
         * @param id The {@link WContact} Id, which is a WhatsApp contact identifier.
         */
        public Builder(@NonNull Long id) {
            this.id = id;
            wDataList = new ArrayList<>();
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setNumber(String number) {
            this.number = number;
            return this;
        }

        public Builder setPhotoUri(String photoUri) {
            if (photoUri != null) {
                this.photoUri = Uri.parse(photoUri);
            }
            return this;
        }

        public Builder setPhotoUri(Uri photoUri) {
            this.photoUri = photoUri;
            return this;
        }

        public Builder setPhotoThumbUri(String photoThumbUri) {
            if (!TextUtils.isEmpty(photoThumbUri)) {
                this.photoThumbUri = Uri.parse(photoThumbUri);
            }
            return this;
        }

        public Builder setPhotoThumbUri(Uri photoThumbUri) {
            this.photoThumbUri = photoThumbUri;
            return this;
        }

        public Builder setLookupUri(String lookupKey) {
            if (!TextUtils.isEmpty(lookupKey)) {
                this.lookupUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI,
                        lookupKey);
            }
            return this;
        }

        public Builder setLookupUri(Uri lookupUri) {
            this.lookupUri = lookupUri;
            return this;
        }

        public Builder addWData(@NonNull WData wData) {
            this.wDataList.add(wData);
            return this;
        }

        public Builder addAllWData(@NonNull List<WData> wDataList) {
            this.wDataList.addAll(wDataList);
            return this;
        }

        @NonNull
        public WContact build() {
            return new WContact(this);
        }
    }
}
