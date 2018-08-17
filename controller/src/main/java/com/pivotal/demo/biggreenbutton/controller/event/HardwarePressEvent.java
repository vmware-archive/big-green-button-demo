package com.pivotal.demo.biggreenbutton.controller.event;

/**
 * Event generated when a button press is detected.
 */
public class HardwarePressEvent extends HardwareEvent {
    public HardwarePressEvent(Object source) {
        super(source);
    }
}
