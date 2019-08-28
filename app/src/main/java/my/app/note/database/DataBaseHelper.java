package my.app.note.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by CCP on 2017.11.6.
 *
 */

public class DataBaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = "DataBaseHelper";
    private static final String DB_NAME = "my_app_note.db";
    private static final int DB_VERSION = 3; // 每次升级数据库版本号+1

    private DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private static volatile DataBaseHelper instance;

    static DataBaseHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (DataBaseHelper.class) {
                if (instance == null) {
                    instance = new DataBaseHelper(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            Log.i(TAG, "Database onCreate");
            TableUtils.createTableIfNotExists(connectionSource, NoteBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        Log.i(TAG, "Database onUpgrade");
        try {
            Log.i(TAG, "Begin to upgrade from Version:" + oldVersion + " to Version:" + newVersion);
            if (oldVersion == 1) {
                upgradeToVersion2();
                oldVersion++;
            }
            if (oldVersion == 2) {
                upgradeToVersion3();
                oldVersion++;
            }
            if (oldVersion != newVersion) {
                throw new Exception("oldVersion != newVersion");
            }
            Log.i(TAG, "Upgrade finished to Version:" + newVersion);
        } catch (Exception e) {
            e.printStackTrace();
            dropAndCreateTables(sqLiteDatabase, connectionSource);
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "Database onDowngrade");
        try {
            Log.i(TAG, "Begin to downgrade from Version:" + oldVersion + " to Version:" + newVersion);
            dropAndCreateTables(db, connectionSource);
            Log.i(TAG, "Downgrade finished to Version:" + newVersion);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dropAndCreateTables(SQLiteDatabase database, ConnectionSource connectionSource) {
        dropAllTables(connectionSource);
        onCreate(database, connectionSource);
    }

    private void dropAllTables(ConnectionSource connectionSource) {
        try {
            TableUtils.dropTable(connectionSource, NoteBean.class, true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* ----以下数据库升级记录----*/

    // Ver2:在表"table_note"中插入一列"NOTE_REMIND_TIME"用于记录提醒时间
    private void upgradeToVersion2() throws SQLException {
        getDao(NoteBean.class).executeRaw("ALTER TABLE `table_note` ADD COLUMN NOTE_REMIND_TIME VARCHAR ;");
    }

    // Ver3:在表"table_note"中插入一列"NOTE_TAGS"用于记录笔记标签
    private void upgradeToVersion3() throws SQLException {
        getDao(NoteBean.class).executeRaw("ALTER TABLE `table_note` ADD COLUMN NOTE_TAGS VARCHAR ;");
    }
}
