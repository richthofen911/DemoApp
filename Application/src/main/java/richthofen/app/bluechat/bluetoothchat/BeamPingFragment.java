package richthofen.app.bluechat.bluetoothchat;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import richthofen.app.bluechat.bluechat.R;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.List;
import java.sql.SQLException;

public class BeamPingFragment extends Fragment {

    private static String url_prefix = "http://54.149.146.72/inout.php?activity=poke&macaddress=";
    ToggleStatus toggleStatus2;
    DatabaseHelper databaseHelper;
    WebView webView;

    private static RuntimeExceptionDao<ToggleStatus, Integer> mToggleStatusDao;

    @Override
    public void onAttach(Activity activity) {super.onAttach(activity); }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView;
        String url = url_prefix + MainActivity.macAddress;
        databaseHelper = new DatabaseHelper(getActivity().getApplicationContext());
        mToggleStatusDao = databaseHelper.getToggleStautsDataDao();
        toggleStatus2 = searchToggleStatus("privacy2");
        if (toggleStatus2 == null){
            rootView = inflater.inflate(R.layout.fragment_beam, container, false);
            webView = (WebView) rootView.findViewById(R.id.wv_beam);
            WebSettings setting = webView.getSettings();
            setting.setJavaScriptEnabled(true);
            webView.loadUrl(url);
            //webView.postUrl(URL, EncodingUtils.getBytes(postData, "BASE64"));

            return rootView;
        }
        else
        if(toggleStatus2.getStatus().equals("ON")){
            rootView = inflater.inflate(R.layout.fragment_beam, container, false);
            webView = (WebView) rootView.findViewById(R.id.wv_beam);
            WebSettings setting = webView.getSettings();
            setting.setJavaScriptEnabled(true);
            Log.e("urlbeam", url);
            webView.loadUrl(url);
            //webView.postUrl(URL, EncodingUtils.getBytes(postData, "BASE64"));
            return rootView;
        }else{
            rootView = inflater.inflate(R.layout.fragment_beam_off, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.beam_off);
            textView.setText("Beam setting in Privacy is off,\nturn it on to see the content");
            return rootView;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) { super.onActivityCreated(savedInstanceState); }
    @Override
    public void onStart() {super.onStart();}
    @Override
    public void onResume() {super.onResume();}
    @Override
    public void onPause() { super.onPause(); }
    @Override
    public void onStop() { super.onStop(); }
    @Override
    public void onDestroyView() { super.onDestroyView(); }
    @Override
    public void onDestroy() { super.onDestroy(); }
    @Override
    public void onDetach() { super.onDetach(); }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.optionmenu, menu);
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
}
