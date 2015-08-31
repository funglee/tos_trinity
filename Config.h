#ifndef DATA_STRUCTURE_H
#define DATA_STRUCTURE_H



enum
{
	DATA_TIMER_PERIOD = 30000,
	TIME_FRAME_PERIOD = 1000,
	
	SINK_NODE_ID = 0,
	
	CHILD_SIZE = 20,
	
	
	TEST_FREQUENCY = 2475,
	DEFAULT_FREQUENCY = 2480,
	
	MAX_NODE_NUM = 20,

	TIME_FRAME_RECORD_SIZE = 30,
	
	MAX_TIME_FRAME_NUM = 30,
	
	CSMA_FLAG = 0,
	CSMA_SYNC_BACK_OFF_TIME = 20,
	NO_CSMA_SYNC_BACK_OFF_TIME = 4,
	
	ACTIVE_PERIOD = 1024*30+70, //1024*30+10,
	SLEEP_PERIOD = 1024*30,
	
	
	
	
	VOL_READ_PERIOD = 128, // in microseconds
	SEN_BUFFER_SIZE = 80,
	

	
	
};

typedef struct TimeFrameRecord
{
	uint16_t start_time_frame;
	uint16_t end_time_frame;
	uint32_t generate_data_pkt_num;
	uint32_t receive_data_pkt_num;
	uint32_t send_data_pkt_num;
} TimeFrameRecord;

typedef struct BufferList
{
	nx_uint32_t frame_id;			// # of item when receives a pkt, ++ is ok
	nx_uint16_t node_id;			// own node id
	nx_uint32_t period_counter;		// period counter when receive a pkt, each sleep period 
	nx_uint16_t packet_source;  	// packet source, where it indeed from
	nx_uint32_t packet_analysis;	// nodeid 000000 data_pkt_id
	nx_uint32_t packet_analysis_2;	// nodeid 000000 period_counter when gen this pkt
	nx_uint32_t packet_path;		// node_source 00 node_1 00 node_2 00 root 
	nx_uint32_t via_id;				// via which node it get, direct son
}BufferList;








#endif
