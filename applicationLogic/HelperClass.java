/*
 * Author: Nandan Desai
 * Year: 2017
 */
package applicationLogic;

import com.alee.laf.WebLookAndFeel;

import com.jcabi.github.Github;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.ImageIcon;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.jachievement.Achievement;
import org.jachievement.AchievementConfig;
import org.jachievement.AchievementPosition;
import org.jachievement.AchievementQueue;
import twitter4j.auth.AccessToken;
import userInterface.HomePageUI;
import userInterface.SplashScreen;

/**
 *
 * @author nandan
 */
public class HelperClass {

    Connection con;
    TwitterActivity twitterActivity;
    GitHubClient github;
    Github githubSearch;
    GithubSetupLogic githubSetupLogic;
    String twitterScreenName; //mine
    String githubLoginName; //mine
    SplashScreen splash;

    public HelperClass(Connection con, SplashScreen splash) {
        this.con = con;
        this.splash = splash;

        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, this.getClass().getResourceAsStream("/userInterface/Resources/lato-thin.ttf")));
            System.out.println("lato thin font is registered.");
        } catch (IOException | FontFormatException e) {
            //Handle exception
            System.out.println("font exception: " + e);
        }

        githubSetupLogic = new GithubSetupLogic();
    }

    public void startHomePageUI() throws Exception {

        PreparedStatement ps = con.prepareStatement("select * from TwitterSession");
        ResultSet rs = ps.executeQuery();
        System.out.println("Prepared Statement executed");
        if (rs.next()) {
            System.out.println("Retriving content from TwitterSession");
            twitterScreenName = rs.getString(1);
            twitterActivity = new TwitterActivity(new AccessToken(rs.getString(2), rs.getString(3)), twitterScreenName);
        }
        //rs.close();
        ps = con.prepareStatement("select * from GithubSession");
        rs = ps.executeQuery();
        if (rs.next()) {
            System.out.println("Retriving content from GithubSession");
            githubLoginName = rs.getString(1);
            github = githubSetupLogic.getGithubObj(rs.getString(2));
            githubSearch = githubSetupLogic.getGithubSearchObj(rs.getString(2));
        }
        rs.close();
        try {
            //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            URL iconURL = getClass().getResource("/userInterface/Resources/497.gif");
            ImageIcon icon = new ImageIcon(iconURL);
            //Image image = icon.getImage() ; 
            //Image newimg = image.getScaledInstance( 75, 75,  java.awt.Image.SCALE_SMOOTH ) ;  
            //icon = new ImageIcon( newimg );
            AchievementConfig config = new AchievementConfig();
            config.setIcon(icon);
            config.setDuration(8000);
            config.setBorderThickness(0);
            config.setDescriptionFont(new java.awt.Font("Dialog", 0, 12));
            config.setAudioEnabled(false);
            //config.setTitleFont(new java.awt.Font("Lato Thin", 0, 18));
            //config.setDistanceFromScreen(10);
            config.setAchievementPosition(AchievementPosition.BOTTOM_RIGHT);
            Achievement achievement = new Achievement("", "You are logged in as  <span style=\"color: blue;\">@" + twitterScreenName + "</span> on Twitter and as  <span style=\"color: blue;\">@" + githubLoginName + "</span> on Github", config);
            AchievementQueue queue = new AchievementQueue();
            queue.add(achievement);
        } catch (Exception e) {
            System.out.println(e);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    WebLookAndFeel.install();

                    javax.swing.JFrame homePageui = new HomePageUI(con, twitterActivity.twitter, twitterActivity.myScreenName, github, githubSearch, githubLoginName);
                    homePageui.setLocationRelativeTo(null);
                    URL iconURL = getClass().getResource("/userInterface/Resources/logo512.png");
                    ImageIcon icon = new ImageIcon(iconURL);
                    homePageui.setIconImage(icon.getImage());

                    homePageui.setVisible(true);
                    splash.setVisible(false);
                    splash.dispose();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });
    }
}
