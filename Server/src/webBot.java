import java.net.URL;
import java.io.*;
import java.net.*;

public class webBot {
	
	public static void main(String[] args) {
		
		
		String bbcUrl = "http://feeds.bbci.co.uk/news/rss.xml";
		String bbcSourceDescription = "BBC News RSS Feed";
		String guardianUrl = "http://feeds.guardian.co.uk/theguardian/world/rss";
		String guardianSourceDescription = "The Guardian RSS Feed";
		
		
		rssReader reader = new rssReader(bbcUrl, bbcSourceDescription);
		reader.start();
		
		rssReader guardianReader = new rssReader(guardianUrl, guardianSourceDescription);
		guardianReader.start();
		
		
		googleTrendsReader google = new googleTrendsReader();
		google.start();
		
		twitterReader twitter = new twitterReader();
		twitter.start();
		
		tweetProcessor tweets = new tweetProcessor();
		tweets.start();
		
	}
	

	
}
	

