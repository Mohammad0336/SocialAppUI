package com.example.socialapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AddPostActivity extends AppCompatActivity {

    private final int GALLERY_REQ_CODE = 1;
    private final int CAMERA_REQUEST_CODE = 2;

    private ImageView imageView;
    private EditText captionEditText;
    private byte[] imageBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        imageView = findViewById(R.id.imageView);
        captionEditText = findViewById(R.id.captionEditText);

        Button takePhotoButton = findViewById(R.id.takePhotoButton);
        Button uploadPhotoButton = findViewById(R.id.uploadPhotoButton); // New button for uploading from the gallery

        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });

        // Set OnClickListener for the "Upload from Gallery" button
        uploadPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertImage(v);
            }
        });

        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPost();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // Capturing picture
            if (requestCode == CAMERA_REQUEST_CODE) {
                if (data != null && data.getExtras() != null) {

                    Bitmap capturedImage = (Bitmap) data.getExtras().get("data");

                    // Convert the Bitmap image to a byte array
                    imageBytes = convertBitmapToBytes(capturedImage);

                    // Display image
                    imageView.setImageBitmap(capturedImage);
                }
            }

            // Uploading picture
            if (requestCode == GALLERY_REQ_CODE) {
                if (data != null && data.getData() != null) {
                    Uri imageUri = data.getData();

                    // Convert the selected image to bytes
                    imageBytes = convertImageToBytes(imageUri);

                    // Display image
                    imageView.setImageURI(imageUri);
                }
            }
        }
    }

    // Function to convert an image to a byte array for DB storing
    private byte[] convertImageToBytes(Uri imageUri) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }

    // Function to convert a bitmap to a byte array for DB storing
    private byte[] convertBitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private void addPost() {
        // Get the caption and image (if available)
        String caption = captionEditText.getText().toString();

        // Create a Post object (replace this with your actual Post class constructor)
        Post post = new Post("John_Doe@gmail.com", caption, imageBytes);

        // Pass the post data back to the calling activity (HomeFragment)
        Intent resultIntent = new Intent();
        resultIntent.putExtra("newPost", post);
        setResult(RESULT_OK, resultIntent);

        // Finish the activity
        finish();
    }

    public void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    public void insertImage(View view) {
        Intent gallery = new Intent(Intent.ACTION_PICK);
        gallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, GALLERY_REQ_CODE);
    }
}

