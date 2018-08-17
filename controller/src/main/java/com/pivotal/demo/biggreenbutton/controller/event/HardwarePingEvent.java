package com.pivotal.demo.biggreenbutton.controller.event;

public class HardwarePingEvent extends HardwareEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public HardwarePingEvent(Object source) {
        super(source);
    }
}
