package com.example.cardgameclient;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentResultListener;

import android.os.Handler;

public class MultiplayerWaitingRoom extends AppCompatActivity {

    MultiPlayerConnector _MultiPlayerConnector;
    public Handler _UIHandler;
    String _GameType;

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
        //multiPlayerConnector.addObserver(MultiPlayerConnectorObserver);


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
                // Do something with the result
                changeFragment(fragmentClassName,bundle);
            }
        });

        getSupportFragmentManager().setFragmentResultListener("setGameCreator", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
               PlayerStatus._initiator=true;
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






   /* private void showGameRoomCode() {

        _UIHandler.post(new Runnable() {
            @Override
            public void run() {
                TextView roomCodeView= findViewById(R.id.room_code);
                roomCodeView.setText(multiPlayerConnector.getRoomCode());

            }
        });

    }*/


 /*   private Observer MultiPlayerConnectorObserver = new Observer() {
        @Override
        public void update(Observable o, Object arg) {
            switch ((String)arg){
                case "game-room-request-complete":
                    showGameRoomCode();
                    break;
                case ServerConfig.peerMsg:
                    addPeerMessage();
            }
        }
    };*/

   /* private void addPeerMessage() {

        _UIHandler.post(new Runnable() {
            @Override
            public void run() {
                TextView roomCodeView= findViewById(R.id.message_view);
                roomCodeView.setText(multiPlayerConnector.msg);

            }
        });
    }*/
}