package getfunction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.candroidsample.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class FolderFunction
{
	public void folderBuild(String folder_name)
	{
		
		File folder = new File(Environment.getExternalStorageDirectory() + "/" + folder_name);
		boolean success = true;
		
	    if (!folder.exists()) 
	    {
	        //Toast.makeText(MainActivity.this, "Directory Does Not Exist, Create It", Toast.LENGTH_SHORT).show();
	        success = folder.mkdir();
	    }
	    
	    if (success) 
	    {
	        //Toast.makeText(StrokeShowPage.this, "Directory Created", Toast.LENGTH_SHORT).show();
	    }
	    else 
	    {
	      //  Toast.makeText(StrokeShowPage.this, "Failed - Error", Toast.LENGTH_SHORT).show();
	    }
	}
	public void saveImage(Bitmap bmImage ,String extStorage,String locaction)
	{

		File file = new File(extStorage, locaction);

		try
		{
			OutputStream outStream = new FileOutputStream(file);

			bmImage.compress(Bitmap.CompressFormat.PNG, 100, outStream);

			outStream.flush();
			outStream.close();

		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}
}