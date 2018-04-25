package objects;

import materials.Substance;
import org.apache.log4j.Logger;
import static fire.Physics.*;


public class CampfireObject implements HeatExchanging, Named {
    protected static final Logger log = Logger.getLogger(CampfireObject.class);

    protected String name;
    private Substance substance;
    protected double weight;
    protected double waterWeight;
    protected double temperature;
    private double cachedWarmth = 0;

    CampfireObject(String name, Substance s, double w, double ww, double t){
        this.name = name;
        this.substance = s;
        this.weight = w;
        this.waterWeight = ww;
        this.temperature = t;
    }
    @Override
    public void absorbWarmth(double energy) {
        energy = evaporateWith(energy);
        temperature += toTemperature(energy);
        log.debug(String.format("Object %s is heated by %s",
                name, celsius(toTemperature(energy))
        ));
    }

    @Override
    public final void absorbCachedWarmth() {
        absorbWarmth(cachedWarmth);
        cachedWarmth = 0;
    }

    @Override
    public final void cacheWarmth(double energy) {
        cachedWarmth += energy;
    }

    @Override
    public double emitWarmth() {
        if(temperature > EnvironmentTemperature) {
            double energy = WarmthSpreadCoefficient * Math.pow(temperature - EnvironmentTemperature, 2);
            double tDiff = toTemperature(energy);
            if(tDiff > temperature - EnvironmentTemperature){
                tDiff = temperature - EnvironmentTemperature;
                energy = toEnergy(tDiff);
            }
            temperature -= tDiff;
            log.debug(String.format(
                    "Emission decreased %s temperature by %s", this, celsius(tDiff)
            ));
            return energy;
        }
        return 0;
    }
    /**
     * @param energy Total absorbed energy
     * @return unspent energy;
     */
    protected double evaporateWith(double energy){
        if(waterWeight == 0){
            return energy;
        }
        double spentEnergy = Math.min(temperature, 100) / 100 * energy;
        double evaporated = spentEnergy / WaterEvaporationHeat;
        if(evaporated > waterWeight){
            evaporated = waterWeight;
            spentEnergy = evaporated * WaterEvaporationHeat;
        }
        log.debug(String.format("Object %s spent %s of energy evaporating %s of water",
            name, percent(spentEnergy / energy * 100), gram(evaporated)
        ));
        waterWeight -= evaporated;
        weight -= evaporated;
        return energy - spentEnergy;
    }
    protected double getDryness(){
        return (weight - waterWeight) / weight;
    }
    @Override
    public double getTemperature() {
        return temperature;
    }
    public double getWetness(){
        return waterWeight / weight;
    }

    public String stringifyState() {
        return String.format("Name %s temperature %.2f°C wetness %.0f%% weight %.3f kg",
                name, temperature, waterWeight / weight * 100, weight
        );
    }
    protected double toEnergy(double temperature){
        double cap = substance.heatCapacity * (weight-waterWeight) + WaterHeatCapacity * waterWeight;
        return temperature * cap;
    }
    @Override
    public String toString() {
        return name;
    }

    protected double toTemperature(double energy){
        double cap = substance.heatCapacity * (weight-waterWeight) + WaterHeatCapacity * waterWeight;
        return energy / cap;
    }

    @Override
    public String getName() {
        return name;
    }
}
