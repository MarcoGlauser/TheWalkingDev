package main.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.ui.awt.RelativePoint;

import javax.swing.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Kirusanth Poopalasingam ( pkirusanth@gmail.com )
 */
public class SimpleAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent actionEvent) {
        TrackerSchedular.schedule(new Tracker(), 1, TimeUnit.SECONDS);
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
