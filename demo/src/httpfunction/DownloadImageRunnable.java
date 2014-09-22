package httpfunction;

import getfunction.FolderFunction;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.candroidsample.R;

public class DownloadImageRunnable
{
	String set_img_name;
	String folderName;
	String IP;
	DownloadImage loadPic;
	Context mContext;
	private Callback mCallback = null;
	
	public interface Callback
	{
		public abstract void service_result();
	}
	

	public DownloadImageRunnable(String img_name,Context context ,String folder_name ,String ip)
	{
		this.set_img_name = img_name;
		this.mContext = context;
		this.folderName = folder_name;
		this.IP = ip;
	}

	public DownloadImageRunnable(String img_name,Context context ,String folder_name ,String ip ,Callback callback)
	{
		this.set_img_name = img_name;
		this.mContext = context;
		this.folderName = folder_name;
		this.IP = ip;
		this.mCallback = callback;
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

					String app_path = mContext.getExternalFilesDir(null).getAbsolutePath() + "/"+folderName+"/" + set_img_name + ".png";
					
					if (loadPic.getImg() != null)
					{
						boolean check = setfolder.saveImage(loadPic.getImg(),  app_path);	

						if (mCallback != null && check)
						{
							mCallback.service_result();
						}
					}
					
					break;
				}
				super.handleMessage(msg);
			}
		};
		
		loadPic.handleWebPic(mContext.getString(R.string.IP)
				+ IP + set_img_name + ".png",
				mHandler);
	}
}
