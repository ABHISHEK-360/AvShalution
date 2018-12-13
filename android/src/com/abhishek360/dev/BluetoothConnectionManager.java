package com.abhishek360.dev;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Set;

public abstract class BluetoothConnectionManager extends Activity implements BluetoothApi
{
    private int REQUEST_ENABLE_BT= 101;
    private Context context;
    private BluetoothAdapter bluetoothAdapter= BluetoothAdapter.getDefaultAdapter();
    private Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

    public BluetoothConnectionManager(Context ctx)
    {
        context=ctx;
    }

    @Override
    public boolean isBluetoothAvailable()
    {
        if(bluetoothAdapter==null)
        {
            //oast.makeText(context,"Bluetooth Not Available!",Toast.LENGTH_LONG).show();
            return false;

        }
        else
        {
            //Toast.makeText(context,"Bluetooth Not Available!",Toast.LENGTH_LONG).show();
            return true;

        }
    }

    @Override
    public void turnBluetoothOn(String data)
    {
        if (!bluetoothAdapter.isEnabled())
        {
            Intent btIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(btIntent,REQUEST_ENABLE_BT);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_ENABLE_BT)
        {
            //Text(context, "Bluetooth Enabled!", Toast.LENGTH_LONG).show();
            if (resultCode==RESULT_OK)
            {
            }
            else if(requestCode==RESULT_CANCELED)
            {
                //Toast.makeText(context,"Bluetooth Enable Failed!",Toast.LENGTH_LONG).show();


            }
        }
    }

    @Override
    public boolean startDiscoverBluetooth()
    {
        return false;
    }
}
