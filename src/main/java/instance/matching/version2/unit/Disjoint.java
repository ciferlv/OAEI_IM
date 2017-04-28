package instance.matching.version2.unit;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by ciferlv on 17-4-25.
 */
public class Disjoint {

    private static Map<String, Set<String>> disjoint = new HashMap<>();

    private void swap(String s1, String s2) {

        String tmp = s1;
        s1 = s2;
        s2 = tmp;
    }

    public void addDisjoint(String dis1, String dis2) {

        dis1 = dis1.toLowerCase();
        dis2 = dis2.toLowerCase();

        if (dis1.compareTo(dis2) > 0) {

            String tmp = dis1;
            dis1 = dis2;
            dis2 = tmp;
        }

        if (disjoint.containsKey(dis1)) {

            Set<String> disSet = disjoint.get(dis1);
            disSet.add(dis2);
        } else {

            Set<String> disSet = new HashSet<>();
            disSet.add(dis2);
            disjoint.put(dis1, disSet);
        }
    }

    public static boolean isDisjoint(Instance inst1, Instance inst2) {

        Set<String> typeSet1 = inst1.getTypeSet();
        Set<String> typeSet2 = inst2.getTypeSet();

        for (String type1 : typeSet1) {

            for (String type2 : typeSet2) {

                if (type1.compareTo(type2) > 0) {

                    String tmp = type1;
                    type1 = type2;
                    type2 = tmp;
                }

                if (disjoint.containsKey(type1)) {

                    Set<String> disSet = disjoint.get(type1);
                    if (disSet.contains(type2)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public String toString() {

        StringBuffer buffer = new StringBuffer();
        buffer.append("Disjoint Relation\n");

        for (Map.Entry<String, Set<String>> entry : disjoint.entrySet()) {

            String disjointKey = entry.getKey();

            buffer.append("DisjointKey:\n" + disjointKey + "\n");

            Set<String> disjointSet = entry.getValue();

            buffer.append("DisjointSet:\n");
            disjointSet.stream().forEach(dis -> buffer.append(dis + "\n"));
            buffer.append("\n");
        }

        return String.valueOf(buffer);
    }


}
