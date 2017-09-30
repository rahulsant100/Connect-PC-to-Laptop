package com.example.dell15.unifiedremote;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.UnknownHostException;

import static android.widget.Toast.LENGTH_SHORT;
interface AsyncTaskCompleteListener<T> {
    public void onTaskComplete(PrintWriter result);
}

public class MainActivity1 extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener,AsyncTaskCompleteListener<PrintWriter> {

    Button b1, b2, b3, b4,b5, b6 ,b7, b8;
    private boolean isConnected = false;
    //private boolean mouseMoved=false;
    private Socket socket;
    private PrintWriter out;
    EditText t1, t2;
    TextView q1;
    private static int port;
    private static String server_name;
    private boolean mouseMoved=false;
    private float initX =0;
    private float initY =0;
    private float disX =0;
    private float disY =0;
    private long lastTouchDown;
    private static int CLICK_ACTION_THRESHHOLD = 200;
    public static boolean n1=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        b1 = (Button) findViewById(R.id.button);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.button3);
        t1 = (EditText) findViewById(R.id.editText);
        t2 = (EditText) findViewById(R.id.editText2);
        b4 = (Button) findViewById(R.id.button4);
        q1 = (TextView) findViewById(R.id.textView2);
        b5 = (Button) findViewById(R.id.button9);
        b6 = (Button) findViewById(R.id.button11);
        b7 = (Button) findViewById(R.id.button12);
        b8 = (Button) findViewById(R.id.button13);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        q1.setOnTouchListener(this);
        b5.setOnClickListener(this);
        b6.setOnClickListener(this);
        b7.setOnClickListener(this);
        b8.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return true;

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                if (isConnected && out != null)
                    out.println("Play");
                break;

            case R.id.button2:
                if (isConnected && out != null)
                    out.println("Next");
                break;

            case R.id.button3:
                if (isConnected && out != null)
                    out.println("Previous");
                break;

            case R.id.button4:
                if(!isConnected)
                {
                    server_name = t2.getText().toString();
                    port = Integer.parseInt(t1.getText().toString());
                    launchTask(server_name,port,isConnected);

                }
                break;

            case R.id.button9:
                if (isConnected && out != null)
                    out.println("ShutDown");
                break;

            case R.id.button11:
                if (isConnected && out != null)
                    out.println("Restart");
                break;

            case R.id.button12:
                if (isConnected && out != null)
                    out.println("Sleep");
                break;


            case R.id.button13:
                if (isConnected && out != null)
                    out.println("Lock");
                break;

            default:
                break;

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isConnected && out != null) {
            try {
                out.println("exit"); //tell server to exit
                socket.close(); //close socket
            } catch (IOException e) {
                Log.e("remotedroid", "Error in closing socket", e);
            }
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
    {
        if(isConnected && out!=null)
        {
            switch(motionEvent.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    initX = motionEvent.getX();
                    initY = motionEvent.getY();
                    lastTouchDown = System.currentTimeMillis();
                    mouseMoved = false;
                    break;

               case MotionEvent.ACTION_MOVE:
                    disX = motionEvent.getX() - initX;
                    disY = motionEvent.getY() - initY;
                    initX = motionEvent.getX();
                    initY = motionEvent.getY();

                    if(disX!=0 && disY!=0)
                    out.println(disX+","+disY);

                    mouseMoved = true;
                    break;

                case MotionEvent.ACTION_UP:
                    if (System.currentTimeMillis() - lastTouchDown < CLICK_ACTION_THRESHHOLD)
                    {
                        out.println("left_click");
                    }




            }
        }
        return true;
    }

    @Override
    public void onTaskComplete(PrintWriter out)
    {System.out.println("Hello world !");
        this.out = out;

        isConnected = true;
    }

    public void launchTask(String server_name,int port,boolean isConnected) {
        MySyncTask a = new MySyncTask(server_name,port,getBaseContext(),this);
        a.execute();

    }


    public class ConnectPhoneTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            server_name = t2.getText().toString();
            port = Integer.parseInt(t1.getText().toString());

        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean result = true;

            try {

                InetSocketAddress inet = new InetSocketAddress(server_name,port);
                socket = new Socket();
                socket.bind(null);
                socket.connect(inet,0);
                isConnected = true;
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
                        .getOutputStream())), true);
                //onPostExecute1(true);

            } catch (Exception e) {
                System.out.println("Socket is not created");
                Log.e("remotedroid", "Error while connecting", e);
                result = false;
            }
            //onPostExecute1(result);
            return result;
        }
        public void onPostExecute1(Boolean result) {
            isConnected = result;
            Toast.makeText(getApplicationContext(), isConnected ? "You are connected" : "Error while connecting...", LENGTH_SHORT).show();
            if (isConnected) {
                try {
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
                            .getOutputStream())), true);
                } catch (IOException e) {
                    Log.e("remotedroid", "Error while creating OutWriter", e);
                    Toast.makeText(getApplicationContext(), "Error while connecting", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}



