package com.example.cardgameclient;

public class ServerConfig {

    public static final String ServerURLProduction = "http://chatserver.azurewebsites.net/";
    public static final String ServerURLDebug = "http://10.42.0.145:3030/";

    //events from Server:

    //token for turn and stun servers available from server
    public static final String eventTokenOffer = "token-offer";

    public static final String privateGameRoomRequest = "private-game-room-request";


    //'game-room-request-complete'
    public static final String gameRoomRequestComplete = "game-room-request-complete";
    //'public-game-room-request'
    public static final String publicGameRoomRequest = "public-game-room-request";

    public static final String gameReadyToPlay = "game-ready-to-play";

    public static final String joinPrivateGameRoom = "join-private-game-room";
    public static final String unableToFindRoom = "unable-to-find-room";

    public static final String peerMsg ="peer-msg";





}
