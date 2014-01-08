package be.appify.component;


import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.Port;

public class Motor {
    private NXTRegulatedMotor motor;
    private Port port;
    private boolean forward = true;

    public Motor(Port port) {
        this.port = port;
    }

    public Motor speed(int rotationsPerMinute) {
        initializeIfNecessary();
        motor.setSpeed(rotationsPerMinute);
        return this;
    }

    public Motor start() {
        initializeIfNecessary();
        if(forward) {
            motor.forward();
        } else {
            motor.backward();
        }
        return this;
    }

    public Motor stop() {
        initializeIfNecessary();
        motor.stop();
        motor.close();
        return this;
    }

    public Motor reverse() {
        initializeIfNecessary();
        forward = !forward;
        if(motor.isMoving()) {
            motor.stop();
            start();
        }
        return this;
    }

    private void initializeIfNecessary() {
        if (motor == null) {
            motor = new NXTRegulatedMotor(port);
        }
    }
}
