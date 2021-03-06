package httpfunction;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UploadImage implements Runnable
{
	String UploadUrl = null;
	String ImagePath = null;
	String ImageName = null;
	
	public UploadImage(String uploadUrl,String image_path ,String image_name)
	{
		this.UploadUrl = uploadUrl;
		this.ImagePath = image_path;
		this.ImageName = image_name;

	}

	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "******";
		
		try
		{
			System.out.println(UploadUrl);
			
			URL url = new URL(UploadUrl);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url
					.openConnection();
			
			httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
			// 允许输入输出流
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(false);
			// 使用POST方法
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			DataOutputStream dos = new DataOutputStream(
					httpURLConnection.getOutputStream());
			
			dos.writeBytes(twoHyphens + boundary + end);
			
			dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""
					+ ImageName+".png"
					+ "\""
					+ end);

			dos.writeBytes(end);

			FileInputStream fis = new FileInputStream(ImagePath);

			byte[] buffer = new byte[8192]; // 8k
			int count = 0;
			// 读取文件
			while ((count = fis.read(buffer)) != -1)
			{
				dos.write(buffer, 0, count);
			}
			fis.close();

			dos.writeBytes(end);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
			dos.flush();

			InputStream is = httpURLConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			String result = br.readLine();
			System.out.println(result);
			//Toast.makeText(this, result, Toast.LENGTH_LONG).show();
			dos.close();
			is.close();

		}
		catch (Exception e)
		{
			e.printStackTrace();
			
		}
	}
}
