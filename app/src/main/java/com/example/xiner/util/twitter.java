package com.example.xiner.util;

/**
 * Created by xiner on 14-12-23.
 */
import com.loopj.android.http.*;

public class twitter {
 //   private static final String BASE_URL = "http://api.twitter.com/1/";
private static String Studyingurl;
    private static AsyncHttpClient client = new AsyncHttpClient();

    static {
        client.setBasicAuth("18366116016", "..xiao");
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }

   // private static String getAbsoluteUrl(String relativeUrl) {
     //   return BASE_URL + relativeUrl;
  //  }
}