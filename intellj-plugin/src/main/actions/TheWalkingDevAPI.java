package main.actions;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import org.json.JSONObject;
import org.omg.PortableServer.THREAD_POLICY_ID;

/**
 * Created by tim on 9/17/16.
 */
public class TheWalkingDevAPI {

    // public static final String BASE_URL = "http://172.31.5.34:8000";
    public static final String BASE_URL = "http://172.31.6.187:8000/";

    /* public static JSONObject getUserById(int userId) throws UnirestException {
                HttpResponse<JsonNode> response = Unirest.get("http://localhost:8000/users/users/" + userId + "?format=json")
                        .header("accept", "application/json")
                        .asJson();

                return response.getBody().getObject();
            }
        */

    static class UserData {
        public int id;
        public String refreshToken;
        public String pushToken;
    }

    public static UserData getUserIdByName(String username) {
        try {

            System.out.println("getUserIdByName");
            HttpRequest httpRequest = Unirest.get(BASE_URL + "/users/users?format=json")
                .header("accept", "application/json")
                .queryString("username", username);
            System.out.println(httpRequest.asString().getBody());
            HttpResponse<JsonNode> jsonRespones = httpRequest .asJson();

            JSONObject json = jsonRespones.getBody().getObject();
            UserData userData = new UserData();
            userData.id = json.getJSONArray("results").getJSONObject(0).getInt("id");
            userData.refreshToken = json.getJSONArray("results").getJSONObject(0).getString("refresh_token");
            userData.pushToken = json.getJSONArray("results").getJSONObject(0).getString("push_token");
            return userData;
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }

    }

    /*
        public static JSONObject registerUser(String username) throws UnirestException {
            HttpResponse<JsonNode> response = Unirest.post("http://localhost:8000/users/users/?format=json")
                    .header("accept", "application/json")
                    .field("username", username)
                    .asJson();

            return response.getBody().getObject();
        }
    */
    public static void logSteps(int userId, int numberOfSteps) {
        try {
            Unirest.post(BASE_URL + "/datapoints/step_diffs/?format=json")
                .header("accept", "application/json")
                .field("user", userId)
                .field("number_of_steps", numberOfSteps)
                .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }
}
