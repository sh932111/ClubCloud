package adapter;

import java.util.ArrayList;

import com.candroidsample.R;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageAdapter extends BaseAdapter
{
	private LayoutInflater myInflater;

	ArrayList<Bundle> mArrayList;
	
	public MessageAdapter(Context ctxt, ArrayList<Bundle> bundles )
	{
		myInflater = LayoutInflater.from(ctxt);
		this.mArrayList = bundles;
	}
	

	public void refresh(ArrayList<Bundle> list)
	{
		mArrayList = list;
		notifyDataSetChanged();
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
	public View getView(int position,View convertView,ViewGroup parent)
	{
		ViewTag viewTag;
		
		if(convertView == null)
		{
			
			convertView = myInflater.inflate(R.layout.mylist, null);
			
			viewTag = new ViewTag(
					(ImageView)convertView.findViewById(R.id.imglogo),
					(TextView) convertView.findViewById(R.id.list_title),
					(TextView) convertView.findViewById(R.id.time_text)
					);
			
			convertView.setTag(viewTag);
		}
		
		else
		{
			viewTag = (ViewTag) convertView.getTag();
		}
		
		viewTag.title.setText(mArrayList.get(position).getString("Title"));
		viewTag.text.setText(mArrayList.get(position).getString("Date")+" "+mArrayList.get(position).getString("Time"));
		
		int v = Integer.parseInt(mArrayList.get(position).getString("Check"));
		
		if (v == 0) 
		{
			viewTag.icon.setBackgroundColor(Color.YELLOW);
		}
		else if (v == 1) 
		{
			viewTag.icon.setBackgroundColor(Color.RED);
		}
		
		return convertView;
	}
	
	
	class ViewTag
	{
		ImageView icon;
		TextView title;
		TextView text;
		
		public ViewTag(ImageView icon, TextView title, TextView text)
		{
			this.icon = icon;
			this.title = title;
			this.text = text;
		}
	}

}
