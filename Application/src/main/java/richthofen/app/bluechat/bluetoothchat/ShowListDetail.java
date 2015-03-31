package richthofen.app.bluechat.bluetoothchat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.util.EncodingUtils;

import richthofen.app.bluechat.FragmentTabAdapter;
import richthofen.app.bluechat.bluechat.R;

public class ShowListDetail extends Activity {
    TextView tv_addDetail;
    WebView wv_showDetial;
    Intent intent;
    EditText input;
    HitServerAddDetail hitServerAddDetail;

    private static final String URL = "http://54.149.146.72/list.php";

    String postData_prefix = "activity=listhtml&listname=";
    String type_g = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        final String type = intent.getStringExtra("jumpTo");
        type_g = type;
        String postData = postData_prefix + type;
        setContentView(R.layout.activity_show_list_detail);
        tv_addDetail = (TextView) findViewById(R.id.tv_addDetail);
        wv_showDetial = (WebView) findViewById(R.id.wv_listDetail);

        WebSettings setting = wv_showDetial.getSettings();
        setting.setJavaScriptEnabled(true);
        wv_showDetial.postUrl(URL, EncodingUtils.getBytes(postData, "BASE64"));

        tv_addDetail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0){
                new AlertDialog.Builder(ShowListDetail.this).setTitle("Input A Movie Name").setIcon(android.R.drawable.ic_dialog_info).setView(input = new EditText(ShowListDetail.this))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            hitServerAddDetail = new HitServerAddDetail();
                            Log.e("args ", "add "+type+input.getText().toString());
                            hitServerAddDetail.execute("add", type, input.getText().toString());
                            }
                        })
                        .setNegativeButton("Cancel", null).show();
            }
        });
    }

    public class HitServerAddDetail extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = "";
            HTTPClient httpClient = new HTTPClient();
            result = httpClient.postRequest_addListDetail(params[0], params[1], params[2]);
            Log.e("add movie", params[0]+params[1]+params[2]);
            return result;
        }
        @Override
        protected void onPostExecute(String result){
            Log.e("add result ", result);
            wv_showDetial.postUrl(URL, EncodingUtils.getBytes(postData_prefix + type_g , "BASE64"));
            //Toast.makeText(ShowListDetail.this, "see the added one", Toast.LENGTH_LONG).show();
        }
    }
}
