package com.btec.fpt.campus_expense_manager.fragments;

import static com.btec.fpt.campus_expense_manager.R.*;

        import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
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

import com.btec.fpt.campus_expense_manager.DataStatic;
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
    private ListView expensesListView;
    private EditText dateStart, dateEnd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_expense, container, false);
        dbHelper = new DatabaseHelper(getContext());
        expensesListView = view.findViewById(R.id.expensesListView);
        Spinner categorySpinner = view.findViewById(R.id.categorySpinner);
        dateStart = view.findViewById(R.id.dateStart);
        dateEnd = view.findViewById(R.id.dateEnd);

        dateStart.setOnClickListener(v -> showDatePickerDialogToStart());
        dateEnd.setOnClickListener(v -> showDatePickerDialogToEnd());

        // Load categories into Spinner
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


            // Gọi phương thức lọc
            loadExpensesByCategoryAndDate(selectedCategory, startDate, endDate);
        });

        loadExpenses();


        // Ensure this return statement is outside the listener block
        return view;
    }

    private void loadExpenses() {
        List<Transaction> transactionList = dbHelper.getAllTransactionsByEmail(DataStatic.email);

        if (transactionList.isEmpty()) {
            // Debug: In thông báo nếu danh sách trống
            Toast.makeText(getContext(),"No transactions found!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Extract category names into an ArrayList
        ArrayList<String> transactionName = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            transactionName.add(transaction.getDescription());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, transactionName);
        expensesListView.setAdapter(adapter);
    }



    private void loadExpensesByCategoryAndDate(String category, String startDate, String endDate) {
        // Lấy danh sách giao dịch theo danh mục và email
        List<Transaction> transactionList = dbHelper.getTransactionsByCategoryAndDate(DataStatic.email, category, startDate, endDate);

        if (transactionList.isEmpty()) {
            // Debug: Hiển thị thông báo nếu không có giao dịch
            Toast.makeText(getContext(),"No transactions found for category: " + category + " and date range: " + startDate + " - " + endDate, Toast.LENGTH_SHORT).show();
            
            loadExpenses();
            return;
        }

        // Lấy mô tả giao dịch
        ArrayList<String> transactionDescriptions = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            transactionDescriptions.add(transaction.getDescription());
        }

        // Cập nhật ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, transactionDescriptions);
        expensesListView.setAdapter(adapter);
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

