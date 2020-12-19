package com.example.cardgameclient;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentResultListener;

import android.os.Handler;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

public class MultiplayerWaitingRoom extends AppCompatActivity implements IPublicGameWaitingRoomFragmentUIEvent, IPrivateGameWaitingRoomFragmentUIEvent, IJoinPrivateGameUIEvent {

    MultiPlayerConnector _MultiPlayerConnector;
    public Handler _UIHandler;
    public static String _GameType="fives";
    public static int _MinNumPlayersRequiredForGame = 2;

    String _CurrentFragmentClassName;

    String _GameCode="";



    static class PlayerStatus{
    static boolean _initiator =false;
    static String _PlayerName;
}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_waiting_room);
        initializeFragmentResultListeners();
        _UIHandler = new Handler();

         // get notified on updates from the MultiplayerConnector
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        _GameType = intent.getStringExtra(MainActivity.GAME_TYPE);
        _MultiPlayerConnector = new MultiPlayerConnector();
        _MultiPlayerConnector.addObserver(_MultiPlayerConnectorObserver);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.fragment_container_view, SelectPublicOrPrivateFragment.class, null)
                    .commit();
        }


        Snackbar.make(findViewById(R.id.multiPlayerWaitingRoomCoordinatorLayout), _GameType, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }



    private void initializeFragmentResultListeners() {
        getSupportFragmentManager().setFragmentResultListener("changeFragment", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                // We use a String here, but any type that can be put in a Bundle is supported
                String fragmentClassName = bundle.getString("fragmentClassName");
                _CurrentFragmentClassName=fragmentClassName;
                // Do something with the result
                changeFragment(fragmentClassName,bundle);
            }
        });

        getSupportFragmentManager().setFragmentResultListener("emitEvent", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                String eventName = bundle.getString("eventName");
                boolean emitWithObject = bundle.getBoolean("emitWithObject",false);
                _MultiPlayerConnector.emitEvent(eventName, emitWithObject);
            }
        });
    }



    private void resetPlayerStatus(){
        PlayerStatus._initiator=false;
    }

    protected void changeFragment(String className, Bundle bundle){
        Class fragmentClass = null;
        try {
            fragmentClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_container_view, fragmentClass, bundle)
                .commit();

    }




    @Override
    public void OnPrivateRoomJoin() {
        Bundle result = new Bundle();
        result.putString("fragmentClassName", PrivateGameWaitingRoomFragment.class.getCanonicalName());
        result.putBoolean("gameCreator", false);
        changeFragment(PrivateGameWaitingRoomFragment.class.getCanonicalName(),result);
    }

    @Override
    public void OnRoomNotFound() {

        _UIHandler.post(() -> {
            new AlertDialog.Builder(this)
                    .setTitle("Unable to find game")
                    .setMessage("Check with your friend to make sure your game code is still active and correct.")
                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    /* .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface dialog, int which) {
                             // Continue with delete operation
                         }
                     })*/

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.ok, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });
    }


    public void badInput(String message) {

        _UIHandler.post(() -> {
            new AlertDialog.Builder(this)
                    .setTitle("Check Input")
                    .setMessage(message)
                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    /* .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface dialog, int which) {
                             // Continue with delete operation
                         }
                     })*/

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.ok, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });
    }


    public void goToPrivateGameWaitingRoom() {

        Bundle bundle = new Bundle();
        bundle.putBoolean("gameCreator", true);
        changeFragment(PrivateGameWaitingRoomFragment.class.getCanonicalName(),bundle);

        _UIHandler.post(() -> {
            TextView roomCodeView= findViewById(R.id.gameCodeView);
            roomCodeView.setText(_MultiPlayerConnector.getRoomCode());
        });

    }


    private Observer _MultiPlayerConnectorObserver = new Observer() {
        @Override
        public void update(Observable o, Object arg) {
            switch ((String)arg){
                case ServerConfig.privateGameRoomRequestComplete:
                    goToPrivateGameWaitingRoom();
                    break;
                case ServerConfig.peerMsg:
                    //addPeerMessage();
                    break;
                case ServerConfig.numActivePlayers:
                    updatePublicWaitingRoomActivePlayerCount();
                    break;
                case ServerConfig.unableToFindRoom:
                    OnRoomNotFound();
                    break;
            }
        }
    };

  /*  private void addPeerMessage() {

        _UIHandler.post(new Runnable() {
            @Override
            public void run() {
                TextView roomCodeView= findViewById(R.id.message_view);
                roomCodeView.setText(_MultiPlayerConnector.msg);

            }
        });
    }*/

        public void updatePublicWaitingRoomActivePlayerCount() {

        _UIHandler.post(new Runnable() {
            @Override
            public void run() {
                TextView activePlayersTextView= findViewById(R.id.numActivePublicPlayers);
                activePlayersTextView.setText(_MultiPlayerConnector._NumberActivePublicPlayers);

            }
        });

    }

    @Override
    public void onGameReadyToPlay() {

    }
}