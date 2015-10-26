package at.htl.remotecontrol.server;

import at.htl.remotecontrol.actions.*;
import at.htl.remotecontrol.entity.Session;
import at.htl.remotecontrol.entity.Student;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.TimeUnit;

class SocketWriterThread extends Thread {

    private final RobotActionQueue jobs = new RobotActionQueue();
    private final Student student;
    private final ObjectOutputStream out;
    private volatile boolean active = false;

    public SocketWriterThread(Student student, ObjectOutputStream out) {
        super("Writer to " + student.getName());
        this.student = student;
        Session.getInstance().addStudent(student);
        this.out = out;
    }

    public void setActive(boolean active) {
        this.active = active;
        askForScreenShot();
    }

    private double getZoomFactor() {
        return 1.0;
    }

    public long getWaitTime() {
        return Session.getInstance().getTime();
    }

    public void clickEvent(MouseEvent e) {
        if (active) {
            jobs.add(new MoveMouse(e));
            jobs.add(new ClickMouse(e));
        }
        active = true;
        askForScreenShot();
    }

    private void askForScreenShot() {
        jobs.add(new ScreenShot(getZoomFactor()));
    }

    public void run() {
        askForScreenShot();
        try {
            while (!isInterrupted()) {
                try {
                    RobotAction action = jobs.poll(
                            getWaitTime(),
                            TimeUnit.MILLISECONDS);
                    if (action == null) {
                        // we had a timeout, so do a screen capture
                        askForScreenShot();
                    } else {
                        out.writeObject(action);
                        out.reset();
                        out.flush();
                    }
                } catch (InterruptedException e) {
                    interrupt();
                    break;
                }
            }
            out.close();
        } catch (IOException e) {
            System.out.println("Connection to " + student.getName() +
                    " closed (" + e + ')');
        }
        System.out.println("Closing connection to " + student.getName());
    }

}