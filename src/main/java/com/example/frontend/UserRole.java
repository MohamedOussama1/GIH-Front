package com.example.frontend;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

import java.util.Base64;

public class UserRole {

    String role = "normal";
    Client client = ClientBuilder.newClient();

    WebTarget target = client.target(Connextion_info.url);

    public String getRole(String name, String password) {

        String test = name + ":" + password;

        Response adminResponse = target
                .path("login/admin")
                .request()
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString(test.getBytes()))
                .get();

        Response normalRoleResponse = target
                .path("login/normal")
                .request()
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString(test.getBytes()))
                .get();

        Response allRoleResponse = target
                .path("login/all")
                .request()
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString(test.getBytes()))
                .get();

        if (adminResponse.getStatus() == 200) {
            role = "admin";
        } else if (normalRoleResponse.getStatus() == 200) {
            role = "normal";
        } else if (allRoleResponse.getStatus() == 200) {
            role = "public";
        }

        return role;
    }
}
