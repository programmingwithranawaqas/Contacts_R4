package com.example.contacts_r4;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.helper.widget.Layer;

import java.util.List;

public class ContactAdapter extends ArrayAdapter<Contact> {

    public ContactAdapter(@NonNull Context context, @NonNull List<Contact> objects) {
        super(context, 0, objects);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if(v == null)
        {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.single_contact_design, parent, false);
        }

        Contact c = getItem(position);
        TextView tvName, tvNumber;
        ImageView ivCall;

        tvName = v.findViewById(R.id.tvName);
        tvNumber = v.findViewById(R.id.tvNumber);
        ivCall = v.findViewById(R.id.ivCall);

        assert c != null;
        tvNumber.setText(c.getPhone());
        tvName.setText(c.getName());

        ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:"+c.getPhone()));
                parent.getContext().startActivity(i);
            }
        });

        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Edit "+c.getName());

                View view = LayoutInflater.from(getContext()).inflate(R.layout.contact_form, null, false);

                alert.setView(view);
                EditText etName = view.findViewById(R.id.etName);
                EditText etPhone = view.findViewById(R.id.etPhone);

                etName.setText(c.getName());
                etPhone.setText(c.getPhone());

                alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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



                        DatabaseModel databaseModel = new DatabaseModel(getContext());
                        databaseModel.open();

                        databaseModel.updateContact(c.getId(),name, phone);
                        c.setName(name);
                        c.setPhone(phone);
                        notifyDataSetChanged();

                        // clear the fields after adding records into the table
                        etName.setText("");
                        etPhone.setText("");

                        databaseModel.close();
                    }

                });

                alert.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseModel db = new DatabaseModel(getContext());
                        db.open();
                        db.removeContact(c.getId());
                        remove(c);
                        notifyDataSetChanged();
                        db.close();
                    }
                });

                alert.show();

                return false;
            }
        });

        return v;
    }
}
