package getdb;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

public class EventDB
{
	private Context mContext = null;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;

	private static final String DATABASE_NAME = "eventdb.db";
	private static final int DATABASE_VERSION = 4;
	private static final String DATABASE_TABLE = "eventlist";

	public static final String KEY_ROWID = "_id";
	public static final String KEY_ITEM = "detail";
	public static final String KEY_TITLE = "title";
	public static final String KEY_CREATED = "time";
	public static final String KEY_TimeDetail = "time_detail";
	public static final String KEY_IMAGE = "image";
	public static final String KEY_TYPE = "type";

	/** Constructor */
	@SuppressLint("SdCardPath")
	public EventDB(Context context)
	{
		this.mContext = context;
	}

	public EventDB open() throws SQLException
	{
		dbHelper = new DatabaseHelper(mContext);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close()
	{
		dbHelper.close();
	}

	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		private static final String DATABASE_CREATE = "CREATE TABLE "
				+ DATABASE_TABLE + "(" + KEY_ROWID + " INTEGER PRIMARY KEY,"
				+ KEY_TITLE + " TEXT NOT NULL," + KEY_ITEM + " TEXT,"
				+ KEY_CREATED + " TEXT," 
				+ KEY_TimeDetail + " TEXT,"
				+ KEY_TYPE + " TEXT,"
				+ KEY_IMAGE + " TEXT );";

		public DatabaseHelper(Context context)
		{
			super(context, context.getExternalFilesDir(null).getAbsolutePath()
					+ "/" + DATABASE_NAME, null, DATABASE_VERSION);

			System.out.println(context.getExternalFilesDir(null)
					.getAbsolutePath() + "/" + DATABASE_NAME);
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
			// TODO Auto-generated method stub
			
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}

	}

	public ArrayList<Bundle> getAll()
	{
		Cursor mCursor = db.query(DATABASE_TABLE, // Which table to Select
				// strCols,// Which columns to return
				new String[]
				{ KEY_ROWID, KEY_TITLE, KEY_ITEM, KEY_CREATED, KEY_TimeDetail,
					KEY_IMAGE,KEY_TYPE}, null, // WHERE clause
				null, // WHERE arguments
				null, // GROUP BY clause
				null, // HAVING clause
				KEY_CREATED + " DESC" // Order-by clause
		);
		
		if ( mCursor.getCount() != 0)
		{
			mCursor.moveToFirst();
			
			ArrayList<Bundle> array_list = new ArrayList<Bundle>();
			
			for (int i = 0; i < mCursor.getCount(); i++)
			{
				Long SelectID = mCursor.getLong(0);
				String title = mCursor.getString(1);
				String message = mCursor.getString(2);
				String dateString = mCursor.getString(3);
				String timeString = mCursor.getString(4);
				String image = mCursor.getString(5);
				String type = mCursor.getString(6);
				
				Bundle bundle = new Bundle();

				bundle.putLong("ID", SelectID);
				bundle.putString("Title", title);
				bundle.putString("Message", message);
				bundle.putString("Date", dateString);
				bundle.putString("Time", timeString);
				bundle.putString("Image", image);
				bundle.putString("Type", type);
				
				array_list.add(bundle);
				
				mCursor.moveToNext();
			}
			
			mCursor.close();
			
			return array_list;
		}
		
		return null;
	}

	public ArrayList<Bundle> getType(String tStringype) throws SQLException
	{
		Cursor mCursor = db.query(DATABASE_TABLE, new String[]
		{ KEY_ROWID, KEY_TITLE, KEY_ITEM, KEY_CREATED, KEY_TimeDetail,
				KEY_IMAGE ,KEY_TYPE}, KEY_TYPE + " = ?", new String[]
		{ "" + tStringype + "" }, null, null, null, null);

		if ( mCursor.getCount() != 0)
		{
			mCursor.moveToFirst();
			
			ArrayList<Bundle> array_list = new ArrayList<Bundle>();
			
			for (int i = 0; i < mCursor.getCount(); i++)
			{
				Long SelectID = mCursor.getLong(0);
				String title = mCursor.getString(1);
				String message = mCursor.getString(2);
				String dateString = mCursor.getString(3);
				String timeString = mCursor.getString(4);
				String image = mCursor.getString(5);
				String type = mCursor.getString(6);
				
				Bundle bundle = new Bundle();

				bundle.putLong("ID", SelectID);
				bundle.putString("Title", title);
				bundle.putString("Message", message);
				bundle.putString("Date", dateString);
				bundle.putString("Time", timeString);
				bundle.putString("Image", image);
				bundle.putString("Type", type);
				
				array_list.add(bundle);
				
				mCursor.moveToNext();
			}
			
			mCursor.close();
			
			return array_list;
		}
		
		return null;
	}
	// query single entry
	public Bundle get(Long rowId) throws SQLException
	{
		Cursor mCursor = db.query(DATABASE_TABLE, new String[]
		{ KEY_ROWID, KEY_TITLE, KEY_ITEM, KEY_CREATED, KEY_TimeDetail,
				KEY_IMAGE ,KEY_TYPE}, KEY_ROWID + " = ?", new String[]
		{ "" + rowId + "" }, null, null, null, null);

		if ( mCursor.getCount() != 0)
		{
			mCursor.moveToFirst();
			
			Long SelectID = mCursor.getLong(0);
			String title = mCursor.getString(1);
			String message = mCursor.getString(2);
			String dateString = mCursor.getString(3);
			String timeString = mCursor.getString(4);
			String image = mCursor.getString(5);
			String type = mCursor.getString(6);
			
			Bundle bundle = new Bundle();

			bundle.putLong("ID", SelectID);
			bundle.putString("Title", title);
			bundle.putString("Message", message);
			bundle.putString("Date", dateString);
			bundle.putString("Time", timeString);
			bundle.putString("Image", image);
			bundle.putString("Type", type);
			
			mCursor.close();
			
			return bundle;
		}
		
		return null;
	}

	// add an entry
	public long create(Long ID, String title, String detail, String time,
			String value,String image,String type)
	{
		// SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm",
		// Locale.ENGLISH);
		// Date now = new Date();
		ContentValues args = new ContentValues();

		args.put(KEY_ROWID, ID);
		args.put(KEY_TITLE, title);
		args.put(KEY_ITEM, detail);
		
		args.put(KEY_CREATED, time);
		// args.put(KEY_CREATED, df.format(now.getTime()));
		args.put(KEY_TimeDetail, value);
		args.put(KEY_IMAGE, image);
		args.put(KEY_TYPE, type);

		return db.insert(DATABASE_TABLE, null, args);
	}

	// remove an entry
	public boolean delete(Long rowId)
	{
		return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public boolean updateAll(Long id, String title, String list, String time,
			String value)
	{
		ContentValues args = new ContentValues();

		args.put(KEY_ROWID, id);
		args.put(KEY_TITLE, title);
		args.put(KEY_ITEM, list);
		args.put(KEY_CREATED, time);
		args.put(KEY_TimeDetail, value);

		db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + id, null);

		return true;
	}

	
	public boolean updateImage(Long Id, String i)
	{
		ContentValues args = new ContentValues();

		args.put(KEY_IMAGE, i);

		db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + Id, null);

		return true;

	}
}
