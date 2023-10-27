package com.example.sapper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.util.Pair;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
  Random random = new Random();
  Button[][] cells;
  char[][] cellsValue;
  final int MINESCONST = random.nextInt(30 - 5) + 5;
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
    mines.setText("" + flagsCurrent + " / " + MINESCONST + " \uD83D\uDEA9");
    generate();
  }

  private void generate() {
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
        SetCellColor(i, j, false);
        fillTheCellsWithNumbers(i, j);
      }
    }

    for (int i = 0; i < HEIGHT; i++) {
      for (int j = 0; j < WIDTH; j++) {
        int finalI = i;
        int finalJ = j;

        cells[i][j].setTag("" + (j + HEIGHT * i));
        cells[i][j].setOnClickListener(v -> CellOnClick(finalI, finalJ));
        cells[i][j].setOnLongClickListener(
            v -> {
              CellOnLongClick(finalI, finalJ);
              return true;
            });
        layout.addView(cells[i][j]);
      }
    }
  }

  private void CellOnClick(int finalI, int finalJ) {
    if (cellsValue[finalI][finalJ] == '*') {
      Toast.makeText(getApplicationContext(), "LOSE(((", Toast.LENGTH_LONG).show();
      Runtime.getRuntime().exit(0);
    }
    if (cellsValue[finalI][finalJ] != '0') {
      cells[finalI][finalJ].setText("" + cellsValue[finalI][finalJ]);
    }
    SetCellColor(finalI, finalJ, true);
    OpenZeroCells(finalI, finalJ);
  }

  private void OpenZeroCells(int finalI, int finalJ) {
    if (cellsValue[finalI][finalJ] == '0') {
      Set<Pair<Integer, Integer>> visitedCells = new HashSet<>();
      openAdjacentCells(finalI, finalJ, visitedCells);
    }
  }

  private void openAdjacentCells(int i, int j, Set<Pair<Integer, Integer>> visitedCells) {
    for (int x = i - 1; x <= i + 1; x++) {
      for (int y = j - 1; y <= j + 1; y++) {
        if (x >= 0 && x < HEIGHT && y >= 0 && y < WIDTH) {
          Pair<Integer, Integer> cell = new Pair<>(x, y);
          if (!visitedCells.contains(cell)) {
            visitedCells.add(cell);
            if (cellsValue[x][y] == '0') {
              SetCellColor(x, y, true);
              openAdjacentCells(x, y, visitedCells);
            } else {
              SetCellColor(x, y, true);
              cells[x][y].setText("" + cellsValue[x][y]);
            }
          }
        }
      }
    }
  }



  private void CellOnLongClick(int finalI, int finalJ) {
    if (flagsCurrent > 0 && cells[finalI][finalJ].getText() != "\uD83D\uDEA9") {
      cells[finalI][finalJ].setText("\uD83D\uDEA9");
      --flagsCurrent;
      mines.setText("" + flagsCurrent + " / " + MINESCONST + " \uD83D\uDEA9");
      if (cellsValue[finalI][finalJ] == '*') {
        cntOfDefused++;
      }
      if (cntOfDefused == MINESCONST) {
        Toast.makeText(getApplicationContext(), "WIN!!11!", Toast.LENGTH_LONG).show();
        Runtime.getRuntime().exit(0);
      }
    } else if (cells[finalI][finalJ].getText() == "\uD83D\uDEA9") {
      if (cellsValue[finalI][finalJ] == '*') {
        cntOfDefused--;
      }
      flagsCurrent++;
      mines.setText("" + flagsCurrent + " / " + MINESCONST + " \uD83D\uDEA9");
      cells[finalI][finalJ].setText("");
    }
  }

  private void fillTheCellsWithNumbers(int i, int j) {
    if (cellsValue[i][j] == '*') {
      if (i > 0 && cellsValue[i - 1][j] != '*') {
        cellsValue[i - 1][j] = (char) ('0' + (Character.getNumericValue(cellsValue[i - 1][j]) + 1));
      }
      if (j > 0 && cellsValue[i][j - 1] != '*') {
        cellsValue[i][j - 1] = (char) ('0' + (Character.getNumericValue(cellsValue[i][j - 1]) + 1));
      }
      if (j != WIDTH - 1 && cellsValue[i][j + 1] != '*') {
        cellsValue[i][j + 1] = (char) ('0' + (Character.getNumericValue(cellsValue[i][j + 1]) + 1));
      }
      if (i != WIDTH - 1 && cellsValue[i + 1][j] != '*') {
        cellsValue[i + 1][j] = (char) ('0' + (Character.getNumericValue(cellsValue[i + 1][j]) + 1));
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

  private void SetCellColor(int i, int j, boolean isOpened) {
    if (!isOpened) {
      if ((i + j) % 2 == 0) {
        cells[i][j].setBackgroundColor(0xC860ff38);
      } else {
        cells[i][j].setBackgroundColor(0xff3dff0d);
      }
    } else {
      if ((i + j) % 2 == 0) {
        cells[i][j].setBackgroundColor(0xfffafafa);
      } else {
        cells[i][j].setBackgroundColor(0xC8fafafa);
      }
    }
  }
}
