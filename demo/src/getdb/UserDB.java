package getdb;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDB
{
	private Context mContext = null;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;

	private static final String DATABASE_NAME = "userdb.db";
	private static final int DATABASE_VERSION = 4;
	private static final String DATABASE_TABLE = "userlist";

	public static final String KEY_USERNAME = "username";
	public static final String KEY_NAME = "name";
	public static final String KEY_PASSWORD = "password";
	public static final String KEY_USERID = "user_id";
	public static final String KEY_DEVICETOKEN = "device_token";
	public static final String KEY_OS = "device_os";

	public static final String KEY_USER_CITY = "user_city";
	public static final String KEY_USER_CITY_DETAIL = "user_city_detail";
	public static final String KEY_CITY_ID = "city_id";
	public static final String KEY_CITY_DETAIL_ID = "city_detail_id";
	public static final String KEY_CELLPHONE = "cellphone";

	/** Constructor */
	@SuppressLint("SdCardPath")
	public UserDB(Context context)
	{
		this.mContext = context;

	}

	public UserDB open() throws SQLException
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
				+ DATABASE_TABLE + "(" + KEY_USERNAME
				+ " TEXT NOT NULL PRIMARY KEY," + KEY_PASSWORD
				+ " TEXT NOT NULL," + KEY_NAME + " TEXT," + KEY_USERID
				+ " TEXT," + KEY_DEVICETOKEN + " TEXT," + KEY_OS + " TEXT,"
				+ KEY_USER_CITY + " TEXT," + KEY_USER_CITY_DETAIL + " TEXT,"
				+ KEY_CITY_ID + " TEXT," 
				+ KEY_CITY_DETAIL_ID + " TEXT," 
				+ KEY_CELLPHONE + " TEXT" + ");";

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

	public ArrayList<String> getAllDate()
	{
		ArrayList<String> array_list = new ArrayList<String>();

		Cursor mCursor = db
				.query(DATABASE_TABLE, // Which table to Select
						// strCols,// Which columns to return
						new String[] { KEY_USERNAME, KEY_PASSWORD, KEY_NAME,
								KEY_OS, KEY_USERID, KEY_DEVICETOKEN,
								KEY_USER_CITY, KEY_USER_CITY_DETAIL,
								KEY_CITY_ID, KEY_CITY_DETAIL_ID ,KEY_CELLPHONE}, null, // WHERE
																			// clause
						null, // WHERE arguments
						null, // GROUP BY clause
						null, // HAVING clause
						KEY_USERID + " DESC" // Order-by clause
				);

		if (mCursor.getCount() != 0)
		{
			mCursor.moveToFirst();

			for (int i = 0; i < mCursor.getColumnCount(); i++)
			{
				String colum = mCursor.getString(i);

				array_list.add(colum);
			}

			return array_list;
		}

		return array_list;
	}

	public long create(String username, String password, String name,
			String user_id, String device_token, String device_os,
			String user_city, String user_city_detail, String city_id,
			String user_city_detail_id,String cellphone)
	{
		ContentValues args = new ContentValues();

		args.put(KEY_USERNAME, username);
		args.put(KEY_PASSWORD, password);
		args.put(KEY_NAME, name);
		args.put(KEY_OS, device_os);

		args.put(KEY_USERID, user_id);
		// args.put(KEY_USERID, df.format(now.getTime()));
		args.put(KEY_DEVICETOKEN, device_token);

		args.put(KEY_USER_CITY, user_city);
		args.put(KEY_USER_CITY_DETAIL, user_city_detail);
		args.put(KEY_CITY_ID, city_id);
		args.put(KEY_CITY_DETAIL_ID, user_city_detail_id);
		args.put(KEY_CELLPHONE, cellphone);

		return db.insert(DATABASE_TABLE, null, args);
	}

	public int isTableExists()
	{
		Cursor c = null;

		try
		{
			c = db.query(DATABASE_TABLE, null, null, null, null, null, null);
		}
		catch (Exception e)
		{
			/* fail */
		}

		return c.getCount();
	}

	// remove an entry
	// public boolean delete(Long rowId)
	// {
	// return db.delete(DATABASE_TABLE, KEY_USERNAME + "=" + rowId, null) > 0;
	// }
	//
	// public boolean updateAll(Long id ,String title ,String list,String
	// time,String value)
	// {
	// ContentValues args = new ContentValues();
	//
	// args.put(KEY_USERNAME, id);
	// args.put(KEY_PASSWORD, title);
	// args.put(KEY_NAME, list);
	// args.put(KEY_USERID, time);
	// args.put(KEY_DEVICETOKEN, value);
	//
	// db.update(DATABASE_TABLE, args, KEY_USERNAME + "=" + id, null) ;
	//
	// return true;
	// }
	// //update
	//
	// public boolean updateLook(Long Id ,String i)
	// {
	// ContentValues args = new ContentValues();
	//
	// args.put(KEY_OS, i);
	//
	// db.update(DATABASE_TABLE, args, KEY_USERNAME + "=" + Id, null) ;
	//
	// return true;
	//
	// }
}
