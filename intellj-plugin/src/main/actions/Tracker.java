package main.actions;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.joda.time.DateTime;

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
        userId = TheWalkingDevAPI.getUserIdByName("tim");
        requestCreator = new RequestCreator();
    }

    @Override
    public void run() {
        System.out.println(new DateTime() + " Show message ");

        JFrame frame = displayLock();
        initial = requestCreator.sendRequest();
        System.out.println(new DateTime() + " Initial, got " + initial);

        TrackerSchedular.scheduleAtFixedRate(() -> {
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
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                });
            }
        }, 0, 2, TimeUnit.SECONDS);
    }

    private JFrame displayLock() {
        JLabel label = new JLabel("Take a break - Walk a few steps, then it'll unlock itself!");
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
