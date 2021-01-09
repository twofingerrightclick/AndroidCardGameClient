package com.example.cardgameclient;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Observable;
import java.util.Observer;

import io.socket.client.Socket;

public class JoinPrivateGameFragment extends Fragment implements IMultiplayerConnectorSocketEventUser {

    MultiPlayerConnector _MultiplayerConnector;
    MultiplayerWaitingRoomActivity _MultiplayerWaitingRoomActivity;

    public JoinPrivateGameFragment() {
        super(R.layout.fragment_join_private_game);
        _MultiplayerConnector= MultiPlayerConnector.get_Instance();
        _MultiplayerWaitingRoomActivity = (MultiplayerWaitingRoomActivity)getActivity();

    }


    private static final String TAG = JoinPrivateGameFragment.class.getSimpleName();

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _MultiplayerConnector.addObserver(_MultiPlayerConnectorObserver);

        TextView playerNameTextInput= view.findViewById(R.id.playerNameInput);
        TextView gameCodeTextInput = view.findViewById(R.id.gameCodeInput);

        Button joinButton = view.findViewById(R.id.joinButton);

        joinButton.setOnClickListener(v -> {
            //p2psocket.emit('private-game-room-request', {numPlayersRequiredForGame:2, gameType: 'fives'})
            //_ParentActivity._MultiPlayerConnector.joinToPublicGame(2, _ParentActivity._GameType);

            emitJoinPrivateGame(playerNameTextInput,gameCodeTextInput);

            //getParentFragmentManager().setFragmentResult("setGameCreator", result);
        });
    }

    private boolean emitJoinPrivateGame(TextView playerNameTextInput, TextView gameCodeTextInput) {
        String playerName = playerNameTextInput.getText().toString();
        String gameCode = gameCodeTextInput.getText().toString();

        if (playerName.isEmpty()) {

            _MultiplayerWaitingRoomActivity.badInputDialog("Player name cannot be empty");
            return false;
        }
        if ( gameCode.isEmpty()) {

            _MultiplayerWaitingRoomActivity.badInputDialog("game code cannot be empty");
            return false;
        }

        JSONObject args = new JSONObject();
        try {
            args.put("playerName", playerName);
            args.put("roomName",gameCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        _MultiplayerConnector.emitEvent(ServerConfig.joinPrivateGameRoom, args);

        return true;

    }

    public void onPrivateRoomJoin() {
        Bundle result = new Bundle();
        result.putString("fragmentClassName", JoiningPrivateGameWaitingRoomFragment.class.getCanonicalName());
        _MultiplayerWaitingRoomActivity.changeFragment(JoiningPrivateGameWaitingRoomFragment.class.getCanonicalName(), result);

    }


    public void OnRoomNotFound() {

        _MultiplayerWaitingRoomActivity._UIHandler.post(() -> {
            new AlertDialog.Builder(_MultiplayerWaitingRoomActivity)
                    .setTitle("Unable to find game")
                    .setMessage("Check with your friend to make sure your game code is still active and correct.")
                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    /* .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface dialog, int which) {
                             // Continue with delete operation
                         }
                     })*/

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.ok, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });
    }


    private Observer _MultiPlayerConnectorObserver = new Observer() {
        @Override
        public void update(Observable o, Object arg) {



                switch ((String) arg) {

                    case ServerConfig.joinPrivateGameRoomRequestComplete:
                        onPrivateRoomJoin();
                        break;
                    case ServerConfig.unableToFindRoom:
                        OnRoomNotFound();
                        break;

                }
            }

    };




    static void AddSocketEvents(Socket socket, MultiPlayerConnector multiPlayerConnector){

        socket.on(ServerConfig.unableToFindRoom, args -> {
            Log.d(TAG, "unable to find room");
            multiPlayerConnector.notifyObservers(ServerConfig.unableToFindRoom);

        });

    }


}
