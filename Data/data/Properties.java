package cs247.group15.data;

public class Properties {

	//This is how often the service should check for updates
	private static long updateFrequency = 900000L;
	//This is time of the last update
	private static long lastUpdate = 0L;
	//This is the level of importance that the user will be alerted about (will be 0 if alerts are off)
	private static int importanceLevel = 20;
	//This is the level of importance that the user wants to be alerted about
	private static int requiredImportanceLevel = 20;

	public static void setUpdateFrequency(long updateFrequency) {
		Properties.updateFrequency = updateFrequency;
	}
	public static long getUpdateFrequency()
	{
		return updateFrequency;
	}
	public static long getLastUpdate() {
		return lastUpdate;
	}
	public static void setLastUpdate(long lastUpdate) {
		Properties.lastUpdate = lastUpdate;
	}
	public static int getImportanceLevel() {
		return importanceLevel;
	}
	public static void setImportanceLevel(int importanceLevel) {
		Properties.importanceLevel = importanceLevel;
	}
	public static int getRequiredImportanceLevel() {
		return requiredImportanceLevel;
	}
	public static void setRequiredImportanceLevel(int requiredImportanceLevel) {
		Properties.requiredImportanceLevel = requiredImportanceLevel;
	}
	
}
