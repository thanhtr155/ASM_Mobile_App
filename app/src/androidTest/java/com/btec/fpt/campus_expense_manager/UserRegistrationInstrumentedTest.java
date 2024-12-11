package com.btec.fpt.campus_expense_manager;

import static junit.framework.TestCase.assertFalse;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.btec.fpt.campus_expense_manager.database.DatabaseHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class UserRegistrationInstrumentedTest {

    private DatabaseHelper dbHelper;

    @Before
    public void setUp() {
        // Initialize the DatabaseHelper with the application context
        dbHelper = new DatabaseHelper(InstrumentationRegistry.getInstrumentation().getTargetContext());
    }

    // TC01: Successful user registration
    @Test
    public void userRegistration_Successful() {
        assertTrue(dbHelper.signUp("Alice", "Smith", "alice@gmail.com", "password123"));
    }

    // TC02: Registration with existing email
    @Test
    public void userRegistration_DuplicateEmail() {
        dbHelper.signUp("Bob", "Johnson", "alice@gmail.com", "password123"); // Register first
        assertFalse(dbHelper.signUp("Charlie", "Brown", "alice@gmail.com", "newpassword"));
    }

    // TC03: Registration with invalid email format
    @Test
    public void userRegistration_InvalidEmailFormat() {
        assertTrue(dbHelper.signUp("David", "Green", "invalid-email", "password456"));
    }

    // TC04: Registration with empty password
    @Test
    public void userRegistration_EmptyPassword() {
        assertTrue(dbHelper.signUp("Eve", "White", "eve@gmail.com", ""));
    }

    // TC05: Registration with short password
    @Test
    public void userRegistration_ShortPassword() {
        assertTrue(dbHelper.signUp("Frank", "Black", "frank@gmail.com", "123"));
    }
}