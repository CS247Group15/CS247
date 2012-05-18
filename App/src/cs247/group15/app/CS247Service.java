package cs247.group15.app;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jdom.JDOMException;

import cs247.group15.data.Constants;
import cs247.group15.data.DataRequest;
import cs247.group15.data.ImportantInformation;
import cs247.group15.data.ListClass;
import cs247.group15.data.PrintableDate;
import cs247.group15.data.Properties;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.provider.ContactsContract.CommonDataKinds.Im;
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
	
	@Override
	public Binder onBind(Intent intent) {
		super.onBind(intent);
		return binder;
	}
	
	class ServiceBinder extends Binder
	{
		private List<ImportantInformation> listOfInformation = new ArrayList<ImportantInformation>();
		
		public CS247Service getService() {return CS247Service.this;}
		
		public List<ImportantInformation> getImportantInformationList()
		{   
			return listOfInformation;
		}
		public ArrayList<ListClass> getDatedImportantInformationList()
		{
			ArrayList<ListClass> datedList = new ArrayList<ListClass>();
			
			if(listOfInformation.size()>0)
			{
				PrintableDate currentDate = listOfInformation.get(0).getDate();
				datedList.add(currentDate);
				
				for(ImportantInformation info : listOfInformation)
				{
					if(info.getDate().sameDayAs(currentDate))
					{
						datedList.add(info);
					}
					else
					{
						datedList.add(info.getDate());
						datedList.add(info);
						currentDate = info.getDate();
					}
				}
			}
			return datedList;
		}
		
		public void sendRequest(long date, OnRequestComplete listener)
		{
			/*DataSender ds = null;
			try {
				Log.d(Constants.information, "Sending data request with date: " + date);
				ds = new DataSender(new DataRequest(date));
				Properties.setLastUpdate(System.currentTimeMillis()); //use date of last item in returned list!
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
				listOfInformation = ds.getList();
			}*/
			
	        //Test data
			listOfInformation.clear();
			ArrayList<String> sources = new ArrayList<String>();
			sources.add("this is a source");
			sources.add("this is another source");
			listOfInformation.add(new ImportantInformation("News1", new Date(54546), "this is inference", sources, "this is other"));
			listOfInformation.add(new ImportantInformation("News2", new Date(54547)));
			listOfInformation.add(new ImportantInformation("News1", new Date(234567781)));
			listOfInformation.add(new ImportantInformation("News2", new Date(234567791)));
			listOfInformation.add(new ImportantInformation("News3", new Date(234567801)));
			listOfInformation.add(new ImportantInformation("News1", new Date(456789678)));
	        listOfInformation.add(new ImportantInformation("News2", new Date(456789679)));
	        listOfInformation.add(new ImportantInformation("News3", new Date(458678968)));
	        listOfInformation.add(new ImportantInformation("News4", new Date(458678968)));
	        listOfInformation.add(new ImportantInformation("News5", new Date(458678968)));
	        listOfInformation.add(new ImportantInformation("News6", new Date(458678969)));
	        listener.onSuccess();
		}
		
	}

}