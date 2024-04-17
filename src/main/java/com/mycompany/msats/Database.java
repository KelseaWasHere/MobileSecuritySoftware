/**
 * Make database java class to check and save data.
 */

import java.sql.*;
import java.util.ArrayList;

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
