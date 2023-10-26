package abb.interview.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Direction {
    @JsonProperty("in")
    IN,
    @JsonProperty("out")
    OUT
}
