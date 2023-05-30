import java.sql.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author junyo
 */
public class DB_MAN {
    String strDriver ="oracle.jdbc.driver.OracleDriver";
    String strURL = "jdbc:oracle:thin:@192.168.55.76:1521:orcl"; //네트워크 
    //String strURL = "jdbc:oracle:thin:@localhost:1521:orcl"; //local
    //String strURL="jdbc:oracle:thin:@DB 202111270014_high?TNS_ADMIN=C:\\Wallet_DB202111270014";
    String strUser="SYSTEM";
    String strPWD="!dbwls159951";
    
    Connection DB_con;
    Statement DB_stmt;
    ResultSet DB_rs;
    
    public void dbOpen() throws IOException{
        try{
            Class.forName(strDriver);
            DB_con=DriverManager.getConnection(strURL,strUser,strPWD);
            DB_stmt=DB_con.createStatement();
        }catch (Exception e){
            System.out.println("open 오류 :"+e.getMessage());
        }
        
    }
    
    public void dbClose() throws IOException{
        try{
            DB_stmt.close();
            DB_con.close();
        }catch(Exception e){
            System.out.println("close 오류  :" +e.getMessage());
        }
    }
}
