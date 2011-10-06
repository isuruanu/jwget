package com.filipovskii.jwget.http;

import com.filipovskii.jwget.common.IDownloadData;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Implement equals and hashCode
 */
public final class HttpDownloadData implements IDownloadData {

  public static final String URL_KEY = "url";
  public static final String PATH_KEY = "path";

  public static HttpDownloadData parseFrom(Map<String, String> properties) {
    Map<String, String> propCopy = new HashMap(properties);
    String url = propCopy.get(URL_KEY);
    String path = propCopy.get(PATH_KEY);
    propCopy.remove(URL_KEY);
    propCopy.remove(PATH_KEY);

    return new HttpDownloadData(
        url, path, Collections.unmodifiableMap(propCopy));
  }

  private final String url;
  private final String path;
  private final Map<String, String> properties;

  public HttpDownloadData(
      String url,
      String path,
      Map<String, String> properties) {
    this.url = url;
    this.path = path;
    this.properties = properties;
  }

  public String getUrl() {
    return url;
  }

  public String getDownloadPath() {
    return path;
  }

  public Map<String, String> getProperties() {
    return properties;
  }

  @Override
  public String toString() {
    return "Http download from " + getUrl() + " to " + getDownloadPath();
  }
}
