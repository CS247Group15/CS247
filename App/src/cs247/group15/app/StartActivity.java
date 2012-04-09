package cs247.group15.app;

import cs247.group15.data.Constants;
import cs247.group15.data.ImportantInformation;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartActivity extends Activity implements OnClickListener {
	
	//MAIN SCREEN
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen1);
        ((Button)findViewById(R.id.button1)).setOnClickListener(this);
    }
    
    public void onClick(View v)
    {
    	switch(v.getId())
    	{
    		case R.id.button1 : 
    		{
    			Intent intent = new Intent(this, DrillDownInformation.class);
    			intent.putExtra(Constants.ImportantInformationTag, new ImportantInformation("test"));
    			startActivity(intent);
    			break;
    		}
    	}
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
    	//TODO: implement this
    	return super.onOptionsItemSelected(item);
    }
    
}