package getfunction;

import getfunction.MyAdapter.ViewTag;

import java.util.ArrayList;

import com.candroidsample.R;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EventAdpter extends BaseAdapter
{
	private LayoutInflater myInflater;
	ArrayList<Bundle> mArrayList;
	String appPath;
	public EventAdpter(Context ctxt,ArrayList<Bundle> bundles,String path)
	{
		myInflater = LayoutInflater.from(ctxt);
		this.mArrayList = bundles;
		this.appPath = path;
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
		ViewTag viewTag;
		if(convertView == null)
		{
			convertView = myInflater.inflate(R.layout.eventlist, null);
			viewTag = new ViewTag(
					(ImageView)convertView.findViewById(R.id.list_image),
					(TextView) convertView.findViewById(R.id.list_title),
					(TextView) convertView.findViewById(R.id.list_detail),
					(TextView) convertView.findViewById(R.id.list_time)
					);
			
			convertView.setTag(viewTag);
		}
		else
		{
			viewTag = (ViewTag) convertView.getTag();
		}
		
		viewTag.mTitie.setText(mArrayList.get(position).getString("Title"));
		viewTag.mDetail.setText(mArrayList.get(position).getString("Message"));
		viewTag.mTime.setText(mArrayList.get(position).getString("Date")+" "+mArrayList.get(position).getString("Time"));
		
		if (mArrayList.get(position).getString("Image").equals("1"))
		{
			ImageFunction get_image = new ImageFunction();

			String app_path = appPath + mArrayList.get(position).getLong("ID") + ".png";
			
			System.out.println("appPath:"+app_path);
			
			viewTag.mIcon.setImageBitmap(get_image
							.getBitmapFromSDCard(app_path));
			
		}
		else 
		{
			viewTag.mIcon.setImageBitmap(null);
		}
		// TODO Auto-generated method stub
		return convertView;
	}

	class ViewTag
	{
		ImageView mIcon;
		TextView mTitie;
		TextView mDetail;
		TextView mTime;
		
		public ViewTag(ImageView icon, TextView title, TextView text,TextView time)
		{
			this.mIcon = icon;
			this.mTitie = title;
			this.mDetail = text;
			this.mTime = time;
		}
	}

}
