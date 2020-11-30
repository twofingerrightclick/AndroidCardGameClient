package com.example.cardgameclient;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PrivateGameWaitingRoomFragment extends Fragment {
    public PrivateGameWaitingRoomFragment() {
        super(R.layout.fragment_private_game_waiting_room);
    }



    String _DefaultFragmentStatusMessage ="Private Game Joined";
    String _CreatorStatusMessage ="Private Game Creator";



    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle extras = getArguments();

        boolean gameCreator=false;
        gameCreator= extras.getBoolean("gameCreator");

        if (gameCreator) {
            TextView statusMessage = view.findViewById(R.id.statusMessage);
            statusMessage.setText(_CreatorStatusMessage);
        }


    }
}