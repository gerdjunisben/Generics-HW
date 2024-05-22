import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HigherOrderUtil {
    static interface NamedBiFunction<A,B,C> extends BiFunction<A, B, C> {
        String name();
    }

    public static NamedBiFunction<Double,Double,Double> add = new NamedBiFunction<Double,Double,Double>() {


        @Override
        public String name() {
            return "plus";
        }

        @Override
        public Double apply(Double aDouble, Double aDouble2) {
            return aDouble + aDouble2;
        }
    };

    public static NamedBiFunction<Double,Double,Double> subtract = new NamedBiFunction<Double,Double,Double>() {
        @Override
        public String name() {
            return "minus";
        }

        @Override
        public Double apply(Double aDouble, Double aDouble2) {
            return aDouble - aDouble2;
        }
    };

    public static NamedBiFunction<Double,Double,Double> multiply = new NamedBiFunction<Double,Double,Double>() {
        @Override
        public String name() {
            return "mult";

        }

        @Override
        public Double apply(Double aDouble, Double aDouble2) {
            return aDouble * aDouble2;
        }
    };

    public static NamedBiFunction<Double,Double,Double> divide = new NamedBiFunction<Double,Double,Double>() {
        @Override
        public String name() {
            return "div";
        }

        @Override
        public Double apply(Double aDouble, Double aDouble2) throws ArithmeticException
        {
            return aDouble / aDouble2;
        }
    };

    public static <T> T zip(List<T> args, List<? extends BiFunction<T,T,T>> biFunctions)
    {
        if(args.size() != biFunctions.size()+1)
            throw new IllegalArgumentException("Args length or bifunctions length is invalid, there should be one less bifunction than arg");
        //we stream through range 0 to the size of biFunctions so
        //we can access the elems in both lists at the same time
        return IntStream.range(0, biFunctions.size())
                .boxed()                //turns our range into a stream from an int stream
                .reduce(args.get(0),    //res = elem 0 initially
                        (res, i) -> biFunctions.get(i).apply(res, args.get(i + 1)), //get the ith function and apply to res and arg i+1
                        (a, b) -> b);   //since we are working on a range when we use reduce we grab the elems in two
                                        //this says to use the second as the index for the next iteration


    }

    public static void main(String args[])
    {
        List<Double> numbers = Arrays.asList(-0.5, 2d, 3d, 0d, 4d); // documentation example
        List<NamedBiFunction<Double, Double, Double>> operations = Arrays.asList(add,multiply,add,divide);
        Double d = zip(numbers, operations); // expected correct value: 1.125
// different use case, not with NamedBiFunction objects
        List<String> strings = Arrays.asList("a", "n", "t");
// note the syntax of this lambda expression
        BiFunction<String, String, String> concat = (s, t) -> s + t;
        String s = zip(strings, Arrays.asList(concat, concat)); // expected correct value: "ant"
        System.out.println(d +" " + s);

    }




}
