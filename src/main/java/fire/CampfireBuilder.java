package fire;


import objects.HeatExchanging;

import java.util.List;

public interface CampfireBuilder {
    void addObject(HeatExchanging object);
    HeatExchanging getCover(HeatExchanging obj);
    HeatExchanging getCover(String name);
    HeatExchanging getObject(String name);
    List<HeatExchanging> getObjects();
    void placeUpon(HeatExchanging object, HeatExchanging below);
    void placeUpon(String name1, String name2);
    void removeObject(HeatExchanging obj);
    void removeObject(String name);
    void swapObjects(HeatExchanging obj1, HeatExchanging obj2);
    void swapObjects(String name1, String name2);
}
