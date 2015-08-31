#include "Timer.h"
#include "Config.h"
#include "VolReadMsg.h"


configuration TosTrinityDemoAppC{
}
implementation{

	
	components MainC;
  components TosTrinityDemoC as App;
  
  components SerialActiveMessageC;
  components ActiveMessageC;
  //components CC2420ActiveMessageC;
  //components CC2420PacketC;

  components new TrinityVoltageC();
  components new TrinityVoltageStreamC();
  components new VoltageC();

  components HplAtm128Timer0AsyncP;
  components McuSleepC;
  components HplAtm128GeneralIOC;

  
  components LedsC;

  components new TimerMilliC() as Timer0;
  components new TimerMilliC() as Timer1;

  
  App -> MainC.Boot;

  App.SerialControl -> SerialActiveMessageC;
  App.RadioControl -> ActiveMessageC;


  App.McuPowerOverride -> HplAtm128Timer0AsyncP;
  App.McuSleep -> McuSleepC;
  App.McuPowerState -> McuSleepC;

    
  App.RadioSend -> ActiveMessageC.AMSend;
  App.RadioReceive[AM_VOLREADMSG] -> ActiveMessageC.Receive[AM_VOLREADMSG];
  App.RadioPacket -> ActiveMessageC;
  App.RadioAMPacket -> ActiveMessageC;
    
    
  App.SerialSend -> SerialActiveMessageC;
  App.SerialReceive -> SerialActiveMessageC.Receive;
  App.SerialPacket -> SerialActiveMessageC;
  App.SerialAMPacket -> SerialActiveMessageC;
    

  App.Leds -> LedsC;
    
    
  App.SenVolRead -> TrinityVoltageC;
  App.SenVolReadStream -> TrinityVoltageStreamC;
  App.VolRead -> VoltageC;

  App.ActiveTimer -> Timer0;
  App.SleepTimer-> Timer1;


  //App.SenGenIO -> HplAtm128GeneralIOC.PortF3; // use ADC3 as GPIO output for sensing purpose
  

}
