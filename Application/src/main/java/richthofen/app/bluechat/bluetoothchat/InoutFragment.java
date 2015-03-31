package richthofen.app.bluechat.bluetoothchat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import org.apache.http.client.HttpClient;
import org.apache.http.util.EncodingUtils;

import java.sql.SQLException;
import java.util.List;

import richthofen.app.bluechat.bluechat.R;

public class InoutFragment extends Fragment {

    ToggleStatus toggleStatus1;
    DatabaseHelper databaseHelper;

    String postData_prefix = "macaddress=";
    String postData_suffix = "&activity=inout";
    HTTPClient httpClient;
    String htmlResult = "";
    WebView webView;
//    HitServerWebView hitServerWebView;

    private static RuntimeExceptionDao<ToggleStatus, Integer> mToggleStatusDao;

    private static final String URL = "http://54.149.146.72/inout.php";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("MACAFROM MAINACTIVITY ", "" + MainActivity.macAddress);
        View rootView;
        databaseHelper = new DatabaseHelper(getActivity().getApplicationContext());
        mToggleStatusDao = databaseHelper.getToggleStautsDataDao();
        toggleStatus1 = searchToggleStatus("privacy1");
        String postData = postData_prefix + MainActivity.macAddress + postData_suffix;
        Log.e("postincludingmac", postData);
        if (toggleStatus1 == null){
            rootView = inflater.inflate(R.layout.fragment_inout, container, false);
            WebView webView = (WebView) rootView.findViewById(R.id.wv_inout);
            WebSettings setting = webView.getSettings();
            //setting.setJavaScriptEnabled(true);
            //webView.loadUrl(URL);
            webView.postUrl(URL, EncodingUtils.getBytes(postData, "BASE64"));

            return rootView;
        }
        else
            if(toggleStatus1.getStatus().equals("ON")){
                rootView = inflater.inflate(R.layout.fragment_inout, container, false);
                webView = (WebView) rootView.findViewById(R.id.wv_inout);
                WebSettings setting = webView.getSettings();
                setting.setJavaScriptEnabled(true);
                //webView.loadUrl(URL);
                webView.postUrl(URL, EncodingUtils.getBytes(postData, "BASE64"));
                return rootView;
            }else{
                rootView = inflater.inflate(R.layout.fragment_inout_off, container, false);
                TextView textView = (TextView) rootView.findViewById(R.id.inout_off);
                textView.setText("In/Out setting in Privacy is off,\nturn it on to see the content");
                return rootView;
            }
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.optionmenu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.privacy: {
                Intent serverIntent = new Intent(getActivity(), PrivacySettings.class);
                startActivity(serverIntent);
                return true;
            }
            case R.id.beaconid: {
                Intent beaconId = new Intent(getActivity(), BeaconID.class);
                startActivity(beaconId);
                return true;
            }
            case R.id.analytics: {
                Intent analyticsIntent = new Intent(getActivity(), Analytics.class);
                startActivity(analyticsIntent);
                return true;
            }
        }
        return false;
    }

    public static ToggleStatus searchToggleStatus(String name){
        try{
            List<ToggleStatus> toggleStatuses = mToggleStatusDao.queryBuilder().where().eq("name", name).query();
            if (toggleStatuses.size() > 0)
                return toggleStatuses.get(0);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

}
