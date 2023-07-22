package visual;

import java.awt.Toolkit;
import java.awt.Dimension;

public class Startup {
    private static Window window;
    public static void main(String[] args) {
        int width = 0;
        int height = 0;

        //use toolkit to get the primary monitor width and height
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        width = (int) screenSize.getWidth();
        height = (int) screenSize.getHeight();

        //create window instance
        window = new Window("SH2", width, height);
        Loop loop = new Loop();

        loop.start();
    }

    public static Window getWindow() {
        return window;
    }
}