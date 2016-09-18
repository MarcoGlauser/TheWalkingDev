package org.jetbrains.tutorials.actions;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.awt.RelativePoint;
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
        System.out.println("initComponent()");


    }

    // Disposes system resources.
    public void disposeComponent() {
        System.out.println("disposeComponent()");
    }
}