package getdb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.candroidsample.R;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class CityDB
{
	private final int BUFFER_SIZE = 400000;

	public static final String DB_NAME = "city.db"; // 保存的数据库文件名

	public static final String PACKAGE_NAME = "com.candroidsample";

	private SQLiteDatabase database;

	private Context context;

	public CityDB(Context context)
	{
		this.context = context;
	}

	public void openDatabase()
	{
		this.database = this.openDatabase(context.getExternalFilesDir(null)
				.getAbsolutePath() + "/" + DB_NAME);
	}

	private SQLiteDatabase openDatabase(String dbfile)
	{
		try
		{
			if (!(new File(dbfile).exists()))
			{
				InputStream is = this.context.getResources().openRawResource(
						R.raw.city);

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
		return database.query("city_table", // Which table to Select
				// strCols,// Which columns to return
				new String[] { "territory_name", "city_id" }, null, // WHERE
																	// clause
				null, // WHERE arguments
				null, // GROUP BY clause
				null, // HAVING clause
				null // Order-by clause
				);
	}

	public ArrayList<Bundle> getCity()
	{
		Cursor mCursor = database.query("city_table", // Which table to Select
				// strCols,// Which columns to return
				new String[] { "territory_name", "city_id" }, null, // WHERE
																	// clause
				null, // WHERE arguments
				null, // GROUP BY clause
				null, // HAVING clause
				null // Order-by clause
				);
		ArrayList<Bundle> array_list = new ArrayList<Bundle>();

		if (mCursor.getCount() != 0)
		{
			mCursor.moveToFirst();
			for (int i = 0; i < mCursor.getCount(); i++)
			{
				String city = mCursor.getString(0);
				
				String city_id = mCursor.getString(1);
				
				Bundle bundle = new Bundle();

				bundle.putString("city", city);
				bundle.putString("city_id", city_id);
					
				array_list.add(bundle);
				mCursor.moveToNext();
			}			
			mCursor.close();
			
			return array_list;
		}
		return array_list;
	}

	public ArrayList<Bundle> getArea(String rowId) throws SQLException
	{
		Cursor mCursor = database.query("city_detail_table", new String[] {
				"territory_name", "district_name", "district_id" },
				"territory_name" + " = ?", new String[] { "" + rowId + "" },
				null, null, null, null);
		
		ArrayList<Bundle> array_list = new ArrayList<Bundle>();

		if (mCursor.getCount() != 0)
		{
			mCursor.moveToFirst();
			
			for (int i = 0; i < mCursor.getCount(); i++)
			{
				String area = mCursor.getString(1);
				String area_id = mCursor.getString(2);
				
				Bundle bundle = new Bundle();

				bundle.putString("area", area);
				bundle.putString("area_id", area_id);

				array_list.add(bundle);	
				
				mCursor.moveToNext();
			}	
			mCursor.close();
			
			return array_list;
		}
		return array_list;
	}
	// query single entry
	public Cursor get(String rowId) throws SQLException
	{
		Cursor mCursor = database.query("city_detail_table", new String[] {
				"territory_name", "district_name", "district_id" },
				"territory_name" + " = ?", new String[] { "" + rowId + "" },
				null, null, null, null);

		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		return mCursor;
	}

}
