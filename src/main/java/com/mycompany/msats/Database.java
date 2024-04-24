package com.mycompany.msats;

import java.sql.*;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;    
import java.util.Arrays;

/*
 * Make database java class to check and save data.
 */

public class Database
{

    // Database statement and connection.
    private Connection con = null;
    private Statement stmt = null;

    /**
     * Constructor for the database class.
     */
    public Database()
    {
        // Try to establish connection.
        try
        {
            con = DriverManager.getConnection("jdbc:mysql://sql5.freesqldatabase.com:3306/sql5699628", "sql5699628", "B395ypbazU");
            stmt = con.createStatement();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Close database.
     */
    public void closeDatabase()
    {
        try
        {
            con.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Get an arraylist of scores with the following data based on a userID:
     * - Username
     * - Score
     * - Date achieved.
     * @param userID
     * @return
     */
    public ArrayList<String> getRows(int userID)
    {
        ArrayList<String> rows = new ArrayList<>();
        try {
            String query = "SELECT USERS.USER_NAME, SCORE.SCORE, SCORE.DATE " +
                           "FROM USERS " +
                           "INNER JOIN SCORE ON USERS.USER_ID = SCORE.USER_ID " +
                           "WHERE USERS.USER_ID = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, userID);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String username = resultSet.getString("USER_NAME");
                int score = resultSet.getInt("SCORE");
                String date = resultSet.getString("DATE");
                String row = username + ", " + score + ", " + date;
                rows.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }
    
    /**
     * Get an arraylist of all scores with the following data:
     * - Username
     * - Score
     * - Date achieved.
     * @return
     */
    public ArrayList<String> getAllRows()
    {
        ArrayList<String> allRows = new ArrayList<>();
        try {
            String query = "SELECT USERS.USER_NAME, SCORE.SCORE, SCORE.DATE " +
                           "FROM USERS " +
                           "INNER JOIN SCORE ON USERS.USER_ID = SCORE.USER_ID";
            ResultSet resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                String username = resultSet.getString("USER_NAME");
                int score = resultSet.getInt("SCORE");
                String date = resultSet.getString("DATE");
                String row = username + ", " + score + ", " + date;
                allRows.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allRows;
    }

    /**
     * Save User to the database.
     * @param user
     * @param pass
     * @return
     */
    public boolean saveUser(String user, String pass)
    {

        String q = "INSERT INTO USERS (USER_NAME, USER_PASS) VALUES('" + user + "', '" + pass + "')";
        try
        {
            if (stmt.executeUpdate(q) > 0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Save score to the database.
     * @param userID
     * @param score
     * @return
     */
    public boolean saveScore(int userID, int score)
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"); 
        LocalDateTime date = LocalDateTime.now();
        String formattedDate = dtf.format(date);
        String q = "INSERT INTO SCORE (USER_ID, SCORE, DATE) VALUES('" + userID + "', '" + score + "',  '" + formattedDate + "')";
        try
        {
            if (stmt.executeUpdate(q) > 0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Check if the username and password for user exists.
     * @param user
     * @param pass
     * @return
     */
    public boolean checkUserAccount(String user, String pass)
    {
        try
        {
            String q = "SELECT * FROM USERS WHERE USER_NAME = '" + user + "'";
            ResultSet set = stmt.executeQuery(q);
            if (!set.next())
            {
                return false;
            }
            else
            {
                do
                {
                    if (set.getString("user_pass").equals(pass)) {
                        return true;
                    }
                    else {
                        return false;
                    }
                } while(set.next());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }
    
    /**
     * Get userID based on Username
     * @param username
     * @return
     */
    public int getUserId(String username) {
        String q = "SELECT USER_ID FROM USERS WHERE USER_NAME = ?";
    try {
        PreparedStatement stmt = con.prepareStatement(q);
        stmt.setString(1, username);
        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("USER_ID");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return -1; // Return -1 if user not found or error occurred
    }
    
    /**
     * Change the user password
     * @param userID
     * @param oldPassword
     * @param newPassword
     * @return
     */
    public boolean changePassword(int userID, String oldPassword, String newPassword)
    {
        try
    {
        String q = "SELECT * FROM USERS WHERE USER_ID = '" + userID + "' AND USER_PASS = '" + oldPassword + "'";
        ResultSet resultSet = stmt.executeQuery(q);

        if (resultSet.next()) {
            String updateQuery = "UPDATE USERS SET USER_PASS = '" + newPassword + "' WHERE USER_ID = '" + userID + "'";
            int rowsUpdated = stmt.executeUpdate(updateQuery);

            if (rowsUpdated > 0) {
                return true; // Password changed successfully
            } else {
                return false; // Password update failed
            }
        } else {
            // User does not exist or old password is incorrect
            return false;
        }
    }
    catch (Exception e)
    {
        e.printStackTrace();
    }
    return false; // Password change failed due to exception
    }

    /**
     * Check if the username and password for admin user exists.
     * @param user
     * @param pass
     * @return
     */
    public boolean checkAdminAccount(String user, String pass)
    {
        try
        {
            String q = "SELECT * FROM ADMIN WHERE ADMIN_USER_NAME = '" + user + "'";
            ResultSet set = stmt.executeQuery(q);
            if (!set.next())
            {
                return false;
            }
            else
            {
                do
                {
                    if (set.getString("admin_user_pass").equals(pass)) {
                        return true;
                    }
                    else {
                        return false;
                    }
                } while(set.next());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;

    }
    
    /**
     * Check if password is valid.
     * @param pass
     * @return 
     */
    public boolean validPassword(String pass)
    {
        // Return if password is smaller than 10
        if (pass.length() < 10)
        {
            return false;
        }
        
        // Preset values.
        boolean hasLower = false, hasUpper = true, hasDigit = false, 
                hasSpecialChar = false;
        ArrayList<Character> specialChars =
        new ArrayList<>(Arrays.asList('!', '@', '#', '$', '%', '^', '&',
                '*', '(', ')', '-', '+'));
        
        for (char i : pass.toCharArray())
        {
            if (Character.isLowerCase(i))
            {
                hasLower = true;
            }
            if (Character.isUpperCase(i))
            {
                hasUpper = true;
            }
            if (Character.isDigit(i))
            {
                hasDigit = true;
            }
            if (specialChars.contains(i))
            {
                hasSpecialChar = true;
            }
        }
        
        // Return true when password is verified.
        if (hasLower && hasUpper && hasDigit && hasSpecialChar)
        {
            return true;
        }
        
        return false;
    }

}
