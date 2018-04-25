package materials;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static fire.Physics.*;
import static java.lang.Double.NaN;

public class Substance {
    String name;
    public double heatCapacity;

    Substance(String name, double hc){
        this.name = name;
        this.heatCapacity = hc;
    }
    static List<Substance> Substances = new ArrayList<Substance>(Arrays.asList(
        new Substance("Water", 4200),
        new Fuel("Wood", 2700, 300, 1027, 21.2*MJ),
        new Substance("Rock", 1000),
        new Fuel("Coal", 1200, 700, 1400, 32.5*MJ),
        new Fuel("Cellulose", 1550, 240, 1027, 17*MJ),
        new Fuel("BirchBark", 1600, 240, 1200, 35*MJ),
        new Fuel("Propane-Butane", 400, 600, NaN, 48*MJ)
    ));
    public static Substance getSubstance(String name){
        for(Substance s : Substances) {
            if (s.name.equals(name)) return s;
        }
        throw new RuntimeException("No substance with name "+name);
    }
}
