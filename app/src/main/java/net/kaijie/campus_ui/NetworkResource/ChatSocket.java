package net.kaijie.campus_ui.NetworkResource;

import android.os.Build;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.Transport;

/**
 * Created by Rabbit on 2017/11/14.
 */
public class ChatSocket {

    private static final String Server = "https://luhao.ddns.net:5050/";
    private Socket mSocket;
    public boolean isConnected;
    private int roomID;
    public static final String Add_User = "add user";
    public static final String Chat_Msg = "chat message";
    private SocketCallback socketCallback;

    public ChatSocket(SocketCallback socketCallback){
        this.socketCallback = socketCallback;
        //mSocket = IO.socket(Server);
        initSocketHttps();
        Emitter.Listener onTransport = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Transport transport = (Transport) args[0];
                transport.on(Transport.EVENT_REQUEST_HEADERS, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        @SuppressWarnings("unchecked")
                        Map<String, List<String>> headers = (Map<String, List<String>>) args[0];
                        headers.put("referer", Collections.singletonList(Server + "/socket/" + roomID));
                        headers.put("deviceUID", Collections.singletonList(Build.SERIAL));
                    }
                }).on(Transport.EVENT_RESPONSE_HEADERS, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                    }
                });
            }
        };
        mSocket.io().on(Manager.EVENT_TRANSPORT, onTransport);
    }

    private void initSocketHttps() {
        SSLContext sc;
        TrustManager[] trustCerts = new TrustManager[] { new X509TrustManager() {

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkServerTrusted( X509Certificate[] chain, String authType )
                    throws CertificateException {
            }

            @Override
            public void checkClientTrusted( X509Certificate[] chain, String authType )
                    throws CertificateException {
            }
        } };
        try {
            sc = SSLContext.getInstance( "TLS" );
            sc.init( null, trustCerts, null );
            IO.Options opts = new IO.Options();
            opts.sslContext = sc;
            opts.hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify( String s, SSLSession sslSession ) {
                    return true;
                }
            };
            mSocket = IO.socket( Server , opts );
        } catch ( NoSuchAlgorithmException | URISyntaxException | KeyManagementException e ) {
            e.printStackTrace();
        }
    }

    public void setSocketCallback(SocketCallback socketCallback){
        this.socketCallback = socketCallback;
    }

    public void connect(int roomID, Object... id){
        this.roomID = roomID;

        mSocket.connect();
        mSocket.emit(Add_User , id);
        mSocket.on( Socket.EVENT_CONNECT, onConnect );
        mSocket.on( Socket.EVENT_DISCONNECT, onDisconnect );
        mSocket.on( Socket.EVENT_CONNECT_ERROR, onConnectError );
        mSocket.on( Socket.EVENT_CONNECT_TIMEOUT, onConnectTimeoutError );
        mSocket.on( Add_User, onConnectServerMsg );
        mSocket.on( Chat_Msg , onConnectServerMsg);
        mSocket.on( "user left" ,onConnectServerMsg );

    }

    public void disconnect() {
        mSocket.disconnect();
        isConnected = false;
        mSocket.off( Socket.EVENT_CONNECT, onConnect );
        mSocket.off( Socket.EVENT_DISCONNECT, onDisconnect );
        mSocket.off( Socket.EVENT_CONNECT_ERROR, onConnectError );
        mSocket.off( Socket.EVENT_CONNECT_TIMEOUT, onConnectTimeoutError );
        mSocket.off( Add_User, onConnectServerMsg );
        mSocket.off( Chat_Msg , onConnectServerMsg);
        mSocket.off( "user left" ,onConnectServerMsg );
    }

    public void emit(String topic,Object msg){
        mSocket.emit(topic, msg);
    }

    private Emitter.Listener onConnectServerMsg = new Emitter.Listener() {
        @Override
        public void call( final Object... args ) {
            Log.d( "Server_Socket_msg", "伺服器回覆 : " + args[0] );
            socketCallback.onReceived(args[0].toString());
        }
    };

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call( final Object... args ) {
            if (!isConnected) {
                isConnected = true;
            }
            Log.d( "Server_Socket", "連線成功");
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call( Object... args ) {
            Log.d( "Server_Socket", "已斷線 " + args[0] );
            isConnected = false;
            socketCallback.onError(new Exception(args[0].toString()));
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call( final Object... args ) {
            Log.d( "Server_Socket", "連線失敗 " + args[0] );
            disconnect();
            socketCallback.onError(new Exception(args[0].toString()));
        }
    };

    private Emitter.Listener onConnectTimeoutError = new Emitter.Listener() {
        @Override
        public void call( final Object... args ) {
            Log.d( "Server_Socket", "連線逾時 " + args[0] );
            disconnect();
            socketCallback.onError(new Exception(args[0].toString()));
        }
    };

    public interface SocketCallback{
        void onReceived(String result);
        void onError(Exception err);
    }
}
