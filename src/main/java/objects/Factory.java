package objects;

import fire.Physics;
import materials.Fuel;
import materials.Substance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Factory {

    private class NamedCounter{
        String name;
        int counter = 0;

        NamedCounter(String name){
            this.name = name;
        }
        String getName(){
            return String.format("%s %d", name, counter++);
        }
    }
    private NamedCounter coalCounter = new NamedCounter("Coal");
    private NamedCounter logCounter = new NamedCounter("Log");
    private NamedCounter paperCounter = new NamedCounter("Paper");
    private NamedCounter stoneCounter = new NamedCounter("Stone");

    private double temperature = Physics.EnvironmentTemperature;

    public CampfireObject getCoal(double weight){
        return new CombustibleObject(coalCounter.getName(), Fuel.getFuel("Coal"), weight, 0, temperature);
    }
    public CampfireObject getLog(double weight, double wetness){
        return new CombustibleObject(logCounter.getName(), Fuel.getFuel("Wood"), weight, weight*wetness, temperature);
    }
    public CampfireObject getPaper(double weight){
        return new CombustibleObject(paperCounter.getName(), Fuel.getFuel("Cellulose"), weight, 0, temperature);
    }
    public CampfireObject getStone(double weight){
        return new CampfireObject(stoneCounter.getName(), Substance.getSubstance("Rock"), weight, 0, temperature);
    }
    public void resetObjectTemperature(){
        this.temperature = Physics.EnvironmentTemperature;
    }
    public void setObjectTemperature(double temperature){
        this.temperature = temperature;
    }
}
