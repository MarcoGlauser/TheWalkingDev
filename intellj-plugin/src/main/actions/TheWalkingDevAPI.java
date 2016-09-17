package main.actions;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Created by tim on 9/17/16.
 */
public class TheWalkingDevAPI {

    // Response Is { "id": int, "username": string, "created_at": datetime, "updated_at: datetime }
    public static HttpResponse<JsonNode> registerUser(String username) throws UnirestException {
        return Unirest.post("http://localhost:8000/users/users")
                .header("accept", "application/json")
                .field("username", username)
                .asJson();
    }

    public static HttpResponse<JsonNode> logSteps(int userId, int numberOfSteps) throws UnirestException {
        return Unirest.post("http://localhost:8000/datapoints/step_diff")
                .header("accept", "application/json")
                .field("user", userId)
                .field("number_of_steps", numberOfSteps)
                .asJson();
    }
}
