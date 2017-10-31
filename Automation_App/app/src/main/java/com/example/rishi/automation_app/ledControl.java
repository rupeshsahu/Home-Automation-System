package com.example.rishi.automation_app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.UUID;


public class ledControl extends ActionBarActivity {

    Button btnDis;
    ToggleButton tb5;
    ToggleButton tb6;
    ToggleButton tb7;
    ToggleButton tb8;


    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent newint = getIntent();
        address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS); //receive the address of the bluetooth device

        //view of the ledControl
        setContentView(R.layout.activity_led_control);

        //call the widgtes

        btnDis = (Button)findViewById(R.id.button4);

        tb5= (ToggleButton) findViewById(R.id.toggleButton5);
        tb6= (ToggleButton) findViewById(R.id.toggleButton6);
        tb7= (ToggleButton) findViewById(R.id.toggleButton7);
        tb8= (ToggleButton) findViewById(R.id.toggleButton8);


        new ConnectBT().execute(); //Call the class to connect

        //commands to be sent to bluetooth

            tb5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
                    if(isChecked){
                       turnOn12();
                    }
                    else{
                        turnOff12();
                    }
                }
            });
        tb6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
                if(isChecked){
                    turnOn11();
                }
                else{
                    turnOff11();
                }
            }
        });
        tb7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
                if(isChecked){
                    turnOn10();
                }
                else{
                    turnOff10();
                }
            }
        });
        tb8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
                if(isChecked){
                    turnOn8();
                }
                else{
                    turnOff8();
                }
            }
        });



        btnDis.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Disconnect(); //close connection
            }
        });


    }

    private void Disconnect()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            { msg("Error");}
        }
        finish(); //return to the first layout

    }
    private void turnOn12()
    {

        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("TOA".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }


    private void turnOff12()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("TFA".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void turnOn11()
    {

        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("TOB".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void turnOff11()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("TFB".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void turnOn10()
    {

        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("TOC".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void turnOff10()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("TFC".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }


    private void turnOn8()
    {

        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("TOD".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    private void turnOff8()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("TFD".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }




    // fast way to call Toast
    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_led_control, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(ledControl.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                 myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                 BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                 btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                 BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                 btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }
}
