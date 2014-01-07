package ExtractTweets;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;
import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
public class TweetFilterbyLocandKeywords {
    private static Logger logger = Logger.getLogger(
    		TweetFilterbyLocandKeywords.class.getName());

    private static final String API_PROTOCOL = "https";
    private static final String API_DOMAIN = "stream.twitter.com";
    private static final String API_URL = "1.1/";
    private static final String FILTER_URL = API_URL + "statuses/filter.json";
    static String profile = "/home/sandeep/jars/langdetect-09-13-2011/profiles";

    public static void readFrom(HttpRequestBase requestMethod) throws IOException {

        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams();
        HttpResponse response = httpclient.execute(requestMethod);
        HttpEntity entity = response.getEntity();
        if (entity == null) throw new IOException("No entity");
        BufferedReader br = new BufferedReader(new InputStreamReader(
                entity.getContent(), "UTF-8"));
        try {
            initLangDetect(profile);
            while (true) {
                String line = br.readLine();
               
                if (line != null && !line.isEmpty()) {
                   
                    String outline="";
                    JSONObject tweet= new JSONObject(line);
                    if(detect(tweet.getString("text")).compareToIgnoreCase("en")==0)
                    {
                    	 System.out.println(line);
                         System.out.println(tweet.getString("id_str"));
                        // DataObject obj = gson.fromJson(br, DataObject.class);
                        System.out.println(tweet.get("retweeted"));
                         outline=outline+"tweet_id.."+tweet.getString("id_str")+" #$%^%$# ";
                         outline=outline+"text.."+tweet.getString("text")+" #$%^%$# ";
                         outline=outline+"retweeted.."+tweet.get("retweeted")+" #$%^%$# ";
                         outline=outline+"retweet_count.."+tweet.get("retweet_count")+" #$%^%$# ";
                         outline=outline+"created_at.."+tweet.getString("created_at")+" #$%^%$# ";
                         outline=outline+"inreplytostatus.."+tweet.get("in_reply_to_status_id")+" #$%^%$# ";
                        
                         JSONObject userinf= tweet.getJSONObject("user");
                         outline=outline+"user_id.."+userinf.getString("id_str")+" #$%^%$# ";
                         outline=outline+"user_name.."+userinf.getString("name")+" #$%^%$# ";
                         outline=outline+"user_location.."+userinf.getString("location")+" #$%^%$# ";
                         outline=outline+"user_created_at.."+userinf.getString("created_at")+" #$%^%$# ";
                         outline=outline+"user_url.."+userinf.get("url")+" #$%^%$# ";
                         outline=outline+"user_description.."+userinf.get("description")+" #$%^%$# ";
                         outline=outline+"followers_count.."+userinf.get("followers_count");
                         outline=outline+ " &&**&&\n";
                        System.out.println(outline);	
                    }
                   
                       
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HttpRequestBase getConnection()
            throws OAuthMessageSignerException,
            OAuthExpectationFailedException, OAuthCommunicationException,
            IllegalStateException, IOException {
        String base = FILTER_URL;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("track","Mobile"));
        //params.add(new BasicNameValuePair("locations","-99,20,-70,41"));


        HttpPost method = new HttpPost(API_PROTOCOL + "://" + API_DOMAIN + "/"
                + base);
        logger.info("" + method.getURI());

        UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(params,
                HTTP.UTF_8);
        method.setEntity(postEntity);

        String consumerKey = "0zDW3Uj4CtmQKzX1HKEjXA";
        String consumerSecret = "xuzKrFyeupmZOe1JW0V8bhgYNvz2TDRYnyjy5kCg";
        CommonsHttpOAuthConsumer consumer = new CommonsHttpOAuthConsumer(
                consumerKey, consumerSecret);

        String token = "841749481-q9soJTFSglt4d73oEtjePjVFw0aTlP7ISYvYgzmH";
        String tokenSecret = "5k2M2eaeIJ4Legeu0m8udDGCgJ4MPNG574nmR5XST8";
        consumer.setTokenWithSecret(token, tokenSecret);
        consumer.sign(method);

        return method;
    }

    public static void main(String[] args) throws Exception {
        readFrom(getConnection());
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


}

