package at.fhv.sysarch.lab3.pipeline.filters;

public interface IPipe<T> {
    void push(T Data);
}
