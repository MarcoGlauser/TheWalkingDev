package main.actions;

import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

/**
 * Created by mglauser on 17.09.16.
 */
public class PluginRegistration implements ApplicationComponent {
    // Returns the component name (any unique string value).
    @NotNull
    public String getComponentName() {
        return "TheWalkingDev";
    }


    // this method is called on IDEA start-up.
    public void initComponent() {
        TrackerSchedular.scheduleAtFixedRate(new Tracker(), 1, 1, TimeUnit.HOURS);
    }

    // Disposes system resources.
    public void disposeComponent() {
        System.out.println("disposeComponent()");
    }
}