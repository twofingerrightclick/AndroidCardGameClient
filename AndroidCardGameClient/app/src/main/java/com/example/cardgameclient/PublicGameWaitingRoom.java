package com.example.cardgameclient;

import android.os.Bundle;

import androidx.annotation.NonNull;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Observable;
import java.util.Observer;

import io.socket.client.Socket;


public class PublicGameWaitingRoom extends MultiplayerWaitingRoomActivityFragment implements IMultiplayerConnectorSocketEventUser {
    private static final String TAG = PublicGameWaitingRoom.class.getSimpleName();
    public PublicGameWaitingRoom() {
        super(R.layout.fragment_public_game_waiting_room);
        _MultiPlayerConnectorObserver= multiPlayerConnectorObserver;

    }

    String _DefaultFragmentStatusMessage ="Private Game Joined";
    String _StatusMessage ="Finding An Opponent";
    //MultiPlayerConnector _MultiplayerConnector;
    //MultiplayerWaitingRoomActivity _MultiplayerWaitingRoomActivity;



    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requestPublicGameRoom();
       /* Bundle extras = getArguments();

        boolean gameCreator=false;
        gameCreator= extras.getBoolean("gameCreator");

        if (gameCreator) {
            TextView statusMessage = view.findViewById(R.id.statusMessage);
            statusMessage.setText(_CreatorStatusMessage);
        }*/


    }

    private void requestPublicGameRoom() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("minPlayersRequiredForGame", MultiplayerWaitingRoomActivity._MinNumPlayersRequiredForGame);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            obj.put("gameType", MultiplayerWaitingRoomActivity._GameType);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //emit events
        _MultiplayerConnector.emitEvent(ServerConfig.publicGameRoomRequest, obj);
        _MultiplayerConnector.emitEvent(ServerConfig.getNumActivePlayers);


        Log.d(TAG, "requesting public game room");
    }


    private Observer multiPlayerConnectorObserver = new Observer() {
        @Override
        public void update(Observable o, Object arg) {


            switch ((String)arg){

                    case ServerConfig.numActivePlayers:
                        updatePublicWaitingRoomActivePlayerCount();
                        break;
                    case ServerConfig.gameReadyToPlay:
                        //go to game
                        break;
                    case ServerConfig.eventConnectError:
                        _MultiplayerWaitingRoomActivity.badInputDialog("Unable To Connect To Server" + TAG);
                        //showBadInputDialogForTesting();
                        break;
                }

            }

    };






    public void updatePublicWaitingRoomActivePlayerCount() {

        _MultiplayerWaitingRoomActivity._UIHandler.post(new Runnable() {
            @Override
            public void run() {
                TextView activePlayersTextView= _MultiplayerWaitingRoomActivity.findViewById(R.id.numActivePublicPlayers);
                activePlayersTextView.setText(_MultiplayerConnector._NumberActivePublicPlayers);

            }
        });

    }



    public static void AddSocketEvents(Socket socket, MultiPlayerConnector multiPlayerConnector){

        socket.on(ServerConfig.numActivePlayers, args -> {
            Log.d(TAG, "num active players received");
            multiPlayerConnector._NumberActivePublicPlayers=(int)((JSONObject)args[0]).opt("numPlayers");
            multiPlayerConnector.notifyObservers(ServerConfig.numActivePlayers);
        });
        socket.on(ServerConfig.publicGameRoomRequestComplete, args -> {
            Log.d(TAG, "public game room found");

        });


    }

}
