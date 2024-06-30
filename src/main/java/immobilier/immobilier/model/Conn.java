/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package immobilier.immobilier.model;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author dm
 */
public class Conn {
        public static Connection con() throws Exception {
        String driver = "org.postgresql.Driver";
        String inGetConnection = "jdbc:postgresql://localhost:5432/immobilier";
        Class.forName(driver);

        Connection con = DriverManager.getConnection(inGetConnection, "postgres", "366325");
        con.setAutoCommit(false);

        return con;
    }
}