package at.fhv.sysarch.lab3.pipeline.filters;

public interface IPushFilter<T> {
    void setSuccessor (IPushFilter<?> successor);
    void push (T data);
}
