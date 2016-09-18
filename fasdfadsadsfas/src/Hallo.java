import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.apache.GoogleApacheHttpTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.fitness.Fitness;
import com.google.api.services.fitness.FitnessRequest;
import com.google.api.services.fitness.model.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Kirusanth Poopalasingam ( pkirusanth@gmail.com )
 */
public class Hallo {
    public static void main(String[] args) throws IOException {

        GoogleCredential.Builder builder = new GoogleCredential.Builder()
            .setJsonFactory(new JacksonFactory())
            .setTransport(new ApacheHttpTransport())
            .setClientSecrets(
                "",
                ""
            );

        Credential applicationDefault = builder.build();
        applicationDefault.setRefreshToken("NOi4AoGhMrrzEXBxM22vmDOm54fSgk67MtOusWQhiw8");
        applicationDefault.setAccessToken("ya29.Ci9gA-Z7xI-MFfRoXPwqze-BoroUA7Mp4B8fgli9788W-avzo-XOW7BXJ5Fl8qwWag");


        Fitness service = new Fitness.Builder(
            Utils.getDefaultTransport(), Utils.getDefaultJsonFactory(), applicationDefault)
            .setApplicationName("953621258938-jd68gcvpihti7m4kl0c2u40u5lt2e2hk.apps.googleusercontent.com")
            .build();

        Long startTime;
        Long endTime;
        service.users().dataset().aggregate(
            "me",
            new AggregateRequest()
                .setStartTimeMillis(startTime)
                .setEndTimeMillis(endTime)
                .setAggregateBy(
                    new AggregateBy()
                        .setDataSourceId("dataSourceId")
                        .setDataTypeName("derived:com.google.step_count.delta:com.google.android.gms:estimated_steps")
                )
                .setBucketByTime(
                    new BucketByTime()
                        .setDurationMillis(e)
                )
        )

    }
}
