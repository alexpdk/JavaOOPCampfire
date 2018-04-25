package fire;

public class Physics {
    public static final double EnvironmentTemperature = 20;
    public static final double kJ = 1000;
    public static final double MJ = 1000000;// J in MJ

    // How fast object is burning per flame degree: 1000 degrees flame = 50g per time
    public static final double FireIntensity = 0.00005;

    // 60% of warmth emitted by flames goes in upward direction
    public static final double FireUpwardWarmthTransition = 0.6;

    // How much energy heated object is emitting per degrees^2: 100 degrees object = 50J
    public static final double WarmthSpreadCoefficient = 0.005;

    // How much of emitted energy is dispersed
    public static final double WarmthDisperseCoefficient = 0.5;

    public static final double WaterEvaporationHeat = 2.2*MJ;
    public static final double WaterHeatCapacity = 4200;

    public static String celsius(double temperature){
        return String.format("%.2f°C", temperature);
    }
    public static String gram(double weight){
        return String.format("%.2fg", weight*1000);
    }
    public static String joule(double temperature){
        return String.format("%.2fJ", temperature);
    }
    public static String kJoule(double temperature){
        return String.format("%.2fkJ", temperature / kJ);
    }
    public static String percent(double value){
        return String.format("%.0f%%", value);
    }
}
