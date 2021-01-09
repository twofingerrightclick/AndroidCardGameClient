package com.example.cardgameclient;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Observer;

public abstract class MultiplayerWaitingRoomActivityFragment extends Fragment {

    MultiPlayerConnector _MultiplayerConnector = MultiPlayerConnector.get_Instance();
    Observer _MultiPlayerConnectorObserver;
    MultiplayerWaitingRoomActivity _MultiplayerWaitingRoomActivity;

    public MultiplayerWaitingRoomActivityFragment(int fragment_public_game_waiting_room) {
        super(fragment_public_game_waiting_room);
    }



    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         _MultiplayerWaitingRoomActivity = (MultiplayerWaitingRoomActivity) getActivity();
    }

    @Override
    public void onPause() { //make sure events don't fire unless fragment is in use
        super.onPause();
  if(_MultiPlayerConnectorObserver!=null)
            _MultiplayerConnector.deleteObserver(_MultiPlayerConnectorObserver);


    }

    @Override
    public void onResume() { //make sure events don't fire unless fragment is in use

        super.onResume();
        if(_MultiPlayerConnectorObserver!=null)
        _MultiplayerConnector.addObserver(_MultiPlayerConnectorObserver);

    }




}
