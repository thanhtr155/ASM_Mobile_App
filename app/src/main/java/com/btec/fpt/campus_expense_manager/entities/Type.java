package com.btec.fpt.campus_expense_manager.entities;


public class Type {
    private int id;

    private int type;

    private String email;

    public Type (int type , String email, int id){
        this.id = id;
        this.email = email;
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
