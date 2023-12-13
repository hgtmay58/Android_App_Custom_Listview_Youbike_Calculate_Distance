package com.example.myapplicationcustlist_youbike;

import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class okhttp_util {
    static OkHttpClient client;

    public okhttp_util()
    {
        client= new OkHttpClient();
    }

    public static String urlget(String urls)
    {
        Log.d(" TAG ", urls);
        Request request = new Request.Builder()
                .url(urls)
                .build();

        try {
            Response responses = client.newCall(request).execute();
            if (responses.isSuccessful())
                return responses.body().string();
            else
                return "error in getting response ";
        } catch (IOException e) {
            Log.e(" TAG ", "error in getting response get request okhttp");
            return "error in call get response ";
        }


    }
    public String urlpost(String urls)
    {
        Log.d(" TAG ", urls);
        Uri uri= Uri.parse(urls);
        String path=uri.getPath();
        Log.d(" TAG ", path);
        String q=uri.getQuery();
        Log.d(" TAG ", q);
        Set<String> args = uri.getQueryParameterNames();
        Object[] a=args.toArray();
        String url = "http://"+uri.getAuthority()+path;
        Log.d(" TAG ", url);

        FormBody.Builder formBody = new FormBody.Builder();
        for(int i=0;i<a.length;i++) {
            formBody.add(a[i].toString(),uri.getQueryParameter(a[i].toString()));
            Log.d(" TAG ", a[i].toString()+","+uri.getQueryParameter(a[i].toString()));
        }
        RequestBody formBodys = formBody.build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBodys)
                .build();


        try {
            Response responses = client.newCall(request).execute();
            if (responses.isSuccessful())
                return responses.body().string();
            else
                return "error in postting response ";
        } catch (IOException e) {
            Log.e(" TAG ", "error in getting response get request okhttp");
            return "error in call post response ";
        }



    }
    public String urljson(String url, String jsonStr)
    {
        final MediaType mediaType  = MediaType.parse("application/json");
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(mediaType, jsonStr))
                .build();

        try {
            Response responses = client.newCall(request).execute();
            if (responses.isSuccessful())
                return responses.body().string();
            else
                return "error in postting json response ";
        } catch (IOException e) {
            Log.e(" TAG ", "error in getting response get request okhttp");
            return "error in call post json response ";
        }
    }
    public String urlupload(String url, String filename, String sid, String fpath)
    {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id", sid)
                .addFormDataPart("filename", filename, RequestBody.create(MediaType.parse("application/xml"), new File(fpath)))
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try {
            Response responses = client.newCall(request).execute();
            if (responses.isSuccessful())
                return responses.body().string();
            else
                return "error in upload file response ";
        } catch (IOException e) {
            Log.e(" TAG ", "error in uploadfile response get request okhttp");
            return "error in upload file response ";
        }
    }
}
