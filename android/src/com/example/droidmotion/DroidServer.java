package com.example.droidmotion;

import java.net.InetSocketAddress;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import android.util.Log;

public class DroidServer extends WebSocketServer {

	private static DroidServer server = null; 
	
	private static InetSocketAddress address = new InetSocketAddress(3139);
	
	private static WebSocket chromeClientSocket = null;
	
	
	public static WebSocket getChromeClientSocket() {
		return chromeClientSocket;
	}
	
	public static DroidServer getDroidServer() {
		return server == null ? server = new DroidServer(address) : server;
	}
	
	private DroidServer(InetSocketAddress address) {
		super(address);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {

		if( conn != chromeClientSocket && chromeClientSocket != null ){
			denyClient(conn);
			return;
		}
		
		Log.i("DroidMotion", "on open");
		chromeClientSocket = conn;
		chromeClientSocket.send("{\"type\":\"hello\",\"name\":\"server_connected\",\"data\":{}}");
	}
	
	@Override
	public void onMessage(WebSocket conn, String message) {
		
		if( conn != chromeClientSocket ){
			return;
		}
		
		Log.i("DroidMotion", "on message : " + message);
		
		chromeClientSocket.send("{\"type\":\"text\",\"name\":\"receive_text\",\"data\":{\"rec\":\""+message+"\"}");
	}
	
	@Override
	public void onError(WebSocket conn, Exception ex) {
		if( conn != chromeClientSocket ){
			return;
		}
		
		Log.i("DroidMotion", "on err");

	}
	
	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		if( conn != chromeClientSocket ){
			return;
		}
		
		Log.i("DroidMotion", "on close");
		
		chromeClientSocket = null;
	}

	private static void denyClient(WebSocket conn) {

		conn.send("{\"type\":\"err\",\"name\":\"exception\",\"data\":{\"reason\":\"can only serve one client\"}");
		conn.closeConnection(0, "0");
		Log.i("DroidMotion", "deny a client");
	}
}
