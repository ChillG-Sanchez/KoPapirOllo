package com.example.kopapirollo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ImageView playerChoiceImageView, computerChoiceImageView;
    private TextView resultTextView, tiesTextView;
    private ImageView[] playerLives, computerLives;
    private int playerScore = 0;
    private int computerScore = 0;
    private int ties = 0;
    private final int maxLives = 3;
    private final Handler handler = new Handler();
    private final String[] choices = {"Kő", "Papír", "Olló"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton rockButton = findViewById(R.id.rockButton);
        ImageButton paperButton = findViewById(R.id.paperButton);
        ImageButton scissorsButton = findViewById(R.id.scissorsButton);
        playerChoiceImageView = findViewById(R.id.playerChoiceImageView);
        computerChoiceImageView = findViewById(R.id.computerChoiceImageView);
        resultTextView = findViewById(R.id.resultTextView);
        tiesTextView = findViewById(R.id.tiesTextView);

        playerLives = new ImageView[]{
                findViewById(R.id.playerLife1),
                findViewById(R.id.playerLife2),
                findViewById(R.id.playerLife3)
        };
        computerLives = new ImageView[]{
                findViewById(R.id.computerLife1),
                findViewById(R.id.computerLife2),
                findViewById(R.id.computerLife3)
        };
        rockButton.setOnClickListener(view -> startGame("Kő"));
        paperButton.setOnClickListener(view -> startGame("Papír"));
        scissorsButton.setOnClickListener(view -> startGame("Olló"));
    }

    @SuppressLint("SetTextI18n")
    private void startGame(String playerChoice) {
        simulateComputerThinking(() -> {
            String computerChoice = getComputerChoice();
            updateChoices(playerChoice, computerChoice);
            String result = getResult(playerChoice, computerChoice);
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
            resultTextView.setText(result);
            tiesTextView.setText("Döntetlenek száma: " + ties);
            if (playerScore == maxLives || computerScore == maxLives) {
                showEndGameDialog();
            }
        });
    }

    private void simulateComputerThinking(Runnable onComplete) {
        final int[] currentIndex = {0};
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentIndex[0] < choices.length) {
                    updateComputerChoice(choices[currentIndex[0]]);
                    currentIndex[0]++;
                    handler.postDelayed(this, 500);
                } else {
                    onComplete.run();
                }
            }
        }, 500);
    }

    private void updateChoices(String playerChoice, String computerChoice) {
        updatePlayerChoice(playerChoice);
        updateComputerChoice(computerChoice);
    }

    private void updatePlayerChoice(String choice) {
        if (choice.equals("Kő")) {
            playerChoiceImageView.setImageResource(R.drawable.rock);
        } else if (choice.equals("Papír")) {
            playerChoiceImageView.setImageResource(R.drawable.paper);
        } else if (choice.equals("Olló")) {
            playerChoiceImageView.setImageResource(R.drawable.scissors);
        }
    }

    private String getComputerChoice() {
        return choices[new Random().nextInt(choices.length)];
    }

    private void updateComputerChoice(String choice) {
        if (choice.equals("Kő")) {
            computerChoiceImageView.setImageResource(R.drawable.rock);
        } else if (choice.equals("Papír")) {
            computerChoiceImageView.setImageResource(R.drawable.paper);
        } else if (choice.equals("Olló")) {
            computerChoiceImageView.setImageResource(R.drawable.scissors);
        }
    }

    private String getResult(String playerChoice, String computerChoice) {
        if (playerChoice.equals(computerChoice)) {
            ties++;
            return "Döntetlen!";
        } else if ((playerChoice.equals("Kő") && computerChoice.equals("Olló")) ||
                (playerChoice.equals("Papír") && computerChoice.equals("Kő")) ||
                (playerChoice.equals("Olló") && computerChoice.equals("Papír"))) {
            playerScore++;
            updateLives(computerLives, computerScore);
            return "Nyertél!";
        } else {
            computerScore++;
            updateLives(playerLives, playerScore);
            return "Vesztettél!";
        }
    }

    private void updateLives(ImageView[] lives, int currentScore) {
        for (int i = 0; i < lives.length; i++) {
            if (i < currentScore) {
                lives[i].setVisibility(View.INVISIBLE);
            } else {
                lives[i].setVisibility(View.VISIBLE);
            }
        }
    }

    private void showEndGameDialog() {
        String message = (playerScore == maxLives) ? "Győzelem!" : "Vereség!";
        new AlertDialog.Builder(this)
                .setTitle(message)
                .setMessage("Szeretnél új játékot játszani?")
                .setPositiveButton("Igen", (dialog, which) -> resetGame())
                .setNegativeButton("Nem", (dialog, which) -> finish())
                .show();
    }

    @SuppressLint("SetTextI18n")
    private void resetGame() {
        playerScore = 0;
        computerScore = 0;
        ties = 0;
        resultTextView.setText("");
        tiesTextView.setText("Döntetlenek száma: 0");
        for (ImageView life : playerLives) life.setVisibility(View.VISIBLE);
        for (ImageView life : computerLives) life.setVisibility(View.VISIBLE);
    }
}
