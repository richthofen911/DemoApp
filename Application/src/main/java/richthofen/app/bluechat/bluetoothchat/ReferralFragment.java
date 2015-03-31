package richthofen.app.bluechat.bluetoothchat;

/**
 * Created by richthofen80 on 3/9/15.
 */
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import richthofen.app.bluechat.bluechat.R;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import org.apache.http.util.EncodingUtils;

import java.util.List;
import java.sql.SQLException;

public class ReferralFragment extends Fragment {

    ToggleStatus toggleStatus4;
    DatabaseHelper databaseHelper;

    String activity = "referral";
    String referralContent = "";
    WebView webView_referral;

    private static RuntimeExceptionDao<ToggleStatus, Integer> mToggleStatusDao;

    @Override
    public void onAttach(Activity activity) {super.onAttach(activity); }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView;
        String macaddress = MainActivity.macAddress;
        databaseHelper = new DatabaseHelper(getActivity().getApplicationContext());
        mToggleStatusDao = databaseHelper.getToggleStautsDataDao();
        toggleStatus4 = searchToggleStatus("privacy1");
        if (toggleStatus4 == null){
            rootView = inflater.inflate(R.layout.fragment_referral, container, false);
            Button button = (Button) rootView.findViewById(R.id.btn_sendEmail);
            webView_referral = (WebView) rootView.findViewById(R.id.wv_referral);
            GetReferralContent getReferralContent = new GetReferralContent();
            getReferralContent.execute(macaddress, activity);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    //        Log.e("todo", tobe);
                    String html = "";
                    //String html = "<!DOCTYPE html><html><body>Hello<a href=\"http://www.google.ca\">Google</a></body></html>";
                    Intent intentEmail = new Intent(android.content.Intent.ACTION_SEND);
                    intentEmail.putExtra(android.content.Intent.EXTRA_SUBJECT, "REFERRAL");
                    intentEmail.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(referralContent));
                    intentEmail.setType("text/html");
                    startActivity(Intent.createChooser(intentEmail, "Mail Chooser"));
                }
            });
            return rootView;
        }else{
            if(toggleStatus4.getStatus().equals("ON")){
                rootView = inflater.inflate(R.layout.fragment_referral, container, false);
                Button button = (Button) rootView.findViewById(R.id.btn_sendEmail);
                webView_referral = (WebView) rootView.findViewById(R.id.wv_referral);
                GetReferralContent getReferralContent = new GetReferralContent();
                getReferralContent.execute(macaddress, activity);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        //        Log.e("todo", tobe);
                        String html = "";
                        //String html = "<!DOCTYPE html><html><body>Hello<a href=\"http://www.google.ca\">Google</a></body></html>";
                        Intent intentEmail = new Intent(android.content.Intent.ACTION_SEND);
                        intentEmail.putExtra(android.content.Intent.EXTRA_SUBJECT, "REFERRAL");
                        intentEmail.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(referralContent));
                        intentEmail.setType("text/html");
                        startActivity(Intent.createChooser(intentEmail, "Mail Chooser"));
                    }
                });
                return rootView;
            }else{
                rootView = inflater.inflate(R.layout.fragment_referral_off, container, false);
                TextView textView = (TextView) rootView.findViewById(R.id.referral_off);
                textView.setText("Referral setting in Privacy is off,\nturn it on to see the content");
                return rootView;
            }
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

    public class GetReferralContent extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = "";
            HTTPClient httpClient = new HTTPClient();
            result = httpClient.postRequest_getReferralContent(params[0], params[1]);
            return result;
        }
        protected void onPostExecute(String result){
            webView_referral.loadData(result, "text/html", "utf-8");
            referralContent = result;
        }
    }

}
