package com.pivotal.demo.biggreenbutton.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortPacketListener;
import com.pivotal.demo.biggreenbutton.controller.event.HardwarePingEvent;
import com.pivotal.demo.biggreenbutton.controller.event.HardwarePressEvent;

/**
 * Listener class for async data packets from the serial port. A data packet is considered a single byte.
 */
@Component
public class ButtonControllerSerialListener implements SerialPortPacketListener {
    private ApplicationContext context;

    public ButtonControllerSerialListener(@Autowired ApplicationContext context) {
        this.context = context;
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
                System.out.println("Read unknown packet: " + data[0]);
        }
    }
}
