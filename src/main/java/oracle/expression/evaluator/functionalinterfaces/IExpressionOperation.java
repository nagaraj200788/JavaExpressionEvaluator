package oracle.expression.evaluator.functionalinterfaces;

@FunctionalInterface
public interface IExpressionOperation<A,B,C,D> {
    A perform(B b,C c,D d);
}
