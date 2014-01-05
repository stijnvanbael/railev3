package be.appify.lego.ev3.component;

import lejos.hardware.Button;

public class Light {
    private LightColor color = LightColor.GREEN;

    public Light color(LightColor color) {
        this.color = color;
        return this;
    }

    public Light off() {
        Button.LEDPattern(0);
        return this;
    }

    public Light flash() {
        Button.LEDPattern(color.value() + 3);
        return this;
    }

    public Light blink() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    on();
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
                finally {
                    off();
                }
            }
        }, "Light: blink").start();
        return this;
    }

    public Light on() {
        Button.LEDPattern(color.value());
        return this;
    }
}
