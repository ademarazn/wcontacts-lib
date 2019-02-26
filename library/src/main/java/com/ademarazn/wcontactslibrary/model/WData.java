package com.ademarazn.wcontactslibrary.model;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.ademarazn.wcontactslibrary.util.WDataUtils;

/**
 * WData class used to create object instances of this type
 * with WhatsApp contacts data of available mime types.
 *
 * @author ademarazn
 * @see Builder
 * @since 01/29/2019
 */
public abstract class WData {
    private Long id;
    private String description;
    private String mimeType;

    private WData(WData.Builder builder) {
        this.id = builder.id;
        this.description = builder.description;
        this.mimeType = builder.mimeType;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getMimeType() {
        return mimeType;
    }

    public abstract void sendIntent(@NonNull Activity activity);

    @Override
    public String toString() {
        return "WData{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", mimeType='" + mimeType + '\'' +
                '}';
    }

    /**
     * Builder class that is used to build a {@link WData} instance.
     */
    public static class Builder {
        private Long id;
        private String description;
        private String mimeType;

        public Builder(Long id) {
            this.id = id;
        }

        public WData.Builder description(String description) {
            this.description = description;
            return this;
        }

        public WData.Builder mimeType(String mimeType) {
            this.mimeType = mimeType;
            return this;
        }

        public WData build() {
            return new WData(this) {
                @Override
                public void sendIntent(@NonNull Activity activity) {
                    WDataUtils.sendWhatsAppIntent(activity, this);
                }
            };
        }
    }
}
