package com.indiabizforsale.email;


import com.google.cloud.datastore.*;
import org.slf4j.LoggerFactory;


public class ConfigurationService {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ConfigurationService.class);

    private String accessKeyId;
    private String secretAccessKey;

    private void getCredentials() {

        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

        StructuredQuery<Entity> query = Query.newEntityQueryBuilder()
                .setKind("credential")
                .setFilter(StructuredQuery.PropertyFilter.eq("Type", "AwsSesCredential"))
                .build();

        QueryResults<Entity> queryResults = datastore.run(query);
        Entity credential = queryResults.next();
        accessKeyId = credential.getString("AccessKeyId");
        secretAccessKey = credential.getString("SecretAccessKey");
    }

    public String getAccessKey() {
        getCredentials();
        logger.info(accessKeyId);
        return accessKeyId;

    }

    public String getSecretKey() {
        getCredentials();
        logger.info(secretAccessKey);
        if(secretAccessKey!=null)
        return secretAccessKey;
        else
            return "Secret key is null";
    }
}