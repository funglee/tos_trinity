import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;

import java.io.FileWriter;
import java.io.PrintWriter;


import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;

import javax.tools.JavaCompiler;

import net.tinyos.message.*;
import net.tinyos.packet.*;
import net.tinyos.util.*;
import net.tinyos.message.*;
import net.tinyos.packet.*;
import net.tinyos.util.*;

import java.io.*;

public class SerialServer implements net.tinyos.message.MessageListener
{
	private MoteIF m_moteIF;
	private Connection m_connection = null;
	
	public SerialServer(MoteIF moteIF)
	{
		this.m_moteIF = moteIF;
		this.m_moteIF.registerListener(new VolReadMsg(), this);
	}
	
	private static void usage()
	{
		System.err.println("usage: SerialPort [-comm <source>]");
	}
	
	public void printVolReadMsg(VolReadMsg msg)
	{
		System.err.println("node_id : " + msg.get_node_id());
		System.err.println("data_id : " + msg.get_data_id());
		System.err.println("data_max : " + msg.get_data_max());
		System.err.println("data_min : " + msg.get_data_min());
		System.err.println("data_mean : " + msg.get_data_mean());
		System.err.println(" ");
		System.err.println(" ");
	}


	public void createVolReadTable(String table_name)
	{
		Connection connection = this.getConnection();
		Statement sql_statement;
		
		String sqlUpdate = "CREATE TABLE IF NOT EXISTS " +table_name+ " " +
							"(node_id INT(16) NOT NULL, " +
							"data_id INT(32) NULL, " +
							"data_max INT(32) NULL, " +
							"data_min INT(32) NULL, " +
							"data_mean INT(32) NULL, " +
							"date TIMESTAMP NULL, " +
							"PRIMARY KEY (data_id))";
				
		try 
		{
			sql_statement = connection.createStatement();
			sql_statement.executeUpdate(sqlUpdate);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}


	public Connection getConnection() 
	{
		if(m_connection != null){
			return m_connection;
		}
		try 
		{
			Class.forName("com.mysql.jdbc.Driver");
		} 
		catch (ClassNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 取得连接的url,能访问MySQL数据库的用户名,密码；studentinfo：数据库名
		
		String url = "jdbc:mysql://localhost:3306/lifeng_db";
		String username = "lifeng";
		String password = "lifeng123";

		// 第二步：创建与MySQL数据库的连接类的实例
		
		try 
		{
			m_connection = DriverManager.getConnection(url, username, password);
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return m_connection;
	}



	public void insertVolReadItem(String table_name, int node_id, long data_id, long data_max, long data_min, long data_mean )
	{
		Connection connection = this.getConnection();
		Statement sql_statement;

		String node_id_str = Integer.toString(node_id);
		String data_id_str = Long.toString(data_id);
		String data_max_str = Long.toString(data_max);
		String data_min_str = Long.toString(data_min);
		String data_mean_str = Long.toString(data_mean);
		
		String  dateTime = MessageFormat.format("{0,date,yyyy-MM-dd HH:mm:ss}" ,
                new Object[]  {new java.sql.Date(System.currentTimeMillis())});
		
		String sqlInsert = "INSERT INTO " + table_name +" (" +
							node_id_str + "," +
							data_id_str + "," +
							data_max_str + "," +
							data_min_str + "," +
							data_mean_str + "," +
							"'" + dateTime + "'" + ")";
		System.err.println(sqlInsert);
		try 
		{
			sql_statement = connection.createStatement();
			sql_statement.executeUpdate(sqlInsert);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	

	
	public void messageReceived(int to, Message message)
	{
		System.err.println("Receive a packet !");
		System.err.println("Type = " + message.getClass());
		System.err.println("Time = " + (new java.util.Date()));
		if(message instanceof VolReadMsg)
		{
			VolReadMsg msg  = (VolReadMsg) message;
			printVolReadMsg(msg);

			String table_name = "VolReadTable";
			createVolReadTable(table_name);
			insertVolReadItem(  table_name,
								msg.get_node_id(),
								msg.get_data_id(),
								msg.get_data_max(),
								msg.get_data_min(),
								msg.get_data_mean());			
			
			System.err.println("Updated!");
			System.err.println(" ");
			System.err.println(" ");
			
			
			
		}
		else
		{
			
			
		}
	}
	
	

	
	public static void main(String[] args) throws Exception {
		String source = null;
		if (args.length == 2) {
			if (!args[0].equals("-comm")) {
				usage();
				System.exit(1);
			}
			source = args[1];
		} else if (args.length != 0) {
			usage();
			System.exit(1);
		}

		PhoenixSource phoenix;

		if (source == null) {
			phoenix = BuildSource.makePhoenix(PrintStreamMessenger.err);
		} else {
			phoenix = BuildSource.makePhoenix(source, PrintStreamMessenger.err);
		}

		MoteIF mif = new MoteIF(phoenix);
		SerialServer serial_server = new SerialServer(mif);
		
		// release the following sentence to send ReportQueryMsg
	    //serial_server.SendReportQueryMsg();
		
		//serial_server.sendRxReportQueryMsg();
		//serial_server.SendTimeFrameInfoQueryMsg(2, 16);
		
	}
}
