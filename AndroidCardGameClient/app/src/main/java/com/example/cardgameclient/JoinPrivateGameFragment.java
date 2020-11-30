package com.example.cardgameclient;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class JoinPrivateGameFragment extends Fragment {
    public JoinPrivateGameFragment() {
        super(R.layout.fragment_join_private_game);
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Button joinButton = view.findViewById(R.id.joinButton);

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //p2psocket.emit('private-game-room-request', {numPlayersRequiredForGame:2, gameType: 'fives'})
                //_ParentActivity._MultiPlayerConnector.joinToPublicGame(2, _ParentActivity._GameType);

                Bundle result = new Bundle();
                result.putString("fragmentClassName", PrivateGameWaitingRoomFragment.class.getCanonicalName());
                result.putBoolean("gameCreator", false);
                // The child fragment needs to still set the result on its parent fragment manager
                getParentFragmentManager().setFragmentResult("changeFragment", result);
                //getParentFragmentManager().setFragmentResult("setGameCreator", result);
            }


        });
    }
}
