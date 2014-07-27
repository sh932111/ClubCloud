package getdb;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PushDB
{
	private Context mContext = null;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;

	private static final String DATABASE_NAME = "pushdb.db";
	private static final int DATABASE_VERSION = 4;
	private static final String DATABASE_TABLE = "pushlist";

	public static final String KEY_ROWID = "_id";
	public static final String KEY_ITEM = "detail";
	public static final String KEY_TITLE = "title";
	public static final String KEY_CREATED = "time";
	public static final String KEY_TimeDetail = "time_detail";
	public static final String KEY_CHECK = "look";

	/** Constructor */
	@SuppressLint("SdCardPath")
	public PushDB(Context context)
	{
		this.mContext = context;

	}

	public PushDB open() throws SQLException
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
				+ KEY_CREATED + " TEXT," + KEY_TimeDetail + " TEXT,"
				+ KEY_CHECK + " TEXT );";

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

	public Cursor getAllDate()
	{
		return db.query(DATABASE_TABLE, // Which table to Select
				// strCols,// Which columns to return
				new String[]
				{ KEY_CREATED }, null, // WHERE clause
				null, // WHERE arguments
				null, // GROUP BY clause
				null, // HAVING clause
				KEY_CREATED + " DESC" // Order-by clause
		);
	}

	public Cursor getAll()
	{
		return db.query(DATABASE_TABLE, // Which table to Select
				// strCols,// Which columns to return
				new String[]
				{ KEY_ROWID, KEY_TITLE, KEY_ITEM, KEY_CREATED, KEY_TimeDetail,
						KEY_CHECK }, null, // WHERE clause
				null, // WHERE arguments
				null, // GROUP BY clause
				null, // HAVING clause
				KEY_CREATED + " DESC" // Order-by clause
		);
	}

	// query single entry
	public Cursor get(String rowId) throws SQLException
	{
		System.out.println(rowId);

		Cursor mCursor = db.query(DATABASE_TABLE, new String[]
		{ KEY_ROWID, KEY_TITLE, KEY_ITEM, KEY_CREATED, KEY_TimeDetail,
				KEY_CHECK }, KEY_CREATED + " = ?", new String[]
		{ "" + rowId + "" }, null, null, null, null);

		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	// add an entry
	public long create(Long ID, String title, String detail, String time,
			String value, String check)
	{
		// SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm",
		// Locale.ENGLISH);
		// Date now = new Date();
		ContentValues args = new ContentValues();

		args.put(KEY_ROWID, ID);
		args.put(KEY_TITLE, title);
		args.put(KEY_ITEM, detail);
		args.put(KEY_CHECK, check);

		args.put(KEY_CREATED, time);
		// args.put(KEY_CREATED, df.format(now.getTime()));
		args.put(KEY_TimeDetail, value);

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

	// update

	public boolean updateLook(Long Id, String i)
	{
		ContentValues args = new ContentValues();

		args.put(KEY_CHECK, i);

		db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + Id, null);

		return true;

	}
}
