package cs247.group15.app;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import org.jdom.JDOMException;

import cs247.group15.data.Constants;
import cs247.group15.data.DataRequest;
import cs247.group15.data.ImportantInformation;
import cs247.group15.data.Properties;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.util.Log;

public class CS247Service extends IntentService {

	public CS247Service() {
		super("CS247Service");
	}

	ServiceBinder binder;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(Constants.information, "The service has been started.");
		binder = new ServiceBinder();
		//binder.sendRequest(Properties.getLastUpdate());
		return START_STICKY;
	}

	@Override
	protected void onHandleIntent(Intent intent) {}
	
	class ServiceBinder extends Binder
	{
		public CS247Service getService() {return CS247Service.this;}
		
		/*public List<ImportantInformation> sendRequest(long date)
		{
			DataSender ds = null;
			try {
				Log.d(Constants.information, "Sending data request with date: " + date);
				ds = new DataSender(new DataRequest(date));
				Properties.setLastUpdate(System.currentTimeMillis()); 
				//TODO: might want to put a "correction constant" in here to allow for delay between updates being available & getting the updates
			} 
			catch (UnknownHostException e) {
				Log.d(Constants.error, e.getMessage());
				e.printStackTrace();
			} 
			catch (IOException e) {
				Log.d(Constants.error, e.getMessage());
				e.printStackTrace();
			} 
			catch (JDOMException e) {
				Log.d(Constants.error, e.getMessage());
				e.printStackTrace();
			}
			
			if(ds!=null)
			{
				Log.d(Constants.information, ds.getList().toString());
				return ds.getList();
			}
			else {return null;}
		}*/
		
	}

}