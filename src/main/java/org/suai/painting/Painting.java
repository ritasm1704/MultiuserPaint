package org.suai.painting;

import org.suai.client.ClientWindow;

/**
 * отвечает за обновление данных холста во время рисования
 */

public class Painting extends Thread{

    private org.suai.painting.Canvas canvas;
    private final String canvasName;
    private final ClientWindow window;

    public  Painting(Canvas canvas, ClientWindow window) {
        System.out.println("Painting is create");
        this.canvas = canvas;

        this.window = window;
        this.canvasName = canvas.getMyName();
    }

    @Override
    public void run() {

        System.out.println("Painting is run");
        while (true) {

            if(window.getUpdateImage()) {
                System.out.println("Update Image");
                canvas.updateImage(window.getImage(canvasName));
                window.setUpdateImage(false);
            }
        }
    }
}
