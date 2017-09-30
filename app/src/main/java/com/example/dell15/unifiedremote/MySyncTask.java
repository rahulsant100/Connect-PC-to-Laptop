package com.example.dell15.unifiedremote;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by dell  15 on 11/18/2016.
 */


public class MySyncTask extends AsyncTask<String, String, Boolean> {

    private Context context;
    public Socket socket;
    boolean connect;
    boolean result=false;
    private static int port=1;
    private static String server_name="192.168.43.43";
    boolean isConnected;
    PrintWriter out;
    private AsyncTaskCompleteListener<PrintWriter> callback;




    public MySyncTask(String server_name,int port,Context context, AsyncTaskCompleteListener<PrintWriter> cb) {
        this.server_name = server_name;
        this.port = port;
        this.context = context;
        this.callback = cb;
    }
    
    public MySyncTask(String server_name, int port, boolean isConnected)

    {
        this.server_name = server_name;
        this.port = port;
        this.isConnected = isConnected;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {

            InetSocketAddress inet = new InetSocketAddress(server_name,port);
           // connect = Boolean.parseBoolean(params[2]);
            socket = new Socket();
            socket.bind(null);
            socket.connect(inet,0);
            isConnected = true;
           // connect = true;
         //   out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
                 //   .getOutputStream())), true);
            //onPostExecute1(true);

        } catch (Exception e) {
            System.out.println("Socket is not created");
            Log.e("remotedroid", "Error while connecting", e);
            result = false;
        }
        //onPostExecute1(result);
        return result;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean)
    {
        try {
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            System.out.println("rahul");
        } catch (IOException e) {
            e.printStackTrace();
        }
        callback.onTaskComplete(out); ;

    }



}
