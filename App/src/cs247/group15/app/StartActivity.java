package cs247.group15.app;

import java.util.ArrayList;
import java.util.Date;

import cs247.group15.data.Constants;
import cs247.group15.data.ImportantInformation;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class StartActivity extends ListActivity {
	
	//MAIN SCREEN
	
	ArrayList<Object> listItems;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen1);
        
        Log.d(Constants.information, "Starting the service.");
        startService(new Intent(StartActivity.this, CS247Service.class));
        
        //This stores the list of ImportantInformations
        listItems = new ArrayList<Object>();
        //Test data
        listItems.add(new Date(54546));
        listItems.add(new ImportantInformation("News1"));
        listItems.add(new ImportantInformation("News2"));
        listItems.add(new Date(23456778));
        listItems.add(new ImportantInformation("News1"));
        listItems.add(new ImportantInformation("News2"));
        listItems.add(new ImportantInformation("News3"));
        listItems.add(new Date(456789678));
        listItems.add(new ImportantInformation("News1"));
        listItems.add(new ImportantInformation("News2"));
        listItems.add(new ImportantInformation("News3"));
        listItems.add(new ImportantInformation("News4"));
        listItems.add(new ImportantInformation("News5"));
        listItems.add(new ImportantInformation("News6"));
        
        //Used to create the row for the list
        ArrayAdapter<Object> adapter = 
        		new ArrayAdapter<Object>(this, R.layout.row_element, R.id.titletext, listItems)
        		{
        			//Used to modify the view of each row depending on which ImportantInformation it refers to
        			@Override
        			public View getView(int position, View convertView, ViewGroup parent)
        			{
        				if(listItems.get(position) instanceof ImportantInformation)
        				{
        					if(convertView.findViewById(R.id.titletext)==null)
        					{
        						convertView = ((LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.row_element, null);
        					}
	        				View v = super.getView(position, convertView, parent);
	        				TextView tv = ((TextView)v.findViewById(R.id.titletext));
	        				tv.setText(tv.getText()+"!");
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