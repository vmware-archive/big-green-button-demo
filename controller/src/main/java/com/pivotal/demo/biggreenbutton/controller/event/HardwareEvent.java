package com.pivotal.demo.biggreenbutton.controller.event;

import org.springframework.context.ApplicationEvent;

public class HardwareEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public HardwareEvent(Object source) {
        super(source);
    }
}
