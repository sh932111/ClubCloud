package utils;

import homedetail.PersonalInformation;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

//import com.candroidsample.HomePage;

public class NotificationUtls
{
	public static void generateNotification(Context context, String message,
			String title)
	{
		String ns = Context.NOTIFICATION_SERVICE;
		
		NotificationManager mNotificationManager = 
                (NotificationManager) context.getSystemService(ns);
		
		Long when = System.currentTimeMillis();
		
		CharSequence tickerText = message;
		
        Notification notification = 
                new Notification(com.candroidsample.R.drawable.icon, tickerText, when);
        
        notification.vibrate = new long[] {100,250,100,500};
        
        CharSequence contentTitle = title;
        CharSequence contentText = message;
        
        PendingIntent contentIntent = 
                PendingIntent.getActivity(context, 0, new Intent(context ,PersonalInformation.class), 0);
        
        notification.setLatestEventInfo(
        		context, contentTitle, contentText, contentIntent); 
        
        mNotificationManager.notify(9998, notification);
	}
	
}
