package cs247.group15.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import cs247.group15.data.Constants;
import cs247.group15.data.ImportantInformation;

public class DrillDownInformation extends Activity {
	
	ImportantInformation information;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        information = (ImportantInformation) getIntent().getSerializableExtra(Constants.ImportantInformationTag);
        
        updateScreen();
	}

	private void updateScreen()
	{
		if(information!=null)
		{
			((TextView)findViewById(R.id.heading_text)).setText(information.toString());
		}
	}
}