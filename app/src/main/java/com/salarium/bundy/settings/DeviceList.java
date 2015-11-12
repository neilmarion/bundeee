package com.salarium.bundy.settings;

import com.salarium.bundy.persistence.SimplePersistence;
import com.salarium.bundy.R;
import com.salarium.bundy.settings.ClockingMethodSettings;
import com.salarium.bundy.values.*;
import com.salarium.bundy.view.ViewManager;

import android.widget.Button;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.Set;

public class DeviceList extends AlertDialog
    implements DialogInterface.OnClickListener {

    private Context context;
    private ClockingMethodSettings clockingMethodSettings;
    private String chosenDeviceMacAddress;
    private BluetoothAdapter bluetoothAdapter;
    private ArrayAdapter<String> pairedDevicesArrayAdapter;
    private final DeviceList dialog = this;
    private DeviceListOnSelectListener deviceListOnSelectListener;

    public DeviceList(
        Context context, DeviceListOnSelectListener deviceListOnSelectListener
    ) {
        super(context);
        this.context = context;
        this.clockingMethodSettings = clockingMethodSettings;
        setButton(AlertDialog.BUTTON_NEGATIVE,
            Values.Labels.CANCEL,
            this);
        this.deviceListOnSelectListener = deviceListOnSelectListener;
        setup(context);
    }

	private AdapterView.OnItemClickListener deviceClickListener =
        new AdapterView.OnItemClickListener() {
            public void onItemClick (
                AdapterView<?> av, View v, int arg2, long arg3
            ) {
                String info = ((TextView) v).getText().toString();
                String address = info.substring(info.length() - 17);
                deviceListOnSelectListener.onSelect(address);
                dialog.dismiss();
            }
	};

    private void setup(Context context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        final Context c = context;

        LayoutInflater inflater =
            (LayoutInflater) context.
                getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(
            R.layout.device_list, null);
        ListView pairedListView =
            (ListView) view.findViewById(R.id.paired_devices);
        pairedDevicesArrayAdapter = new ArrayAdapter<String>(context, R.layout.device_name);
        pairedListView.setAdapter(pairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(deviceClickListener);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

		if (pairedDevices.size() > 0) {
			view.findViewById(R.id.title_paired_devices).
                setVisibility(View.VISIBLE);
			for (BluetoothDevice device : pairedDevices) {
				pairedDevicesArrayAdapter.add(device.getName() + "\n"
						+ device.getAddress()
                );
			}
		} else {
			pairedDevicesArrayAdapter.add(
                Values.Messages.Neutral.NO_PAIRED_DEVICES
            );
		}
        setView(view);
    }

    public void showDialog() {
        show();
        styleButton();
    }

    private void styleButton() {
        Button negativeButton = getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(Color.parseColor("#ffffff"));
        negativeButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
        negativeButton.
            setBackgroundResource(R.drawable.negative_action_button);
        negativeButton.setPadding(20, 20, 20, 20);
    }

    public void onClick(DialogInterface dialogInterface, int which) {}
}
