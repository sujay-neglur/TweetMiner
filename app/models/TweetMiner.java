package models;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.User;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface TweetMiner {
    Twitter getInstance();
    CompletableFuture<String[]> getLatLongPositions(String address);
    CompletableFuture<HashMap<String, Integer>> calculateStatistics(CompletableFuture<List<Details>> cfDetails);
    CompletableFuture<List<Details>> reduceTweets(CompletableFuture<List<Details>> cfOriginalTweetData);
    CompletableFuture<String> analyzeSentiment(List<Details> sentimentData);
    CompletableFuture<List<Details>> searchTweetByTopic(String topic);
    CompletableFuture<List<Status>> getUserTimeline(String screenName);
    CompletableFuture<User> showUser(String screenName);
    CompletableFuture<String> constructGeoString(CompletableFuture<String []> latLongs);
    CompletableFuture<List<Details>> searchTweetByLocation(CompletableFuture<String> geoLocation);
    void setDataStatMap(String key, CompletableFuture<HashMap<String, Integer>> value);
    CompletableFuture<HashMap<String, Integer>> getDataStatMap(String key);
}
