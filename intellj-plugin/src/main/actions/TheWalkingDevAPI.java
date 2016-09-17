package main.actions;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

/**
 * Created by tim on 9/17/16.
 */
public class TheWalkingDevAPI {
/* public static JSONObject getUserById(int userId) throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get("http://localhost:8000/users/users/" + userId + "?format=json")
                .header("accept", "application/json")
                .asJson();

        return response.getBody().getObject();
    }
*/
    public static JSONObject getUserByName(String username) throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.get("http://localhost:8000/users/users?format=json")
                .header("accept", "application/json")
                .queryString("username", username)
                .asJson();

        return response.getBody().getObject();
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
    public static void logSteps(int userId, int numberOfSteps) throws UnirestException {
        Unirest.post("http://localhost:8000/datapoints/step_diffs/?format=json")
                .header("accept", "application/json")
                .field("user", userId)
                .field("number_of_steps", numberOfSteps)
                .asJson();
    }
}
