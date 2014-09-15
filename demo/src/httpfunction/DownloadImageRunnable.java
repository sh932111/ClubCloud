package httpfunction;

import getfunction.FolderFunction;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.candroidsample.R;

public class DownloadImageRunnable
{
	String set_img_name;
	DownloadImage loadPic;
	Context mContext;
	
	public DownloadImageRunnable(String img_name,Context context)
	{
		this.set_img_name = img_name;
		this.mContext = context;
	}
	
	@SuppressLint("HandlerLeak")
	public void downLoadImage()
	{
		this.loadPic = new DownloadImage();
		
		Handler mHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				switch (msg.what)
				{
				case 1:

					FolderFunction setfolder = new FolderFunction();

					String app_path = mContext.getExternalFilesDir(null).getAbsolutePath() + "/userphoto/" + set_img_name + ".PNG";
					
					if (loadPic.getImg() != null)
					{
						setfolder.saveImage(loadPic.getImg(),  app_path);	
					}
					
					break;
				}
				super.handleMessage(msg);
			}
		};
		
		loadPic.handleWebPic(mContext.getString(R.string.IP)
				+ mContext.getString(R.string.downloadUserImage) + set_img_name + ".png",
				mHandler);
	}
}
