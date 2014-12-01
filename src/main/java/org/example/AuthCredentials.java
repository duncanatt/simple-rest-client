package org.example;

import java.io.UnsupportedEncodingException;

/**
 * Handles the encoding of the HTTP authentication credentials.
 *
 * @author Duncan Attard
 */
public interface AuthCredentials {

    /**
     * Returns the authentication token to be sent to the server.
     *
     * @return The encoded authentication token.
     * @throws UnsupportedEncodingException An error occurred while encoding the credentials.
     */
    String getAuthToken() throws UnsupportedEncodingException;
}
