package nyc.c4q.anthonyfermin.sqlleadingcausesofdeath071415.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by c4q-anthonyf on 7/14/15.
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    public static final String MYDB = "myDb";
    public static final int VERSION = 1;

    public static MySQLiteOpenHelper INSTANCE;

    public static synchronized MySQLiteOpenHelper getInstance(Context context)
    {
        if(INSTANCE == null)
        {
            INSTANCE = new MySQLiteOpenHelper(context.getApplicationContext());
        }

        return INSTANCE;
    }

    public MySQLiteOpenHelper(Context context) {
        super(context, MYDB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    public void insertRow(String[] row){

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CauseOfDeathDB.COLUMN_NAME_YEAR, Integer.parseInt(row[0]));
        values.put(CauseOfDeathDB.COLUMN_NAME_ETHNICITY, row[1] );
        values.put(CauseOfDeathDB.COLUMN_NAME_SEX, row[2]);
        values.put(CauseOfDeathDB.COLUMN_NAME_CAUSE, row[3]);
        values.put(CauseOfDeathDB.COLUMN_NAME_COUNT, Integer.parseInt(row[4]));
        values.put(CauseOfDeathDB.COLUMN_NAME_PERCENT, Integer.parseInt(row[5]));

        db.insertOrThrow(
                CauseOfDeathDB.TABLE_NAME,
                null,
                values);

    }

    public static abstract class CauseOfDeathDB implements BaseColumns {


        public static final String TABLE_NAME = "causes_of_death";
        public static final String COLUMN_NAME_YEAR = "year";
        public static final String COLUMN_NAME_ETHNICITY = "ethnicity";
        public static final String COLUMN_NAME_SEX = "sex";
        public static final String COLUMN_NAME_CAUSE = "cause_of_death";
        public static final String COLUMN_NAME_COUNT = "count";
        public static final String COLUMN_NAME_PERCENT = "percent";

    }

    public List<CauseOfDeath> loadData()
    {
        String[] projection = {
                CauseOfDeathDB._ID,
                CauseOfDeathDB.COLUMN_NAME_YEAR,
                CauseOfDeathDB.COLUMN_NAME_ETHNICITY,
                CauseOfDeathDB.COLUMN_NAME_SEX,
                CauseOfDeathDB.COLUMN_NAME_CAUSE,
                CauseOfDeathDB.COLUMN_NAME_COUNT,
                CauseOfDeathDB.COLUMN_NAME_PERCENT,
        };

        SQLiteDatabase db = getWritableDatabase();

        List<CauseOfDeath> causes = new ArrayList<>();

        Cursor cursor = db.query(
                CauseOfDeathDB.TABLE_NAME,
                projection,
                "Ethnicity = ?", //where
                new String[] {"NON-HISPANIC BLACK"}, //where args
                null, //group by (ignore)
                null, //having (ignore)
                CauseOfDeathDB._ID + " asc");//order by
        while(cursor.moveToNext())
        {
            causes.add(new CauseOfDeath(cursor.getInt(cursor.getColumnIndex(CauseOfDeathDB._ID)),
                    cursor.getInt(
                            cursor.getColumnIndex(CauseOfDeathDB.COLUMN_NAME_YEAR)),
                    cursor.getString(
                            cursor.getColumnIndex(CauseOfDeathDB.COLUMN_NAME_ETHNICITY)),
                    cursor.getString(cursor.getColumnIndex(
                            CauseOfDeathDB.COLUMN_NAME_SEX)),
                    cursor.getString(cursor.getColumnIndex(
                            CauseOfDeathDB.COLUMN_NAME_CAUSE)),
                    cursor.getInt(cursor.getColumnIndex(
                            CauseOfDeathDB.COLUMN_NAME_COUNT)),
                    cursor.getInt(cursor.getColumnIndex(
                            CauseOfDeathDB.COLUMN_NAME_PERCENT))));
        }

        cursor.close();

        return causes;
    }

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + CauseOfDeathDB.TABLE_NAME + " (" +
            CauseOfDeathDB._ID + " INTEGER PRIMARY KEY," +
            CauseOfDeathDB.COLUMN_NAME_YEAR + " INTEGER," +
            CauseOfDeathDB.COLUMN_NAME_ETHNICITY + " TEXT," +
            CauseOfDeathDB.COLUMN_NAME_SEX + " TEXT," +
            CauseOfDeathDB.COLUMN_NAME_CAUSE + " TEXT," +
            CauseOfDeathDB.COLUMN_NAME_COUNT + " INTEGER," +
            CauseOfDeathDB.COLUMN_NAME_PERCENT + " INTEGER" +
            " )";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + CauseOfDeathDB.TABLE_NAME;


}
