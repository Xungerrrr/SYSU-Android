package com.xungerrrr.storage2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private myDB db;
    private byte[] img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();

        RadioGroup options = findViewById(R.id.option);
        final ImageButton avatar = findViewById(R.id.avatar);
        final EditText username = findViewById(R.id.username);
        final EditText password = findViewById(R.id.password);
        final EditText confirm = findViewById(R.id.confirm_password);
        Button ok = findViewById(R.id.ok);
        Button clr = findViewById(R.id.clear);

        db = new myDB(getApplicationContext());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bmp = ((BitmapDrawable)getResources().getDrawable(R.mipmap.me)).getBitmap();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        img = baos.toByteArray();


        options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.login) {
                    avatar.setVisibility(View.GONE);
                    confirm.setVisibility(View.GONE);
                    password.setHint("Password");
                    password.setText("");
                    confirm.setText("");
                }
                else if (checkedId == R.id.register) {
                    avatar.setVisibility(View.VISIBLE);
                    confirm.setVisibility(View.VISIBLE);
                    password.setHint("New Password");
                    password.setText("");
                    confirm.setText("");
                }
            }
        });
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 0);
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(username.getText().toString())) {
                    // 用户名为空
                    Toast.makeText(getApplicationContext(), R.string.empty_name, Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(password.getText().toString())) {
                    // 密码为空
                    Toast.makeText(getApplicationContext(), R.string.empty_password, Toast.LENGTH_SHORT).show();
                }
                else if (!TextUtils.equals(password.getText().toString(), confirm.getText().toString()) &&
                        confirm.getVisibility() == View.VISIBLE) {
                    // 前后密码不匹配
                    Toast.makeText(getApplicationContext(), R.string.password_mismatch, Toast.LENGTH_SHORT).show();
                }
                else if (confirm.getVisibility() == View.GONE) {
                    // 若在登录界面
                    if (db.login(getApplicationContext(), username.getText().toString(), password.getText().toString())) {
                        // 登录成功，跳转到评论页面
                        Intent intent = new Intent(MainActivity.this, CommentActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("name", username.getText().toString());
                        intent.putExtras(bundle);
                        password.setText("");
                        startActivity(intent);
                    }
                }
                else {
                    // 注册用户
                    db.register(getApplicationContext(), username.getText().toString(), password.getText().toString(), img);
                }
            }
        });
        clr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setText("");
                password.setText("");
                confirm.setText("");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && requestCode == 0) {
            Uri uri = data.getData();
            ImageButton avatar = findViewById(R.id.avatar);
            avatar.setImageURI(uri);
            // 获取图像的bitmap
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Bitmap bmp = ((BitmapDrawable)avatar.getDrawable()).getBitmap();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
            img = baos.toByteArray();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void checkPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS}, 0);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {}
}
