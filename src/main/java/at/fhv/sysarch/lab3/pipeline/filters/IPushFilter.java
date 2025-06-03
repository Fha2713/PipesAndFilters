package at.fhv.sysarch.lab3.pipeline.filters;

public interface IPushFilter<T> {
    void push (T data);
    void setSuccessor (IPushFilter<?> successor);
}
