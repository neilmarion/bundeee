package com.salarium.bundy.clocking.method.device;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import com.fgtit.reader.BluetoothReaderService;
import com.salarium.bundy.persistence.EmployeeDatabasePersistence;
import com.salarium.bundy.persistence.SimplePersistence;
import com.salarium.bundy.settings.ClockingMethodSettings;
import java.io.IOException;
import java.lang.Thread;
import java.lang.InterruptedException;
import com.salarium.bundy.view.BundyAlertDialog;
import com.salarium.bundy.view.NegativeNonExpiringDismissDialog;
import android.content.DialogInterface;
import com.salarium.bundy.values.*;
import android.content.Intent;
import android.content.ComponentName;
import java.lang.NullPointerException;
import com.salarium.bundy.settings.SettingsActivity;
import java.lang.IllegalArgumentException;

/** Fingerprint method class that uses the HF7000 fingerprint scanner
 *
 * @author Neil Marion dela Cruz
 */
public class FingerprintDeviceHF7000 implements FingerprintDeviceImplementation, DialogInterface.OnClickListener {
    private Activity activity;
    private BluetoothReaderService mChatService = null;
    private byte[] referenceData;
    private EmployeeDatabasePersistence employeeDatabasePersistence;
    private final static byte CMD_CAPTUREHOST=0x08; //Caputre to Host
    private final static byte CMD_ENROLHOST=0x07;
    private final static byte CMD_MATCH=0x09;
    private final static byte CMD_CLEARID=0x06;
    private FingerprintResultHandler fingerprintResultHandler;
    private Handler fingerprintAPIHandler;
    private Handler fingerprintProcessHandler;
    private String currentEmployeeCompanyId;
    private String currentEmployeeFullName;
    private String currentEmployeeId;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    private BluetoothDevice device;
    private boolean terminate = false;
    private ClockingMethodSettings clockingMethodSettings;
    private NegativeNonExpiringDismissDialog noDeviceDialog = null;

    // The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_WRITE:
                byte[] writeBuf = (byte[]) msg.obj;
                break;
            case MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
                System.out.println("receiveMessage");
                receiveMessageFromDevice(readBuf,msg.arg1);
                break;
            }
        }
    };

    public FingerprintDeviceHF7000(
        Looper looper,
        Handler fingerprintProcessHandler,
        Activity activity,
        EmployeeDatabasePersistence employeeDatabasePersistence
    ) {
        setFingerprintProcessHandler(fingerprintProcessHandler);
        createFingerprintAPIHandler(looper);
        createFingerprintResultHandler(looper);
        this.activity = activity;
        this.employeeDatabasePersistence = employeeDatabasePersistence;
        clockingMethodSettings =
            new ClockingMethodSettings(
                new SimplePersistence(activity), activity
            );
        mChatService = new BluetoothReaderService(activity, mHandler);
        System.out.println("mac address: " + clockingMethodSettings.getFingerprintDeviceMacAddress());
        connectToDevice();
    }

    private void createFingerprintAPIHandler(Looper looper) {
        fingerprintAPIHandler = new FingerprintAPIHandler(looper);
    }

    private void createFingerprintResultHandler(Looper looper) {
        fingerprintResultHandler = new FingerprintResultHandler(looper);
    }

    /** Registering an employee fingerprint
     *
     * @param currentEmployeeId             employee id
     * @param currentEmployeeCompanyId      employee company id
     * @param currentEmployeeFullName       employee company id
     */
    public void register(
        String currentEmployeeId,
        String currentEmployeeCompanyId,
        String currentEmployeeFullName
    ) {
        this.currentEmployeeId = currentEmployeeId;
        this.currentEmployeeCompanyId = currentEmployeeCompanyId;
        this.currentEmployeeFullName = currentEmployeeFullName;
        fingerprintAPIHandler.sendEmptyMessage(REGISTER);
    }

    /** Verifying employee fingerprint
     *
     * @param employeeId             employee id
     */
    public void verify(String currentEmployeeId) {
        terminate = false;
        byte[] model = employeeDatabasePersistence.
            getEmployeeFingerprintByEmployeeId(currentEmployeeId);
        if (model != null) {
            fingerprintAPIHandler.obtainMessage(VERIFY, model).sendToTarget();
        } else {
            fingerprintProcessHandler.sendEmptyMessage(
                NO_FINGERPRINT_REGISTERED
            );
        }
    }

    public void setFingerprintProcessHandler (
        Handler fingerprintProcessHandler
    ) {
        this.fingerprintProcessHandler = fingerprintProcessHandler;
    }

    protected class FingerprintResultHandler extends Handler {
        public FingerprintResultHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REGISTER_SUCCESS:
                    fingerprintProcessHandler.
                        sendEmptyMessage(REGISTER_SUCCESS);
                    break;
                case REGISTER_FAIL:
                    fingerprintProcessHandler.
                        sendEmptyMessage(REGISTER_FAIL);
                    break;
                case VERIFY_RESULT:
                    fingerprintProcessHandler.
                        obtainMessage(SHOW_RESULT, (Boolean) msg.obj).
                            sendToTarget();
            }
        }
    }

    protected class FingerprintAPIHandler extends Handler {
        public FingerprintAPIHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REGISTER:
                    try {
                        registerFingerprint();
                    } catch (IOException e) {
                        e.printStackTrace(System.out);
                        fingerprintProcessHandler.
                            sendEmptyMessage(REGISTER_FAIL);
                    }
                    break;
                case VERIFY:
                    try {
                        byte[] data = (byte[]) msg.obj;
                        verifyFingerprint(data);
                    } catch (IOException e) {
                        e.printStackTrace(System.out);
                        fingerprintProcessHandler.
                            sendEmptyMessage(REGISTER_FAIL);
                    }
                    break;
            }
        }
    }

    private void clear() throws IOException {
        sendCommandToDevice(CMD_CLEARID,null,0);
    }

    private void registerFingerprint() throws IOException {
        fingerprintProcessHandler.
            obtainMessage(SHOW_EXPECTING_INPUT).sendToTarget();
        sendCommandToDevice(CMD_ENROLHOST,null,0);
    }

	private void verifyFingerprint(byte[] model) throws IOException {
        referenceData = model;
        fingerprintProcessHandler.
            obtainMessage(SHOW_EXPECTING_INPUT).sendToTarget();
            sendCommandToDevice(CMD_CAPTUREHOST,null,0);
	}

    private void sendCommandToDevice(byte cmdid,byte[] data,int size) {
    	int sendsize=9+size;
    	byte[] sendbuf = new byte[sendsize];
    	sendbuf[0]='F';
    	sendbuf[1]='T';
    	sendbuf[2]=0;
    	sendbuf[3]=0;
    	sendbuf[4]=cmdid;
    	sendbuf[5]=(byte)(size);
    	sendbuf[6]=(byte)(size>>8);
    	if(size>0) {
    		for(int i=0;i<size;i++) {
    			sendbuf[7+i]=data[i];
    		}
    	}
    	int sum=calcCheckSum(sendbuf,(7+size));
    	sendbuf[7+size]=(byte)(sum);
    	sendbuf[8+size]=(byte)(sum>>8);
    	
    	mChatService.write(sendbuf);
    }

    private void receiveMessageFromDevice(byte[] databuf,int datasize) {
        byte mCmdData[] = new byte[10240];
        int mCmdSize = 0;
        byte mRefData[] = new byte[512];
        byte mMatData[] = new byte[512];

        memcpy(mCmdData,mCmdSize,databuf,0,datasize);
        mCmdSize=mCmdSize+datasize;   			
        int totalsize=(byte)(mCmdData[5])+((mCmdData[6]<<8)&0xFF00)+9;

        System.out.println("totalSize: " + Boolean.toString(mCmdSize>=totalsize));
        System.out.println("data: " + mCmdData[4]);
        if(mCmdSize>=totalsize){
            mCmdSize=0;
            System.out.println("hello");
            if((mCmdData[0]=='F')&&(mCmdData[1]=='T'))	{
                System.out.println("hi");
                switch(mCmdData[4]) {
                case CMD_ENROLHOST: {
                        fingerprintProcessHandler.
                            obtainMessage(SHOW_CURRENTLY_VALIDATING, null).
                                sendToTarget();
                        int size=(byte)(mCmdData[5])+((mCmdData[6]<<8)&0xFF00)-1;
                        if (mCmdData[7]==1) {
                            memcpy(mRefData,0,mCmdData,8,size);
                            employeeDatabasePersistence.
                                addOrUpdateEmployee(
                                    currentEmployeeId,
                                    currentEmployeeCompanyId,
                                    currentEmployeeFullName,
                                    mRefData
                                );
                            fingerprintResultHandler.
                                sendEmptyMessage(REGISTER_SUCCESS);
                        } else {
                            fingerprintResultHandler.
                                sendEmptyMessage(REGISTER_FAIL);
                        }
                    }

                    break;
                case CMD_MATCH:
                    fingerprintProcessHandler.
                        obtainMessage(SHOW_CURRENTLY_VALIDATING, null).
                            sendToTarget();
                    int score=(byte)(mCmdData[8])+((mCmdData[9]<<8)&0xF0);
                    System.out.println("HEHEHEHEHEHEHE");
                    if (mCmdData[7]==1) {
                        fingerprintResultHandler.
                            obtainMessage(VERIFY_RESULT, true).sendToTarget();
                    } else {
                        System.out.println("cancelll");
                        fingerprintResultHandler.
                            obtainMessage(VERIFY_RESULT, false).sendToTarget();
                    }

                    break;
                case CMD_CAPTUREHOST:
                    int size=(byte)(mCmdData[5])+((mCmdData[6]<<8)&0xFF00)-1;
                    if(mCmdData[7]==1) {
                        memcpy(mMatData,0,mCmdData,8,size);

                        byte buf[]=new byte[1024];
                        memcpy(buf,0,referenceData,0,512);
                        memcpy(buf,512,mMatData,0,512);
                        sendCommandToDevice(CMD_MATCH,buf,1024);
                    } else {
                        fingerprintResultHandler.
                            obtainMessage(VERIFY_RESULT, false).sendToTarget();
                    }
                    break;
                case CMD_CLEARID:
                    break;
                case 0:
                    try {
                        System.out.println("clearing");
                        clear();
                    } catch (Exception e) {
                        e.printStackTrace(System.out);
                    }
                    break;
                default:
                    if (terminate == false) {
                        System.out.println("TERMINATE 1");
                        fingerprintProcessHandler.sendEmptyMessage(TERMINATE);
                        terminate = true;
                    }
                    break;
                }
            } else {
                if (terminate == false) {
                    System.out.println("TERMINATE 2");
                    fingerprintProcessHandler.sendEmptyMessage(TERMINATE);
                    terminate = true;
                }
            }
        } else {
            if (terminate == false) {
                System.out.println("TERMINATE 3");
                fingerprintProcessHandler.sendEmptyMessage(TERMINATE);
                terminate = true;
            }
        }
    }

	private void memcpy(byte[] dstbuf,int dstoffset,byte[] srcbuf,int srcoffset,int size) {
        System.out.println("dstbuf: " + Boolean.toString(dstbuf == null));
        System.out.println("srcbuf: " + Boolean.toString(srcbuf == null));
		for(int i=0;i<size;i++) {
            try {
			    dstbuf[dstoffset+i]=srcbuf[srcoffset+i];
            } catch (NullPointerException e) {
                //e.printStackTrace(System.out);
                //fingerprintProcessHandler.sendEmptyMessage(
                //    TERMINATE
                //);
            }
		}
        return;
	}

	private int calcCheckSum(byte[] buffer,int size) {
		int sum=0;
		for(int i=0;i<size;i++) {
			sum=sum+buffer[i];
		}
		return (sum & 0x00ff);
	}

    public void close() {
        mChatService.stop();
    }

    public void reconnect() {
        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            NegativeNonExpiringDismissDialog bluetoothOffDialog =
                new NegativeNonExpiringDismissDialog(
                    activity,
                    Values.Labels.OK,
                    Values.Messages.Error.BLUETOOTH_OFF,
                    Values.Icons.EXCLAMATION,
                    States.BLUETOOTH_OFF,
                    this);
            bluetoothOffDialog.showDialog();
        } else {
            connectToDevice();
        }
    }

    private void connectToDevice() {
        String macAddress =
            clockingMethodSettings.getFingerprintDeviceMacAddress();
        System.out.println("noDeviceDialog: " + noDeviceDialog);

        if (macAddress.isEmpty()) {
            if (noDeviceDialog == null) {
                noDeviceDialog =
                    new NegativeNonExpiringDismissDialog(
                        activity,
                        Values.Labels.OK,
                        Values.Messages.Error.BLUETOOTH_DEVICE_NOT_SET_ERROR,
                        Values.Icons.EXCLAMATION,
                        States.BLUETOOTH_DEVICE_NOT_SET,
                        this);
                noDeviceDialog.showDialog();
            }
        } else {
            try {
                device =
                    BluetoothAdapter.getDefaultAdapter().getRemoteDevice(
                        macAddress
                    );
                System.out.println("connecting");
                mChatService.connect(device);
            } catch (IllegalArgumentException e) {
                if (noDeviceDialog == null) {
                    noDeviceDialog =
                        new NegativeNonExpiringDismissDialog(
                            activity,
                            Values.Labels.OK,
                            Values.Messages.Error.BLUETOOTH_DEVICE_NOT_SET_ERROR,
                            Values.Icons.EXCLAMATION,
                            States.BLUETOOTH_DEVICE_NOT_SET,
                            this);
                    noDeviceDialog.showDialog();
                }
            }
        }
    }

    public void onClick(DialogInterface dialog, int which) {
        switch(((BundyAlertDialog) dialog).getState()) {
            case States.BLUETOOTH_OFF:
                final Intent intent = new Intent(Intent.ACTION_MAIN, null);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.bluetooth.BluetoothSettings");
                intent.setComponent(cn);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
                break;
            case States.BLUETOOTH_DEVICE_NOT_SET:
                activity.startActivityForResult(new Intent(activity, SettingsActivity.class), 0);
                break;
        }
        noDeviceDialog = null;
    }
}
