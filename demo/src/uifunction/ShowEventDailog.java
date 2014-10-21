package uifunction;

import java.util.ArrayList;

import getfunction.DBTools;

import com.candroidsample.R;

import adapter.EventAdpter;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class ShowEventDailog extends DialogFragment
{
	ListView mylist; 
	TextView nodata;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		String year = getArguments().getString("YEAR");
		String month = getArguments().getString("MONTH");

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.showeventdailog, null);
		
		mylist = ( ListView ) view.findViewById(R.id.listView1); 
		nodata = ( TextView ) view.findViewById(R.id.nodata); 
		
		String app_path = getActivity().getExternalFilesDir(null).getAbsolutePath() + "/"+"pushphoto"+"/" ;
		
		ArrayList<Bundle> mArrayList= DBTools.getType(getActivity(),"event",year,month);
		
		EventAdpter adapter = new EventAdpter(getActivity(), mArrayList, app_path ,1);
		
		if (mArrayList.size() != 0)
		{
			nodata.setVisibility(View.GONE);
		}
        
		mylist.setAdapter(adapter);

		builder.setView(view)
		.setNegativeButton("Close", null);
		
		return builder.create();
	}
}
