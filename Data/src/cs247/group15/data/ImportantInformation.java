package cs247.group15.data;

import java.io.Serializable;

/*
 * Contains the information that will be sent from the server to the app, when
 * important information is discovered by the data mining.
 * Author: KB
 */

public class ImportantInformation implements Serializable {

	String heading;
	
	public ImportantInformation(String heading)
	{
		this.heading = heading;
	}
}
