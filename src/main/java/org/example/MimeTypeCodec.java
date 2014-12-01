package org.example;

import java.io.IOException;

/**
 * Defines an abstract codec class responsible of encoding outgoing and decoding
 * incoming HTTP messages.
 * </p>
 * These encoding and decoding capabilities should be advertised accordingly using the
 * {@link #getEncodingMimeType()} and {@link #getDecodingMimeType()} methods.
 *
 * @author Duncan Attard
 */
public abstract class MimeTypeCodec {

    /**
     * Encodes the specified {@code object} into an array of bytes.
     *
     * @param object
     *         The object to encode.
     *
     * @return An array of bytes representing the encoded object. If {@code null} was
     * specified as the {@code object}, {@code null} is returned instead.
     *
     * @throws IOException
     *         An error occurred while encoding {@code object}.
     */
    public final byte[] encode(Object object) throws IOException {

        if (object == null) {
            return null;
        }

        return doEncode(object);
    }

    /**
     * Decodes the specified array of bytes into a new object specified by {@code type}.
     *
     * @param bytes
     *         The array of bytes to decode.
     * @param type
     *         The object type to which the array of bytes is to be decoded.
     * @param <T>
     *         The type parameter {@code T}.
     *
     * @return A new decoded object of type {@code type}. If {@code null} was
     * specified as the array of bytes, {@code null} is returned instead.
     *
     * @throws IOException
     *         An error occurred while decoding {@code bytes}.
     */
    public final <T> T decode(byte[] bytes, Class<T> type) throws IOException {

        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }

        if (bytes == null) {
            return null;
        }

        return doDecode(bytes, type);
    }

    /**
     * Template method which should handle the encoding of {@code object} into an
     * array of bytes.
     *
     * @param object
     *         The object to encode.
     *
     * @return An array of bytes representing the encoded object.
     *
     * @throws IOException
     *         An error occurred while encoding {@code object}.
     */
    public abstract byte[] doEncode(Object object) throws IOException;

    /**
     * Template method which should handle the decoding of {@code bytes} into a
     * new object of type {@code type}.
     *
     * @param bytes
     *         The array of bytes to decode.
     * @param type
     *         The object type to which the array of bytes is to be decoded.
     * @param <T>
     *         The type parameter {@code T}.
     *
     * @return A new decoded object of type {@code type}.
     *
     * @throws IOException
     *         An error occurred while decoding {@code bytes}.
     */
    public abstract <T> T doDecode(byte[] bytes, Class<T> type) throws IOException;

    /**
     * Returns the {@link RestClient.MimeType} this codec is able to encode.
     *
     * @return The {@link RestClient.MimeType} this codec is able to encode.
     */
    public abstract RestClient.MimeType getEncodingMimeType();

    /**
     * Returns the {@link RestClient.MimeType} this codes is able to decode.
     *
     * @return The {@link RestClient.MimeType} this codes is able to decode.
     */
    public abstract RestClient.MimeType getDecodingMimeType();
}
