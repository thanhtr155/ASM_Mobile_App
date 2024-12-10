package com.btec.fpt.campus_expense_manager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.btec.fpt.campus_expense_manager.database.DatabaseHelper;
import com.btec.fpt.campus_expense_manager.entities.Category;
import com.btec.fpt.campus_expense_manager.entities.Transaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditExpenseActivity extends AppCompatActivity {
    private EditText descriptionEditText, amountEditText, dateEditText;
    private Spinner categorySpinner;
    private Button saveButton;
    private DatabaseHelper dbHelper;
    private int transactionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);

        descriptionEditText = findViewById(R.id.descriptionEditText);
        amountEditText = findViewById(R.id.amountEditText);
        dateEditText = findViewById(R.id.dateEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        saveButton = findViewById(R.id.saveButton);

        dbHelper = new DatabaseHelper(this);

        // Load categories into the spinner
        loadCategories();

        // Get transaction ID from intent
        transactionId = getIntent().getIntExtra("transaction_id", -1);
        if (transactionId != -1) {
            loadTransactionDetails(transactionId);
        } else {
            Toast.makeText(this, "Error loading transaction details", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Set date picker dialog for the date EditText
        dateEditText.setOnClickListener(v -> showDatePickerDialog());

        saveButton.setOnClickListener(v -> saveTransactionDetails());
    }

    private void loadCategories() {
        List<Category> categories = dbHelper.getAllCategoryByEmail(DataStatic.email);
        ArrayList<String> categoryNames = new ArrayList<>();
        for (Category category : categories) {
            categoryNames.add(category.getName());
        }
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categoryNames);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
    }

    private void loadTransactionDetails(int transactionId) {
        Transaction transaction = dbHelper.getTransactionById(transactionId);
        if (transaction != null) {
            descriptionEditText.setText(transaction.getDescription());
            amountEditText.setText(String.valueOf(transaction.getAmount()));
            dateEditText.setText(transaction.getDate());
            categorySpinner.setSelection(getCategoryPosition(transaction.getCategory()));
        } else {
            Toast.makeText(this, "Transaction not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private int getCategoryPosition(String categoryName) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) categorySpinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(categoryName)) {
                return i;
            }
        }
        return 0; // Default position if not found
    }

    private void showDatePickerDialog() {
        // Get current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Set selected date to EditText
                    String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    dateEditText.setText(date);
                }, year, month, day);

        // Show the dialog
        datePickerDialog.show();
    }

    private void saveTransactionDetails() {
        String description = descriptionEditText.getText().toString().trim();
        String amountStr = amountEditText.getText().toString().trim();
        String date = dateEditText.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString();

        if (description.isEmpty() || amountStr.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double amount = Double.parseDouble(amountStr);

        boolean success = dbHelper.updateTransaction(transactionId, description, amount, date, category);
        if (success) {
            Toast.makeText(this, "Transaction updated successfully", Toast.LENGTH_SHORT).show();
            finish(); // Go back to the previous screen
        } else {
            Toast.makeText(this, "Failed to update transaction", Toast.LENGTH_SHORT).show();
        }
    }
}