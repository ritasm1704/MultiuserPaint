package org.suai.network;

import java.io.*;
import java.net.Socket;

/**
 * храниет соединение сервера с клиентом и наоборот
 * отвечает за их обмен данными
 */

public class TCPConnection {
    private final Socket socket;
    private final Thread rxThread;
    private final TCPConnectionListener eventListener;
    private final InputStream in;
    private final OutputStream out;

    public TCPConnection(TCPConnectionListener eventListener, String ipAddr, int port) throws IOException {
        this(eventListener, new Socket(ipAddr, port));
    }

    public TCPConnection(TCPConnectionListener eventListener, Socket socket) throws IOException {
        this.eventListener = eventListener;
        this.socket = socket;

        in = socket.getInputStream();
        out = socket.getOutputStream();

        rxThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    eventListener.onConnectionReady(TCPConnection.this);

                    while (!rxThread.isInterrupted()) {
                        ObjectInputStream oIn = new ObjectInputStream(in);
                        eventListener.onReceiveObject(TCPConnection.this, oIn.readObject());
                    }
                } catch (IOException | ClassNotFoundException e) {
                    eventListener.onException(TCPConnection.this, e);
                } finally {
                    eventListener.onDisconnect(TCPConnection.this);
                }
            }
        });
        rxThread.start();
    }

    public synchronized void disconnect() {
        rxThread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            eventListener.onException(TCPConnection.this, e);
        }
    }

    @Override
    public String toString() {
        return "TCPConnection: " + socket.getInetAddress() + ": " + socket.getPort();
    }

    public synchronized void sendObject(Message message) {

        try {
            ObjectOutputStream oOut = new ObjectOutputStream(out);
            oOut.writeObject(message);
            oOut.flush();
        } catch (IOException e) {
            eventListener.onException(TCPConnection.this, e);
            disconnect();
        }
    }

    public int getPort() {
        return socket.getPort();
    }
}
