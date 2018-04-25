package fire;

import objects.Factory;
import objects.Lighter;
import org.apache.log4j.Logger;

public class Emulation {
    private static final Logger logger = Logger.getLogger(Emulation.class);
    private static Emulation Instance = new Emulation();

    private Campfire campfire = new Campfire();
    private int iteration = 0;
    private int printFrequency;
    private int totalIterationNumber;

    private void initIgnitionFromStoneScenario(){
        Factory factory = new Factory();
        campfire.addObject(factory.getPaper(0.02));
        campfire.addObject(factory.getLog(0.060, 0.3));
        campfire.addObject(factory.getLog(0.060, 0.08)); //0.1 - not burning
        campfire.addObject(factory.getLog(0.060, 0.08));
        campfire.addObject(factory.getLog(0.1, 0.1));
        campfire.addObject(factory.getLog(0.2, 0.3));

        campfire.placeUpon("Log 1", "Paper 0");
        campfire.placeUpon("Log 0", "Log 1");
        campfire.placeUpon("Log 4", "Log 3");

        factory.setObjectTemperature(800);
        campfire.addObject(factory.getStone(1.200));
        factory.resetObjectTemperature();
    }

    private void initIgnitionFromLighterScenario() {
        Factory factory = new Factory();

        campfire.addObject(factory.getPaper(0.02));
        campfire.addObject(factory.getLog(0.05, 0.05));
        campfire.addObject(factory.getLog(0.05, 0.05));
        campfire.addObject(factory.getLog(0.05, 0.05));
        campfire.addObject(factory.getLog(0.1, 0.1));
        campfire.addObject(factory.getLog(0.2, 0.3));

        Lighter lighter = new Lighter();
        campfire.addObject(lighter);
        campfire.placeUpon(campfire.getObject("Paper 0"), lighter);

        campfire.addObject(factory.getStone(1.200));
        factory.resetObjectTemperature();

        campfire.placeUpon("Log 0", "Paper 0");
        campfire.placeUpon("Log 1", "Log 0");
        campfire.placeUpon("Log 3", "Log 2");
        // place larger wet log over smaller dry
        campfire.placeUpon("Log 4", "Log 3");
    }

    private void initTwoLogsScenario() {
        Factory factory = new Factory();

        factory.setObjectTemperature(320);
        campfire.addObject(factory.getLog(0.5, 0.05));
        factory.resetObjectTemperature();
        campfire.addObject(factory.getLog(0.5, 0.55));

        campfire.placeUpon("Log 1", "Log 0");
    }

    private void init(){
        initIgnitionFromLighterScenario();
        campfire.printStats();
    }
    private void goToIteration(int iterationNum){
        run(iterationNum, true);
    }
    private void run(){
        run(totalIterationNumber, false);
    }
    private void run(int maxIteration, boolean skipPrint){
        for(; iteration<maxIteration; iteration++){
            boolean skip = skipPrint || ((iteration % printFrequency) > 0);
            if(!skip){
                System.out.println(" ");
                logger.info(String.format("Iteration %d", iteration));
            }
            campfire.nextIteration(!skip);
            if(!skip) campfire.printStats();
        }
    }
    private void verboseRunN(int N){
        int frequency = printFrequency;
        printFrequency = 1;
        run(iteration+N, false);
        printFrequency = frequency;
    }

    public static void main(String[] args){

        Instance.totalIterationNumber = 120;
        Instance.printFrequency = 8;
        Instance.init();
        Instance.verboseRunN(12);
        //Instance.run(12, false);
        //Instance.goToIteration(40);
        //Instance.verboseRunN(10);
    }
}
