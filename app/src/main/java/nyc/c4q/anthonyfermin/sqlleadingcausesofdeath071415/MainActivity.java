package nyc.c4q.anthonyfermin.sqlleadingcausesofdeath071415;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

import nyc.c4q.anthonyfermin.sqlleadingcausesofdeath071415.db.CauseOfDeath;
import nyc.c4q.anthonyfermin.sqlleadingcausesofdeath071415.db.MySQLiteOpenHelper;


public class MainActivity extends ActionBarActivity {

    ListView list;
    final String DB_FULL_PATH = "//data/data/nyc.c4q.anthonyfermin.sqlleadingcausesofdeath071415/databases/myDb";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = (ListView) findViewById(R.id.list);

        if(dataBaseExists()){
            list.setAdapter(new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_list_item_1,loadData()));
        }else{
            (new AsyncTask<Void, Void, List<CauseOfDeath>>() {
                @Override
                protected List<CauseOfDeath> doInBackground(Void... voids) {

                    fileParser();

                    return loadData();
                }

                @Override
                protected void onPostExecute(List<CauseOfDeath> causesOfDeath) {
                    super.onPostExecute(causesOfDeath);

                    list.setAdapter(new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_list_item_1,causesOfDeath));

                }
            }).execute();
        }



    }

    private boolean dataBaseExists() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(DB_FULL_PATH, null,
                    SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {
            // database doesn't exist yet.
        }
        return checkDB != null;
    }

    private void insertRow(String[] row){
        MySQLiteOpenHelper db = MySQLiteOpenHelper.getInstance(this);
        db.insertRow(row);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private List<CauseOfDeath> loadData(){
        MySQLiteOpenHelper db = MySQLiteOpenHelper.getInstance(this);
        return db.loadData();
    }

    private void fileParser(){

        InputStream fileIS;
        InputStreamReader isr = null;

        try {
            fileIS = getAssets().open("New_York_City_Leading_Causes_of_Death.csv");
            isr = new InputStreamReader(fileIS);
        } catch (IOException e) {
            e.printStackTrace();
        }

            Scanner dataSearch = new Scanner(isr);

            //skips first line
            if(dataSearch.hasNextLine())
                dataSearch.nextLine();

            while(dataSearch.hasNextLine()) {

                String[] columns = dataSearch.nextLine().split(",");
                if (columns.length > 6){
                    int offset = columns.length - 6;

                    String temp[] = columns;
                    columns = new String[6];
                    columns[0] = temp[0];
                    columns[1] = temp[1];
                    columns[2] = temp[2];
                    columns[3] = "";
                    for(int i = 0; i < offset; i ++){
                        columns[3] += temp[3+i];
                    }
                    columns[4] = temp[4+offset];
                    columns[5] = temp[5+offset];

                }
                insertRow(columns);

            }

    }

}
