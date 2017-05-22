/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interop.beddingDiscriminator;

import interop.log.model.LogValue;
import interop.log.model.WellLog;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lucas
 */
public class WindowInfo {

    private int totalWeight;
    private ArrayList<Float> changePoints;

    public WindowInfo(List<WellLog> logs, int index, int window) {
        this.totalWeight = 0;
        this.changePoints = new ArrayList<>();

        for (int i = index; i < index + window; i++) {
            for (WellLog log : logs) {
                LogValue pair = log.getLogValues().getPair(i);
                if (log.getLogValues().size() >= i && pair.getLogValue() != 0) {
                    this.totalWeight += log.getConfiguration().getWeight();
                    changePoints.add(pair.getDepth());
                }
            }
        }
    }
    
    public int getTotalWeight() {
        return this.totalWeight;
    }
    
    public float getAvarageDepth() {
        int avarage = 0;
        for(Float f : this.changePoints)
            avarage += f;
        
        return avarage / this.changePoints.size();
    }

}
