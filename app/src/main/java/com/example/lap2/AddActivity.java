package com.example.lap2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lap2.model.Contact;

public class AddActivity extends AppCompatActivity {
    private EditText editName,editId,editPhone;
    private ImageView ivContact;
    private Button btnAddContact,btnChooseImage;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private Uri img;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        editName=findViewById(R.id.editName);
        editId=findViewById(R.id.editId);
        editPhone=findViewById(R.id.editPhone);
        ivContact=findViewById(R.id.ivContact);
        btnAddContact=findViewById(R.id.btnAddContact);
        btnChooseImage=findViewById(R.id.btnChooseImage);
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        // Xử lý ảnh đã chọn
                        Intent data = result.getData();
                        Uri selectedImageUri = data.getData();

                        // Hiển thị ảnh lên ImageView
                        if (selectedImageUri != null) {
                            img=selectedImageUri;
                            ivContact.setImageURI(selectedImageUri);

                        }
                    }
                });

        btnChooseImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryLauncher.launch(intent);

        });

        btnAddContact.setOnClickListener(v -> {
            String id=editId.getText().toString().trim();
            String name=editName.getText().toString().trim();
            String phone=editPhone.getText().toString().trim();
            if(id.isEmpty()||name.isEmpty()||phone.isEmpty()){
                Toast.makeText(AddActivity.this,"Thông tin chưa đầy đủ!",Toast.LENGTH_SHORT).show();
                return;
            }
            Contact contact=new Contact();
            contact.setId(Integer.parseInt(id));
            contact.setName(name);
            contact.setPhoneNumber(phone);
            contact.setStatus(false);
            contact.setImg(img.toString());
            Intent intent=new Intent(AddActivity.this, MainActivity.class);
            intent.putExtra("contact",contact);
            setResult(RESULT_OK,intent);
            finish();
        });
    }
}
