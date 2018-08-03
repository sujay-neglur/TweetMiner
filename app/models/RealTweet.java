package models;

import org.w3c.dom.Document;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class RealTweet implements TweetMiner {
    private TwitterFactory tf;
    Twitter twitter;
    private static final String CONSUMER_KEY = "Gkth7i2tEjADOeMh1eg9GSM6c";
    private static final String CONSUMER_SECRET = "ZjkVWqpuLmVA6lDd70KtY4115aONH8SyfHrlOYbfffJp85hj4a";
    private static final String ACCESS_TOKEN = "1021898405029847040-JNPu1IXByi70qAc9CvR0HJyVw4f2qh";
    private static final String ACCESS_TOKEN_SECRET = "s2spDv48YW2q5gEu0bGNzycMs5s09KASnTWzbwCShtBlU";
    private CompletableFuture<List<Details>> listCompletableFuture;
    List<Status> tweetStatusObjects;
    HashMap<String,CompletableFuture<HashMap<String,Integer>>> dataStatMap= new LinkedHashMap<>();

    public void setDataStatMap(String key, CompletableFuture<HashMap<String, Integer>> value) {
        dataStatMap.put(key,value);
    }

    public CompletableFuture<HashMap<String, Integer>> getDataStatMap(String key){
        return dataStatMap.get(key);
    }

    public RealTweet() {
        ConfigurationBuilder cf = new ConfigurationBuilder();
        cf.setDebugEnabled(true).setOAuthConsumerKey(CONSUMER_KEY).setOAuthConsumerSecret(CONSUMER_SECRET)
                .setOAuthAccessToken(ACCESS_TOKEN).setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET)
                .setTweetModeExtended(true);
        tf = new TwitterFactory(cf.build());
    }

    public Twitter getInstance() {
        this.twitter = tf.getInstance();
        return twitter;
    }

    /**
     * This method uses the google api to find the latitude and longitude
     * of the given location
     *
     * @author Nishant
     */

    public CompletableFuture<String[]> getLatLongPositions(String address) {
        return CompletableFuture.supplyAsync(() -> {
            String pos[] = new String[2];
            try {
                int responseCode = 0;
                String api = "http://maps.googleapis.com/maps/api/geocode/xml?address=" + URLEncoder.encode(address, "UTF-8") + "&sensor=true";
                URL url = new URL(api);
                HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.connect();
                responseCode = httpConnection.getResponseCode();
                if (responseCode == 200) {
                    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    ;
                    Document document = builder.parse(httpConnection.getInputStream());
                    XPathFactory xPathfactory = XPathFactory.newInstance();
                    XPath xpath = xPathfactory.newXPath();
                    XPathExpression expr = xpath.compile("/GeocodeResponse/status");
                    String status = (String) expr.evaluate(document, XPathConstants.STRING);
                    if (status.equals("OK")) {
                        expr = xpath.compile("//geometry/location/lat");
                        String latitude = (String) expr.evaluate(document, XPathConstants.STRING);
                        expr = xpath.compile("//geometry/location/lng");
                        String longitude = (String) expr.evaluate(document, XPathConstants.STRING);
                        pos[0]=latitude;
                        pos[1]=longitude;
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return pos;
        });
    }

    /**
     * This method takes as input the screen name of the user and gets all the data for user profile
     * @author Pallavi
     */

    public CompletableFuture<List<Status>> getUserTimeline(String screenName){
        CompletableFuture<List<Status>> userTimeline= null;

        try {
            userTimeline=CompletableFuture.supplyAsync(() -> {
                List<Status> temp=null;
                try{
                    temp=twitter.getUserTimeline(screenName).stream().limit(10).collect(Collectors.toList());
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                return temp;
            });
            return userTimeline;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return userTimeline;
    }


    /**
     * This method displays displays a word-level statistics for the
     * 100 latest tweets, counting all unique words in descending order.     *
     * @author Sujay
     */
    public CompletableFuture<HashMap<String, Integer>> calculateStatistics(CompletableFuture<List<Details>> cfDetails) {
        HashMap<String, Integer> statMap = new LinkedHashMap<>();
        return CompletableFuture.supplyAsync(() -> {
            try {
                List<Details> detailsList = cfDetails.get();

                detailsList.stream()
                        .map((Details d) -> {
                            String splitWords[] = d.text.split(" ");
                            Arrays.stream(splitWords)
                                    .map((String s) -> {
                                        if (statMap.containsKey(s)) {
                                            int value = statMap.get(s);
                                            value++;
                                            statMap.put(s, value);
                                        } else {
                                            statMap.put(s, 1);
                                        }
                                        return statMap;
                                    }).collect(Collectors.toList());
                            return statMap;
                        }).collect(Collectors.toList());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return statMap;
        }).thenApply((wordCount) -> wordCount.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldvalue, newvalue) -> oldvalue, LinkedHashMap::new)));

    }

    /**
     * This method takes as input all the tweets received from twitter and
     * reduces the number to 10 only
     * @author Pallavi
     */

    public CompletableFuture<List<Details>> reduceTweets(CompletableFuture<List<Details>> cfOriginalTweetData)  {
        CompletableFuture<List<Details>> cfReducedTweets=null;

            cfReducedTweets= CompletableFuture.supplyAsync(() ->{
                List<Details> reducedTweetList=null;
                try{
                    reducedTweetList= cfOriginalTweetData.get().stream()
                            .limit(10).collect(Collectors.toList());
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                return reducedTweetList;
            });

        return cfReducedTweets;
    }

    /**
     * This method calculates the sentiment happy , sad or neutral for
     * the resulting tweets      *
     * @author Suyash
     */
    public CompletableFuture<String> analyzeSentiment(List<Details> sentimentData)  {

        CompletableFuture<String> sentiment=null;
        CompletableFuture<List<Details>> happyTweets=null;

            int count=0;
            happyTweets = CompletableFuture.supplyAsync(() -> sentimentData.stream()
                    .filter(d -> d.text.contains(":-)"))
                    .collect(Collectors.toList()));

            CompletableFuture<List<Details>> sadTweets = CompletableFuture.supplyAsync(() -> sentimentData.stream()
                    .filter(d -> d.text.contains(":-("))
                    .collect(Collectors.toList()));

            sentiment = happyTweets.thenCombine(sadTweets, (happyFilter, sadFilter) -> {
                if (happyFilter.size() / sentimentData.size() >= 0.7)
                    return ":-)";
                else if (sadFilter.size() / sentimentData.size() >= 0.7)
                    return ":-(";
                else
                    return ":-|";
            });

        return sentiment;
    }

    /**
     * This method displays the 10 latest tweets
     * from the same location as a tweet from the search
     * results
     * @author Nishant
     */

    public CompletableFuture<List<Details>> searchTweetByLocation(CompletableFuture<String> geoLocation){
        try{
            String topic= geoLocation.get();
            return searchTweetByTopic(topic);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;

   }

    /**
     * This method calls the twitter api and
     * gets all the tweets for a given topic
     * @author Suyash
     */

    public CompletableFuture<List<Details>> searchTweetByTopic(String topic) {

        List<Details> tweetData = new ArrayList<>();
        try {
            Query query = new Query(topic);
            query.setCount(100);
            listCompletableFuture = CompletableFuture.supplyAsync(() -> {
                QueryResult result = null;
                try {
                    result = twitter.search(query);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }).thenApply((QueryResult result) -> {
                tweetStatusObjects = result.getTweets();
                return tweetStatusObjects;
            }).thenApply((tweetStatusObjects) -> {
                tweetStatusObjects.stream()
                        .map((Status s) -> {
                            tweetData.add(new Details(s.getUser().getName(), s.getUser().getLocation(),
                                    s.getUser().getFollowersCount(), s.getUser().getScreenName(), s.getText(),s.getHashtagEntities()));
                            return tweetData;
                        })
                        .collect(Collectors.toList());
                return tweetData;
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listCompletableFuture;
    }

    /**
     * This method takes as input the screen name of the user and gets all the data for user profile
     * @author Pallavi
     */

    public CompletableFuture<User> showUser(String screenName){
        CompletableFuture<User> user=null;
        try {
            user=CompletableFuture.supplyAsync(() ->{
                User temp=null;
                try {
                    temp=twitter.showUser(screenName);;
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                return temp;
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

    /**
     *This method builds the string used for searching location
     * tweets as per twitter standards
     * @author Nishant
     */


    public CompletableFuture<String> constructGeoString(CompletableFuture<String []> latLongs){

        return CompletableFuture.supplyAsync(() ->{
            String [] positions= null;
            String geoLocation=null;
            try{
               positions=latLongs.get();
                geoLocation = "geocode:"+positions[0]+","+positions[1]+",50km";
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return geoLocation;
        });


    }

}
