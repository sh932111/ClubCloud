package httpfunction;


import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SendPostRunnable implements Runnable
{	
	private Callback mCallback;
	String URL = null;
	String ID = null;
	List<NameValuePair> parems = null;

	public interface Callback
	{
		public abstract void service_result(Message msg);
	}
	
	public SendPostRunnable(String url ,List<NameValuePair> list ,Callback callback)
	{
		this.URL = url;
		this.parems = list;
		this.mCallback = callback;
	}

	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		GetServerMessage demo = new GetServerMessage();

		HashMap<String, Object> map = demo.stringQuery(URL, parems);
		
		Bundle countBundle = new Bundle();
		
		countBundle.putSerializable("resultData", map);  

		Message msg = new Message();
		
		msg.setData(countBundle);
		
		mHandler.sendMessage(msg);
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);

			mCallback.service_result(msg);
		}
	};
	
}

