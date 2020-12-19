package com.example.cardgameclient;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.View;

import org.json.JSONObject;

import io.socket.client.Socket;


public class PublicGameWaitingRoom extends Fragment implements IMultiplayerConnectorEventUser {
    private static final String TAG = PublicGameWaitingRoom.class.getSimpleName();
    public PublicGameWaitingRoom() {
        super(R.layout.fragment_public_game_waiting_room);
    }



    String _DefaultFragmentStatusMessage ="Private Game Joined";
    String _StatusMessage ="Finding An Opponent";



    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       /* Bundle extras = getArguments();

        boolean gameCreator=false;
        gameCreator= extras.getBoolean("gameCreator");

        if (gameCreator) {
            TextView statusMessage = view.findViewById(R.id.statusMessage);
            statusMessage.setText(_CreatorStatusMessage);
        }*/


    }



    public static void AddSocketEvents( Socket socket, MultiPlayerConnector multiPlayerConnector){

        socket.on(ServerConfig.numActivePlayers, args -> {
            Log.d(TAG, "num active players received");
            multiPlayerConnector._NumberActivePublicPlayers=(int)((JSONObject)args[0]).opt("numPlayers");
            multiPlayerConnector.notifyObservers(ServerConfig.numActivePlayers);
        });
        socket.on(ServerConfig.publicGameRoomRequestComplete, args -> {
            Log.d(TAG, "public game room found");

        });

        socket.on(ServerConfig.gameReadyToPlay, args -> {
            Log.d(TAG, "starting Public game");

        });
    }

}
