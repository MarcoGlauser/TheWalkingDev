package main.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.awt.RelativePoint;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

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

    @Override
    public void actionPerformed(AnActionEvent actionEvent) {
        TrackerSchedular.schedule(new Tracker(), 0, TimeUnit.HOURS);
    }

    private void showMessage(StatusBar statusBar, String s) {
        SwingUtilities.invokeLater(() -> {
            JBPopupFactory.getInstance()
                    .createHtmlTextBalloonBuilder(s, MessageType.INFO, null)
                    .setFadeoutTime(7500)
                    .createBalloon()
                    .show(RelativePoint.getCenterOf(statusBar.getComponent()), Balloon.Position.atRight);
        });
    }
}
