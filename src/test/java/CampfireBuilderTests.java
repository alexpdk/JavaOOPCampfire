import fire.CampfireBuilder;
import fire.CampfireConfiguration;
import objects.Factory;
import objects.HeatExchanging;
import objects.Named;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class CampfireBuilderTests {

    @Test
    void placeUponTests(){
        Throwable e;
        Factory factory = new Factory();
        CampfireBuilder builder = new CampfireConfiguration();
        builder.addObject(factory.getLog(0.5, 0));
        builder.addObject(factory.getLog(0.5, 0));
        builder.addObject(factory.getLog(0.5, 0));

        // Placing existing object
        builder.placeUpon("Log 1", "Log 0");
        assertTrue(((Named)builder.getCover("Log 0")).getName().equals("Log 1"));

        // Attempt to place object upon covered one causes exception
        e = assertThrows(RuntimeException.class, ()->{
            builder.placeUpon("Log 2", "Log 0");
        });
        assertTrue(e.getMessage().equals("Object Log 0 is covered in campfire by Log 1"));

        // Handling non-existent objects
        assertThrows(RuntimeException.class, ()->{
            builder.placeUpon("Log 1", "Stone 0");
        });
        assertThrows(RuntimeException.class, ()->{
            builder.placeUpon("Stone 0", "Log 1");
        });
    }

    @Test
    void removeObjectTests(){
        Factory factory = new Factory();
        CampfireBuilder builder = new CampfireConfiguration();
        builder.addObject(factory.getLog(0.5, 0));
        builder.addObject(factory.getLog(0.5, 0));
        builder.addObject(factory.getLog(0.5, 0));
        builder.placeUpon("Log 1", "Log 0");
        builder.placeUpon("Log 2", "Log 1");

        HeatExchanging log1 = builder.getObject("Log 1");
        builder.removeObject(log1);

        assertTrue(!builder.getObjects().contains(log1));
        assertTrue(builder.getCover("Log 0") == null);

        HeatExchanging belowRemoved = null;
        for(HeatExchanging obj : builder.getObjects()) if(builder.getCover(obj) == log1){
            belowRemoved = obj;
        }
        assertTrue(belowRemoved == null);
    }

    @Test
    void swapObjectsTests(){
        Factory factory = new Factory();
        CampfireBuilder builder = new CampfireConfiguration();
        builder.addObject(factory.getLog(0.5, 0));
        builder.addObject(factory.getLog(0.5, 0));
        builder.addObject(factory.getLog(0.5, 0));
        builder.placeUpon("Log 1", "Log 0");

        builder.addObject(factory.getStone(0.5));
        builder.addObject(factory.getStone(0.5));
        builder.addObject(factory.getStone(0.5));
        builder.placeUpon("Stone 2", "Stone 1");

        builder.swapObjects("Log 1", "Stone 1");

        assertTrue(builder.getCover("Log 1") == builder.getObject("Stone 2"));
        assertTrue(builder.getCover("Stone 1") == null);
        assertTrue(builder.getCover("Log 0") == builder.getObject("Stone 1"));
        assertTrue(builder.getCover("Stone 0") == null);

        builder.placeUpon("Log 2", "Stone 1");
        builder.placeUpon("Log 1", "Stone 0");

        builder.swapObjects("Log 1", "Stone 1");

        assertTrue(builder.getCover("Log 1") == builder.getObject("Log 2"));
        assertTrue(builder.getCover("Stone 1") == builder.getObject("Stone 2"));
        assertTrue(builder.getCover("Log 0") == builder.getObject("Log 1"));
        assertTrue(builder.getCover("Stone 0") == builder.getObject("Stone 1"));
    }
}
