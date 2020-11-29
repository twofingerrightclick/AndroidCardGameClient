package com.example.cardgameclient;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.net.URISyntaxException;
import java.util.Observable;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.Transport;
import io.socket.engineio.client.transports.WebSocket;

import static io.socket.client.Manager.EVENT_CONNECT_ERROR;
import static io.socket.client.Manager.EVENT_TRANSPORT;
import static io.socket.client.Socket.EVENT_CONNECT;
import static io.socket.client.Socket.EVENT_DISCONNECT;


public class MultiPlayerConnector extends Observable {
    private static final String TAG = "MultiPlayerConnector";
    private String serverUrl = (BuildConfig.DEBUG) ? ServerConfig.ServerURLDebug : ServerConfig.ServerURLProduction;
    private Socket _Socket;

    private String _RoomCode;
    public String getRoomCode() { return _RoomCode; }
    private void setRoomCode(String roomCode) {
        setChanged();
        this._RoomCode = roomCode;
    }

    public String msg;


    private JSONArray _TurnStunServers;
    public MultiPlayerConnector(){

        connectToSignallingServer();


    }

    private void connectToSignallingServer() {
        try {
            IO.Options opts = new IO.Options();
            opts.transports = new String[] { WebSocket.NAME };
            _Socket = IO.socket(serverUrl); //put url in config resource file

            _Socket.on(EVENT_CONNECT,  args-> {
                    //JSONObject obj = (JSONObject)args[0];
                    Log.d(TAG, "Connected to server");
                }).on("public-game-room-request", args -> {
                Log.d(TAG, "requesting public game room");
            }).on("game-room-request-complete", args -> {
                Log.d(TAG, "game room request complete");
                setRoomCode(((JSONObject)args[0]).opt("gameRoomName").toString());
                notifyObservers(ServerConfig.gameRoomRequestComplete);
            }).on(EVENT_CONNECT_ERROR, args ->{
                Log.d(TAG,"failed to connect:");
                for(int i =0; i<args.length; i++){
                    Log.d(TAG,args[i].toString());
                }
            }).on("token-offer", args -> {
                Log.d(TAG, "tokens for twilio recieved");
                _TurnStunServers = (JSONArray)args[0];
            }).on(ServerConfig.peerMsg, args -> {
                Log.d(TAG, "peer message");
                this.msg=((JSONObject)args[0]).opt("textVal").toString();
                setChanged();
                notifyObservers(ServerConfig.peerMsg);
            });/*.on("ipaddr", args -> {
                Log.d(TAG, "connectToSignallingServer: ipaddr");
            }).on("created", args -> {
                Log.d(TAG, "connectToSignallingServer: created");
                isInitiator = true;
            }).on("full", args -> {
                Log.d(TAG, "connectToSignallingServer: full");
            }).on("join", args -> {
                Log.d(TAG, "connectToSignallingServer: join");
                Log.d(TAG, "connectToSignallingServer: Another peer made a request to join room");
                Log.d(TAG, "connectToSignallingServer: This peer is the initiator of room");
                isChannelReady = true;
            }).on("joined", args -> {
                Log.d(TAG, "connectToSignallingServer: joined");
                isChannelReady = true;
            }).on("log", args -> {
                for (Object arg : args) {
                    Log.d(TAG, "connectToSignallingServer: " + String.valueOf(arg));
                }
            }).on("message", args -> {
                Log.d(TAG, "connectToSignallingServer: got a message");
            }).on("message", args -> {
                try {
                    if (args[0] instanceof String) {
                        String message = (String) args[0];
                        if (message.equals("got user media")) {
                            maybeStart();
                        }
                    } else {
                        JSONObject message = (JSONObject) args[0];
                        Log.d(TAG, "connectToSignallingServer: got message " + message);
                        if (message.getString("type").equals("offer")) {
                            Log.d(TAG, "connectToSignallingServer: received an offer " + isInitiator + " " + isStarted);
                            if (!isInitiator && !isStarted) {
                                maybeStart();
                            }
                            peerConnection.setRemoteDescription(new SimpleSdpObserver(), new SessionDescription(OFFER, message.getString("sdp")));
                            doAnswer();
                        } else if (message.getString("type").equals("answer") && isStarted) {
                            peerConnection.setRemoteDescription(new SimpleSdpObserver(), new SessionDescription(ANSWER, message.getString("sdp")));
                        } else if (message.getString("type").equals("candidate") && isStarted) {
                            Log.d(TAG, "connectToSignallingServer: receiving candidates");
                            IceCandidate candidate = new IceCandidate(message.getString("id"), message.getInt("label"), message.getString("candidate"));
                            peerConnection.addIceCandidate(candidate);
                        }
                        *//*else if (message === 'bye' && isStarted) {
                        handleRemoteHangup();
                    }*//*
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }).on(EVENT_DISCONNECT, args -> {
                Log.d(TAG, "connectToSignallingServer: disconnect");
            });*/


            _Socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyObservers(Object arg) {
        super.notifyObservers(arg);
    }



    public void joinToPublicGame(int i, String gameType){

        JSONObject obj = new JSONObject();
        try {
            obj.put("numPlayersRequiredForGame", i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            obj.put("gameType", gameType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        _Socket.emit("public-game-room-request", obj);
        Log.d(TAG, "requesting public game room");

    }
}
