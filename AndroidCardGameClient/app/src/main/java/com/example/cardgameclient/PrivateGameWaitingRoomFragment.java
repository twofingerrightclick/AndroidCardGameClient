package com.example.cardgameclient;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

class PrivateGameWaitingRoomFragment extends Fragment {
    public PrivateGameWaitingRoomFragment() {
        super(R.layout.fragment_private_game_waiting_room);
    }


    String _DefaultFragmentStatusMessage ="Private Game Joined";

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView statusMessage = getActivity().findViewById(R.id.statusMessage);

        statusMessage.setText(_DefaultFragmentStatusMessage);
    }
}