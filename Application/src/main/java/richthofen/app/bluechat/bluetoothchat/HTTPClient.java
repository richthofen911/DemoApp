package richthofen.app.bluechat.bluetoothchat;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class HTTPClient {

    private static String url_auth = "http://54.149.146.72/auth.php";
    private static String url_checkinout = "http://54.149.146.72/index.php";
    private static String url_webview = "http://54.149.146.72/inout.php";
    private static String url_addListDetail = "http://54.149.146.72/list.php";
    private static String url_getReferral = "http://54.149.146.72/index.php";

    private String json;

    private int result;
    private String uuid;
    private String major;
    private String minor;
    private String macaddress;
    private String message;

    public String getJson() { return json; }
    public void setJson(String json) {
        this.json = json;
    }
    public String getMacaddress() { return macaddress; }
    public void setMacaddress(String macaddr) { this.macaddress = macaddr; }

    public void parseJSON(String input) {
        json = input.replace("\\", "");
        try {
            JSONObject jsonObject = new JSONObject(json);
            result = jsonObject.getInt("result");
            if (result == 1){
                uuid = jsonObject.getString("uuid");
                major = jsonObject.getString("major");
                minor = jsonObject.getString("minor");
            }
            else
                message = jsonObject.getString("message");
            }
        catch (JSONException e) { e.printStackTrace(); }
    }

    public static String postRequest_auth(String MacAddr) {
        //      CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url_auth);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("macaddress", MacAddr));
        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            String comeIn;
            if (entity != null)
                comeIn = EntityUtils.toString(entity, "UTF-8");
            else
                comeIn = "receiveError";
            return comeIn;
        } catch (Exception e) {}
        return "not received anything";
    }

    public static String postRequest_checkinout(String MacAddr, String presence) {
  //      CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url_checkinout);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("macaddress", MacAddr));
        nameValuePairs.add(new BasicNameValuePair("presence", presence));
        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            String comeIn;
            if (entity != null)
                comeIn = EntityUtils.toString(entity, "UTF-8");
            else
                comeIn = "receiveError";
            return comeIn;
        } catch (Exception e) {}
        return "not received anything";
    }

    public static String postRequest_addListDetail(String activity, String listname, String newitem) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url_addListDetail);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("activity", activity));
        nameValuePairs.add(new BasicNameValuePair("listname", listname));
        nameValuePairs.add(new BasicNameValuePair("newitem", newitem));
        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            String comeIn;
            if (entity != null)
                comeIn = EntityUtils.toString(entity, "UTF-8");
            else
                comeIn = "receiveError";
            return comeIn;
        } catch (Exception e) {}
        return "not received anything";
    }

    public static String postRequest_webView(String macaddress, String activity) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url_webview);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("macaddress", macaddress));
        nameValuePairs.add(new BasicNameValuePair("activity", activity));
        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            String comeIn;
            if (entity != null)
                comeIn = EntityUtils.toString(entity, "UTF-8");
            else
                comeIn = "receiveError";
            return comeIn;
        } catch (Exception e) {}
        return "not received anything";
    }

    public static String postRequest_getReferralContent(String macaddress, String activity) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url_getReferral);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("macaddress", macaddress));
        nameValuePairs.add(new BasicNameValuePair("activity", activity));
        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            String comeIn;
            if (entity != null)
                comeIn = EntityUtils.toString(entity, "UTF-8");
            else
                comeIn = "receiveError";
            return comeIn;
        } catch (Exception e) {}
        return "not received anything";
    }

    public static String getRequest() {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet();
        try{
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            String comeIn;
            if (entity != null)
                comeIn = EntityUtils.toString(entity, "UTF-8");
            else
                comeIn = "receiveError";
            return comeIn;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return "not received anything";
    }
}
