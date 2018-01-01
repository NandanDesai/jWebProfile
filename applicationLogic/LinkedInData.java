/*
 * Author: Nandan Desai
 * Year: 2017
 */
package applicationLogic;

/**
 *
 * @author nandan
 */
public class LinkedInData {
    public String name;
    public String description;
    public String location;
    public String organization;
    public String role;
    public LinkedInData(){}
    public LinkedInData(String name, String description, String location, String organization, String role){
        this.name=name;
        this.description=description;
        this.location=location;
        this.organization=organization;
        this.role=role;
    }
}
