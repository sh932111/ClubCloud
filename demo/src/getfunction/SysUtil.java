package getfunction;

import com.candroidsample.CaldroidSampleActivity;

import android.content.Context;  
import android.content.Intent;  
public class SysUtil 
{  
    public static final int  EXIT_APPLICATION = 0x0001;  
      
    private Context mContext;  
      
    public SysUtil(Context context){  
        this.mContext = context;  
    }  
    public void exit()
    {
    	Intent mIntent = new Intent();  
        mIntent.setClass(mContext, CaldroidSampleActivity.class);  
        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
        mIntent.putExtra("flag", EXIT_APPLICATION);  
        mContext.startActivity(mIntent);  
    }  
}  