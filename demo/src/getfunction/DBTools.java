package getfunction;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import getdb.DBManager;
import getdb.EventDB;
import getdb.PushDB;
import getdb.TravelDB;
import getdb.UserDB;
import homedetail.ShowPushDetail;

public class DBTools
{
	public static void deleteDB(Context context ,Long id)
	{
		PushDB mDbHelper = new PushDB(context);

		mDbHelper.open();

		mDbHelper.delete(id);

		mDbHelper.close();
	}

	
	public static void updatePushDBImage(Context context ,Long id)
	{

		PushDB mDbHelper = new PushDB(context);

		mDbHelper.open();

		mDbHelper.updateImage(id, "2");

		mDbHelper.close();
	}

	public static void updatePushDBLook(Context context ,Long id)
	{
		PushDB mDbHelper = new PushDB(context);

		mDbHelper.open();

		mDbHelper.updateLook(id, "0");

		mDbHelper.close();
	}
	
	public static Bundle getPushDetail(Context context ,Long id)
	{
		PushDB pushDB = new PushDB(context);

		pushDB.open();

		Bundle get_bundle = pushDB.get(id);
		
		pushDB.close();

		return get_bundle;
	}
	
	public static ArrayList<Bundle> getType(Context context,String type,String getYeay,String getMonth)
	{
		EventDB db = new EventDB(context);

		db.open();

		ArrayList<Bundle> list = db.getType(type, getYeay,
				getMonth);
				
		db.close();
		
		return list;
	}
	
	public static void addTravel(Context context ,Long id,String title ,String detail ,String date ,String time , String check, String image ,String address)
	{
		TravelDB mDbHelper = new TravelDB(context);
		
		mDbHelper.open();

		mDbHelper.create(id, 
				title, 
				detail, 
				date, 
				time, 
				check, 
				image,
				address
				);

		mDbHelper.close();
	}
	
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
	
	public static ArrayList<String> getUserAllData(Context context)
	{
		UserDB userDB = new UserDB(context);

		userDB.open();

		ArrayList<String> array_list = userDB.getAllDate();

		userDB.close();
		
		return array_list;
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
	
	public static void saveEventData(Context context ,Object object ,String type)
	{
		if (object instanceof Bundle)
		{
			Bundle bundle = (Bundle)object;
			
			String _id = bundle.getString("data_id");

			String title = bundle.getString("title");

			String detail = bundle.getString("detail"); // getString(R.string.gcm_message);

			String time = bundle.getString("time");

			String time_detail = bundle.getString("time_detail"); // getString(R.string.gcm_message);

			String image = bundle.getString("image");
			
			EventDB mDbHelper = new EventDB(context);

			mDbHelper.open();

			mDbHelper.create(Long.parseLong(_id), title, detail, time,
					time_detail, image, type);

			mDbHelper.close();
		}
		
		else 
		{
			JSONObject result = (JSONObject)object;
			
			String data_id = null;
			String title = null;
			String detail = null;
			String date = null;
			String time = null;
			String image = null;
			
			try
			{
				data_id = result
						.getString("data_id");
				title = result.getString("title");
				detail = result.getString("detail");
				date = result.getString("date");
				time = result.getString("time");
				image = result.getString("image");
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch
				// block
				e.printStackTrace();
			}
			EventDB mDbHelper = new EventDB(
					context);

			mDbHelper.open();

			mDbHelper.create(
					Long.parseLong(data_id),
					title, detail, date, time,
					image, type);

			mDbHelper.close();
		}
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
