package models;


import twitter4j.HashtagEntity;
import java.util.Arrays;
import java.util.List;

/**
 * This class contains all properties to be displayed
 * extracted from twitter
 * @author Pallavi
 */

public class Details {
    public String userName;
    public String location;
    public int followersCount;
    public String screenName;
    public String text;
    public List<HashtagEntity> hashtagEntities;

    public Details(String userName, String location, int followersCount, String screenName, String text,HashtagEntity [] hashtagEntities) {
        this.userName = userName;
        this.location = location;
        this.followersCount = followersCount;
        this.screenName = screenName;
        this.text = text;
        this.hashtagEntities= Arrays.asList(hashtagEntities);


    }

}
