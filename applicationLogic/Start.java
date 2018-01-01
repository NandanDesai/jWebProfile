/*
 * Author: Nandan Desai
 * Year: 2017
 */
package applicationLogic;

import com.alee.laf.WebLookAndFeel;
import java.awt.FontFormatException;
import java.net.URL;
import java.sql.*;
import javax.swing.ImageIcon;
import org.jachievement.Achievement;
import org.jachievement.AchievementConfig;
import org.jachievement.AchievementPosition;
import org.jachievement.AchievementQueue;
import twitter4j.Twitter;
import userInterface.SetupAccountsUI;
import userInterface.SplashScreen;

/**
 *
 * @author nandan
 */
public class Start {

    private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String JDBC_URL = "jdbc:derby:webprofileDB;create=true";
    Twitter twitter;

    Start() {
    }

    void notification() {
        try {

            URL iconURL = getClass().getResource("/userInterface/Resources/258.gif");
            ImageIcon icon = new ImageIcon(iconURL);
            AchievementConfig config = new AchievementConfig();
            config.setIcon(icon);
            config.setDuration(3000);
            config.setBorderThickness(0);
            config.setAudioEnabled(false);
            config.setDescriptionFont(new java.awt.Font("Dialog", 0, 12));
            //config.setTitleFont(new java.awt.Font("Dialog", 0, 18));
            //config.setDistanceFromScreen(10);
            config.setAchievementPosition(AchievementPosition.BOTTOM_RIGHT);
            Achievement achievement = new Achievement("", "jWebProfile is getting configured. Please wait...", config);
            AchievementQueue queue = new AchievementQueue();
            queue.add(achievement);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static void main(String args[]) throws FontFormatException {
        try {

            SplashScreen splashScreen = new SplashScreen();
            Start s = new Start();
            s.notification();
            Class.forName(DRIVER);
            Connection con = DriverManager.getConnection(JDBC_URL);
            //DatabaseActivity.deleteAllTables(con);

            DatabaseActivity.dbCheck(con);
            WebLookAndFeel.install();
            HelperClass helper = new HelperClass(con, splashScreen);
            if (!DatabaseActivity.checkSessionsTable(con)) {  //dbCh returns True if it is the first time
                //calling SetupUI
                java.awt.EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        javax.swing.JFrame Setupui = new SetupAccountsUI(con, splashScreen);
                        Setupui.setLocationRelativeTo(null);
                        Setupui.setVisible(true);
                        //splashScreen.setVisible(false);
                    }
                });
            } else {
                System.out.println("Entering else");
                try {
                    helper.startHomePageUI();
                    //splashScreen.setVisible(false);
                } catch (Exception e) {
                    System.out.println(e);
                }

                // DatabaseActivity.TESTcleanAllTables(con);
                DatabaseActivity.TESTshowAllTableRecords(con);

            }

            //splashScreen.setVisible(false);
            /*
                    if(rs.next()){
                        new TwitterActivity(new AccessToken(rs.getString(2),rs.getString(3)),rs.getString(1));
                    }
             */
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }
    }
}
