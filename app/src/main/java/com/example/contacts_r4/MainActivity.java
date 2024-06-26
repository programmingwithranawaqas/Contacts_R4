package com.example.contacts_r4;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    EditText etName, etPhone;
    Button btnAdd, btnView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        btnAdd.setOnClickListener(v->{
            String name = etName.getText().toString().trim();
            String phone = etPhone.getText().toString();

            if(TextUtils.isEmpty(name))
            {
                etName.setError("Enter the name");
                return;
            }

            if(TextUtils.isEmpty(phone))
            {
                etPhone.setError("Enter the phone");
                return;
            }



            DatabaseModel databaseModel = new DatabaseModel(this);
            databaseModel.open();

            databaseModel.addContact(name, phone);

            // clear the fields after adding records into the table
            etName.setText("");
            etPhone.setText("");

            databaseModel.close();

        });



        btnView.setOnClickListener(v->{
            startActivity(new Intent(MainActivity.this, ViewContacts.class));
        });




    }

    private void init()
    {
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        btnAdd = findViewById(R.id.btnAdd);
        btnView = findViewById(R.id.btnViewContact);
    }
}