package org.jetbrains.tutorials.actions;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.fitness.Fitness;
import com.google.api.services.fitness.model.*;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.Collections;

/**
 * @author Kirusanth Poopalasingam ( pkirusanth@gmail.com )
 */
public class RequestCreator {

    private final Fitness service;

    public RequestCreator() {
        service = createService();
    }

    public Integer sendRequest() {
        try {
            return careless();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Integer careless() throws IOException {
        DateTime startTime = DateTime.now();
        DateTime endTime = DateTime.now().plusDays(1);
        Fitness.Users.Dataset.Aggregate aggregate = service.users().dataset().aggregate(
            "me",
            new AggregateRequest()
                .setStartTimeMillis(startTime.getMillis())
                .setEndTimeMillis(endTime.getMillis())
                .setAggregateBy(Collections.singletonList(
                    new AggregateBy()
                        .setDataSourceId("dataSourceId")
                        .setDataTypeName("derived:com.google.step_count.delta:com.google.android.gms:estimated_steps")
                ))
                .setBucketByTime(
                    new BucketByTime()
                        .setDurationMillis(endTime.minus(startTime.getMillis()).getMillis())
                )
        );
        AggregateResponse response = aggregate.execute();
        Integer value = getValueOrNul(response);
        return value;
    }

    @NotNull
    private static Fitness createService() {
        GoogleCredential.Builder builder = new GoogleCredential.Builder()
            .setJsonFactory(new JacksonFactory())
            .setTransport(new ApacheHttpTransport())
            .setClientSecrets(
                "953621258938-te4scnmnukcj23m778hq2p2bhsbrf74t.apps.googleusercontent.com",
                "HNSm0ENFLQHRpZzI3n_dz2XV"
            );

        Credential applicationDefault = builder.build();
        applicationDefault.setRefreshToken("NOi4AoGhMrrzEXBxM22vmDOm54fSgk67MtOusWQhiw8");
        applicationDefault.setAccessToken("ya29.Ci9gA-Z7xI-MFfRoXPwqze-BoroUA7Mp4B8fgli9788W-avzo-XOW7BXJ5Fl8qwWag");


        return new Fitness.Builder(
            Utils.getDefaultTransport(), Utils.getDefaultJsonFactory(), applicationDefault)
            .setApplicationName("953621258938-jd68gcvpihti7m4kl0c2u40u5lt2e2hk.apps.googleusercontent.com")
            .build();
    }

    private static Integer getValueOrNul(AggregateResponse response) {
        for (AggregateBucket aggregateBucket : response.getBucket()) {
            for (Dataset dataset : aggregateBucket.getDataset()) {
                for (DataPoint dataPoint : dataset.getPoint()) {
                    for (Value value : dataPoint.getValue()) {
                        return value.getIntVal();
                    }
                }
            }
        }
        return null;
    }
}
