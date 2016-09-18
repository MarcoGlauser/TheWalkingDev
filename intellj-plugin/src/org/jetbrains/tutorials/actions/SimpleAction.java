package org.jetbrains.tutorials.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.awt.RelativePoint;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Kirusanth Poopalasingam ( pkirusanth@gmail.com )
 */
public class SimpleAction extends AnAction {


    ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void actionPerformed(AnActionEvent actionEvent) {

        System.out.println("actionPerformed");

        StatusBar statusBar = WindowManager.getInstance()
            .getStatusBar(DataKeys.PROJECT.getData(actionEvent.getDataContext()));

        JBPopupFactory.getInstance()
            .createHtmlTextBalloonBuilder("Take a break in 30s", MessageType.INFO, null)
            .setFadeoutTime(7500)
            .createBalloon()
            .show(RelativePoint.getCenterOf(statusBar.getComponent()), Balloon.Position.atRight);

        Callable<Object> objectCallable = () -> {
            System.out.println("After 30s");

            JLabel label = new JLabel("Take a break");

            JFrame yourFrame = new JFrame();
            yourFrame.setLayout(new GridBagLayout());
            yourFrame.add(label);

            yourFrame.setVisible(true);
            yourFrame.setResizable(false);
            yourFrame.setExtendedState(yourFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
            yourFrame.setUndecorated(true);

            new WindowsSecurity(yourFrame);

            return null;
        };

        exec.schedule(objectCallable, 30, TimeUnit.SECONDS);
    }
}
