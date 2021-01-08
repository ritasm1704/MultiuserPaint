package org.suai.server;

import org.suai.network.Account;
import org.suai.network.Message;
import org.suai.network.TCPConnection;
import org.suai.network.TCPConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server implements TCPConnectionListener {

    //"C:/doc/ТП2/MultiuserPaint/log.txt"
    private final LogsWriter logsWriter;
    private final ArrayList<TCPConnection> connections = new ArrayList<>();
    private final ArrayList<Account> accountArray = new ArrayList<>();

    public static void main(String[] args) {

        new Server(args[0]);
    }

    private Server(String path) {
        logsWriter = new LogsWriter(path);
        System.out.println("Server running...");
        logsWriter.append("Server running...");

        try(ServerSocket serverSocket = new ServerSocket(1111)) {

            logsWriter.append("ServerIP: " + serverSocket.getInetAddress() +
                    " listen port: " + serverSocket.getLocalPort());
            while (true) {
                try {
                    new TCPConnection(this, serverSocket.accept());
                } catch (IOException e) {
                    System.out.println("TCPConnection exception: " + e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        System.out.println("Connection is ready...");
        logsWriter.append("Connection is ready... " + tcpConnection.toString());
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        connections.remove(tcpConnection);
        logsWriter.append("Disconnect: " + tcpConnection.toString());
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection exception: " + e);
        logsWriter.append("TCPConnection exception:" + e + "  :  " + tcpConnection.toString());
    }

    @Override
    public synchronized void onReceiveObject(TCPConnection tcpConnection, Object in) {
        Message message = (Message) in;
        logsWriter.append("Client: " + tcpConnection.toString() + " request: " + message.getCommand());

        if (message.getCommand().equals("@canICreate")) {

            for (int i = 0; i < accountArray.size(); i++) {
                for (int j = 0; j < accountArray.get(i).getSize(); j++) {

                    if (accountArray.get(i).getImageName(j).equals(message.getImageName())) {
                        System.out.println("@noYouCannotCreate canvas: " + message.getImageName());
                        Message message1 = new Message("@noYouCannotCreate");
                        message1.setUserName(message.getUserName());
                        tcpConnection.sendObject(message1);
                        return;
                    }
                }
            }
            Message message1 = new Message("@yesYouCanCreate");
            System.out.println("@yesYouCanCreate canvas: " + message.getImageName());
            message1.setImageName(message.getImageName());
            tcpConnection.sendObject(message1);

        } else if (message.getCommand().equals("@sendToAllUpdateImage")) {

            for (int i = 0; i < accountArray.size(); i++) {
                for (int j = 0; j < accountArray.get(i).getSize(); j++) {

                    if (accountArray.get(i).getImageName(j).equals(message.getImageName())) {
                        Account account = accountArray.get(i);
                        account.setImage(message.getImageName(), message.getImage());
                        accountArray.set(i, account);

                        Message message1 = new Message("@updateImage");
                        message1.setImage(message.getImageName(), message.getImage());
                        sendNotToAll(tcpConnection, message1);

                        Message message2 = new Message("@yourImage");
                        message2.setImage(message.getImageName(), message.getImage());
                        tcpConnection.sendObject(message2);
                        return;
                    }
                }
            }
        } else if (message.getCommand().equals("@canIRegistered")) {

            for (int i = 0; i < accountArray.size(); i++) {
                if(accountArray.get(i).getUsername().equals(message.getUserName())) {

                    if (accountArray.get(i).getPassword().equals(message.getPassword())) {
                        Message message1 = new Message("@yesYouCanRegistered");
                        tcpConnection.sendObject(message1);
                        return;
                    } else {
                        Message message1 = new Message("@noYouCannotRegistered");
                        tcpConnection.sendObject(message1);
                        return;
                    }
                }
            }
            Account account = new Account(message.getUserName(), message.getPassword(), tcpConnection);
            accountArray.add(account);
            Message message1 = new Message("@yesYouCanRegistered");
            tcpConnection.sendObject(message1);

        } else if (message.getCommand().equals("@newImage")) {

            System.out.println("@newImage : " + message.getImageName());
            for (int i = 0; i < accountArray.size(); i++) {

                if (accountArray.get(i).getUsername().equals(message.getUserName())) {

                    System.out.println("Create new Image: userName: " + message.getUserName() + " imageName: " + message.getImageName());
                    Account account = accountArray.get(i);
                    account.setImage(message.getImageName(), null);
                    accountArray.set(i, account);
                    sendToAllConnections(message);
                }
            }
        } else if (message.getCommand().equals("@sendMeImagesArray")) {

            Message message1 = new Message("@imagesArray");
            for (int i = 0; i < accountArray.size(); i++) {
                for (int j = 0; j < accountArray.get(i).getSize(); j++) {

                    String string = accountArray.get(i).getImageName(j);
                    message1.putInImagesArray(string, accountArray.get(i).getImage(string));
                }
                tcpConnection.sendObject(message1);
            }
        } else if (message.getCommand().equals("@sendMeMyAcc")) {

            for (int i = 0; i < accountArray.size(); i++) {
                if (accountArray.get(i).getUsername().equals(message.getUserName())) {

                    Message message1 = new Message("@yourAcc");
                    for (int j = 0; j < accountArray.get(i).getSize(); j++) {

                        String string = accountArray.get(i).getImageName(j);
                        message1.putInImagesArray(string, accountArray.get(i).getImage(string));

                    }
                    message1.setAccesses(accountArray.get(i).getAccesses());
                    tcpConnection.sendObject(message1);
                }
            }
        } else if (message.getCommand().equals("@iAddAccess")) {

            for (int i = 0; i < accountArray.size(); i++) {
                if (accountArray.get(i).getUsername().equals(message.getUserName())) {
                    Account account = accountArray.get(i);
                    account.addAccess(message.getAccess());
                    accountArray.set(i, account);
                }
            }

        }else if (message.getCommand().equals("@iRemoveAccess")) {

            for (int i = 0; i < accountArray.size(); i++) {
                if (accountArray.get(i).getUsername().equals(message.getUserName())) {
                    Account account = accountArray.get(i);
                    account.removeAccess(message.getAccess());
                    accountArray.set(i, account);
                }
            }
        } else if (message.getCommand().equals("@canIPainting")) {

            for (int i = 0; i < accountArray.size(); i++) {
                for (int j = 0; j < accountArray.get(i).getSize(); j++) {

                    if (accountArray.get(i).getImageName(j).equals(message.getImageName())) {
                        if (accountArray.get(i).getUsername().equals(message.getUserName())) {
                            Message message1 = new Message("@yesYouCanPainting");
                            tcpConnection.sendObject(message1);
                            return;
                        }
                        ArrayList<String> names = accountArray.get(i).getAccesses();

                        for (int k = 0; k < names.size(); k++) {
                            if (names.get(k).equals(message.getUserName())) {
                                Message message1 = new Message("@yesYouCanPainting");
                                tcpConnection.sendObject(message1);
                                return;
                            }
                        }
                        Message message1 = new Message("@noYouCannotPainting");
                        tcpConnection.sendObject(message1);
                        return;
                    }
                }
            }
        }
    }

    private synchronized void sendToAllConnections(Message message) {

        int size = connections.size();
        for (int i = 0; i < size; i++) {
            connections.get(i).sendObject(message);
        }
        logsWriter.append("send to all connections: " + message.getCommand());
    }

    private synchronized void sendNotToAll(TCPConnection tcpConnection, Message message) {

        int size = connections.size();
        for (int i = 0; i < size; i++) {
            if (connections.get(i).getPort() != tcpConnection.getPort()) {
                connections.get(i).sendObject(message);
            }
        }
        logsWriter.append("send to all connections: " + message.getCommand() + " except for him: " + tcpConnection.toString());
    }
}
