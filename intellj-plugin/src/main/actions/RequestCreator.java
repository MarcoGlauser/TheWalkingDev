package main.actions;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.joda.time.DateTime;
import org.json.JSONException;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author Kirusanth Poopalasingam ( pkirusanth@gmail.com )
 */
public class RequestCreator {


    // private final Fitness service;
    private static String accessToken;

    public RequestCreator(String refreshToken) {
        Unirest.setHttpClient(unsafeHttpClient);
        refreshAccessToken(refreshToken);
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

    public Integer sendRequest(DateTime startTime) {
        Unirest.setHttpClient(unsafeHttpClient);

        System.out.println("sendRequest");
        DateTime endTime = DateTime.now().plusDays(1);
        System.out.println("after start end");

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
            System.out.println("Got json");
            int value = 0;
            try {
                System.out.println("get response");
                value = authorization.getBody()
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
            } catch (JSONException e) {
                return 0;
            }
            return value;
        } catch (UnirestException e) {
            return 0;
        }
    }

    private void refreshAccessToken(String refreshToken) {

        String clientId = "953621258938-te4scnmnukcj23m778hq2p2bhsbrf74t.apps.googleusercontent.com";
        String secret = "rh9WS0DnNujfrrwCXdb2JMKc";

        try {
            HttpResponse<JsonNode> stringHttpResponse = Unirest.post("https://www.googleapis.com/oauth2/v4/token")
                .header("content-type", "application/x-www-form-urlencoded")
                .body("client_secret=" + secret + "&grant_type=refresh_token&refresh_token=" + refreshToken + "&client_id=" + clientId)
                .asJson();

            accessToken = stringHttpResponse.getBody().getObject().getString("access_token");
            System.out.println(accessToken);
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
    }
}
