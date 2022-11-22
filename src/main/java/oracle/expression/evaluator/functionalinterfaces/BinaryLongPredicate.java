package oracle.expression.evaluator.functionalinterfaces;

@FunctionalInterface
public interface BinaryLongPredicate
{
    boolean test(long d1, long d2);
}
