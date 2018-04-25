package fire;

import objects.HeatExchanging;
import objects.Named;

import java.util.*;

public class CampfireConfiguration implements CampfireBuilder{
    private List<HeatExchanging> objects = new ArrayList<>();
    private Map<HeatExchanging, HeatExchanging> coveredBy = new HashMap<>();

    @Override
    public void addObject(HeatExchanging object) {
        objects.add(object);
    }

    public HeatExchanging getCover(String name){
        return getCover(getObject(name));
    }
    public HeatExchanging getCover(HeatExchanging obj){
        return coveredBy.get(obj);
    }
    @Override
    public HeatExchanging getObject(String name) {
        for(HeatExchanging obj: objects){
            if(obj instanceof Named && ((Named) obj).getName().equals(name)){
                return obj;
            }
        }
        return null;
    }
    public List<HeatExchanging> getObjects(){
        return objects;
    }
    @Override
    public void placeUpon(String name1, String name2) {
        HeatExchanging obj1 = getObject(name1);
        HeatExchanging obj2 = getObject(name2);
        if(obj1 == null || obj2 == null){
            throw new RuntimeException(String.format(
                    "Incorrect object names %s %s", name1, name2
            ));
        }
        placeUpon(obj1, obj2);
    }

    @Override
    public void removeObject(String name) {
        removeObject(getObject(name));
    }

    public void placeUpon(HeatExchanging object, HeatExchanging below) {
        if(coveredBy.get(below) != null){
            throw new RuntimeException(String.format(
                    "Object %s is covered in campfire by %s", below, coveredBy.get(below)
            ));
        }
        coveredBy.put(below, object);
    }

    public void removeObject(HeatExchanging object) {
        objects.remove(object);
        if(coveredBy.get(object) != null){
            coveredBy.remove(object);
        }
        Iterator it = coveredBy.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if(pair.getValue() == object) {
                it.remove();
            }
        }
    }

    @Override
    public void swapObjects(HeatExchanging obj1, HeatExchanging obj2) {
        HeatExchanging val1 = coveredBy.get(obj1);
        HeatExchanging val2 = coveredBy.get(obj2);
        if(val2 != null){
            coveredBy.put(obj1, val2);
        }else{
            coveredBy.remove(obj1);
        }
        if(val1 != null){
            coveredBy.put(obj2, val1);
        }else{
            coveredBy.remove(obj2);
        }
        for (Map.Entry<HeatExchanging, HeatExchanging> pair : coveredBy.entrySet()) {
            if (pair.getValue() == obj1) {
                pair.setValue(obj2);
            } else if (pair.getValue() == obj2) {
                pair.setValue(obj1);
            }
        }
    }
    @Override
    public void swapObjects(String name1, String name2) {
        swapObjects(getObject(name1), getObject(name2));
    }
}
