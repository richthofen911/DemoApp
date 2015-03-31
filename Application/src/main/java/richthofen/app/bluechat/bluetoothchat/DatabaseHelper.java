package richthofen.app.bluechat.bluetoothchat;

/**
 * Created by richthofen80 on 2/26/15.
 */
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import java.sql.SQLException;
/**
 * Created by richthofen80 on 2/23/15.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper{
    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "ToggleStatus.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<ToggleStatus, Integer> toggleStatusDao = null;
    private RuntimeExceptionDao<ToggleStatus, Integer> toggleStatusesRuntimeDao = null;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DatabaseHelper(Context context, String databaseName, CursorFactory factory, int databaseVersion)
    {
        super(context, databaseName, factory, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource){
        try{
            TableUtils.createTable(connectionSource, ToggleStatus.class);
            toggleStatusDao = getToggleStatusDao();
            toggleStatusesRuntimeDao = getToggleStautsDataDao();
        }
        catch(SQLException e){
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion)
    {
        try
        {
            TableUtils.dropTable(connectionSource, ToggleStatus.class, true);
        }
        catch (SQLException e)
        {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
    }

    private Dao<ToggleStatus, Integer> getToggleStatusDao() throws SQLException{
        if (toggleStatusDao == null)
            toggleStatusDao = getDao(ToggleStatus.class);
        return toggleStatusDao;
    }

    public RuntimeExceptionDao<ToggleStatus, Integer> getToggleStautsDataDao(){
        if (toggleStatusesRuntimeDao == null)
            toggleStatusesRuntimeDao = getRuntimeExceptionDao(ToggleStatus.class);
        return toggleStatusesRuntimeDao;
    }

    @Override
    public void close(){
        super.close();
        toggleStatusesRuntimeDao = null;
    }
}