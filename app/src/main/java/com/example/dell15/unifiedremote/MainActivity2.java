package com.example.dell15.unifiedremote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import static com.example.dell15.unifiedremote.MainActivity1.*;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener,AsyncTaskCompleteListener {

Button b1,b2,b3,b4,b5;
    EditText t3,t4;
    private boolean isConnected = false;
    //private boolean mouseMoved=false;
    private Socket socket;
    private PrintWriter out;
    private static int port = 1;
    private static String server_name="192.168.43.43";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        b1 = (Button) findViewById(R.id.button5);
        b2 = (Button) findViewById(R.id.button6);
        b3 = (Button) findViewById(R.id.button7);
        b4 = (Button) findViewById(R.id.button8);
        b5 = (Button) findViewById(R.id.button10);
        t3 = (EditText) findViewById(R.id.editText3);
        t4 = (EditText) findViewById(R.id.editText4);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        b5.setOnClickListener(this);


    }

public void onBackPressed()
{
    Intent i5 = new Intent(this,MainActivity1.class);
    startActivity(i5);
}
    @Override
    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.button5:
                if (isConnected && out != null)
                    out.println("ShutDown");
                break;

            case R.id.button6:
                if (isConnected && out != null)
                    out.println("Switch User");
                break;

            case R.id.button7:
                if (isConnected && out != null)
                    out.println("Sleep");
                break;

            case R.id.button8:
                if (isConnected && out != null)
                    out.println("Lock");
                break;

            case R.id.button10:
                if (!isConnected) {
                    server_name = t3.getText().toString();
                    port = Integer.parseInt(t4.getText().toString());
                    launchTask(server_name, port, isConnected);

                }
                break;


            default:
                break;

        }

        }

    @Override
    public void onTaskComplete(PrintWriter out) {
        System.out.println("Hello world !");
        this.out = out;

        isConnected = true;
    }

    public void launchTask(String server_name,int port,boolean isConnected) {
        MySyncTask a = new MySyncTask(server_name,port,getBaseContext(),this);
        a.execute();

    }
}

