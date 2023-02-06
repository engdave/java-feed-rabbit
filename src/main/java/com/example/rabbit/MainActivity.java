package com.example.rabbit;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private int score = 0;
    private int name_rabbit = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView mainImage = findViewById(R.id.main_image);
        ImageView smallImage1 = findViewById(R.id.small_image_1);
        ImageView smallImage2 = findViewById(R.id.small_image_2);
        ImageView smallImage3 = findViewById(R.id.small_image_3);

        smallImage1.setOnTouchListener(new ImageTouchListener());
        smallImage2.setOnTouchListener(new ImageTouchListener());
        smallImage3.setOnTouchListener(new ImageTouchListener());

        mainImage.setOnDragListener(new MainImageDragListener());
        MediaPlayer rabbit = MediaPlayer.create(MainActivity.this, R.raw.rabbit);
        mainImage.setOnClickListener(new MainImageClickListener(rabbit));
    }

    private static class MainImageClickListener implements View.OnClickListener {
        private final MediaPlayer sound;

        public MainImageClickListener(MediaPlayer sound) {
            this.sound = sound;
        }

        @Override
        public void onClick(View view) {
            sound.start();
        }
    }

    private static class ImageTouchListener implements View.OnTouchListener {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDragAndDrop(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }

    private class MainImageDragListener implements View.OnDragListener {
        @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
        @Override
        public boolean onDrag(View view, DragEvent event) {
            ImageView draggedImage = (ImageView) event.getLocalState();
            switch (event.getAction()) {
                case DragEvent.ACTION_DROP:
                    if (isOverlapping(draggedImage, view)) {
                        switch (draggedImage.getId()) {
                            case R.id.small_image_1:
                            case R.id.small_image_2:
                            case R.id.small_image_3:
                                score ++;
                                name_rabbit ++;
                                break;
                        }
                        TextView scoreText2 = findViewById(R.id.name_rabbit);
                        TextView scoreText = findViewById(R.id.score);
                        scoreText.setTextColor(Color.WHITE);
                        scoreText.setText("Wynik: " + score);
                        MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.sound);
                        mediaPlayer.start();
                    } else {
                        draggedImage.setVisibility(View.VISIBLE);
                        MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.sound);
                        mediaPlayer.start();
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    if (!event.getResult()) {
                        draggedImage.setVisibility(View.VISIBLE);
                    }
                    break;
            }
            return true;
        }

        private boolean isOverlapping(ImageView image1, View image2) {
            int[] image1Coords = new int[2];
            int[] image2Coords = new int[2];
            image1.getLocationOnScreen(image1Coords);
            image2.getLocationOnScreen(image2Coords);
            Rect image1Rect = new Rect(image1Coords[0], image1Coords[1], image1Coords[0] + image1.getWidth(), image1Coords[1] + image1.getHeight());
            Rect image2Rect = new Rect(image2Coords[0], image2Coords[1], image2Coords[0] + image2.getWidth(), image2Coords[1] + image2.getHeight());
            return image1Rect.intersect(image2Rect);
        }
    }
}
