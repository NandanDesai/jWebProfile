/*
 * Author: Nandan Desai
 * Year: 2017
 */
package applicationLogic;

import com.twitter.Extractor;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.ImageIcon;

/**
 *
 * @author nandan
 */
public class JWebProfileActivity {

    String[] getNames;
    int getSize;
    Connection con;
    String TWITTERname;
    ImageIcon icon;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<SearchResult> searchResults;

    public JWebProfileActivity(Connection con) throws Exception {
        this.con = con;
        searchResults = getAlljWebProfileUsernames();
        getSize = searchResults.size();

    }

    //check for the user before retrieving anything from the database
    public boolean checkIfUserExists(String queryName) throws Exception {
        //JWebProfileActivity activity=new JWebProfileActivity(con);
        //ArrayList<SearchResult> searchResults=getAlljWebProfileUsernames();
        //getSize=searchResults.size();
        getNames = new String[getSize];
        System.out.println("Number of users in total: " + getSize);
        int i = 0;
        for (SearchResult result : searchResults) {
            System.out.println("Checking user github user: " + result.githubLoginName);
            getNames[i] = getNameOfThejWebProfile(result.twitterScreenName, result.githubLoginName);
            System.out.println("List of jWebProfile names: " + getNames[i]);
            i++;
        }
        for (i = 0; i < getSize; i++) {
            if (getNames[i].equalsIgnoreCase(queryName)) {
                return true;
            }
        }
        return false;
    }

    /*
        No need to display pics for search results.
        Write a method which takes search name as input and returns usernames as an array list.
        Write another method which takes a single username as input and returns corresponding name.
        This will make sure that you have the username when quering the database and also you have the names to display for 
        search results.
        Delete unwanted methods and classes(this shitty inner class below :) )
        -Nandan
     */
    //this method should not be used
    public ArrayList<String> getTWITTERUserNames(String queryName) throws Exception {
        ArrayList<String> resultUserNames = new ArrayList<String>();
        PreparedStatement ps = con.prepareStatement("select screenName from TwitterProfiles where UPPER(name)=?");
        queryName = queryName.toUpperCase();
        ps.setString(1, queryName);
        System.out.println("Query name is " + queryName);
        ResultSet rs = ps.executeQuery();
        System.out.println("inside the profile activity method. got the ResultSet. going for loop");
        while (rs.next()) {
            System.out.println("Got the user with name " + rs.getString(1) + " in profile activity method");
            resultUserNames.add(rs.getString(1));
        }
        System.out.println("done with profile activity method");
        return resultUserNames;
    }

    public String getTWITTERname(String queryUsername) throws Exception {
        PreparedStatement ps = con.prepareStatement("select name from TwitterProfiles where screenName=?");
        ps.setString(1, queryUsername);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getString(1);
    }

    public String getGITHUBname(String queryUsername) throws Exception {
        PreparedStatement ps = con.prepareStatement("select name from GithubProfiles where loginName=?");
        ps.setString(1, queryUsername);
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getString(1);
    }

    //this method should not be used
    public ArrayList<String> getAllTWITTERUserNames() throws Exception {
        ArrayList<String> resultUserNames = new ArrayList<String>();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select screenName from TwitterProfiles");
        System.out.println("inside the ALL profile activity method. got the ResultSet. going for loop");
        while (rs.next()) {
            System.out.println("Got the user with name " + rs.getString(1) + " in ALL profile activity method");
            resultUserNames.add(rs.getString(1));
        }
        System.out.println("done with ALL profile activity method");
        return resultUserNames;
    }

    public ArrayList<SearchResult> getAlljWebProfileUsernames() throws Exception {
        ArrayList<SearchResult> results = new ArrayList<SearchResult>();
        SearchResult searchResultObj;
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select twitterScreenName,githubLoginName from jWebProfile");
        System.out.println("inside the ALL profile activity method. got the ResultSet. going for loop");
        while (rs.next()) {
            searchResultObj = new SearchResult(rs.getString(1), rs.getString(2));
            System.out.println("got the searchResult obj for getAlljWebProfile: github: " + searchResultObj.githubLoginName + " ---------twitter:" + searchResultObj.twitterScreenName);
            results.add(searchResultObj);
        }
        System.out.println("done with ALL profile activity method");
        return results;
    }

    public ArrayList<SearchResult> getjWebProfileUsernamesForGivenQuery(String query) throws Exception {
        ArrayList<SearchResult> results = new ArrayList<SearchResult>();
        SearchResult searchResultObj;
        //Statement stmt=con.createStatement();
        //ResultSet rs=stmt.executeQuery("select twitterScreenName,githubLoginName from jWebProfile where ? in (select name from twitterprofiles) or ? in (select name from githubprofiles)");
        PreparedStatement ps = con.prepareStatement("select twitterScreenName,githubLoginName from jWebProfile where (twitterScreenName in (select screenname from twitterprofiles where Upper(twitterprofiles.name)=?) or githubloginname in (select loginname from githubprofiles where Upper(githubprofiles.name)=?))");
        ps.setString(1, query.toUpperCase());
        ps.setString(2, query.toUpperCase());
        ResultSet rs = ps.executeQuery();
        System.out.println("inside the QUERY profile activity method. got the ResultSet. going for loop");
        while (rs.next()) {
            searchResultObj = new SearchResult(rs.getString(1), rs.getString(2));
            System.out.println("got the searchResult obj for getAlljWebProfile");
            results.add(searchResultObj);
        }
        System.out.println("done with given QUERY profile activity method");
        return results;
    }

    //this method will take care of everything. just send whatever you get from getAlljWebProfileUsernames() method
    public String getNameOfThejWebProfile(String twitterScreenName, String githubLoginName) {
        String name;
        try {
            if (twitterScreenName == null) {
                PreparedStatement ps = con.prepareStatement("select name from GithubProfiles where loginName=?");
                ps.setString(1, githubLoginName);
                ResultSet rs = ps.executeQuery();
                rs.next();
                name = rs.getString(1);
            } else if (githubLoginName == null) {
                PreparedStatement ps = con.prepareStatement("select name from TwitterProfiles where screenname=?");
                ps.setString(1, twitterScreenName);
                ResultSet rs = ps.executeQuery();
                rs.next();
                name = rs.getString(1);
            } else {
                //here, I am assuming that the name of the person on twitter profile and github profile is the same. This should be changed some day
                PreparedStatement ps = con.prepareStatement("select name from TwitterProfiles where screenname=?");
                ps.setString(1, twitterScreenName);
                //ps.setString(2, githubLoginName);
                ResultSet rs = ps.executeQuery();
                rs.next();
                name = rs.getString(1);
            }

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
        return name;
    }

    //this method will return an existing username. If both usernames exist, then you can identify both profiles with anyone of them. That's where jwebprofile table has one of it's use
    public String getValidUsernameFromjWebProfile(SearchResult sr) {
        if (sr.githubLoginName == null) {
            return sr.twitterScreenName;
        } else {
            return sr.githubLoginName;
        }
    }

    //used when setting up a new twitter a/c to jWebProfile
    public ArrayList<SearchResult> getAllTWITTERScreenNamesWithNoGithub() throws Exception {
        ArrayList<SearchResult> results = new ArrayList<SearchResult>();
        SearchResult searchResultObj;
        ImageIcon image;
        Statement stmt = con.createStatement();
        //select screenname,profilephoto from TwitterProfiles,jWebProfile where twitterScreenName=screenName and screenName in (select twitterscreenname from jWebProfile where githubLoginName is null)
        ResultSet rs = stmt.executeQuery("select screenname,profilephoto from TwitterProfiles,jWebProfile where twitterScreenName=screenName and screenName in (select twitterscreenname from jWebProfile where githubLoginName is null)");
        System.out.println("getting twitter screennames and pics with no github a/c. now going for loop");
        while (rs.next()) {
            System.out.println("Got the user with name " + rs.getString(1) + "");
            Blob blob = rs.getBlob(2);
            image = new ImageIcon(blob.getBytes(1L, (int) blob.length()));
            System.out.println("Got the image icon");
            searchResultObj = new SearchResult(rs.getString(1), image);
            results.add(searchResultObj);
        }
        System.out.println("done with twitter with no github a/c method");
        return results;
    }

    //used when adding a new Github a/c to jWebProfile
    public ArrayList<SearchResult> getAllGITHUBLoginNamesWithNoTwitter() throws Exception {
        ArrayList<SearchResult> results = new ArrayList<SearchResult>();
        SearchResult searchResultObj;
        ImageIcon image;
        // PreparedStatement ps=con.prepareStatement("select githubloginname,profilephoto from GithubProfiles,jWebProfile where (githubLoginName=loginname and (? is null and twitterScreenName is null))");
        //ps.setNull(1, java.sql.Types.INTEGER);
        //ResultSet rs=ps.executeQuery();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select loginname,profilephoto from GithubProfiles,jWebProfile where githubLoginName=loginName and LoginName in (select githubloginname from jWebProfile where twitterScreenName is null)");
        System.out.println("getting github loginnames and pics with no twitter a/c. now going for loop");
        while (rs.next()) {
            System.out.println("Got the user with name " + rs.getString(1) + "");
            Blob blob = rs.getBlob(2);
            image = new ImageIcon(blob.getBytes(1L, (int) blob.length()));
            System.out.println("Got the image icon");
            searchResultObj = new SearchResult(rs.getString(1), image);
            results.add(searchResultObj);
            System.out.println(searchResultObj.username + ": login name from Search Result Object");
        }
        System.out.println("done with twitter with no github a/c method");
        return results;
    }

    public ArrayList<String> getUserTwitterMentions(String screenName) {
        ArrayList<String> mentions = new ArrayList<String>();
        Extractor e = new Extractor();
        try {
            List<String> tweetMentions;
            List<String> tweets = DatabaseActivity.getUserTweets(con, screenName);
            Iterator it1 = tweets.iterator();
            Iterator it2;
            while (it1.hasNext()) {
                tweetMentions = e.extractMentionedScreennames(it1.next().toString());
                it2 = tweetMentions.iterator();
                while (it2.hasNext()) {
                    mentions.add(it2.next().toString());
                }
                //mentions.addAll(e.extractMentionedScreennames(tweets.iterator().next()));
            }
        } catch (SQLException ex) {
            System.out.println("Error in JWebProfileActivity class getUserMentions method: " + ex);
        }
        return mentions;
    }

    public ArrayList<String> getUserTwitterHashtags(String screenName) {
        ArrayList<String> hashtags = new ArrayList<String>();
        Extractor e = new Extractor();
        try {
            List<String> tweetHashtags;
            List<String> tweets = DatabaseActivity.getUserTweets(con, screenName);
            Iterator it1 = tweets.iterator();
            Iterator it2;
            while (it1.hasNext()) {
                tweetHashtags = e.extractHashtags(it1.next().toString());
                it2 = tweetHashtags.iterator();
                while (it2.hasNext()) {
                    hashtags.add(it2.next().toString());
                }
                //mentions.addAll(e.extractMentionedScreennames(tweets.iterator().next()));
            }
        } catch (SQLException ex) {
            System.out.println("Error in JWebProfileActivity class getUserMentions method: " + ex);
        }
        return hashtags;
    }

}
