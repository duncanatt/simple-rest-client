package org.example;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;

/**
 * Handles the encoding of HTTP BASIC authentication credentials.
 *
 * @author Duncan Attard
 */
public final class BasicAuthCredentials implements AuthCredentials {

    /**
     * The authentication user name.
     */
    private String username;

    /**
     * The authentication password.
     */
    private String password;

    /**
     * Creates and initializes the {@link BasicAuthCredentials} with the specified credentials.
     *
     * @param username The authentication user name.
     * @param password The authentication password.
     */
    public BasicAuthCredentials(String username, String password) {

        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }

        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        // Initialize.
        this.username = username;
        this.password = password;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAuthToken() throws UnsupportedEncodingException {

        // Prepare credentials for Base64 encoding to use for BASIC authentication.
        String credentials = username + ":" + password;

        // Compute the BASIC authentication token.
        return "Basic " + DatatypeConverter.printBase64Binary(credentials.getBytes("UTF-8"));
    }

    /**
     * Returns the authentication user name.
     *
     * @return The authentication user name.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the authentication password.
     *
     * @return The authentication password.
     */
    public String getPassword() {
        return password;
    }
}
