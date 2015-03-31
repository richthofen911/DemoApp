package richthofen.app.bluechat.bluetoothchat;

/**
 * Created by richthofen80 on 3/9/15.
 */
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

public class LisstFragment extends Fragment {

    ToggleStatus toggleStatus3;
    DatabaseHelper databaseHelper;

    private static RuntimeExceptionDao<ToggleStatus, Integer> mToggleStatusDao;

    @Override
    public void onAttach(Activity activity) {super.onAttach(activity); }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView;
        databaseHelper = new DatabaseHelper(getActivity().getApplicationContext());
        mToggleStatusDao = databaseHelper.getToggleStautsDataDao();
        toggleStatus3 = searchToggleStatus("privacy3");
        if (toggleStatus3 == null){
            rootView = inflater.inflate(R.layout.fragment_list, container, false);
            TextView tv_movies = (TextView) rootView.findViewById(R.id.list_movies);
            TextView tv_food = (TextView) rootView.findViewById(R.id.list_food);
            TextView tv_songs = (TextView) rootView.findViewById(R.id.list_songs);

            tv_movies.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View arg0){
                    Intent listIntent = new Intent(getActivity(), ShowListDetail.class);
                    listIntent.putExtra("jumpTo", "movies");
                    startActivity(listIntent);
                }
            });
            tv_food.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View arg0){
                    Intent listIntent = new Intent(getActivity(), ShowListDetail.class);
                    listIntent.putExtra("jumpTo", "food");
                    startActivity(listIntent);
                }
            });
            tv_songs.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View arg0){
                    Intent listIntent = new Intent(getActivity(), ShowListDetail.class);
                    listIntent.putExtra("jumpTo", "songs");
                    startActivity(listIntent);
                }
            });

            return rootView;
        }
        else
          if(toggleStatus3.getStatus().equals("ON")){
              rootView = inflater.inflate(R.layout.fragment_list, container, false);
              TextView tv_movies = (TextView) rootView.findViewById(R.id.list_movies);
              TextView tv_food = (TextView) rootView.findViewById(R.id.list_food);
              TextView tv_songs = (TextView) rootView.findViewById(R.id.list_songs);

              tv_movies.setOnClickListener(new View.OnClickListener(){
                  @Override
                  public void onClick(View arg0){
                      Intent listIntent = new Intent(getActivity(), ShowListDetail.class);
                      listIntent.putExtra("jumpTo", "movies");
                      startActivity(listIntent);
                  }
              });
              tv_food.setOnClickListener(new View.OnClickListener(){
                  @Override
                  public void onClick(View arg0){
                      Intent listIntent = new Intent(getActivity(), ShowListDetail.class);
                      listIntent.putExtra("jumpTo", "food");
                      startActivity(listIntent);
                  }
              });
              tv_songs.setOnClickListener(new View.OnClickListener(){
                  @Override
                  public void onClick(View arg0){
                      Intent listIntent = new Intent(getActivity(), ShowListDetail.class);
                      listIntent.putExtra("jumpTo", "songs");
                      startActivity(listIntent);
                  }
              });

              return rootView;
          }
          else{
              rootView = inflater.inflate(R.layout.fragment_list_off, container, false);
              TextView textView = (TextView) rootView.findViewById(R.id.list_off);
              textView.setText("List setting in Privacy is off,\nturn it on to see the content");
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
