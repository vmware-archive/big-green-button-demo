package com.pivotal.demo.biggreenbutton.controller;

import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

/**
 * Processes Spring Shell commands.
 */
@ShellComponent
public class ButtonControllerCommandProcessor {
    private ButtonControllerManager manager;

    public ButtonControllerCommandProcessor(ButtonControllerManager manager) {
        this.manager = manager;
    }

    @ShellMethod("List available serial ports")
    public String ports() {
        return manager.getPorts();
    }

    @ShellMethod("Get the controller status")
    public String status() {
        StringBuilder sb = new StringBuilder();
        if (manager.isPortOpen()) {
            sb.append("The serial port is open");
            if (manager.getHardwareLastSeen() > 0) {
                sb.append(" and the button hardware was last heard from ").
                        append(Math.min(1, System.currentTimeMillis() - manager.getHardwareLastSeen() / 1000)).
                        append(" seconds ago");
            } else {
                sb.append(" but the button hardware has not yet been detected");
            }
        } else {
            sb.append("The serial port is not open");
        }
        return sb.toString();
    }

    @ShellMethod("Start the controller")
    public String start(@ShellOption String sp) {
        return (manager.openPort(sp)) ? "Controller successfully started on port \"" + sp + "\"" : "Unable to open serial port :-(";
    }

    public Availability startAvailability() {
        return (!manager.isPortOpen()) ? Availability.available() : Availability.unavailable("the controller is already running");
    }

    @ShellMethod("Stop the controller")
    public String stop() {
        return (manager.closePort()) ? "Controller successfully stopped" : "Unable to stop controller :-(";
    }

    public Availability stopAvailability() {
        return (manager.isPortOpen()) ? Availability.available() : Availability.unavailable("the controller is not running");
    }

    @ShellMethod("Simulate a button press")
    public void simulate() {
        manager.simulateButtonPress();
    }

    @ShellMethod("Show current configuration")
    public String config() {
        return manager.getConfiguration();
    }
}
