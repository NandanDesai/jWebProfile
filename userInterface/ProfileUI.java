/*
 * Year: 2017
 */
package userInterface;

import applicationLogic.DatabaseActivity;
import applicationLogic.JWebProfile;
import applicationLogic.TwitterActivity;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import org.jachievement.Achievement;
import org.jachievement.AchievementConfig;
import org.jachievement.AchievementPosition;
import org.jachievement.AchievementQueue;

/**
 *
 * @author nandan
 */
public class ProfileUI extends javax.swing.JFrame {

    /**
     * Creates new form ProfileUI
     */
    private javax.swing.JLabel jLabel;
    JWebProfile profile;
    Connection con;
    String username;
    SplashScreen splash;
    ArrayList<String> twitterInfoTitles;
    ArrayList<String> githubInfoTitles;
    ArrayList<String> twitterInfoValues;
    ArrayList<String> githubInfoValues;
    String cachedTime;

    public ProfileUI(Connection con, String username) {
        this.con = con;
        this.username = username;
        Calendar cal = Calendar.getInstance();
        Date currentTime = cal.getTime();
        twitterInfoTitles = new ArrayList<String>();
        twitterInfoTitles.add("     Twitter username: ");
        twitterInfoTitles.add("     Verified account?: ");
        twitterInfoTitles.add("     Description (as on Twitter): ");
        twitterInfoTitles.add("     Location: ");
        twitterInfoTitles.add("     Website: ");
        twitterInfoTitles.add("     No. of Twitter followers: ");
        twitterInfoTitles.add("     No. of people this person/account is following on Twitter: ");
        twitterInfoTitles.add("     Are you following this person on Twitter?: ");
        twitterInfoTitles.add("     Is this person following you on Twitter?: ");
        githubInfoTitles = new ArrayList<String>();
        githubInfoTitles.add("      Github username: ");
        githubInfoTitles.add("      Email: ");
        githubInfoTitles.add("      Blog URL: ");
        githubInfoTitles.add("      No. of Github followers: ");
        githubInfoTitles.add("      No. of people this person is following on Github: ");
        githubInfoTitles.add("      Is this person hireable?: ");
        githubInfoTitles.add("      Company this person is working for: ");
        githubInfoTitles.add("      No. of repositories of this person: ");
        initComponents();
//        jProgressBar1.setVisible(false);
        jLabel11.setVisible(false);
        jLabel13.setVisible(false);
        jLabel14.setVisible(false);
        try {
            cachedTime = DatabaseActivity.getCachedDate(con, username);
        } catch (Exception e) {
            System.out.println("Error getting CachedDate from database: " + e);
        }
        jLabel12.setText("<html>This jWebProfile was last updated on <b>" + cachedTime + "</b></html>");
        jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);
        jScrollPane2.getVerticalScrollBar().setUnitIncrement(16);
        jScrollPane3.getVerticalScrollBar().setUnitIncrement(16);
        jScrollPane4.getVerticalScrollBar().setUnitIncrement(16);
        try {
            System.out.println("Getting the profile object");
            System.out.println(":::::::::::::::::::::::existing accounts are: " + DatabaseActivity.existingAccountsForGivenUsername(con, username));
            this.profile = new JWebProfile(con, username);
            System.out.println("Going to check");
            if (DatabaseActivity.existingAccountsForGivenUsername(con, username).equalsIgnoreCase("twitter")) {
                twitterInfoValues = new ArrayList<String>();
                twitterInfoValues.add("@" + profile.TWITTERusername);
                twitterInfoValues.add(profile.TWITTERisVerified);
                String rawDesc = profile.TWITTERdescription;
                StringBuilder sb = new StringBuilder(rawDesc);
                int i = 0;
                while ((i = sb.indexOf(" ", i + 30)) != -1) {
                    sb.replace(i, i + 1, "\n");
                }
                rawDesc = sb.toString();
                //System.out.println("rawDesc::::::::::::::::"+rawDesc);
                String formattedDesc = rawDesc.replace("\n", "<br>");
                formattedDesc = "<html>" + formattedDesc + "</html>";

                twitterInfoValues.add(formattedDesc);
                twitterInfoValues.add(profile.TWITTERlocation);
                twitterInfoValues.add(profile.TWITTERwebsite);
                twitterInfoValues.add(profile.TWITTERnoOfFollowers + "");
                twitterInfoValues.add(profile.TWITTERfollowingCount + "");
                twitterInfoValues.add(profile.TWITTERyouFollowing);
                twitterInfoValues.add(profile.TWITTERyouFollowedby);
                jLabel3.setIcon(profile.TWITTERprofilePhoto);
                jLabel5.setVisible(false);
                jLabel6.setVisible(false);
                jLabel2.setText(profile.TWITTERname);
                settingBasicInformation(); //HERE IT IS >>>> STILL IN EXPERIMENT: setting only for those with twitter a/c
                insertTweets();
            } else if (DatabaseActivity.existingAccountsForGivenUsername(con, username).equalsIgnoreCase("github")) {
                githubInfoValues = new ArrayList<String>();
                githubInfoValues.add("@" + profile.GITHUBusername);
                githubInfoValues.add(profile.GITHUBemail);
                githubInfoValues.add(profile.GITHUBblogUrl);
                githubInfoValues.add(profile.GITHUBnoOfFollowers + "");
                githubInfoValues.add(profile.GITHUBfollowingCount + "");
                githubInfoValues.add(profile.GITHUBisHireable);
                githubInfoValues.add(profile.GITHUBcompany);
                githubInfoValues.add(profile.GITHUBnoOfRepositories + "");
                Image image = profile.GITHUBprofilePhoto.getImage();
                Image newimg = image.getScaledInstance(75, 75, java.awt.Image.SCALE_SMOOTH);
                profile.GITHUBprofilePhoto = new ImageIcon(newimg);
                jLabel3.setIcon(profile.GITHUBprofilePhoto);
                jLabel4.setText("Profile photo set on Github");
                jLabel5.setVisible(false);
                jLabel6.setVisible(false);
                //shit
                PreparedStatement ps = con.prepareStatement("select name from GithubProfiles where loginName=?");
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();
                rs.next();
                jLabel2.setText(rs.getString(1));
                settingBasicInformation();
                insertGithubRepositories();
                jButton1.setEnabled(false);
                jButton4.setEnabled(false);
                jButton5.setEnabled(false);
            } else {
                twitterInfoValues = new ArrayList<String>();
                twitterInfoValues.add("@" + profile.TWITTERusername);
                twitterInfoValues.add(profile.TWITTERisVerified);
                String rawDesc = profile.TWITTERdescription;
                StringBuilder sb = new StringBuilder(rawDesc);
                int i = 0;
                while ((i = sb.indexOf(" ", i + 30)) != -1) {
                    sb.replace(i, i + 1, "\n");
                }
                rawDesc = sb.toString();
                String formattedDesc = rawDesc.replace("\n", "<br>");
                formattedDesc = "<html>" + formattedDesc + "</html>";
                twitterInfoValues.add(formattedDesc);
                twitterInfoValues.add(profile.TWITTERlocation);
                twitterInfoValues.add(profile.TWITTERwebsite);
                twitterInfoValues.add(profile.TWITTERnoOfFollowers + "");
                twitterInfoValues.add(profile.TWITTERfollowingCount + "");
                twitterInfoValues.add(profile.TWITTERyouFollowing);
                twitterInfoValues.add(profile.TWITTERyouFollowedby);
                jLabel3.setIcon(profile.TWITTERprofilePhoto);
                githubInfoValues = new ArrayList<String>();
                githubInfoValues.add("@" + profile.GITHUBusername);
                githubInfoValues.add(profile.GITHUBemail);
                githubInfoValues.add(profile.GITHUBblogUrl);
                githubInfoValues.add(profile.GITHUBnoOfFollowers + "");
                githubInfoValues.add(profile.GITHUBfollowingCount + "");
                githubInfoValues.add(profile.GITHUBisHireable);
                githubInfoValues.add(profile.GITHUBcompany);
                githubInfoValues.add(profile.GITHUBnoOfRepositories + "");
                Image image = profile.GITHUBprofilePhoto.getImage();
                Image newimg = image.getScaledInstance(75, 75, java.awt.Image.SCALE_SMOOTH);
                profile.GITHUBprofilePhoto = new ImageIcon(newimg);
                jLabel5.setIcon(profile.GITHUBprofilePhoto);
                jLabel2.setText(profile.TWITTERname);
                settingBasicInformation();
                insertTweets();
                insertGithubRepositories();

            }

            int numOfArchTweets = DatabaseActivity.getNoOfArchivedTweets(con, profile.TWITTERusername);
            jLabel15.setText(numOfArchTweets + " tweets have been archived for this profile.");
            int numOfCurTweets = DatabaseActivity.getNoOfCurrentTweets(con, profile.TWITTERusername);
            jLabel16.setText(numOfCurTweets + " tweets are being displayed to you.");
            String scrapedData = DatabaseActivity.getScrapedData(con, username);

            jTextArea1.setText(scrapedData);
            jTextArea1.setEditable(false);
            jButton7.setEnabled(false);
            jTextArea2.setText(DatabaseActivity.getLinkedInData(con, username));
            jTextArea2.setEditable(false);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Profile");
        setResizable(false);

        jScrollPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jPanel2.setBackground(new java.awt.Color(255, 204, 204));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Lato Thin", 0, 14)); // NOI18N
        jLabel1.setText("jWebProfile");

        jLabel2.setFont(new java.awt.Font("Lato Thin", 0, 36)); // NOI18N
        jLabel2.setText("Name");

        jLabel4.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel4.setText("Profile picture set on Twitter");

        jLabel6.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel6.setText("Profile picture set on Github");

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/userInterface/Resources/logo60.png"))); // NOI18N

        jLabel12.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel12.setText("You are viewing the cached data from sdfsdfsd");

        jLabel15.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        jLabel15.setText("0 tweets have been archived for this profile");

        jLabel16.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        jLabel16.setText("0 tweets are being displayed to you.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(239, 239, 239)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(192, 192, 192)
                        .addComponent(jLabel4)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(193, 193, 193)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(142, 142, 142)
                        .addComponent(jLabel6)))
                .addContainerGap(233, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(jLabel16)
                            .addComponent(jLabel15))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel2))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel10))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addGap(64, 64, 64)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)))
                .addGap(32, 32, 32))
        );

        jPanel2.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 920, -1));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new java.awt.GridLayout(0, 2, 5, 10));
        jScrollPane2.setViewportView(jPanel3);

        jPanel2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 383, 920, 267));

        jLabel7.setFont(new java.awt.Font("Lato Light", 0, 14)); // NOI18N
        jLabel7.setText("Basic information:");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 361, -1, -1));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new java.awt.GridLayout(0, 1, 5, 5));
        jScrollPane3.setViewportView(jPanel4);

        jPanel2.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 676, 920, 238));

        jLabel8.setFont(new java.awt.Font("Lato Light", 0, 14)); // NOI18N
        jLabel8.setText("Twitter Tweets:");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 653, -1, -1));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new java.awt.GridLayout(0, 1, 5, 0));
        jScrollPane4.setViewportView(jPanel5);

        jPanel2.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 943, 920, 246));

        jLabel9.setFont(new java.awt.Font("Lato Light", 0, 14)); // NOI18N
        jLabel9.setText("Github Repositories:");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 920, -1, -1));

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jButton1.setText("Analyse");
        jButton1.setToolTipText("You need to be connected to the internet.");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(643, 1219, -1, -1));

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jButton2.setText("Update");
        jButton2.setToolTipText("New tweets will be added and Github repos will be updated.");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(743, 1219, -1, -1));

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jButton3.setText("Delete");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(841, 1219, -1, -1));

        jLabel11.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel11.setText("This process may take some time. Please wait!");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(511, 1198, -1, -1));

        jLabel13.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel13.setText("jLabel13");
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(137, 655, -1, -1));

        jLabel14.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel14.setText("jLabel14");
        jPanel2.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 922, -1, -1));

        jButton4.setBackground(new java.awt.Color(255, 255, 255));
        jButton4.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jButton4.setText("Export tweets");
        jButton4.setToolTipText("Writes this user's tweets to a CSV file");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(511, 1219, -1, -1));

        jButton5.setBackground(new java.awt.Color(255, 255, 255));
        jButton5.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jButton5.setText("Get 3200 more tweets");
        jButton5.setToolTipText("Downloads 3200 latest tweets from this user. This number is due to the API limit.");
        jButton5.setFocusPainted(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(319, 1219, -1, -1));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setLayout(new java.awt.BorderLayout());

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane7.setViewportView(jTextArea1);

        jPanel7.add(jScrollPane7, java.awt.BorderLayout.CENTER);

        jScrollPane5.setViewportView(jPanel7);

        jPanel2.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1290, 920, 347));

        jLabel18.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel18.setText("Data from WebScraping");
        jPanel2.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 1269, -1, -1));

        jLabel19.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel19.setText("LinkedIn");
        jPanel2.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 1671, -1, -1));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setLayout(new java.awt.BorderLayout());

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane8.setViewportView(jTextArea2);

        jPanel6.add(jScrollPane8, java.awt.BorderLayout.CENTER);

        jScrollPane6.setViewportView(jPanel6);

        jPanel2.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 1698, 920, 97));

        jButton6.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jButton6.setText("Edit");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 1640, -1, 20));

        jButton7.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jButton7.setText("Save");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 1640, -1, 20));

        jScrollPane1.setViewportView(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 941, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 605, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    class Task extends SwingWorker<Void, Void> {

        /*
         * Main task. Executed in background thread.
         */
        //SplashScreen splash=new SplashScreen() ;
        @Override
        public Void doInBackground() {
            //splash=new SplashScreen(); 
            //         jProgressBar1.setVisible(true);
            ;
            //      jProgressBar1.setIndeterminate(true);
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    javax.swing.JFrame analysisFrame = new AnalysisFrame(con, profile.TWITTERusername, profile.TWITTERname);
                    analysisFrame.setLocationRelativeTo(null);
                    URL iconURL = getClass().getResource("/userInterface/Resources/logo512.png");
                    ImageIcon icon = new ImageIcon(iconURL);
                    analysisFrame.setIconImage(icon.getImage());
                    analysisFrame.setVisible(true);
                }
            });
            return null;
        }

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
//           jProgressBar1.setVisible(false);
            jLabel11.setVisible(false);
        }
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        //splash=new SplashScreen("Resources/loading.png");
        jLabel11.setVisible(true);
        new Task().execute();
    }//GEN-LAST:event_jButton1ActionPerformed

    class UpdateTask extends SwingWorker<Void, Void> {

        @Override
        public Void doInBackground() {
            jLabel11.setVisible(true);
            DatabaseActivity.updateEverything(con, profile.TWITTERusername, profile.GITHUBusername, TwitterActivity.getTwitterObject());
            return null;
        }

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {

            jLabel11.setVisible(false);
            JOptionPane.showMessageDialog(null, "This jWebProfile is now up-to-date! Please relaunch this profile window.");
            dispose();

        }
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:

        new UpdateTask().execute();

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        int opinion = JOptionPane.showConfirmDialog(null, "Warning! " + profile.TWITTERname + "'s data will be permanently deleted from your local disk.\n Are you sure you want to continue?");
        if (opinion == JOptionPane.YES_OPTION) {
            DatabaseActivity.deleteTwitterUser(con, profile.TWITTERusername);
            DatabaseActivity.deleteGithubUser(con, profile.GITHUBusername);
            JOptionPane.showMessageDialog(null, "This jWebProfile has been deleted.");
            dispose();

        }

    }//GEN-LAST:event_jButton3ActionPerformed

    class Task1 extends SwingWorker<Void, Void> {

        /*
         * Main task. Executed in background thread.
         */
        //SplashScreen splash=new SplashScreen() ;
        @Override
        public Void doInBackground() {
            jLabel11.setVisible(true);
            DatabaseActivity.insertAllTweets(con, TwitterActivity.getTwitterObject(), profile.TWITTERusername);
            return null;
        }

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {

            jLabel11.setVisible(false);
            JOptionPane.showMessageDialog(null, "All the tweets are downloaded and stored in the database. These will not be displayed to you.\nOnly the latest tweets are displayed in the profile window.\nYou need to reopen this profile window for the changes to take effect.");
            dispose();
        }
    }
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:

        /**
         * ************************
         */
        new Task1().execute();


    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        try {
            JFileChooser f = new JFileChooser();
            f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            f.setDialogTitle("Select a folder to export the files");
            f.showDialog(null, "Export tweets");
            String path = f.getSelectedFile() + "";
            System.out.println(path);
            DatabaseActivity.exportTweets(con, profile.TWITTERname, profile.TWITTERusername, path);
            JOptionPane.showMessageDialog(null, "Files have been exported to " + path);
        } catch (Exception e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Failed to export the tweets. Unexpected error occured :(");
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        jTextArea1.setEditable(true);
        jTextArea1.setFocusable(true);
        jButton7.setEnabled(true);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        jTextArea1.setEditable(false);
        jTextArea1.setFocusable(false);
        DatabaseActivity.insertScrapedData(con, username, jTextArea1.getText());
        JOptionPane.showMessageDialog(null, "Your inference from web scraping has been successfully saved in the database.");
    }//GEN-LAST:event_jButton7ActionPerformed

    /**
     * @param args the command line arguments
     */
    private void settingBasicInformation() {
        if (DatabaseActivity.existingAccountsForGivenUsername(con, username).equalsIgnoreCase("twitter")) {
            Iterator<String> it1 = twitterInfoTitles.iterator();
            Iterator<String> it2 = twitterInfoValues.iterator();
            while (it1.hasNext() && it2.hasNext()) {
                //jLabel= new private javax.swing.JLabel("");
                jLabel = new javax.swing.JLabel();
                jLabel.setText(it1.next());
                jLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
                jPanel3.add(jLabel);
                jLabel = new javax.swing.JLabel();
                jLabel.setText(it2.next());
                jLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
                try {
                    if (jLabel.getText().equals(profile.TWITTERwebsite)) {
                        jLabel.setForeground(Color.BLUE);
                        jLabel.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {

                                String myString = profile.TWITTERwebsite;
                                StringSelection stringSelection = new StringSelection(myString);
                                Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                                clpbrd.setContents(stringSelection, null);
                                try {
                                    //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                                    URL iconURL = getClass().getResource("/userInterface/Resources/496.gif");
                                    ImageIcon icon = new ImageIcon(iconURL);
                                    //Image image = icon.getImage() ; 
                                    //Image newimg = image.getScaledInstance( 75, 75,  java.awt.Image.SCALE_SMOOTH ) ;  
                                    //icon = new ImageIcon( newimg );
                                    AchievementConfig config = new AchievementConfig();
                                    config.setIcon(icon);
                                    config.setDuration(3000);
                                    config.setBorderThickness(0);
                                    config.setAudioEnabled(false);
                                    config.setDescriptionFont(new java.awt.Font("Dialog", 0, 12));
                                    //config.setTitleFont(new java.awt.Font("Dialog", 0, 18));
                                    //config.setDistanceFromScreen(10);
                                    config.setAchievementPosition(AchievementPosition.BOTTOM_RIGHT);
                                    Achievement achievement = new Achievement("", "Link is copied to clipboard", config);
                                    AchievementQueue queue = new AchievementQueue();
                                    queue.add(achievement);
                                } catch (Exception ex) {
                                    System.out.println(ex);
                                }
                            }

                        });
                    }
                } catch (Exception e) {

                }
                jPanel3.add(jLabel);
            }

        } else if (DatabaseActivity.existingAccountsForGivenUsername(con, username).equalsIgnoreCase("github")) {
            Iterator<String> it1 = githubInfoTitles.iterator();
            Iterator<String> it2 = githubInfoValues.iterator();
            while (it1.hasNext() && it2.hasNext()) {
                //jLabel= new private javax.swing.JLabel("");
                jLabel = new javax.swing.JLabel();
                jLabel.setText(it1.next());
                jLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
                jPanel3.add(jLabel);
                jLabel = new javax.swing.JLabel();
                jLabel.setText(it2.next());
                jLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
                try {
                    if (jLabel.getText().equals(profile.GITHUBblogUrl)) {
                        jLabel.setForeground(Color.BLUE);
                        jLabel.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseEntered(MouseEvent e) {
                                //jLabel.setCursor(new Cursor(Cursor.HAND));
                            }

                            @Override
                            public void mouseClicked(MouseEvent e) {

                                String myString = profile.GITHUBblogUrl;
                                StringSelection stringSelection = new StringSelection(myString);
                                Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                                clpbrd.setContents(stringSelection, null);
                                try {
                                    //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                                    URL iconURL = getClass().getResource("/userInterface/Resources/496.gif");
                                    ImageIcon icon = new ImageIcon(iconURL);
                                    //Image image = icon.getImage() ; 
                                    //Image newimg = image.getScaledInstance( 75, 75,  java.awt.Image.SCALE_SMOOTH ) ;  
                                    //icon = new ImageIcon( newimg );
                                    AchievementConfig config = new AchievementConfig();
                                    config.setIcon(icon);
                                    config.setDuration(3000);
                                    config.setBorderThickness(0);
                                    config.setAudioEnabled(false);
                                    config.setDescriptionFont(new java.awt.Font("Dialog", 0, 12));
                                    //config.setTitleFont(new java.awt.Font("Dialog", 0, 18));
                                    //config.setDistanceFromScreen(10);
                                    config.setAchievementPosition(AchievementPosition.BOTTOM_RIGHT);
                                    Achievement achievement = new Achievement("", "Link is copied to clipboard", config);
                                    AchievementQueue queue = new AchievementQueue();
                                    queue.add(achievement);
                                } catch (Exception ex) {
                                    System.out.println(ex);
                                }
                            }

                        });
                    }
                } catch (Exception e) {

                }
                jPanel3.add(jLabel);
            }
        } else if (DatabaseActivity.existingAccountsForGivenUsername(con, username).equalsIgnoreCase("both")) {
            Iterator<String> it1 = twitterInfoTitles.iterator();
            Iterator<String> it2 = twitterInfoValues.iterator();
            while (it1.hasNext() && it2.hasNext()) {
                //jLabel= new private javax.swing.JLabel("");
                jLabel = new javax.swing.JLabel();
                jLabel.setText(it1.next());
                jLabel.setFont(new Font("Dialog", Font.PLAIN, 14));

                jPanel3.add(jLabel);
                jLabel = new javax.swing.JLabel();
                jLabel.setText(it2.next());
                jLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
                try {
                    if (jLabel.getText().equals(profile.TWITTERwebsite)) {
                        jLabel.setForeground(Color.BLUE);
                        jLabel.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {

                                String myString = profile.TWITTERwebsite;
                                StringSelection stringSelection = new StringSelection(myString);
                                Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                                clpbrd.setContents(stringSelection, null);
                                try {
                                    //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                                    URL iconURL = getClass().getResource("/userInterface/Resources/496.gif");
                                    ImageIcon icon = new ImageIcon(iconURL);
                                    //Image image = icon.getImage() ; 
                                    //Image newimg = image.getScaledInstance( 75, 75,  java.awt.Image.SCALE_SMOOTH ) ;  
                                    //icon = new ImageIcon( newimg );
                                    AchievementConfig config = new AchievementConfig();
                                    config.setIcon(icon);
                                    config.setDuration(3000);
                                    config.setBorderThickness(0);
                                    config.setAudioEnabled(false);
                                    config.setDescriptionFont(new java.awt.Font("Dialog", 0, 12));
                                    //config.setTitleFont(new java.awt.Font("Dialog", 0, 18));
                                    //config.setDistanceFromScreen(10);
                                    config.setAchievementPosition(AchievementPosition.BOTTOM_RIGHT);
                                    Achievement achievement = new Achievement("", "Link is copied to clipboard", config);
                                    AchievementQueue queue = new AchievementQueue();
                                    queue.add(achievement);
                                } catch (Exception ex) {
                                    System.out.println(ex);
                                }
                            }

                        });
                    }
                } catch (Exception e) {
                }
                jPanel3.add(jLabel);
            }
            it1 = githubInfoTitles.iterator();
            it2 = githubInfoValues.iterator();
            while (it1.hasNext() && it2.hasNext()) {
                //jLabel= new private javax.swing.JLabel("");
                jLabel = new javax.swing.JLabel();
                jLabel.setText(it1.next());
                jLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
                jPanel3.add(jLabel);
                jLabel = new javax.swing.JLabel();
                jLabel.setText(it2.next());
                jLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
                try {
                    if (jLabel.getText().equals(profile.GITHUBblogUrl)) {
                        jLabel.setForeground(Color.BLUE);
                        jLabel.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {

                                String myString = profile.GITHUBblogUrl;
                                StringSelection stringSelection = new StringSelection(myString);
                                Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                                clpbrd.setContents(stringSelection, null);
                                try {
                                    //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                                    URL iconURL = getClass().getResource("/userInterface/Resources/496.gif");
                                    ImageIcon icon = new ImageIcon(iconURL);

                                    AchievementConfig config = new AchievementConfig();
                                    config.setIcon(icon);
                                    config.setDuration(3000);
                                    config.setBorderThickness(0);
                                    config.setAudioEnabled(false);
                                    config.setDescriptionFont(new java.awt.Font("Dialog", 0, 12));

                                    config.setAchievementPosition(AchievementPosition.BOTTOM_RIGHT);
                                    Achievement achievement = new Achievement("", "Link is copied to clipboard", config);
                                    AchievementQueue queue = new AchievementQueue();
                                    queue.add(achievement);
                                } catch (Exception ex) {
                                    System.out.println(ex);
                                }
                            }

                        });
                    }
                } catch (Exception e) {
                }
                jPanel3.add(jLabel);
            }
        }
    }

    private void insertTweets() throws SQLException, Exception {

        String date = DatabaseActivity.getTwitterInsertDate(con, profile.TWITTERusername);
        jLabel13.setText("(You are viewing the cached tweets from " + date + ")");
        jLabel13.setVisible(true);

        ArrayList<JWebProfile.TwitterTweet> tweets = profile.getTwitterTweets(con, profile.TWITTERusername);
        String twitterTweet;
        String tweetTime;
        int noOfRetweets;
        int noOfLikes;
        String tweetText;
        for (JWebProfile.TwitterTweet tweet : tweets) {
            try {
                StringBuilder sb = new StringBuilder(tweet.tweet);
                int i = 0;
                while ((i = sb.indexOf(" ", i + 70)) != -1) {
                    sb.replace(i, i + 1, "\n");
                }
                twitterTweet = sb.toString();
            } catch (Exception e) {
                System.out.println(e);
                twitterTweet = tweet.tweet;
            }
            //twitterTweet=tweet.tweet;
            tweetTime = tweet.tweetTime;
            noOfRetweets = tweet.noOfRetweets;
            noOfLikes = tweet.noOfLikes;

            tweetText = "&nbsp;&nbsp;&nbsp;&nbsp;<b>Tweet:</b> " + twitterTweet
                    + "<br>" + "&nbsp;&nbsp;&nbsp;&nbsp;<b>Time:</b> " + tweetTime
                    + "<br>" + "&nbsp;&nbsp;&nbsp;&nbsp;<b>Number of retweets:</b> " + noOfRetweets
                    + "<br>" + "&nbsp;&nbsp;&nbsp;&nbsp;<b>Number of likes:</b> " + noOfLikes;

            String formattedText = tweetText.replace("\n", "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            formattedText = "<html>" + formattedText + "<br><br>" + "</html>";
            jLabel = new javax.swing.JLabel();
            jLabel.setText(formattedText);
            jLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
            jPanel4.add(jLabel);
        }

    }

    private void insertGithubRepositories() throws Exception {
        String date = DatabaseActivity.getGithubInsertDate(con, profile.GITHUBusername);
        jLabel14.setText("(You are viewing the cached repo data from " + date + ")");
        jLabel14.setVisible(true);
        ArrayList<JWebProfile.GithubRepository> repos = profile.getGithubRepos(con, username);
        String repoName;
        String description;
        String language;
        String created;
        String updated;
        String isFork;
        String repoText;
        for (JWebProfile.GithubRepository repo : repos) {
            repoName = repo.repositoryName;
            try {
                StringBuilder sb = new StringBuilder(repo.description);
                int i = 0;
                while ((i = sb.indexOf(" ", i + 30)) != -1) {
                    sb.replace(i, i + 1, "\n");
                }
                description = sb.toString();
            } catch (Exception e) {
                System.out.println(e);
                description = repo.description;
            }
            language = repo.language;
            created = repo.created;
            updated = repo.updated;
            isFork = repo.isFork;
            repoText = "&nbsp;&nbsp;&nbsp;&nbsp;Name of the repository: " + repoName
                    + "<br>" + "&nbsp;&nbsp;&nbsp;&nbsp;Description: " + description
                    + "<br>" + "&nbsp;&nbsp;&nbsp;&nbsp;Language used: " + language
                    + "<br>" + "&nbsp;&nbsp;&nbsp;&nbsp;Created on: " + created
                    + "<br>" + "&nbsp;&nbsp;&nbsp;&nbsp;Last updated on: " + updated
                    + "<br>" + "&nbsp;&nbsp;&nbsp;&nbsp;Is this repo a fork?: " + isFork;
            String formattedText = repoText.replace("\n", "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            formattedText = "<html>" + formattedText + "<br><br>" + "</html>";
            jLabel = new javax.swing.JLabel();
            jLabel.setText(formattedText);
            jLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
            jPanel5.add(jLabel);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    // End of variables declaration//GEN-END:variables
}
