package com.btec.fpt.campus_expense_manager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.btec.fpt.campus_expense_manager.database.DatabaseHelper;
import com.btec.fpt.campus_expense_manager.entities.Transaction;

public class EditExpenseActivity extends AppCompatActivity {
    private EditText descriptionEditText, amountEditText;
    private Button saveButton;
    private DatabaseHelper dbHelper;
    private int transactionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);

        descriptionEditText = findViewById(R.id.descriptionEditText);
        amountEditText = findViewById(R.id.amountEditText);
        saveButton = findViewById(R.id.saveButton);

        dbHelper = new DatabaseHelper(this);

        // Get transaction ID from intent
        transactionId = getIntent().getIntExtra("transaction_id", -1);
        if (transactionId != -1) {
            loadTransactionDetails(transactionId);
        } else {
            Toast.makeText(this, "Error loading transaction details", Toast.LENGTH_SHORT).show();
            finish();
        }

        saveButton.setOnClickListener(v -> saveTransactionDetails());
    }

    private void loadTransactionDetails(int transactionId) {
        Transaction transaction = dbHelper.getTransactionById(transactionId);
        if (transaction != null) {
            descriptionEditText.setText(transaction.getDescription());
            amountEditText.setText(String.valueOf(transaction.getAmount()));
        } else {
            Toast.makeText(this, "Transaction not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void saveTransactionDetails() {
        String description = descriptionEditText.getText().toString().trim();
        String amountStr = amountEditText.getText().toString().trim();

        if (description.isEmpty() || amountStr.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);

        boolean success = dbHelper.updateTransaction(transactionId, description, amount);
        if (success) {
            Toast.makeText(this, "Transaction updated successfully", Toast.LENGTH_SHORT).show();

            // Set result and finish activity to notify the fragment to refresh the data
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Failed to update transaction", Toast.LENGTH_SHORT).show();
        }
    }


}
