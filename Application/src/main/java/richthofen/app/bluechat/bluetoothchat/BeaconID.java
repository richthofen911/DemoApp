package richthofen.app.bluechat.bluetoothchat;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import richthofen.app.bluechat.bluechat.R;

public class BeaconID extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_id);

        TextView tv1 = (TextView) findViewById(R.id.beacon_uuid);
        TextView tv2 = (TextView) findViewById(R.id.beacon_major);
        TextView tv3 = (TextView) findViewById(R.id.beacon_minor);

        tv1.setText("UUID: " + RECOBackgroundMonitoringService.uuid);
        tv2.setText("MAJOR: " + RECOBackgroundMonitoringService.major);
        tv3.setText("MINOR: " + RECOBackgroundMonitoringService.minor);
    }


}
