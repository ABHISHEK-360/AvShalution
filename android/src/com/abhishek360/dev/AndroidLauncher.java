package com.abhishek360.dev;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.abhishek360.dev.FloorSimulation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class AndroidLauncher extends AndroidApplication implements BluetoothApi {
    private int REQUEST_ENABLE_BT = 101;
    private Context context;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private Set<BluetoothDevice> pairedDevices;
    private BluetoothDevice serverBluetoothDevice;
    private String heightData = " ";

    private final String bluetoothServerMac = "98:D3:31:80:A1:98";//pixel="40:4E:36:49:BC:DF";//j5="C0:11:73:56:8F:AB";
    private final UUID MY_UUID = UUID.fromString("4bbbaf50-50b4-4bc4-87d3-2986727021f9");


    @SuppressLint("HandlerLeak")
    private static Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
        String aResponse = msg.obj.toString();
        switch (msg.what) {
            case 1:
                Log.d("bluetooth_data_transfer", " " + aResponse);
                break;
            case -2:

                Log.d("bluetooth_connect", " " + aResponse);
                break;
            case 100:
                Log.d("bluetooth_connected_svr", "Connected");
                break;
            default:
                Log.d("bluetooth_handler", " Handler running");
        }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(bluetoothReceiver, filter);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new FloorSimulation(this), config);
    }

    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            String deviceName = device.getName();
            String deviceHardwareAddress = device.getAddress();
            Log.d("device_mac:", deviceHardwareAddress);

            if (deviceHardwareAddress.equals(bluetoothServerMac)) {
                bluetoothAdapter.cancelDiscovery();
                ConnectThread connectThread = new ConnectThread(device, MY_UUID, heightData);
                connectThread.run();

                Log.d("device_name_2:", deviceName);
            }
            //else  Log.d("device_name:",pairStatus);
        }
        }
    };


    @Override
    public boolean isBluetoothAvailable() {
        if (bluetoothAdapter == null) {
            //Toast.makeText(context,"Bluetooth Not Available!",Toast.LENGTH_LONG).show();
            return false;
        } else {
            //Toast.makeText(context,"Bluetooth Not Available!",Toast.LENGTH_LONG).show();
            return true;
        }
    }

    @Override
    public void turnBluetoothOn(String data) {
        heightData = data;

        if (!bluetoothAdapter.isEnabled()) {
            Intent btIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(btIntent, REQUEST_ENABLE_BT);
        } else {
            pairedDevices = bluetoothAdapter.getBondedDevices();

            String pairStatus = isMyDevicePaired(bluetoothServerMac);
            if (pairStatus.equals("NOT_FOUND")) {
                Log.d("device_name:", pairStatus + ",Starting Discovery");
                startDiscoverBluetooth();
            } else Log.d("device_name:", pairStatus);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            //Text(context, "Bluetooth Enabled!", Toast.LENGTH_LONG).show();
            if (resultCode == RESULT_OK) {
                pairedDevices = bluetoothAdapter.getBondedDevices();

                String pairStatus = isMyDevicePaired(bluetoothServerMac);
                if (pairStatus.equals("NOT_FOUND")) {
                    startDiscoverBluetooth();
                    Log.d("device_name_1:", pairStatus + ",Starting Discovery");
                } else {
                    Log.d("device_name_1:", pairStatus);
                }
            } else if (requestCode == RESULT_CANCELED) {
                //Toast.makeText(context,"Bluetooth Enable Failed!",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean startDiscoverBluetooth() {
        bluetoothAdapter.cancelDiscovery();
        bluetoothAdapter.startDiscovery();
        return false;
    }

    @Override
    public String isMyDevicePaired(String deviceMac) {
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceMacAddress = device.getAddress();
                //Log.d("device_mac",deviceMacAddress);
                if (deviceMac.equals(deviceMacAddress)) {

                    serverBluetoothDevice = device;
                    //AcceptThread acceptThread = new AcceptThread();
                    //acceptThread.start();
                    ConnectThread connectThread = new ConnectThread(device, MY_UUID, heightData);
                    connectThread.start();

                    return deviceName;
                }
            }
        }

        return "NOT_FOUND";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bluetoothAdapter.disable();

        unregisterReceiver(bluetoothReceiver);
    }

    private class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        //private static Handler mHandler;
        private String heightData;


        public ConnectThread(BluetoothDevice device, UUID MY_UUID, String data) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            //BluetoothSocket tmp = null;
            mmDevice = device;
            heightData = data;

            /*try
            {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID);
                Log.d("bluetooth_connect", "Socket Created");
            } catch (IOException e)
            {
                Log.e("bluetooth_connect", "Socket's create() method failed", e);
            }
            mmSocket = tmp;*/
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            BluetoothSocket tmp = null;

            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID);
                Log.d("bluetooth_connect", "Socket Created");
            } catch (IOException e) {
                Log.e("bluetooth_connect", "Socket's create() method failed", e);
            }
            mmSocket = tmp;

            bluetoothAdapter.cancelDiscovery();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket = (BluetoothSocket) mmDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(mmDevice, 1);

                mmSocket.connect();
                Message readMsg = mHandler.obtainMessage(-2, "Client Socket Connected");
                readMsg.sendToTarget();

                Log.e("bluetooth_connection", "Client Socket Connected");
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                Log.e("bluetooth_connection", "Client Socket NOT Connected" + connectException);

                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e("bluetooth_connection", "Could not close the client socket", closeException);
                }
                return;

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            manageConnections(mmSocket);

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            //String testData="Hello from Shalution.";

            //connectedThread.cancel();
        }

        private void manageConnections(BluetoothSocket socket) {
            // Log.e("Data_value:",heightData);
            String[] arrData = heightData.split(" ");

            ConnectedThread connectedThread = new ConnectedThread(socket);
            connectedThread.start();
            for (int i = 0; i < 30; i++) {
                Log.e("Data_value:", arrData[i]);

                byte[] data = arrData[i].getBytes();
                connectedThread.write(data);
            }
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e("bluetooth_connection", "Could not close the client socket", e);
            }
        }
    }


    private static class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private byte[] mmBuffer; // mmBuffer store for the stream

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams; using temp objects because
            // member streams are final.
            try {
                tmpIn = socket.getInputStream();
                Log.d("bluetooth_data_transfer", "Grabbed input stream.");
            } catch (IOException e) {
                Log.e("bluetooth_data_transfer", "Error occurred when creating input stream.", e);
            }
            try {
                tmpOut = socket.getOutputStream();
                Log.d("bluetooth_data_transfer", "Grabbed output stream.");

            } catch (IOException e) {
                Log.e("bluetooth_data_transfer", "Error occurred when creating output stream.", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            mmBuffer = new byte[1024];
            int numBytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                try {
                    // Read from the InputStream.
                    numBytes = mmInStream.read(mmBuffer);
                    // Send the obtained bytes to the UI activity.
                    // Message readMsg = mHandler.obtainMessage(-1, numBytes, -1, mmBuffer);
                    //readMsg.sendToTarget();
                } catch (IOException e) {
                    Log.d("bluetooth_data_transfer", "Input stream was disconnected", e);
                    break;
                }
            }
        }

        // Call this from the main activity to send data to the remote device.
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);

                // Share the sent message with the UI activity.
                //Message writtenMsg = mHandler.obtainMessage(1, -1, -1, mmBuffer);
                //writtenMsg.sendToTarget();
            } catch (IOException e) {
                Log.e("bluetooth_data_transfer", "Error occurred when sending data", e);

                // Send a failure message back to the activity.
                Message writeErrorMsg =
                        mHandler.obtainMessage(2);
                Bundle bundle = new Bundle();
                bundle.putString("toast", "Couldn't send data to the other device");
                writeErrorMsg.setData(bundle);
                mHandler.sendMessage(writeErrorMsg);
            }
        }

        // Call this method from the main activity to shut down the connection.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e("bluetooth_data_transfer", "Could not close the connect socket", e);
            }
        }
    }

    public class AcceptThread extends Thread {
        private final BluetoothServerSocket serverSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code
                tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord("NAME", MY_UUID);
                Log.d("bluetooth_connect_svr", "Socket Created");

            } catch (IOException e) {

            }
            serverSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned
            while (true) {
                try {
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    break;
                }

                // If a connection was accepted
                if (socket != null) {
                    // Do work to manage the connection (in a separate thread)
                    ConnectedThread connectedThread = new ConnectedThread(socket);
                    connectedThread.start();
                    connectedThread.write(heightData.getBytes());

                    mHandler.obtainMessage(100).sendToTarget();
                }
            }
        }
    }
}
