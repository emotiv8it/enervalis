package abb.interview.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("power")
public class Power {
    @JsonProperty
    private double min;
    @JsonProperty
    private double max;
    @JsonProperty
    private double avg;
    @JsonProperty
    private long timestamp;

    public Power() {
    }

    public Power(double min, double max, double avg, long timestamp) {
        this.min = min;
        this.max = max;
        this.avg = avg;
        this.timestamp = timestamp;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getAvg() {
        return avg;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
