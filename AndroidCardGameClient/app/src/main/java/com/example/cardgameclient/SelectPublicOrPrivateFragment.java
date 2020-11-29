package com.example.cardgameclient;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SelectPublicOrPrivateFragment extends Fragment {

    MultiplayerWaitingRoom _ParentActivity;

        public SelectPublicOrPrivateFragment() {
            super(R.layout.fragment_select_public_or_private_buttons);
        }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        TextView statusMessage =getActivity().findViewById(R.id.statusMessage);
        statusMessage.setText("Multiplayer Options");
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        _ParentActivity=(MultiplayerWaitingRoom)getActivity();

        Button joinPrivateButton = view.findViewById(R.id.privateButton);

        Button joinPublicButton = view.findViewById(R.id.publicButton);

        joinPrivateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //p2psocket.emit('private-game-room-request', {numPlayersRequiredForGame:2, gameType: 'fives'})
                //_ParentActivity._MultiPlayerConnector.joinToPublicGame(2, _ParentActivity._GameType);

                Bundle result = new Bundle();
                result.putString("fragmentClassName", PrivateGameOptionButtons.class.getCanonicalName());
                // The child fragment needs to still set the result on its parent fragment manager
                getParentFragmentManager().setFragmentResult("changeFragment", result);
            }


        });

        joinPublicButton.setOnClickListener(new View.OnClickListener() {
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
