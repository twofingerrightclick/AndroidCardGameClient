package com.example.cardgameclient;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;

public class SelectPublicOrPrivateFragment extends Fragment implements IMultiplayerConnectorEventUser {

    MultiplayerWaitingRoom _ParentActivity;
    private static final String TAG = SelectPublicOrPrivateFragment.class.getSimpleName();

        public SelectPublicOrPrivateFragment() {
            super(R.layout.fragment_select_public_or_private_buttons);
        }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
       

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _ParentActivity=(MultiplayerWaitingRoom)getActivity();

        Button joinPrivateButton = view.findViewById(R.id.privateButton);

        Button joinPublicButton = view.findViewById(R.id.publicButton);

        joinPrivateButton.setOnClickListener(v -> {
            //p2psocket.emit('private-game-room-request', {numPlayersRequiredForGame:2, gameType: 'fives'})
            //_ParentActivity._MultiPlayerConnector.joinToPublicGame(2, _ParentActivity._GameType);
            Bundle result = new Bundle();
            result.putString("fragmentClassName", PrivateGameOptionsFragment.class.getCanonicalName());
            // The child fragment needs to still set the result on its parent fragment manager
            getParentFragmentManager().setFragmentResult("changeFragment", result);
        });

        joinPublicButton.setOnClickListener(v -> joinToPublicGame());


    }


    private void joinToPublicGame(){

        JSONObject obj = new JSONObject();
        try {
            obj.put("minPlayersRequiredForGame", MultiplayerWaitingRoom._MinNumPlayersRequiredForGame);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            obj.put("gameType", MultiplayerWaitingRoom._GameType);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MultiPlayerConnector._EmitObject=obj;

        Bundle publicGameRoomRequestBundle = new Bundle();
        publicGameRoomRequestBundle.putString("eventName", ServerConfig.publicGameRoomRequest);
        publicGameRoomRequestBundle.putBoolean(MultiPlayerConnector.emitWithObjectString, true);
        // The child fragment needs to still set the result on its parent fragment manager
        getParentFragmentManager().setFragmentResult("emitEvent", publicGameRoomRequestBundle);

        Bundle getNumPlayersBundle = new Bundle();
        getNumPlayersBundle.putString("eventName", ServerConfig.getNumActivePlayers);
        // The child fragment needs to still set the result on its parent fragment manager
        getParentFragmentManager().setFragmentResult("emitEvent", getNumPlayersBundle);

        Log.d(TAG, "requesting public game room");

        Bundle result = new Bundle();
        result.putString("fragmentClassName", PublicGameWaitingRoom.class.getCanonicalName());
        // The child fragment needs to still set the result on its parent fragment manager
        getParentFragmentManager().setFragmentResult("changeFragment", result);

    }





    public static void AddSocketEvents(Socket socket, MultiPlayerConnector multiPlayerConnector){

    }

}
