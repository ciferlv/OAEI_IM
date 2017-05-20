package instance.matching.unit;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by ciferlv on 17-4-25.
 */
public class SubClass {

    private static Map<String, Set<String>> subClassMap = new HashMap<String, Set<String>>();

    public void addsSubClass(String parent, String child) {

        parent = parent.toLowerCase();
        child = child.toLowerCase();

        if (subClassMap.containsKey(parent)) {

            Set<String> children = subClassMap.get(parent);
            children.add(child);
        } else {

            Set<String> children = new HashSet<>();
            children.add(child);
            subClassMap.put(parent, children);
        }
    }

    public static Set<String> getSubClass(String parent) {

        return subClassMap.get(parent);
    }

    @Override
    public String toString() {

        StringBuffer buffer = new StringBuffer();
        buffer.append("SubClass Relation\n");

        for (Map.Entry<String, Set<String>> entry : subClassMap.entrySet()) {

            String parent = entry.getKey();

            buffer.append("Parent:\n" + parent + "\n");

            Set<String> children = entry.getValue();

            buffer.append("Child:\n");
            children.stream().forEach(child -> buffer.append(child + "\n"));
            buffer.append("\n");
        }

        return String.valueOf(buffer);
    }
}
