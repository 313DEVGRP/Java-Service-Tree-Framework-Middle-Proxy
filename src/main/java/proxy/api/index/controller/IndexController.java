package proxy.api.index.controller;

import org.keycloak.KeycloakSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import proxy.api.config.security.Identity;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * @author <a href="mailto:psilva@redhat.com">Pedro Igor</a>
 */
@Controller
public class IndexController {

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
    public String handleHome(Model model) {
        configCommonAttributes(model);
        return "home";
    }

    @RequestMapping(value = "/accessDenied", method = RequestMethod.GET)
    public String handleAccessDenied() {
        return "access-denied";
    }

    private void configCommonAttributes(Model model) {
        model.addAttribute("identity", new Identity(getKeycloakSecurityContext()));
    }

    private KeycloakSecurityContext getKeycloakSecurityContext() {
        return (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
    }

    @Autowired
    @Qualifier("oauth2RestTemplateByUser")
    OAuth2RestTemplate oauth2RestTemplate;

    @RequestMapping(value = "/auth-user/test", method = RequestMethod.GET)
    @ResponseBody
    public String getJavaServiceTreeFrameworkAuthUser() {
        String apiUrl = "http://313.co.kr/com/ext/jstree/springHibernate/core/getChildNode.do?c_id=2";
        String resultStr = oauth2RestTemplate.getForObject(apiUrl, String.class);
        return resultStr;
    }

    @RequestMapping(value = "/auth-admin/test", method = RequestMethod.GET)
    @ResponseBody
    public String getJavaServiceTreeFrameworkAuthAdmin() {
        String apiUrl = "http://313.co.kr:7003/roles/sync";

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity request = new HttpEntity<>(headers);

        ResponseEntity<String> response = oauth2RestTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);
        return response.getBody();
    }

}
