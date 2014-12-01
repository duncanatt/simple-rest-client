package org.example;

import java.io.IOException;
import java.util.Map;

/**
 * Created by duncan on 30/11/2014.
 */
public class SimpleRestClient extends RestClient {

    private MimeTypeCodec codec;

    public SimpleRestClient(String url, MimeTypeCodec codec, int connectionTimeoutMs, int readTimeoutMs) {
        super(url, connectionTimeoutMs, readTimeoutMs);
        this.codec = codec;
    }

//    public <T> T create(String resource, Object body, Class<T> type, Map<String, String> requestProperties) throws IOException {
//        return codec.decode(doRequest(resource, codec.encode(body), HttpMethod.POST, codec.getEncodingMimeType(), codec.getDecodingMimeType(), requestProperties), type);
//    }

    public <T> T read(String resource, Object body, Class<T> type, Map<String, String> requestProperties) throws IOException {
        return codec.decode(
                doRequest(resource,
                        codec.encode(body),
                        HttpMethod.GET,
                        codec.getEncodingMimeType(),
                        codec.getDecodingMimeType(),
                        requestProperties),
                type
        );
    }


}
