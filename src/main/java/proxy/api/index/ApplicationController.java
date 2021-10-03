package proxy.api.index;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.keycloak.KeycloakSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import proxy.api.config.security.Identity;

/**
 * @author <a href="mailto:psilva@redhat.com">Pedro Igor</a>
 */
@Controller
public class ApplicationController {

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "/protected", method = RequestMethod.GET)
    public String handleProtected(Model model) {
        configCommonAttributes(model);
        return "protected";
    }

    @RequestMapping(value = "/protected/premium", method = RequestMethod.GET)
    public String handlePremium(Model model) {
        configCommonAttributes(model);
        return "premium";
    }

    @RequestMapping(value = "/protected/alice", method = RequestMethod.GET)
    public String handleAliceResources(Model model) {
        configCommonAttributes(model);
        return "alice";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String handleLogoutt() throws ServletException {
        request.logout();
        return "redirect:http://192.168.25.46:8585/auth/realms/master/protocol/openid-connect/logout";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String handleHome(Model model) throws ServletException {
        configCommonAttributes(model);
        return "home";
    }

    @RequestMapping(value = "/accessDenied", method = RequestMethod.GET)
    public String handleAccessDenied() throws ServletException {
        return "access-denied";
    }

    private void configCommonAttributes(Model model) {
        model.addAttribute("identity", new Identity(getKeycloakSecurityContext()));
    }

    private KeycloakSecurityContext getKeycloakSecurityContext() {
        return (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
    }

    @Autowired
    @Qualifier("oAuth2RestTemplateByUser")
    OAuth2RestTemplate oAuth2RestTemplateByUser;

    @RequestMapping(value = "/auth-user/test", method = RequestMethod.GET)
    @ResponseBody
    public String getJavaServiceTreeFrameworkAuthUser() throws ServletException {
        String apiUrl = "http://www.313.co.kr/com/ext/jstree/springHibernate/core/getChildNode.do?c_id=2";
        String resultStr =  oAuth2RestTemplateByUser.getForObject(apiUrl, String.class);
        return resultStr;
    }

    @RequestMapping(value = "/auth-admin/test", method = RequestMethod.GET)
    @ResponseBody
    public String getJavaServiceTreeFrameworkAuthAdmin() throws ServletException {
        String apiUrl = "http://www.313.co.kr/com/ext/jstree/springHibernate/core/getChildNode.do?c_id=1";
        String resultStr =  oAuth2RestTemplateByUser.getForObject(apiUrl, String.class);
        return resultStr;
    }

}