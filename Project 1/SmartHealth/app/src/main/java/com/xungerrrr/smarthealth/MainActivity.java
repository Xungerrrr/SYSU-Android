package com.xungerrrr.smarthealth;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setButtonOnClickListener();
        setRadioGroupOnCheckedChangeListener();
    }

    public void setButtonOnClickListener() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.dialog_title).setPositiveButton(R.string.positive_btn_text,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),
                                R.string.confirm_msg, Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton(R.string.negative_btn_text,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),
                                R.string.cancel_msg, Toast.LENGTH_SHORT).show();
                    }
                }).create();

        Button btn = findViewById(R.id.search_button);
        if (btn != null) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText search_content = findViewById(R.id.search_src_text);
                    RadioGroup category = findViewById(R.id.category);
                    RadioButton checked_btn = findViewById(category.getCheckedRadioButtonId());
                    if (TextUtils.isEmpty(search_content.getText().toString())) {
                        Toast.makeText(getApplicationContext(),
                                R.string.empty_msg, Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.equals(search_content.getText().toString(), getString(R.string.search_target))) {
                        alertDialog.setMessage(checked_btn.getText().toString() + getString(R.string.succeed_msg)).show();
                    } else {
                        alertDialog.setMessage(R.string.fail_msg).show();
                    }
                }
            });
        }
    }

    public void setRadioGroupOnCheckedChangeListener() {
        RadioGroup category = findViewById(R.id.category);
        category.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checked_btn = findViewById(checkedId);
                Toast.makeText(getApplicationContext(),
                        checked_btn.getText().toString() + getString(R.string.was_chosen), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
