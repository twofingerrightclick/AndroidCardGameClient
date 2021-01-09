package com.example.cardgameclient;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentResultListener;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.net.URISyntaxException;
import java.util.Observable;
import java.util.Observer;

public class MultiplayerWaitingRoomActivity extends AppCompatActivity  {

    MultiPlayerConnector _MultiPlayerConnector;
    public Handler _UIHandler;
    public static String _GameType="fives";
    public static int _MinNumPlayersRequiredForGame = 2;

    String _CurrentFragmentClassName;

    String _GameCode="";
    private static final String TAG = MultiplayerWaitingRoomActivity.class.getSimpleName();

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


        _MultiPlayerConnector = MultiPlayerConnector.get_Instance();
        try {
            _MultiPlayerConnector.connectToSignallingServer();
        } catch (URISyntaxException e) {
           Log.d(TAG, "socket.io server url is malformed. check url in Server.Config");
           e.printStackTrace();
        }
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


//Delete and just use _MultiplayerWaitingRoomActivity.changeFragment
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


    }



    private void resetPlayerStatus(){
        PlayerStatus._initiator=false;
    }

    protected void changeFragment(String className, Bundle bundle ){
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





    /**
     * Displays a warning dialog to the user with passed message as the contents
     * @param message
     */
    public void badInputDialog(String message) {

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



   /* public void retryConnectionDialog(String message) {

        _UIHandler.post(() -> {
            new AlertDialog.Builder(this)
                    .setTitle("Unable to connect to server")
                    .setMessage(message)

                    .setPositiveButton("Yes", (dialog, id) -> {

                        retryConnectingToServer();
                    })
                    .setNegativeButton("No", (dialog, id) -> {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        });
    }*/

   /* private void retryConnectingToServer(){
        try {
            _MultiPlayerConnector.connectToSignallingServer();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
*/





    private Observer _MultiPlayerConnectorObserver = new Observer() {
        @Override
        public void update(Observable o, Object arg) {
            switch ((String)arg){

                case ServerConfig.peerMsg:
                    //addPeerMessage();
                    break;
                case ServerConfig.unableToFindRoom:
                    ///OnRoomNotFound();
                    break;
               /* case ServerConfig.eventConnectError:
                    badInputDialog("Unable To Connect To Server WaitingRoomActivity");
                    break;*/
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



    public void onGameReadyToPlay() {

    }
}