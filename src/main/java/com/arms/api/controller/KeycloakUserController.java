package com.arms.api.controller;

import com.arms.config.*;

import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserProfileResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@AllArgsConstructor
@Slf4j
public class KeycloakUserController {

    private final RealmResource realmResource;
    @GetMapping("/auth-user/search-user/{userName}")
    @ResponseBody
    public Mono<List<UserRepresentation>> getUser(
        @PathVariable("userName") String userName
    ) {
        log.info("userName -> " + userName);
        List<UserRepresentation> userRepresentations
            = realmResource.users().search(userName, 0, 1000, true);
        return Mono.just(userRepresentations);
    }

    @GetMapping("/auth-user/users")
    @ResponseBody
    public Mono<List<UserRepresentation>> getUsers() {
        List<UserRepresentation> userRepresentations = realmResource.users().list();
        return Mono.just(userRepresentations);
    }


}
