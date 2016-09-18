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
    ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
    private int THREASHOLD = 10;

    @Override
    public void actionPerformed(AnActionEvent actionEvent) {
        System.out.println("actionPerformed");

        StatusBar statusBar = WindowManager.getInstance().getStatusBar(DataKeys.PROJECT.getData(actionEvent.getDataContext()));

        showMessage(statusBar, "Take a break in 10s");

        Callable<Object> objectCallable = () -> {
            System.out.println("After 10s");

            JLabel label = new JLabel("Take a break - Walk a few steps, then it'll unlock itself");

            JFrame yourFrame = new JFrame();
            yourFrame.setLayout(new GridBagLayout());
            yourFrame.add(label);

            yourFrame.setSize(500, 500);
            yourFrame.setResizable(false);
            centreWindow(yourFrame);

            RequestCreator requestCreator = new RequestCreator();
            System.out.println("Created RequestCreator()");
            Integer initial = requestCreator.sendRequest();
            System.out.println("Initial, got" + initial);

            int userId = TheWalkingDevAPI.getUserByName("tim").getInt("id");

            exec.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    Integer now = requestCreator.sendRequest();
                    System.out.println("Check again, got " + now);
                    Integer diff = now - initial;
                    if (diff > 0) {
                        try {
                            TheWalkingDevAPI.logSteps(userId, diff);
                        } catch (UnirestException e) {
                            e.printStackTrace();
                        }
                    }

                    if (diff >= THREASHOLD) {
                        SwingUtilities.invokeLater(() -> {
                            showMessage(statusBar, "Finished back, go back to to work. Be awesome.");
                        });
                    }
                }
            }, 0, 2, TimeUnit.SECONDS);

            yourFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            yourFrame.setVisible(true);
            yourFrame.setUndecorated(true);
            yourFrame.toFront();
            ;
            return null;
        };

        exec.schedule(objectCallable, 10, TimeUnit.SECONDS);
    }

    private void sendPushOrGoHome() {

        String appKey = "a7eqv3s8aomqh5hfwiziv94a69ww45";
        String userKey = "ufzb39246jvwofzdspvb5pefubjxjq";

        try {
            Unirest.post("https://api.pushover.net/1/messages.jso")
                .field("token", appKey)
                .field("user", userKey)
                .field("title", "Back to work")
                .field("message", "Hey buddy wanna take a break?")
                .asBinary();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
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

    public static void centreWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }
}
