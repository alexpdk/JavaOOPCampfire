package objects;

/**Combustible describes object which can be set on fire when reaches ignition temperature,
   or by contact with another burning object.*/
public interface Combustible extends HeatExchanging {
    double getIgnitionTemperature();
    Burning setOnFire();
}
