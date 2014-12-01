package org.example;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by duncan on 23/11/2014.
 */
public abstract class RestClient {

    /**
     * The base URL to which the client is to connect to.
     */
    private String url;

    /**
     * The connection timeout in milliseconds.
     */
    private int connectionTimeoutMs;

    /**
     * The read timeout in milliseconds.
     */
    private int readTimeoutMs;

    /**
     * The optional authentication credentials.
     */
    private AuthCredentials credentials;

    /**
     * Creates and initializes a new instance of the {@link RestClient} class with the specified
     * arguments.
     *
     * @param url
     *         The base URL to which the client is to connect to.
     * @param connectionTimeoutMs
     *         The connection timeout in milliseconds.
     * @param readTimeoutMs
     *         The read timeout in milliseconds.
     */
    public RestClient(String url, int connectionTimeoutMs, int readTimeoutMs) {

        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("URL cannot be empty");
        }

        // Normalize and initialize.
        this.url = normalizeUrl(url);
        this.connectionTimeoutMs = Math.abs(connectionTimeoutMs);
        this.readTimeoutMs = Math.abs(readTimeoutMs);
    }

    /**
     * Reads all of the specified input stream as an array of bytes.
     *
     * @param input
     *         The input stream to read.
     *
     * @return An array of bytes containing all of the input stream.
     *
     * @throws IOException
     *         An error occurred while reading from the input stream.
     */
    private static byte[] readBytes(InputStream input) throws IOException {

        // The array containing all of the input stream data.
        byte[] bytes = new byte[0];

        // The number of bytes to read at one go.
        byte[] buffer = new byte[512];

        // The number of actual bytes read.
        int bytesRead;

        while ((bytesRead = input.read(buffer, 0, buffer.length)) != -1) {

            // Create new enlarged array to contain old and new data.
            byte[] temp = new byte[bytes.length + bytesRead];

            // Copy the original byte array.
            System.arraycopy(bytes, 0, temp, 0, bytes.length);

            // Copy the newly read data after the original bytes.
            System.arraycopy(buffer, 0, temp, bytes.length, bytesRead);

            // Set the bytes array to the updated array.
            bytes = temp;
        }

        return bytes;
    }

    /**
     * Normalizes the specified URL by making sure it does not end with a
     * trailing forward slash '/'.
     *
     * @param url
     *         The URL to normalize.
     *
     * @return The normalized URL.
     */
    private static String normalizeUrl(String url) {

        if (url.endsWith("/")) {
            return url.substring(0, url.length() - 1);
        }

        return url;
    }

    /**
     * Creates a fully qualified resource URL using the specified base URL and the {@code resource} name.
     *
     * @param url
     *         The base URL at which {@code resource} is located.
     * @param resource
     *         The resource to access.
     *
     * @return The fully qualified resource URL.
     *
     * @throws MalformedURLException
     *         An error occurred while creating the resource URL.
     */
    private static URL createResourceUrl(String url, String resource) throws MalformedURLException {

        URL resourceUrl;

        if (resource == null || resource.isEmpty()) {

            // Resource is empty - return original URL.
            resourceUrl = new URL(url);
        }
        else {

            // Normalize resource URL.
            String normalizedResource = normalizeUrl(resource);

            if (normalizedResource.startsWith("/")) {
                resourceUrl = new URL(url + resource);
            }
            else {
                resourceUrl = new URL(url + "/" + resource);
            }
        }

        return resourceUrl;
    }

    /**
     * Closes the specified {@code stream}.
     *
     * @param stream The stream to close.
     */
    private static void closeStream(Closeable stream) {

        if (stream != null) {
            try {
                stream.close();
            }
            catch (IOException ex) {
                System.out.println("WARNING: Unable to close stream. Cause: " + ex.getMessage());
            }
        }
    }


    /**
     * Makes a request to the specified {@code resource}, using the optional {@code body} of bytes as a payload.
     * </p>
     * The
     *
     * @param resource
     * @param body
     * @param method
     * @param accept
     * @param contentType
     * @param requestProperties
     *
     * @return
     *
     * @throws IOException
     */
    protected final byte[] doRequest(String resource, byte[] body, HttpMethod method,
                                     MimeType accept, MimeType contentType,
                                     Map<String, String> requestProperties) throws IOException {

        // The connection used to make the request.
        HttpURLConnection connection = null;

        // The output and input streams used to write and read data
        // to/from the server.
        OutputStream output = null;
        InputStream input = null;

        try {

            // Open a new connection to the resource.
            connection = openResourceConnection(resource, body, method, accept, contentType, requestProperties);

            if (body != null) {

                // Write request to server.
                output = connection.getOutputStream();
                output.write(body);
            }

            // Read response from server.
            input = connection.getInputStream();
            byte[] bytes = readBytes(input);

            return bytes;
        }
        finally {

            // Close output and input streams.
            closeStream(output);
            closeStream(input);

            // Disconnect connection to close underlying socket.
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private void addMimeTypes() {

    }

    private HttpURLConnection openResourceConnection(String resource, byte[] body, HttpMethod method,
                                                     MimeType accept, MimeType contentType,
                                                     Map<String, String> requestProperties) throws IOException {

        // Create full resource URL.
        URL resourceUrl = createResourceUrl(url, resource);

        // Open connection to server and read response.
        return openConnection(resourceUrl, body, method, accept, contentType, requestProperties);
    }

    public HttpURLConnection openConnection(URL url, byte[] body, HttpMethod method,
                                            MimeType accept, MimeType contentType,
                                            Map<String, String> requestProperties) throws IOException {

        // Create a new HTTP connection using the specified URL.
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Configure HTTP connection method.
        connection.setRequestMethod(method.name);
        connection.setDoOutput(body != null);

        // Configure timeouts.
        connection.setConnectTimeout(connectionTimeoutMs);
        connection.setReadTimeout(readTimeoutMs);

        // Add Accept and Content-Type headers.
        connection.addRequestProperty("Accept", accept.getMediaString());
        connection.addRequestProperty("Content-Type", contentType.getMediaString());

        // Add Authorization header and token if present.
        if (credentials != null) {
            connection.addRequestProperty("Authorization", credentials.getAuthToken());
        }

        // Finally add all other request properties if present.
        if (requestProperties != null && !requestProperties.isEmpty()) {
            for (Map.Entry<String, String> requestProperty : requestProperties.entrySet()) {
                connection.setRequestProperty(requestProperty.getKey(), requestProperty.getValue());
            }
        }

        return connection;
    }

    public AuthCredentials getCredentials() {
        return credentials;
    }

    public void setCredentials(AuthCredentials credentials) {
        this.credentials = credentials;
    }


    public static enum MimeType {

        ALL("*/*"),

        JSON("application/json"),

        PNG("image/png");


        private String mediaString;

        private MimeType(String media) {
            this.mediaString = media;
        }

        public String getMediaString() {
            return mediaString;
        }
    }

    public static enum HttpMethod {

        GET("GET", true),

        POST("POST", true),

        PUT("PUT", true),

        DELETE("DELETE", true);

        private String name;

        private boolean write;

        private HttpMethod(String name, boolean write) {
            this.name = name;
            this.write = write;
        }
    }
}
