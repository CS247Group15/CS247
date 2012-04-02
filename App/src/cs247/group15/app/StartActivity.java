package cs247.group15.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class StartActivity extends Activity {
	
	//MAIN SCREEN
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
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