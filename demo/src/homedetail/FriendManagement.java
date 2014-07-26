package homedetail;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.candroidsample.GetServerMessage;
import com.candroidsample.R;
import com.candroidsample.LoginPage.sendPostRunnable;
import com.candroidsample.R.layout;
import com.candroidsample.R.menu;

import android.os.Bundle;
import android.os.Message;
import android.app.Activity;
import android.view.Menu;

public class FriendManagement extends Activity
{
	private String srcPath = "/storage/sdcard0/myFile.png";
	private String actionUrl = "http://192.168.1.31:8888/ClubCloud/upload.php";
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_management);
		
		sendPostRunnable post = new sendPostRunnable(actionUrl);
		
		Thread t = new Thread(post);

		t.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friend_management, menu);
		return true;
	}

	private void uploadFile(String uploadUrl)
	{
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "******";
		
		try
		{
			URL url = new URL(uploadUrl);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url
					.openConnection();
			// 设置每次传输的流大小，可以有效防止手机因为内存不足崩溃
			// 此方法用于在预先不知道内容长度时启用没有进行内部缓冲的 HTTP 请求正文的流。
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
					+ "qqqyg.png"
					+ "\""
					+ end);

			dos.writeBytes(end);

			FileInputStream fis = new FileInputStream(srcPath);

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
			
			//Toast.makeText(this, result, Toast.LENGTH_LONG).show();
			dos.close();
			is.close();

		}
		catch (Exception e)
		{
			e.printStackTrace();
			setTitle(e.getMessage());
		}
	}
	public class sendPostRunnable implements Runnable
	{
		String URL = null;

		public sendPostRunnable(String url)
		{
			this.URL = url;
		}

		@Override
		public void run()
		{
			// TODO Auto-generated method stub
			uploadFile(URL);
		}

	}
}
