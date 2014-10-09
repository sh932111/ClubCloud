package uifunction;

import java.util.ArrayList;

import getdb.EventDB;
import getfunction.EventAdpter;
import getfunction.MyAdapter;
import homedetail.PersonalInformation;

import com.candroidsample.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

@SuppressLint("NewApi")
public class ShowEventDailog extends DialogFragment
{
	ListView mylist; 
	
	String[] listitems = { "item01", "item02", "item03", "item04", "item01", "item02", "item03", "item04", "item01", "item02", "item03", "item04", "item01", "item02", "item03", "item04" }; 
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.showeventdailog, null);
		
		mylist = ( ListView ) view.findViewById(R.id.listView1); 
		
		String app_path = getActivity().getExternalFilesDir(null).getAbsolutePath() + "/"+"pushphoto"+"/" ;
		
		EventDB db = new EventDB(getActivity());
		
		db.open();
		
		ArrayList<Bundle> mArrayList= db.getType("event");
		
		EventAdpter adapter = new EventAdpter(getActivity(), mArrayList, app_path);
		
		db.close();
		
//		ArrayAdapter< String > adapter = new ArrayAdapter< String >(getActivity(),
//                android.R.layout.simple_list_item_1, listitems);
        
		mylist.setAdapter(adapter); 
		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		builder.setView(view)
		.setNegativeButton("Close", null);
		
		return builder.create();
	}
//	public ShowEventDailog(ArrayList<Bundle> bundles,String app_path)
//	{
//		this.mArrayList = bundles;
//		this.appPath = app_path;
//	}
	
}
