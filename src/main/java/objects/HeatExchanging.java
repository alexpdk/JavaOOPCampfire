package objects;

public interface HeatExchanging {
    /**
     * Absorb warmth from nearby space. Absorbed energy increases object temperature and causes water evaporation,
     * if it has non-zero humidity.
     * @param energy Absorbed energy in joules
     */
    void absorbWarmth(double energy);
    /**
     * Object can directly obtain energy from another one below. That energy should be absorbed only
     * after object itself emitted warmth, to avoid influencing its temperature. Method causes absorbing of
     * previously cashed warmth.
     */
    void absorbCachedWarmth();
    /**
     * Object can directly obtain energy from another one below. That energy should be absorbed only
     * after object itself emitted warmth, to avoid influencing its temperature. Obtained energy is temporary cached
     * inside object, to be transformed to warmth later.
     * @param energy cashed energy
     */
    void cacheWarmth(double energy);
    /**
     * Emit some warmth to nearby space.
     * Every heated object has standard heat emanation as it gets cold,
     * and burning objects emit energy of destroyed substance.
     * @return amount of energy in joules
     */
    double emitWarmth();
    /**
     * Get current temperature of object
     * @return temperature
     */
    double getTemperature();
}
