package Colourama;

import java.io.IOException;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class NamexTweet2 {

	private final static String CONSUMER_KEY = "AjY28ieRa1eT4HFX0SQGZ4qfm";
	private final static String CONSUMER_KEY_SECRET = "clEHA0Isg1ni2JkpvnHWVF2V43tavVZDloXdacgXmor2BxEnc9";

	public void start() throws TwitterException, IOException {

		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_KEY_SECRET);

		String accessToken = getSavedAccessToken();
		String accessTokenSecret = getSavedAccessTokenSecret();
		AccessToken oathAccessToken = new AccessToken(accessToken,
				accessTokenSecret);

		twitter.setOAuthAccessToken(oathAccessToken);

		//twitter.updateStatus("Hi, im updating status again from the Namex Tweet for Demo ");

		System.out.println("\nMy Timeline:");

		ResponseList<Status> list = twitter.getHomeTimeline();
		for (Status each : list) {

			System.out.println("Sent by: @" + each.getUser().getScreenName()
					+ " - " + each.getUser().getName() + "\n" + each.getText()
					+ "\n");
		}

	}

	private String getSavedAccessTokenSecret() {
		return "h4KzykpBkaxszaeQtcyODM3yQV3nBXOk21R11efqQwFBf";
	}

	private String getSavedAccessToken() {
		return "2990543673-oqKgwE9cBidpb94XXMXjJASjHYhzbh14KVr04w5";
	}

	public static void main(String[] args) throws Exception {
		new NamexTweet2().start();
	}
}