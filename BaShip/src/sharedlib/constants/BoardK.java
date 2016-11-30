package sharedlib.constants;

import java.util.*;

public class BoardK {

    public static final int BOARD_SIZE = 10;

    public static final Map<Integer, Integer> SHIP_COUNT_FOR_SIZE
            = Collections.unmodifiableMap(
                    new HashMap<Integer, Integer>() {
                {
                    put(1, 4);
                    put(2, 3);
                    put(3, 2);
                    put(4, 1);
                }
            });

    public static final int SHIP_COUNT = SHIP_COUNT_FOR_SIZE.values().stream().reduce(0, Integer::sum);
    public static final int BOTTOM_INFO_SQUARES_COUNT = SHIP_COUNT_FOR_SIZE.entrySet().stream().map((e) -> e.getKey() * e.getValue()).reduce(0, Integer::sum);

}
