package main.actions;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by tim on 9/17/16.
 */
public class Tracker implements Runnable {
    private static final int THREASHOLD = 10;

    private RequestCreator requestCreator;
    private Integer initial;
    private int userId = 0;

    public Tracker() {
        try {
            userId = TheWalkingDevAPI.getUserByName("tim").getInt("id");
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        requestCreator = new RequestCreator();
        initial = requestCreator.sendRequest();
        System.out.println("Initial, got" + initial);
    }

    @Override
    public void run() {
        JFrame frame = displayLock();

        TrackerSchedular.scheduleAtFixedRate((Runnable) () -> {
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
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                });
            }
        }, 0, 2, TimeUnit.SECONDS);
    }

    private JFrame displayLock() {
        JLabel label = new JLabel("Take a break - Walk a few steps, then it'll unlock itself");
        JFrame frame = new JFrame();
        frame.setLayout(new GridBagLayout());
        frame.add(label);
        frame.setSize(500, 500);
        frame.setResizable(false);
        centreWindow(frame);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setVisible(true);
        frame.setUndecorated(true);
        frame.toFront();
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
}
