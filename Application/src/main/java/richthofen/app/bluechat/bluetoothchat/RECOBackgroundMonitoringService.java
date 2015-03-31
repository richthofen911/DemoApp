package richthofen.app.bluechat.bluetoothchat;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.*;
import android.util.Log;

import android.widget.Toast;
import com.perples.recosdk.RECOBeaconManager;
import com.perples.recosdk.RECOBeaconRegion;
import com.perples.recosdk.RECOBeaconRegionState;
import com.perples.recosdk.RECOMonitoringListener;
import com.perples.recosdk.RECOServiceConnectListener;

import org.json.JSONException;
import org.json.JSONObject;

public class RECOBackgroundMonitoringService extends Service implements RECOMonitoringListener, RECOServiceConnectListener {
    HTTPClient httpClient;
    public String userId;
    SharedPreferences prefs;
    private Handler myHandler;

    private static final String RECO_UUID = "24DDF4118CF1440C87CDE368DAF9C93E";
    private static final Integer RECO_MAJOR = 17222;
    private static final String RECO_MINOR = "17993";

    private HitServerIn hitServerIn;
    private HitServerOut hitServerOut;
    private HitServerAuth hitServerAuth;

    protected RECOBeaconManager recoManager;
    protected ArrayList<RECOBeaconRegion> regions;

    public static String uuid;
    public static String major;
    public static String minor;

    private static final String TAG = "RECOBackgroundRangingService";
	
	private static int notificationID = 9999;


	private final IBinder binder = new MyBinder();
	
	private static long scanDuration = 1*1000L;
	private static long sleepDuration = 10*1000L;

    protected static boolean enterFlag = true;

    private String macAddress = "";

	private ArrayList<RECOBeaconRegion> monitoringRegions;

	
	@Override
	public void onCreate() {
		super.onCreate();

        myHandler = new Handler();

        prefs = this.getSharedPreferences(
                "com.facebook.samples.hellofacebook", Context.MODE_PRIVATE);

        this.recoManager = RECOBeaconManager.getInstance(getApplicationContext());

        httpClient=new HTTPClient();

        this.recoManager = RECOBeaconManager.getInstance(getApplicationContext());
        this.regions = generateBeaconRegion();

        this.recoManager.setMonitoringListener(this);
        this.recoManager.setScanPeriod(1*1000L);
        this.recoManager.setSleepPeriod(5*1000L);

        this.recoManager.bind(this);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
        macAddress = intent.getStringExtra("macAddress");
        hitServerAuth = new HitServerAuth();
        hitServerAuth.execute(macAddress);

        this.recoManager = RECOBeaconManager.getInstance(getApplicationContext());
        this.regions = generateBeaconRegion();

        this.recoManager.setMonitoringListener(this);
        this.recoManager.setScanPeriod(1*1000L);
        this.recoManager.setSleepPeriod(5*1000L);

        this.recoManager.bind(this);

		return START_STICKY;
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
        stop(this.regions);
        unbind();

	}

	

//
//	private void startMonitoring(ArrayList<RECOBeaconRegion> regions) {
//		this.recoManager.setScanPeriod(scanDuration);
//		this.recoManager.setSleepPeriod(sleepDuration);
//
//		for(RECOBeaconRegion region : regions) {
//			try {
//				region.setRegionExpirationTimeMillis(3*1000L);
//				this.recoManager.startMonitoringForRegion(region);
//			} catch (RemoteException e) {
//				Log.i(TAG, "Remote Exception");
//				e.printStackTrace();
//			} catch (NullPointerException e) {
//				Log.i(TAG, "Null Pointer Exception");
//				e.printStackTrace();
//			}
//		}
//	}
//
//    private void start() {
//        this.monitoringRegions = generateBeaconRegion();
//        this.rangingRegions = new ArrayList<RECOBeaconRegion>();
//
//        this.recoManager.setMonitoringListener(this);
//        this.recoManager.setRangingListener(this);
//
//        this.recoManager.bind(this);
//    }
//
//	private void stop() {
//		this.stopMonitoring(this.monitoringRegions);
//		if(!this.rangingRegions.isEmpty()) {
//			this.stopRanging(this.rangingRegions);
//		}
//		this.unbind();
//	}
//
//	public void stopMonitoring(ArrayList<RECOBeaconRegion> regions) {
//		for(RECOBeaconRegion region : regions) {
//			try {
//				this.recoManager.stopMonitoringForRegion(region);
//			} catch (RemoteException e) {
//				Log.i(TAG, "Remote Exception");
//				e.printStackTrace();
//			} catch (NullPointerException e) {
//				Log.i(TAG, "Null Pointer Exception");
//				e.printStackTrace();
//			}
//		}
//	}
	

	private void unbind() {
		try {
			this.recoManager.unbind();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

    @Override
    public void onServiceConnect() {
        //Toast.makeText(getApplicationContext(), macaddress, Toast.LENGTH_LONG).show();

        if (!this.recoManager.isMonitoringAvailable()) {
            try {
                this.recoManager.setBluetoothOn();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        start(this.regions);
    }

    @Override
    public void didDetermineStateForRegion(RECOBeaconRegionState recoRegionState, RECOBeaconRegion recoRegion) {

    }

    @Override
    public void didEnterRegion(RECOBeaconRegion recoRegion) {
        Toast.makeText(getApplicationContext(), "enter the beacon region", Toast.LENGTH_LONG).show();
//        //TODO something you want after entering region

//        if(enterFlag == false) {
//            myHandler.removeCallbacks(mExitRunnable);
//            enterFlag = true;
//        }
         hitServerIn = new HitServerIn();
         hitServerIn.execute();

    }

    @Override
    public void didExitRegion(RECOBeaconRegion recoRegion) {
        Toast.makeText(getApplicationContext(), "exit the beacon region region" , Toast.LENGTH_LONG).show();
        // TODO something you want after exiting region
        //if(enterFlag) {
        //    myHandler.postDelayed(mExitRunnable, 15000);
        //    enterFlag = false;
        //}
        hitServerOut = new HitServerOut();
        hitServerOut.execute();
    }

    @Override
    public void didStartMonitoringForRegion(RECOBeaconRegion recoRegion) {
        try {
            this.recoManager.requestStateForRegion(recoRegion);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NullPointerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private ArrayList<RECOBeaconRegion> generateBeaconRegion() {
        ArrayList<RECOBeaconRegion> regions = new ArrayList<RECOBeaconRegion>();

        RECOBeaconRegion recoRegion;
//		recoRegion = new RECOBeaconRegion(RECO_UUID, "RECO Sample Region");
        recoRegion = new RECOBeaconRegion(RECO_UUID, RECO_MAJOR, RECO_MINOR);
        regions.add(recoRegion);

        return regions;
    }

	public class MyBinder extends Binder {
		RECOBackgroundMonitoringService getService() {
			return RECOBackgroundMonitoringService.this;
		}
	}
	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}


    protected void start(ArrayList<RECOBeaconRegion> regions) {

        for(RECOBeaconRegion region : regions) {
            try {
                region.setRegionExpirationTimeMillis(3*1000L);
                this.recoManager.startMonitoringForRegion(region);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }


    protected void stop(ArrayList<RECOBeaconRegion> regions) {
        for(RECOBeaconRegion region : regions) {
            try {
                this.recoManager.stopMonitoringForRegion(region);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }



    public class HitServerIn extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            httpClient.postRequest_checkinout(macAddress, "in");
            Log.e("checkInRequest: ", macAddress);
            return null;
        }
    }

    public class HitServerOut extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            httpClient.postRequest_checkinout(macAddress, "out");
            Log.i("hithithit", "succeffully hit server delete.php");
            return null;
        }
    }

    public class HitServerAuth extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String...params) {
            String result;
            Log.e("macaddress ", params[0]);
            result = httpClient.postRequest_auth(params[0]);
            return result;
        }
        @Override
        protected void onPostExecute(String result){
            //Log.e("result ", result);
            try{
                JSONObject beaconInfo = new JSONObject(result);
                uuid = beaconInfo.getString("uuid");
                major = beaconInfo.getString("major");
                minor = beaconInfo.getString("minor");
            }catch (JSONException e) { e.printStackTrace(); }
        }
    }

    private Runnable mExitRunnable = new Runnable() {
        @Override
        public void run() {
            new HitServerOut().execute();
            Toast.makeText(getApplicationContext(), "Exit the region", Toast.LENGTH_LONG).show();
            enterFlag = true;
        }

    };
}
