package getfunction;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import getdb.CityDB;
import getdb.EventDB;
import getdb.PushDB;
import getdb.TravelDB;
import getdb.UserDB;

public class DBTools
{
	public static ArrayList<Bundle> getCity(Context context)
	{
		CityDB dbManager = new CityDB(context);

		dbManager.openDatabase();

		ArrayList<Bundle> arrayList = dbManager.getCity();
		
		Cursor mCursor = dbManager.get(arrayList.get(0).getString("city_id"));
		
		System.out.println(mCursor.getCount());
		
		dbManager.closeDatabase();
		
		return arrayList;
	}
	
	public static ArrayList<Bundle> getArea(Context context,String rowID)
	{
		CityDB dbManager = new CityDB(context);
		
		dbManager.openDatabase();

		ArrayList<Bundle> arrayList = dbManager.getArea(rowID);
		
		dbManager.closeDatabase();
		
		return arrayList;
	}

	
	public static void creatUserData(Context context,String username, String password, String name,
			String user_id, String device_token, String device_os,
			String user_city, String user_city_detail, String city_id,
			String user_city_detail_id,String cellphone )
	{
		UserDB mDbHelper = new UserDB(context);

		mDbHelper.open();
		
		mDbHelper.create(username, password, name, user_id,
				device_token, "android",user_city,user_city_detail,city_id,user_city_detail_id,cellphone);
		
		mDbHelper.close();
	}
	
	public static ArrayList<Bundle> getPushAll(Context context)
	{
		PushDB mDbHelper = new PushDB(context);

		mDbHelper.open();

		ArrayList<Bundle> arrayList = mDbHelper.getAll();

		mDbHelper.close();
		
		return arrayList;
	}
	
	public static ArrayList<Bundle> getTravelToDate(Context context ,String date)
	{
		TravelDB mDbHelper = new TravelDB(context);

		mDbHelper.open();
		
		ArrayList<Bundle> arrayList = mDbHelper.getDateData(date);

		mDbHelper.close();
	
		return arrayList;
	}
	
	public static void updateTravelAll(Context context ,Long id, String title ,String detail,String date,String time,String image)
	{
		TravelDB mDbHelper = new TravelDB(context);
		
		mDbHelper.open();

		mDbHelper.updateAll(id, title, detail,date ,time,image);

		mDbHelper.close();
	}
	
	public static void updateTravelImage(Context context ,Long id)
	{
		TravelDB mDbHelper = new TravelDB(context);

		mDbHelper.open();

		mDbHelper.updateImage(id, "2");

		mDbHelper.close();
	}
	
	public static void updateTravelLook(Context context ,Long id)
	{
		TravelDB mDbHelper = new TravelDB(context);

		mDbHelper.open();

		mDbHelper.updateLook(id, "0");

		mDbHelper.close();
	}
	
	public static void deleteDB(Context context ,Long id,int obj)
	{
		if(obj == 1)
		{
			PushDB mDbHelper = new PushDB(context);

			mDbHelper.open();

			mDbHelper.delete(id);

			mDbHelper.close();
		}
		else if (obj == 2) 
		{
			TravelDB mDbHelper = new TravelDB(context);
			
			mDbHelper.open();

			mDbHelper.delete(id);

			mDbHelper.close();
		}
	}

	public static Bundle getTravelDetail(Context context ,Long id)
	{
		TravelDB travelDB = new TravelDB(context);

		travelDB.open();
		
		Bundle get_bundle = travelDB.get(id);

		travelDB.close();
		
		return get_bundle;
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
	
	public static void savePushData(Context context ,Object object)
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

			String address = bundle.getString("city")
					+ bundle.getString("area") + bundle.getString("address");

			PushDB mDbHelper = new PushDB(context);

			mDbHelper.open();

			mDbHelper.create(Long.parseLong(_id), title, detail, time,
					time_detail, "1", image, address);

			mDbHelper.close();
		}
		else if (object instanceof JSONObject)
		{
			JSONObject bundle = (JSONObject)object;
			
			String _id = "";
			String title = "";
			String detail = "";
			String time= "";
			String time_detail= "";
			String image= "";
			String address= "";
			
			try
			{
				_id = bundle.getString("data_id");
				title = bundle.getString("title");
				detail = bundle.getString("detail");
				time = bundle.getString("time");
				time_detail = bundle.getString("time_detail");
				image = bundle.getString("image");
				address = bundle.getString("address_city")
							+ bundle.getString("address_area") + bundle.getString("address");
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			PushDB mDbHelper = new PushDB(context);

			mDbHelper.open();

			mDbHelper.create(Long.parseLong(_id), title, detail, time,
					time_detail, "1", image, address);

			mDbHelper.close();
		}
	}
}
