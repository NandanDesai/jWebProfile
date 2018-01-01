/*
 * Author: Nandan Desai
 * Year: 2017
 */
package applicationLogic;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 *
 * @author nandan
 */
public class JWebProfile {

    public String TWITTERusername;
    public String TWITTERname;
    public ImageIcon TWITTERprofilePhoto;
    public String TWITTERdescription;
    public String TWITTERlocation;
    public String TWITTERwebsite;
    public int TWITTERnoOfFollowers;
    public int TWITTERfollowingCount;
    public String TWITTERisVerified;
    public String TWITTERyouFollowing;
    public String TWITTERyouFollowedby;
    //Try to use Arraylist here as it may change
    ArrayList<String> list1 = new ArrayList<>();
    ArrayList<String> list2 = new ArrayList<>();
    int size1;
    int size2;
    /*
    public String[] TWITTERfollowingUsername;
    public String[] TWITTERfollowingName;
    public String[] TWITTERfollowedbyUsername;
    public String[] TWITTERfollowedbyName;
     */
    //public String[] TWITTERtweets;

    /*
        for Github
     */
    public String GITHUBusername;
    public ImageIcon GITHUBprofilePhoto;
    public String GITHUBemail;
    public String GITHUBblogUrl;
    public int GITHUBnoOfFollowers;
    public int GITHUBfollowingCount;
    public String GITHUBisHireable;
    public String GITHUBcompany;
    public int GITHUBnoOfRepositories;

    public JWebProfile(Connection con, String username) throws Exception {
        //also I can call this method whereever I am creating objects of this class
        if (DatabaseActivity.existingAccountsForGivenUsername(con, username).equalsIgnoreCase("twitter")) {
            System.out.println("initializing twitter attributes");
            initializeTwitterAttributes(con, username);
            System.out.println("Inside JWebProfile constructor. No github account has been setup");
        } else if (DatabaseActivity.existingAccountsForGivenUsername(con, username).equalsIgnoreCase("github")) {
            initializeGithubAttributes(con, username);
            System.out.println("Inside JWebProfile constructor. No twitter account has been setup");
        } else {

            if (DatabaseActivity.isTwitterUsername(con, username)) {
                initializeTwitterAttributes(con, username);
                PreparedStatement ps = con.prepareStatement("select githubloginname from jwebprofile where twitterscreenname=?");
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();
                rs.next();
                String githubUsername = rs.getString(1);
                initializeGithubAttributes(con, githubUsername);
                System.out.println("Inside JWebProfile constructor. both accounts have been setup(1)");
            } else {
                initializeGithubAttributes(con, username);
                PreparedStatement ps = con.prepareStatement("select twitterscreenname from jwebprofile where githubloginname=?");
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();
                rs.next();
                String twitterUsername = rs.getString(1);
                initializeTwitterAttributes(con, twitterUsername);
                System.out.println("Inside JWebProfile constructor. both accounts have been setup(2)");
            }
        }
    }

    private void initializeTwitterAttributes(Connection con, String TwitterUsername) throws Exception {
        PreparedStatement ps = con.prepareStatement("select * from TwitterProfiles where screenName=?");
        ps.setString(1, TwitterUsername);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            this.TWITTERusername = rs.getString(1);
            this.TWITTERname = rs.getString(2);
            Blob blob = rs.getBlob(3);
            this.TWITTERprofilePhoto = new ImageIcon(blob.getBytes(1L, (int) blob.length()));
            this.TWITTERlocation = rs.getString(5);
            this.TWITTERdescription = rs.getString(4);
            this.TWITTERwebsite = rs.getString(6);
            this.TWITTERnoOfFollowers = rs.getInt(7);
            this.TWITTERfollowingCount = rs.getInt(8);
            this.TWITTERisVerified = rs.getString(9);
            this.TWITTERyouFollowing = rs.getString(10);
            this.TWITTERyouFollowedby = rs.getString(11);
        }

    }

    private void initializeGithubAttributes(Connection con, String GithubUsername) throws Exception {
        PreparedStatement ps = con.prepareStatement("select * from GithubProfiles where loginname=?");
        ps.setString(1, GithubUsername);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            this.GITHUBusername = rs.getString(1);
            Blob blob = rs.getBlob(3);
            this.GITHUBprofilePhoto = new ImageIcon(blob.getBytes(1L, (int) blob.length()));
            this.GITHUBemail = rs.getString(4);
            this.GITHUBblogUrl = rs.getString(5);
            this.GITHUBnoOfFollowers = rs.getInt(6);
            this.GITHUBfollowingCount = rs.getInt(7);
            this.GITHUBisHireable = rs.getString(8);
            this.GITHUBcompany = rs.getString(9);
            this.GITHUBnoOfRepositories = rs.getInt(10);
        }

    }

    public ArrayList<JWebProfile.GithubRepository> getGithubRepos(Connection con, String githubUsername) throws Exception {

        String repoName;
        String description;
        String language;
        String created;
        String updated;
        String isFork;
        PreparedStatement ps = con.prepareStatement("select * from GithubRepository where loginname=?");
        ps.setString(1, githubUsername);
        ResultSet rs = ps.executeQuery();
        ArrayList<JWebProfile.GithubRepository> gitRepos = new ArrayList<JWebProfile.GithubRepository>();
        JWebProfile.GithubRepository gitRepo;
        while (rs.next()) {
            repoName = rs.getString(2);
            description = rs.getString(3);
            language = rs.getString(4);
            created = rs.getString(5);
            updated = rs.getString(6);
            isFork = rs.getString(7);
            gitRepo = new JWebProfile.GithubRepository(repoName, description, language, created, updated, isFork);
            gitRepos.add(gitRepo);
        }
        return gitRepos;
    }

    public ArrayList<JWebProfile.TwitterTweet> getTwitterTweets(Connection con, String twitterScreenName) throws Exception {
        String tweet;
        String tweetTime;
        int noOfRetweets;
        int noOfLikes;

        PreparedStatement ps = con.prepareStatement("select * from TwitterTweets where screenName=? order by tweetID desc");
        ps.setString(1, twitterScreenName);
        ResultSet rs = ps.executeQuery();
        ArrayList<JWebProfile.TwitterTweet> twitterTweets = new ArrayList<JWebProfile.TwitterTweet>();
        JWebProfile.TwitterTweet twitterTweet;
        while (rs.next()) {
            tweet = rs.getString(2);
            tweetTime = rs.getString(3);
            noOfRetweets = rs.getInt(4);
            noOfLikes = rs.getInt(5);
            twitterTweet = new JWebProfile.TwitterTweet(tweet, tweetTime, noOfRetweets, noOfLikes);
            twitterTweets.add(twitterTweet);
        }
        return twitterTweets;
    }

    public static class GithubRepository {

        public String repositoryName;
        public String description;
        public String language;
        public String created;
        public String updated;
        public String isFork;

        GithubRepository(String repositoryName, String description, String language, String created, String updated, String isFork) {
            this.repositoryName = repositoryName;
            this.description = description;
            this.language = language;
            this.created = created;
            this.updated = updated;
            this.isFork = isFork;
        }
    }

    public static class TwitterTweet {

        public String tweet;
        public String tweetTime;
        public int noOfRetweets;
        public int noOfLikes;

        TwitterTweet(String tweet, String tweetTime, int noOfRetweets, int noOfLikes) {
            this.tweet = tweet;
            this.tweetTime = tweetTime;
            this.noOfRetweets = noOfRetweets;
            this.noOfLikes = noOfLikes;
        }
    }

}
