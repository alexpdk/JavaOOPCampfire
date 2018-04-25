package fire;

import objects.*;
import org.apache.log4j.Logger;
import static fire.Physics.*;
import java.util.*;

public class Campfire implements CampfireBuilder{
    private static final Logger logger = Logger.getLogger(Campfire.class);
    private CampfireConfiguration config = new CampfireConfiguration();

    private int objectNum = 0;
    /**All the objects which can be ignited (excluding those already burning)*/
    private List<Combustible> combustibles = new ArrayList<>();
    /**Smoldering objects, recomputed and printed each iteration*/
    private List<Combustible> smoldering = new ArrayList<>();
    /**Currently burning objects*/
    private List<Burning> burning = new ArrayList<>();

    //Following three collections assist in handling the ArrayList ConcurrentModificationException

    /**List of objects ignited during iteration*/
    private List<Combustible> ignited = new ArrayList<>();
    /**List of objects which stopped burning during iteration*/
    private List<Burning> extinguished = new ArrayList<>();
    /**List of objects destroyed (or burned down) during iteration*/
    private List<HeatExchanging> destroyed = new ArrayList<>();

    public void addObject(HeatExchanging object){
        config.addObject(object);
        if(object instanceof Combustible){
            combustibles.add((Combustible) object);
        }
        else if(object instanceof Burning){
            burning.add((Burning) object);
        }
    }

    @Override
    public HeatExchanging getCover(HeatExchanging obj) {
        return config.getCover(obj);
    }
    @Override
    public HeatExchanging getCover(String name) {
        return config.getCover(name);
    }

    /**Each object emits warmth during iteration, which can be accumulated as total emitted energy, or be
     * directly transferred to the object above.
     * @param printInfo  print info about how much energy each object emitted
     * @return total emitted energy in joules*/
    private double emitWarmth(boolean printInfo){
        double totalEmitted = 0;
        for(HeatExchanging obj : config.getObjects()){
            double emitted = obj.emitWarmth();
            if(obj instanceof Burning){
                double emittedUpwards = emitted * FireUpwardWarmthTransition;
                emitted -= emittedUpwards;

                HeatExchanging objAbove = config.getCover(obj);
                if(objAbove != null){
                    objAbove.cacheWarmth(emittedUpwards);
                    if(emittedUpwards >= 10.0 && printInfo){
                        logger.info(String.format("%s emitted %s of energy to %s", obj, kJoule(emittedUpwards), objAbove));
                    }
                }else if(emittedUpwards >= 10.0 && printInfo){
                    logger.info(String.format("%s emitted %s of energy to air", obj, kJoule(emittedUpwards)));
                }
            }
            if(emitted >= 10.0 && printInfo){
                logger.info(String.format("%s emitted %s of energy", obj, kJoule(emitted)));
            }
            totalEmitted += emitted * objectNum / 10.0;
        }
        return totalEmitted;
    }
    private void extinguishObject(Burning obj, boolean print) {
        Combustible newObj = obj.extinguish();
        addObject(newObj);
        // newObj takes old object place in campfire cover-below relations
        swapObjects(newObj, obj);
        // old object can't be excluded from object list right now, but will be at the turn end
        destroyed.add(obj);
        if (print) {
            logger.info(String.format("EXTINGUISHED: %s", obj));
        }
    }
    public HeatExchanging getObject(String name){
        return config.getObject(name);
    }

    @Override
    public List<HeatExchanging> getObjects() {
        return config.getObjects();
    }

    private void igniteObject(Combustible obj, boolean print) {
        Burning newObj = obj.setOnFire();
        addObject(newObj);
        // newObj takes old object place in campfire cover-below relations
        swapObjects(newObj, obj);
        // old object can't be excluded from object list right now, but will be at the turn end
        destroyed.add(obj);
        if (print) {
            logger.info(String.format("IGNITED: %s", obj));
        }
    }

    /**
     * Run next iteration of the campfire modeling
     * @param print Print info about energy transfer inside campfire
     */
    public void nextIteration(boolean print){
        setObjectNum();
        smoldering.clear();
        // Warmth emitting
        double totalEmitted = emitWarmth(print);
        // Warmth absorbing
        double portion = totalEmitted / objectNum;

        for(HeatExchanging obj : config.getObjects()) {
            obj.absorbWarmth(portion);
            obj.absorbCachedWarmth();
        }
        updateBurningState();

        // Let ignited objects burn
        for(Combustible obj : ignited) igniteObject(obj, print);
        ignited.clear();

        // Let cold items stop burning
        for(Burning obj: extinguished) extinguishObject(obj, print);
        extinguished.clear();

        // Remove outdated objects from collections
        for(HeatExchanging obj : destroyed) removeObject(obj, print);
        destroyed.clear();

        setObjectNum();
        if(print) logger.info(String.format("Every object absorbed %s", kJoule(portion)));
    }
    @Override
    public void placeUpon(HeatExchanging object, HeatExchanging below) {
        config.placeUpon(object, below);
    }
    @Override
    public void placeUpon(String name1, String name2){
        config.placeUpon(name1, name2);
    }

    public void printStats(){
        for(HeatExchanging obj: config.getObjects()){
            String display;
            if(obj instanceof CampfireObject){
                display = ((CampfireObject) obj).stringifyState();
            }else{
                display = obj.toString();
            }
            if(config.getCover(obj) != null){
                display += " -> " + config.getCover(obj);
            }
            logger.info(display);
        }
        if(smoldering.size() > 0) {
            StringBuilder smolderingList = new StringBuilder("Smoldering: ");
            for(Combustible obj: smoldering){
                smolderingList.append(obj).append(", ");
            }
            logger.info(smolderingList.toString().substring(0, smolderingList.length() - 2));
        }
        if(burning.size() > 0) {
            StringBuilder burningList = new StringBuilder("Burning: ");
            for(Burning obj: burning){
                burningList.append(obj).append(", ");
            }
            logger.info(burningList.toString().substring(0, burningList.length() - 2));
        }
    }
    @Override
    public void removeObject(String name){
        config.removeObject(name);
    }
    @Override
    public void removeObject(HeatExchanging object){
        config.removeObject(object);
        if(object instanceof Combustible){
            combustibles.remove(object);
        }
        if(object instanceof Burning){
            burning.remove(object);
        }
    }
    private void removeObject(HeatExchanging object, boolean print){
        config.removeObject(object);
        if(object instanceof Combustible){
            combustibles.remove(object);
        }
        if(object instanceof Burning){
            if(((Burning)object).hasBurnedDown() && print){
                logger.info(String.format("BURNED DOWN: %s", object));
            }
            burning.remove(object);
        }
    }
    public void setObjectNum(){
        objectNum = getObjects().size();
    }
    @Override
    public void swapObjects(HeatExchanging obj1, HeatExchanging obj2) {
        config.swapObjects(obj1, obj2);
    }
    @Override
    public void swapObjects(String name1, String name2) {
        config.swapObjects(name1, name2);
    }

    private void updateBurningState(){
        // Check ignition of heated objects
        for(Combustible obj : combustibles){
            if(obj.getTemperature() >= obj.getIgnitionTemperature()){
                ignited.add(obj);
            }
            else if(obj.getTemperature() >= obj.getIgnitionTemperature() * 0.9){
                smoldering.add(obj);
            }
        }
        for(Burning obj : burning){
            // Check ignition by open fire
            HeatExchanging cover = getCover(obj);
            if(cover != null && cover instanceof Combustible){
                Combustible cCover = (Combustible)cover;
                //if object itself is hot enough for bright fire, it can ignite one above
                //we should also check, that object can't ignite or smolder on its own
                boolean fireSpread = cCover.getTemperature() < cCover.getIgnitionTemperature()
                                     && obj.getTemperature() >= cCover.getIgnitionTemperature();
                boolean almostSpread = cCover.getTemperature() < cCover.getIgnitionTemperature() * 0.9
                                       && obj.getTemperature() >= cCover.getIgnitionTemperature() * 0.9;
                if(fireSpread){
                    ignited.add(cCover);
                }else if(almostSpread){
                    smoldering.add(cCover);
                }
            }
            // Check destruction of burning objects
            if(obj.hasBurnedDown()){
                destroyed.add(obj);
                //Suppress fire in cold-burning objects above
                cover = getCover(obj);
                if(cover != null && cover instanceof Burning){
                    Burning cCover = (Burning)cover;
                    if(cCover.getTemperature() < cCover.getIgnitionTemperature()){
                        extinguished.add(cCover);
                    }
                }
            }
        }
    }
}
