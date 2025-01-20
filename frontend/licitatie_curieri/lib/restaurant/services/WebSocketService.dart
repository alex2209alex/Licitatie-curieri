import 'dart:convert';
import 'dart:developer';
import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:web_socket_channel/io.dart';
import 'package:web_socket_channel/web_socket_channel.dart';
import 'package:websocket_universal/websocket_universal.dart';

import '../../common/GetToken.dart';

class WebSocketService {
  late WebSocketChannel _webSocket;
  bool _isConnected = false;

  Function(String)? onMessageReceivedCallback;

  final String serverUrl = 'ws://192.168.1.132:8080/web-socket';

  // Connect to the WebSocket server
  Future<void> connect() async {
    String? token = await GetToken().getToken();
    log("token:Bearer $token");
    if (token == null) {
      throw Exception("Authentication token not found");
    }
    String? bearerToken = "Bearer $token";
    // Add token as a query parameter or header if needed
    final uri = Uri.parse('$serverUrl');
    //final textSocketHandler = IWebSocketHandler<String,String>.createClient(uri.toString(), SocketSimpleTextProcessor());
    //await textSocketHandler.connect();
    //textSocketHandler.sendMessage("am trimis de pe FE");
    log("Urmeaze IOWEBSOCKETCHANNEL");
    _webSocket = IOWebSocketChannel.connect(uri, headers: {"Authorization" : "Bearer $token}"});
    _webSocket.stream.listen(
        (message) {
          if (onMessageReceivedCallback != null) {
            onMessageReceivedCallback!(message);
          }
        },
      onDone: () {
        _isConnected = false;
        log("WebSocket connection closed");
      },
      onError: (error)
        {
          _isConnected = false;
          log('WebSocket error: $error');
        },
    );
    _isConnected = true;

    log('Connected to WebSocket: $serverUrl');
  }

  // Send data to the WebSocket server
  void send(Map<String, dynamic> messageData) {
    if (_isConnected) {
      final message = jsonEncode(messageData);
      _webSocket.sink.add(message);
      log('WebSocket message sent: $message');
    } else {
      log('Cannot send message. WebSocket is not connected.');
    }
  }

  // Disconnect from the WebSocket server
  void disconnect() {
    if (_isConnected) {
      _webSocket.sink.close();
      _isConnected = false;
      log('WebSocket connection closed.');
    }
  }

  // Set a callback for receiving messages
  void setOnMessageReceived(Function(String) callback) {
    onMessageReceivedCallback = callback;
  }
}
