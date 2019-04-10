package BackEnd;

import FrontEnd.Home;

import java.sql.*;

public class Queries
{
    public static Connection connect()
    {
        return Home.getConnection();
    }

    public static ResultSet getResultSet(String table)
    {
        String query = "select * from " + table;
        System.out.println(query);
        ResultSet rs = null;
        try
        {
            Statement stmt = connect().createStatement();
            rs = stmt.executeQuery(query);
        }
        catch (SQLException e)
        {e.printStackTrace();}

        return rs;
    }

    public static ResultSet getResultSet(String table,String columns)
    {
        String query = "select "+columns+" from " + table;
        System.out.println(query);
        ResultSet rs = null;
        try
        {
            Statement stmt = connect().createStatement();
            rs = stmt.executeQuery(query);
        }
        catch (SQLException e)
        {e.printStackTrace();}

        return rs;
    }

    public static ResultSet getResultSetAdvanced(String table, String columns, String advanced)
    {
        String query = "select "+ columns +" from " + table + " " + advanced +";";
        System.out.println(query);
        ResultSet rs = null;
        try
        {
            Statement stmt = connect().createStatement();
            rs = stmt.executeQuery(query);
        }
        catch (SQLException e)
        {e.printStackTrace();}

        return rs;
    }

    public static ResultSet getResultSetWhere(String table, String columns, String condition)
    {
        String query = "select "+ columns +" from " + table + " where " + condition +";";
        System.out.println(query);
        ResultSet rs = null;
        try
        {
            Statement stmt = connect().createStatement();
            rs = stmt.executeQuery(query);
        }
        catch (SQLException e)
        {e.printStackTrace();}

        return rs;
    }

    public static void modifyCell(String table, String column, String value, String condition)
    {
        String query = "UPDATE "+ table +" SET  "+ column + " = '" + value + "' WHERE "+ condition +";";
        System.out.println(query);
        try
        {
            Statement stmt = connect().createStatement();
            stmt.executeUpdate(query);
        }
        catch (SQLException e)
        {e.printStackTrace();}

    }

    public static void deleteRow(String table, String condition)
    {
        String query = "DELETE FROM "+table+" WHERE "+condition+";";
        System.out.println(query);
        try
        {
            Statement stmt = connect().createStatement();
            stmt.executeUpdate(query);
        }
        catch (SQLException e)
        {e.printStackTrace();}

    }

    public static void insertInto(String table, String values)
    {
        String query = "INSERT INTO "+table + " VALUES ("+values+");";
        System.out.println(query);
        try
        {
            Statement stmt = connect().createStatement();
            stmt.executeUpdate(query);
        }
        catch (SQLException e)
        {e.printStackTrace();}
    }
}
