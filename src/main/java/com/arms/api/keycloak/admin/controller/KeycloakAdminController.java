package com.arms.api.keycloak.admin.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
@AllArgsConstructor
@Slf4j
public class KeycloakAdminController {

    private final Keycloak keycloak;
    @GetMapping("/auth-admin/realms/")
    @ResponseBody
    public Mono<List<RealmRepresentation>> getRealms(
        ServerWebExchange exchange
    ) {
        return Mono.just(keycloak.realms().findAll());
    }

    @GetMapping("/auth-admin/realm/{realm}")
    @ResponseBody
    public Mono<RealmRepresentation> getRealms(
            ServerWebExchange exchange,
            @PathVariable("realm") String realm
    ) {
        return Mono.just(
            keycloak.realm(realm).toRepresentation()
        );
    }

    @GetMapping("/auth-admin/realm/{realm}/clients")
    @ResponseBody
    public Mono<List<ClientRepresentation>>  getClients(
            ServerWebExchange exchange,
            @PathVariable("realm") String realm
    ) {
        return Mono.just(
            keycloak.realm(realm)
                .clients()
                .findAll()
        );
    }

    @GetMapping("/auth-admin/realm/{realm}/client-id/{clientId}")
    @ResponseBody
    public Mono<List<ClientRepresentation>> getClient(
        ServerWebExchange exchange,
        @PathVariable("realm") String realm,
        @PathVariable("clientId") String clientId
        ) {
        return Mono.just(
                keycloak.realm(realm)
                        .clients()
                        .findByClientId(clientId)
        );
    }

    @GetMapping("/auth-admin/realm/{realm}/groups")
    @ResponseBody
    public Mono<List<GroupRepresentation>> getGroups(
            ServerWebExchange exchange,
            @PathVariable("realm") String realm
            ) {

        return Mono.just(
                keycloak.realm(realm)
                    .groups().groups()
        );
    }

    @GetMapping("/auth-admin/realm/{realm}/group/{group}")
    @ResponseBody
    public Mono<GroupRepresentation> getGroup(
            ServerWebExchange exchange,
            @PathVariable("realm") String realm,
            @PathVariable("group") String group
    ) {

        return Mono.just(
                keycloak.realm(realm)
                        .groups()
                        .group(group).toRepresentation()
        );
    }

    @GetMapping("/auth-admin/realm/{realm}/roles")
    @ResponseBody
    public Mono<List<RoleRepresentation>> getRoles(
            ServerWebExchange exchange,
            @PathVariable("realm") String realm
    ) {

        return Mono.just(
                keycloak.realm(realm)
                        .roles().list()
        );
    }

    @GetMapping("/auth-admin/realm/{realm}/role/{role}")
    @ResponseBody
    public Mono<RoleRepresentation> getRole(
            ServerWebExchange exchange,
            @PathVariable("realm") String realm,
            @PathVariable("role") String role
    ) {

        return Mono.just(
                keycloak.realm(realm)
                        .roles()
                        .get(role).toRepresentation()
        );
    }

    @GetMapping("/auth-admin/realm/{realm}/users")
    @ResponseBody
    public Mono<List<UserRepresentation>>  getUsers(
            ServerWebExchange exchange,
            @PathVariable("realm") String realm
    ) {

        return Mono.just(
                keycloak.realm(realm)
                        .users().list()
        );
    }

    @GetMapping("/auth-admin/realm/{realm}/user/{user}")
    @ResponseBody
    public Mono<UserRepresentation> getUser(
            ServerWebExchange exchange,
            @PathVariable("realm") String realm,
            @PathVariable("user") String user
    ) {

        return Mono.just(
                keycloak.realm(realm)
                        .users()
                        .get(user).toRepresentation()
        );
    }




}
