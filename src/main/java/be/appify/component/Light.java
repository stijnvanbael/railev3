package be.appify.component;


public interface Light {

    Light color(LightColor color);

    Light off();

    Light flash();

    Light blink();

    Light on();
}
