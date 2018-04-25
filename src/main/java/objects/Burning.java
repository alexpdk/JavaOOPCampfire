package objects;

public interface Burning extends HeatExchanging {
    double getFireTemperature();
    double getIgnitionTemperature();
    boolean hasBurnedDown();
    Combustible extinguish();
}
