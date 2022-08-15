package dev.hellojonas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryParser implements Parser<List<Criteria<Object>>> {

    @Override
    public List<Criteria<Object>> parse(String query) {
        
        List<Criteria<Object>> criterias = new ArrayList<>();

        String regex = "(\\w+)(:|\\[.+?\\]):?(.+)";
        Pattern p = Pattern.compile(regex);

        Arrays
            .stream(query.split(";"))
            .forEach(cString -> {
                Matcher m = p.matcher(cString);

                if (m.groupCount() < 3) {
                    return;
                }

                Criteria<Object> criteria = new Criteria<>();
                criteria.setKey(m.group(1));
                criteria.setOp(m.group(2));

                String value = m.group(3);
                criteria.setValue(value);

                if(isArray(value)) {
                    criteria.setValue(value.substring(1, value.length() - 1).split(","));
                } else if (isRange(value)) {
                    criteria.setValue(value.split(","));
                } 

                criterias.add(criteria);
            });

        return criterias;        
    }
    
    public boolean isValidQuery(String q) {
        // TODO: write regex to validate the whole query
        // It will be used to define strict mode
        Pattern p = Pattern.compile("(?:\\w+(?::|\\[.+?\\])(?:\\[.+?\\]|\\w+)");
        Matcher m = p.matcher(q);

        return m.matches();
    }

    public boolean isRange(String rng) {
        return rng.split(",").length == 2;
    }

    public boolean isArray(String str) {
        return str.startsWith("[")
            && str.endsWith("]");
    }
}
