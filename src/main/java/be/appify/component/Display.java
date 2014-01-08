package be.appify.component;

import be.appify.ui.Text;
import lejos.hardware.lcd.LCD;

public interface Display {

    Text text(String text);

    void clear();

    void clearLine(int y);
}
