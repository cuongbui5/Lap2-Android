package com.example.lap2;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.lap2.model.Contact;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> launcher;
    private ContactAdapter contactAdapter;
    private RecyclerView rvContact;
    private List<Contact> contacts=new ArrayList<>();
    //private MyContentProvider myContentProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnAdd=findViewById(R.id.btnAdd);
        rvContact=findViewById(R.id.rvContact);
        rvContact.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.ItemDecoration itemDecoration=new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        rvContact.addItemDecoration(itemDecoration);
        contactAdapter=new ContactAdapter(contacts);
        rvContact.setAdapter(contactAdapter);
        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU&&data!=null) {
                            Contact contact=data.getSerializableExtra("contact", Contact.class);
                            Log.d("contact",contact.getName());
                            contacts.add(contact);
                            contactAdapter.notifyDataSetChanged();

                        }

                    }
                }
        );
        btnAdd.setOnClickListener(v -> {
            Intent intent=new Intent(MainActivity.this, AddActivity.class);
            launcher.launch(intent);


        });

        Button btnRemove=findViewById(R.id.btnDelete);
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean c = false;
                for(Contact contact:contacts){
                    if(contact.isStatus()){
                        c=true;
                        break;
                    }
                }

                if(!c||contacts.isEmpty()){
                    Toast.makeText(MainActivity.this,"Không có mục nào đc chọn!",Toast.LENGTH_SHORT).show();

                }else {
                    Dialog alertDialog = new AlertDialog.Builder(MainActivity.this) // this: Activity
                            .setTitle("Xác nhận")
                            .setMessage("Xóa các mục đã chọn.Bạn có chắc không?")
                            .setPositiveButton("ok", (dialog, which) -> {
                                contacts.removeIf(c1 -> c1.isStatus());
                                contactAdapter.notifyDataSetChanged();
                                dialog.dismiss();
                            })
                            .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                            .show();
                }



            }
        });

    }




}