package com.example.bartolomedelunamatchinggame;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private GridLayout gameGrid;
    private TextView scoreText;
    private Button restartButton;
    private List<Integer> cardImages;
    private List<ImageButton> cardButtons;
    private int flippedCards = 0;
    private ImageButton firstCard, secondCard;
    private int matchesFound = 0;
    private final int TOTAL_PAIRS = 8;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameGrid = findViewById(R.id.gameGrid);
        scoreText = findViewById(R.id.scoreText);
        restartButton = findViewById(R.id.restartButton);

        initializeGame();

        restartButton.setOnClickListener(v -> initializeGame());
    }

    private void initializeGame() {
        gameGrid.removeAllViews();
        cardButtons = new ArrayList<>();
        matchesFound = 0;
        scoreText.setText("Matches: 0");
        cardImages = new ArrayList<>();
        for (int i = 1; i <= TOTAL_PAIRS; i++) {
            cardImages.add(getCardImageResource(i));
            cardImages.add(getCardImageResource(i));
        }
        Collections.shuffle(cardImages);

        for (int i = 0; i < 16; i++) {
            ImageButton card = new ImageButton(this);
            card.setTag(i);
            card.setImageResource(R.drawable.card_back);
            card.setBackgroundResource(R.drawable.card_background);
            card.setOnClickListener(this::onCardClick);
            card.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LayoutParams params = new GridLayout.LayoutParams();
            params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
            params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics());
            params.setMargins(4, 4, 4, 4);
            card.setLayoutParams(params);

            gameGrid.addView(card);
            cardButtons.add(card);
        }

        flippedCards = 0;
        firstCard = secondCard = null;
    }

    private void onCardClick(View view) {
        if (flippedCards >= 2) return;

        ImageButton card = (ImageButton) view;
        int position = (int) card.getTag();

        if (card.getDrawable().getConstantState() ==
                getResources().getDrawable(R.drawable.card_back).getConstantState()) {

            card.setImageResource(cardImages.get(position));
            flippedCards++;

            if (flippedCards == 1) {
                firstCard = card;
            } else {
                secondCard = card;
                checkForMatch();
            }
        }
    }

    private void checkForMatch() {
        if (firstCard.getDrawable().getConstantState() ==
                secondCard.getDrawable().getConstantState()) {

            matchesFound++;
            scoreText.setText("Matches: " + matchesFound);

            firstCard.setEnabled(false);
            secondCard.setEnabled(false);

            if (matchesFound == TOTAL_PAIRS) {
                scoreText.setText("Game Over! All matches found!");
            }

            resetTurn();
        } else {
            handler.postDelayed(this::flipCardsBack, 1000);
        }
    }

    private void flipCardsBack() {
        firstCard.setImageResource(R.drawable.card_back);
        secondCard.setImageResource(R.drawable.card_back);
        resetTurn();
    }

    private void resetTurn() {
        flippedCards = 0;
        firstCard = secondCard = null;
    }

    private int getCardImageResource(int value) {
        switch(value) {
            case 1: return R.drawable.card1;
            case 2: return R.drawable.card2;
            case 3: return R.drawable.card3;
            case 4: return R.drawable.card4;
            case 5: return R.drawable.card5;
            case 6: return R.drawable.card6;
            case 7: return R.drawable.card7;
            case 8: return R.drawable.card8;
            default: return R.drawable.card_back;
        }
    }
}