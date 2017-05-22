/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interop.well;

import interop.log.model.WellLog;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author lucas
 */
public class Well {
    
    // List of all Well Segments.
    private List<WellSegment> segments;
    // Logs used to find the change point from each segment.
    private List<WellLog> logs;
   
    
    public static void main(String[] args) {
        HashMap<Double, String> teste = new HashMap<>();
        for(int i = 0; i < 100; i++)
            teste.put(0.1 + i, "Teste");
        
        for(Double i : teste.keySet())
            System.out.println(i);
    }
}
