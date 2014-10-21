package utils;

import java.text.ParseException;
import java.util.Calendar;

import com.candroidsample.R;
import com.candroidsample.StartActivity;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmUtils
{
	public static void showTravelAlarm(Context context,String set_time ,String id ,String title,String list)
	{
		Calendar cal = Calendar.getInstance();

		int i = 0;

		try
		{
			i = TimeUtils.getTimeGap(set_time)
					- (3600 * 5);
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cal.add(Calendar.SECOND, i);

		Intent intent = new Intent(context,
				getfunction.AlarmReceiver.class);

		intent.addCategory(id);

		intent.putExtra("TITLE", title);

		intent.putExtra("MESSAGE", list);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				context, 1, intent, PendingIntent.FLAG_ONE_SHOT);

		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(context.ALARM_SERVICE);

		alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
				pendingIntent);

		Toast.makeText(context,
				"open time:" + set_time,
				Toast.LENGTH_SHORT).show();
	}
	
}
