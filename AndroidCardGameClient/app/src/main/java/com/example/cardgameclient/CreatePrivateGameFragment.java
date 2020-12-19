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

public class CreatePrivateGameFragment extends Fragment {
    public CreatePrivateGameFragment() {
        super(R.layout.fragment_create_private_game);
    }

    private static final String TAG = CreatePrivateGameFragment.class.getSimpleName();

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button createButton = view.findViewById(R.id.createButton);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //p2psocket.emit('private-game-room-request', {numPlayersRequiredForGame:2, gameType: 'fives'})
                //_ParentActivity._MultiPlayerConnector.joinToPublicGame(2, _ParentActivity._GameType);
                TextView playerNameInput= view.findViewById(R.id.playerNameInput);

                createButton.setClickable(false); ///wait until success

                if(emitCreatePrivateGame(playerNameInput)) {

                  /*  Bundle result = new Bundle();
                    result.putString("fragmentClassName", PrivateGameWaitingRoomFragment.class.getCanonicalName());
                    result.putBoolean("gameCreator", true);
                    // The child fragment needs to still set the result on its parent fragment manager
                    getParentFragmentManager().setFragmentResult("changeFragment", result);*/

                }
                else{
                    createButton.setClickable(true);
                }
                //getParentFragmentManager().setFragmentResult("setGameCreator", result);
            }


        });

    }

    private boolean emitCreatePrivateGame(TextView playerNameTextInput) {
        String playerName = playerNameTextInput.getText().toString();

        if (playerName.isEmpty()) {

            ((MultiplayerWaitingRoom)getActivity()).badInput("Player name cannot be empty");
            return false;
        }

        JSONObject args = new JSONObject();
        try {
            args.put("playerName", playerName);
            args.put("minPlayersRequiredForGame",MultiplayerWaitingRoom._MinNumPlayersRequiredForGame);
            args.put("gameType", MultiplayerWaitingRoom._GameType);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        ((MultiplayerWaitingRoom)getActivity())._MultiPlayerConnector.emitEvent(ServerConfig.privateGameRoomRequest,args);


        return true;

    }

    static void AddSocketEvents(Socket socket, MultiPlayerConnector multiPlayerConnector){

        socket.on(ServerConfig.privateGameRoomRequestComplete, args -> {
            Log.d(TAG, "created Room");
            multiPlayerConnector.setRoomCode(((JSONObject)args[0]).opt("gameRoomName").toString());
            multiPlayerConnector.notifyObservers(ServerConfig.privateGameRoomRequestComplete);

        });

    }

}
