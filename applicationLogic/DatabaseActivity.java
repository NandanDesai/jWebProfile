/*
 * Author: Nandan Desai
 * Year: 2017
 */
package applicationLogic;

import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import org.apache.derby.shared.common.error.DerbySQLIntegrityConstraintViolationException;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.UserService;
import twitter4j.Paging;
import twitter4j.Relationship;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author nandan
 */
public class DatabaseActivity {

    public static Boolean dbCheck(Connection con) {
        Boolean firstTime; //is this the firstTime you are running our software, return True if yes else False
        try {

            String sql = "create table TwitterProfiles\n"
                    + "(\n"
                    + "	screenName varchar(50) primary key,\n"
                    + "	name varchar(200),\n"
                    + "	profilePhoto blob,\n"
                    + "	description varchar(1000),\n"
                    + "	location varchar(100),\n"
                    + "	website varchar(500),\n"
                    + "	noOfFollowers int,\n"
                    + "	followingCount int,\n"
                    + "	verifiedAccount varchar(10),\n"
                    + "	Are_You_Following varchar(10),\n"
                    + "	You_Are_Followedby varchar(10),\n"
                    + "	insertDate varchar(100)\n"
                    + ")";

            con.createStatement().execute(sql);
            System.out.println("Table didn't exist previously");
            System.out.println("Table created successfully");
            firstTime = true;

        } catch (SQLException e) {
            System.out.println("[IGNORE THIS MESSAGE]" + e);
            firstTime = false;
        }
       
        try {
            String sql = "create table TwitterTweets\n"
                    + "(\n"
                    + "	screenName varchar(50) references TwitterProfiles(screenName),\n"
                    + "	tweet varchar(1000),\n"
                    + "	tweetTime varchar(50),\n"
                    + "	noOfRetweets int,\n"
                    + "	noOfLikes int,\n"
                    + "	tweetID varchar(20),\n"
                    + "	snapshotTime varchar(50),\n"
                    + "	isRetweet varchar(10),\n"
                    + "	isSensitive varchar(10)\n"
                    + ")";

            con.createStatement().execute(sql);
            System.out.println("Table didn't exist previously");
            System.out.println("Table created successfully");
            firstTime = true;
        } catch (SQLException e) {
            System.out.println("[IGNORE THIS MESSAGE]" + e);
            firstTime = false;
        }

        try {
            String sql = "create table TwitterSession\n"
                    + "(\n"
                    + "	myScreenName varchar(50) primary key,\n"
                    + "	token varchar(500),\n"
                    + "	tokenSecret varchar(1000)\n"
                    + ")";

            con.createStatement().execute(sql);
            System.out.println("Table didn't exist previously");
            System.out.println("Table created successfully");
            firstTime = true;
        } catch (SQLException e) {
            System.out.println("[IGNORE THIS MESSAGE]" + e);
            firstTime = false;
        }

        try {

            String sql = "create table GithubProfiles\n"
                    + "(\n"
                    + "	loginName varchar(50) primary key,\n"
                    + "	name varchar(200),\n"
                    + "	profilePhoto blob,\n"
                    + "	email varchar(200),\n"
                    + "	blogUrl varchar(500),\n"
                    + "	noOfFollowers int,\n"
                    + "	followingCount int,\n"
                    + "	isHireable varchar(50),\n"
                    + "	company varchar(100),\n"
                    + "	noOfRepositories int,\n"
                    + "	insertDate varchar(100)\n"
                    + ")";

            con.createStatement().execute(sql);
            System.out.println("Table didn't exist previously");
            System.out.println("Table created successfully");
            firstTime = true;

        } catch (SQLException e) {
            System.out.println("[IGNORE THIS MESSAGE]" + e);
            firstTime = false;
        }

        try {

            String sql = "create table GithubRepository\n"
                    + "(\n"
                    + "	loginName varchar(50) references GithubProfiles(loginName),\n"
                    + "	repositoryName varchar(300),\n"
                    + "	description varchar(1000),\n"
                    + "	language varchar(100),\n"
                    + "	created varchar(100),\n"
                    + "	updated varchar(100),\n"
                    + "	isFork varchar(50),\n"
                    + "	repoID varchar(20) primary key\n"
                    + ")";

            con.createStatement().execute(sql);
            System.out.println("Table didn't exist previously");
            System.out.println("Table created successfully");
            firstTime = true;

        } catch (SQLException e) {
            System.out.println("[IGNORE THIS MESSAGE]" + e);
            firstTime = false;
        }

        try {

            String sql = "create table GithubSession\n"
                    + "(\n"
                    + "	myloginName varchar(50) primary key,\n"
                    + "	accessToken varchar(500)\n"
                    + ")";

            con.createStatement().execute(sql);
            System.out.println("Table didn't exist previously");
            System.out.println("Table created successfully");
            firstTime = true;

        } catch (SQLException e) {
            System.out.println("[IGNORE THIS MESSAGE]" + e);
            firstTime = false;
        }

        try {

            String sql = "create table jWebProfile\n"
                    + "(\n"
                    + "	twitterScreenName varchar(50) references TwitterProfiles(screenName),\n"
                    + "	githubLoginName varchar(50) references GithubProfiles(loginName),\n"
                    + "	cachedDate varchar(100),\n"
                    + "	constraint uc_jWebProfiles unique(twitterScreenName,githubLoginName)\n"
                    + ")";

            con.createStatement().execute(sql);
            System.out.println("Table didn't exist previously");
            System.out.println("Table created successfully");
            firstTime = true;

        } catch (SQLException e) {
            System.out.println("[IGNORE THIS MESSAGE]" + e);
            firstTime = false;
        }

        try {

            String sql = "create table ScrapedData\n"
                    + "(\n"
                    + "	twitterScreenName varchar(50) primary key references TwitterProfiles(screenName),\n"
                    + "	scrapedData varchar(32000)\n"
                    + ")";

            con.createStatement().execute(sql);
            System.out.println("Table didn't exist previously");
            System.out.println("Table created successfully");
            firstTime = true;

        } catch (SQLException e) {
            System.out.println("[IGNORE THIS MESSAGE]" + e);
            firstTime = false;
        }

        try {

            String sql = "create table LinkedInData\n"
                    + "(\n"
                    + "	twitterScreenName varchar(50) references TwitterProfiles(screenName),\n"
                    + "	name varchar(200),\n"
                    + "	description varchar(500),\n"
                    + "	location varchar(100),\n"
                    + "	organization varchar(200),\n"
                    + "	role varchar(200)\n"
                    + ")";

            con.createStatement().execute(sql);
            System.out.println("Table didn't exist previously");
            System.out.println("Table created successfully");
            firstTime = true;

        } catch (SQLException e) {
            System.out.println("[IGNORE THIS MESSAGE]" + e);
            firstTime = false;
        }

        try {
            String sql = "create table TwitterAllTweets\n"
                    + "(\n"
                    + "	screenName varchar(50) references TwitterProfiles(screenName),\n"
                    + "	tweet varchar(1000),\n"
                    + "	tweetTime varchar(50),\n"
                    + "	noOfRetweets int,\n"
                    + "	noOfLikes int,\n"
                    + "	tweetID varchar(20) primary key,\n"
                    + "	snapshotTime varchar(50),\n"
                    + "	isRetweet varchar(10),\n"
                    + "	isSensitive varchar(10)\n"
                    + ")";

            con.createStatement().execute(sql);
            System.out.println("Table didn't exist previously");
            System.out.println("Table created successfully");
            firstTime = true;
        } catch (SQLException e) {
            System.out.println("[IGNORE THIS MESSAGE]" + e);
            firstTime = false;
        }

        return firstTime;

    }

    public static void twitterSessionInsert(Connection con, String myScreenName, String token, String tokenSecret) {
        try {
            PreparedStatement ps = con.prepareStatement("insert into TwitterSession values(?,?,?)");
            ps.setString(1, myScreenName);
            ps.setString(2, token);
            ps.setString(3, tokenSecret);
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static void githubSessionInsert(Connection con, String myLoginName, String accessToken) {
        try {
            PreparedStatement ps = con.prepareStatement("insert into GithubSession values(?,?)");
            ps.setString(1, myLoginName);
            ps.setString(2, accessToken);
            ps.execute();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    //if adding a new twitter a/c, githubLoginName=null, else mention it
    //here by 'new twitter a/c' means jWebProfile doesn't have a github a/c for this person yet
    public static boolean twitterNewProfileInsert(Connection con, User user, Twitter twitter, String githubLoginName) {
        try {

            PreparedStatement ps = con.prepareStatement("insert into TwitterProfiles values(?,?,?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1, user.getScreenName());
            ps.setString(2, user.getName());
            ps.setString(4, user.getDescription());
            ps.setString(5, user.getLocation());
            ps.setString(6, user.getURL());
            ps.setInt(7, user.getFollowersCount());
            ps.setInt(8, user.getFriendsCount());
            ps.setString(9, user.isVerified() ? "Yes" : "No");
            Relationship relation = twitter.showFriendship(twitter.getScreenName(), user.getScreenName());
            ps.setString(10, relation.isTargetFollowedBySource() ? "Yes" : "No");
            ps.setString(11, relation.isTargetFollowingSource() ? "Yes" : "No");
            InputStream imageStream = new URL(user.getBiggerProfileImageURLHttps()).openStream();
            ps.setBinaryStream(3, imageStream);
            Calendar cal = Calendar.getInstance();
            java.util.Date currentTime = cal.getTime();
            ps.setString(12, currentTime + "");
            ps.execute();

            System.out.println("TwitterProfile table got 1 row inserted and is updated");
            if (githubLoginName == null) {
                jWebProfileInsert(con, user.getScreenName(), null, currentTime + "");
            } else {
                jWebProfileUpdate(con, user.getScreenName(), githubLoginName, currentTime + "");
            }
           
            try {
                List<Status> userTweets = TwitterActivity.getUserTweets(twitter, user);
                for (Status tweet : userTweets) {
                    String insertTweet = tweet.getText();
                    String tweetTime = tweet.getCreatedAt() + "";
                    int noOfRetweets = tweet.getRetweetCount();
                    int noOfLikes = tweet.getFavoriteCount();
                    long tweetID = tweet.getId();
                    ps = con.prepareStatement("insert into TwitterTweets values (?,?,?,?,?,?,?,?,?)");
                    ps.setString(1, user.getScreenName());
                    ps.setString(2, insertTweet);
                    ps.setString(3, tweetTime);
                    ps.setInt(4, noOfRetweets);
                    ps.setInt(5, noOfLikes);
                    ps.setString(6, tweetID + "");
                    cal = Calendar.getInstance();
                    currentTime = cal.getTime();
                    ps.setString(7, currentTime + "");
                    ////////////
                    ps.setString(8, tweet.isRetweet() + ""); /////////////
                    ps.setString(9, tweet.isPossiblySensitive() + "");
                    ps.execute();
                }
                System.out.println("TwitterTweets table got updated");
                System.out.println(user.getName() + " is successfully added to the database");
            } catch (Exception e) {
                System.out.println(e + " - TwitterTweets");
            }

            return true;
        } catch (IOException | IllegalStateException | SQLException | TwitterException e) {
            System.out.println("Failed to add the user to the database");
            System.out.println(e);
            System.out.println(e.getStackTrace());
            return false;
        }
    }

    //if adding a new github a/c, twitterScreenName=null, else mention it
    //here by 'new github a/c' means jWebProfile doesn't have a twitter a/c for this person yet
    public static boolean githubNewProfileInsert(Connection con, org.eclipse.egit.github.core.User user, GitHubClient client, String twitterScreenName) {
        try {
            PreparedStatement ps = con.prepareStatement("insert into GithubProfiles values(?,?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1, user.getLogin());
            ps.setString(2, user.getName());
            InputStream imageStream = new URL(user.getAvatarUrl()).openStream();
            ps.setBinaryStream(3, imageStream);
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getBlog());
            ps.setInt(6, user.getFollowers());
            ps.setInt(7, user.getFollowing());
            ps.setString(8, user.isHireable() ? "Yes" : "No");
            ps.setString(9, user.getCompany());
            ps.setInt(10, user.getPublicRepos());
            Calendar cal = Calendar.getInstance();
            java.util.Date currentTime = cal.getTime();
            ps.setString(11, currentTime + "");
            ps.execute();

            System.out.println("GithubProfiles table got 1 row inserted and is updated");
            if (twitterScreenName == null) {
                jWebProfileInsert(con, null, user.getLogin(), currentTime + "");
            } else {
                jWebProfileUpdate(con, twitterScreenName, user.getLogin(), currentTime + "");
            }
        } catch (IOException | SQLException e) {
            System.out.println(e);
            return false;
        }

        try {
            RepositoryService rservice = new RepositoryService(client);
            for (Repository repo : rservice.getRepositories(user.getLogin())) {
                PreparedStatement ps = con.prepareStatement("insert into GithubRepository values(?,?,?,?,?,?,?,?)");
                ps.setString(1, user.getLogin());
                ps.setString(2, repo.getName());
                ps.setString(3, repo.getDescription());
                ps.setString(4, repo.getLanguage());
                ps.setString(5, repo.getCreatedAt() + "");
                ps.setString(6, repo.getUpdatedAt() + "");
                ps.setString(7, repo.isFork() ? "Yes" : "No");
                ps.setString(8, repo.getId() + "");
                ps.execute();
                System.out.println("GithubRepository table got 1 row inserted and is updated");

            }

            return true;
        } catch (IOException | SQLException e) {
            System.out.println(e);
            return false;
        }

    }

    public static boolean isTwitterUsername(Connection con, String username) {
        try {
            PreparedStatement ps = con.prepareStatement("select twitterScreenName from jWebProfile");
            ResultSet rs = ps.executeQuery(); //EXCEPTION BEING GENERATED SOMEWHERE HERE

            while (rs.next()) {
                String res = rs.getString(1);
                if (res != null) {
                    if (res.equalsIgnoreCase(username)) {//here, it is like i am comparing res with null
                        System.out.println(rs.getString(1));
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("++++++++++++++exception caught here: in isTwitterUsername(): " + e);
            return false;
        }
        return false;
    }

    public static boolean isGithubUsername(Connection con, String username) {
        try {
            PreparedStatement ps = con.prepareStatement("select githubloginName from jWebProfile");
            ResultSet rs = ps.executeQuery();  //EXCEPTION BEING GENERATED SOMEWHERE HERE
            while (rs.next()) {
                String res = rs.getString(1);
                if (res != null) {
                    if (res.equalsIgnoreCase(username)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("++++++++++++++++exception caught here: in isGithubUsername(): " + e);
            return false;
        }
        return false;
    }

    public static String existingAccountsForGivenUsername(Connection con, String username) {
        try {
            System.out.println("inside " + "existingAccountsForGivenUsername()" + " method: username:" + username);
            System.out.println("is twitter username:" + isTwitterUsername(con, username) + "");
            System.out.println("is github username:" + isGithubUsername(con, username) + "");
            if (isTwitterUsername(con, username)) {
                PreparedStatement ps = con.prepareStatement("select githubloginname from jwebprofile where twitterscreenname=?");
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();
                //System.out.println("the output for the this is : "+rs.getString(1));

                /*
                    There is some problem with the evaluation of correct existing accounts for the given username.
                    Not an exception (maybe).
                    It is some logical error.
                 */
                rs.next();
                if (rs.getString(1) != null) {
                    System.out.println("checking what githubloginname is appearing for a given twitter user:" + rs.getString(1));
                    return "both";
                } else {
                    return "twitter";
                }
            } else {
                PreparedStatement ps = con.prepareStatement("select twitterscreenname from jwebprofile where githubloginname=?");
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();
                rs.next();
                if (rs.getString(1) != null) {
                    System.out.println("checking what twitterscreenname is appearing for a given github user:" + rs.getString(1));
                    return "both";
                } else {
                    return "github";
                }
            }
        } catch (Exception e) {
            System.out.println("++++++++++++++++exception caught here: in existingAccountsForGivenUsername(): " + e);
            return "doesn't exist";
        }

    }

    public static void jWebProfileInsert(Connection con, String twitterScreenName, String githubLoginName, String cachedDate) {
        try {
            PreparedStatement ps = con.prepareStatement("insert into jWebProfile values(?,?,?)");
            if (twitterScreenName == null) {
                ps.setNull(1, java.sql.Types.VARCHAR);
            } else {
                ps.setString(1, twitterScreenName);
            }
            if (githubLoginName == null) {
                ps.setNull(2, java.sql.Types.VARCHAR);
            } else {
                ps.setString(2, githubLoginName);
            }
            if (cachedDate == null) {
                ps.setNull(3, java.sql.Types.VARCHAR);
            } else {
                ps.setString(3, cachedDate);
            }

            ps.execute();
            System.out.println("jWebProfile table got 1 row inserted");
        } catch (DerbySQLIntegrityConstraintViolationException e) {
            System.out.println(e);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /*
        this update method is for different purpose.
        when, say, there is an existing twitter a/c and you want to connect github a/c to that, then you can 
        you should "update" jWebProfile table.
        that is what this is.
     */
    public static void jWebProfileUpdate(Connection con, String twitterScreenName, String githubLoginName, String cachedDate) {
        try {
            PreparedStatement ps = con.prepareStatement("update jWebProfile set twitterScreenName=?,githubLoginName=?,cachedDate=? where twitterScreenName=? or githubLoginName=?");
            ps.setString(1, twitterScreenName);
            ps.setString(2, githubLoginName);
            if (cachedDate == null) {
                ps.setNull(3, java.sql.Types.VARCHAR);
            } else {
                ps.setString(3, cachedDate);
            }

            ps.setString(4, twitterScreenName);
            ps.setString(5, githubLoginName);
            ps.execute();
            System.out.println("jWebProfile table got 1 row updated");
        } catch (SQLException exc) {
            System.out.println(exc);
        }
    }

    public static void deleteTwitterUser(Connection con, String screenName) {
        try {
            PreparedStatement ps = con.prepareStatement("delete from jWebProfile where twitterscreenName=?");
            try {

                ps.setString(1, screenName);
                ps.execute();
            } catch (Exception e) {
            }
            ps = con.prepareStatement("delete from TwitterTweets where screenName=?");
            ps.setString(1, screenName);
            ps.execute();
            /* 
                ps=con.prepareStatement("delete from TwitterFollowingList where screenName=?");
                ps.setString(1, screenName);
                ps.execute();
                
                ps=con.prepareStatement("delete from TwitterFollowedbyList where screenName=?");
                ps.setString(1, screenName);
                ps.execute();
             */
            
            try{
                ps = con.prepareStatement("delete from TwitterAllTweets where screenName=?");
                ps.setString(1, screenName);
                ps.execute();
            }catch(Exception ex){
                System.out.println(ex);
            }
            
            try{
                ps = con.prepareStatement("delete from ScrapedData where twitterScreenName=?");
                ps.setString(1, screenName);
                ps.execute();
            }catch(Exception ex){
                System.out.println(ex);
            }
            
            try{
                ps = con.prepareStatement("delete from LinkedInData where twitterScreenName=?");
                ps.setString(1, screenName);
                ps.execute();
            }catch(Exception ex){
                System.out.println(ex);
            }
            ps = con.prepareStatement("delete from TwitterProfiles where screenName=?");
            ps.setString(1, screenName);
            ps.execute();
            
            

            System.out.println("Successfully deleted the user " + screenName);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void deleteGithubUser(Connection con, String loginName) {
        try {
            PreparedStatement ps = con.prepareStatement("delete from jWebProfile where githubloginName=?");
            try {

                ps.setString(1, loginName);
                ps.execute();
            } catch (Exception e) {
            }
            ps = con.prepareStatement("delete from GithubRepository where loginName=?");
            ps.setString(1, loginName);
            ps.execute();

            ps = con.prepareStatement("delete from GithubProfiles where loginName=?");
            ps.setString(1, loginName);
            ps.execute();

            System.out.println("Successfully deleted the user " + loginName);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void clearAllRowsFromTable(Connection con, String tableName) {
        try {
            PreparedStatement ps = con.prepareStatement("delete from " + tableName);
            ps.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void TESTcleanAllTables(Connection con) {
        clearAllRowsFromTable(con, "jWebProfile");
        clearAllRowsFromTable(con, "TwitterTweets");
        clearAllRowsFromTable(con, "TwitterAllTweets");
        clearAllRowsFromTable(con, "ScrapedData");
        clearAllRowsFromTable(con, "LinkedInData");
        clearAllRowsFromTable(con, "TwitterProfiles");
        clearAllRowsFromTable(con, "GithubRepository");
        clearAllRowsFromTable(con, "GithubProfiles");
        System.out.println("All tables are now clean! Sessions are not cleaned here.");
    }

    public static void TESTshowAllTableRecords(Connection con) {
        try {
            PreparedStatement ps = con.prepareStatement("select screenName,name from TwitterProfiles");
            ResultSet rs = ps.executeQuery();
            System.out.println("-----Data from TwitterProfiles table-----");
            while (rs.next()) {
                System.out.println("Username: " + rs.getString(1) + " :::: Name: " + rs.getString(2));
            }
            System.out.println("\n\n");
           
            ps = con.prepareStatement("select * from TwitterTweets");
            rs = ps.executeQuery();
            System.out.println("------Data from TwitterTweets table------");
            while (rs.next()) {
                System.out.println("Username: " + rs.getString(1) + " :::: Tweet: " + rs.getString(2));
            }

            ps = con.prepareStatement("select * from GithubProfiles");
            rs = ps.executeQuery();
            System.out.println("-----Data from GithubProfiles table------");
            while (rs.next()) {
                System.out.println("LoginName: " + rs.getString(1) + "::::::: Github Name: " + rs.getString(2));
            }

            ps = con.prepareStatement("select * from GithubRepository");
            rs = ps.executeQuery();
            System.out.println("-----Data from GithubRepository table------");
            while (rs.next()) {
                System.out.println("RepositoryName: " + rs.getString(2));
            }

            ps = con.prepareStatement("select * from jWebProfile");
            rs = ps.executeQuery();
            System.out.println("-----Data from jWebProfile table------");
            while (rs.next()) {
                System.out.println("TwitterScreenName: " + rs.getString(1) + "::::::" + "GithubLoginName: " + rs.getString(2));
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void deleteAllTables(Connection con) {
        try {

           
            try {
                con.createStatement().execute("drop table TwitterTweets");
            } catch (Exception e) {
                System.out.println(e);
            }
            try {
                con.createStatement().execute("drop table ScrapedData");
            } catch (Exception e) {
                System.out.println(e);
            }
            try {
                con.createStatement().execute("drop table TwitterAllTweets");
            } catch (Exception e) {
                System.out.println(e);
            }
            try {
                con.createStatement().execute("drop table LinkedInData");
            } catch (Exception e) {
                System.out.println(e);
            }
            try {
                con.createStatement().execute("drop table TwitterSession");
            } catch (Exception e) {
                System.out.println(e);
            }
            try {
                con.createStatement().execute("drop table jWebProfile");
            } catch (Exception e) {
                System.out.println(e);
            }
            try {
                con.createStatement().execute("drop table TwitterProfiles");
            } catch (Exception e) {
                System.out.println(e);
            }
            try {
                con.createStatement().execute("drop table GithubRepository");
            } catch (Exception e) {
                System.out.println(e);
            }
            try {
                con.createStatement().execute("drop table GithubSession");
            } catch (Exception e) {
                System.out.println(e);
            }
            try {
                con.createStatement().execute("drop table GithubProfiles");
            } catch (Exception e) {
                System.out.println(e);
            }
            System.out.println("All tables have been dropped");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static boolean checkSessionsTable(Connection con) {
        try {
            boolean githubDone = false, twitterDone = false;
            PreparedStatement ps = con.prepareStatement("select * from GithubSession");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                githubDone = true;
            }
            ps = con.prepareStatement("select * from TwitterSession");
            rs = ps.executeQuery();
            if (rs.next()) {
                twitterDone = true;
            }
            return githubDone && twitterDone;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    public static boolean checkTwitterSessionTable(Connection con) {
        try {
            boolean twitterDone = false;
            PreparedStatement ps = con.prepareStatement("select * from TwitterSession");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                twitterDone = true;
            }
            return twitterDone;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    public static boolean checkGithubSessionTable(Connection con) {
        try {
            boolean githubDone = false;
            PreparedStatement ps = con.prepareStatement("select * from GithubSession");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                githubDone = true;
            }
            return githubDone;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    public static List<String> getUserTweets(Connection con, String screenName) throws SQLException {
        PreparedStatement ps = con.prepareStatement("select tweet from TwitterTweets where screenName=?");
        ps.setString(1, screenName);
        ResultSet rs = ps.executeQuery();
        List<String> tweets = new ArrayList<String>();
        while (rs.next()) {
            tweets.add(rs.getString(1));
        }
        return tweets;
    }

    /*
        Try to use isGithubUsername() and isTwitterUsername() methods. That means, try to accept a single username for getCachedDate(Connection con, String username)
     */
    public static String getCachedDate(Connection con, String username) throws SQLException {
        String cachedDate = "";
        if (isGithubUsername(con, username)) {
            PreparedStatement ps = con.prepareStatement("select cachedDate from jwebprofile where githubloginname=?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                cachedDate = rs.getString(1);
            }
        } else {
            PreparedStatement ps = con.prepareStatement("select cachedDate from jwebprofile where twitterscreenname=?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                cachedDate = rs.getString(1);
            }
        }
        return cachedDate;

    }

    public static String getTwitterInsertDate(Connection con, String username) throws SQLException {
        String cachedDate = "";

        PreparedStatement ps = con.prepareStatement("select insertDate from TwitterProfiles where screenname=?");
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            cachedDate = rs.getString(1);
        }

        return cachedDate;

    }

    public static String getGithubInsertDate(Connection con, String username) throws SQLException {
        String cachedDate = "";

        PreparedStatement ps = con.prepareStatement("select insertDate from GithubProfiles where loginname=?");
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            cachedDate = rs.getString(1);
        }

        return cachedDate;

    }

    public static void insertAllTweets(Connection con, Twitter twitter, String username) {
        try {
            int pageNo = 1;
            while (true) {

                Paging page = new Paging(pageNo++, 200);
                List<Status> userTweets = twitter.getUserTimeline(username, page);
                if (userTweets.size() == 0) {
                    break;
                }
                for (Status tweet : userTweets) {
                    String insertTweet = tweet.getText();
                    String tweetTime = tweet.getCreatedAt() + "";
                    int noOfRetweets = tweet.getRetweetCount();
                    int noOfLikes = tweet.getFavoriteCount();
                    long tweetID = tweet.getId();
                    PreparedStatement ps = con.prepareStatement("insert into TwitterAllTweets values (?,?,?,?,?,?,?,?,?)");
                    ps.setString(1, username);
                    ps.setString(2, insertTweet);
                    ps.setString(3, tweetTime);
                    ps.setInt(4, noOfRetweets);
                    ps.setInt(5, noOfLikes);
                    ps.setString(6, tweetID + "");
                    Calendar cal = Calendar.getInstance();
                    java.util.Date currentTime = cal.getTime();
                    ps.setString(7, currentTime + "");
                    ps.setString(8, tweet.isRetweet() + ""); /////////////
                    ps.setString(9, tweet.isPossiblySensitive() + "");
                    ps.execute();
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static int getNoOfArchivedTweets(Connection con, String username) {
        int numOfTweets = 0;
        try {
            System.out.println("++++++ Getting number of archived tweets");
            PreparedStatement ps = con.prepareStatement("select count(*) from TwitterAllTweets where screenName=?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                numOfTweets = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return numOfTweets;
    }

    //"Current" tweets are the ones present in TwitterTweets table
    public static int getNoOfCurrentTweets(Connection con, String username) {
        int numOfTweets = 0;
        try {
            System.out.println("++++++ Getting number of archived tweets");
            PreparedStatement ps = con.prepareStatement("select count(*) from TwitterTweets where screenName=?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                numOfTweets = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return numOfTweets;
    }

    /*
        this updates every table related to a particular jWebProfile
        also, don't forget to update the cachedDateTime.
     */
    public static void updateEverythingTwitter(Connection con, String username, Twitter twitter) {
        try {
            System.out.println("$$$$ $$$$ updating basic profile info. [updateEverything]");
            User user = twitter.showUser(username);
            PreparedStatement userUpdate = con.prepareStatement("update TwitterProfiles set name=?, profilePhoto=?,description=?,location=?,website=?,noOfFollowers=?,followingCount=?,verifiedAccount=?,Are_You_Following=?,You_Are_FollowedBy=? where screenName=?");
            userUpdate.setString(1, user.getName());

            InputStream imageStream = new URL(user.getBiggerProfileImageURLHttps()).openStream();
            userUpdate.setBinaryStream(2, imageStream);
            userUpdate.setString(3, user.getDescription());
            userUpdate.setString(4, user.getLocation());
            userUpdate.setString(5, user.getURL());
            userUpdate.setInt(6, user.getFollowersCount());
            userUpdate.setInt(7, user.getFriendsCount());
            userUpdate.setString(8, user.isVerified() ? "Yes" : "No");
            Relationship relation = twitter.showFriendship(twitter.getScreenName(), user.getScreenName());
            userUpdate.setString(9, relation.isTargetFollowedBySource() ? "Yes" : "No");
            userUpdate.setString(10, relation.isTargetFollowingSource() ? "Yes" : "No");
            userUpdate.setString(11, username);
            userUpdate.execute();

            System.out.println("$$$$ $$$$ updating tweets. [updateEverything]");

            //get the tweetID of all the existing tweets in the database
            ArrayList<String> tweetIDs = new ArrayList<String>();
            PreparedStatement chkTweets = con.prepareStatement("select tweetID from TwitterTweets where screenName=?");
            chkTweets.setString(1, username);
            ResultSet rs = chkTweets.executeQuery();
            while (rs.next()) {
                tweetIDs.add(rs.getString(1));
            }
            ///

            //Now request the new tweets from Twitter
            int pageNo = 1;
            while (true) {

                Paging page = new Paging(pageNo++, 10);
                List<Status> userTweets = twitter.getUserTimeline(username, page);
                if (userTweets.size() == 0) {
                    break;
                }
                for (Status tweet : userTweets) {
                    //assuming that tweets are sent by Twitter with newest tweet first, return when there is a first match of tweetID
                    long tweetID = tweet.getId();
                    String insertTweet = "@" + tweet.getUser().getScreenName() + " - " + tweet.getText();
                    System.out.println("[update Everything] received tweet : " + insertTweet);
                    boolean isPresent = false;
                    Iterator it = tweetIDs.iterator();
                    while (it.hasNext()) {
                        isPresent = it.next().toString().equalsIgnoreCase(tweetID + "");
                        if (isPresent) {
                            break;
                        }
                    }
                    if (isPresent) {

                        //update the cached date and time
                        PreparedStatement dt = con.prepareStatement("update TwitterProfiles set insertDate=? where screenName=?");
                        Calendar cal = Calendar.getInstance();
                        java.util.Date currentTime = cal.getTime();
                        dt.setString(1, currentTime + "");
                        dt.setString(2, username);
                        dt.execute();
                        dt = con.prepareStatement("update jWebProfile set cachedDate=? where twitterScreenName=?");
                        cal = Calendar.getInstance();
                        currentTime = cal.getTime();
                        dt.setString(1, currentTime + "");
                        dt.setString(2, username);
                        dt.execute();
                        return;
                    }

                    String tweetTime = tweet.getCreatedAt() + "";
                    int noOfRetweets = tweet.getRetweetCount();
                    int noOfLikes = tweet.getFavoriteCount();

                    PreparedStatement ps = con.prepareStatement("insert into TwitterTweets values (?,?,?,?,?,?,?,?,?)");
                    ps.setString(1, username);
                    ps.setString(2, insertTweet);
                    ps.setString(3, tweetTime);
                    ps.setInt(4, noOfRetweets);
                    ps.setInt(5, noOfLikes);
                    ps.setString(6, tweetID + "");
                    Calendar cal = Calendar.getInstance();
                    java.util.Date currentTime = cal.getTime();
                    ps.setString(7, currentTime + "");
                    ps.setString(8, tweet.isRetweet() + ""); /////////////
                    ps.setString(9, tweet.isPossiblySensitive() + "");
                    System.out.println("[Twitter Update: Executing the TwitterTweets update statement]");
                    ps.execute();
                    System.out.println("[Twitter Update: done!]");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public static void updateEverythingGithub(Connection con, String username) {
        /*
            put the code in try catch, 
            make the repoID as primary key, 
            try to insert the data first, 
            in catch, update the data.
            I hope you know what I am trying to say.
         */
        GitHubClient github = null;
        GithubSetupLogic githubSetupLogic = new GithubSetupLogic();
        try {
            PreparedStatement ps = con.prepareStatement("select * from GithubSession");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("Retriving content from GithubSession for profile update");
                github = githubSetupLogic.getGithubObj(rs.getString(2));
            }
            UserService userService = new UserService(github);
            org.eclipse.egit.github.core.User user = userService.getUser(username);
            ps = con.prepareStatement("update GithubProfiles set name=?,profilePhoto=?,email=?,blogUrl=?,noOfFollowers=?,followingCount=?,isHireable=?,company=?,noOfRepositories=?,insertDate=? where loginName=?");
            ps.setString(1, user.getName());

            InputStream imageStream = new URL(user.getAvatarUrl()).openStream();
            ps.setBinaryStream(2, imageStream);
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getBlog());
            ps.setInt(5, user.getFollowers());
            ps.setInt(6, user.getFollowing());
            ps.setString(7, user.isHireable() ? "Yes" : "No");
            ps.setString(8, user.getCompany());
            ps.setInt(9, user.getPublicRepos());
            Calendar cal = Calendar.getInstance();
            java.util.Date currentTime = cal.getTime();
            ps.setString(10, currentTime + "");
            ps.setString(11, username);
            ps.execute();
            System.out.println("Github basic profile info is updated!");

            try {
                long repoID = 0;
                RepositoryService rservice = new RepositoryService(github);
                for (Repository repo : rservice.getRepositories(user.getLogin())) {
                    try {
                        //try to insert the row. 
                        ps = con.prepareStatement("insert into GithubRepository values(?,?,?,?,?,?,?,?)");
                        ps.setString(1, user.getLogin());
                        ps.setString(2, repo.getName());
                        ps.setString(3, repo.getDescription());
                        ps.setString(4, repo.getLanguage());
                        ps.setString(5, repo.getCreatedAt() + "");
                        ps.setString(6, repo.getUpdatedAt() + "");
                        ps.setString(7, repo.isFork() ? "Yes" : "No");
                        ps.setString(8, repo.getId() + "");
                        repoID = repo.getId();
                        ps.execute();
                        System.out.println("GithubRepository table got 1 row inserted and is updated");
                    } catch (Exception e) {
                        //else try to update existing rows
                        try {
                            ps = con.prepareStatement("update GithubRepository set repositoryName=?,description=?,language=?,updated=? where repoID=?");
                            ps.setString(1, repo.getName());
                            ps.setString(2, repo.getDescription());
                            ps.setString(3, repo.getLanguage());
                            ps.setString(4, repo.getUpdatedAt() + "");
                            ps.setString(5, repoID + "");
                            ps.execute();
                            System.out.println(repo.getName() + " is updated %%%%%%%%%%%%^^^^^^^^^" + repo.getUpdatedAt());

                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
            

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void updateEverything(Connection con, String twitterUsername, String githubUsername, Twitter twitter) {
        if (DatabaseActivity.existingAccountsForGivenUsername(con, twitterUsername).equalsIgnoreCase("twitter")) {
            DatabaseActivity.updateEverythingTwitter(con, twitterUsername, twitter);
            System.out.println("$$$$$$$$$$$$$ $$$$$$$$$$$$$$$$$ TWITTER UPDATE DONE");
        } else if (DatabaseActivity.existingAccountsForGivenUsername(con, githubUsername).equalsIgnoreCase("github")) {
            DatabaseActivity.updateEverythingGithub(con, githubUsername);
            System.out.println("$$$$$$$$$$$$$ $$$$$$$$$$$$$$$$$ GITHUB UPDATE DONE");
        } else {
            DatabaseActivity.updateEverythingTwitter(con, twitterUsername, twitter);
            DatabaseActivity.updateEverythingGithub(con, githubUsername);
            System.out.println("$$$$$$$$$$$$$ $$$$$$$$$$$$$$$$$$$$ TWITTER AND GITHUB UPDATES DONE");
        }
    }

    public static void exportTweets(Connection con, String name, String username, String path) throws Exception {
        Calendar cal = Calendar.getInstance();
        java.util.Date currentTime = cal.getTime();
        String curTime = currentTime + "";
        curTime = curTime.replace(' ', '_');
        name = name.replace(' ', '_');
        curTime=curTime.replace(':','_');
        Path os_p=Paths.get(path + File.separator + name + "_tweets_" + curTime + ".csv");
        System.out.println(os_p.toAbsolutePath().toString());
        CSVWriter writer = new CSVWriter(new FileWriter(os_p.toAbsolutePath().toString()), ',');
        //String sql="select TwitterTweets.tweetID,TwitterTweets.tweet,TwitterTweets.tweetTime,TwitterTweets.noOfRetweets,TwitterTweets.noOfLikes,TwitterTweets.snapshotTime from TwitterTweets,TwitterAllTweets where TwitterTweets.tweetID=TwitterAllTweets.tweetID and TwitterTweets.screenName=? UNION select TwitterAllTweets.tweetID,TwitterAllTweets.tweet,TwitterAllTweets.tweetTime,TwitterAllTweets.noOfRetweets,TwitterAllTweets.noOfLikes,TwitterAllTweets.snapshotTime from TwitterTweets,TwitterAllTweets where TwitterTweets.tweetID!=TwitterAllTweets.tweetID and TwitterAllTweets.screenName=?";
        String sql = "select tweet,tweetTime,noOfRetweets,noOfLikes,isRetweet,isSensitive,snapshotTime from TwitterTweets where screenName=?"
                + "UNION select tweet,tweetTime,noOfRetweets,noOfLikes,isRetweet,isSensitive,snapshotTime from TwitterAllTweets where screenName=? and tweetID not in (select tweetID from TwitterTweets where screenName=?)";
        PreparedStatement ps = con.prepareStatement(sql);

        //"select tweetID,tweet,tweetTime,noOfRetweets,noOfLikes,snapshotTime from TwitterTweets where screenName=? UNION select tweetID,tweet,tweetTime,noOfRetweets,noOfLikes,snapshotTime from TwitterAllTweets where screenName=?"
        ps.setString(1, username);
        ps.setString(2, username);
        ps.setString(3, username);

        ResultSet rs = ps.executeQuery();
        writer.writeAll(rs, true);
        writer.close();

    }

    public static void insertScrapedData(Connection con, String twitterUsername, String data) {
        try {
            PreparedStatement ps = con.prepareStatement("insert into ScrapedData values(?,?)");
            ps.setString(1, twitterUsername);
            ps.setString(2, data);
            ps.execute();
            System.out.println("Scraped data inserted");
        } catch (Exception e) {
            try {
                System.out.println("$$$$$ sssssssssSSSSSSSSS " + e);
                PreparedStatement ps = con.prepareStatement("update ScrapedData set scrapedData=? where twitterScreenName=?");
                ps.setString(1, data);
                ps.setString(2, twitterUsername);
                ps.execute();
                System.out.println("Scraped data updated");
            } catch (Exception ex) {

                System.out.println(ex);
            }
        }
    }

    public static String getScrapedData(Connection con, String twitterUsername) {
        try {
            String data = "";
            PreparedStatement ps = con.prepareStatement("select scrapedData from ScrapedData where twitterScreenName=?");
            ps.setString(1, twitterUsername);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                data = rs.getString(1);
            }
            return data;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static void insertLinkedInData(Connection con, String username, LinkedInData ld) {
        //continue...
        try {
            PreparedStatement ps = con.prepareStatement("insert into LinkedInData values (?,?,?,?,?,?)");
            ps.setString(1, username);
            ps.setString(2, ld.name);
            ps.setString(3, ld.description);
            ps.setString(4, ld.location);
            ps.setString(5, ld.organization);
            ps.setString(6, ld.role);
            ps.execute();
        } catch (Exception e) {
            try {
                PreparedStatement ps = con.prepareStatement("update LinkedInData set name=?,description=?,location=?,organization=?,role=? where twitterScreenName=?");
                ps.setString(6, username);
                ps.setString(1, ld.name);
                ps.setString(2, ld.description);
                ps.setString(3, ld.location);
                ps.setString(4, ld.organization);
                ps.setString(5, ld.role);
                ps.execute();
            } catch (Exception ex) {
                System.out.println(ex);
            }
            System.out.println(e);
        }
    }
    
    public static String getLinkedInData(Connection con,String username){
        try{
            String name=null;
            String desc=null;
            String loc=null;
            String org=null,role=null;
            PreparedStatement ps=con.prepareStatement("select name,description,location,organization,role from LinkedInData where twitterScreenName=?");
            ps.setString(1, username);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                name=rs.getString(1);
                desc=rs.getString(2);
                loc=rs.getString(3);
                org=rs.getString(4);
                role=rs.getString(5);
            }
            if(name==null){
                return null;
            }
            return "Name:   "+name+"\n"
                    +"Description:  "+desc+"\n"
                    +"Location: "+loc+"\n"
                    +"Organization: "+org+"\n"
                    +"Role: "+role;
        }catch(Exception e){
            System.out.println(e);
            return null;
        }
    }
}
