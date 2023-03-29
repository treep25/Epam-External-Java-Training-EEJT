package com.epam.esm.commercetools.client;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.defaultconfig.ApiRootBuilder;
import com.commercetools.api.defaultconfig.ServiceRegion;
import io.vrap.rmf.base.client.oauth2.ClientCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class CommerceToolsClient {

    private final Environment environment;

    @Autowired
    public CommerceToolsClient(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public ProjectApiRoot projectApiRoot() {

        return ApiRootBuilder.of()
                .defaultClient(ClientCredentials.of()
                                .withClientId(environment.getProperty("commerce.clientId"))
                                .withClientSecret(environment.getProperty("commerce.clientSecret"))
                                .build(),
                        ServiceRegion.GCP_EUROPE_WEST1)
                .build("spring-commersetools");
    }
}
