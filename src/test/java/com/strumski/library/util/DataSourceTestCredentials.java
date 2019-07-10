package com.strumski.library.util;

import org.springframework.context.annotation.Bean;

public class DataSourceTestConfig {

    @Bean(name = "daoPassword")
    String providesDaoPassword() {
        return "";
    }

    @Bean(name = "daoUsername")
    String providesDaoUsername() {
        return "";
    }

    /*public DataSource dataSourceForTest() {
        System.out.println("Initializing data source for test");
        return new EmbeddedDatabaseBuilder()
                //.generateUniqueName(true)
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding("UTF-8")
                .ignoreFailedDrops(true)
                .addScript("schema.sql")
                .addScripts("data.sql")
                .build();
}*/
}
