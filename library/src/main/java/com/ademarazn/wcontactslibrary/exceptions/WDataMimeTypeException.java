package com.ademarazn.wcontactslibrary.exceptions;

/**
 * @author ademarazn
 * @since 01/31/2019
 */
public class WDataMimeTypeException extends Exception {
    private static final String MESSAGE = "There is no WData with MIMETYPE = '%s' available to send WhatsApp Intent.";

    public WDataMimeTypeException(String mimeType) {
        super(String.format(MESSAGE, mimeType));
    }
}
