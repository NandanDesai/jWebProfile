/*
 * Author: Nandan Desai
 * Year: 2017
 */
package applicationLogic;

import javax.swing.ImageIcon;

/**
 *
 * @author nandan
 */
public class SearchResult {
    public String twitterScreenName;
    public String githubLoginName;
    public ImageIcon photo;
    
    public String username;
    public SearchResult(String username,ImageIcon photo){
        
        this.username=username;
        this.photo=photo;
    }
    //use this for displaying the results of all jWebProfiles
    public SearchResult(String twitterScreenName,String githubLoginName){
        this.twitterScreenName=twitterScreenName;
        this.githubLoginName=githubLoginName;
    }
    
}
