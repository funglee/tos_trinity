
/**
 * The formula to convert the read value to voltage is
 * V_{IN} = ADC*V_{REF}/1024
 *
 * @author Razvan Musaloiu-E.
 */

generic configuration TrinityVoltageC()
{
  provides interface Read<uint16_t>;
}

implementation
{
  components new AdcReadClientC(), TrinityVoltageP;

  Read = AdcReadClientC;

  AdcReadClientC.Atm128AdcConfig -> TrinityVoltageP;
}
