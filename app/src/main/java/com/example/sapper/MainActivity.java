package com.example.sapper;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
  Random random = new Random();
  Button[][] cells;
  char[][] cellsValue;
  final int MINESCONST = 5;
  int flagsCurrent = MINESCONST;
  int cntOfDefused = 0;
  TextView mines;
  final int WIDTH = 10;
  final int HEIGHT = 10;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mines = findViewById(R.id.mines);
    mines.setText("" + flagsCurrent + " / " + MINESCONST);
    generate();
  }

  public void generate() {
    GridLayout layout = findViewById(R.id.Grid);
    layout.removeAllViews();
    layout.setColumnCount(WIDTH);

    cells = new Button[HEIGHT][WIDTH];
    cellsValue = new char[HEIGHT][WIDTH];

    for (int i = 0; i < HEIGHT; i++) {
      for (int j = 0; j < WIDTH; j++) {
        cellsValue[i][j] = '0';
      }
    }

    LayoutInflater inflater =
        (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
    for (int i = 0; i < MINESCONST; i++) {
      cellsValue[random.nextInt(HEIGHT)][random.nextInt(WIDTH)] = '*';
    }
    for (int i = 0; i < HEIGHT; i++) {
      for (int j = 0; j < WIDTH; j++) {
        cells[i][j] = (Button) inflater.inflate(R.layout.cell, layout, false);
        if (cellsValue[i][j] == '*') {
          if (i > 0 && cellsValue[i - 1][j] != '*') {
            cellsValue[i - 1][j] =
                (char) ('0' + (Character.getNumericValue(cellsValue[i - 1][j]) + 1));
          }
          if (j > 0 && cellsValue[i][j - 1] != '*') {
            cellsValue[i][j - 1] =
                (char) ('0' + (Character.getNumericValue(cellsValue[i][j - 1]) + 1));
          }
          if (j != WIDTH - 1 && cellsValue[i][j + 1] != '*') {
            cellsValue[i][j + 1] =
                (char) ('0' + (Character.getNumericValue(cellsValue[i][j + 1]) + 1));
          }
          if (i != WIDTH - 1 && cellsValue[i + 1][j] != '*') {
            cellsValue[i + 1][j] =
                (char) ('0' + (Character.getNumericValue(cellsValue[i + 1][j]) + 1));
          }
          //
          if (i > 0 && j > 0 && cellsValue[i - 1][j - 1] != '*') {
            cellsValue[i - 1][j - 1] =
                (char) ('0' + (Character.getNumericValue(cellsValue[i - 1][j - 1]) + 1));
          }
          if (i != WIDTH - 1 && j > 0 && cellsValue[i + 1][j - 1] != '*') {
            cellsValue[i + 1][j - 1] =
                (char) ('0' + (Character.getNumericValue(cellsValue[i + 1][j - 1]) + 1));
          }
          if (i > 0 && j != WIDTH - 1 && cellsValue[i - 1][j + 1] != '*') {
            cellsValue[i - 1][j + 1] =
                (char) ('0' + (Character.getNumericValue(cellsValue[i - 1][j + 1]) + 1));
          }
          if (i != WIDTH - 1 && j != WIDTH - 1 && cellsValue[i + 1][j + 1] != '*') {
            cellsValue[i + 1][j + 1] =
                (char) ('0' + (Character.getNumericValue(cellsValue[i + 1][j + 1]) + 1));
          }

        }
      }
    }

    for (int i = 0; i < HEIGHT; i++) {
      for (int j = 0; j < WIDTH; j++) {
        int finalI = i;
        int finalJ = j;

        // cells[i][j].setText("" + (j + HEIGHT * i + 1));
        // cells[finalI][finalJ].setText("" + cellsValue[finalI][finalJ]);


        cells[i][j].setTag("" + (j + HEIGHT * i));
        cells[i][j].setOnClickListener(
            new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                if (cellsValue[finalI][finalJ] == '*') {
                  Toast.makeText(getApplicationContext(), "LOSE(((", Toast.LENGTH_LONG).show();
                  Runtime.getRuntime().exit(0);
                }
                cells[finalI][finalJ].setText("" + cellsValue[finalI][finalJ]);
                v.setBackgroundColor(Color.GRAY);
              }
            });
        cells[i][j].setOnLongClickListener(
            new OnLongClickListener() {
              @Override
              public boolean onLongClick(View v) {
                if (flagsCurrent > 0) {
                  v.setBackgroundColor(Color.BLUE);
                  --flagsCurrent;
                  mines.setText("" + flagsCurrent + " / " + MINESCONST);
                  if (cellsValue[finalI][finalJ] == '*') {
                    cntOfDefused++;
                  }
                  if (cntOfDefused == MINESCONST) {
                    Toast.makeText(getApplicationContext(), "WIN!!11!", Toast.LENGTH_LONG).show();
                    Runtime.getRuntime().exit(0);
                  }
                }
                return true;
              }
            });
        layout.addView(cells[i][j]);
      }
    }
  }
}
