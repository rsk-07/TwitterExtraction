package ExtractTweets;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
 
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;


public class TweetExtraction{
static String profile = "/home/sandeep/jars/langdetect-09-13-2011/profiles";
/***
* Tweets list will have new tweet at multiple of 4th index ( 0-> id, 1-> user, 2->time, 3-> text) 
*/
public static void main(String args[]) throws Exception{
run();
}
public static void run() throws Exception {
String userid ="";
String password = "";
String oAuthAccessToken = "841749481-q9soJTFSglt4d73oEtjePjVFw0aTlP7ISYvYgzmH";
String oAuthAccessTokenSecret= "5k2M2eaeIJ4Legeu0m8udDGCgJ4MPNG574nmR5XST8";
String oAuthConsumerKey= "0zDW3Uj4CtmQKzX1HKEjXA";
String oAuthConsumerSecret= "xuzKrFyeupmZOe1JW0V8bhgYNvz2TDRYnyjy5kCg";
try {
// Initialising Language detection
initLangDetect(profile);
// For Authentication to twitter-stream
ConfigurationBuilder builder = new ConfigurationBuilder();
builder = builder.setOAuthAccessToken(oAuthAccessToken);
builder = builder.setOAuthAccessTokenSecret(oAuthAccessTokenSecret);
builder = builder.setOAuthConsumerKey(oAuthConsumerKey).setOAuthConsumerSecret(oAuthConsumerSecret);
Configuration conf = builder.build();
// Connecting to twitter-stream
TwitterStream twitterStream = new TwitterStreamFactory(conf).getInstance();
StatusListener listener = getListener();
twitterStream.addListener(listener);
       twitterStream.sample();
 
} catch (Exception e) {
// TODO Auto-generated catch block
e.printStackTrace();
}
  
  
}
private static StatusListener getListener() throws IOException {
// TODO Auto-generated method stub
StatusListener listener = new StatusListener() {
           
public void onStatus(Status status) {
            String text = status.getText();
            String[] temp;
            String date;  
                   
            try {
             
String lang = detect(text);
if(lang.compareToIgnoreCase("en") == 0){
String timeCreatedAt = status.getCreatedAt().toString();
temp = timeCreatedAt.split(" ");
date = temp[1] +"-"+ temp[2] + "-" + temp[5];
String time = temp[3].replace(":", "");
System.out.println("user--> " + status.getUser().getScreenName() + " ---- " + status.getText() +" time-----> " + 
         status.getCreatedAt().toString() + " location---> " + status.getPlace() + "geolocation----->" + status.getGeoLocation());
String str = status.getUser().getScreenName() + "\t" + status.getText() +"\t" + status.getCreatedAt().toString() + "\t" + status.getPlace() + "\t" + status.getGeoLocation() +"\n" ;

//addToFile(str);
}
} catch (LangDetectException e) {
return;
// TODO Auto-generated catch block
//e.printStackTrace();
}catch(Exception e){
e.printStackTrace();
}
               
            }
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                //System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            return;
            }
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
            return;
                //System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }
            public void onScrubGeo(long userId, long upToStatusId) {
            return;
                //System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
            public void onStallWarning(StallWarning warning) {
                
            }
        };
return listener;
}
static void addToFile(String str) throws IOException{
FileWriter fstream = new FileWriter("output.txt",true);
  BufferedWriter out = new BufferedWriter(fstream);
  out.append(str);
  out.close();
}
//** Loading Profiles of languages given 
static void initLangDetect(String profileDirectory) throws LangDetectException {
DetectorFactory.loadProfile(profileDirectory);
}
//** Language Detection 
static String detect(String text) throws LangDetectException {
Detector detector = DetectorFactory.create();
detector.append(text);
return detector.detect();
}
//** Loading Profiles of languages given 
}


