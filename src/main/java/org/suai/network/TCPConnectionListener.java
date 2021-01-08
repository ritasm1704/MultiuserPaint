package org.suai.network;

/**
 * шаблон для клиента и сервера с описанием методов реагирования на события
 */

public interface TCPConnectionListener {

    void onConnectionReady(TCPConnection tcpConnection);
    void onDisconnect(TCPConnection tcpConnection);
    void onException(TCPConnection tcpConnection, Exception e);
    void onReceiveObject(TCPConnection tcpConnection, Object in);
}
