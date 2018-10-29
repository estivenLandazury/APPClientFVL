package com.example.landa.clientefvl.com;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;


import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Juliana on 20/08/2015.
 */
public class WEBUtilDomi {

    public static String JsonByPOSTrequestTwo(String url, String json) throws IOException {
        URL page = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) page.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.connect();

        String query = json;


        OutputStream os = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

        writer.write(query);
        writer.flush();

        if ((""+connection.getResponseCode()).startsWith("2")) {
            return connection.getResponseMessage();
        } else {
            byte[] buffer = new byte[4096];
            if(connection.getErrorStream() != null) {
                connection.getErrorStream().read(buffer);
                throw new IOException(new String(buffer).trim());
            } else throw new IOException("ERROR");

        }

    }


    //HACER EL POST REQUEST
    public static String POSTrequest(String url, Uri.Builder builder) throws IOException {
        URL page = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) page.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);

        String query = builder.build().getEncodedQuery();

        OutputStream os = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

        writer.write(query);
        writer.flush();
        writer.close();
        os.close();

        InputStream is = connection.getInputStream();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = is.read(buffer)) != -1) {
            bytes.write(buffer, 0, bytesRead);
        }
        is.close();
        connection.disconnect();

        return new String(bytes.toByteArray(), "UTF-8");
    }

    //HACER EL POST REQUEST
    public static final MediaType  JSON= MediaType.parse("application/json; charset=utf-8");

    public static String JsonByPOSTrequestOK(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        Log.e(">OKHTTP", response.body().string());
        return response.body().string();
    }

    public static String JsonByPOSTrequest(String url, String json) throws IOException {
        URL page = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) page.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoInput(true);
        connection.setDoOutput(true);

        /*
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init( null, new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        }}, new SecureRandom());

        connection.setSSLSocketFactory(sslContext.getSocketFactory());
        */

        String query = json;

        OutputStream os = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

        writer.write(query);
        writer.flush();

        InputStream is = connection.getInputStream();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;

        while ((bytesRead = is.read(buffer)) != -1) {
            bytes.write(buffer, 0, bytesRead);
        }
        is.close();
        writer.close();
        os.close();
        connection.disconnect();

        return new String(bytes.toByteArray(), "UTF-8");
    }


    //HACER EL GETREQUEST
    public static String GETrequest(String url) throws IOException {
        URL page = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) page.openConnection();

        InputStream is = connection.getInputStream();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = is.read(buffer)) != -1) {
            bytes.write(buffer, 0, bytesRead);
        }
        is.close();
        connection.disconnect();
        return new String(bytes.toByteArray(), "UTF-8");
    }

    //SIRVE PARA BAJAR IMAGENES DIRECTAMENTE PARA PONER EN UN IMAGEVIEW
    public static Bitmap getImagefromURL(String url) throws IOException {
        URL image_url = new URL(url);
        InputStream in = image_url.openStream();
        Bitmap image = BitmapFactory.decodeStream(in);
        return image;
    }
}