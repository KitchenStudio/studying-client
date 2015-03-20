package com.example.xiner.net;

import android.util.Log;


import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;

/**
 * Created by xiner on 15-3-9.
 */

public class ChatSocket extends WebSocketClient {


    public ChatSocket(URI serverURI) {
        super(serverURI, new Draft_17());
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.v("ChatSocket", "connect open");
        send("from android client");
    }

    @Override
    public void onMessage(String message) {
        Log.v("ChatSocket", message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onError(Exception ex) {

       ex.printStackTrace();
    }
}
