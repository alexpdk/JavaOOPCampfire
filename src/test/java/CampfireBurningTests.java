import fire.Campfire;
import objects.Factory;
import objects.Lighter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CampfireBurningTests {

    private int testDuration = 50;

    @Test
    void wetSplinterNotIgnited(){
        Campfire campfire = new Campfire();
        Factory factory = new Factory();
        campfire.addObject(factory.getLog(0.05, 0.3));

        factory.setObjectTemperature(800);
        campfire.addObject(factory.getStone(1.200));
        factory.resetObjectTemperature();

        for (int i = 0; i < testDuration; i++) campfire.nextIteration(false);
        assertTrue(campfire.getObject("Log 0") != null);
    }

    @Test
    void drySplinterNotIgnited(){
        Campfire campfire = new Campfire();
        Factory factory = new Factory();
        campfire.addObject(factory.getLog(0.05, 0.05));

        factory.setObjectTemperature(800);
        campfire.addObject(factory.getStone(1.200));
        factory.resetObjectTemperature();

        for (int i = 0; i < testDuration; i++) campfire.nextIteration(false);
        assertTrue(campfire.getObject("Log 0") != null);
    }

    @Test
    void paperCanIgniteDrySplinterAbove(){
        Campfire campfire = new Campfire();
        Factory factory = new Factory();
        campfire.addObject(factory.getPaper(0.005));
        campfire.addObject(factory.getLog(0.05, 0.05));
        campfire.addObject(factory.getLog(0.05, 0.05));

        factory.setObjectTemperature(800);
        campfire.addObject(factory.getStone(1.200));
        factory.resetObjectTemperature();

        campfire.placeUpon("Log 0", "Paper 0");
        for (int i = 0; i < testDuration; i++) campfire.nextIteration(false);

        // paper ignited from stone heat
        assertTrue(campfire.getObject("Paper 0") == null);
        assertTrue(campfire.getObject("Log 0") == null);
        assertTrue(campfire.getObject("Log 1") == null);
    }

    @Test
    void twoDrySplintersAreEnoughToIgniteThirdNearby(){
        Campfire campfire = new Campfire();
        Factory factory = new Factory();
        campfire.addObject(factory.getPaper(0.005));
        campfire.addObject(factory.getLog(0.05, 0.05));
        campfire.addObject(factory.getLog(0.05, 0.05));
        campfire.addObject(factory.getLog(0.05, 0.05));
        campfire.addObject(factory.getLog(0.2, 0.1));

        factory.setObjectTemperature(800);
        campfire.addObject(factory.getStone(1.200));
        factory.resetObjectTemperature();

        campfire.placeUpon("Log 0", "Paper 0");
        campfire.placeUpon("Log 1", "Log 0");

        for (int i = 0; i < testDuration; i++) campfire.nextIteration(false);
        // nearby splinter was ignited
        assertTrue(campfire.getObject("Log 2") == null);
        // but not enough to ignite small log
        assertTrue(campfire.getObject("Log 3") != null);
        campfire.printStats();
    }

    @Test
    void threeSplintersAreEnoughToIgniteSmallLogAbove(){
        Campfire campfire = new Campfire();
        Factory factory = new Factory();
        campfire.addObject(factory.getPaper(0.005));
        campfire.addObject(factory.getLog(0.05, 0.05));
        campfire.addObject(factory.getLog(0.05, 0.05));
        campfire.addObject(factory.getLog(0.05, 0.05));
        campfire.addObject(factory.getLog(0.1, 0.1));
        campfire.addObject(factory.getLog(0.3, 0.05));

        factory.setObjectTemperature(800);
        campfire.addObject(factory.getStone(1.200));
        factory.resetObjectTemperature();

        campfire.placeUpon("Log 0", "Paper 0");
        campfire.placeUpon("Log 1", "Log 0");
        campfire.placeUpon("Log 3", "Log 2");

        for (int i = 0; i < testDuration; i++) campfire.nextIteration(false);

        // small log burned down
        assertTrue(campfire.getObject("Log 3") == null);
        // but warmth was not enough to ignite larger dry log
        assertTrue(campfire.getObject("Log 4") != null);
        campfire.printStats();
    }

    @Test
    void aWayToIgniteLargerWetLog(){
        Campfire campfire = new Campfire();
        Factory factory = new Factory();
        campfire.addObject(factory.getPaper(0.005));
        campfire.addObject(factory.getLog(0.05, 0.05));
        campfire.addObject(factory.getLog(0.05, 0.05));
        campfire.addObject(factory.getLog(0.05, 0.05));
        campfire.addObject(factory.getLog(0.1, 0.1));
        campfire.addObject(factory.getLog(0.3, 0.3));

        factory.setObjectTemperature(800);
        campfire.addObject(factory.getStone(1.200));
        factory.resetObjectTemperature();

        campfire.placeUpon("Log 0", "Paper 0");
        campfire.placeUpon("Log 1", "Log 0");
        campfire.placeUpon("Log 3", "Log 2");
        // place larger wet log over smaller dry
        campfire.placeUpon("Log 4", "Log 3");

        for (int i = 0; i < testDuration; i++) campfire.nextIteration(false);

        assertTrue(campfire.getObject("Log 4") == null);
        campfire.printStats();
    }
}

