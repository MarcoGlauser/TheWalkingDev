package org.jetbrains.tutorials.actions;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;

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
        System.out.println("asdf");
        //TODO add startup Code

    }

    // Disposes system resources.
    public void disposeComponent() {
    }
}