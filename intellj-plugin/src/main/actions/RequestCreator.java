package main.actions;

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
        DateTime startTime = DateTime.now().minusDays(1);
        DateTime endTime = DateTime.now().plusDays(1);
        Fitness.Users.Dataset.Aggregate aggregate = service.users().dataset().aggregate(
            "me",
            new AggregateRequest()
                .setStartTimeMillis(startTime.getMillis())
                .setEndTimeMillis(endTime.getMillis())
                .setAggregateBy(Collections.singletonList(
                    new AggregateBy()
                        .setDataSourceId("derived:com.google.step_count.delta:com.google.android.gms:estimated_steps")
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
        applicationDefault.setRefreshToken("1/it4zEnXHD8CBJzecQtdI1fwezjjtARxUilQGZ8oAI-g");
        applicationDefault.setAccessToken("ya29.Ci9hAws7Eh9Fc_TU4MwRfZ1WscKOGMf_s0pR-48VUMNz33EbnkNtrJ3qjsbcedhjmQ");

        return new Fitness.Builder(
            Utils.getDefaultTransport(), Utils.getDefaultJsonFactory(), applicationDefault)
            .setApplicationName("953621258938-te4scnmnukcj23m778hq2p2bhsbrf74t.apps.googleusercontent.com")
            .build();
    }

    private static Integer getValueOrNul(AggregateResponse response) {
        System.out.println(response);
        for (AggregateBucket aggregateBucket : response.getBucket()) {
            for (Dataset dataset : aggregateBucket.getDataset()) {
                for (DataPoint dataPoint : dataset.getPoint()) {
                    for (Value value : dataPoint.getValue()) {
                        return value.getIntVal();
                    }
                }
            }
        }
        return 0;
    }

    public static void main(String[] args) {
        Integer integer = new RequestCreator().sendRequest();
        System.out.println(integer);
    }
}
