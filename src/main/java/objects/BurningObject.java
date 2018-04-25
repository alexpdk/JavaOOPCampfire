package objects;

import materials.Fuel;
import static fire.Physics.*;

public class BurningObject extends FuelObject implements Burning {

    public BurningObject(String name, Fuel fuel, double weight, double waterWeight, double temperature) {
        super(name, fuel, weight, waterWeight, temperature);
    }

    @Override
    public double emitWarmth() {
        return burnWeight() + super.emitWarmth();
    }

    @Override
    public double getFireTemperature() {
        return fuel.fireTemperature;
    }

    /**When object temperature drops below this temperature, it should stop burning*/
    @Override
    public double getIgnitionTemperature() {
        return fuel.ignitionTemperature / getDryness();
    }
    @Override
    public boolean hasBurnedDown() {
        return weight == 0;
    }

    @Override
    public Combustible extinguish() {
        return new CombustibleObject(name, fuel, weight, waterWeight, temperature);
    }
}
