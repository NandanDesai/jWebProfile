/*
 * Author: Nandan Desai
 * Year: 2017
 */
package applicationLogic;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 *
 * @author nandan
 */
public class TwitterSetupLogic {

    private String twitterAuthLink;
    private Twitter twitter;
    private RequestToken requestToken;
    private AccessToken accessToken;

    //1 - create an object using this method
    public TwitterSetupLogic() {

    }

    public Twitter getTwitterObj() {
        twitter = TwitterFactory.getSingleton();
        twitter.setOAuthConsumer("TOKEN", "TOKEN SECRET");
        return twitter;
    }

    //2 - get the twitter object from new TwitterSetupLogic() and send it to this method
    public RequestToken pullRequestToken(Twitter twitter) throws Exception {
        requestToken = twitter.getOAuthRequestToken();
        return requestToken;
    }

    //3 - get the URL from request token
    public String pullAuthorizationURL(RequestToken requestToken) {
        twitterAuthLink = requestToken.getAuthenticationURL();
        return twitterAuthLink;
    }

    //4 - get the access token by using the request token and pin
    public AccessToken pullAccessToken(RequestToken requestToken, String pin) throws Exception {

        if (pin.length() > 0) {
            accessToken = twitter.getOAuthAccessToken(requestToken, pin);
        } else {
            accessToken = twitter.getOAuthAccessToken();
        }
        return accessToken;

    }

}
