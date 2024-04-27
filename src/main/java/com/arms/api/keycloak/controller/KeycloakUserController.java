package com.arms.api.keycloak.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

import java.util.List;

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
