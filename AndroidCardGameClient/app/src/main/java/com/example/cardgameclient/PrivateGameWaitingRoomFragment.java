package com.example.cardgameclient;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class PrivateGameWaitingRoomFragment extends Fragment {
    public PrivateGameWaitingRoomFragment() {
        super(R.layout.fragment_private_game_waiting_room);
    }



    String _DefaultFragmentStatusMessage ="Private Game Joined";
    String _CreatorStatusMessage ="Private Game Creator";
    boolean _PlayerIsInitiator=false;
    MultiplayerWaitingRoom _ParentActivity;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        OnBackPressedCallback callback = new OnBackPressedCallback(
                true // default to enabled
        ) {
            @Override
            public void handleOnBackPressed() {
                showAreYouSureDialog();
            }

            private void showAreYouSureDialog() {

                ((MultiplayerWaitingRoom)getActivity())._UIHandler.post(() -> {
                    new AlertDialog.Builder(context)
                            .setTitle("Close Game?")
                            .setMessage(getBackButtonMessage())
                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                    goBackToSelectPrivateOrPublic();
                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                });

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(
                this, // LifecycleOwner
                callback);
    }

    private void goBackToSelectPrivateOrPublic() {
        _ParentActivity.changeFragment(SelectPublicOrPrivateFragment.class.getCanonicalName(), null);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _ParentActivity= ((MultiplayerWaitingRoom)getActivity());

        Bundle extras = getArguments();


        _PlayerIsInitiator= extras.getBoolean("gameCreator");

        if (_PlayerIsInitiator) {
            TextView statusMessage = view.findViewById(R.id.statusMessage);
            statusMessage.setText(_CreatorStatusMessage);
        }

    }

    private String getBackButtonMessage() {

        if (_PlayerIsInitiator){
             return "If you leave the game will be deleted";
        }
        return "If you leave you will have to rejoin the game.";
    }
}