package cs247.group15.app;

import java.util.ArrayList;
import java.util.Date;

import cs247.group15.app.CS247Service.ServiceBinder;
import cs247.group15.data.Constants;
import cs247.group15.data.ImportantInformation;
import cs247.group15.data.ListClass;
import cs247.group15.data.Properties;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
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
	TextView serverStatus;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen1);
        
        if(Constants.serverURL.equals(""))
        {
        	showDialog();
        }
        else
        {
        	continueCreatingScreen();
        }
    }
    
    private void showDialog()
    {
    	final Dialog dialog = new Dialog(StartActivity.this);
        dialog.setContentView(R.layout.server_set_up_dialog);
        dialog.findViewById(R.id.serverSelectButton).setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				String serverAddress = ((TextView)dialog.findViewById(R.id.serverentertext)).getText().toString().trim();
				if(!serverAddress.equals(""))
				{
					Constants.serverURL = "http://" + serverAddress + ":8085/JavaServlet/Servlet";
					continueCreatingScreen();
					dialog.dismiss();
				}
			}
		});
        dialog.setTitle("Server Set-Up");
        dialog.setCancelable(false);
        dialog.show();
    }
    
    public void continueCreatingScreen()
    {
        Log.d(Constants.information, "Starting the service.");
        startService(new Intent(StartActivity.this, CS247Service.class));
        bindService(new Intent(StartActivity.this, CS247Service.class), serviceConnection, 0);
        
        //
        serverStatus = (TextView)findViewById(R.id.serverstatus);
        
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
        					if(convertView==null||convertView.findViewById(R.id.titletext)==null)
        					{
        						convertView = ((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.row_element, null);
        					}
	        				View v = super.getView(position, convertView, parent);
	        				TextView tv = ((TextView)v.findViewById(R.id.titletext));
	        				if(info.getImportance()>Properties.getRequiredImportanceLevel())
	        				{
	        					tv.setTypeface(null, Typeface.BOLD);
	        				}
	        				else
	        				{
	        					tv.setTypeface(null, Typeface.NORMAL);
	        				}
	        				ImageView iv = ((ImageView)v.findViewById(R.id.gradientimage));
	        				switch(info.getSentimence())
	        				{
	        				case -50 : iv.setImageResource(R.drawable.slider0001); break;
	        				case -49 : iv.setImageResource(R.drawable.slider0002); break;
	        				case -48 : iv.setImageResource(R.drawable.slider0003); break;
	        				case -47 : iv.setImageResource(R.drawable.slider0004); break;
	        				case -46 : iv.setImageResource(R.drawable.slider0005); break;
	        				case -45 : iv.setImageResource(R.drawable.slider0006); break;
	        				case -44 : iv.setImageResource(R.drawable.slider0007); break;
	        				case -43 : iv.setImageResource(R.drawable.slider0008); break;
	        				case -42 : iv.setImageResource(R.drawable.slider0009); break;
	        				case -41 : iv.setImageResource(R.drawable.slider0010); break;
	        				case -40 : iv.setImageResource(R.drawable.slider0011); break;
	        				case -39 : iv.setImageResource(R.drawable.slider0012); break;
	        				case -38 : iv.setImageResource(R.drawable.slider0013); break;
	        				case -37 : iv.setImageResource(R.drawable.slider0014); break;
	        				case -36 : iv.setImageResource(R.drawable.slider0015); break;
	        				case -35 : iv.setImageResource(R.drawable.slider0016); break;
	        				case -34 : iv.setImageResource(R.drawable.slider0017); break;
	        				case -33 : iv.setImageResource(R.drawable.slider0018); break;
	        				case -32 : iv.setImageResource(R.drawable.slider0019); break;
	        				case -31 : iv.setImageResource(R.drawable.slider0020); break;
	        				case -30 : iv.setImageResource(R.drawable.slider0021); break;
	        				case -29 : iv.setImageResource(R.drawable.slider0022); break;
	        				case -28 : iv.setImageResource(R.drawable.slider0023); break;
	        				case -27 : iv.setImageResource(R.drawable.slider0024); break;
	        				case -26 : iv.setImageResource(R.drawable.slider0025); break;
	        				case -25 : iv.setImageResource(R.drawable.slider0026); break;
	        				case -24 : iv.setImageResource(R.drawable.slider0027); break;
	        				case -23 : iv.setImageResource(R.drawable.slider0028); break;
	        				case -22 : iv.setImageResource(R.drawable.slider0029); break;
	        				case -21 : iv.setImageResource(R.drawable.slider0030); break;
	        				case -20 : iv.setImageResource(R.drawable.slider0031); break;
	        				case -19 : iv.setImageResource(R.drawable.slider0032); break;
	        				case -18 : iv.setImageResource(R.drawable.slider0033); break;
	        				case -17 : iv.setImageResource(R.drawable.slider0034); break;
	        				case -16 : iv.setImageResource(R.drawable.slider0035); break;
	        				case -15 : iv.setImageResource(R.drawable.slider0036); break;
	        				case -14 : iv.setImageResource(R.drawable.slider0037); break;
	        				case -13 : iv.setImageResource(R.drawable.slider0038); break;
	        				case -12 : iv.setImageResource(R.drawable.slider0039); break;
	        				case -11 : iv.setImageResource(R.drawable.slider0040); break;
	        				case -10 : iv.setImageResource(R.drawable.slider0041); break;
	        				case -9 : iv.setImageResource(R.drawable.slider0042); break;
	        				case -8 : iv.setImageResource(R.drawable.slider0043); break;
	        				case -7 : iv.setImageResource(R.drawable.slider0044); break;
	        				case -6 : iv.setImageResource(R.drawable.slider0045); break;
	        				case -5 : iv.setImageResource(R.drawable.slider0046); break;
	        				case -4 : iv.setImageResource(R.drawable.slider0047); break;
	        				case -3 : iv.setImageResource(R.drawable.slider0048); break;
	        				case -2 : iv.setImageResource(R.drawable.slider0049); break;
	        				case -1 : iv.setImageResource(R.drawable.slider0050); break;
	        				case 0 : iv.setImageResource(R.drawable.slider0051); break;
	        				case 1 : iv.setImageResource(R.drawable.slider0052); break;
	        				case 2 : iv.setImageResource(R.drawable.slider0053); break;
	        				case 3 : iv.setImageResource(R.drawable.slider0054); break;
	        				case 4 : iv.setImageResource(R.drawable.slider0055); break;
	        				case 5 : iv.setImageResource(R.drawable.slider0056); break;
	        				case 6 : iv.setImageResource(R.drawable.slider0057); break;
	        				case 7 : iv.setImageResource(R.drawable.slider0058); break;
	        				case 8 : iv.setImageResource(R.drawable.slider0059); break;
	        				case 9 : iv.setImageResource(R.drawable.slider0060); break;
	        				case 10 : iv.setImageResource(R.drawable.slider0061); break;
	        				case 11 : iv.setImageResource(R.drawable.slider0062); break;
	        				case 12 : iv.setImageResource(R.drawable.slider0063); break;
	        				case 13 : iv.setImageResource(R.drawable.slider0064); break;
	        				case 14 : iv.setImageResource(R.drawable.slider0065); break;
	        				case 15 : iv.setImageResource(R.drawable.slider0066); break;
	        				case 16 : iv.setImageResource(R.drawable.slider0067); break;
	        				case 17 : iv.setImageResource(R.drawable.slider0068); break;
	        				case 18 : iv.setImageResource(R.drawable.slider0069); break;
	        				case 19 : iv.setImageResource(R.drawable.slider0070); break;
	        				case 20 : iv.setImageResource(R.drawable.slider0071); break;
	        				case 21 : iv.setImageResource(R.drawable.slider0072); break;
	        				case 22 : iv.setImageResource(R.drawable.slider0073); break;
	        				case 23 : iv.setImageResource(R.drawable.slider0074); break;
	        				case 24 : iv.setImageResource(R.drawable.slider0075); break;
	        				case 25 : iv.setImageResource(R.drawable.slider0076); break;
	        				case 26 : iv.setImageResource(R.drawable.slider0077); break;
	        				case 27 : iv.setImageResource(R.drawable.slider0078); break;
	        				case 28 : iv.setImageResource(R.drawable.slider0079); break;
	        				case 29 : iv.setImageResource(R.drawable.slider0080); break;
	        				case 30 : iv.setImageResource(R.drawable.slider0081); break;
	        				case 31 : iv.setImageResource(R.drawable.slider0082); break;
	        				case 32 : iv.setImageResource(R.drawable.slider0083); break;
	        				case 33 : iv.setImageResource(R.drawable.slider0084); break;
	        				case 34 : iv.setImageResource(R.drawable.slider0085); break;
	        				case 35 : iv.setImageResource(R.drawable.slider0086); break;
	        				case 36 : iv.setImageResource(R.drawable.slider0087); break;
	        				case 37 : iv.setImageResource(R.drawable.slider0088); break;
	        				case 38 : iv.setImageResource(R.drawable.slider0089); break;
	        				case 39 : iv.setImageResource(R.drawable.slider0090); break;
	        				case 40 : iv.setImageResource(R.drawable.slider0091); break;
	        				case 41 : iv.setImageResource(R.drawable.slider0092); break;
	        				case 42 : iv.setImageResource(R.drawable.slider0093); break;
	        				case 43 : iv.setImageResource(R.drawable.slider0094); break;
	        				case 44 : iv.setImageResource(R.drawable.slider0095); break;
	        				case 45 : iv.setImageResource(R.drawable.slider0096); break;
	        				case 46 : iv.setImageResource(R.drawable.slider0097); break;
	        				case 47 : iv.setImageResource(R.drawable.slider0098); break;
	        				case 48 : iv.setImageResource(R.drawable.slider0099); break;
	        				case 49 : iv.setImageResource(R.drawable.slider0100); break;
	        				case 50 : iv.setImageResource(R.drawable.slider0100); break;
	        				}
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
			try {service.sendRequest(screenUpdater);}
			catch(IllegalArgumentException e)
			{
				Log.d(Constants.error, e.getMessage());
				showDialog();
			}
		}
	};
    
    @Override
    public void onDestroy()
    {
    	super.onDestroy();
    	if(service!=null){unbindService(serviceConnection);}
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
    		serverStatus.setText("Server Online");
    		serverStatus.setBackgroundColor(Color.GREEN);
    		if(Properties.getImportanceLevel()>0) {((ToggleButton)findViewById(R.id.toggleButton)).setChecked(true);}
		}
		
		public void onFail() {
			runOnUiThread(new Runnable() {
				
				public void run() {
					Toast.makeText(getApplicationContext(), "Error occurred when requesting update", Toast.LENGTH_LONG).show();
					serverStatus.setText("Server Offline");
					serverStatus.setBackgroundColor(Color.RED);
					((ToggleButton)findViewById(R.id.toggleButton)).setChecked(false);
				}
			});
		}
	};
    
}