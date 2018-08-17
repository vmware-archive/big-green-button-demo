package com.pivotal.demo.biggreenbutton.controller.event;

/**
 * Event generated when the button hardware has been heard from.
 */
public class HardwarePingEvent extends HardwareEvent {
    public HardwarePingEvent(Object source) {
        super(source);
    }
}
