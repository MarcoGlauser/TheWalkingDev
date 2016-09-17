package org.jetbrains.tutorials.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import javax.swing.*;
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
    public void actionPerformed(AnActionEvent anActionEvent) {



        JOptionPane.showMessageDialog(null, "Starting timer ...");

        Callable<Object> objectCallable = () -> {
            JOptionPane.showMessageDialog(null, "After ten seconds .. locking ... ");

            JFrame yourFrame = new JFrame();
            yourFrame.setVisible(true);
            yourFrame.setResizable(false);
            yourFrame.setExtendedState(yourFrame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
            yourFrame.setUndecorated(true);

            new WindowsSecurity(yourFrame);
            return null;
        };

        exec.schedule(objectCallable, 10, TimeUnit.SECONDS);
    }
}
