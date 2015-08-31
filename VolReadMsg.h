#ifndef VOL_READ_MSG_H
#define VOL_READ_MSG_H


typedef nx_struct VolReadMsg
{
	
	nx_uint16_t node_id;
	nx_uint32_t data_id;
	nx_uint32_t data_max;
	nx_uint32_t data_min;
	nx_uint32_t data_mean;
	

} VolReadMsg;

enum
{
	AM_VOLREADMSG = 135,
};
#endif /* DUTY_CYCLE_INFO_MSG_H */