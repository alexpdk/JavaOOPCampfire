package materials;

public class Fuel extends Substance {
    public double ignitionTemperature;
    public double fireTemperature;
    public double heatingValue;

    Fuel(String name, double hc, double it, double ft, double hv){
        super(name, hc);
        this.ignitionTemperature = it;
        this.fireTemperature = ft;
        this.heatingValue = hv;
    }
    public static Fuel getFuel(String name){
        for(Substance s : Substances) {
            if (s.name.equals(name)) return (Fuel) s;
        }
        throw new RuntimeException("No fuel with name "+name);
    }
}
