package getfunction;
import com.candroidsample.HomePage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;


public class AlarmReceiver extends BroadcastReceiver 
{
	
	@Override
	public void onReceive(Context arg0, Intent data) 
	{
		String id = data.getStringExtra("TITLE") ;

		String message = data.getStringExtra("MESSAGE") ;

		System.out.println(id);
		
		String ns = Context.NOTIFICATION_SERVICE;
		
		NotificationManager mNotificationManager = 
                (NotificationManager) arg0.getSystemService(ns);
		
		Long when = System.currentTimeMillis();
		
        
		CharSequence tickerText = message;
		
        Notification notification = 
                new Notification(com.candroidsample.R.drawable.icon, tickerText, when);
        
        notification.vibrate = new long[] {100,250,100,500};
        
        CharSequence contentTitle = id;
        CharSequence contentText = message;
        
        PendingIntent contentIntent = 
                PendingIntent.getActivity(arg0, 0, new Intent(arg0 ,HomePage.class), 0);
        
        notification.setLatestEventInfo(
        		arg0, contentTitle, contentText, contentIntent); 
        
        mNotificationManager.notify(9998, notification);
		
        		
	}
}

