package com.pivotal.demo.biggreenbutton.controller.test;

import com.pivotal.demo.biggreenbutton.controller.ButtonControllerHardware;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Test hardware class to prove that the serial implementation can actually be overridden :-)
 */
@Component
@Profile("test")
public class ButtonControllerTestHardware implements ButtonControllerHardware {
    @Override
    public boolean start(String name) {
        return false;
    }

    @Override
    public boolean stop() {
        return false;
    }

    @Override
    public boolean isActive() {
        return false;
    }
}
