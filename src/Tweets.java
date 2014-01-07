import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import java.util.List;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Tweets {

	    public static void main(String[] args) throws IOException {
	    	
        	BufferedWriter out = new BufferedWriter(new FileWriter(new File("moredata.txt")));

	    	
	    	System.setProperty("http.proxyHost", "proxy.iiit.ac.in");
	    	System.setProperty("http.proxyPort", "8080");
	    	
	    	ConfigurationBuilder cb = new ConfigurationBuilder();
	    	cb.setDebugEnabled(true)
	    	  .setOAuthConsumerKey("6YV0iHjRz0fzzvU1HS2yzA")
	    	  .setOAuthConsumerSecret("oXz48oXtWTGIgy97mMt2rdS1BDCd4AGt0WAJ08JcDA")
	    	  .setOAuthAccessToken("596643894-Nfua3e6G9jqqzuWNIvjOw0ZUcBYQDEst5ZwLOYUd")
	    	  .setOAuthAccessTokenSecret("XuPi3sUWHqomXatc0ESxcuCrWdLBKyXAijHa4gSqIY6Kw");
	    	TwitterFactory tf = new TwitterFactory(cb.build());
	    	Twitter twitter = tf.getInstance();
	    	
	    	//From here the different file works 
//	        System.out.println("Yes");
	    	Query query;
	        Long id=0L;
	        try {
	            query = new Query("sachin");
	            QueryResult result;
	            do {
//	            	System.out.println("Here too");
	            	result = twitter.search(query);
	                List<Status> tweets = result.getTweets();
	                for (Status tweet : tweets) {
//	                	System.out.println("hmm");
	                	id=tweet.getId();
	                	
	                	// Writing in file
	                	
	                    out.write("@" + tweet.getUser().getScreenName() + " - " + tweet.getText() + tweet.getPlace() + "\n");
	                }
	                query.setSinceId(id);
	            } while ((query = result.nextQuery()) != null);
	            out.flush();
        		out.close();
	            System.exit(0);
	        } catch (TwitterException te) {
	            te.printStackTrace();
	            System.out.println("Failed to search tweets: " + te.getMessage());
	            System.exit(-1);
	        }
	    }
}