package cs247.group15.app;

import java.util.ArrayList;
import java.util.Date;

import cs247.group15.app.CS247Service.ServiceBinder;
import cs247.group15.data.Constants;
import cs247.group15.data.ImportantInformation;
import cs247.group15.data.ListClass;
import cs247.group15.data.Properties;
import android.app.Activity;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class StartActivity extends ListActivity {
	
	//MAIN SCREEN
	ArrayList<ListClass> listItems;
	ArrayAdapter<ListClass> adapter;
	ServiceBinder service;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen1);
        
        Log.d(Constants.information, "Starting the service.");
        startService(new Intent(StartActivity.this, CS247Service.class));
        bindService(new Intent(StartActivity.this, CS247Service.class), serviceConnection, 0);
        
        //This stores the list of ImportantInformations
        listItems = new ArrayList<ListClass>();

        
        //Used to create the row for the list
        adapter = 
        		new ArrayAdapter<ListClass>(this, R.layout.row_element, R.id.titletext, listItems)
        		{
        			//Used to modify the view of each row depending on which ImportantInformation it refers to
        			@Override
        			public View getView(int position, View convertView, ViewGroup parent)
        			{
        				if(listItems.get(position) instanceof ImportantInformation)
        				{
        					ImportantInformation info = (ImportantInformation)listItems.get(position);
        					if(convertView.findViewById(R.id.titletext)==null)
        					{
        						convertView = ((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.row_element, null);
        					}
	        				View v = super.getView(position, convertView, parent);
	        				TextView tv = ((TextView)v.findViewById(R.id.titletext));
	        				if(info.getImportance()>Properties.getRequiredImportanceLevel())
	        				{
	        					tv.setTypeface(null, Typeface.BOLD);
	        				}
	        				ImageView iv = ((ImageView)v.findViewById(R.id.gradientimage));
	        				return v;
        				}
        				else
        				{
        					if(convertView==null||convertView.findViewById(R.id.date_text)==null)
        					{
        						convertView = ((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.date_row, null);
        					}
        					TextView dateText = (TextView)convertView.findViewById(R.id.date_text);
    						dateText.setText(((Date)listItems.get(position)).toString());
    						return convertView;
        				}
        			}
        		};
        getListView().setAdapter(adapter);
        
        //Updates on/off button
        ToggleButton tg = (ToggleButton)findViewById(R.id.toggleButton);
        tg.setChecked(Properties.getImportanceLevel()!=0);
        tg.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				ToggleButton tg = (ToggleButton)v;
				if(!tg.isChecked()) //button if isn't checked, remove updates
				{
					Properties.setImportanceLevel(0);
				}
				else
				{
					Properties.setImportanceLevel(Properties.getRequiredImportanceLevel());
				}
				Log.d("Properties", "Current importance Level: " + Properties.getImportanceLevel());
			}
		});
    }
    
    private ServiceConnection serviceConnection = new ServiceConnection() {
		
		public void onServiceDisconnected(ComponentName name) {}
		
		public void onServiceConnected(ComponentName name, IBinder iService) {
			service = (ServiceBinder) iService;
			service.sendRequest(screenUpdater);
		}
	};
    
    @Override
    public void onDestroy()
    {
    	super.onDestroy();
    	unbindService(serviceConnection);
    }
    
    //When an element in the list is clicked, go to the DrillDownInformation screen & pass it which ImportantInformation to populate the list with
    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
    	if(listItems.get(position) instanceof ImportantInformation)
    	{
	    	Intent intent = new Intent(this, DrillDownInformation.class);
			intent.putExtra(Constants.ImportantInformationTag, (ImportantInformation)listItems.get(position));
			startActivity(intent);
    	}
    }
    
    private void updateScreen(final ArrayList<ListClass> list)
    {
    	runOnUiThread(new Runnable(){

			public void run() {
				listItems.clear();
				for(ListClass item : list)
				{
					listItems.add(item);
				}
				if(adapter!=null)
				{
					adapter.notifyDataSetChanged();
				}
			}});
    }
    
    //MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	menu.add("Update");
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override 
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	if(item.getTitle().equals("Update"))
    	{
	    	if(service!=null)
	    	{
	    		service.sendRequest(screenUpdater);
	    	}
    	}
    	return super.onOptionsItemSelected(item);
    }
    
    OnRequestComplete screenUpdater = new OnRequestComplete() {
		
		public void onSuccess() {
    		updateScreen(service.getDatedImportantInformationList());
		}
		
		public void onFail() {
			runOnUiThread(new Runnable() {
				
				public void run() {
					Toast.makeText(getApplicationContext(), "Error occurred when requesting update", Toast.LENGTH_LONG).show();
				}
			});
		}
	};
    
}