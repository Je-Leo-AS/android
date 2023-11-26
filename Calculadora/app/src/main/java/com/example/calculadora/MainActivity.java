package com.example.calculadora;

import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;



import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView resultTextView, expressionTextView;
    private StringBuilder input = new StringBuilder(); // Declare the 'input' variable

    private double num1;
    private double num2 = 0;

    private char operation = ' ';
    public String operationAct;
    private double result;
    private boolean newInput = true; // Flag to track if a new input is started

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the TextView by its ID
        resultTextView = findViewById(R.id.TextResultado);
        expressionTextView = findViewById(R.id.TextExpressao);

        // Other initialization code...
    }

    // Handle number button clicks
    public void onNumberButtonClick(View view) {
        Button button = (Button) view;
        String buttonText = button.getText().toString();

        if (newInput) {
            input.setLength(0); // Clear the input if a new number entry has started
            newInput = false; // Reset the flag
        }

        input.append(buttonText);

        expressionTextView.setText(input.toString());
    }

    public void onButtonDotClick(View view) {
        // Verifique se a entrada já contém um ponto decimal
        if (!input.toString().contains(".")) {
            input.append(".");
            expressionTextView.setText(input.toString());
        }
    }

    // Method to clear the input
    public void onDeleteButtonClick(View view) {
        int length = input.length();
        if (length > 0) {
            input.deleteCharAt(length - 1);
            expressionTextView.setText(input.toString()); // Atualiza o TextView com a StringBuilder atualizada
            resultTextView.setText("");
        }
    }

    public void onClearButtonClick(View view) {
        input.setLength(0); // Clear the input StringBuilder
        expressionTextView.setText(""); // Clear the TextView
        resultTextView.setText("");
    }

    public void onOperationClick(View view) {

        Button button = (Button) view;

        if (input.length() > 0) {
            num1 = Double.parseDouble(input.toString());
            input.setLength(0); // Clear the input for the next number
            operationAct = button.getText().toString();
            expressionTextView.setText(String.valueOf(operationAct));
            newInput = true; // Set the flag to indicate a new input is expected
        }
    }

    public void onEqualClick(View view) {
        if (input.length() > 0) {
            num2 = Double.parseDouble(input.toString());
            newInput = true;
        }

        performCalculation(operationAct);
        DecimalFormat decimalFormat = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));
        String formattedResult = decimalFormat.format(result);

        resultTextView.setText(formattedResult);

        input.setLength(0);
        input.append(formattedResult);
    }

    private void performCalculation(String operation) {
        switch (operation) {
            case "+":
                result = performAddition();
                break;
            case "-":
                result = performSubtraction();
                break;
            case "*":
                result = performMultiplication();
                break;
            case "/":
                result = performDivision();
                break;

            case "%":
                result = performDivisionRest();
                break;
            case "^":
                result = performElev();
                break;

            default:

                // Lidar com o caso em que uma operação desconhecida é selecionada
                break;
        }
    }

    private double performAddition() {
        // Implement addition logic here
        return num1 + num2;
    }

    private double performSubtraction() {
        // Implement subtraction logic here
        return num1 - num2;
    }

    private double performMultiplication() {
        // Implement multiplication logic here
        return num1 * num2;
    }
    private double performDivisionRest() {
        // Implement multiplication logic here
        return num1 % num2;
    }
    private double performElev() {
        // Implement addition logic here
        return Math.pow(num1, num2);

    }
    private double performDivision() {
        // Implement division logic here
        if (num2 != 0) {
            return num1 / num2;
        } else {
            // Handle division by zero
            return 0; // or show an error message
        }
    }
}