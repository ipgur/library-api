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
package library.configuration;

import library.configuration.secrets.SecretProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Arrays;

/**
 * Bean to provide and configure data source
 */
@Configuration
public class ApiSecretsConfigBean {

    private final static Logger logger = LoggerFactory.getLogger(ApiSecretsConfigBean.class);

    // dependency injection of the secret provider
    @Autowired
    private SecretProvider secretProvider;

    // enables AWS secrets
    @Value("${api.secrets.aws.enabled:false}")
    private boolean enabled;

    // keygroup for the secrets
    @Value("${api.secrets.aws.keyGroup:prod_strumski}")
    private String keyGroup;

    // db password
    @Value("${spring.datasource.password}")
    private String mysqlPassword;

    // db username
    @Value("${spring.datasource.username}")
    private String mysqlUsername;

    @Bean(name = "mysqlPassword")
    String providesMysqlPassword() {
        return mysqlPassword;
    }

    @Bean(name = "mysqlUsername")
    String providesMysqlUsername() {
        return mysqlUsername;
    }

    /**
     * loads the secrets using the injected provider
     */
    @PostConstruct
    public void loadSecrets() {

        if (!enabled) {
            logger.info("AWS Secrets manager is disabled, continuing with values from xml file.");
            logger.info("Secrets: loading xml file.");
            return;
        }

        String[] keys = new String[] {
                keyGroup + "_prod_db_username",
                keyGroup + "_prod_db_password",
        };

        logger.info("AWS Secret Manager is enabled, fetching secrets...");
        secretProvider.fetchSecrets(Arrays.asList(keys), (key, value) -> {
            if (value == null || value.isEmpty()) {
                logger.info("secret of group/key {}/{} contained no value!", keyGroup, key);
                return;
            }
            logger.info("secret of group/key {}/{} value present.", keyGroup, key);
            if (key.endsWith("prod_db_username")) {
                mysqlUsername = value;
            } else if (key.endsWith("prod_db_password")) {
                mysqlPassword = value;
            } else {
                logger.info("secret of group/key {}/{} could not be assigned to any found variable!", keyGroup, key);
            }
        });

    }
}
