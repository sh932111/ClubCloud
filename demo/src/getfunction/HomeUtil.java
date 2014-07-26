package getfunction;

import com.candroidsample.HomePage;

import android.content.Context;
import android.content.Intent;

public class HomeUtil
{
	public static final int EXIT_APPLICATION = 0x0002;

	private Context mContext;

	public HomeUtil(Context context)
	{
		this.mContext = context;
	}

	public void exit()
	{
		Intent mIntent = new Intent();
		mIntent.setClass(mContext, HomePage.class);
		mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
		mIntent.putExtra("flag", EXIT_APPLICATION);
		mContext.startActivity(mIntent);
	}
}
