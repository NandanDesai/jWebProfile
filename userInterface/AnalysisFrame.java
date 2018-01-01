/*
 * Year: 2017
 */
package userInterface;

import applicationLogic.DatabaseActivity;
import applicationLogic.GoogleSearchResults;
import applicationLogic.JWebProfileActivity;
import applicationLogic.LinkedInData;
import applicationLogic.ScrapeWebPage;
import applicationLogic.TwitterActivity;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingWorker;
import twitter4j.Twitter;

/**
 *
 * @author nandan
 */
public class AnalysisFrame extends javax.swing.JFrame {

    /**
     * Creates new form AnalysisFrame
     */
    String inference = ""; //final scraped text will be held by this variable
    String name;
    String screenName;
    Connection con;
    Twitter twitter;
    GoogleSearchResults result = new GoogleSearchResults();
    ArrayList<GoogleSearchResults> googleResults;
    ArrayList<LinkedInData> linkedinData;
    HashMap<String, ArrayList<Object>> hmGoogleSearchResults;
    HashMap<String, Object> hmLinkedInResults;

    public AnalysisFrame(Connection con, String screenName, String name) {
        this.con = con;
        this.screenName = screenName;
        this.name = name;
        initComponents();
        jLabel13.setVisible(false);
        hmGoogleSearchResults = new HashMap<String, ArrayList<Object>>();
        hmLinkedInResults = new HashMap<String, Object>();
        //testing this.

        twitter = TwitterActivity.getTwitterObject();
        jLabel1.setText("Analysis report for " + name);
        jTextField1.setVisible(false);
        jButton2.setVisible(false);
        //jProgressBar1.setVisible(true);
        jLabel10.setText("[Analysing " + name + "'s tweets. This may take some time.]");
        new Task1().execute();

    }

    class Task1 extends SwingWorker<Void, Void> {

        /*
         * Main task. Executed in background thread.
         */
        int error = 0;
        //SplashScreen splash=new SplashScreen() ;

        @Override
        public Void doInBackground() {
            //splash=new SplashScreen(); 

            try {

                LinkedInDisplay lid;
                WebScrapingResult wsr;
                JWebProfileActivity jwa = new JWebProfileActivity(con);
                List<String> mentions = jwa.getUserTwitterMentions(screenName);
                javax.swing.JLabel jLabel;
                Iterator it = mentions.iterator();
                while (it.hasNext()) {
                    jLabel = new javax.swing.JLabel();
                    String mention = it.next().toString();
                    System.out.println(mention);
                    //+" ("+twitter.showUser(mention).getName()+")"
                    jLabel.setText("@" + mention + " (" + twitter.showUser(mention).getName() + ")");
                    jPanel3.add(jLabel);
                }
                List<String> hashtags = jwa.getUserTwitterHashtags(screenName);
                it = hashtags.iterator();
                while (it.hasNext()) {
                    jLabel = new javax.swing.JLabel();
                    jLabel.setText("#" + it.next().toString());
                    jPanel4.add(jLabel);
                }
                googleResults = result.getGoogleSearchResults(name);
                linkedinData = result.getLinkedInData(name);
                ActionListener linkedinCheckbox = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Linkedin Checkbox ticked");
                        LinkedInData l = (LinkedInData) hmLinkedInResults.get(e.getActionCommand());

                        DatabaseActivity.insertLinkedInData(con, screenName, l);
                        //put the linkedin data into the database here

                    }
                };
                int i = 0;
                for (LinkedInData l : linkedinData) {
                    lid = new LinkedInDisplay(l);
                    lid.cb.addActionListener(linkedinCheckbox);
                    hmLinkedInResults.put(i + "", l);
                    lid.cb.setActionCommand(i + "");
                    jPanel10.add(lid);
                    i++;
                }

                ActionListener buttonListener;
                buttonListener = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("##########################RADIO BUTTON PRESSED");
                        //AnalysisFrame.inference = AnalysisFrame.inference + gsr.title; //test
                        GoogleSearchResults gsr = (GoogleSearchResults) hmGoogleSearchResults.get(e.getActionCommand()).toArray()[0];
                        String category = (String) hmGoogleSearchResults.get(e.getActionCommand()).toArray()[1];
                        System.out.println("Title: " + gsr.title);
                        System.out.println("category: " + category);
                        Task4 task = new Task4();
                        task.setTask4Attributes(gsr.link, category, name, gsr.snippet, gsr.title);
                        task.execute();

                    }
                };

                i = 0;
                ArrayList<Object> resultsAndCategory; //this arraylist will store the googleSearchResults object and a string 
                for (GoogleSearchResults gsr : googleResults) {
                    wsr = new WebScrapingResult(gsr);
                    resultsAndCategory = new ArrayList<Object>();
                    resultsAndCategory.add(gsr);
                    resultsAndCategory.add("News");
                    hmGoogleSearchResults.put(i + "", resultsAndCategory);
                    wsr.news.addActionListener(buttonListener);//test
                    wsr.news.setActionCommand(i + "");
                    i++;
                    resultsAndCategory = new ArrayList<Object>();
                    resultsAndCategory.add(gsr);
                    resultsAndCategory.add("Personal Blog");
                    hmGoogleSearchResults.put(i + "", resultsAndCategory);
                    wsr.blog.addActionListener(buttonListener);//test
                    wsr.blog.setActionCommand(i + "");
                    i++;
                    resultsAndCategory = new ArrayList<Object>();
                    resultsAndCategory.add(gsr);
                    resultsAndCategory.add("Article");
                    hmGoogleSearchResults.put(i + "", resultsAndCategory);
                    wsr.artPost.addActionListener(buttonListener);//test
                    wsr.artPost.setActionCommand(i + "");
                    i++;
                    resultsAndCategory = new ArrayList<Object>();
                    resultsAndCategory.add(gsr);
                    resultsAndCategory.add("Social media");
                    hmGoogleSearchResults.put(i + "", resultsAndCategory);
                    wsr.socialMedia.addActionListener(buttonListener);//test
                    wsr.socialMedia.setActionCommand(i + "");
                    i++;
                    resultsAndCategory = new ArrayList<Object>();
                    resultsAndCategory.add(gsr);
                    resultsAndCategory.add("Publication");
                    hmGoogleSearchResults.put(i + "", resultsAndCategory);
                    wsr.pub.addActionListener(buttonListener);//test
                    wsr.pub.setActionCommand(i + "");

                    //jTextArea1.setText(inference);
                    jPanel8.add(wsr);
                    i++;
                }

            } catch (Exception e) {
                System.out.println(e);
                error = 1;

            }
            return null;
        }

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            //jPanel3.repaint();

            if (error == 1) {
                jLabel11.setText("Connection problem :(");
                jLabel12.setText("Connection problem :(");
                jLabel15.setText("Connection problem :(");
                jLabel16.setText("Connection problem :(");
                jPanel3.revalidate();
                jPanel3.repaint();
                jPanel4.revalidate();
                jPanel4.repaint();
                jLabel10.setVisible(false);
                jPanel10.revalidate();
                jPanel10.repaint();
                jPanel8.revalidate();
                jPanel8.repaint();
            } else {
                jLabel11.setVisible(false);
                jLabel12.setVisible(false);
                jLabel15.setVisible(false);
                jLabel16.setVisible(false);
                jPanel3.revalidate();
                jPanel3.repaint();
                jPanel4.revalidate();
                jPanel4.repaint();
                //jProgressBar1.setVisible(false);
                jLabel10.setText("Note: The analysis is based on the saved data from the local database.");
                jPanel10.revalidate();
                jPanel10.repaint();
                jPanel8.revalidate();
                jPanel8.repaint();
            }
        }
    }

    class Task4 extends SwingWorker<Void, Void> {

        String link;
        String category;
        String name;
        String metadata;
        String title;

        //GoogleSearchResults gsr;
        void setTask4Attributes(String link, String category, String name, String metadata, String title) {
            this.link = link;
            this.category = category;
            this.name = name;
            this.metadata = metadata;
            this.title = title;
        }

        @Override
        public Void doInBackground() {
            jLabel13.setVisible(true);
            if (category.equalsIgnoreCase("Social media")) {
                inference = inference + title + "\n" + metadata + "\n\n";
                jTextArea1.setText(inference);
                inference = jTextArea1.getText();
                return null;
            }
            ScrapeWebPage swp = new ScrapeWebPage(link, category, name);
            try {
                inference = inference + swp.scrapeLinkAndGetText() + "\n\n";
                jTextArea1.setText(inference);
                inference = jTextArea1.getText();
            } catch (Exception ex) {
                Logger.getLogger(AnalysisFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }

        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {
            jLabel13.setVisible(false);
            jLabel14.setText("Current length of the inference text: " + jTextArea1.getText().length() + " characters. Max length: 32000");
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

        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jPanel10 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jPanel8 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel14 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Analysis");

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Lato Thin", 0, 36)); // NOI18N
        jLabel1.setText("Analysis");
        jPanel5.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, -1));

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel3.setText("Twitter mentions(@) and hashtags(#)");
        jPanel5.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, -1, -1));

        jPanel2.setBackground(new java.awt.Color(102, 204, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new java.awt.GridLayout(0, 1, 3, 0));

        jLabel11.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel11.setText("Please wait!");
        jPanel3.add(jLabel11);

        jScrollPane1.setViewportView(jPanel3);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 31, 330, 160));

        jLabel4.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel4.setText("Mentions(@)");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new java.awt.GridLayout(0, 1, 3, 3));

        jLabel12.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel12.setText("Please wait!");
        jPanel4.add(jLabel12);

        jScrollPane3.setViewportView(jPanel4);

        jPanel2.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 30, 380, 160));

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel5.setText("Hashtags(#)");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 10, -1, -1));

        jPanel5.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 890, 200));

        jLabel6.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel6.setText("Data from LinkedIn");
        jPanel5.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 350, -1, -1));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/userInterface/Resources/linkedin.png"))); // NOI18N
        jPanel5.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 340, -1, -1));

        jPanel6.setBackground(new java.awt.Color(204, 255, 204));

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setLayout(new java.awt.GridLayout(0, 1));
        jScrollPane5.setViewportView(jPanel10);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel5.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 380, 890, -1));

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jButton1.setText("?");
        jButton1.setToolTipText("If the name is not appropriate, click here and you can alter the name.");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 350, -1, 20));

        jTextField1.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        jPanel5.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 350, 180, 20));

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jButton2.setText("Submit");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 350, -1, 20));

        jLabel8.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel8.setText("Data extracted from Google");
        jPanel5.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 550, -1, -1));

        jPanel7.setBackground(new java.awt.Color(102, 255, 255));

        jPanel8.setBackground(new java.awt.Color(255, 204, 255));
        jPanel8.setLayout(new java.awt.GridLayout(0, 1));
        jScrollPane4.setViewportView(jPanel8);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel5.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 580, 890, 290));

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jButton3.setText("Save");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 1240, 140, 40));

        jLabel9.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel9.setText("Inference (auto-generated)");
        jPanel5.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 910, -1, -1));

        jPanel9.setBackground(new java.awt.Color(255, 204, 204));

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setAutoscrolls(false);
        jTextArea1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextArea1FocusGained(evt);
            }
        });
        jTextArea1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextArea1MouseClicked(evt);
            }
        });
        jTextArea1.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                jTextArea1InputMethodTextChanged(evt);
            }
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                jTextArea1CaretPositionChanged(evt);
            }
        });
        jTextArea1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jTextArea1PropertyChange(evt);
            }
        });
        jTextArea1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextArea1KeyTyped(evt);
            }
        });
        jScrollPane6.setViewportView(jTextArea1);

        jLabel14.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel14.setText("Length of the inference text:");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 866, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 769, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jLabel14)
                .addGap(5, 5, 5)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 930, 890, 290));

        jLabel10.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel10.setText("Analysing ");
        jPanel5.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 80, -1, -1));

        jLabel15.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel15.setText("Please wait!");
        jPanel5.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 350, -1, -1));

        jLabel16.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel16.setText("Please wait!");
        jPanel5.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 550, -1, -1));

        jLabel13.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel13.setText("Please wait... Scanning the link for relavant content");
        jPanel5.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 910, 330, -1));

        jButton4.setBackground(new java.awt.Color(255, 255, 255));
        jButton4.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jButton4.setText("Close");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 1240, 130, 40));

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        jPanel5.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 1270, 400, 40));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/userInterface/Resources/icons8-google-50.png"))); // NOI18N
        jPanel5.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 530, 50, 50));

        jScrollPane2.setViewportView(jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1006, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 669, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        jTextField1.setVisible(true);
        jButton2.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        this.name = jTextField1.getText();
        jPanel8.removeAll();
        jPanel10.removeAll();
        jLabel13.setVisible(false);
        hmGoogleSearchResults = new HashMap<String, ArrayList<Object>>();
        hmLinkedInResults = new HashMap<String, Object>();
        //testing this.

        twitter = TwitterActivity.getTwitterObject();
        jLabel1.setText("Analysis report for " + name);
        //jTextField1.setVisible(false);
        //jButton2.setVisible(false);
        //jProgressBar1.setVisible(true);
        jLabel15.setVisible(true);
        jLabel16.setVisible(true);
        jLabel10.setText("[Analysing " + name + "'s tweets. This may take some time.]");
        new Task1().execute();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        String data = jTextArea1.getText();
        if (data.length() > 32000) {
            JOptionPane.showMessageDialog(null, "Your inference text after web scraping cannot be more than 32,000 characters.\nYou can edit the inference text to make it within the specified limit.");
            return;
        }
        DatabaseActivity.insertScrapedData(con, screenName, data);
        JOptionPane.showMessageDialog(null, "Your inference from web scraping has been successfully saved in the database.");
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextArea1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextArea1FocusGained
        // TODO add your handling code here:
        jLabel14.setText("Current length of the inference text: " + jTextArea1.getText().length() + " characters. Max length: 32000");
    }//GEN-LAST:event_jTextArea1FocusGained

    private void jTextArea1CaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTextArea1CaretPositionChanged
        // TODO add your handling code here:
        jLabel14.setText("Current length of the inference text: " + jTextArea1.getText().length() + " characters. Max length: 32000");
    }//GEN-LAST:event_jTextArea1CaretPositionChanged

    private void jTextArea1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jTextArea1PropertyChange
        // TODO add your handling code here:
        jLabel14.setText("Current length of the inference text: " + jTextArea1.getText().length() + " characters. Max length: 32000");
    }//GEN-LAST:event_jTextArea1PropertyChange

    private void jTextArea1InputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTextArea1InputMethodTextChanged
        // TODO add your handling code here:
        jLabel14.setText("Current length of the inference text: " + jTextArea1.getText().length() + " characters. Max length: 32000");
    }//GEN-LAST:event_jTextArea1InputMethodTextChanged

    private void jTextArea1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea1KeyTyped
        // TODO add your handling code here:
        jLabel14.setText("Current length of the inference text: " + jTextArea1.getText().length() + " characters. Max length: 32000");
    }//GEN-LAST:event_jTextArea1KeyTyped

    private void jTextArea1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextArea1MouseClicked
        // TODO add your handling code here:
        jLabel14.setText("Current length of the inference text: " + jTextArea1.getText().length() + " characters. Max length: 32000");
    }//GEN-LAST:event_jTextArea1MouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}

class WebScrapingResult extends JPanel {

    JRadioButton news;
    JRadioButton blog;
    JRadioButton artPost;
    JRadioButton socialMedia;
    JRadioButton pub;
    ButtonGroup bg;

    WebScrapingResult(GoogleSearchResults result) {

        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setLayout(layout);
        this.setSize(new Dimension(887, 108));
        this.setBackground(Color.WHITE);
        java.awt.Font font = new java.awt.Font("Dialog", 0, 12);
        //this.setFont(new java.awt.Font("Dialog", 0, 12));
        JLabel title = new JLabel();
        title.setFont(font);
        title.setText("<html><b>Title:</b> " + result.title + "</html>");
        JLabel snippet = new JLabel("<html><b>Metadata:</b> " + result.snippet + "</html>");
        snippet.setFont(font);
        JLabel link = new JLabel("<html><b>Link:</b> " + result.link + "</html>");

        link.setFont(font);
        news = new JRadioButton("News");
        blog = new JRadioButton("Personal Blog");
        artPost = new JRadioButton("Article about this person");
        socialMedia = new JRadioButton("Social media");
        pub = new JRadioButton("Publication");
        bg = new ButtonGroup();
        bg.add(news);
        bg.add(blog);
        bg.add(artPost);
        bg.add(socialMedia);
        bg.add(pub);

        //JCheckBox cb = new JCheckBox("Allow recurence search of data from this site.");
        JLabel gap = new JLabel("<html><br><br></html>");

        this.add(title);
        this.add(snippet);
        this.add(link);
        this.add(news);
        this.add(blog);
        this.add(artPost);
        this.add(socialMedia);
        this.add(pub);
//        this.add(cb);
        this.add(gap);

        this.repaint();
    }
}

class LinkedInDisplay extends JPanel {

    JCheckBox cb;

    LinkedInDisplay(LinkedInData linkedin) {

        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setLayout(layout);
        this.setSize(new Dimension(887, 108));
        this.setBackground(Color.WHITE);
        java.awt.Font font = new java.awt.Font("Dialog", 0, 12);
        //this.setFont(new java.awt.Font("Dialog", 0, 12));
        JLabel name = new JLabel();
        name.setFont(font);
        name.setText("<html><b>Title:</b> " + linkedin.name + "</html>");
        JLabel description = new JLabel("<html><b>Metadata:</b> " + linkedin.description + "</html>");
        description.setFont(font);
        JLabel location = new JLabel("<html><b>Location:</b> " + linkedin.location + "</html>");
        location.setFont(font);
        JLabel org = new JLabel("<html><b>Organization:</b> " + linkedin.organization + "</html>");
        org.setFont(font);
        JLabel role = new JLabel("<html><b>Role:</b> " + linkedin.role + "</html>");
        role.setFont(font);
        cb = new JCheckBox("Select this linkedin Data");
        JLabel gap = new JLabel("<html><br><br></html>");

        this.add(name);
        this.add(description);
        this.add(location);
        this.add(org);
        this.add(role);
        this.add(cb);
        this.add(gap);
        this.repaint();

    }
}
