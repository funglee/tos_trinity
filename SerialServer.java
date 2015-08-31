import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;

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
		this.m_moteIF.registerListener(new VolReadMsg(), this)
	}
	
	private static void usage()
	{
		System.err.println("usage: SerialPort [-comm <source>]");
	}
	
	public void PrintRadioDataMsg(RadioDataMsg msg)
	{
		System.err.println("Node ID : " + msg.get_node_id());
		System.err.println("Central Frequency : " + msg.get_frequency());
		System.err.println("DataMsg ID : " + msg.get_msg_id());
		
	}
	
	public void PrintCcaReportMsg(CcaReportMsg msg)
	{	
		System.err.println("Frame ID = " + msg.get_frame_id());
		System.err.println("Node ID = " + msg.get_node_id());
		System.err.println("CCA THR = " + msg.get_cca_thr());
		System.err.println("Data Pkt Gen Num = " + msg.get_data_pkt_gen_counter());
		System.err.println("Data Pkt Rx Num = " + msg.get_data_pkt_rx_counter());
		System.err.println("Data Pkt Tx Num = " + msg.get_data_pkt_tx_counter());
	}
	
	public void PrintInitialBackoffReportMsg(InitialBackoffReportMsg msg)
	{	
		System.err.println("Frame ID = " + msg.get_frame_id());
		System.err.println("Node ID = " + msg.get_node_id());
		System.err.println("Initial Backoff Time = " + msg.get_init_backoff());
		System.err.println("Data Pkt Gen Num = " + msg.get_data_pkt_gen_counter());
		System.err.println("Data Pkt Rx Num = " + msg.get_data_pkt_rx_counter());
		System.err.println("Data Pkt Tx Num = " + msg.get_data_pkt_tx_counter());
	}
	
	public void PrintRxReportMsg(RxReportMsg rxrm)
	{
		System.err.println("Receive "  + rxrm.get_pkt_num() + " from " + rxrm.get_node_id());
	}
	
	public void PrintCcaThrMsg(CcaThrMsg ctm)
	{
		System.err.println("Node ID = " + ctm.get_node_id());
		System.err.println("CCA Thr = " + ctm.get_cca_thr());
	}
	
	public void PrintSenseDataMsg(SenseDataMsg sdm)
	{
		System.err.println("Node ID = " + sdm.get_id());
		System.err.println("Accel X = " + sdm.get_accel_x_val());
		System.err.println("Accel Y = " + sdm.get_accel_y_val());
		System.err.println("Light = " + sdm.get_light_val());
		System.err.println("Mag X = "+ sdm.get_mag_x_val());
		System.err.println("Mag Y = " + sdm.get_mag_y_val());
		System.err.println("Mic = " + sdm.get_mic_val());
		System.err.println("Temp = " + sdm.get_temp_val());
		System.err.println("Vref = " + sdm.get_vref_val());
	}
	
	public void PrintTimeFrameInfoMsg(TimeFrameInfoMsg tfm)
	{
		
		System.err.println("Frame ID = "+ tfm.get_frame_id());
		//System.err.println("Tx/Rx Mode = " + tfm.get_type());
		System.err.println("node ID = " + tfm.get_type());
		System.err.println("Data Pkt Num = " + tfm.get_data_pkt_num());
		System.err.println("Data Pkt Rx Num = " + tfm.get_data_pkt_rx_num());
		System.err.println("Data Pkt Tx Num = " + tfm.get_data_pkt_tx_num());
	}
	
	public void PrintTestFtspMsg(TestFtspMsg tspr)
	{
        System.err.println("System Current Time (Millis) = "+System.currentTimeMillis());
		System.err.println("Src Addr = " + tspr.get_src_addr());
		System.err.println("Counter = " + tspr.get_counter());
		System.err.println("Local Rx Timestamp = " + tspr.get_local_rx_timestamp());
		System.err.println("Global Rx Timestamp = " + tspr.get_global_rx_timestamp());
		System.err.println("Skew Times (*1000000) = " + tspr.get_skew_times_1000000());
		System.err.println("Is Synced = " + tspr.get_is_synced());
		System.err.println("Ftsp Root Addr = " + tspr.get_ftsp_root_addr());
		System.err.println("Ftsp Seq = " + tspr.get_ftsp_seq());
		System.err.println("Ftsp Table Entries = " + tspr.get_ftsp_table_entries());

	}
	
	public void PrintScheduleReportMsg(ScheduleReportMsg srm)
	{
        
        System.err.println("System Current Time (Millis) = "+System.currentTimeMillis());
        
               
		System.err.println("Node id = " + srm.get_id());
		System.err.println("Frame Counter = " + srm.get_frame_counter());
		
		int type = srm.get_type();
		if(type==0) System.err.println("Frame Type = TX" );
		else System.err.println("Frame Type = RX");
		
		System.err.println("Local Time = " + srm.get_local_time());
		System.err.println("Global Time = " + srm.get_global_time());
		System.err.println("Frequency = " + srm.get_frequency());
		System.err.println("Successful Send = " + srm.get_send_success());
		System.err.println("Send Done = " + srm.get_send_done());

	}
	
	public void PrintChannelReportMsg(ChannelReportMsg msg)
	{
		System.err.println("Node ID : " + msg.get_id());
		System.err.println("Central Frequency : " + msg.get_frequency());
		System.err.println("# of generated data/report pkts : " + msg.get_pkt_counter());
		System.err.println("# of radio_receive_counter : " + msg.get_radio_receive_counter());
		System.err.println("# of radio_send_counter : " + msg.get_radio_send_counter());
		System.err.println("# of radio_drop_counter : " + msg.get_radio_drop_counter());
		System.err.println("Start Time : " + msg.get_start_time());
		System.err.println("End Time : " + msg.get_end_time());
	}
	
	public void PrintDebugMsg(DebugMsg msg)
	{
		System.err.println("Static Counter = " + msg.get_static_counter());
		System.err.println("Accel X Value = " + msg.get_accel_x_val());
		System.err.println("Accel Y Value = " + msg.get_accel_y_val());
	}
	
	public void PrintTRFrameReportMsg(TRFrameReportMsg msg)
	{
		System.err.println("Frame ID = " + msg.get_frame_id());
		System.err.println("Node ID = " + msg.get_node_id());
		System.err.println("Valid = " + msg.get_is_valid());
		System.err.println("Data Pkt Gen Num = " + msg.get_data_pkt_gen_num());
		System.err.println("Data Pkt Rx Num = " + msg.get_data_pkt_rx_num());
		System.err.println("Data Pkt Tx Num = " + msg.get_data_pkt_tx_num());
		System.err.println("Start Time = " + msg.get_start_time());
		System.err.println("End Time = " + msg.get_end_time());
	}
	
	public void PrintPrrReportMsg(PrrReportMsg msg)
	{
		System.err.println("Node ID = " + msg.get_node_id());
		System.err.println("Prr Report ID = " + msg.get_prr_report_id());
		System.err.println("Head Pkt ID = " + msg.get_head_pkt_id());
		System.err.println("Data Rx Num = " + msg.get_num_rec());
		System.err.println("CSMA Probability = " + msg.get_csma_prob());
		System.err.println(" ");
	}
	
	public void PrintCcaThrMsgToFile(CcaThrMsg msg)
	{
		long node_id = msg.get_node_id();
		int cca_thr = msg.get_cca_thr();
		
		try
		{
			FileOutputStream fout = new FileOutputStream("CcaThrMsg.txt", true);
			PrintStream out = new PrintStream(fout);
			System.setOut(out);
			
			System.out.println("node_id = " + Long.toString(node_id) + " " +
			  		   "cca_thr = " + Integer.toString(cca_thr));
		}
		catch(Exception e)
		{
			
		}
	}
	
	public void PrintPrrReportMsgToFile(PrrReportMsg msg)
	{
		long node_id = msg.get_node_id();
		long prr_report_id = msg.get_prr_report_id();
		long head_pkt_id = msg.get_head_pkt_id();
		long num_rec = msg.get_num_rec();
		long csma_prob = msg.get_csma_prob();
		
		try
		{
			FileOutputStream fout = new FileOutputStream("result.txt", true);
			PrintStream out = new PrintStream(fout);
			System.setOut(out);
		}
		catch(Exception e)
		{
			System.out.println("node_id = " + Long.toString(node_id) +
					  			"prr_report_id = " + Long.toString(prr_report_id) +
					  			"head_pkt_id = " + Long.toString(head_pkt_id) +
					  			"num_rec = " + Long.toString(num_rec) +
					  			"csma_prob = " + Long.toString(csma_prob));
		}
	}
	
	public void PrintDropReportMsg(DropReportMsg msg)
	{
		System.err.println("node_id = " + msg.get_node_id());
		System.err.println("inter_win_drop_flag = " + msg.get_inter_win_drop_flag());
		System.err.println("intra_win_drop_flag = " + msg.get_intra_win_drop_flag());
		System.err.println("first_inter_win_drop_id = " + msg.get_first_inter_win_drop_id());
		System.err.println("first_intra_win_drop_id = " + msg.get_first_intra_win_drop_id());
		System.err.println("inter_win_drop_counter = " + msg.get_inter_win_drop_counter());
		System.err.println("intra_win_drop_counter = " + msg.get_intra_win_drop_counter());
		System.err.println(" ");
	}
	
	public void PrintDutyCycleTimeMsg(DutyCycleTimeMsg msg)
	{
		System.err.println("Receive a packet from Node "+msg.get_node_id());
		System.err.println("node_id = " + msg.get_node_id());
		System.err.println("frame_id = " + msg.get_frame_id());
		System.err.println("data_pkt_counter = " + msg.get_data_pkt_counter());
		System.err.println("active_global_time = " + msg.get_active_global_time());
		System.err.println("sleep_global_time = " + msg.get_sleep_global_time());
		System.err.println("active_local_time = " + msg.get_active_local_time());
		System.err.println("sleep_local_time = " + msg.get_sleep_local_time());
		System.err.println("data_tr_local_time = " + msg.get_data_tr_local_time());
		
	}
	
	public void PrintDutyCycleInfoMsg(DutyCycleInfoMsg msg)
	{
	
		System.err.println("Receive a packet from Node "+msg.get_node_id());
		System.err.println("node_id = " + msg.get_node_id());
		System.err.println("dc_frame_id = " + msg.get_dc_frame_id());
		System.err.println("data_pkt_counter = " + msg.get_data_pkt_counter());
		System.err.println("ack_counter = " + msg.get_ack_counter());
		System.err.println("sync_frame_id = " + msg.get_sync_frame_id());
		System.err.println("route = " + msg.get_route());
		System.err.println("data = " + msg.get_data());
		
	}
	
	public void messageReceived(int to, Message message)
	{
		System.err.println("Receive a packet !");
		System.err.println("Type = " + message.getClass());
		System.err.println("Time = " + (new java.util.Date()));
		if(message instanceof ChannelReportMsg)
		{
			ChannelReportMsg msg  = (ChannelReportMsg) message;
			PrintChannelReportMsg(msg);
			
			
			UpdateNodeThroughput(  
					msg.get_id(),
					msg.get_pkt_counter(),
					msg.get_radio_receive_counter(),
					msg.get_radio_send_counter(),
					msg.get_radio_drop_counter(),
					msg.get_frequency(),
					msg.get_start_time(),
					msg.get_end_time());
					
			/*
			insertNodeThroughput( 
					report_num,
					msg.get_id(),
					msg.get_pkt_counter(),
					msg.get_radio_receive_counter(),
					msg.get_radio_send_counter(),
					msg.get_radio_drop_counter(),
					msg.get_frequency());
			*/
			
			
			System.err.println("Updated!");
			System.err.println(" ");
			System.err.println(" ");
			
			
			
		}
		else if(message instanceof TestFtspMsg)
		{
			TestFtspMsg msg = (TestFtspMsg) message;
			PrintTestFtspMsg(msg);
			System.err.println(" ");
			System.err.println(" ");
			System.err.println(" ");
		}
		else if(message instanceof ScheduleReportMsg)
		{
			ScheduleReportMsg msg = (ScheduleReportMsg) message;
			PrintScheduleReportMsg(msg);
			System.err.println(" ");
			System.err.println(" ");
			System.err.println(" ");
		}
		else if(message instanceof RxReportMsg)
		{
			RxReportMsg msg = (RxReportMsg) message;
			PrintRxReportMsg(msg);
			CreateRxReportTable( );
			UpdateRootRxReport(msg.get_node_id(), msg.get_pkt_num());
			
			System.err.println("Updated!");
			System.err.println(" ");
			System.err.println(" ");
		}
		else if(message instanceof DutyCycleTimeMsg)
		{
			DutyCycleTimeMsg msg = (DutyCycleTimeMsg) message;
			PrintDutyCycleTimeMsg(msg);
			//CreateDutyCycleTimeTable(msg.get_node_id());
			//UpdateDutyCycleTimeReport(msg.get_frame_id(), msg.get_node_id(), msg.get_data_pkt_counter(),
			//		msg.get_active_global_time(), msg.get_sleep_global_time(),
			//		msg.get_active_local_time(), msg.get_sleep_local_time(),
			//		msg.get_data_tr_local_time());
			//System.err.println("Updated!");
			System.err.println(" ");
			System.err.println(" ");
		}
		else if(message instanceof DutyCycleInfoMsg)
		{
			DutyCycleInfoMsg msg = (DutyCycleInfoMsg) message;
			PrintDutyCycleInfoMsg(msg);
			CreateDutyCycleInfoTable();
			UpdateDutyCycleInfoReport(msg.get_node_id(), msg.get_dc_frame_id(),
										msg.get_data_pkt_counter(), msg.get_ack_counter(),
										msg.get_sync_frame_id(), msg.get_route(),
										msg.get_data());
			System.err.println(" ");
			System.err.println(" ");
			
		}
		else if(message instanceof TimeFrameInfoMsg)
		{
			TimeFrameInfoMsg msg = (TimeFrameInfoMsg) message;
			PrintTimeFrameInfoMsg(msg);
			CreateTimeFrameReportTable(msg.get_type());
			UpdateTimeFrameReport(msg.get_frame_id(),
									msg.get_type(),
									msg.get_data_pkt_num(),
									msg.get_data_pkt_rx_num(),
									msg.get_data_pkt_tx_num());
			
			System.err.println("Updated!");
			System.err.println(" ");
			System.err.println(" ");
		}
		else if(message instanceof SenseDataMsg)
		{
			SenseDataMsg msg = (SenseDataMsg) message;
			PrintSenseDataMsg(msg);
			UpdateSenseDataReport(msg.get_id(), msg.get_accel_x_val(), msg.get_accel_y_val(),
									msg.get_light_val(),
									msg.get_mag_x_val(),
									msg.get_mag_y_val(),
									msg.get_mic_val(),
									msg.get_temp_val(),
									msg.get_vref_val());
			System.err.println("Updated!");
			System.err.println(" ");
			System.err.println(" ");
		}
		else if(message instanceof DebugMsg)
		{
			DebugMsg msg = (DebugMsg) message;
			PrintDebugMsg(msg);
			System.err.println(" ");
		}
		else if(message instanceof TRFrameReportMsg)
		{
			TRFrameReportMsg msg = (TRFrameReportMsg) message;
			PrintTRFrameReportMsg(msg);
			UpdateTRFrameReport(msg.get_frame_id(), msg.get_node_id(), msg.get_is_valid(),
								msg.get_data_pkt_gen_num(),
								msg.get_data_pkt_rx_num(),
								msg.get_data_pkt_tx_num(),
								msg.get_start_time(),
								msg.get_end_time());
			System.err.println(" ");
		}
		else if(message instanceof CcaThrMsg)
		{
			CcaThrMsg msg = (CcaThrMsg) message;
			PrintCcaThrMsg(msg);
			PrintCcaThrMsgToFile(msg);
			System.err.println(" ");
		}
		else if(message instanceof CcaReportMsg)
		{
			CcaReportMsg msg = (CcaReportMsg) message;
			PrintCcaReportMsg(msg);
			CreateCcaReportTable(msg.get_node_id());
			UpdateCcaReport(msg.get_frame_id(),
							msg.get_node_id(),
							msg.get_cca_thr(),
							msg.get_data_pkt_gen_counter(),
							msg.get_data_pkt_rx_counter(),
							msg.get_data_pkt_tx_counter());
			System.err.println("Updated!");
			System.err.println(" ");
			System.err.println(" ");
		}
		else if(message instanceof InitialBackoffReportMsg)
		{
			InitialBackoffReportMsg msg = (InitialBackoffReportMsg) message;
			PrintInitialBackoffReportMsg(msg);
			CreateInitialBackoffReportTable(msg.get_node_id());
			UpdateInitialBackoffReport(msg.get_frame_id(),
										msg.get_node_id(),
										msg.get_init_backoff(),
										msg.get_data_pkt_gen_counter(),
										msg.get_data_pkt_rx_counter(),
										msg.get_data_pkt_tx_counter());
			System.err.println("Updated!");
			System.err.println(" ");
			System.err.println(" ");
		}
		else if(message instanceof PrrReportMsg)
		{
			PrrReportMsg msg = (PrrReportMsg) message;
			PrintPrrReportMsg(msg);
			System.err.println(" ");
			System.err.println(" ");
			PrintPrrReportMsgToFile(msg);
			
		}
		else if(message instanceof DropReportMsg)
		{
			DropReportMsg msg = (DropReportMsg) message;
			PrintDropReportMsg(msg);
			System.err.println(" ");
			System.err.println(" ");
		}
		else if (message instanceof RadioDataMsg)
		{
			RadioDataMsg msg = (RadioDataMsg) message;
			PrintRadioDataMsg(msg);
			System.err.println(" ");
			System.err.println(" ");
		}
		else
		{
			
			
		}
	}
	
	
	
	public void insertNodeThroughput(int report_id, int node_id, long generate_pkt_num, long receive_pkt_num,
										long send_pkt_num, long drop_pkt_num, int frequency)
	{
		Connection connection = this.getConnection();
		Statement sql_statement;
		
		String report_id_str = Integer.toString(report_id);
		String node_id_str = Long.toString(node_id);
		String generate_num_str = Long.toString(generate_pkt_num);
		String receive_num_str = Long.toString(receive_pkt_num);
		String send_num_str = Long.toString(send_pkt_num);
		String drop_num_str = Long.toString(drop_pkt_num);
		String frequency_str = Integer.toString(frequency);
		
		String  dateTime = MessageFormat.format("{0,date,yyyy-MM-dd HH:mm:ss}" ,
                new Object[]  {new java.sql.Date(System.currentTimeMillis())});
		
		String sqlInsert = "INSERT INTO link_test_coloring_9 VALUES (" +
							report_id_str + "," +
							node_id_str + "," +
							generate_num_str + "," +
							receive_num_str + "," +
							send_num_str + "," +
							drop_num_str + "," +
							frequency_str + "," +
							"'" + dateTime + "'" + ")";
		System.err.println(sqlInsert);
		try 
		{
			sql_statement = connection.createStatement();
			sql_statement.executeUpdate(sqlInsert);
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
							
		
	}
	
	public void UpdateTRFrameReport(int frame_id, int node_id, int is_valid,
									long data_pkt_gen_num, long data_pkt_rx_num, long data_pkt_tx_num,
									long start_time, long end_time)
	{
		Connection connection = this.getConnection();
		Statement sql_statement;
		
		String frame_id_str = Integer.toString(frame_id);
		String node_id_str = Integer.toString(node_id);
		String is_valid_str = Integer.toString(is_valid);
		String data_pkt_gen_num_str = Long.toString(data_pkt_gen_num);
		String data_pkt_rx_num_str = Long.toString(data_pkt_rx_num);
		String data_pkt_tx_num_str = Long.toString(data_pkt_tx_num);
		String start_time_str = Long.toString(start_time);
		String end_time_str = Long.toString(end_time);
		
		String  dateTime = MessageFormat.format("{0,date,yyyy-MM-dd HH:mm:ss}" ,
		new Object[]  {new java.sql.Date(System.currentTimeMillis())});
		
		String sqlUpdate = "REPLACE INTO tr_frame_report_table" + 
							"(`frame_id`, `node_id`, `is_valid`, `data_pkt_gen_num`, `data_pkt_rx_num`, `data_pkt_tx_num`, `start_time`, `end_time`, `date`)" +
							" VALUES (" + frame_id_str + ", " + node_id_str + ", " + is_valid_str + ", " +
							data_pkt_gen_num_str + ", " +
							data_pkt_rx_num_str + ", " +
							data_pkt_tx_num_str + ", " +
							start_time_str + ", " +
							end_time_str + ", " +
							"'" + dateTime + "')";
		try 
		{
			sql_statement = connection.createStatement();
			sql_statement.executeUpdate(sqlUpdate);
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void CreateInitialBackoffReportTable(int node_id)
	{
		Connection connection = this.getConnection();
		Statement sql_statement;
		
		String node_id_str = Integer.toString(node_id);
		String sqlUpdate = "CREATE TABLE IF NOT EXISTS initial_backoff_report_table_" + node_id_str + " " +
							"(frame_id INT(32) NOT NULL, " +
							"node_id INT(32) NULL, " +
							"initial_backoff INT(32) NULL, " +
							"data_pkt_gen_num INT(32) NULL, " +
							"data_pkt_rx_num INT(32) NULL, " +
							"data_pkt_tx_num INT(32) NULL, " +
							"date TIMESTAMP NULL, " +
							"PRIMARY KEY (frame_id))";
				
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
	
	public void CreateDutyCycleTimeTable(int node_id)
	{
		Connection connection = this.getConnection();
		Statement sql_statement;
		
		String node_id_str = Integer.toString(node_id);
		String sqlUpdate = "CREATE TABLE IF NOT EXISTS duty_cycle_time_table_" + node_id_str + " " +
							"(frame_id INT NOT NULL, " +
							"node_id INT NULL, " +
							"data_pkt_counter INT(32) NULL, " +
							"active_global_time INT(32) NULL, " +
							"sleep_global_time INT(32) NULL, " +
							"active_local_time INT(32) NULL, " +
							"sleep_local_time INT(32) NULL, " +
							"data_tr_local_time INT(32) NULL, " +
							"date TIMESTAMP NULL, " +
							"PRIMARY KEY (frame_id))";
				
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
	
	public void CreateDutyCycleInfoTable()
	{
		Connection connection = this.getConnection();
		Statement sql_statement;
		
		
		String sqlUpdate = "CREATE TABLE IF NOT EXISTS duty_cycle_info_table "+
							"(ID INT(32) NOT NULL AUTO_INCREMENT, "+
							"node_id INT NOT NULL, " +
							"frame_id INT(32) NULL, " +
							"data_pkt_counter INT(32) NULL, " +
							"ack_counter INT(32) NULL, " +
							"sync_frame_id INT(32) NULL, " +
							"route INT(32) NULL, " +
							"data INT(32) NULL, " +
							"date TIMESTAMP NULL, " +
							"PRIMARY KEY (ID))";
				
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
	
	public void CreateCcaReportTable(int node_id)
	{
		Connection connection = this.getConnection();
		Statement sql_statement;
		
		String node_id_str = Integer.toString(node_id);
		String sqlUpdate = "CREATE TABLE IF NOT EXISTS cca_report_table_" + node_id_str + " " +
							"(frame_id INT(32) NOT NULL, " +
							"node_id INT(32) NULL, " +
							"cca_thr INT NULL, " +
							"data_pkt_gen_num INT(32) NULL, " +
							"data_pkt_rx_num INT(32) NULL, " +
							"data_pkt_tx_num INT(32) NULL, " +
							"date TIMESTAMP NULL, " +
							"PRIMARY KEY (frame_id))";
				
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
	
	public void CreateRxReportTable()
	{
		Connection connection = this.getConnection();
		Statement sql_statement;
		
		String sqlUpdate = "CREATE TABLE IF NOT EXISTS rx_report_table"+ " " +
							"(node_id INT(32) NOT NULL, " +
							"receive_pkt_num INT(32) NULL, " +
							"date TIMESTAMP NULL, " +
							"PRIMARY KEY (node_id))";
				
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
	
	public void UpdateDutyCycleTimeReport(int frame_id, int node_id, long data_pkt_counter,
											long active_global_time, long sleep_global_time,
											long active_local_time, long sleep_local_time,
											long data_tr_local_time)
	{

		Connection connection = this.getConnection();
		Statement sql_statement;
		
	
		String frame_id_str = Integer.toString(frame_id);
		String node_id_str = Integer.toString(node_id);
		String data_pkt_counter_str = Long.toString(data_pkt_counter);
		String active_global_time_str = Long.toString(active_global_time);
		String sleep_global_time_str = Long.toString(sleep_global_time);
		String active_local_time_str = Long.toString(active_local_time);
		String sleep_local_time_str = Long.toString(sleep_local_time);
		String data_tr_local_time_str = Long.toString(data_tr_local_time);
		
		String  dateTime = MessageFormat.format("{0,date,yyyy-MM-dd HH:mm:ss}" ,
		new Object[]  {new java.sql.Date(System.currentTimeMillis())});
		
		String sqlUpdate = "REPLACE INTO duty_cycle_time_table_" + node_id_str + " " + 
				"(`frame_id`, `node_id`, `data_pkt_counter`, `active_global_time`, `sleep_global_time`, `active_local_time`, `sleep_local_time`, `data_tr_local_time`, `date`)" +
				" VALUES (" + frame_id_str + ", " + node_id_str + ", " + 
				data_pkt_counter_str + ", " +
				active_global_time_str + ", " +
				sleep_global_time_str + ", " +
				active_local_time_str + ", " +
				sleep_local_time_str + ", " +
				data_tr_local_time_str + ", " +
				"'" + dateTime + "')";
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
	
	public void UpdateDutyCycleInfoReport(int node_id, long dc_frame_id, long data_pkt_counter, 
											long ack_counter, long sync_frame_id, long route, long data)
	{
		Connection connection = this.getConnection();
		Statement sql_statement;
		
		double thr = (double)10000*(1023-route)/route;
		double vol = (double)data/1000.0;
		double temp = 0.00130705+0.000214381*Math.log(thr)+0.000000093*Math.pow(Math.log(thr),3);
		temp = 1/temp;
		
		String node_id_str = Integer.toString(node_id);
		String dc_frame_id_str = Long.toString(dc_frame_id);
		String data_pkt_counter_str = Long.toString(data_pkt_counter);
		String ack_counter_str = Long.toString(ack_counter);
		String sync_frame_id_str = Long.toString(sync_frame_id);
		String route_str = Long.toString(route); //Double.toString(temp-273.15);//
		String data_str = Double.toString(vol);//Long.toString(data);
	
		
		String  dateTime = MessageFormat.format("{0,date,yyyy-MM-dd HH:mm:ss}" ,
		new Object[]  {new java.sql.Date(System.currentTimeMillis())});
		
		String sqlUpdate = "INSERT INTO duty_cycle_info_table"+ " " + 
		"(`node_id`, `frame_id`, `data_pkt_counter`, `ack_counter`, `sync_frame_id`, `route`, `data`, `date`)" +
		" VALUES (" + node_id_str + ", " + dc_frame_id_str + ", " + 
		data_pkt_counter_str + ", " +
		ack_counter_str + ", " +
		sync_frame_id_str + ", " +
		route_str + ", " +
		data_str + ", " +
		"'" + dateTime + "')";
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
	
	public void UpdateCcaReport(long frame_id, int node_id, int cca_thr, long data_pkt_gen_num, 
								long data_pkt_rx_num, long data_pkt_tx_num)
	{
		Connection connection = this.getConnection();
		Statement sql_statement;
		
		String frame_id_str = Long.toString(frame_id);
		String node_id_str = Integer.toString(node_id);
		String cca_thr_str = Integer.toString(cca_thr);
		String data_pkt_gen_num_str = Long.toString(data_pkt_gen_num);
		String data_pkt_rx_num_str = Long.toString(data_pkt_rx_num);
		String data_pkt_tx_num_str = Long.toString(data_pkt_tx_num);
		
		String  dateTime = MessageFormat.format("{0,date,yyyy-MM-dd HH:mm:ss}" ,
		new Object[]  {new java.sql.Date(System.currentTimeMillis())});
		
		String sqlUpdate = "REPLACE INTO cca_report_table_" + node_id_str + " " + 
							"(`frame_id`, `node_id`, `cca_thr`, `data_pkt_gen_num`, `data_pkt_rx_num`, `data_pkt_tx_num`, `date`)" +
							" VALUES (" + frame_id_str + ", " + node_id_str + ", " + cca_thr_str + ", " +
							data_pkt_gen_num_str + ", " +
							data_pkt_rx_num_str + ", " +
							data_pkt_tx_num_str + ", " +
							"'" + dateTime + "')";
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
	
	public void UpdateInitialBackoffReport(long frame_id, int node_id, int initial_backoff, 
											long data_pkt_gen_num, long data_pkt_rx_num, 
											long data_pkt_tx_num)
	{
		Connection connection = this.getConnection();
		Statement sql_statement;
		
		String frame_id_str = Long.toString(frame_id);
		String node_id_str = Integer.toString(node_id);
		String initial_backoff_str = Integer.toString(initial_backoff);
		String data_pkt_gen_num_str = Long.toString(data_pkt_gen_num);
		String data_pkt_rx_num_str = Long.toString(data_pkt_rx_num);
		String data_pkt_tx_num_str = Long.toString(data_pkt_tx_num);
		
		String  dateTime = MessageFormat.format("{0,date,yyyy-MM-dd HH:mm:ss}" ,
		new Object[]  {new java.sql.Date(System.currentTimeMillis())});
		
		String sqlUpdate = "REPLACE INTO initial_backoff_report_table_" + node_id_str + " " + 
				"(`frame_id`, `node_id`, `initial_backoff`, `data_pkt_gen_num`, `data_pkt_rx_num`, `data_pkt_tx_num`, `date`)" +
				" VALUES (" + frame_id_str + ", " + node_id_str + ", " + initial_backoff_str + ", " +
				data_pkt_gen_num_str + ", " +
				data_pkt_rx_num_str + ", " +
				data_pkt_tx_num_str + ", " +
				"'" + dateTime + "')";
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
	
	public void UpdateSenseDataReport(int node_id, int accel_x_val, int accel_y_val,
										int light_val, int mag_x_val, int mag_y_val,
										int mic_val, int temp_val, int vref_val)
	{
		Connection connection = this.getConnection();
		Statement sql_statement;
		
		String node_id_str = Integer.toString(node_id);
		String accel_y_str = Integer.toString(accel_x_val);
		String accel_x_str = Integer.toString(accel_y_val);
		String light_str = Integer.toString(light_val);
		String mag_x_str = Integer.toString(mag_x_val);
		String mag_y_str = Integer.toString(mag_y_val);
		String mic_str = Integer.toString(mic_val);
		String temp_str = Integer.toString(temp_val);
		String vref_str = Integer.toString(vref_val);
		
		
		String  dateTime = MessageFormat.format("{0,date,yyyy-MM-dd HH:mm:ss}" ,
		new Object[]  {new java.sql.Date(System.currentTimeMillis())});
		
		/*
		String sqlUpdate = "UPDATE TimeFrameReport SET " + 
		"type=" + type_str + "," +
		"data_pkt_num=" + data_pkt_num_str + "," +
		"data_pkt_rx_num=" + data_pkt_rx_num_str + "," +
		"data_pkt_tx_num=" + data_pkt_tx_num_str + "," +
		"date='" + dateTime + "' " +
		"WHERE "+"frame_id=" + frame_id_str;
		*/
		
		String sqlUpdate = "REPLACE INTO sense_data_table" + 
				" (`node_id`,`accel_x_val`,`accel_y_val`,`light_val`,`mag_x_val`,`mag_y_val`,`mic_val`,`temp_val`,`vref_val`,`date`)" +
				" values (" + node_id_str + "," +
				accel_x_str + ", " +
				accel_y_str + ", " +
				light_str + ", " +
				mag_x_str + ", " +
				mag_y_str + ", " +
				mic_str + ", " +
				temp_str + ", " +
				vref_str + ", " +
				"'" + dateTime + "')";

		
		try 
		{
			sql_statement = connection.createStatement();
			sql_statement.executeUpdate(sqlUpdate);
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void CreateTimeFrameReportTable(int node_id)
	{
		Connection connection = this.getConnection();
		Statement sql_statement;
		
		String node_id_str = Integer.toString(node_id);
		String sqlUpdate = "CREATE TABLE IF NOT EXISTS time_frame_report_table_" + node_id_str + " " +
							"(frame_id INT(11) NOT NULL, " +
							"node_id INT(11) NULL, " +
							"data_pkt_gen_num INT(32) NULL, " +
							"data_pkt_rx_num INT(32) NULL, " +
							"data_pkt_tx_num INT(32) NULL, " +
							"date TIMESTAMP NULL, " +
							"PRIMARY KEY (frame_id))";
		
		
				
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

	
	public void UpdateTimeFrameReport(int frame_id, int type, 
										long data_pkt_num,
										long data_pkt_rx_num,
										long data_pkt_tx_num )
	{
		Connection connection = this.getConnection();
		Statement sql_statement;
		
		String frame_id_str = Long.toString(frame_id);
		String type_str = Long.toString(type);
		String data_pkt_num_str = Long.toString(data_pkt_num);
		String data_pkt_rx_num_str = Long.toString(data_pkt_rx_num);
		String data_pkt_tx_num_str = Long.toString(data_pkt_tx_num);
		
		String  dateTime = MessageFormat.format("{0,date,yyyy-MM-dd HH:mm:ss}" ,
		new Object[]  {new java.sql.Date(System.currentTimeMillis())});
		
		
		String sqlUpdate = "REPLACE INTO time_frame_report_table_" + type_str + 
				" (`frame_id`,`node_id`,`data_pkt_gen_num`,`data_pkt_rx_num`,`data_pkt_tx_num`,`date`)" +
				" values (" + frame_id_str + ", " +
				type_str + ", " +
				data_pkt_num_str + ", " +
				data_pkt_rx_num_str + ", " +
				data_pkt_tx_num_str + ", " +
				"'" + dateTime + "')";
		
		/*
		String sqlUpdate = "REPLACE INTO time_frame_report_table" + 
				" (`frame_id`,`type`,`data_pkt_gen_num`,`data_pkt_rx_num`,`data_pkt_tx_num`,`date`)" +
				" values (" + frame_id_str + ", " +
				type_str + ", " +
				data_pkt_num_str + ", " +
				data_pkt_rx_num_str + ", " +
				data_pkt_tx_num_str + ", " +
				"'" + dateTime + "')";
		*/
		try 
		{
			sql_statement = connection.createStatement();
			sql_statement.executeUpdate(sqlUpdate);
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void UpdateRootRxReport(int node_id, long pkt_num) 
	{
		Connection connection = this.getConnection();
		Statement sql_statement;
		
		String node_id_str = Long.toString(node_id);
		String pkt_num_str = Long.toString(pkt_num);
		
		String  dateTime = MessageFormat.format("{0,date,yyyy-MM-dd HH:mm:ss}" ,
		new Object[]  {new java.sql.Date(System.currentTimeMillis())});
		
		
		String sqlUpdate = "UPDATE rx_report_table SET " + 
		"receive_pkt_num=" + pkt_num_str + ", " +
		"date='" + dateTime + "' " +
		"WHERE "+"node_id=" + node_id_str;
		
		try 
		{
			sql_statement = connection.createStatement();
			sql_statement.executeUpdate(sqlUpdate);
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void UpdateNodeThroughput(int node_id, long generate_pkt_num, long receive_pkt_num, 
										long send_pkt_num, long drop_pkt_num, int frequency, long start_time,
										long end_time) 
	{
		Connection connection = this.getConnection();
		Statement sql_statement;
		
		String node_id_str = Long.toString(node_id);
		String generate_num_str = Long.toString(generate_pkt_num);
		String receive_num_str = Long.toString(receive_pkt_num);
		String send_num_str = Long.toString(send_pkt_num);
		String drop_num_str = Long.toString(drop_pkt_num);
		String frequency_str = Integer.toString(frequency);
		String start_time_str = Long.toString(start_time);
		String end_time_str = Long.toString(end_time);
		
		String  dateTime = MessageFormat.format("{0,date,yyyy-MM-dd HH:mm:ss}" ,
                new Object[]  {new java.sql.Date(System.currentTimeMillis())});
	
		String sqlUpdate = "REPLACE INTO SensorNode" + " " + 
				"(`node_id`, `generate_pkt_num`, `receive_pkt_num`, `send_pkt_num`, `drop_pkt_num`, `central_frequency`," +
				"`start_time`, `end_time`, `date`)" +
				" VALUES (" + node_id_str + ", " +
				generate_num_str + ", " +
				receive_num_str + ", " +
				send_num_str + ", " +
				drop_num_str + ", " +
				frequency_str + ", " +
				"'" + dateTime + "')";
				//start_time_str + ", " +
				//end_time_str + ", " +
				//"'" + dateTime + "')";
		 sqlUpdate = "REPLACE INTO SensorNode" + " " + 
				"(`node_id`, `generate_pkt_num`, `receive_pkt_num`, `send_pkt_num`, `drop_pkt_num`, `central_frequency`," +
				"`date`)" +
				" VALUES (" + node_id_str + ", " +
				generate_num_str + ", " +
				receive_num_str + ", " +
				send_num_str + ", " +
				drop_num_str + ", " +
				frequency_str + ", " +
				"'" + dateTime + "')";
		
		try 
		{
			sql_statement = connection.createStatement();
			sql_statement.executeUpdate(sqlUpdate);
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
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
		
		String url = "jdbc:mysql://155.69.146.70:3306/lifeng_db";
		String username = "lifeng";
		String password = "lifeng";

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
	
	public void SendRxReportQueryMsg() 
	{
		RxReportQueryMsg rrqm = new RxReportQueryMsg();
	    
	    try 
	    {
	    	 System.out.println("Sending RxReportQueryMsg");
	    	 rrqm.set_node_id(0);
	    	 m_moteIF.send(1, rrqm);

	    }
	    catch (IOException exception) 
	    {
	      System.err.println("Exception thrown when sending packets. Exiting.");
	      System.err.println(exception);
	    }
	}
	
	public void SendTimeFrameInfoQueryMsg(int node_id, int time_frame_id) 
	{
		TimeFrameInfoQueryMsg tfqm = new TimeFrameInfoQueryMsg();
	    
	    try 
	    {
	    	 System.out.println("Sending TimeFrameInfoQueryMsg");
	    	 tfqm.set_frame_id(time_frame_id);
	    	 m_moteIF.send(node_id, tfqm);
	    }
	    catch (IOException exception) 
	    {
	      System.err.println("Exception thrown when sending packets. Exiting.");
	      System.err.println(exception);
	    }
	}
	
	public void SendDropReportQueryMsg()
	{
		 DropReportQueryMsg drm = new DropReportQueryMsg();
		 try 
		 {
		    System.out.println("SendingDropReportQueryMsg");
		    drm.set_id(0);
		    drm.set_type((short)1);
		    
		    m_moteIF.send(0xFF, drm);
		 }
		 catch (IOException exception) 
		 {
		    System.err.println("Exception thrown when sending packets. Exiting.");
		    System.err.println(exception);
		 }
	}
	
	public void SendReportQueryMsg()
	{
		ReportQueryMsg rqm = new ReportQueryMsg();
		
		 try 
		 {
		    System.out.println("Sending TimeFrameInfoQueryMsg");
		    rqm.set_id(0);
		 	rqm.set_type((short)1);
		    m_moteIF.send(0xFF, rqm);
		 }
		 catch (IOException exception) 
		 {
		    System.err.println("Exception thrown when sending packets. Exiting.");
		    System.err.println(exception);
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
