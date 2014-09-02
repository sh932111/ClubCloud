package getfunction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogShow
{
	private Callback mCallback;

	public interface Callback
	{
		public abstract void work();
		public abstract void cancel();
	}

	public void showStyle1(Context context, String title, String mesString,
			String checkString,Callback callback)
	{
		this.mCallback = callback;
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle(title);
		dialog.setIcon(android.R.drawable.ic_dialog_alert);
		dialog.setMessage(mesString);
		dialog.setCancelable(false);
		dialog.setPositiveButton(checkString,
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						mCallback.work();
					}	
				});
		dialog.show();
	}
	
	public void show(Context context, String title, String mesString,
			String checkString, String cancelString ,Callback callback)
	{
		this.mCallback = callback;
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle(title);
		dialog.setIcon(android.R.drawable.ic_dialog_alert);
		dialog.setMessage(mesString);
		dialog.setCancelable(false);
		dialog.setNegativeButton(checkString,
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						// TODO Auto-generated method stub
						mCallback.work(); 
					}

				});
		dialog.setPositiveButton(cancelString,
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						mCallback.cancel();
					}	
				});
		dialog.show();
	}
}
