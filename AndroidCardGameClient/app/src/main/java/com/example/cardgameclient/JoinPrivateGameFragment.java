package com.example.cardgameclient;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;

public class JoinPrivateGameFragment extends Fragment implements IMultiplayerConnectorEventUser {
    public JoinPrivateGameFragment() {
        super(R.layout.fragment_join_private_game);
    }

    private static final String TAG = JoinPrivateGameFragment.class.getSimpleName();

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


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

            ((MultiplayerWaitingRoom)getActivity()).badInput("Player name cannot be empty");
            return false;
        }
        if ( gameCode.isEmpty()) {

            ((MultiplayerWaitingRoom)getActivity()).badInput("game code cannot be empty");
            return false;
        }

        JSONObject args = new JSONObject();
        try {
            args.put("playerName", playerName);
            args.put("roomName",gameCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MultiPlayerConnector._EmitObject=args;

        Bundle result = new Bundle();
        result.putString("eventName", ServerConfig.joinPrivateGameRoom);
        result.putBoolean(MultiPlayerConnector.emitWithObjectString, true);
        // The child fragment needs to still set the result on its parent fragment manager
        getParentFragmentManager().setFragmentResult("emitEvent", result);


        return true;

    }


    static void AddSocketEvents(Socket socket, MultiPlayerConnector multiPlayerConnector){

        socket.on(ServerConfig.unableToFindRoom, args -> {
            Log.d(TAG, "unable to find room");
            multiPlayerConnector.notifyObservers(ServerConfig.unableToFindRoom);

        });

    }


}
