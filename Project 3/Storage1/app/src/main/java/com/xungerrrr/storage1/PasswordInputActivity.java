package com.xungerrrr.storage1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Text;

public class PasswordInputActivity extends AppCompatActivity {
    public static int MODE = MODE_PRIVATE;
    public static final String PREFERENCE_NAME = "SavedPassword";
    public static boolean isSaved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_input);

        final EditText newPass = findViewById(R.id.new_password);
        final EditText confirmPass = findViewById(R.id.confirm_password);
        Button ok = findViewById(R.id.ok_btn);
        Button clr = findViewById(R.id.clr_btn);
        final SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_NAME, MODE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        if (!TextUtils.isEmpty(sharedPreferences.getString("Password", ""))) {
            confirmPass.setVisibility(View.GONE);
            newPass.setHint("Password");
            // 标记已存储
            isSaved = true;
        }
        else {
            // 标记未存储
            isSaved = false;
        }


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(newPass.getText().toString())) {
                    if (newPass.getText().toString() == "")
                    Toast.makeText(getApplicationContext(), R.string.empty_pass, Toast.LENGTH_SHORT).show();
                }
                else if (isSaved) {
                    if (!TextUtils.equals(newPass.getText().toString(), sharedPreferences.getString("Password", ""))) {
                        Toast.makeText(getApplicationContext(), R.string.wrong_pass, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // 进入文件编辑Activity
                        Intent intent = new Intent(PasswordInputActivity.this, FileEditActivity.class);
                        startActivity(intent);
                    }
                }
                else {
                    if (!TextUtils.equals(newPass.getText().toString(), confirmPass.getText().toString())) {
                        Toast.makeText(getApplicationContext(), R.string.mismatch_pass, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        editor.putString("Password", newPass.getText().toString());
                        editor.commit();
                        // 进入文件编辑Activity
                        Intent intent = new Intent(PasswordInputActivity.this, FileEditActivity.class);
                        startActivity(intent);
                    }
                }

            }
        });
        clr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPass.setText("");
                if (!isSaved)
                    confirmPass.setText("");
            }
        });
    }
}
