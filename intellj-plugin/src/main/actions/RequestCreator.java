package main.actions;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.fitness.Fitness;
import com.google.api.services.fitness.model.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;

/**
 * @author Kirusanth Poopalasingam ( pkirusanth@gmail.com )
 */
public class RequestCreator {

    private final Fitness service;
    private static String accessToken;

    public RequestCreator() {
        service = createService();
    }

    private static CloseableHttpClient unsafeHttpClient;

    static {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustSelfSignedStrategy() {
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();

            unsafeHttpClient = HttpClients.custom().setSslcontext(sslContext)
                .setSSLHostnameVerifier(new NoopHostnameVerifier()).build();

        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            e.printStackTrace();
        }
    }

    public static CloseableHttpClient getClient() {
        return unsafeHttpClient;
    }

    public Integer sendRequest() {
        Unirest.setHttpClient(unsafeHttpClient);

        DateTime startTime = DateTime.now().minusDays(1);
        DateTime endTime = DateTime.now().plusDays(1);

        try {
            HttpResponse<JsonNode> authorization = Unirest.post("https://www.googleapis.com/fitness/v1/users/me/dataset:aggregate")
                .header("Content-Type", "application/json;encoding=utf-8")
                .header("Authorization", "Bearer " + accessToken)
                .body("{\n" +
                    "  \"aggregateBy\": [{\n" +
                    "    \"dataTypeName\": \"com.google.step_count.delta\",\n" +
                    "    \"dataSourceId\": \"derived:com.google.step_count.delta:com.google.android.gms:estimated_steps\"\n" +
                    "  }],\n" +
                    "  \"bucketByTime\": { \"durationMillis\": " + endTime.minus(startTime.getMillis()).getMillis() + " },\n" +
                    "  \"startTimeMillis\": " + startTime.getMillis() + ",\n" +
                    "  \"endTimeMillis\": " + endTime.getMillis() + "\n" +
                    "}")
                .asJson();
            int value = authorization.getBody()
                .getObject()
                .getJSONArray("bucket")
                .getJSONObject(0)
                .getJSONArray("dataset")
                .getJSONObject(0)
                .getJSONArray("point")
                .getJSONObject(0)
                .getJSONArray("value")
                .getJSONObject(0)
                .getInt("intVal");
            return value;
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return 0;
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
                "rh9WS0DnNujfrrwCXdb2JMKc"
            );

        Credential applicationDefault = builder.build();
        applicationDefault.setRefreshToken("1/ocvAqbXjm-dnsMaX2agrne9L3HIxoPf3SCXqb3FtrfQ");

        try {
            applicationDefault.refreshToken();
        } catch (IOException e) {
            e.printStackTrace();
        }

        accessToken = applicationDefault.getAccessToken();

        return new Fitness.Builder(Utils.getDefaultTransport(), Utils.getDefaultJsonFactory(), applicationDefault)
            .setApplicationName("ItelliJ-Tool")
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
