package com.pivotal.demo.biggreenbutton.controller;

/**
 * Interface for hardware that connects to a physical button. It is expected that implementations of this interface
 * will have a constructor that takes an ApplicationContext and will use it to dispatch application events (e.g.
 * HardwarePressEvent).
 */
public interface ButtonControllerHardware {
    /**
     * Starts the hardware connection.
     *
     * @param name a configuration parameter (e.g. port name)
     *
     * @return a boolean indicating whether the connection was successful
     */
    boolean start(String name);

    /**
     * Stops the hardware connection.
     *
     * @return a boolean indicating whether the connection was stopped successfully
     */
    boolean stop();

    /**
     * Indicates if the hardware connection is active.
     *
     * @return a boolean
     */
    boolean isActive();
}
