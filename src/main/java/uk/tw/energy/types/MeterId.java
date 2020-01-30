package uk.tw.energy.types;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class MeterId {
    private final String value;

    private MeterId(String value) {
        this.value = value;
    }

    public static MeterId of(String value){
        return new MeterId(value);
    }

    public String getValue(){
        return new String(value);
    }
}
