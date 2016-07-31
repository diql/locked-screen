package org.diql.lockedscreen;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE = 1;
    private DevicePolicyManager mDevicePolicyManager;
    private ComponentName mComponentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDevicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        mComponentName = new ComponentName(this, AdminDevReceiver.class);
        lockDevice();
        setContentView(R.layout.activity_main);
    }

    private void lockDevice(){
        Log.i(TAG, "lockDevice().");
        boolean active = mDevicePolicyManager.isAdminActive(mComponentName);
        if(active){
            mDevicePolicyManager.lockNow();//直接锁屏
            finish();
        } else {
            addDeviceAdmin();//去获得权限
        }
    }

    private void addDeviceAdmin() {
        Log.i(TAG, "addDeviceAdmin().");
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                getString(R.string.add_device_admin_explanation));
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult().requestCode:" + requestCode
                + "resultCode:" + resultCode + ",data:" + data);
        if (RESULT_OK == resultCode) {
            lockDevice();
        } else {
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}
