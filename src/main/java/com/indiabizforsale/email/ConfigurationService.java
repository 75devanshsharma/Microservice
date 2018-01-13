package com.indiabizforsale.email;


import com.google.cloud.datastore.*;
import org.slf4j.LoggerFactory;

import static java.util.Optional.ofNullable;


public class ConfigurationService {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ConfigurationService.class);

    private String accessKeyId;
    private String secretAccessKey;

    private void getCredentials() {

        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

        StructuredQuery<Entity> query = Query.newEntityQueryBuilder()
                .setKind("credential")
                .setFilter(StructuredQuery.PropertyFilter.eq("type", "AwsSesCredential"))
                .build();

        QueryResults<Entity> queryResults = datastore.run(query);
        Entity credential = queryResults.next();
        accessKeyId = credential.getString("AccessKeyId");
        secretAccessKey = credential.getString("SecretAccessKey");
    }

    private String getAccessKey() {
        if (!ofNullable(accessKeyId).isPresent())
            getCredentials();
        logger.info(accessKeyId);
        return accessKeyId;

    }

    private String getSecretKey() {
        if (!ofNullable(secretAccessKey).isPresent())
            getCredentials();
        logger.info(secretAccessKey);
        return secretAccessKey;
    }

    public void setEmailCredentials() {
        System.setProperty("AwsAccessKey",getAccessKey());
        System.setProperty("AwsSecretKey", getSecretKey());
    }

}