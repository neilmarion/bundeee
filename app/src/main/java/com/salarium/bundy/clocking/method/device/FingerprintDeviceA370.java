package com.salarium.bundy.clocking.method.device;

import android_serialport_api.FingerprintAPI;
import android_serialport_api.SerialPortManager;
import android.app.Activity;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import com.salarium.bundy.persistence.EmployeeDatabasePersistence;
import java.io.IOException;

/** Fingerprint method class that uses the a370 fingerprint scanner
 *
 * @author Neil Marion dela Cruz
 */
public class FingerprintDeviceA370 implements FingerprintDeviceImplementation {
    private Activity activity;
    private EmployeeDatabasePersistence employeeDatabasePersistence;
    private FingerprintAPI fingerprintAPI;
    private Handler fingerprintAPIHandler;
    private Handler fingerprintProcessHandler;
    private String currentEmployeeId;
    private String currentEmployeeCompanyId;
    private String currentEmployeeFullName;

    public FingerprintDeviceA370(
        Looper looper,
        Handler fingerprintProcessHandler,
        Activity activity,
        EmployeeDatabasePersistence employeeDatabasePersistence
    ) {
        setFingerprintProcessHandler(fingerprintProcessHandler);
        createFingerprintAPIHandler(looper);
        fingerprintAPI = new FingerprintAPI();
        fingerprintAPI.setFingerprintType(FingerprintAPI.SMALL_FINGERPRINT_SIZE);
        this.activity = activity;
        this.employeeDatabasePersistence = employeeDatabasePersistence;
    }

    private Handler createFingerprintAPIHandler(Looper looper) {
        return fingerprintAPIHandler = new FingerprintAPIHandler(looper);
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
        byte[] model = employeeDatabasePersistence.
            getEmployeeFingerprintByEmployeeId(currentEmployeeId);
        if (model != null) {
            fingerprintAPIHandler.obtainMessage(VERIFY, model).sendToTarget();
        } else {
            fingerprintProcessHandler.sendEmptyMessage(NO_FINGERPRINT_REGISTERED);
        }
    }

    public void setFingerprintProcessHandler (
        Handler fingerprintProcessHandler) {
        this.fingerprintProcessHandler = fingerprintProcessHandler;
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
                        boolean isSuccess = registerFingerprint();
                        if (isSuccess) {
                            fingerprintProcessHandler.
                                sendEmptyMessage(REGISTER_SUCCESS);
                        } else {
                            fingerprintProcessHandler.
                                sendEmptyMessage(REGISTER_FAIL);
                        }
                    } catch (IOException e) {
                        e.printStackTrace(System.out);
                        fingerprintProcessHandler.
                            sendEmptyMessage(REGISTER_FAIL);
                    }
                    break;
                case VERIFY:
                    try {
                        byte[] data = (byte[]) msg.obj;
                        boolean result = verifyFingerprint(data);
                        fingerprintProcessHandler.
                            obtainMessage(SHOW_RESULT, result).sendToTarget();
                    } catch (IOException e) {
                        e.printStackTrace(System.out);
                        fingerprintProcessHandler.
                            sendEmptyMessage(REGISTER_FAIL);
                    }
                    break;
            }
        }
    }

    private boolean registerFingerprint() throws IOException {
        fingerprintProcessHandler.
            obtainMessage(SHOW_EXPECTING_INPUT).sendToTarget();
        SerialPortManager.getInstance().openSerialPort();

        for (int i = 1; i < 3; i++) {
            System.out.println("step: " + i);
            int getImageResult = -1;
            do {
                getImageResult = fingerprintAPI.PSGetImage();
                if (!SerialPortManager.getInstance().isOpen()) {
                    closeSerialPort();
                    System.out.println("closed");
                    return false;
                }
            } while (getImageResult != 0x00);

            byte[] image = fingerprintAPI.PSUpImage();
            if (image == null) {
                return false;
            }

            if (i == 1) {
                fingerprintProcessHandler.
                    obtainMessage(SHOW_CURRENTLY_VALIDATING, image).
                        sendToTarget();
            }

            int genChar = fingerprintAPI.PSGenChar(i);
            if (genChar != 0x00) {
                return false;
             }
        }

		int regModel = fingerprintAPI.PSRegModel();
		if (regModel != 0x00) {
			return false;
		}

        byte[] imageData = fingerprintAPI.PSUpChar(2);
        if (imageData == null) {
            return false;
        }

        employeeDatabasePersistence.addOrUpdateEmployee(
            currentEmployeeId,
            currentEmployeeCompanyId,
            currentEmployeeFullName,
            imageData);
        return true;
    }

	private boolean verifyFingerprint(byte[] model) throws IOException {
        fingerprintProcessHandler.
            obtainMessage(SHOW_EXPECTING_INPUT).sendToTarget();
        SerialPortManager.getInstance().openSerialPort();
		int getImageResult = -1;

		do {
			getImageResult = fingerprintAPI.PSGetImage();
            System.out.println("waiting for finter");
            if (!SerialPortManager.getInstance().isOpen()) {
                closeSerialPort();
                return false;
            }
		} while (getImageResult != 0x00);

		byte[] image = fingerprintAPI.PSUpImage();
		fingerprintProcessHandler.obtainMessage(
            SHOW_CURRENTLY_VALIDATING, image
        ).sendToTarget();

		if (image == null) {
            System.out.println("Debug 1");
			return false;
		}

		int genChar = fingerprintAPI.PSGenChar(1);
		if (genChar != 0x00) {
            System.out.println("Debug 2");
			return false;
		}

		int downChar = fingerprintAPI.PSDownChar(2, model);
		if (downChar != 0x00) {
            System.out.println("Debug 3");
			return false;
		}

		return fingerprintAPI.PSMatch(); // the fingerprint matching happens here
	}

    public void close() {
        closeSerialPort();
    }

    public void closeSerialPort() {
        SerialPortManager.getInstance().closeSerialPort();
    }

    public void reconnect() {
        close();
    }
}
