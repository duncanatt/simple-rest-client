package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * An implementation for encoding and decoding JSON HTTP messages.
 *
 * @author Duncan Attard
 */
public class JsonMimeTypeCodec extends MimeTypeCodec {

    /**
     * The object mapper encoding and decoding JSON objects.
     */
    public ObjectMapper mapper;

    public JsonMimeTypeCodec() {
        this.mapper = new ObjectMapper();
    }

    /**
     * Encodes the specified object into JSON, and returns the result as a
     * byte array.
     *
     * @param object
     *         The object to encode.
     *
     * @return The encoded object as JSON in a byte array.
     *
     * @throws IOException
     *         An error occurred while encoding {@code object} to JSON.
     */
    @Override
    public byte[] doEncode(Object object) throws IOException {
        return mapper.writeValueAsBytes(object);
    }

    /**
     * Decodes the specified JSON in a byte array and converts the result to the type
     * specified by {@code type}.
     *
     * @param bytes
     *         The array of bytes to decode.
     * @param type
     *         The object type to which the array of bytes is to be decoded.
     * @param <T>
     *         The type parameter T.
     *
     * @return A new decoded object of type {@code type}.
     *
     * @throws IOException
     *         An error occurred while decoding {@code bytes} from JSON.
     */
    @Override
    public <T> T doDecode(byte[] bytes, Class<T> type) throws IOException {
        return mapper.readValue(bytes, type);
    }

    /**
     * Returns {@link RestClient.MimeType#JSON} as the mime type this codec is able to encode.
     *
     * @return {@link RestClient.MimeType#JSON} as the mime type this codec is able to encode.
     */
    @Override
    public RestClient.MimeType getEncodingMimeType() {
        return RestClient.MimeType.ALL;
    }

    /**
     * Returns {@link RestClient.MimeType#JSON} as the mime type this codec is able to decode.
     *
     * @return {@link RestClient.MimeType#JSON} as the mime type this codec is able to decode.
     */
    @Override
    public RestClient.MimeType getDecodingMimeType() {
        return RestClient.MimeType.JSON;
    }
}
