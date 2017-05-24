package interop.log.model;

/**
 * @author Lucas Hagen
 */

public class ConfigurationCache {

    private float smallWindow;
    private float bigWindow;
    private float weight;

    private boolean active;

    public ConfigurationCache(LogConfiguration config) {
        this(config.getSmallWindow(), config.getBigWindow(), config.getWeight(), config.isActive());
    }

    public ConfigurationCache(float smallWindow, float bigWindow, float weight, boolean active) {
        this.smallWindow = smallWindow;
        this.bigWindow = bigWindow;
        this.weight = weight;
        this.active = active;
    }


    public float getSmallWindow() {
        return smallWindow;
    }

    public void setSmallWindow(float smallWindow) {
        this.smallWindow = smallWindow;
    }

    public float getBigWindow() {
        return bigWindow;
    }

    public void setBigWindow(float bigWindow) {
        this.bigWindow = bigWindow;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
