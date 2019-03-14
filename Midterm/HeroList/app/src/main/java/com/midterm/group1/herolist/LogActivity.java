package com.midterm.group1.herolist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class LogActivity extends AppCompatActivity {
    private MyData myData = new MyData(this);
    private Boolean tags = true;
    private Bitmap bitmap;

    private AutoCompleteTextView mUserNameView;
    private TextInputLayout inputName, inputPassword, inputEnPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log);

        mUserNameView = findViewById(R.id.username);
        inputName = findViewById(R.id.input_user_name);
        inputPassword = findViewById(R.id.input_password);
        inputEnPassword = findViewById(R.id.input_enpassword);

        final ImageView imageView = (ImageView) findViewById(R.id.img);
        //final EditText username = (EditText) findViewById(R.id.username);
        final EditText password = (EditText) findViewById(R.id.password);
        final EditText enpassword = (EditText) findViewById(R.id.enpassword);
        final Button btn_ensure = (Button) findViewById(R.id.btn_ensure);
        final Button btn_clear = (Button) findViewById(R.id.btn_clear);
        final Button btn_register_or_login = (Button) findViewById(R.id.btn_register_or_login);
        //true -- login, false -- register

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.me);

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserNameView.setText("");
                password.setText("");
                inputName.setError(null);
                inputPassword.setError(null);

                if(!tags){
                    enpassword.setText("");
                    imageView.setImageResource(R.drawable.add);
                    inputEnPassword.setError(null);
                    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.me);
                }
            }
        });

        btn_ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputName.setError(null);
                inputPassword.setError(null);
                inputEnPassword.setError(null);

                if (tags){
                    if(TextUtils.isEmpty(mUserNameView.getText().toString())) {
                        inputName.setError("Input your username");
                    }
                    else if(TextUtils.isEmpty(password.getText().toString())) {
                        inputPassword.setError("Input your password");
                    }
                    else{
                        User user = myData.getByUsername(mUserNameView.getText().toString());
                        if (user == null) {
                            inputName.setError("Username not existed");
                        }
                        else if (!TextUtils.equals(password.getText().toString(), user.getPassword())){
                            inputPassword.setError("Invalid Password");
                        }
                        else {
                            Intent intent = new Intent();
                            intent.putExtra("name", user.getUsername());
                            setResult(1, intent);
                            finish();
                        }
                    }
                }
                else{
                    if(TextUtils.isEmpty(mUserNameView.getText().toString())) {
                        inputName.setError("Input your username");
                    }
                    else if(TextUtils.isEmpty(password.getText().toString())) {
                        inputPassword.setError("Input your password");
                    }
                    else if(!TextUtils.equals(password.getText().toString(), enpassword.getText().toString())){
                        inputEnPassword.setError("Password mismatch");
                    }
                    else{
                        User user = myData.getByUsername(mUserNameView.getText().toString());
                        if (user != null) {
                            inputName.setError("Username already exists");
                        }
                        else{
                            ByteArrayOutputStream os = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                            myData.insertUser(new User(mUserNameView.getText().toString(), password.getText().toString(), os.toByteArray()));
                            Snackbar.make(getCurrentFocus(), "Register successfully!", Snackbar.LENGTH_LONG).show();
                            enpassword.setText("");
                            password.setText("");
                        }
                    }
                }
            }
        });

        btn_register_or_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            inputName.setError(null);
            inputPassword.setError(null);
            if(tags){
                inputEnPassword.setError(null);
                password.setText("");
                imageView.setVisibility(View.VISIBLE);
                enpassword.setVisibility(View.VISIBLE);
                password.setText("");
                enpassword.setText("");
                btn_ensure.setText("REGISTER");
                btn_register_or_login.setText("LOGIN");
                tags = false;
            } else {
                imageView.setVisibility(View.GONE);
                enpassword.setVisibility(View.GONE);
                password.setText("");
                btn_ensure.setText("LOGIN");
                btn_register_or_login.setText("REGISTER");
                tags = true;
            }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null){
            Uri uri = data.getData();

            ImageView imageView = (ImageView) findViewById(R.id.img);
            imageView.setImageURI(uri);
            try {
                bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(uri));
            }
            catch (Exception ex){
                Toast.makeText(LogActivity.this,"fail",Toast.LENGTH_SHORT ).show();
            }
        }
    }
}
