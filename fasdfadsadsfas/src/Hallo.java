import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.apache.GoogleApacheHttpTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.util.Utils;
import com.google.api.services.fitness.Fitness;
import com.google.api.services.fitness.model.DataType;
import com.google.api.services.fitness.model.DataTypeField;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Kirusanth Poopalasingam ( pkirusanth@gmail.com )
 */
public class Hallo {
    public static void main(String[] args) throws IOException {


        Credential applicationDefault = GoogleCredential.getApplicationDefault();
        applicationDefault.setRefreshToken("NOi4AoGhMrrzEXBxM22vmDOm54fSgk67MtOusWQhiw8");
        applicationDefault.setAccessToken("ya29.Ci9gA-Z7xI-MFfRoXPwqze-BoroUA7Mp4B8fgli9788W-avzo-XOW7BXJ5Fl8qwWag");

        String DT_STEPS = "com.google.step_count.delta";
        DataTypeField FIELD_STEPS = new
            DataTypeField().setName("steps").setFormat("int");
        List<DataTypeField> STEPS_FIELDS = Arrays.asList(FIELD_STEPS);
        DataType DATA_TYPE_STEPS =
            new DataType()
                .setName(DT_STEPS)
                .setField(STEPS_FIELDS);

        Fitness service = new Fitness.Builder(
            Utils.getDefaultTransport(), Utils.getDefaultJsonFactory(), applicationDefault)
            .setApplicationName("953621258938-jd68gcvpihti7m4kl0c2u40u5lt2e2hk.apps.googleusercontent.com")
            .build();
        // service.users().dataSources().get(h)

    }
}
