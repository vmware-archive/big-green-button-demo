package com.pivotal.demo.biggreenbutton.controller.event;

import org.springframework.context.ApplicationEvent;

/**
 * Base class for all hardware events.
 */
public class HardwareEvent extends ApplicationEvent {
    public HardwareEvent(Object source) {
        super(source);
    }
}
