/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interop.beddingDiscriminator;

import interop.log.model.WellLog;
import interop.log.model.LogValues;
import interop.movingMeans.MovingMeans;

import java.util.List;

/**
 *
 * @author Cauã Antunes
 */
public class BeddingDiscriminator {
    
    // Input: list of pairs (depth, log value)
    // Output: list of pairs (depth, x),
    //        where x equals 0 if no break is found at the appointed depth
    //        or a value near to 1 otherwise
    public static LogValues findChangePoints(LogValues logList, float shortWindowSize, float longWindowSize){
        LogValues result = new LogValues();
        
        // Computes the distance between two log entries
        float step = logList.getPair(1).getDepth() - logList.getPair(0).getDepth();
        // Apply the moving means with the adequate windows
        LogValues shortWindow = MovingMeans.apply(logList, (int) (shortWindowSize / step));
        LogValues longWindow = MovingMeans.apply(logList, (int) (longWindowSize / step));
        
        LogValues under;
        LogValues over;
        int i = 0;
        
        // Iterates over both lists until a difference is found, so we can start finding change points.
        while(i < logList.size() && shortWindow.getPair(i).getLogValue() == longWindow.getPair(i).getLogValue()){
            result.put(logList.getPair(i).getDepth(), 0F);
            i++;
        }
        
        // Stores the list with the lower value in 'under', and the one with the higher in 'over'
        if(shortWindow.getPair(i).getLogValue() < longWindow.getPair(i).getLogValue()){
            under = shortWindow;
            over = longWindow;
        } else {
            under = longWindow;
            over = shortWindow;
        }
        
        // Iterates over the remaining entries
        while(i < logList.size()){
            // Whenever the log value in 'under' is greater than the one in 'over',
            // swaps 'over' and 'under' and signals the break
            if(over.getPair(i).getLogValue() < under.getPair(i).getLogValue()){
                float deltaThen = over.getPair(i - 1).getLogValue() - under.getPair(i - 1).getLogValue();
                float deltaNow = under.getPair(i - 1).getLogValue() - over.getPair(i - 1).getLogValue();
                result.put(logList.getPair(i).getDepth(), 1 + deltaThen/(deltaThen + deltaNow));
                
                LogValues aux = over;
                over = under;
                under = aux;
            } else {
                result.put(logList.getPair(i).getDepth(), 0f);
            }
            i++;
        }
        
        return result;
    }
    
    /**
     * This function finds all Change Points from WellLogs in a list
     * @param logs WellLogs to find change points
     */
    public static void findChangePoints(List<WellLog> logs) {
        for(WellLog log : logs)
            log.getChangePoints();
    }
    
    /**
     * Algorithm for integration of chance point detection scross multiple logs.
     * 
     * @param logs
     * @param minWeight
     * @param window
     * 
     * @return LogValueList
     */
    public static LogValues integrateChangePoints(List<WellLog> logs, int minWeight, float window) {
        LogValues changePoints = new LogValues();
        
        int dc = 0; // Cropped log height
        for(WellLog log : logs)
            if(log.getLogValues().size() > dc)
                dc = log.getLogValues().size();
        
        if(dc == 0) // If no Log is found, exit.
            return null;
        
        for(int i = 0; i < dc; i++) {
            /*WindowInfo info = new WindowInfo(logs, i, window);
            
            if(info.getTotalWeight() >= minWeight) {
                //changePoints.add(new LogValuePair());
            } else {
                
            }*/
        }
        
        return changePoints;
    }
  
}
