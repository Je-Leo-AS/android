package com.example.termo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private boolean secretWordCondition = true;
    private boolean gameOverCondition = false;
    private boolean tryWordCondition = false;
    public String[] letters = new String[5];
    public List<String> valores = new ArrayList<>();
    private boolean gameStarted = false;
    int letterClick = 0;
    int enterClick = 1;
    String wordSecret;
    String tryWord;
    Random random;
    Banco banco;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        random = new Random();
        banco = Banco.getInstance();
        onClickStart();
    }

    private void readCSV() {
        try {
            String palavra = null;
            Log.d("Read csv","Begin Read csv");

            int pos = 1 + random.nextInt(26);
            InputStream inputStream = getApplicationContext().getResources().openRawResource(R.raw.palavras_termo);
            Scanner scanner = new Scanner(inputStream);
            for (int i = 1; i <= pos; i++)
            {
                palavra = scanner.nextLine();
            }
            String[] termo = palavra.split("\\s*;\\s*");
            wordSecret = termo[1];
            Toast.makeText(getApplicationContext(), "Game Started: " + wordSecret, Toast.LENGTH_LONG).show();
            inputStream.close();
            scanner.close();
        }
        catch (Exception e) {
            Log.e("Read csv", "Something Wrong: " + e.getMessage());

        }
    }
    private void readBanco() {
        try {
            InputStream inputStream = getApplicationContext().getResources().openRawResource(R.raw.palavras_completo);
            Scanner scanner = new Scanner(inputStream);
            Log.d("Read Banco", "Begin Read banco");

            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                String[] colunas = linha.split("\\s*;\\s*");
                Log.d("Read Banco", "WordRead: " + colunas[1]);
                valores.add(colunas[1]);
            }
        }
        catch (Exception e) {
            Log.e("Read Banco", "Something Wrong: " + e.getMessage());
        }
    }
    public void onClickRestart(){
        if(isGameStarted()) {
            Toast.makeText(getApplicationContext(), "Game Restarted", Toast.LENGTH_LONG).show();
            setGameStart(false);
            setSecretWordCondition(true);
            for (int i = 0; i < letterClick; i++) {
                int textViewId = getResources().getIdentifier("tvLetter" + enterClick + letterClick, "id", getPackageName());
                AppCompatTextView textView = findViewById(textViewId);
                textView.setText("");
                readCSV();
                setGameStart(true);
                setSecretWordCondition(false);
            }
            letterClick = 0;

        }
    }
    public void onClickStart() {
        if(!isGameStarted()) {
            readCSV();
            readBanco();
            setGameStart(true);
            setSecretWordCondition(false);
        }
        else if(!isGameStarted() && isGameOver()){
            Toast.makeText(getApplicationContext(), "GameOver", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Game Started", Toast.LENGTH_LONG).show();
        }
    }

      public void onClickClear(View view) {
        if(!isGameOver() && letterClick > 0) {

            letters[letterClick - 1] = "";

            int textViewId = getResources().getIdentifier("tvLetter" + enterClick + letterClick, "id", getPackageName());
            AppCompatTextView textView = findViewById(textViewId);


            if (letters[letterClick - 1] != "") {
                String text = textView.getText().toString();
                textView.setText(letters[letterClick - 1].toString());
                textView.setText("");
            }
            else {
                textView.setText("");
            }
        } else {
            Toast.makeText(getApplicationContext(), "Insert a Letter", Toast.LENGTH_SHORT).show();
        }
        if(letterClick > 0){
            letterClick--;
        }


    }
    public void onClickTextView(View view) {
        int textViewId = view.getId();
        String textViewIdAsString = getResources().getResourceEntryName(textViewId);

        String[] parts = textViewIdAsString.split("tvLetter");
        if (parts.length == 2)
        {
            String lineStr = parts[1].substring(0, 1);
            String columnStr = parts[1].substring(1, 2);

            int line = Integer.parseInt(lineStr);
            int column = Integer.parseInt(columnStr);
            letterClick = column;
        }
    }
    public void verifyTryWord() {
            for (int i = 0; i < 5; i++)
            {
                if (letters[i] == null || letters[i].isEmpty())
                {
                    setValidWord(false);
                    break;
                } else {
                    setValidWord(true);
                }
            }

    }
    public void onClickLetter(View view)
    {
        if(!isGameOver()) {
            if (letterClick<5){
                letterClick++;
            }
            Button button = (Button) view;
            String buttonText = button.getText().toString();

            letters[letterClick - 1] = buttonText;

            int textViewId = getResources().getIdentifier("tvLetter" + enterClick + letterClick, "id", getPackageName());
            AppCompatTextView textView = findViewById(textViewId);

            String text = textView.getText().toString();
            textView.setText(buttonText.toString());


            if (letterClick > 5){

                letterClick = 5;
            }

        }
    }

    public void onClickEnter(View view)
    {
        verifyTryWord();
        if (isValidWord()) {
            tryWord = letters[0] + letters[1] + letters[2] + letters[3] + letters[4];
        }
        //Toast.makeText(getApplicationContext(), "TryWord: " + tryWord, Toast.LENGTH_LONG).show();
        if (!valores.contains(tryWord)){
            Toast.makeText(getApplicationContext(), "Invalid Word", Toast.LENGTH_LONG).show();
            return;
        }
        if (isSecretWordNull()){
            Toast.makeText(getApplicationContext(), "Start Game", Toast.LENGTH_LONG).show();
            return;
        }
        boolean[] letterMatched = new boolean[5];
        Arrays.fill(letterMatched, false);
        if(tryWord == wordSecret){
            Toast.makeText(getApplicationContext(), "GameOver", Toast.LENGTH_LONG).show();
            return;
        }

        if(isValidWord() && enterClick < 7)
        {
            for (int i = 0; i < 5; i++)
            {
                char letterTryWord = tryWord.charAt(i);

                for (int j = 0; j < 5; j++)
                {
                    char letterWordSecret = wordSecret.charAt(j);

                    // Log.d("click enter", wordSecret);
                    int editViewId = getResources().getIdentifier("tvLetter" + (enterClick) + (i + 1), "id", getPackageName());
                    TextView editView = findViewById(editViewId);

                    int resID = getResources().getIdentifier(letters[i], "id", getPackageName());
                    Button editButton = findViewById(resID);

                    if (letterTryWord == letterWordSecret && i == j){
                        if (!letterMatched[i])
                        {
                            setGreenTextView(editView);
                            setGreenKeyboardButton(editButton);
                            letterMatched[i] = true;
                            break;
                        }
                    }
                    else if (letterTryWord == letterWordSecret && i != j && !letterMatched[i])
                    {
                        setYellowTextView(editView);
                        setYellowKeyboardButton(editButton);
                        letterMatched[i] = true;
                        break;
                    }
                    else
                    {
                        setGrayTextView(editView);
                        setGrayKeyboardButton(editButton);
                    }
                }
            }

            Arrays.fill(letters, null);
            enterClick++;
            letterClick = 0;
            setValidWord(false);
            if(enterClick < 7)
                startIconColor();
        }

        else if (!isValidWord() && enterClick < 7){
            Toast.makeText(getApplicationContext(), "Finish Word", Toast.LENGTH_LONG).show();
        }

        else{
            enterClick = 7;
            setGameOver(true);
            Toast.makeText(getApplicationContext(), "GameOver", Toast.LENGTH_LONG).show();
        }
    }

    public void startIconColor(){
        for (int i = 0; i < 5; i++)
        {
            int editViewId = getResources().getIdentifier("tvLetter" + (enterClick) + (i + 1), "id", getPackageName());
            TextView editView = findViewById(editViewId);
            editView.setBackgroundResource(R.drawable.start_icon);
            editView.setTextAppearance(R.style.TextView_Style);
        }
    }

    public void setGreenTextView(TextView editView){
        editView.setBackgroundResource(R.drawable.green_icon);
        editView.setTextAppearance(R.style.TextView_Style);
    }
    public void setYellowTextView(TextView editView){
        editView.setBackgroundResource(R.drawable.yellow_icon);
        editView.setTextAppearance(R.style.TextView_Style);
    }

    public void setGrayTextView(TextView editView){
        editView.setBackgroundResource(R.drawable.gray_icon);
        editView.setTextAppearance(R.style.TextView_Style);
    }

    public void setGreenKeyboardButton(Button button){
        button.setBackgroundResource(R.drawable.green_icon);
        button.setTextAppearance(R.style.TextView_Style);
        button.setEnabled(true);
    }
    public void setYellowKeyboardButton(Button button){
        button.setBackgroundResource(R.drawable.yellow_icon);
        button.setTextAppearance(R.style.TextView_Style);
        button.setEnabled(true);
    }

    public void setGrayKeyboardButton(Button button){
        button.setBackgroundResource(R.drawable.gray_icon);
        button.setTextAppearance(R.style.TextView_Style);
        button.setEnabled(true);
    }

    public boolean isSecretWordNull()
    {
        return secretWordCondition;
    }

    public void setSecretWordCondition(boolean condition){
        secretWordCondition = condition;
    }
    public void setGameOver(boolean condition)
    {
        gameOverCondition = condition;
    }

    public boolean isGameOver()
    {
        return gameOverCondition;
    }

    public void setValidWord(boolean condition){
        tryWordCondition = condition;
    }

    public boolean isValidWord(){
        return tryWordCondition;
    }

    public void setGameStart(boolean condition){
        gameStarted = condition;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }
}

