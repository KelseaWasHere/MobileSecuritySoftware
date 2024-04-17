package com.mycompany.msats;

import java.sql.*;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;    

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
     * Get an arraylist of scores with the following data:
     * - Username
     * - Score
     * - Date achieved.
     * @param max
     * @return
     */
    public ArrayList<String> getRows(int max)
    {
        String q1 = "SELECT COUNT(*) FROM SCORE";
        int totalRows = 0;

        ArrayList<String> rowData = new ArrayList<>();
        try
        {
            if (stmt.executeUpdate(q1) > 0)
            {
                totalRows = stmt.getMaxRows();
                if (totalRows < max)
                {
                    max = totalRows;
                }

                String q2 = "SELECT TOP " + max + " FROM SCORE";
                ResultSet set = stmt.executeQuery(q2);

            }
            else
            {
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return rowData;
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
        String q = "INSERT INTO SCORE (USER_ID, SCORE, DATE) VALUES('" + userID + "', '" + score + "',  '" + date + "')";
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

}
