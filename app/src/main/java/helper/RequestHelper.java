package helper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import activity.StartGameActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class RequestHelper {
    public static void httpGetRequest(String urlBase, String data, Callback callback) {
        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .retryOnConnectionFailure(false)
                .connectTimeout(3600, TimeUnit.SECONDS)
                .readTimeout(3600, TimeUnit.SECONDS)
                .writeTimeout(3600, TimeUnit.SECONDS)
                .build();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(urlBase).newBuilder();
        urlBuilder.addQueryParameter("JSON", data);

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public static void httpPostRequest(String url, String data, Callback callback) {
        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .retryOnConnectionFailure(false)
                .connectTimeout(3600, TimeUnit.SECONDS)
                .readTimeout(3600, TimeUnit.SECONDS)
                .writeTimeout(3600, TimeUnit.SECONDS)
                .build();


//        RequestBody requestBody = null;
//        try {
        FormBody requestBody = new FormBody.Builder()
                .add("JSON", data)
//                    .addFormDataPart("JSON", URLEncoder.encode(data, "utf-8"))
                .build();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }


    public static void httpPutRequest(String url, String data, Callback callback) {
        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .retryOnConnectionFailure(false)
                .connectTimeout(3600, TimeUnit.SECONDS)
                .readTimeout(3600, TimeUnit.SECONDS)
                .writeTimeout(3600, TimeUnit.SECONDS)
                .build();

        FormBody requestBody = new FormBody.Builder()
                .add("JSON", data)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .put(requestBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }
}
