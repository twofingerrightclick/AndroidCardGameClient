package com.example.cardgameclient;

public class ServerConfig {

    public static final String ServerURLProduction = "http://chatserver.azurewebsites.net/";
    public static final String ServerURLDebug = "http://10.42.0.145:3030/";

    //events from Server:

    //token for turn and stun servers available from server
    public static final String eventTokenOffer = "token-offer";

    public static final String privateGameRoomRequest = "private-game-room-request";

    public static final String privateGameRoomRequestComplete = "private-game-room-request-complete";
    public static final String publicGameRoomRequestComplete= "public-game-room-request-complete";


    //'game-room-request-complete'
    public static final String gameRoomRequestComplete = "game-room-request-complete";
    //'public-game-room-request'
    public static final String publicGameRoomRequest = "public-game-room-request";

    public static final String gameReadyToPlay = "game-ready-to-play";

    public static final String joinPrivateGameRoom = "join-private-game-room";
    public static final String unableToFindRoom = "unable-to-find-room";

    public static final String getNumActivePlayers = "get-num-active-public-players";
    public static final String numActivePlayers = "num-active-public-players";

    public static final String privateGameInitiatorLeftTheGame = "private-game-initiator-left-game";

    public static final String peerMsg ="peer-msg";


    public static final String joinPrivateGameRoomRequestComplete = "join-private-game-room-request-complete";
    public static final String privateGameJoiningPlayerLeftTheGame = "private-game-joiner-player-left-game";

    public static final String eventConnectError = "EVENT_CONNECT_ERROR";

    //public static final String startGame ="start-game";





}
