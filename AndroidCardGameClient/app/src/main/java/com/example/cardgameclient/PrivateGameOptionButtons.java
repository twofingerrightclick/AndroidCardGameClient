package com.example.cardgameclient;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PrivateGameOptionButtons extends Fragment {
    public PrivateGameOptionButtons() {
        super(R.layout.fragment_private_options_buttons);
    }

    String _DefaultFragmentStatusMessage ="Private Game Options";

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView statusMessage = getActivity().findViewById(R.id.statusMessage);

        statusMessage.setText(_DefaultFragmentStatusMessage);


        Button createButton = view.findViewById(R.id.createPrivateButton);

        Button joinButton = view.findViewById(R.id.joinPrivateButton);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //p2psocket.emit('private-game-room-request', {numPlayersRequiredForGame:2, gameType: 'fives'})
                //_ParentActivity._MultiPlayerConnector.joinToPublicGame(2, _ParentActivity._GameType);

                Bundle result = new Bundle();
                result.putString("fragmentClassName", PrivateGameWaitingRoomFragment.class.getCanonicalName());
                result.putBoolean("gameCreator", true);
                // The child fragment needs to still set the result on its parent fragment manager
                getParentFragmentManager().setFragmentResult("changeFragment", result);
                getParentFragmentManager().setFragmentResult("setGameCreator", result);
            }


        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //p2psocket.emit('public-game-room-request', {numPlayersRequiredForGame:2, gameType: 'fives'})
                //_ParentActivity._MultiPlayerConnector.joinToPublicGame(2, _ParentActivity._GameType);

               /* Bundle result = new Bundle();
                result.putString("fragmentClassName", Public.class.getCanonicalName());
                // The child fragment needs to still set the result on its parent fragment manager
                getParentFragmentManager().setFragmentResult("changeFragment", result);*/
            }


        });


    }
}
