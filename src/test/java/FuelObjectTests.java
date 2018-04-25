import materials.Fuel;
import objects.Factory;
import objects.FuelObject;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FuelObjectTests {
    @Test
    void heatPreservingTests() throws Exception{
        Factory factory = new Factory();
        double diffs[] = new double[] {1E6, 2E6, 4E6, 6E6};

        for(double diff: diffs){
            factory.setObjectTemperature(Fuel.getFuel("Wood").ignitionTemperature);
            FuelObject log1 = (FuelObject) factory.getLog(0.5, 0);
            FuelObject log2 = (FuelObject) factory.getLog(0.5, 0);

            log2.absorbWarmth(diff);

            Method burnWeight = FuelObject.class.getDeclaredMethod("burnWeight");
            burnWeight.setAccessible(true);
            double energy1 = (double) burnWeight.invoke(log1);
            double energy2 = (double) burnWeight.invoke(log2);

            assertEquals(energy2 - energy1, diff, 1.0);
            assertEquals(log2.getTemperature(), Fuel.getFuel("Wood").ignitionTemperature, 1.0);
        }
    }
}
