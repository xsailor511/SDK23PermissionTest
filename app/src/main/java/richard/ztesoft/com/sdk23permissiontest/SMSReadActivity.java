package richard.ztesoft.com.sdk23permissiontest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

import java.sql.Date;
import java.text.SimpleDateFormat;


import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TableLayout;


public class SMSReadActivity extends AppCompatActivity {

    private static int READ_SMS_REQUEST_CODE = 0;

    @Bind(R.id.textview)
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsread);
        ButterKnife.bind(this);

        String permission = "android.permission.READ_SMS"; //你要判断的权限名字
        /**
         * 状态1：在AndroidManifest.xml文件里定义<uses-permission android:name="android.permission.READ_SMS" />
         * 那么不管用户是否手动禁止了读取短信的权限，res=true，ppp=0
         * 对于用户手动管理的三个权限状态：禁止，询问，允许
         * 如果用户选择禁止，那么res=true，ppp=0, ContentResolver会识别出权限异常，抛出空指针异常，app闪退
         * 如果用户选择询问，那么第一次res=false，ppp=-1 ，会申请权限，之后不会再次申请，就会弹框询问，
         * 询问用户，如果用户拒绝，程序闪退，如果接受，则正常显示
         * 如果用户选择允许，那么res=true，ppp=0 不会有询问，正常显示
         *
         * 状态2：没有在AndroidManifest.xml定义<uses-permission android:name="android.permission.READ_SMS" />
         *  res= false;
         *  ppp = -1
         *  @see line 69
         *
         */
        int res = getApplicationContext().checkCallingOrSelfPermission(permission);
        Log.i("xsailor","res = "+(res == PackageManager.PERMISSION_GRANTED));

        Log.i("xsailor","ppp = "+ContextCompat.checkSelfPermission(SMSReadActivity.this, Manifest.permission.READ_SMS));
        if (ContextCompat.checkSelfPermission(SMSReadActivity.this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SMSReadActivity.this,Manifest.permission.READ_SMS)){
                Log.i("xsailor","shouldShowRequestPermissionRationale");
            }else{
                Log.i("xsailor","requestPermissions");
                /**
                 * ！！！！！！！！！！！！对于安装后的第一次运行，必须要动态申请权限之后才可以读取短信，之后就不需要再次动态申请了，只需要动态申请一次
                 * 对于短信权限，系统会在读取时再次弹框！！！！！！！！！！！！！！！！
                 * 状态2：会执行到本行，然后app闪退，但是没有看到输出异常
                 */
                ActivityCompat.requestPermissions(SMSReadActivity.this, new String[]{Manifest.permission.READ_SMS},
                        READ_SMS_REQUEST_CODE);
            }
            //申请WRITE_EXTERNAL_STORAGE权限

        }else{
            textView.setText(getSmsInPhone());
        }

    }

    public String getSmsInPhone() {
        final String SMS_URI_ALL = "content://sms/";
        final String SMS_URI_INBOX = "content://sms/inbox";
        final String SMS_URI_SEND = "content://sms/sent";
        final String SMS_URI_DRAFT = "content://sms/draft";
        final String SMS_URI_OUTBOX = "content://sms/outbox";
        final String SMS_URI_FAILED = "content://sms/failed";
        final String SMS_URI_QUEUED = "content://sms/queued";

        StringBuilder smsBuilder = new StringBuilder();

        try {
            Uri uri = Uri.parse(SMS_URI_ALL);
            String[] projection = new String[] { "_id", "address", "person", "body", "date", "type" };
            Cursor cur = getContentResolver().query(uri, projection, null, null, "date desc");		// 获取手机内部短信

            if (cur.moveToFirst()) {
                int index_Address = cur.getColumnIndex("address");
                int index_Person = cur.getColumnIndex("person");
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                int index_Type = cur.getColumnIndex("type");

                do {
                    String strAddress = cur.getString(index_Address);
                    int intPerson = cur.getInt(index_Person);
                    String strbody = cur.getString(index_Body);
                    long longDate = cur.getLong(index_Date);
                    int intType = cur.getInt(index_Type);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date d = new Date(longDate);
                    String strDate = dateFormat.format(d);

                    String strType = "";
                    if (intType == 1) {
                        strType = "接收";
                    } else if (intType == 2) {
                        strType = "发送";
                    } else {
                        strType = "null";
                    }

                    smsBuilder.append("[ ");
                    smsBuilder.append(strAddress + ", ");
                    smsBuilder.append(intPerson + ", ");
                    smsBuilder.append(strbody + ", ");
                    smsBuilder.append(strDate + ", ");
                    smsBuilder.append(strType);
                    smsBuilder.append(" ]\n\n");
                } while (cur.moveToNext());

                if (!cur.isClosed()) {
                    cur.close();
                    cur = null;
                }
            } else {
                smsBuilder.append("no result!");
            } // end if

            smsBuilder.append("getSmsInPhone has executed!");

        } catch (SQLiteException ex) {
            Log.d("SQLiteException ", ex.getMessage());
        }

        return smsBuilder.toString();
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
    }
}
