package richthofen.app.bluechat.bluetoothchat;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.sql.SQLException;
import java.util.List;

import richthofen.app.bluechat.FragmentTabAdapter;
import richthofen.app.bluechat.bluechat.R;

public class PrivacySettings extends OrmLiteBaseActivity<DatabaseHelper> {
    ToggleButton toggle1;
    ToggleButton toggle2;
    ToggleButton toggle3;
    ToggleButton toggle4;

    TextView setting1;
    TextView setting2;
    TextView setting3;
    TextView setting4;

    ToggleStatus toggleStatus1;
    ToggleStatus toggleStatus2;
    ToggleStatus toggleStatus3;
    ToggleStatus toggleStatus4;

    String status1;
    String status2;
    String status3;
    String status4;

    ToggleStatus toggleStatus;


    private static RuntimeExceptionDao<ToggleStatus, Integer> mToggleStatusDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_privacy);

        mToggleStatusDao = getHelper().getToggleStautsDataDao();

        toggle1 = (ToggleButton) findViewById(R.id.toggle1);
        toggle2 = (ToggleButton) findViewById(R.id.toggle2);
        toggle3 = (ToggleButton) findViewById(R.id.toggle3);
        toggle4 = (ToggleButton) findViewById(R.id.toggle4);

        setting1 = (TextView) findViewById(R.id.setting1);
        setting2 = (TextView) findViewById(R.id.setting2);
        setting3 = (TextView) findViewById(R.id.setting3);
        setting4 = (TextView) findViewById(R.id.setting4);

        toggleStatus1 = searchToggleStatus("privacy1");
        if (toggleStatus1 == null){
            insertToggleStatus("privacy1", "ON");
            toggleStatus1 = searchToggleStatus("privacy1");
        }
        toggleStatus2 = searchToggleStatus("privacy2");
        if (toggleStatus2 == null) {
            insertToggleStatus("privacy2", "ON");
            toggleStatus2 = searchToggleStatus("privacy2");
        }
        toggleStatus3 = searchToggleStatus("privacy3");
        if (toggleStatus3 == null) {
            insertToggleStatus("privacy3", "ON");
            toggleStatus3 = searchToggleStatus("privacy3");
        }
        toggleStatus4 = searchToggleStatus("privacy4");
        if (toggleStatus4 == null){
            insertToggleStatus("privacy4", "ON");
            toggleStatus4 = searchToggleStatus("privacy4");
        }

        status1 = toggleStatus1.getStatus();
        status2 = toggleStatus2.getStatus();
        status3 = toggleStatus3.getStatus();
        status4 = toggleStatus4.getStatus();

        if (status1.equalsIgnoreCase("ON")){
            toggle1.setChecked(true);
        }
        if (status2.equalsIgnoreCase("ON")) {
            toggle2.setChecked(true);
        }
        if (status3.equalsIgnoreCase("ON")) {
            toggle3.setChecked(true);
        }
        if (status4.equalsIgnoreCase("ON")){
            toggle4.setChecked(true);
        }

        CompoundButton.OnCheckedChangeListener listener1 = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    toggleStatus1.setStatus("ON");
                    mToggleStatusDao.update(toggleStatus1);
                    //FragmentTabAdapter.reloadAFragment(MainActivity.fragment_inout);
                } else {
                    toggleStatus1.setStatus("OFF");
                    mToggleStatusDao.update(toggleStatus1);
                    //FragmentTabAdapter.reloadAFragment(MainActivity.fragment_inout);
                }
            }
        };
        CompoundButton.OnCheckedChangeListener listener2 = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    toggleStatus2.setStatus("ON");
                    mToggleStatusDao.update(toggleStatus2);
                } else {
                    toggleStatus2.setStatus("OFF");
                    mToggleStatusDao.update(toggleStatus2);
                }
            }
        };
        CompoundButton.OnCheckedChangeListener listener3 = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    toggleStatus3.setStatus("ON");
                    mToggleStatusDao.update(toggleStatus3);
                } else {
                    toggleStatus3.setStatus("OFF");
                    mToggleStatusDao.update(toggleStatus3);
                }
            }
        };
        CompoundButton.OnCheckedChangeListener listener4 = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                if (isChecked) {
                    toggleStatus4.setStatus("ON");
                    mToggleStatusDao.update(toggleStatus4);
                } else {
                    toggleStatus4.setStatus("OFF");
                    mToggleStatusDao.update(toggleStatus4);
                }
            }
        };

        toggle1.setOnCheckedChangeListener(listener1);
        toggle2.setOnCheckedChangeListener(listener2);
        toggle3.setOnCheckedChangeListener(listener3);
        toggle4.setOnCheckedChangeListener(listener4);
    }

    private void insertToggleStatus(String name, String status){
        toggleStatus = new ToggleStatus();
        toggleStatus.setName(name);
        toggleStatus.setStatus(status);
        mToggleStatusDao.createIfNotExists(toggleStatus);
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
