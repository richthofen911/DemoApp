package richthofen.app.bluechat.bluetoothchat;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import richthofen.app.bluechat.FragmentTabAdapter;
import richthofen.app.bluechat.bluechat.R;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Manifest;

public class MainActivity extends FragmentActivity {

    public static final String TAG = "MainActivity";

    public static List<Fragment> fragments = new ArrayList<Fragment>();
    private RadioGroup radioGroup;

    //public static boolean in_out = true;

    private Intent i = null;
    public static String macAddress;

    public static Fragment fragment_inout;
    public static Fragment fragment_beam;
    public static Fragment fragment_list;
    public static Fragment fragment_referral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragment_inout = new InoutFragment();
        fragment_beam = new BeamPingFragment();
        fragment_list = new LisstFragment();
        fragment_referral = new ReferralFragment();

        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);

        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        macAddress = getMacAddress(getApplicationContext(), wifiManager);
        i= new Intent(this, RECOBackgroundMonitoringService.class);
        i.putExtra("macAddress", macAddress);
        this.startService(i);
        //mRECOService = new RECOBackgroundMonitoringService(getActivity())
        Toast.makeText(getApplicationContext(), "service started", Toast.LENGTH_SHORT).show();

        fragments.add(fragment_inout);
        fragments.add(fragment_beam);
        fragments.add(fragment_list);
        fragments.add(fragment_referral);

        radioGroup = (RadioGroup) findViewById(R.id.tabs_rg);

        FragmentTabAdapter fragmentTabAdapter = new FragmentTabAdapter(this, fragments, R.id.tab_content, radioGroup);

        fragmentTabAdapter.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener(){
            @Override
            public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {
                System.out.println("Extra---- " + index + " checked!!! ");
            }
        });
    }

    public static String getMacAddress(Context context, WifiManager wifiManager) {
        String macStr;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getMacAddress() != null) {
            macStr = wifiInfo.getMacAddress();
        } else {
            macStr = "null";
        }
        return macStr;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.stopService(i);
    }
}
