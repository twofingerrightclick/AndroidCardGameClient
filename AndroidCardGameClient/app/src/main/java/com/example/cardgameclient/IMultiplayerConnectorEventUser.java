package com.example.cardgameclient;

import io.socket.client.Socket;

public interface IMultiplayerConnectorEventUser {

     static void AddSocketEvents(Socket socket,  MultiPlayerConnector multiPlayerConnector){};
}
