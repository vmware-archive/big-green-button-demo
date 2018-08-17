package com.pivotal.demo.biggreenbutton.controller;

import com.fazecast.jSerialComm.SerialPort;
import com.pivotal.demo.biggreenbutton.controller.event.HardwareEvent;
import com.pivotal.demo.biggreenbutton.controller.event.HardwarePingEvent;
import com.pivotal.demo.biggreenbutton.controller.event.HardwarePressEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.logging.Logger;

/**
 * Manages the serial port and listens for events from the serial hardware.
 */
@Component
public class ButtonControllerManager implements ApplicationListener<HardwareEvent> {
    private final Logger logger = Logger.getLogger(getClass().getName());

    @Value("${FLY_PATH:/usr/local/bin/fly}")
    private String flyPath;
    @Value("${FLY_TARGET:wings}")
    private String target;
    @Value("${FLY_PIPELINE:fussball-demo}")
    private String pipeline;
    @Value("${FLY_JOB:job-www-prod}")
    private String job;

    private ButtonControllerHardware hardware;
    private long hardwareLastSeen = -1;

    @Autowired
    public ButtonControllerManager(@Autowired ButtonControllerHardware hardware) {
        this.hardware = hardware;
        logger.info("Using hardware implementation: " + hardware.getClass().getName());
    }

    boolean start(String name) {
        validateFlyPath();
        return hardware.start(name);
    }

    boolean stop() {
        return hardware.stop();
    }

    boolean isActive() {
        return hardware.isActive();
    }

    String getSerialPorts() {
        StringBuilder sb = new StringBuilder();
        for (SerialPort port : SerialPort.getCommPorts()) {
            sb.append(port.getSystemPortName()).append("\n");
        }
        return sb.toString();
    }

    void simulateButtonPress() {
        onApplicationEvent(new HardwarePressEvent(hardware));
    }

    long getHardwareLastSeen() {
        return hardwareLastSeen;
    }

    String getConfiguration() {
        validateFlyPath();

        return "FLY_PATH: " + flyPath + "\n" +
                "FLY_TARGET: " + target + "\n" +
                "FLY_PIPELINE: " + pipeline + "\n" +
                "FLY_JOB: " + job + "\n";
    }

    @Override
    public void onApplicationEvent(HardwareEvent event) {
        logger.fine("Got hardware event: " + event);

        if (event instanceof HardwarePingEvent) {
            onHardwarePing();
        } else if (event instanceof HardwarePressEvent) {
            onHardwarePressEvent();
        }
    }

    private void onHardwarePing() {
        if (hardwareLastSeen == -1) {
            logger.info("Button hardware detected");
        }
        hardwareLastSeen = System.currentTimeMillis();
    }

    private void onHardwarePressEvent() {
        logger.info("Button press detected");

        if (validateFlyPath()) {
            try {
                // trigger job
                Process p = new ProcessBuilder(flyPath, "-t", target, "trigger-job", "-j", pipeline + "/" + job).inheritIO().start();
                p.waitFor();
                if (p.exitValue() != 0) {
                    System.out.println("Unable to start Concourse job");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validateFlyPath() {
        if (flyPath == null || !new File(flyPath).exists()) {
            logger.severe("WARNING: Fly executable not found at: " + flyPath);
            return false;
        } else {
            return true;
        }
    }
}
