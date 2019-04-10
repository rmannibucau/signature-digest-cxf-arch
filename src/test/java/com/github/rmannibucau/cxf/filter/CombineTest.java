/**
 * Copyright (C) 2006-2019 Talend Inc. - www.talend.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.rmannibucau.cxf.filter;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.apache.meecrowave.Meecrowave;
import org.apache.meecrowave.junit5.MeecrowaveConfig;
import org.apache.meecrowave.testing.ConfigurationInject;
import org.junit.jupiter.api.Test;

@MeecrowaveConfig
class CombineTest {
    @ConfigurationInject
    private Meecrowave.Builder config;

    @Test
    void run() throws NoSuchAlgorithmException {
        final Client client = ClientBuilder.newClient();
        try {
            final Response response = client.target("http://localhost:" + config.getHttpPort())
                  .path("test")
                  .request(TEXT_PLAIN_TYPE)
                  .post(entity("hello", TEXT_PLAIN_TYPE));
            assertEquals(HttpURLConnection.HTTP_OK, response.getStatus());
            assertEquals(getDigest(), response.getHeaderString("This-Is-The-Digest"));
            assertEquals("<hello>", response.readEntity(String.class));
        } finally {
            client.close();
        }
    }

    private String getDigest() throws NoSuchAlgorithmException {
        return Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-512").digest("hello".getBytes(
                StandardCharsets.UTF_8)));
    }
}
