package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.parceler.Parcels;

import java.util.Objects;

import okhttp3.Headers;

public class TweetDetailsActivity extends AppCompatActivity {

    Tweet tweet;

    TextView tvDetailsUser;
    TextView tvDetailsTweet;
    ImageView ivDetailsImage;
    ImageView ivDetailsMedia;
    TextView tvDetailsName;
    Button likeButton;
    Button retweetButton;

    TwitterClient client = TwitterApp.getRestClient(this);
    public static final String TAG = "DetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);

        tvDetailsUser = (TextView) findViewById(R.id.tvDetailsUser);
        tvDetailsTweet = (TextView) findViewById(R.id.tvDetailsTweet);
        ivDetailsImage = (ImageView) findViewById(R.id.ivDetailsImage);
        ivDetailsMedia = (ImageView) findViewById(R.id.ivDetailsMedia);
        tvDetailsName = (TextView) findViewById(R.id.tvDetailsName);
        likeButton = (Button) findViewById(R.id.likeButton);
        retweetButton = (Button) findViewById(R.id.retweetButton);

        Context context = ivDetailsImage.getContext();

        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        tvDetailsTweet.setText(tweet.getTweet());
        tvDetailsUser.setText("@" + tweet.user.screenName);
        tvDetailsName.setText(tweet.user.name);
        Glide.with(context).load(tweet.user.profileImageUrl).into(ivDetailsImage);
        if(Objects.equals(tweet.like, "true")){
            likeButton.setBackgroundResource(R.drawable.ic_vector_heart);
        } else {
            likeButton.setBackgroundResource(R.drawable.ic_vector_heart_stroke);
        }
        if(Objects.equals(tweet.retweet, "true")) {
            retweetButton.setBackgroundResource(R.drawable.ic_vector_retweet);
        } else {
            retweetButton.setBackgroundResource(R.drawable.ic_vector_retweet_stroke);
        }
        if (tweet.hasMedia == true){
            ivDetailsMedia.setVisibility(View.VISIBLE);
            Glide.with(context).load(tweet.imageURL).into(ivDetailsMedia);
        }
        else {
            ivDetailsMedia.setVisibility(View.GONE);
        }
    }

    public void onLikeButton(View view) {
        String id = tweet.id;
        if (Objects.equals(tweet.like, "false")) {
            client.likeTweet(id, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.i(TAG, "onSuccess to like tweet");
                    tweet.like = "true";
                    likeButton.setBackgroundResource(R.drawable.ic_vector_heart);
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.e(TAG, "onFailure to like tweet", throwable);
                }
            });
        } else if (Objects.equals(tweet.like, "true")) {
            client.unlikeTweet(id, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.i(TAG, "onSuccess to unlike tweet");
                    tweet.like = "false";
                    likeButton.setBackgroundResource(R.drawable.ic_vector_heart_stroke);
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.e(TAG, "onFailure to unlike tweet", throwable);
                }
            });
        }
    }

    public void onRetweetButton(View view) {
        //get the id of the tweet you're liking
        String id = tweet.id;
        if (Objects.equals(tweet.retweet, "false")) {
            client.retweet(id, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.i(TAG, "onSuccess to retweet");
                    tweet.retweet = "true";
                    retweetButton.setBackgroundResource(R.drawable.ic_vector_retweet);
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.e(TAG, "onFailure to retweet", throwable);
                }
            });
        } else if (Objects.equals(tweet.retweet, "true")) {
            client.unRetweet(id, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.i(TAG, "onSuccess to unretweet");
                    tweet.retweet = "false";
                    retweetButton.setBackgroundResource(R.drawable.ic_vector_retweet_stroke);
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.e(TAG, "onFailure to unretweet", throwable);
                }
            });
        }
    }
}