package richard.ztesoft.com.sdk23permissiontest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static String [] array = {"申请读取短信权限","申请读取通话记录权限"};

    private static int READ_SMS_REQUEST_CODE = 0;

    ListView listView;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         ButterKnife.bind(this);

         listView = (ListView)this.findViewById(R.id.listview);

         ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,array);

         listView.setAdapter(adapter);
         listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 Log.i("xsailor","item = "+i);
                 switch (i){
                     case 0:

                         Intent intent = new Intent(MainActivity.this,SMSReadActivity.class);
                         MainActivity.this.startActivity(intent);

                         break;
                     case 1:

                         break;
                     default:
                         break;
                 }
             }
         });

         /**
          * 存储空间读写权限似乎和短信权限不一样，Manifest.permission.WRITE_EXTERNAL_STORAGE并不会弹框让用户选择
          * 而是直接动态获取权限，对于新安装的应用，只需要申请一次，如果卸载重装，则又会执行申请代码
          */
         //sdk 23（android 6.0）以后需要动态检查是否拥有权限
         if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                 != PackageManager.PERMISSION_GRANTED) {
             if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                 Log.i("xsailor","shouldShowRequestPermissionRationale");
             }else{
                 ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                         READ_SMS_REQUEST_CODE);
             }
             //申请WRITE_EXTERNAL_STORAGE权限

         }

         File sd = Environment.getExternalStorageDirectory();
         boolean can_write = sd.canWrite();
         boolean can_read = sd.canRead();
         android.util.Log.i("xsailor","can_write==============================="+can_write);
         android.util.Log.i("xsailor","can_read==============================="+can_read);


         Log.i("xsailor","ppp INTERNET= "+checkPermission(Manifest.permission.INTERNET));
         Log.i("xsailor","ppp RECEIVE_BOOT_COMPLETED= "+checkPermission(Manifest.permission.RECEIVE_BOOT_COMPLETED));
         Log.i("xsailor","ppp CALL_PHONE= "+checkPermission( Manifest.permission.CALL_PHONE));
         Log.i("xsailor","ppp READ_CONTACTS= "+checkPermission(Manifest.permission.READ_CONTACTS));
         Log.i("xsailor","ppp WRITE_CONTACTS= "+checkPermission( Manifest.permission.WRITE_CONTACTS));
         Log.i("xsailor","ppp RECEIVE_SMS= "+checkPermission(Manifest.permission.RECEIVE_SMS));
         Log.i("xsailor","ppp RECEIVE_MMS= "+checkPermission( Manifest.permission.RECEIVE_MMS));
         Log.i("xsailor","ppp SEND_SMS= "+checkPermission( Manifest.permission.SEND_SMS));
         Log.i("xsailor","ppp VIBRATE= "+checkPermission(Manifest.permission.VIBRATE));
         Log.i("xsailor","ppp READ_SMS= "+checkPermission( Manifest.permission.READ_SMS));
         Log.i("xsailor","ppp ACCESS_NETWORK_STATE= "+checkPermission(Manifest.permission.ACCESS_NETWORK_STATE));
         Log.i("xsailor","ppp CHANGE_NETWORK_STATE= "+checkPermission(Manifest.permission.CHANGE_NETWORK_STATE));
         Log.i("xsailor","ppp READ_PHONE_STATE= "+checkPermission( Manifest.permission.READ_PHONE_STATE));
         Log.i("xsailor","ppp WAKE_LOCK= "+checkPermission( Manifest.permission.WAKE_LOCK));
         Log.i("xsailor","ppp WRITE_EXTERNAL_STORAGE= "+checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE));


         Log.i("xsailor","ppp READ_EXTERNAL_STORAGE= "+checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE));
         Log.i("xsailor","ppp ACCESS_FINE_LOCATION= "+checkPermission(Manifest.permission.ACCESS_FINE_LOCATION));
         Log.i("xsailor","ppp CAMERA= "+checkPermission(Manifest.permission.CAMERA));
         Log.i("xsailor","ppp FLASHLIGHT= "+checkPermission( Manifest.permission.FLASHLIGHT));
         Log.i("xsailor","ppp ACCESS_WIFI_STATE= "+checkPermission( Manifest.permission.ACCESS_WIFI_STATE));
         Log.i("xsailor","ppp CHANGE_WIFI_STATE= "+checkPermission(Manifest.permission.CHANGE_WIFI_STATE));
         Log.i("xsailor","ppp BLUETOOTH= "+checkPermission(Manifest.permission.BLUETOOTH));

         Log.i("xsailor","ppp BLUETOOTH_ADMIN= "+checkPermission( Manifest.permission.BLUETOOTH_ADMIN));
         Log.i("xsailor","ppp MOUNT_UNMOUNT_FILESYSTEMS= "+checkPermission( Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS));
         Log.i("xsailor","ppp READ_LOGS= "+checkPermission(Manifest.permission.READ_LOGS));
         Log.i("xsailor","ppp WRITE_SETTINGS= "+checkPermission( Manifest.permission.WRITE_SETTINGS));
    }

    private int checkPermission(String permission){
        return ContextCompat.checkSelfPermission(MainActivity.this, permission);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //doNext(requestCode,grantResults);
        for (int i=0;i<permissions.length;i++){
            Log.i("xsailor","permission "+i + " = "+permissions[i]);
        }

        for (int i=0;i<grantResults.length;i++){
            Log.i("xsailor","grantResults "+i + " = "+grantResults[i]);
        }


        File sd = Environment.getExternalStorageDirectory();
        boolean can_write = sd.canWrite();
        boolean can_read = sd.canRead();
        android.util.Log.i("xsailor","onRequestPermissionsResult can_write==============================="+can_write);
        android.util.Log.i("xsailor","onRequestPermissionsResult can_read==============================="+can_read);
    }
}
