/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interop.log.model;

/**
 * A pair of depth and log values, represented by two floats.
 *
 * @author Lucas Hagen
 */
public class LogValue {

    private Float depth, value;

    public LogValue(Float depth, Float logValue) {
        this.depth = depth;
        this.value = logValue;
    }

    public Float getDepth() {
        return this.depth;
    }

    public Float getLogValue() {
        return this.value;
    }
}
