package org.boutry.watermelon;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.LocationManager;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import androidx.annotation.NonNull;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.boutry.watermelon.data.LoginDataSource;
import org.boutry.watermelon.data.LoginRepository;
import org.boutry.watermelon.data.db.UserDbHelper;
import org.boutry.watermelon.data.model.NotLoggedInException;
import org.boutry.watermelon.data.model.User;
import org.boutry.watermelon.ui.login.LoginActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PERMISSION_REQUEST_MAPS = 2;
    private String currentPhotoPath;
    private ImageView profilePicture;
    private AppBarConfiguration mAppBarConfiguration;
    private UserDbHelper userDbHelper;
    private User user;
    private LoginRepository instance;
    private TextView displayName;
    private TextView email;
    private Button btnLocale;
    private DrawerLayout drawerLayout;
    private LocationManager locationManager;
    private String provider;
    private final static String INSTANCE_USER = "USER";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((View view) -> {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
        );
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawerLayout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        this.instance = LoginRepository.getInstance();
        this.userDbHelper = new UserDbHelper(getApplicationContext());
        requestMapsPermission();

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    private void checkLoginState() {

        try {
            user = instance.getLoggedInUser().getUser();
            displayName.setText(user.getDisplayName());
            email.setText(user.getEmail());
            /*Request req = LoggedInRequest.get(instance.getLoggedInUser(), "http://192.168.43.27:8000/v1/users");
            new RestAsync(handler, this::handleResult).execute(req);*/
        } catch (NotLoggedInException e) {
            Intent loginActivty = new Intent(this, LoginActivity.class);
            startActivity(loginActivty);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        this.displayName = findViewById(R.id.txtDisplayName);
        this.email = findViewById(R.id.txtEmail);
        this.btnLocale = findViewById(R.id.btnLocale);
        btnLocale.setOnClickListener(this::toggleLocale);
        this.checkLoginState();
        profilePicture = findViewById(R.id.profilePicture);
        profilePicture.setOnClickListener(this::onProfilePicturePress);
        Bitmap dbImage = loadPhotoFromDb();
        if (dbImage != null) {
            profilePicture.setImageBitmap(dbImage);
        }
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void toggleLocale(View view) {
        String otherLocale = getResources().getString(R.string.other_locale);
        Locale locale;
        if (otherLocale.equals("EN")) {
            locale = Locale.getDefault();
        } else {
            locale = new Locale("fr");
        }
        System.out.println("Locale " + locale);
        Locale.setDefault(locale);
        Configuration config = getResources().getConfiguration();
        config.setLocale(locale);
        config.setLayoutDirection(locale);
        createConfigurationContext(config);
        recreate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            File photo = new File(currentPhotoPath);
            if (!photo.exists()) {
                Toast.makeText(this, "Error while getting the photo", Toast.LENGTH_SHORT).show();
                return;
            }
            savePhotoPathToDB(photo.getAbsolutePath());
            Bitmap imageBitmap = loadPhotoFromPath(photo.getAbsolutePath());
            if (profilePicture != null)
                profilePicture.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == PERMISSION_REQUEST_MAPS) {
            // Request for camera permission.
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
            } else {
                // Permission request was denied.
                Snackbar.make(drawerLayout, R.string.maps_access_required,
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }

    public void requestMapsPermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with cda button to request the missing permission.
            Snackbar.make(drawerLayout, R.string.maps_access_required,
                    Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok, view -> {
                // Request the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSION_REQUEST_MAPS);
            }).show();

        }
    }

    private Bitmap loadPhotoFromPath(String path) {
        return ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(path), 150, 200);

    }

    private Bitmap loadPhotoFromDb() {
        SQLiteDatabase db = userDbHelper.getReadableDatabase();

        String[] projection = {
                "photo"
        };

        String selection = "id=?";
        if (this.user == null)
            return null;
        String[] selectionArgs = {this.user.getId() + ""};
        String path = null;

        try (Cursor cursor = db.query(
                UserDbHelper.PHOTOS,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        )) {
            while (cursor.moveToNext()) {
                path = cursor.getString(
                        cursor.getColumnIndexOrThrow("photo"));
            }

        }
        if (path == null) {
            return null;
        }

        return loadPhotoFromPath(path);

    }

    private void savePhotoPathToDB(String path) {
        SQLiteDatabase db = this.userDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", this.user.getId());
        values.put("photo", path);

        long id = db.insertWithOnConflict(UserDbHelper.PHOTOS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        if (id == -1) {
            Toast.makeText(this, "Error while inserting photo in DB", Toast.LENGTH_SHORT).show();
        }
    }

    public void onProfilePicturePress(View v) {
        System.out.println("PICTURE PRESSED !");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "org.boutry.watermelon.provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePictureIntent.setFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

}
