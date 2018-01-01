/*
 * Author: Nandan Desai
 * Year: 2017
 */
package applicationLogic;

import java.util.ArrayList;
import java.util.List;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author nandan
 */
public class TwitterActivity {

    static Twitter twitter;
    String myScreenName;

    public TwitterActivity(AccessToken accessToken, String myScreenName) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("KEY")
                .setOAuthConsumerSecret("SECRET")
                .setOAuthAccessToken(accessToken.getToken())
                .setOAuthAccessTokenSecret(accessToken.getTokenSecret());
        cb.setTweetModeExtended(true);

        TwitterFactory tf = new TwitterFactory(cb.build());
        this.twitter = tf.getInstance();
        this.myScreenName = myScreenName;

    }

    //this is user following list not user followed-by...
    //this is not used anywhere
    static ResponseList<User> getUserFollowingList(Twitter twitter, User user) throws Exception {
        System.out.println("getUserFollowingList is entered");

        int len = twitter.getFriendsIDs(user.getScreenName(), -1).getIDs().length;
        System.out.println("getUserFollowingList:telesto: api called in this method -  1");
        long[] ids = new long[len];
        ids = twitter.getFriendsIDs(user.getScreenName(), -1).getIDs();
        System.out.println("getUserFollowingList:telesto: api called in this method -  2");
        ArrayList<Long> aux = new ArrayList<Long>();
        ResponseList<User> users;
        for (int i = 0; i < len && i <= 20; i++) {
            //auxilary variable
            aux.add(ids[i]);
        }
        long[] auxIds = new long[aux.size()];
        for (int i = 0; i < aux.size(); i++) {
            auxIds[i] = aux.get(i);
        }
        users = twitter.lookupUsers(auxIds);
        return users;

    }

    //this is not used anywhere
    static ResponseList<User> getUserFollowedbyList(Twitter twitter, User user) throws Exception {
        int len = twitter.getFollowersIDs(user.getScreenName(), -1).getIDs().length;
        long[] ids = new long[len];
        //long[] auxIds=new long[20];
        ArrayList<Long> aux = new ArrayList<Long>();
        ids = twitter.getFollowersIDs(user.getScreenName(), -1).getIDs();

        ResponseList<User> users;
        for (int i = 0; i < len && i <= 20; i++) {
            //auxilary variable
            aux.add(ids[i]);
        }
        long[] auxIds = new long[aux.size()];
        for (int i = 0; i < aux.size(); i++) {
            auxIds[i] = aux.get(i);
        }
        users = twitter.lookupUsers(auxIds);
        return users;
    }

    static List<Status> getUserTweets(Twitter twitter, User user) throws Exception {
        Paging paging = new Paging(1, 10);

        return twitter.getUserTimeline(user.getScreenName(), paging);
    }

    public static Twitter getTwitterObject() {
        return twitter;
    }

}
