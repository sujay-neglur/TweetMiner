package controllers;

import models.RealTweet;
import models.Details;
import models.TweetMiner;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;

import twitter4j.*;
import views.html.tweets.create;
import views.html.tweets.*;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.CompletableFuture;



public class TweetsController extends Controller {


    Twitter twitter;
    TweetMiner tf;
    FormFactory formFactory;
    HttpExecutionContext ec;
    HashMap<String,CompletableFuture<List<Details>>> displayMap= new LinkedHashMap<>();
//    private HashMap<String,CompletableFuture<HashMap<String, Integer>>> dataStatsMap= new LinkedHashMap<>();
    @Inject
    public TweetsController(TweetMiner tf, FormFactory formFactory,HttpExecutionContext ec){
        this.twitter= tf.getInstance();
        this.tf=tf;
        this.formFactory=formFactory;
        this.ec=ec;
    }


    public Result create() {
        Form<RealTweet> bookForm = formFactory.form(RealTweet.class);
        displayMap.clear();
        //System.out.println("Bookform "+bookForm+" "+formFactory);
        return ok(welcome.render());
    }

    /**
     * This method takes input query from user and performs all the five functionalities
     * of searching tweets, analyzing sentiments, calculating statistics, generating hashtags
     * and location
     * @author Suyash
     */

    public Result save() {

        try{
            final Map<String, String[]> form_values = request().body().asFormUrlEncoded();
            String data = form_values.get("title")[0];
            CompletableFuture<HashMap<String, Integer>> cfStatMap;

            CompletableFuture<List<Details>> listCompletableFuture = tf.searchTweetByTopic(data);

            cfStatMap = tf.calculateStatistics(listCompletableFuture);
            // counter++;
            tf.setDataStatMap(data,cfStatMap);

            CompletableFuture<List<Details>> reducedTweetData= tf.reduceTweets(listCompletableFuture);

            displayMap.put(data,reducedTweetData);

            CompletableFuture<String> sentiment = tf.analyzeSentiment(listCompletableFuture.get());
            return ok(create.render(displayMap, cfStatMap,sentiment));

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * This method takes as input the screen name of the user and gets all the data for user profile
     * @author Pallavi
     */
    public Result showUser(String screenName) {
        CompletableFuture<User> user = null;
       CompletableFuture< List<Status>> statuses=null;

            statuses = tf.getUserTimeline(screenName);
            //System.out.println("Statuses "+statuses);

            user = tf.showUser(screenName);

        return ok(userprofile.render(user, statuses));
    }


    /**
     * This method displays the 10 latest tweets
     * from the same location as a tweet from the search
     * results
     * @author Nishant
     */
    public Result showLocation(String location)  {

        CompletableFuture<String []> latLongs = tf.getLatLongPositions(location);
        CompletableFuture<String> geoLocation= tf.constructGeoString(latLongs);
        CompletableFuture<List<Details>> lt = tf.searchTweetByLocation(geoLocation);
        CompletableFuture<List<Details>> reducedTweetData= tf.reduceTweets(lt);
        return ok(locationPage.render(location, reducedTweetData));
    }

    /**
     * This method displays displays a word-level statistics for the
     * 100 latest tweets, counting all unique words in descending order.     *
     * @author Sujay
     */
    public Result callStatistics(String keyStat) {
        CompletableFuture<HashMap<String, Integer>> cfStatMap;
        cfStatMap = tf.getDataStatMap(keyStat);
        return ok(statistics.render(keyStat, cfStatMap));
    }
    /**
     * This method takes as input a hashtag and displays the 10 latest tweets containing that hashtag     *
     * @author Shilpi
     */
    public Result getHashtag(String tag) {
        Form<RealTweet> bookForm = formFactory.form(RealTweet.class);
        CompletableFuture<List<Details>> lt = tf.searchTweetByTopic(tag);
        CompletableFuture<List<Details>> reducedTweets = tf.reduceTweets(lt);
        return ok(getHashTag.render(bookForm, reducedTweets,tag));
    }


}



