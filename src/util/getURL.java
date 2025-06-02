package util;

import java.net.URL;

public class getURL {
    public static URL getURL(String url) {
        return getURL.class.getResource(url);
    }
}
