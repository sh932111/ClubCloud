package getfunction;

import homedetail.EmergencyReport;
import homedetail.EventDelivery;
import homedetail.PersonalInformation;
import homedetail.ShowTravelList;

import com.candroidsample.CaldroidSampleActivity;
import com.candroidsample.HomePage;

import android.content.Context;
import android.content.Intent;

public class PageUtil
{
	public static final int EXIT_APPLICATION0 = 0x0001;
	public static final int EXIT_APPLICATION1 = 0x0002;
	public static final int EXIT_APPLICATION2 = 0x0003;
	public static final int EXIT_APPLICATION3 = 0x0004;
	public static final int EXIT_APPLICATION4 = 0x0005;
	public static final int EXIT_APPLICATION5 = 0x0006;
	//public static final int EXIT_APPLICATION6 = 0x0007;

	private Context mContext;

	public PageUtil(Context context)
	{
		this.mContext = context;
	}

	public void exit(int i)
	{
		Intent mIntent = new Intent();
		
		if (i == 0)
		{
			mIntent.setClass(mContext, HomePage.class);
			mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
			mIntent.putExtra("flag", EXIT_APPLICATION0);
		}
		else if (i == 1)
		{
			mIntent.setClass(mContext, PersonalInformation.class);
			mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
			mIntent.putExtra("flag", EXIT_APPLICATION1);
		}
		else if (i == 2)
		{
			mIntent.setClass(mContext, CaldroidSampleActivity.class);
			mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
			mIntent.putExtra("flag", EXIT_APPLICATION2);	
		}
		else if (i == 3)
		{
			mIntent.setClass(mContext, ShowTravelList.class);
			mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
			mIntent.putExtra("flag", EXIT_APPLICATION3);	
		}
		else if (i == 4)
		{
			mIntent.setClass(mContext, EventDelivery.class);
			mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
			mIntent.putExtra("flag", EXIT_APPLICATION4);	
		}
		else if (i == 5)
		{
			mIntent.setClass(mContext, EmergencyReport.class);
			mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
			mIntent.putExtra("flag", EXIT_APPLICATION5);
		}
		
		mContext.startActivity(mIntent);
	}
}
