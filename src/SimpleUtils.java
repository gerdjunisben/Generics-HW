import java.util.*;
import java.util.stream.Collectors;

public class SimpleUtils {
    public static <T extends Comparable<T>> T least(Collection<T> items, boolean from_start)
    {
        if(items==null)
            throw new IllegalArgumentException("items is null");
        return items.stream()
                    .reduce((e1,e2)->{
                        if(e1.compareTo(e2)<0)
                        {
                            return e1;
                        }
                        else if(e1.compareTo(e2)>0)
                        {
                            return e2;
                        }
                        else
                        {
                            if(from_start)
                            {
                                return e1;
                            }
                            else
                            {
                                return e2;
                            }
                        }
                    })
                    .orElse(null);
    }

    public static <K,V> List<String> flatten(Map<K,V> aMap)
    {
        return aMap.entrySet().stream()
                .map((e)->{return e.getKey().toString() + " -> " + e.getValue().toString();})
                .collect(Collectors.toList());
    }

    public static void main(String[] args)
    {
        List<Integer> nums = new ArrayList<>();
        nums.add(1);
        nums.add(35);
        nums.add(7);
        nums.add(47);
        nums.add(2);
        System.out.println(least(nums,true));

        Map<String,Integer> words = new HashMap<>();
        words.put("Key",2);
        words.put("Benjamin",47);
        words.put("CSE",216);
        System.out.println(flatten(words));

    }
}
