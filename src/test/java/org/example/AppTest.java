package org.example;


import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Unit test for simple App.
 */
public class AppTest 
{

    @Test
    public void test() throws IOException {

        //RestClient client = new RestClient("http://th02.deviantart.net/fs70/PRE/f/2010/171/4/0/Paint_Splash_PNG_by_AbsurdWordPreferred.png", 5000, 5000);
        //CrudRestClient client = new JsonRestClient("http://requestb.in/14q90lu1", 5000, 5000);
        //RestClient client = new JsonRestClient("http://www.mocky.io/v2/547a312fd020ce880756e8a0", 5000, 5000);

        SimpleRestClient client = new SimpleRestClient("http://www.mocky.io/v2/547a312fd020ce880756e8a0", new JsonMimeTypeCodec(), 5000, 5000);
        client.setCredentials(new BasicAuthCredentials("duncan", "attard"));


        Map<String, String> requestProperties = new HashMap<>();
        requestProperties.put("Accept", "application/xml");

        Object obj = client.read(null, "{\"name\":\"joan\"}", Object.class, requestProperties);
        System.out.println("OBJ = " + obj);
    }

    @Test
    public void testReplace() {

        //String url = "http://www.mocky.io/v2/547a312fd020ce880756e8a0/";
        String url = "http://fc03.deviantart.net/fs70/f/2013/012/e/c/png_cookie_by_ellatutorials-d5r8nel.png";
        String resource = "/hello";


        //String resourceUrl = createFullResourceUrl(url, resource);
        //System.out.println("resourceUrl = " + resourceUrl);

        //String normalizedUrl = normalizeUrl(url);
        //System.out.println("Norm = " + normalizedUrl);

    }

    private String createFullResourceUrl(String url, String resource) {

        String fullUrl = url;

        if (!url.endsWith("/")) {
            fullUrl += "/";
        }

        if (resource.startsWith("/")) {
            fullUrl += resource.substring(1);
        }
        else {
            fullUrl += resource;
        }

        return fullUrl;
    }




}
