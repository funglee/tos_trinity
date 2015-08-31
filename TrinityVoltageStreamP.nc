
module TrinityVoltageStreamP
{
  provides interface Atm128AdcConfig;
}
implementation
{
  async command uint8_t Atm128AdcConfig.getChannel()
  {
    // select ADC2
    return ATM128_ADC_SNGL_ADC2;
  }

  async command uint8_t Atm128AdcConfig.getRefVoltage()
  {
    return ATM128_ADC_VREF_2_56; //!< VR+ = 2.56V  and VR- = GND
  }

  async command uint8_t Atm128AdcConfig.getPrescaler()
  {
    return ATM128_ADC_PRESCALE;
  }
}
