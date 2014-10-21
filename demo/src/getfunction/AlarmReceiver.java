package getfunction;

import utils.NotificationUtls;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;



public class AlarmReceiver extends BroadcastReceiver 
{
	
	@Override
	public void onReceive(Context arg0, Intent data) 
	{
		String title = data.getStringExtra("TITLE") ;

		String message = data.getStringExtra("MESSAGE") ;

		NotificationUtls.generateNotification(arg0, message, title);
	}
}

