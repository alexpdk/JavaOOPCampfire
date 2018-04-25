package objects;

import materials.Fuel;

import static fire.Physics.FireIntensity;
import static fire.Physics.celsius;
import static fire.Physics.gram;

public class FuelObject extends CampfireObject {

    protected Fuel fuel;

    public FuelObject(String name, Fuel fuel, double weight, double waterWeight, double temperature){
        super(name, fuel, weight, waterWeight, temperature);
        this.fuel = fuel;
    }
    @Override
    public void absorbWarmth(double energy) {
        energy = evaporateWith(energy);
        double initial = temperature;

        if(temperature < fuel.ignitionTemperature){
            double ignitionEnergy = toEnergy(fuel.ignitionTemperature - temperature);
            temperature += Math.min(toTemperature(energy), fuel.ignitionTemperature - temperature);
            energy -= ignitionEnergy;
        }
        // keep extra energy in increased temperature
        if(energy > 0){
            temperature += toBurningTemperature(energy);
        }
        log.debug(String.format("Object %s is heated by %s",
                name, celsius(temperature - initial)
        ));
    }
    protected double burnWeight(){
        double burntWeight = fuel.fireTemperature * FireIntensity * Math.pow(temperature / fuel.ignitionTemperature, 2);
        if(burntWeight > weight){
            burntWeight = weight;
        }
        if(temperature > fuel.ignitionTemperature){
            temperature = fuel.ignitionTemperature;
        }
        double emitted = burntWeight*getDryness()*fuel.heatingValue;
        waterWeight -= burntWeight*getWetness();
        weight -= burntWeight;
        log.debug(String.format("Object %s burnt %s of its mass",
                name, gram(burntWeight)
        ));
        return emitted;
    }
    private double toBurningTemperature(double energy){
        double fireCap = fuel.fireTemperature * FireIntensity * fuel.heatingValue * getDryness();
        double percent = Math.sqrt(1 + energy / fireCap) - 1;
        return fuel.ignitionTemperature * percent;
    }
}
