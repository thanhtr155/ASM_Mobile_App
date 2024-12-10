package com.btec.fpt.campus_expense_manager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.btec.fpt.campus_expense_manager.entities.Transaction;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {
    public interface OnExpenseActionListener {
        void onEditExpense(Transaction transaction);
        void onDeleteExpense(Transaction transaction);
    }

    private final List<Transaction> transactions;
    private final OnExpenseActionListener listener;

    public ExpenseAdapter(List<Transaction> transactions, OnExpenseActionListener listener) {
        this.transactions = transactions;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expense_item, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.expenseDescription.setText(transaction.getDescription());
        holder.expenseAmount.setText(String.valueOf(transaction.getAmount()));

        holder.editButton.setOnClickListener(v -> listener.onEditExpense(transaction));
        holder.deleteButton.setOnClickListener(v -> listener.onDeleteExpense(transaction));
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView expenseDescription;
        TextView expenseAmount;
        Button editButton;
        Button deleteButton;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseDescription = itemView.findViewById(R.id.expenseDescription);
            expenseAmount = itemView.findViewById(R.id.expenseAmount);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}



