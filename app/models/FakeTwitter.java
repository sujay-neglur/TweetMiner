package models;

import play.data.FormFactory;
import play.i18n.MessagesApi;
import twitter4j.*;
import static org.mockito.Mockito.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class FakeTwitter implements TweetMiner {

    HashMap<String,CompletableFuture<HashMap<String,Integer>>> dataStatMap= new LinkedHashMap<>();

    public FakeTwitter(){
        dataStatMap.put("hello",CompletableFuture.supplyAsync(() ->{
            HashMap<String,Integer> temp= new HashMap<>();
            temp.put("one",1);
            return temp;
        }));
    }

    RealTweet realTweet = new RealTweet();


    public void setDataStatMap(String key, CompletableFuture<HashMap<String, Integer>> value) {
        dataStatMap.put(key,value);
    }

    public CompletableFuture<HashMap<String, Integer>> getDataStatMap(String key){
        return dataStatMap.get(key);
    }

    public Twitter getInstance() {
        return new TwitterFactory().getInstance();
    }

    public CompletableFuture<String[]> getLatLongPositions(String address) {
        return CompletableFuture.supplyAsync(() -> new String[]{"100", "200"});
    }

    public CompletableFuture<HashMap<String, Integer>> calculateStatistics(CompletableFuture<List<Details>> cfDetails) {
        return realTweet.calculateStatistics(cfDetails);
    }

    public CompletableFuture<List<Details>> reduceTweets(CompletableFuture<List<Details>> cfOriginalTweetData) {
        return realTweet.reduceTweets(cfOriginalTweetData);
    }

    public CompletableFuture<String> analyzeSentiment(List<Details> sentimentData) {
        return realTweet.analyzeSentiment(sentimentData);
    }

    public CompletableFuture<List<Details>> searchTweetByTopic(String topic) {
        if ("one".equals(topic)) {
            return CompletableFuture.supplyAsync(() -> Arrays.asList(
                    new Details("user1 ", "montreal", 2, "u1", "This is user tweet :-)", new HashtagEntity[10]),
                    new Details("user2 ", "toronto", 2, "u1", "This is user tweet :-)", new HashtagEntity[10]),
                    new Details("user3 ", "mumbai", 2, "u1", "This is user tweet :-)", new HashtagEntity[10]),
                    new Details("user4 ", "delhi", 2, "u1", "This is user tweet :-)", new HashtagEntity[10]),
                    new Details("user5 ", "vancouver", 2, "u1", "This is user tweet :-)", new HashtagEntity[10]),
                    new Details("user6 ", "chennai", 2, "u1", "This is user tweet :-)", new HashtagEntity[10]),
                    new Details("user7 ", "indore", 2, "u1", "This is user tweet :-)", new HashtagEntity[10]),
                    new Details("user8 ", "montreal", 2, "u1", "This is user tweet :-)", new HashtagEntity[10]),
                    new Details("user9 ", "montreal", 2, "u1", "This is user tweet :-)", new HashtagEntity[10]),
                    new Details("user10 ", "montreal", 2, "u1", "This is user tweet :-)", new HashtagEntity[10])

            ));
        }
        if("two".equals(topic)){
           return CompletableFuture.supplyAsync(() -> Arrays.asList(new Details("user1 ", "montreal", 2, "u1", "This is user tweet :-(", new HashtagEntity[10]),
                   new Details("user2 ", "toronto", 2, "u1", "This is user tweet :-(", new HashtagEntity[10]),
                   new Details("user3 ", "mumbai", 2, "u1", "This is user tweet :-(", new HashtagEntity[10]),
                   new Details("user4 ", "delhi", 2, "u1", "This is user tweet :-(", new HashtagEntity[10]),
                   new Details("user5 ", "vancouver", 2, "u1", "This is user tweet :-(", new HashtagEntity[10]),
                   new Details("user6 ", "chennai", 2, "u1", "This is user tweet :-(", new HashtagEntity[10]),
                   new Details("user7 ", "indore", 2, "u1", "This is user tweet :-(", new HashtagEntity[10]),
                   new Details("user8 ", "montreal", 2, "u1", "This is user tweet :-(", new HashtagEntity[10]),
                   new Details("user9 ", "montreal", 2, "u1", "This is user tweet :-(", new HashtagEntity[10]),
                   new Details("user10 ", "montreal", 2, "u1", "This is user tweet :-(", new HashtagEntity[10])));
        }
        if("three".equals(topic)){
            return CompletableFuture.supplyAsync(() -> Arrays.asList(new Details("user1 ", "montreal", 2, "u1", "This is user tweet :-)", new HashtagEntity[10]),
                    new Details("user2 ", "toronto", 2, "u1", "This is user tweet :-)", new HashtagEntity[10]),
                    new Details("user3 ", "mumbai", 2, "u1", "This is user tweet :-)", new HashtagEntity[10]),
                    new Details("user4 ", "delhi", 2, "u1", "This is user tweet :-)", new HashtagEntity[10]),
                    new Details("user5 ", "vancouver", 2, "u1", "This is user tweet :-)", new HashtagEntity[10]),
                    new Details("user6 ", "chennai", 2, "u1", "This is user tweet :-(", new HashtagEntity[10]),
                    new Details("user7 ", "indore", 2, "u1", "This is user tweet :-(", new HashtagEntity[10]),
                    new Details("user8 ", "montreal", 2, "u1", "This is user tweet :-(", new HashtagEntity[10]),
                    new Details("user9 ", "montreal", 2, "u1", "This is user tweet :-(", new HashtagEntity[10]),
                    new Details("user10 ", "montreal", 2, "u1", "This is user tweet :-(", new HashtagEntity[10])));
        }
        return CompletableFuture.supplyAsync(() -> Arrays.asList(new Details("userD","default",2,"default","default tweet",new HashtagEntity[1])));
    }

    public CompletableFuture<List<Status>> getUserTimeline(String screenName){
        if(screenName==null)
            return null;
        List<Status> status = new ArrayList<>();
        for(int i=0;i<10;i++){
            status.add(new Status() {
                @Override
                public Date getCreatedAt() {
                    return new Date();
                }

                @Override
                public long getId() {
                    return 11;
                }

                @Override
                public String getText() {
                    return "This is tweet";
                }

                @Override
                public int getDisplayTextRangeStart() {
                    return 0;
                }

                @Override
                public int getDisplayTextRangeEnd() {
                    return 0;
                }

                @Override
                public String getSource() {
                    return null;
                }

                @Override
                public boolean isTruncated() {
                    return false;
                }

                @Override
                public long getInReplyToStatusId() {
                    return 0;
                }

                @Override
                public long getInReplyToUserId() {
                    return 0;
                }

                @Override
                public String getInReplyToScreenName() {
                    return null;
                }

                @Override
                public GeoLocation getGeoLocation() {
                    return null;
                }

                @Override
                public Place getPlace() {
                    return null;
                }

                @Override
                public boolean isFavorited() {
                    return false;
                }

                @Override
                public boolean isRetweeted() {
                    return false;
                }

                @Override
                public int getFavoriteCount() {
                    return 0;
                }

                @Override
                public User getUser() {
                    return null;
                }

                @Override
                public boolean isRetweet() {
                    return false;
                }

                @Override
                public Status getRetweetedStatus() {
                    return null;
                }

                @Override
                public long[] getContributors() {
                    return new long[0];
                }

                @Override
                public int getRetweetCount() {
                    return 0;
                }

                @Override
                public boolean isRetweetedByMe() {
                    return false;
                }

                @Override
                public long getCurrentUserRetweetId() {
                    return 0;
                }

                @Override
                public boolean isPossiblySensitive() {
                    return false;
                }

                @Override
                public String getLang() {
                    return null;
                }

                @Override
                public Scopes getScopes() {
                    return null;
                }

                @Override
                public String[] getWithheldInCountries() {
                    return new String[0];
                }

                @Override
                public long getQuotedStatusId() {
                    return 0;
                }

                @Override
                public Status getQuotedStatus() {
                    return null;
                }

                @Override
                public int compareTo(Status status) {
                    return 0;
                }

                @Override
                public UserMentionEntity[] getUserMentionEntities() {
                    return new UserMentionEntity[0];
                }

                @Override
                public URLEntity[] getURLEntities() {
                    return new URLEntity[0];
                }

                @Override
                public HashtagEntity[] getHashtagEntities() {
                    return new HashtagEntity[10];
                }

                @Override
                public MediaEntity[] getMediaEntities() {
                    return new MediaEntity[0];
                }

                @Override
                public SymbolEntity[] getSymbolEntities() {
                    return new SymbolEntity[0];
                }

                @Override
                public RateLimitStatus getRateLimitStatus() {
                    return null;
                }

                @Override
                public int getAccessLevel() {
                    return 0;
                }
            });
        }
        return CompletableFuture.supplyAsync(() -> status);
    }
    public CompletableFuture<User> showUser(String screenName){
        User user= new User() {
            @Override
            public long getId() {
                return 0;
            }

            @Override
            public String getName() {
                return "Dummy user";
            }

            @Override
            public String getEmail() {
                return null;
            }

            @Override
            public String getScreenName() {
                return "User";
            }

            @Override
            public String getLocation() {
                return "Montreal";
            }

            @Override
            public String getDescription() {
                return "This is fake user";
            }

            @Override
            public boolean isContributorsEnabled() {
                return false;
            }

            @Override
            public String getProfileImageURL() {
                return null;
            }

            @Override
            public String getBiggerProfileImageURL() {
                return null;
            }

            @Override
            public String getMiniProfileImageURL() {
                return null;
            }

            @Override
            public String getOriginalProfileImageURL() {
                return null;
            }

            @Override
            public String getProfileImageURLHttps() {
                return null;
            }

            @Override
            public String getBiggerProfileImageURLHttps() {
                return null;
            }

            @Override
            public String getMiniProfileImageURLHttps() {
                return null;
            }

            @Override
            public String getOriginalProfileImageURLHttps() {
                return null;
            }

            @Override
            public boolean isDefaultProfileImage() {
                return false;
            }

            @Override
            public String getURL() {
                return null;
            }

            @Override
            public boolean isProtected() {
                return false;
            }

            @Override
            public int getFollowersCount() {
                return 1;
            }

            @Override
            public Status getStatus() {
                return null;
            }

            @Override
            public String getProfileBackgroundColor() {
                return null;
            }

            @Override
            public String getProfileTextColor() {
                return null;
            }

            @Override
            public String getProfileLinkColor() {
                return null;
            }

            @Override
            public String getProfileSidebarFillColor() {
                return null;
            }

            @Override
            public String getProfileSidebarBorderColor() {
                return null;
            }

            @Override
            public boolean isProfileUseBackgroundImage() {
                return false;
            }

            @Override
            public boolean isDefaultProfile() {
                return false;
            }

            @Override
            public boolean isShowAllInlineMedia() {
                return false;
            }

            @Override
            public int getFriendsCount() {
                return 0;
            }

            @Override
            public Date getCreatedAt() {
                return null;
            }

            @Override
            public int getFavouritesCount() {
                return 0;
            }

            @Override
            public int getUtcOffset() {
                return 0;
            }

            @Override
            public String getTimeZone() {
                return null;
            }

            @Override
            public String getProfileBackgroundImageURL() {
                return null;
            }

            @Override
            public String getProfileBackgroundImageUrlHttps() {
                return null;
            }

            @Override
            public String getProfileBannerURL() {
                return null;
            }

            @Override
            public String getProfileBannerRetinaURL() {
                return null;
            }

            @Override
            public String getProfileBannerIPadURL() {
                return null;
            }

            @Override
            public String getProfileBannerIPadRetinaURL() {
                return null;
            }

            @Override
            public String getProfileBannerMobileURL() {
                return null;
            }

            @Override
            public String getProfileBannerMobileRetinaURL() {
                return null;
            }

            @Override
            public boolean isProfileBackgroundTiled() {
                return false;
            }

            @Override
            public String getLang() {
                return null;
            }

            @Override
            public int getStatusesCount() {
                return 0;
            }

            @Override
            public boolean isGeoEnabled() {
                return false;
            }

            @Override
            public boolean isVerified() {
                return false;
            }

            @Override
            public boolean isTranslator() {
                return false;
            }

            @Override
            public int getListedCount() {
                return 0;
            }

            @Override
            public boolean isFollowRequestSent() {
                return false;
            }

            @Override
            public URLEntity[] getDescriptionURLEntities() {
                return new URLEntity[0];
            }

            @Override
            public URLEntity getURLEntity() {
                return null;
            }

            @Override
            public String[] getWithheldInCountries() {
                return new String[0];
            }

            @Override
            public int compareTo(User user) {
                return 0;
            }

            @Override
            public RateLimitStatus getRateLimitStatus() {
                return null;
            }

            @Override
            public int getAccessLevel() {
                return 0;
            }
        };
        return CompletableFuture.supplyAsync(() -> user);
    }

    public CompletableFuture<String> constructGeoString(CompletableFuture<String []> latLongs ){
        return CompletableFuture.supplyAsync(() -> "geoString");
    }

    public CompletableFuture<List<Details>> searchTweetByLocation(CompletableFuture<String> geoLocation){
        CompletableFuture<List<Details>> lt= searchTweetByTopic("one");
        return lt;
    }
}
