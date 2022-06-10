package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Parcel
public class Tweet {

    public String body;
    public String createdAt;
    public User user;
    public Boolean hasMedia;
    public String imageURL;
    public String timestamp;
    public String name;
    public String id;
    public String like;
    public String retweet;

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public Tweet() {}

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.id = jsonObject.getString("id");
        JSONObject jsonEntities = jsonObject.getJSONObject("entities");
        tweet.like = jsonObject.getString("favorited");
        tweet.retweet = jsonObject.getString("retweeted");
        if (jsonEntities.has("media")) {
            JSONArray mediaArray = jsonEntities.getJSONArray("media");
            JSONObject jsonMedia = mediaArray.getJSONObject(0);
            tweet.imageURL = jsonMedia.getString("media_url_https");
            tweet.hasMedia = true;
        }
        else {
            tweet.hasMedia = false;
            tweet.imageURL = "";
        }

        tweet.timestamp = tweet.getRelativeTimeAgo(tweet.createdAt);
        tweet.name = tweet.user.name;
        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException{
        List<Tweet> tweets = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++){
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }

    public String getRelativeTimeAgo(String jsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat simpleFormat = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        simpleFormat.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = simpleFormat.parse(jsonDate).getTime();
            long current = System.currentTimeMillis();

            final long timeDifference = current - dateMillis;
            if (timeDifference < MINUTE_MILLIS) {
                relativeDate = timeDifference / SECOND_MILLIS + "s";
            } else if (timeDifference < 50 * MINUTE_MILLIS) {
                relativeDate = timeDifference / MINUTE_MILLIS + "m";
            } else if (timeDifference < 24 * HOUR_MILLIS) {
                relativeDate = timeDifference / HOUR_MILLIS + "h";
            } else {
                relativeDate = timeDifference / DAY_MILLIS + "d";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return relativeDate;
    }

    public String getTweet(){
        return body;
    }
}
