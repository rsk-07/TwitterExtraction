package ExtractTweets;
import twitter4j.*;

import java.util.List;

public class SearchTweets {

	    public static void main(String[] args) {
	        Twitter twitter = new TwitterFactory().getInstance();
	        Query query;
	        Long id=0L;
	        try {
	            query = new Query("Mobile Recruitment");
	            query.setLang("en");
	            GeoLocation g=new GeoLocation(39.8,98.5);
	            query.setGeoCode(g,408,"mi");
	            QueryResult result;
	            do {
	                
	            	result = twitter.search(query);
	                List<Status> tweets = result.getTweets();
	                for (Status tweet : tweets) {
	                	id=tweet.getId();
	                    System.out.println("@" + tweet.getUser().getScreenName() + " - " + tweet.getText() + tweet.getPlace());
	                }
	                query.setSinceId(id);
	            } while ((query = result.nextQuery()) != null);
	            System.exit(0);
	        } catch (TwitterException te) {
	            te.printStackTrace();
	            System.out.println("Failed to search tweets: " + te.getMessage());
	            System.exit(-1);
	        }
	    }
	}

