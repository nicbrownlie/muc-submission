package uk.ac.gcu.nbrown201.taylorswift;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateUtils;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

// this class is a helper class for opening the SQLite database and reading data from it.
// because this application doesnt need to add any data to the database there is no methods
// to do this. Just read all the database items from the database and return them in an array.
public class TourDateInfoDBMgr extends SQLiteOpenHelper {

    // database version number
    private static final int DB_VER = 6;
    // path to the database on the device.
    private static String DB_PATH = "";
    // the name of the database.
    private static final String DB_NAME = "tourdates.sqlite";
    // the table name to be read from
    private static final String TBL_NAME = "tourdate";

    // below is all the coloumn names in the database.
    public static final String COL_TOURDATEID = "TourDateID";
    public static final String COL_VENUENAME = "VenueName";
    public static final String COL_LONGITUDE = "Longitude";
    public static final String COL_LATITUDE = "Latitude";
    public static final String COL_DATE = "Date";

    // the context of the application
    private final Context appContext;

    // constructor.
    public TourDateInfoDBMgr(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.appContext = context;
        //http://stackoverflow.com/questions/9109438/how-to-use-an-existing-database-with-an-android-application
        // this sets the static method so that it points correct folder for data.
        TourDateInfoDBMgr.DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
    }

    // called if the database doesn't exist yet and it basically just
    // creates a database using an SQL script but using the
    // names of the predefined names so they are always the same.
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_TOURDATES_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TBL_NAME + "("
                + COL_TOURDATEID +" INTEGER PRIMARY KEY, "
                + COL_VENUENAME+" TEXT, "
                + COL_LONGITUDE+" TEXT, "
                +COL_LATITUDE+" TEXT, "
                +COL_DATE+" DATE)";

        sqLiteDatabase.execSQL(CREATE_TOURDATES_TABLE);
    }

    // when the version number is changed it will drop the database
    // and recreate it.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        if(newVersion > oldVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TBL_NAME);
            onCreate(sqLiteDatabase);
        }
    }

    // checks to see if the database exists first and then
    // if it doesnt it will copy the databse from the assets folder.
    public void dbCreate() throws IOException {
        boolean dbExits = dbCheck();

        if(!dbExits) {
            this.getReadableDatabase();
            try {
                copyDBFromAssets();
            }catch(IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    // tried to find the database file on the device
    // it it cant find it it will just return false.
    // this is when the database is read from or created.
    public boolean dbCheck() {
        SQLiteDatabase db = null;

        try {
            String dbPath = DB_PATH + DB_NAME;
            db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
            db.setLocale(Locale.getDefault());
            db.setVersion(DB_VER);
        }catch(SQLiteException e) {
            db = null;
            Log.e("SQL", "Database not found");
        }

        if(db != null) {
            db.close();
        }

        return db != null;
    }

    // will copy the current database from the assets to the device.
    public void copyDBFromAssets() throws IOException {
        InputStream dbInput = null;
        OutputStream dbOutput = null;

        String dbFileName = DB_PATH + DB_NAME;

        try {
            dbInput = appContext.getAssets().open(DB_NAME);
            dbOutput = new FileOutputStream(dbFileName);
            byte[] buffer = new byte[1024];
            int length;
            while((length = dbInput.read(buffer)) > 0) {
                dbOutput.write(buffer, 0, length);
            }

            dbOutput.flush();
            dbOutput.close();
            dbInput.close();
        } catch(IOException e) {
            throw new Error("Problems copying DB!");
        }
    }

    // this will select all the data from the table
    // and return it as an array of tour date information objects.
    public ArrayList<TourDateInfo> getAllTourDates() {

        String query = "SELECT * FROM " + TBL_NAME + "";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<TourDateInfo> dates = new ArrayList<TourDateInfo>();

        if(cursor.moveToFirst()) {

            String t = cursor.getString(1);

            while (!cursor.isAfterLast()) {
                TourDateInfo item = new TourDateInfo();
                item.setId(Integer.parseInt(cursor.getString(0)));
                item.setVenueName(cursor.getString(1));
                item.setLongitude(Float.parseFloat(cursor.getString(2)));
                item.setLatitude(Float.parseFloat(cursor.getString(3)));
                item.setDate(cursor.getString(4));
                dates.add(item);

                cursor.moveToNext();
            }
        } else {
            dates = null;
        }
        return dates;
    }
}
