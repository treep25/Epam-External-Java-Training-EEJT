package com.epam.esm.commercetools.client;

import com.commercetools.api.client.ProjectApiRoot;
import com.commercetools.api.defaultconfig.ApiRootBuilder;
import com.commercetools.api.defaultconfig.ServiceRegion;
import io.vrap.rmf.base.client.oauth2.ClientCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.yaml")
public class CommerceToolsClient {

    private final String clientId;
    private final String clientSecret;
    private final String projectKey;

    @Autowired
    public CommerceToolsClient(@Value("${commerce.clientId}") String clientId,
                               @Value("${commerce.clientSecret}") String clientSecret,
                               @Value("${commerce.projectKey}") String projectKey) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.projectKey = projectKey;
    }

    @Bean
    public ProjectApiRoot projectApiRoot() {
        return ApiRootBuilder.of()
                .defaultClient(ClientCredentials.of()
                                .withClientId(clientId)
                                .withClientSecret(clientSecret)
                                .build(),
                        ServiceRegion.GCP_EUROPE_WEST1)
                .build(projectKey);
    }
}
