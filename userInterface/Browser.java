/*
 * Year: 2017.
 */
package userInterface;

/**
 *
 * @author nandan
 */
import applicationLogic.DatabaseActivity;
import applicationLogic.GithubSetupLogic;
import java.sql.Connection;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.swing.JFrame;

public class Browser {

    JFrame frame;
    private Label location;
    private Label authCode;
    private Label authState;
    private WebView webView;
    private WebEngine webEngine;
    private Button button;
    private String s;   //URL filtering variable. Used to extract code and state during Github setup
    private String code;    //Github setup response code
    private String state;   //Github server state verifier
    public boolean githubDone = false;
    public boolean twitterDone = false;
    //String urlToVisit;
    Connection con;
    GithubSetupLogic githubSetupLogic;

    void initAndShowGUI(String url, String account, Connection con, GithubSetupLogic githubSetupLogic) throws Exception {
        // This method is invoked on the EDT thread
        this.con = con;
        this.githubSetupLogic = githubSetupLogic;
        Platform.setImplicitExit(false);
        frame = new JFrame("jWebProfile Embedded Browser");
        final JFXPanel fxPanel = new JFXPanel();
        frame.add(fxPanel);
        frame.setSize(720, 690);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        Platform.setImplicitExit(false);
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                try {
                    initFX(fxPanel, url, account);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

    }

    private void initFX(JFXPanel fxPanel, String url, String account) {
        // This method is invoked on the JavaFX thread
        try {
            Platform.setImplicitExit(false);
            location = new Label();
            authCode = new Label();
            authState = new Label();
            webView = new WebView();
            button = new Button("Close");
            webEngine = webView.getEngine();
            //webEngine.load("https://github.com/login/oauth/authorize?response_type=code&client_id=088e2168041d4585e497&redirect_uri=https%3A%2F%2Fjwebprofile.wordpress.com%2Flogin-success-page%2F&state=secret898405");
            webEngine.load(url);
            /*
                The method below is used for github setup process only
             */
            button.setOnAction(new EventHandler<ActionEvent>() {

                public void handle(ActionEvent event) {
                    // Platform.exit();
                    Platform.setImplicitExit(false);
                    frame.dispose();
                    twitterDone = true;
                }
            });
            if (account.equalsIgnoreCase("github")) {
                githubSetup();
            }
            try {
                Scene scene = createScene();
                fxPanel.setScene(scene);
            } catch (Exception e) {
                System.out.println(e);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    private Scene createScene() throws Exception {
        Scene scene = new Scene(new VBox(10, button, location, authCode, authState, webView));
        return scene;
    }

    private void githubSetup() {
        location.textProperty().bind(webEngine.locationProperty());
        location.textProperty().addListener((observable, oldValue, newValue) -> {
            s = newValue;

            String token_identifier = "code=";
            if (s.contains("https://jwebprofile.wordpress.com/login-success-page/?code=")) {
                code = s.substring(s.lastIndexOf(token_identifier) + token_identifier.length(), s.lastIndexOf('&'));
                state = s.substring(s.lastIndexOf('=') + 1);
                //expirationTimeMillis = System.currentTimeMillis() + (Integer.parseInt(expires_in) * 1000);
                authCode.setText(code);
                setGithubCode(code);
                authState.setText(state);
                setGithubState(state);
                githubDone = true;
                try {

                    String accessToken = githubSetupLogic.getGithubAccessToken(code, state);
                    DatabaseActivity.githubSessionInsert(con, githubSetupLogic.getGithubMyLoginName(accessToken), accessToken);

                    System.out.println("Github session got set.");
                } catch (Exception e) {
                    System.out.println(e);
                }
                //send the code and state to Github Setup Logic page

            }
        });
    }

    public String getGithubState() {
        return state;
    }

    public void setGithubState(String state) {
        this.state = state;
        System.out.println("github state is set");
    }

    public String getGithubCode() {
        return code;
    }

    public void setGithubCode(String code) {
        this.code = code;
        System.out.println("github code is set");
    }
    /*
    public void openBrowser(String url,String account){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Browser browser=new Browser();
                browser.initAndShowGUI(url,account);
                
            }
        });
    }
     */

}
