
/**
 * The formula to convert the read value to voltage is
 * V_{IN} = ADC*V_{REF}/1024
 *
 * @author Feng Li
 */

generic configuration TrinityVoltageStreamC()
{
  provides interface ReadStream<uint16_t>;
}

implementation
{
  components new AdcReadStreamClientC(), TrinityVoltageStreamP;

  ReadStream = AdcReadStreamClientC;

  AdcReadStreamClientC.Atm128AdcConfig -> TrinityVoltageStreamP;
}
