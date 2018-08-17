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
        return manager.getSerialPorts();
    }

    @ShellMethod("Get the hardware connection status")
    public String status() {
        StringBuilder sb = new StringBuilder();
        if (manager.isActive()) {
            sb.append("The hardware connection is active");
            if (manager.getHardwareLastSeen() > 0) {
                sb.append(" and the button hardware was last heard from ").
                        append(Math.min(1, System.currentTimeMillis() - manager.getHardwareLastSeen() / 1000)).
                        append(" seconds ago");
            } else {
                sb.append(" but the button hardware has not yet been detected");
            }
        } else {
            sb.append("The hardware connection has not been started");
        }
        return sb.toString();
    }

    @ShellMethod("Start the hardware connection")
    public String start(@ShellOption String sp) {
        return (manager.start(sp)) ? "Hardware connection successfully started using: \"" + sp + "\"" : "Unable to establish hardware connection :-(";
    }

    public Availability startAvailability() {
        return (!manager.isActive()) ? Availability.available() : Availability.unavailable("a hardware connection already exists");
    }

    @ShellMethod("Stop the hardware connection")
    public String stop() {
        return (manager.stop()) ? "Hardware connection stopped" : "Unable to stop hardware connection :-(";
    }

    public Availability stopAvailability() {
        return (manager.isActive()) ? Availability.available() : Availability.unavailable("the hardware connection is not running");
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
