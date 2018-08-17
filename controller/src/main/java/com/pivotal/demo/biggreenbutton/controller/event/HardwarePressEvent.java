package com.pivotal.demo.biggreenbutton.controller.event;

public class HardwarePressEvent extends HardwareEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public HardwarePressEvent(Object source) {
        super(source);
    }
}
