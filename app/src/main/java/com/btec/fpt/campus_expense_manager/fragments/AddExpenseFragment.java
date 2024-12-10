package com.btec.fpt.campus_expense_manager.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.btec.fpt.campus_expense_manager.DataStatic;
import com.btec.fpt.campus_expense_manager.R;
import com.btec.fpt.campus_expense_manager.database.DatabaseHelper;
import com.btec.fpt.campus_expense_manager.entities.Category;
import com.btec.fpt.campus_expense_manager.entities.Transaction;
import com.btec.fpt.campus_expense_manager.models.BalanceInfor;

import java.util.ArrayList;
import java.util.Calendar;

public class AddExpenseFragment extends Fragment {


    private DatabaseHelper dbHelper;
    private EditText amountEditText, descriptionEditText, dateEditText;


    public AddExpenseFragment(){

    }
    static  String categoryString = "Food";
    static Integer typeString = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_add_expense, container, false);

        dbHelper = new DatabaseHelper(getContext());

        amountEditText = view.findViewById(R.id.amountEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        dateEditText = view.findViewById(R.id.dateEditText);
        dateEditText.setOnClickListener(v -> showDatePickerDialog());



        // Reference to the Spinner in the layout
        Spinner spinner = view.findViewById(R.id.spinner);


        // Extract category names into an ArrayList
        ArrayList<String> categoryNames = new ArrayList<>();
        for (Category category : dbHelper.getAllCategoryByEmail(DataStatic.email)) {
            categoryNames.add(category.getName());
        }

        ArrayList<Integer> transactionNames = new ArrayList<>();
        for (Transaction transaction : dbHelper.getAllTransactionsByEmail(DataStatic.email)){
            transactionNames.add(transaction.getType());
        }



        // Convert ArrayList to an array if required
        String[] categoryNameArray = categoryNames.toArray(new String[0]);
        Integer[] transactionNameArray = transactionNames.toArray(new Integer[0]);


        // Data for the Spinner
       // String[] categories = {"Food", "Transport", "Entertainment", "Health", "Education"};

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                categoryNameArray
        );

        ArrayAdapter<Integer> adapterTypes = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                transactionNameArray
        );

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterTypes.setDropDownViewResource((android.R.layout.simple_spinner_item));
        adapterTypes.notifyDataSetChanged();

        // Set the adapter to the Spinner
        spinner.setAdapter(adapter);


        // Set a listener for when an item is selected
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = parent.getItemAtPosition(position).toString();
                categoryString = selectedCategory;

                Toast.makeText(getContext(), "Selected: " + selectedCategory, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optional: Handle case when nothing is selected
            }
        });

        Button addButton = view.findViewById(R.id.addButton);

        Button btnDisplay = view.findViewById(R.id.btnDisplay);

        btnDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadFragment(new DisplayExpenseFragment());


            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExpense();
            }
        });
        return view;
    }



    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void addExpense() {
        double amount = Double.parseDouble(amountEditText.getText().toString());
        String description = descriptionEditText.getText().toString();
        String date = dateEditText.getText().toString();

        RadioButton checkboxIncome = getView().findViewById(R.id.checkboxIncome);
        int type = checkboxIncome.isChecked() ? 1 : 0; // 1 for Income, 0 for Expense

        double currentBalance = dbHelper.getBalanceFromEmail(DataStatic.email).getBalance();

        // Check if expense exceeds the current balance
        if (type == 0 && amount > currentBalance) {
            Toast.makeText(getContext(), "Over budget! Expense not added.", Toast.LENGTH_SHORT).show();
            return; // Exit the method without adding the expense
        }

        boolean inserted = dbHelper.insertTransaction(amount, description, date, type, DataStatic.email, categoryString);
        double updatedBalance = dbHelper.getBalanceFromEmail(DataStatic.email).getBalance();

        if (inserted) {
            Toast.makeText(getContext(), "Transaction added", Toast.LENGTH_SHORT).show();
            amountEditText.setText("");
            descriptionEditText.setText("");
            dateEditText.setText("");
        } else {
            Toast.makeText(getContext(), "Error adding transaction", Toast.LENGTH_SHORT).show();
        }
    }


    private void showDatePickerDialog() {
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
                    dateEditText.setText(date);
                }, year, month, day);

        // Hiển thị dialog
        datePickerDialog.show();
    }
}


