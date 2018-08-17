package com.pivotal.demo.biggreenbutton.controller.serial;

import com.pivotal.demo.biggreenbutton.controller.ButtonControllerHardware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortPacketListener;
import com.pivotal.demo.biggreenbutton.controller.event.HardwarePingEvent;
import com.pivotal.demo.biggreenbutton.controller.event.HardwarePressEvent;

import java.util.logging.Logger;

/**
 * Listener class for async data packets from the serial port. A data packet is considered a single byte.
 */
@Component
@Profile("default")
public class ButtonControllerSerialHardware implements ButtonControllerHardware, SerialPortPacketListener {
    private final Logger logger = Logger.getLogger(getClass().getName());

    private ApplicationContext context;
    private SerialPort port;

    public ButtonControllerSerialHardware(@Autowired ApplicationContext context) {
        this.context = context;
    }

    @Override
    public boolean start(String name) {
        port = SerialPort.getCommPort(name);
        port.addDataListener(this);
        return port.openPort();
    }

    @Override
    public boolean stop() {
        if (port != null) {
            port.removeDataListener();
            boolean r = port.closePort();
            port = null;
            return r;
        } else {
            return true;
        }
    }

    @Override
    public boolean isActive() {
        return (port != null);
    }

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }

    @Override
    public int getPacketSize() {
        return 1;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        byte[] data = event.getReceivedData();
        switch (data[0]) {
            case 'P':
                context.publishEvent(new HardwarePingEvent(this));
                break;
            case 'B':
                context.publishEvent(new HardwarePressEvent(this));
                break;
            default:
                logger.finest("Read unknown packet: " + data[0]);
        }
    }
}
