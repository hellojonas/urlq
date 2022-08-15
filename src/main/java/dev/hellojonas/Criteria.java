package dev.hellojonas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Criteria<T> {
    
    private String key;
    private String op;
    private T value;
}
