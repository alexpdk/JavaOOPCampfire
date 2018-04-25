package objects;

import materials.Fuel;

public class Lighter implements HeatExchanging, Burning {

    private static final Fuel fuel = Fuel.getFuel("Propane-Butane");
    private static final double fuelConsumption = 0.001;
    private static final double flameTemperature = 700;

    public Lighter(){}

    @Override
    public void absorbWarmth(double energy) {}

    @Override
    public void absorbCachedWarmth() {}

    @Override
    public void cacheWarmth(double energy) {}

    @Override
    public double emitWarmth() {
        return fuelConsumption * fuel.heatingValue;
    }

    @Override
    public boolean hasBurnedDown() {
        // Lighter acts only one turn
        return true;
    }

    @Override
    public Combustible extinguish() {
        return null;
    }

    @Override
    public double getTemperature() {
        return flameTemperature;
    }

    @Override
    public double getFireTemperature() {
        return flameTemperature;
    }

    @Override
    public double getIgnitionTemperature() {
        return flameTemperature;
    }

    @Override
    public String toString() {
        return "Lighter";
    }
}
