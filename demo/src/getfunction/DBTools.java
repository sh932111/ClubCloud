package getfunction;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import getdb.DBManager;
import getdb.EventDB;
import getdb.PushDB;
import getdb.TravelDB;
import getdb.UserDB;

public class DBTools
{
	public static ArrayList<Bundle> getTravelDBAll(Context context)
	{
		TravelDB mDbHelper = new TravelDB(context);

		mDbHelper.open();

		ArrayList<Bundle> array_list = mDbHelper.getAll();

		mDbHelper.close();
		
		return array_list;
	}
	
	public static int getUserData(Context context)
	{
		UserDB mDbHelper = new UserDB(context);

		mDbHelper.open();

		int count = mDbHelper.isTableExists();

		mDbHelper.close();

		return count;
	}
	
	public static String getUserName(Context context)
	{
		UserDB userDB = new UserDB(context);

		userDB.open();

		ArrayList<String> array_list = userDB.getAllDate();

		userDB.close();

		return array_list.get(0);
	}
	
	public static void startInit(Context context)
	{
		DBManager dbHelper = new DBManager(context);

		dbHelper.openDatabase();

		dbHelper.closeDatabase();
	}
	
	public static void saveEventData(Context context ,Bundle bundle)
	{
		String _id = bundle.getString("data_id");

		String title = bundle.getString("title");

		String detail = bundle.getString("detail"); // getString(R.string.gcm_message);

		String time = bundle.getString("time");

		String time_detail = bundle.getString("time_detail"); // getString(R.string.gcm_message);

		String image = bundle.getString("image");
		
		EventDB mDbHelper = new EventDB(context);

		mDbHelper.open();

		mDbHelper.create(Long.parseLong(_id), title, detail, time,
				time_detail, image, "emergency");

		mDbHelper.close();
	}
	
	public static void savePushData(Context context ,Bundle bundle)
	{
		String _id = bundle.getString("data_id");

		String title = bundle.getString("title");

		String detail = bundle.getString("detail"); // getString(R.string.gcm_message);

		String time = bundle.getString("time");

		String time_detail = bundle.getString("time_detail"); // getString(R.string.gcm_message);

		String image = bundle.getString("image");

		String address = bundle.getString("city")
				+ bundle.getString("area") + bundle.getString("address");

		PushDB mDbHelper = new PushDB(context);

		mDbHelper.open();

		mDbHelper.create(Long.parseLong(_id), title, detail, time,
				time_detail, "1", image, address);

		mDbHelper.close();
	}
}
