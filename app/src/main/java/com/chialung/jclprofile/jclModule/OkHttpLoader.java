package com.chialung.jclprofile.jclModule;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

/*
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
*/

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by chialung on 2015/4/16.
 * 這個Loader主要是為了統合OkHttp的所有功能而製作
 * 製作參考 http://www.cnblogs.com/ct2011/p/4001708.html
 */
public class OkHttpLoader {

    private static final String CHARSET_NAME = "UTF-8";
    private static final int timeout = 30;  //秒
    private static final OkHttpClient client = new OkHttpClient();

    static {
        client.setConnectTimeout(timeout, TimeUnit.SECONDS);
    }

    /**
     * 不開啟異步線程
     * @param request
     * @return
     * @throws IOException
     */
    public static Response execute(Request request) throws IOException {
        return client.newCall(request).execute();
    }

    /**
     * 開啟異步線程
     * @param request
     * @param responseCallback
     */
    public static void enqueue(Request request, Callback responseCallback){
        client.newCall(request).enqueue(responseCallback);
    }

    /**
     * 開啟異步線程, 無返回結果（空的Callback）
     * @param request
     */
    public static void enqueue(Request request){
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

            }
        });
    }

    public static String getStringFromServer(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = execute(request);

        if (response.isSuccessful()){
            String responseUrl = response.body().string();
            return  responseUrl;
        }else {
            throw new IOException("Unexpected code " + response);
        }
    }

    /**
     * ?里使用了HttpClinet的API。只是?了方便
     * @param params
     * @return
     */
/*    public static String formatParams(List<BasicNameValuePair> params){
        return URLEncodedUtils.format(params, CHARSET_NAME);
    }

    *//**
     * ?HttpGet 的 url 方便的添加多?name value ??。
     * @param url
     * @param params
     * @return
     *//*
    public static String attachHttpGetParams(String url, List<BasicNameValuePair> params){
        return url + "?" + formatParams(params);
    }*/

    /**
     * ?HttpGet 的 url 方便的添加1?name value ??。
     * @param url
     * @param name
     * @param value
     * @return
     */
    public static String attachHttpGetParam(String url, String name, String value){
        return url + "?" + name + "=" + value;
    }
}
