package br.net.bmobile.ow.support;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.inject.Inject;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "ow.db";
    private static final String DATABASE_PATH = "/data/data/br.net.bmobile.ow/databases/";
    private static final int DATABASE_VERSION = 1;

    @Inject
    public DatabaseHelper(Context context) {
        super(context, DATABASE_PATH+DATABASE_NAME, null, DATABASE_VERSION);

        boolean dbexist = checkDatabase();
        if (!dbexist) {

            // If database did not exist, try copying existing database from assets folder.
            try {
                InputStream myinput = context.getAssets().open(DATABASE_NAME);

                File f = new File(DATABASE_PATH);
                if (! f.exists())
                	f.mkdir();
                
                String outfilename = DATABASE_PATH + DATABASE_NAME;
                Log.i(DatabaseHelper.class.getName(), "DB Path: " + outfilename);
                OutputStream myoutput = new FileOutputStream(outfilename);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myinput.read(buffer)) > 0) {
                    myoutput.write(buffer, 0, length);
                }
                myoutput.flush();
                myoutput.close();
                myinput.close();            
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    * Check whether or not database exist
    */
    private boolean checkDatabase() {
        boolean checkdb = false;

        String myPath = DATABASE_PATH + DATABASE_NAME;
        File dbfile = new File(myPath);
        checkdb = dbfile.exists();

        Log.i(DatabaseHelper.class.getName(), "DB Exist: " + checkdb);

        return checkdb;
    }

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		
	}
}