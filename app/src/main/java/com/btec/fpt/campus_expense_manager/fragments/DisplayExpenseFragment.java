package com.btec.fpt.campus_expense_manager.fragments;

import static com.btec.fpt.campus_expense_manager.R.*;

        import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import java.util.List;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.btec.fpt.campus_expense_manager.DataStatic;
import com.btec.fpt.campus_expense_manager.EditExpenseActivity;
import com.btec.fpt.campus_expense_manager.ExpenseAdapter;
import com.btec.fpt.campus_expense_manager.R;
import com.btec.fpt.campus_expense_manager.database.DatabaseHelper;
import com.btec.fpt.campus_expense_manager.entities.Category;
import com.btec.fpt.campus_expense_manager.entities.Transaction;
import com.btec.fpt.campus_expense_manager.models.Item;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DisplayExpenseFragment extends Fragment {
    public DisplayExpenseFragment(){
    }

    private DatabaseHelper dbHelper;
    private RecyclerView expensesRecyclerView;
    private EditText dateStart, dateEnd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_expense, container, false);
        dbHelper = new DatabaseHelper(getContext());
        expensesRecyclerView = view.findViewById(R.id.expensesRecyclerView);

        // Setup RecyclerView
        expensesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Spinner categorySpinner = view.findViewById(R.id.categorySpinner);
        dateStart = view.findViewById(R.id.dateStart);
        dateEnd = view.findViewById(R.id.dateEnd);

        dateStart.setOnClickListener(v -> showDatePickerDialogToStart());
        dateEnd.setOnClickListener(v -> showDatePickerDialogToEnd());

        ArrayList<String> categoryNames = new ArrayList<>();
        for (Category category : dbHelper.getAllCategoryByEmail(DataStatic.email)) {
            categoryNames.add(category.getName());
        }

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                categoryNames);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        Button searchButton = view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(v -> {
            String selectedCategory = categorySpinner.getSelectedItem().toString();
            String startDate = dateStart.getText().toString();
            String endDate = dateEnd.getText().toString();

            loadExpensesByCategoryAndDate(selectedCategory, startDate, endDate);
        });

        loadExpenses();
        return view;
    }

    private void loadExpenses() {
        List<Transaction> transactionList = dbHelper.getAllTransactionsByEmail(DataStatic.email);
        if (transactionList.isEmpty()) {
            Toast.makeText(getContext(), "No transactions found!", Toast.LENGTH_SHORT).show();
            return;
        }

        ExpenseAdapter adapter = new ExpenseAdapter(transactionList, new ExpenseAdapter.OnExpenseActionListener() {
            @Override
            public void onEditExpense(Transaction transaction) {
                // Redirect to edit expense page
                Intent intent = new Intent(getContext(), EditExpenseActivity.class);
                intent.putExtra("transaction_id", transaction.getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteExpense(Transaction transaction) {
                dbHelper.deleteTransaction(transaction.getId());
                loadExpenses(); // Refresh the list
            }
        });

        expensesRecyclerView.setAdapter(adapter);
    }

    private void loadExpensesByCategoryAndDate(String category, String startDate, String endDate) {
        List<Transaction> transactionList = dbHelper.getTransactionsByCategoryAndDate(DataStatic.email, category, startDate, endDate);

        if (transactionList.isEmpty()) {
            Toast.makeText(getContext(), "No transactions found for the selected category and date range.", Toast.LENGTH_SHORT).show();
            loadExpenses(); // Load all transactions if none found in the filter
            return;
        }

        // Set the adapter for RecyclerView
        ExpenseAdapter adapter = new ExpenseAdapter(transactionList, new ExpenseAdapter.OnExpenseActionListener() {
            @Override
            public void onEditExpense(Transaction transaction) {
                // Handle edit button click here
            }

            @Override
            public void onDeleteExpense(Transaction transaction) {
                dbHelper.deleteTransaction(transaction.getId());
                loadExpenses(); // Refresh the list
            }
        });
        expensesRecyclerView.setAdapter(adapter);
    }



    private void showDatePickerDialogToStart() {
        // Lấy ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
                    // Đặt ngày đã chọn vào EditText
                    String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    dateStart.setText(date);
                }, year, month, day);

        // Hiển thị dialog
        datePickerDialog.show();
    }

    private void showDatePickerDialogToEnd() {
        // Lấy ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
                    // Đặt ngày đã chọn vào EditText
                    String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    dateEnd.setText(date);
                }, year, month, day);

        // Hiển thị dialog
        datePickerDialog.show();
    }


}

