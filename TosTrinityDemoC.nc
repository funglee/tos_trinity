#include "Timer.h"
#include "Config.h"
#include "VolReadMsg.h"


module TosTrinityDemoC {
	
	uses interface Boot;
	
	uses interface SplitControl as SerialControl;
	uses interface SplitControl as RadioControl;
	
	uses interface AMSend as RadioSend[am_id_t msg_type];
	uses interface Receive as RadioReceive[am_id_t msg_type];
	uses interface AMPacket as RadioAMPacket;
	uses interface Packet as RadioPacket;
	
	
	uses interface AMSend as SerialSend[am_id_t msg_type];
	uses interface Receive as SerialReceive[am_id_t msg_type];
	uses interface AMPacket as SerialAMPacket;
	uses interface Packet as SerialPacket;
   

	//uses interface GeneralIO as SenGenIO;

	uses interface McuPowerOverride;
    uses interface McuSleep;
    uses interface McuPowerState;
    
        
    uses interface Leds;
        
    uses interface Read<uint16_t> as SenVolRead;
    uses interface ReadStream<uint16_t> as SenVolReadStream;
    uses interface Read<uint16_t> as VolRead;



    uses interface Timer<TMilli> as SleepTimer;
	uses interface Timer<TMilli> as ActiveTimer;
    
}
implementation
{
	
  	message_t vol_read_pkt;

  	
  	bool serial_busy;
  	bool radio_busy;
  	
  	uint32_t data_id;

  	uint8_t state;

  	
  	uint16_t sen_vol;
  	uint16_t sen_buffer[SEN_BUFFER_SIZE];
	uint16_t sen_vol_max;
	uint16_t sen_vol_min;
	uint32_t sen_vol_sum;
	uint32_t sen_period;

	uint16_t sen_buffer_counter;

  	
    	
  
 	/******************* Declarations of functions and tasks **********************************/

 	task void VoltageReadTask
 	{
 		call VolRead.read();
 	}
  	
	task void SenVoltageReadTask() 
	{
		call SenVolRead.read();
	}

	task void SenVoltageReadStreamTask() 
	{
		call SenVolReadStream.postBuffer(sen_buffer, SEN_BUFFER_SIZE); 
		call SenVolReadStream.read(VOL_READ_PERIOD); 
  	}


  	event void Boot.booted() 
  	{

  		uint8_t i = 0;
		for(i=0; i<SEN_BUFFER_SIZE; i++) { sen_buffer[i]=0; }
		data_id = 1;

		state = 0;

  		
  		radio_busy = FALSE;
  		serial_busy = FALSE;


  		sen_vol = 0;
		sen_vol_max = 0;
		sen_vol_min = 10000;
		sen_buffer_counter = 0;

		call RadioControl.start();		

  	}
  	
  	
  	/********** Radio/Serial Control Events ***********/

  	event void SerialControl.startDone(error_t err) 
  	{
    	if (err == SUCCESS) 
    	{

   	 	}
    	else 
    	{
      		call SerialControl.start();
    	}
  	}

  	event void SerialControl.stopDone(error_t err) 
  	{
  		
  	
  	}

	event void RadioControl.startDone(error_t err)
	{
		if(err==SUCCESS)
		{
			if(state = 0)
			{
				uint32_t now = call SleepTimer.getNow();
				state = 1;
				call SleepTimer.startPeriodicAt(now+10-ACTIVE_PERIOD, ACTIVE_PERIOD);
				//call RadioControl.stop();
			}
			else
			{
				//post ReadStreamSenVoltageTask();
				post VolReadMsgSendTask();
			}
		}
		else
		{
			call RadioControl.start( );
		}
		
	}
  	
  	
  	event void RadioControl.stopDone(error_t error)
  	{
  		if (error == SUCCESS) 
  		{
  			atomic
	  		{
				call McuPowerState.update();
				call McuSleep.sleep();
			}
		}
		else 
		{
			call RadioControl.stop();
		}
  		
  	}
  	
  	/*************** Radio Receive ***********************/
  	
  	event message_t * RadioReceive.receive[am_id_t msg_type](message_t *msg, void *payload, uint8_t len)
  	{
  		return msg;
  	}
  	

  	event message_t * RadioSnoop.receive[am_id_t msg_type](message_t *msg, void *payload, uint8_t len)
  	{
		return msg;
  	}	
 
  	/*************** Radio Send ***********************/
  	event void RadioSend.sendDone[am_id_t msg_type](message_t* msg, error_t error)
  	{
  		switch(msg_type)
  		{
		  	case AM_VOLREADMSG:
		  		{
		  			VolReadMsg* vrm = (VolReadMsg*) call RadioPacket.getPayload(msg, sizeof(VolReadMsg));
		  			
			  		data_id++;
			  		sen_vol_max = 0;
					sen_vol_min = 1000;
					sen_vol = 0;
					sen_buffer_counter = 0;
		  		}
		  			
		  		break;
  			default:
  				break;
  		}

  		radio_busy = FALSE;
  		call RadioControl.stop();
  		
  	}
 
  
	
	/*************** Serial Receive ***************************/
	
	event message_t * SerialReceive.receive[am_id_t msg_type](message_t *msg, void *payload, uint8_t len)
	{
		return msg;
	}


	/******************* Serial Send *****************************/
	event void SerialSend.sendDone[am_id_t msg_type](message_t* msg, error_t error)
	{

	}


	


	event void ActiveTimer.fired()
	{
		int i=0;
		state = STATE_SEND;
		
		sen_vol_max = 0;
		sen_vol_min = 10000;
		sen_vol = 0;
		sen_buffer_counter = 0;


		for(i=0; i<SEN_BUFFER_SIZE; i++) sen_buffer[i]=0;

		post ReadStreamSenVoltageTask();

	}



	
	task void VolReadMsgSendTask()
	{
		if(radio_busy==FALSE)
		{
			VolReadMsg* msg = (VolReadMsg*) call RadioPacket.getPayload(&vol_read_pkt, sizeof(VolReadMsg));

			msg->node_id = TOS_NODE_ID;
			msg->data_id = data_id;
			msg->data_max = sen_vol_max;
			msg->data_min = sen_vol_min;
			msg->data_mean = sen_vol;

			
				
			call RadioPacket.setPayloadLength(&vol_read_pkt, sizeof(VolReadMsg));
			call RadioAMPacket.setSource(&vol_read_pkt, TOS_NODE_ID);
			call RadioAMPacket.setDestination(&vol_read_pkt, SINK_NODE_ID);
			call RadioAMPacket.setType(&vol_read_pkt, AM_VOLREADMSG);
			
			if(call RadioSend.send[AM_VOLREADMSG](SINK_NODE_ID, &vol_read_pkt, sizeof(VolReadMsg)))
			{
				radio_busy = TRUE;
			}
			else
			{
				post VolReadMsgSendTask();
			}
		}
	}
	
	
	
	event void VolRead.readDone(error_t result, uint16_t val)
	{		
		if(result==SUCCESS)
		{
			sen_vol = val;
		}
		else
		{
			sen_vol = 0;
		}

		call ReadioControl.start( );
	}


		

	event void SenVolRead.readDone(error_t result, uint16_t val)
	{		
		if(result==SUCCESS)
		{
			sen_vol = val;

			post VolReadMsgSendTask( );
		}
		else
		{
			sen_vol = 0;

			post SenVoltageReadTask( );
		}
	}





	event void SenVolReadStream.bufferDone(error_t result, uint16_t *buf, uint16_t count)
	{
		
		if (result == SUCCESS)
		{
			int i;

			//sen_buffer_counter = count;
			sen_buffer_counter += count;
			sen_vol_max = 0;
			sen_vol_min = 10000;
			sen_vol = 0;

			for(i=0; i<sen_buffer_counter; i++) 
			{
				sen_vol += sen_buffer[i];
				if(sen_vol_max<sen_buffer[i]) sen_vol_max = sen_buffer[i];
				if(sen_vol_min>sen_buffer[i]) sen_vol_min = sen_buffer[i];
			}

			if(sen_buffer_counter!=0) sen_vol = sen_vol/(sen_buffer_counter);
		}
		else
		{
			sen_buffer_counter = 0;
			sen_vol_max = 0;
			sen_vol_min = 10000;
			sen_vol = 0;

			post ReadStreamSenVoltageTask( );
		}
		return;
	}

	

	event void SenVolReadStream.readDone(error_t result, uint32_t usActualPeriod)
	{

		if(result==SUCCESS)
		{
			post VolReadMsgSendTask( );
		}
		else
		{
			sen_buffer_counter = 0;
			sen_vol_max = 0;
			sen_vol_min = 10000;
			sen_vol = 0;

			post ReadStreamSenVoltageTask( );
		}

		
	}


	event void SleepTimer.fired()
	{
				
		call ActiveTimer.startOneShot(TX_SLEEP_PERIOD);
		
		if(radio_busy)
		{
			call RadioSend.cancel[AM_VOLREADMSG](&vol_read_pkt);
			radio_busy = FALSE;
		}

		call RadioControl.stop();
		return ;
		
	}


	event void ActiveTimer.fired()
	{
		post VoltageReadTask();
	}
}

