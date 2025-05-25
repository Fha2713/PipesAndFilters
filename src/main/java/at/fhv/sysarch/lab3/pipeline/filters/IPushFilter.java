package at.fhv.sysarch.lab3.pipeline.filters;

public interface IPushFilter<I, O> {
    void process(I input, IPipe<O> output);
}
