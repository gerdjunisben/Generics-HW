import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BijectionGroup {
    public static <T> Set<UnaryOperator<T>> bijectionsOf(Set<T> domain)
    {
        Set<UnaryOperator<T>> bijections = new HashSet<>();
        List<List<T>> perms = getPermutations(domain.stream().collect(Collectors.toList()));
        perms.stream().forEach((e)->bijections.add(new UnaryOperator<T>() {
            private HashMap<T,T> hash = new HashMap<>();
            private List<T> temp = domain.stream().collect(Collectors.toList());



            @Override
            public T apply(T t) {

                if(hash.isEmpty())
                {
                    for(int i = 0;i< e.size();i++)
                    {

                        hash.put(temp.get(i),e.get(i));
                    }
                }
                return hash.get(t);
            }
        }));
        return bijections;


    }

    private static <T> List<List<T>> getPermutations(List<T> elems)
    {
        LinkedList<List<T>> res = new LinkedList<>();
        for(int i=0;i<elems.size();i++)
        {
            List<T> tempElems = new LinkedList<>(elems);
            List<T> tempCurrent = new ArrayList<>();
            tempCurrent.add(tempElems.remove(i));
            getPermutationsHelper(tempElems,tempCurrent,res);
        }
        return res;
    }

    private static <T> void getPermutationsHelper(List<T> elems, List<T> current, List<List<T>> res)
    {
        if(elems.size()==0)
        {
            res.add(current);
            return;
        }
        for(int i=0;i<elems.size();i++)
        {
            List<T> tempElems = new LinkedList<>(elems);
            List<T> tempCurrent = new LinkedList<>(current);
            tempCurrent.add(tempElems.remove(i));
            getPermutationsHelper(tempElems,tempCurrent,res);
        }
    }

    static <T> Group<UnaryOperator<T>>  bijectionGroup(Set<T> set){
        return new Group<UnaryOperator<T>>() {
            @Override
            public UnaryOperator<T> binaryOperation(UnaryOperator<T> one, UnaryOperator<T> other) {
                return new UnaryOperator<T>(){

                    @Override
                    public T apply(T t) {
                        return other.apply(one.apply(t));
                    }
                };
            }

            @Override
            public UnaryOperator<T> identity() {
                return new UnaryOperator<T>() {
                    private HashMap<T,T> hash = new HashMap<>();
                    @Override
                    public T apply(T t) {
                        if(hash.isEmpty())
                        {
                            set.forEach((e)->{
                                hash.put(e,e);
                            });
                        }
                        return hash.get(t);
                    }
                };
            }

            @Override
            public UnaryOperator<T> inverseOf(UnaryOperator<T> tUnaryOperator) {


                return new UnaryOperator<T>() {
                    private HashMap<T,T> hash = new HashMap<>();
                    @Override
                    public T apply(T t) {
                        if(hash.isEmpty())
                        {
                            set.forEach((e)->{
                                hash.put(tUnaryOperator.apply(e),e);
                            });
                        }
                        return hash.get(t);
                    }
                };

            }
        };
    }

    public static void main(String[] args)
    {
        Set<Integer> a_few = Stream.of(1, 2,3,4).collect(Collectors.toSet());

// you have to figure out the data type in the line below

        Set<UnaryOperator<Integer>> bijections = bijectionsOf(a_few);
        bijections.forEach(aBijection -> {
            a_few.forEach(n -> System.out.printf("%d --> %d; ", n, aBijection.apply(n)));
            System.out.println();
        });
        System.out.println();

        /*alternate driver code for readability while testing
        LinkedList<String> test = new LinkedList<>();
        bijections.forEach(aBijection -> {
            StringBuilder sb = new StringBuilder();
            a_few.forEach(n -> sb.append(String.format("%d --> %d; ", n, aBijection.apply(n)))
            );
            test.add(sb.toString());
        });


        Collections.sort(test);
        test.forEach((e)->{
            System.out.println(e);
        });
        */
        //Set<Integer> a_few = Stream.of(1, 2, 3).collect(Collectors.toSet());
        // you have to figure out the data types in the lines below
        // some of these data types are functional objects, so look into java.util.function.Function
        Group<UnaryOperator<Integer>> g = bijectionGroup(a_few);
        UnaryOperator<Integer> f1 = bijectionsOf(a_few).stream().findFirst().get();
        UnaryOperator<Integer> f2 = g.inverseOf(f1);
        UnaryOperator<Integer> id = g.identity();

        a_few.forEach((e)->{
            System.out.printf("%d --> %d; ", e, f1.apply(e));
        });
        System.out.println();
        a_few.forEach((e)->{
            System.out.printf("%d --> %d; ", e, f2.apply(e));
        });
        System.out.println();
        a_few.forEach((e)->{
            System.out.printf("%d --> %d; ", e, id.apply(e));
        });
    }
}
