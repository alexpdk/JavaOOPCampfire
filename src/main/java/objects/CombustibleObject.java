package objects;

import materials.Fuel;

class CombustibleObject extends FuelObject implements Combustible {

    CombustibleObject(String name, Fuel fuel, double weight, double waterWeight, double temperature){
        super(name, fuel, weight, waterWeight, temperature);
    }
    /**When object reaches this temperature, it can burn on its own*/
    @Override
    public double getIgnitionTemperature() {
        return fuel.ignitionTemperature / getDryness();
    }
    @Override
    public Burning setOnFire() {
        return new BurningObject(name, fuel, weight, waterWeight, getTemperature());
    }
}
