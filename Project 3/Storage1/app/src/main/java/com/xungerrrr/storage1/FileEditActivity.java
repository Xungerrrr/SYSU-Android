package com.xungerrrr.storage1;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_edit);

        final EditText content = findViewById(R.id.file_content);
        Button save = findViewById(R.id.save_btn);
        Button load = findViewById(R.id.load_btn);
        Button clr = findViewById(R.id.clear_btn);
        final String FILE_NAME = "Storage1.txt";

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try (FileOutputStream fileOutputStream = openFileOutput(FILE_NAME, MODE_PRIVATE)) {
                    fileOutputStream.write(content.getText().toString().getBytes());
                    Log.i("TAG", "Successfully saved file.");
                    Toast.makeText(getApplicationContext(), R.string.save_success, Toast.LENGTH_SHORT).show();
                } catch (IOException ex) {
                    Log.e("TAG", "Fail to save file.");
                }
            }
        });
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try (FileInputStream fileInputStream = openFileInput(FILE_NAME)) {
                    byte[] contents = new byte[fileInputStream.available()];
                    fileInputStream.read(contents);
                    content.setText(new String(contents));
                    Toast.makeText(getApplicationContext(), R.string.load_success, Toast.LENGTH_SHORT).show();
                } catch (IOException ex) {
                    Log.e("TAG", "Fail to read file.");
                    Toast.makeText(getApplicationContext(), R.string.load_fail, Toast.LENGTH_SHORT).show();
                }
            }
        });
        clr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.setText("");
            }
        });
    }

}
