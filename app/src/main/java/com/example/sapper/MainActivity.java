package com.example.sapper;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.GridLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.net.CookieHandler;

public class MainActivity extends AppCompatActivity {

  Button[][] cells;

  final int WIDTH = 10;
  final int HEIGHT = 10;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    generate();
  }

  public void generate() {
    GridLayout layout = findViewById(R.id.Grid);
    layout.removeAllViews();
    layout.setColumnCount(WIDTH);

    cells = new Button[HEIGHT][WIDTH];
    LayoutInflater inflater =
        (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

    for (int i = 0; i < HEIGHT; i++) {
      for (int j = 0; j < WIDTH; j++) {
        cells[i][j] = (Button) inflater.inflate(R.layout.cell, layout, false);
      }
    }

    for (int i = 0; i < HEIGHT; i++) {
      for (int j = 0; j < WIDTH; j++) {
        cells[i][j].setText("" + (j + HEIGHT * i));
        cells[i][j].setTag("" + (j + HEIGHT * i));
        cells[i][j].setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                v.setBackgroundColor(Color.RED);
              }
            });
        cells[i][j].setOnLongClickListener(new OnLongClickListener() {
          @Override
          public boolean onLongClick(View v) {
            v.setBackgroundColor(Color.BLUE);
            return true;
          }
        });
        layout.addView(cells[i][j]);
      }
    }
  }
}
