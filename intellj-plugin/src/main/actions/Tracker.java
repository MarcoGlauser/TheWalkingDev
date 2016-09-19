package main.actions;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.joda.time.DateTime;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by tim on 9/17/16.
 */
public class Tracker implements Runnable {
    private static final int THREASHOLD = 10;

    private RequestCreator requestCreator;
    private Integer initial;
    private int userId = 0;
    private final TheWalkingDevAPI.UserData userData;

    public Tracker() {
        userData = TheWalkingDevAPI.getUserIdByName("Kiru");
        userId = userData.id;
        requestCreator = new RequestCreator(userData.refreshToken);
    }

    @Override
    public void run() {
        System.out.println(new DateTime() + " Show message ");

        JLabel label = new JLabel("Take a break - Walk a few steps, then it'll unlock itself!");
        JFrame frame = displayLock(label);
        initial = requestCreator.sendRequest();
        System.out.println(new DateTime() + " Initial, got " + initial);

        AtomicBoolean isFinished = new AtomicBoolean(false);
        TrackerSchedular.scheduleAtFixedRate(() -> {
            if (!isFinished.get()) {
                System.out.println(new DateTime() + " Check again ");
                SwingUtilities.invokeLater(frame::toFront);

                Integer now = requestCreator.sendRequest();

                System.out.println(new DateTime() + " Check again, got " + now);
                Integer diff = now - initial;
                if (diff > 0) {
                    TheWalkingDevAPI.logSteps(userId, diff);
                }

                if (diff >= THREASHOLD) {
                    SwingUtilities.invokeLater(() -> {
                        isFinished.set(true);
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        label. setText("Well done! Code on.");
                        sendPushOrGoHome();
                    });
                }
            }
        }, 0, 2, TimeUnit.SECONDS);
    }

    private JFrame displayLock(JLabel label) {
        label.setFont(new Font("Arial", Font.BOLD, 20));
        JFrame frame = new JFrame();
        SwingUtilities.invokeLater(() -> {
            frame.setLayout(new GridBagLayout());
            frame.add(label);
            frame.setSize(500, 500);
            frame.setResizable(false);
            centreWindow(frame);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.setVisible(true);
            frame.setUndecorated(true);
            frame.toFront();
        });
        return frame;
    }

    private static void centreWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }

    private void sendPushOrGoHome() {
        String appKey = "a7eqv3s8aomqh5hfwiziv94a69ww45";

        try {
            Unirest.post("https://api.pushover.net/1/messages.jso")
                .field("token", appKey)
                .field("user", userData.pushToken)
                .field("title", "Back to work")
                .field("message", "Hey buddy wanna take a break?")
                .asBinary();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }
}
