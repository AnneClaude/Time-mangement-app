package com.example.timewise;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private TextView editEmail, editPassword, editUserName;
    private ImageView profileImage;
    private Uri photoUri;

    // Keys and SharedPreferences name for data storage.
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PROFILE_IMAGE = "profile_image"; // New key for storing profile image URI

    private SharedPreferences sharedPreferences;

    private static final int CAMERA_PERMISSION_CODE = 102; //defines a constant integer used as a request code when asking the user for camera permission


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileImage = view.findViewById(R.id.profilePic);
        editPassword = view.findViewById(R.id.text_password);
        editEmail = view.findViewById(R.id.text_email);
        editUserName = view.findViewById(R.id.text_name);

        // Access the shared preferences using the specified name and private mode
        sharedPreferences = requireContext().getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        // Retrieve the stored password from shared preferences
        String password = sharedPreferences.getString(KEY_PASSWORD, null);

        // Retrieve the stored email from shared preferences
        String email = sharedPreferences.getString(KEY_EMAIL, null);

        // Retrieve the stored username from shared preferences
        String username = sharedPreferences.getString(KEY_USERNAME, null);

        // Retrieve the stored profile image URI (as a string) from shared preferences
        String imageUriString = sharedPreferences.getString(KEY_PROFILE_IMAGE, null);

        if (email != null) editEmail.setText(email);
        if (password != null) editPassword.setText(password);
        if (username != null) editUserName.setText(username);

        // Load and display saved profile image
        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            profileImage.setImageURI(imageUri);
        }

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePicker();
            }
        });

        // Request camera permission if not granted
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    private void showImagePicker() {
        // Create an intent to pick an image from the external gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // Create an intent to capture a photo using the device camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Create a file where the captured photo will be saved
        File photoFile = createImageFile();

        // If the file was successfully created, proceed to set its URI for the camera intent
        if (photoFile != null) {
            // Get a URI for the file using FileProvider
            photoUri = FileProvider.getUriForFile(
                    getActivity(),
                    "com.example.TimeWise.fileprovider", // Authority defined in the manifest
                    photoFile
            );

            // Set the output location for the camera intent to the URI of the file
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        }

        // Create a chooser intent to allow the user to select between gallery and camera
        Intent chooser = Intent.createChooser(galleryIntent, "Select an Image");

        // Add the camera intent to the chooser's initial intents
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntent});

        // Launch the chooser activity and wait for result with request code 999
        startActivityForResult(chooser, 999);
    }


    private File createImageFile() {
        File storageDir = requireContext().getExternalFilesDir(null);
        try {
            return File.createTempFile(
                    "IMG_" + System.currentTimeMillis(),
                    ".jpg",
                    storageDir
            );
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Couldn't create image file", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            Uri selectedImage = null;

            if (data != null && data.getData() != null) {
                selectedImage = data.getData();
            } else if (photoUri != null) {
                selectedImage = photoUri;
            }

            if (selectedImage != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                    profileImage.setImageBitmap(getCircularBitmap(bitmap)); // Set circular image

                    // Save the URI in SharedPreferences
                    sharedPreferences.edit().putString(KEY_PROFILE_IMAGE, selectedImage.toString()).apply();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getActivity(), "Failed to get image", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Image selection cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Camera permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Bitmap getCircularBitmap(Bitmap bitmap) {
        int size = Math.min(bitmap.getWidth(), bitmap.getHeight());
        Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        float radius = size / 2f;
        canvas.drawCircle(radius, radius, radius, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, (size - bitmap.getWidth()) / 2f, (size - bitmap.getHeight()) / 2f, paint);

        return output;
    }
}
