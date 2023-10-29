package com.example.sapper;

import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.util.Pair;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
  Random random = new Random();
  Button[][] cells;
  char[][] cellsValue;
  final int MINES = random.nextInt(30 - 5) + 5;
  int cntOfFlags = MINES;
  int cntOfDefused = 0;
  TextView mines;
  final int WIDTH = 10;
  final int HEIGHT = 10;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mines = findViewById(R.id.mines);
    mines.setText("" + cntOfFlags + " / " + MINES + " \uD83D\uDEA9");
    Generate();
  }

  private void Generate() {
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

    for (int i = 0; i < MINES; i++) {
      cellsValue[random.nextInt(HEIGHT)][random.nextInt(WIDTH)] = '*';
    }

    for (int i = 0; i < HEIGHT; i++) {
      for (int j = 0; j < WIDTH; j++) {
        cells[i][j] = (Button) inflater.inflate(R.layout.cell, layout, false);
        SetCellColor(i, j, false);
        FillTheCellsAroundMines(i, j);
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
      OpenAdjacentCells(finalI, finalJ, visitedCells);
    }
  }

  private void OpenAdjacentCells(int i, int j, Set<Pair<Integer, Integer>> visitedCells) {
    for (int row = i - 1; row <= i + 1; row++) {
      for (int col = j - 1; col <= j + 1; col++) {
        if (row >= 0 && row < HEIGHT && col >= 0 && col < WIDTH) {
          Pair<Integer, Integer> cell = new Pair<>(row, col);
          if (!visitedCells.contains(cell)) {
            visitedCells.add(cell);
            if (cellsValue[row][col] == '0') {
              SetCellColor(row, col, true);
              OpenAdjacentCells(row, col, visitedCells);
            } else {
              SetCellColor(row, col, true);
              cells[row][col].setText("" + cellsValue[row][col]);
            }
          }
        }
      }
    }
  }

  private void CellOnLongClick(int finalI, int finalJ) {
    if (cntOfFlags > 0 && cells[finalI][finalJ].getText().length() == 0) {
      cells[finalI][finalJ].setText("\uD83D\uDEA9");
      --cntOfFlags;
      mines.setText("" + cntOfFlags + " / " + MINES + " \uD83D\uDEA9");
      if (cellsValue[finalI][finalJ] == '*') {
        cntOfDefused++;
      }
      if (cntOfDefused == MINES) {
        Toast.makeText(getApplicationContext(), "WIN!!11!", Toast.LENGTH_LONG).show();
        Runtime.getRuntime().exit(0);
      }
    } else if (cells[finalI][finalJ].getText() == "\uD83D\uDEA9") {
      if (cellsValue[finalI][finalJ] == '*') {
        cntOfDefused--;
      }
      cntOfFlags++;
      mines.setText("" + cntOfFlags + " / " + MINES + " \uD83D\uDEA9");
      cells[finalI][finalJ].setText("");
    }
  }

  private void FillTheCellsAroundMines(int i, int j) {
    if (cellsValue[i][j] == '*') {
      for (int row = i - 1; row <= i + 1; row++) {
        for (int col = j - 1; col <= j + 1; col++) {
          if (row >= 0 && row < HEIGHT && col >= 0 && col < WIDTH && cellsValue[row][col] != '*') {
            cellsValue[row][col] =
                (char) ('0' + (Character.getNumericValue(cellsValue[row][col]) + 1));
          }
        }
      }
    }
  }

  private void SetCellColor(int i, int j, boolean isOpened) {
    if (!isOpened) {
      if ((i + j) % 2 == 0) {
        cells[i][j].setBackgroundColor(0xffff7514);
      } else {
        cells[i][j].setBackgroundColor(0xffff4d00);
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
