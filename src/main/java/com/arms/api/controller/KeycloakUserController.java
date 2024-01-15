package com.arms.api.controller;

import com.arms.config.*;
import org.keycloak.admin.client.resource.UserProfileResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class KeycloakUserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/auth-check/getUsers/{userName}", method = RequestMethod.GET)
    @ResponseBody
    public List<UserRepresentation> getUsers(@PathVariable("userName") String userName) {

        logger.info("userName -> " + userName);
        List<UserRepresentation> userRepresentations = KeyCloakConfig.getKeycloakInstance().realm(KeyCloakConfig.realm).users().search(userName, 0, 1000, true);

        return userRepresentations;
    }

    @RequestMapping(value = "/auth-check/getUserProfile", method = RequestMethod.GET)
    @ResponseBody
    public UserProfileResource getUserProfile() {

        UserProfileResource userProfileResource = KeyCloakConfig.getKeycloakInstance().realm(KeyCloakConfig.realm).users().userProfile();

        return userProfileResource;
    }

}