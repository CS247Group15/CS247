package cs247.group15.app;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jdom.JDOMException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cs247.group15.data.Constants;
import cs247.group15.data.DataRequest;
import cs247.group15.data.ImportantInformation;
import cs247.group15.data.ListClass;
import cs247.group15.data.PrintableDate;
import cs247.group15.data.Properties;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Binder;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.text.format.DateFormat;
import android.util.Log;

public class CS247Service extends IntentService {

	public CS247Service() {
		super("CS247Service");
	}

	ServiceBinder binder;

	@Override
	public int onStartCommand (Intent intent, int flags, int startId)
	{
		Log.d(Constants.information, "The service has been started.");
		binder = new ServiceBinder();
		binder.startAutoUpdater();
		return START_STICKY;
	}
	
	@Override
	protected void onHandleIntent(Intent intent)
	{
	}
	
	@Override
	public Binder onBind(Intent intent) {
		super.onBind(intent);
		return binder;
	}
	
	class ServiceBinder extends Binder
	{
		private List<ImportantInformation> listOfInformation = new ArrayList<ImportantInformation>();
		private Timer timer;
		
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
		
		public void sendRequest(OnRequestComplete listener)
		{
			Log.d(Constants.information, "Sending request to: " + Constants.serverURL);
			HttpClient httpclient = new DefaultHttpClient();
		    HttpResponse response;
			try {
				response = httpclient.execute(new HttpGet(Constants.serverURL));
				StatusLine statusLine = response.getStatusLine();
			    if(statusLine.getStatusCode() == HttpStatus.SC_OK){
			        ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
			        out.close();
			        String responseString = out.toString();
			        Log.d("TEST", responseString);
			        
			        List<ImportantInformation> newInfo = new ArrayList<ImportantInformation>();
			        JSONObject json = new JSONObject(responseString);
			        JSONArray newsArray = json.getJSONArray("news");
			        for(int i=0; i<newsArray.length(); i++)
			        {
			        	JSONObject item = newsArray.getJSONObject(i);
			        	String heading = item.getString("Title");
			        	String description = item.getString("Description");
			        	String source = item.getString("Source");
			        	String date = item.getString("Date");
			        	int year = Integer.parseInt(date.substring(0, 4));
			        	int month = Integer.parseInt(date.substring(5,7));
			        	int day = Integer.parseInt(date.substring(8));
			        	Date dateDate = new Date(year, month, day);
			        	String sentiment = item.getString("Sentiment");
			        	int sentimentInt = Integer.parseInt(sentiment);
			        	String importance = item.getString("Importance");
			        	int importanceInt = Integer.parseInt(importance);
			        	String nouns = item.getString("Important Words");
			        	
			        	ImportantInformation info = new ImportantInformation(heading, importanceInt, sentimentInt, dateDate, description, source, nouns);
			        	if(!listOfInformation.contains(info))
			        	{
			        		newInfo.add(info);
			        		listOfInformation.add(info);
			        	}
			        }
			        
			        //sort by date
			        Collections.sort(listOfInformation);
			        
			        for(int i = 20; i<listOfInformation.size(); i++)
			        {
			        	listOfInformation.remove(i);
			        }
			        
			        listener.onSuccess();
			        checkNotifications(newInfo); //put the list of new infos into here
			    } 
			    else
			    {
			        //Closes the connection.
			        response.getEntity().getContent().close();
			        throw new IOException(statusLine.getReasonPhrase());
			    }
			} catch (ClientProtocolException e) {
				listener.onFail();
				e.printStackTrace();
			} catch (IOException e) {
				listener.onFail();
				e.printStackTrace();
			} catch (JSONException e) {
				listener.onFail();
				e.printStackTrace();
			}
		}
		
		private void startAutoUpdater()
		{
			timer = new Timer();
			timer.schedule(new TimerTask(){

				@Override
				public void run() {
					sendRequest(new OnRequestComplete() {
						
						public void onSuccess() {
							//Set last updated time to the time associated with the last ImportantInformation received
							if(listOfInformation.size()>0)
							{
								Properties.setLastUpdate(
									listOfInformation.get(listOfInformation.size()-1).getDate().getTime()
									);
							}
						}
						
						public void onFail() {}
					});
				}}, new Date(System.currentTimeMillis()),
			Properties.getUpdateFrequency());
		}
		
		private void checkNotifications(List<ImportantInformation> newList)
		{
			NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
			for(int i = 0; i<newList.size(); i++)
			{
				ImportantInformation info = newList.get(i);
				if(info.getImportance()>=Properties.getRequiredImportanceLevel())
				{
					Notification not = new Notification(R.drawable.ic_launcher, info.toString(), System.currentTimeMillis());
					
					not.defaults |= Notification.DEFAULT_VIBRATE;
					not.flags |= Notification.FLAG_AUTO_CANCEL;
					
					Intent notificationIntent = new Intent(getApplicationContext(), DrillDownInformation.class);
					notificationIntent.putExtra(Constants.ImportantInformationTag, info);
					
					PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);
					not.setLatestEventInfo(getApplicationContext(), "Important", info.toString(), contentIntent);
					
					notificationManager.notify(i, not);
				}
			}
		}
	}

}