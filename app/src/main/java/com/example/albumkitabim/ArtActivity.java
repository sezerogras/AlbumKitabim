package com.example.albumkitabim;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;


public class ArtActivity extends AppCompatActivity {
    private ActivityArtBinding binding;
    Bitmap select;
    SQLiteDatabase database; // bir cok yerde kullanacagım için buraya tanımladım

    ActivityResultLauncher<Intent> activityResultLauncher;  // galeriye gitmek için
    ActivityResultLauncher<String> permissionLauncher;       // izin istemek için

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityArtBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        registerLauncher();
        database = this.openOrCreateDatabase("Arts", MODE_PRIVATE, null);


        Intent intent = getIntent();
        String info = intent.getStringExtra("info");

        if (info.matches("new")) {
            //new art
            binding.nameText.setText("");
            binding.artText.setText("");
            binding.yearText.setText("");
            Bitmap select = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.select);
            binding.imageView2.setImageResource(R.drawable.select);
            binding.button.setVisibility(View.VISIBLE); // Buton burada görünür olsun

        } else {
            int artId = intent.getIntExtra("artId", 0);
            binding.button.setVisibility(View.INVISIBLE); // burada ise buton gözükmesin


            try {
                Cursor cursor = database.rawQuery("SELECT * FROM arts WHERE id =?", new String[]{String.valueOf(artId)});
                int artNameIx = cursor.getColumnIndex("artname");
                int painterNameIx = cursor.getColumnIndex("paintername");
                int yearIx = cursor.getColumnIndex("year");
                int imageIx = cursor.getColumnIndex("image");

                while (cursor.moveToNext()) {

                    binding.nameText.setText(cursor.getString(artNameIx));
                    binding.artText.setText(cursor.getString(painterNameIx));
                    binding.yearText.setText(cursor.getString(yearIx));
                    byte[] bytes = cursor.getBlob(imageIx);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);// byte dizisini alıp bitmapa ceviriyor
                    binding.imageView2.setImageBitmap(bitmap);


                }
                cursor.close();


            } catch (Exception e) {

                e.printStackTrace(); // bu şekilde sıkıntıyı alabilirim


            }
        }

    }


}