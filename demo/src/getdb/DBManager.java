package getdb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.candroidsample.R;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class DBManager
{
	private final int BUFFER_SIZE = 400000;

	public static final String DB_NAME = "city.db"; // 保存的数据库文件名

	public static final String PACKAGE_NAME = "com.candroidsample";

	private SQLiteDatabase database;

	private Context context;

	public DBManager(Context context)
	{
		this.context = context;
	}

	public void openDatabase()
	{
		this.database = this.openDatabase(context.getExternalFilesDir(null).getAbsolutePath() + "/" +  DB_NAME);
	}

	private SQLiteDatabase openDatabase(String dbfile)
	{
		try
		{
			if (!(new File(dbfile).exists()))
			{
				InputStream is = this.context.getResources().openRawResource(
						R.raw.city); // 欲导入的数据库
				FileOutputStream fos = new FileOutputStream(dbfile);

				byte[] buffer = new byte[BUFFER_SIZE];
				int count = 0;
				while ((count = is.read(buffer)) > 0)
				{
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			}
			SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile,
					null);
			return db;
		}
		catch (FileNotFoundException e)
		{
			System.out.println("File not found");
			e.printStackTrace();
		}
		catch (IOException e)
		{
			System.out.println("IO exception");
			e.printStackTrace();
		}
		return null;
	}

	public void closeDatabase()
	{
		this.database.close();
	}
	
	public Cursor getCityAll() 
	{
	    return database.query("city_table", //Which table to Select
//	         strCols,// Which columns to return
	    	 new String[] {"territory_name","city_id"},
	         null, // WHERE clause
	         null, // WHERE arguments
	         null, // GROUP BY clause
	         null, // HAVING clause
	         null //Order-by clause
	         );
	}
	// query single entry
		public Cursor get(String rowId) throws SQLException
		{
			Cursor mCursor = database.query("city_detail_table", new String[]
			{ "territory_name","district_name" ,"district_id"}, "territory_name" + " = ?", new String[]
			{ "" + rowId + "" }, null, null, null, null);

			if (mCursor != null)
			{
				mCursor.moveToFirst();
			}
			return mCursor;
		}
	
	
}
