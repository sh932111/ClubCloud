package getfunction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.graphics.Bitmap;


public class FolderFunction
{
	public void folderBuild(String folder_name)
	{
		
		File folder = new File(folder_name);
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
	public static boolean saveImage(Bitmap bmImage ,String locaction)
	{

		File file = new File( locaction);

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

			return false;
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();

			return false;
		}

		return true;
	}
}
