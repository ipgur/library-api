/*
 * Copyright 2019 igur.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package library.configuration.secrets;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.function.BiConsumer;

@Component
public class AWSSecretManagerProvider implements SecretProvider{

    private final static Logger logger = LoggerFactory.getLogger(AWSSecretManagerProvider.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void fetchSecrets(List<String> keys, BiConsumer<String, String> keyCallback) {
        String region = "eu-central-1";

        // Create a Secrets Manager client
        AWSSecretsManager client  = AWSSecretsManagerClientBuilder.standard()
                .withRegion(region)
                .build();

        String secret = null;
        for (String key : keys) {
            GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
                    .withSecretId(key);
            GetSecretValueResult getSecretValueResult = null;
            try {
                getSecretValueResult = client.getSecretValue(getSecretValueRequest);

            } catch (ResourceNotFoundException e) {
                logger.info("The requested secret {} was not found", key);
            } catch (InvalidRequestException e) {
                logger.info("The request was invalid due to", e);
            } catch (InvalidParameterException e) {
                logger.info("The request has invalid parameters", e);
            }

            if (getSecretValueResult == null) {
                return;
            }

            // Depending on whether the secret was a string or binary, one of these fields will be populated
            if (getSecretValueResult.getSecretString() != null) {
                String jsonSecret = getSecretValueResult.getSecretString();
                try {
                    JsonNode actualObj = objectMapper.readTree(jsonSecret);
                    secret = actualObj.get(key).asText();
                } catch (IOException e) {
                    logger.error("Error while retrieving secrets from AWS Secret manager", e);
                }
            }

            keyCallback.accept(key, secret);
        }
    }
}
