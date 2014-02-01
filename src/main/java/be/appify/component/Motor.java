package be.appify.component;


import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.Port;

public class Motor {
    public static final int DEGREES_PER_ROTATION = 360;
    private static final int SECONDS_IN_A_MINUTE = 60;
    private NXTRegulatedMotor motor;
    private Port port;
    private boolean forward = true;

    public Motor(Port port) {
        this.port = port;
    }

    public Motor speed(int rotationsPerMinute) {
        initializeIfNecessary();
        motor.setSpeed(rotationsPerMinute * DEGREES_PER_ROTATION / SECONDS_IN_A_MINUTE);
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
        motor.suspendRegulation();
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

    public int getDistance() {
        return motor.getTachoCount() / DEGREES_PER_ROTATION;
    }

    public void resetDistance() {
        motor.resetTachoCount();
    }
}
