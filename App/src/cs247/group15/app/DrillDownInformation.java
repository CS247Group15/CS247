package cs247.group15.app;

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import cs247.group15.app.CS247Service.ServiceBinder;
import cs247.group15.data.Constants;
import cs247.group15.data.ImportantInformation;

public class DrillDownInformation extends Activity {
	
	ImportantInformation information;
	ServiceBinder service;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        bindService(new Intent(DrillDownInformation.this, CS247Service.class), serviceConnection, 0);
        
        information = (ImportantInformation) getIntent().getSerializableExtra(Constants.ImportantInformationTag);
        
        updateScreen();
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		unbindService(serviceConnection);
	}

	private void updateScreen()
	{
		if(information!=null)
		{
			((TextView)findViewById(R.id.heading_text)).setText(information.toString());
			((TextView)findViewById(R.id.inferenceBox)).setText(information.getDescription());
			((TextView)findViewById(R.id.sourcesBox)).setText("Source: " + information.getSource());
			if(information.getNouns().trim().equals("")&&information.getNouns().equals("null"))
			{
				((TextView)findViewById(R.id.otherInfoBox)).setText("Important words: " + information.getNouns());
			}
			else
			{
				((TextView)findViewById(R.id.otherInfoBox)).setText("No important words found.");
			}
		}
	}
	
	ServiceConnection serviceConnection = new ServiceConnection() {
		
		public void onServiceDisconnected(ComponentName name) {}
		
		public void onServiceConnected(ComponentName name, IBinder iService) {
			service = (ServiceBinder)iService;
		}
	};
}