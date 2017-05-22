package interop.well;

/**
 * Represents a Well Segment, which was determined by a analises ran over the well log data.
 * 
 * @author Lucas Hagen
 */
public class WellSegment {
    
    // Height of both top and bottom positions of the segment.
    private float topHeight;
    private float bottomHeight;
    
    // TODO:
    
    public WellSegment(float topHeight, float bottomHeight) {
        this.topHeight = topHeight;
        this.bottomHeight = bottomHeight;
    }
    
    public float getTopPosition() {
        return this.topHeight;
    }
    
    public float getBottomPosition() {
        return this.bottomHeight;
    }
}
