package adapter;

import java.util.ArrayList;

import utils.ViewHolder;

import com.candroidsample.R;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import utils.LoadImageAsyncTask;

public class EventAdpter extends BaseAdapter
{
	private LayoutInflater myInflater;
	ArrayList<Bundle> mArrayList;
	String appPath;
	int Type;
	
	private Callback mCallback;

	public interface Callback
	{
		public abstract void responsePeace(int i);
		public abstract void responseHelp(int i);
	}
	public EventAdpter(Context ctxt,ArrayList<Bundle> bundles,String path ,int type,Callback callback)
	{
		myInflater = LayoutInflater.from(ctxt);
		this.mArrayList = bundles;
		this.appPath = path;
		this.Type = type;
		this.mCallback = callback;
	}
	public EventAdpter(Context ctxt,ArrayList<Bundle> bundles,String path ,int type)
	{
		myInflater = LayoutInflater.from(ctxt);
		this.mArrayList = bundles;
		this.appPath = path;
		this.Type = type;
	}
	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
	
		return mArrayList.size();
	}

	@Override
	public Object getItem(int position) 
	{
		// TODO Auto-generated method stub
		return mArrayList.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (Type == 1)
		{
			ViewHolder viewTag;
			if(convertView == null)
			{
				convertView = myInflater.inflate(R.layout.eventlist, null);
				viewTag = new ViewHolder(
						(ImageView)convertView.findViewById(R.id.list_image),
						(TextView) convertView.findViewById(R.id.list_title),
						(TextView) convertView.findViewById(R.id.list_detail),
						(TextView) convertView.findViewById(R.id.list_time)
						);
				
				convertView.setTag(viewTag);
			}
			else
			{
				viewTag = (ViewHolder) convertView.getTag();
			}
			
			viewTag.mTitie.setText(mArrayList.get(position).getString("Title"));
			viewTag.mDetail.setText(mArrayList.get(position).getString("Message"));
			viewTag.mTime.setText(mArrayList.get(position).getString("Date")+" "+mArrayList.get(position).getString("Time"));
			
			if (mArrayList.get(position).getString("Image").equals("1"))
			{
				String app_path = appPath + mArrayList.get(position).getLong("ID") + ".png";
				
				viewTag.img_path = app_path;
				new LoadImageAsyncTask().execute(viewTag);
			}
			else 
			{
				viewTag.mIcon.setImageBitmap(null);
			}
			// TODO Auto-generated method stub
			return convertView;
		}
		else 
		{
			final int point = position;
			
			EmergencyViewTag viewTag;
			if(convertView == null)
			{
				convertView = myInflater.inflate(R.layout.emergencylist, null);
				viewTag = new EmergencyViewTag((TextView) convertView.findViewById(R.id.list_title2),
						(TextView) convertView.findViewById(R.id.list_detail2),
						(TextView) convertView.findViewById(R.id.list_time2),
						(Button)convertView.findViewById(R.id.responsebt1),
						(Button)convertView.findViewById(R.id.responsebt2));
				convertView.setTag(viewTag);
			}
			else
			{
				viewTag = (EmergencyViewTag) convertView.getTag();
			}
			viewTag.mTitie.setText(mArrayList.get(position).getString("Title"));
			viewTag.mDetail.setText(mArrayList.get(position).getString("Message"));
			viewTag.mTime.setText(mArrayList.get(position).getString("Date")+" "+mArrayList.get(position).getString("Time"));
			
			viewTag.mResponse.setOnClickListener(new Button.OnClickListener()
			{
				@Override
				public void onClick(View arg0)
				{
					mCallback.responsePeace(point);
				}
			});
			viewTag.mResponse_help.setOnClickListener(new Button.OnClickListener()
			{
				@Override
				public void onClick(View arg0)
				{
					mCallback.responseHelp(point);
				}
			});
			return convertView;
		}
		
	}

	class EmergencyViewTag
	{
		TextView mTitie;
		TextView mDetail;
		TextView mTime;
		Button mResponse;
		Button mResponse_help;
		
		public EmergencyViewTag(TextView title, TextView text,TextView time,Button bt1,Button bt2)
		{
			this.mTitie = title;
			this.mDetail = text;
			this.mTime = time;
			this.mResponse = bt1;
			this.mResponse_help = bt2;
		}
	}
	
}
