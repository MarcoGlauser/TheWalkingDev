package main.actions;

import com.intellij.ide.util.PropertiesComponent;

/**
 * Created by tim on 9/17/16.
 */

// TODO: add google authtoken to retrieve access token
public final class UserSettings {
    private static PropertiesComponent pc = PropertiesComponent.getInstance();
    private static final String USER_ID = "TheWalkingDev-userid";

    public static int getUserId() {
        return pc.getInt(USER_ID, 0);
    }

    public static void setUserId(int userId) {
        pc.setValue(USER_ID, userId, 0);
    }
}
