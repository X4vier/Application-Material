package comp1110.ass2;

import java.awt.*;
import java.awt.event.InputEvent;

/**
 * Created by X4 on 5/20/2016.
 */
public class test
{

    public static void click(int x, int y) throws AWTException{
        Robot bot = new Robot();
        bot.mouseMove(x, y);
        bot.mousePress(InputEvent.BUTTON1_MASK);
        bot.mouseRelease(InputEvent.BUTTON1_MASK);
    }

    public static void main(String[] args) {
        try {
            click(5,1060);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

}

