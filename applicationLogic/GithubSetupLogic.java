/*
 * Author: Nandan Desai
 * Year: 2017
 */
package applicationLogic;

import com.github.scribejava.apis.GitHubApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;
import com.jcabi.github.Github;
import com.jcabi.github.RtGithub;
import java.util.Random;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.UserService;

/**
 *
 * @author nandan
 */
public class GithubSetupLogic {

    OAuth2AccessToken accessToken;
    final String clientId = "CLIENT ID";
    final String clientSecret = "CLIENT SECRET";
    final String secretState = "secret" + new Random().nextInt(999_999);
    final OAuth20Service service = new ServiceBuilder(clientId)
            .apiSecret(clientSecret)
            .state(secretState)
            .callback("https://jwebprofile.wordpress.com/login-success-page/")
            .build(GitHubApi.instance());

    public GithubSetupLogic() {
    }

    public String getAuthorizationURL() {
        return service.getAuthorizationUrl();
    }

    public String getGithubAccessToken(String code, String state) throws Exception {
        if (secretState.equals(state)) {
            System.out.println("State value does match!");
        } else {
            System.out.println("Github: State value does not match");
        }
        accessToken = service.getAccessToken(code);
        return accessToken.getAccessToken();
    }

    public String getGithubMyLoginName(String accessToken) throws Exception {
        return new UserService(getGithubObj(accessToken)).getUser().getLogin();
    }

    public Github getGithubSearchObj(String accessToken) {
        return new RtGithub(accessToken);
    }

    public GitHubClient getGithubObj(String accessToken) {
        GitHubClient client = new GitHubClient();
        return client.setOAuth2Token(accessToken);
    }

}
