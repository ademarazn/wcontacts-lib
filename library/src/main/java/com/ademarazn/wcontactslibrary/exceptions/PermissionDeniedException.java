package com.ademarazn.wcontactslibrary.exceptions;

/**
 * @author ademarazn
 * @since 02/08/2019
 */
public class PermissionDeniedException extends Exception {
    private static final String MESSAGE = "Permission denied for %s";

    public PermissionDeniedException(String permission) {
        super(String.format(MESSAGE, permission));
    }
}
