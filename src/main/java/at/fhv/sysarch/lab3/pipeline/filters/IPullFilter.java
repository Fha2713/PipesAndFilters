package at.fhv.sysarch.lab3.pipeline.filters;

public interface IPullFilter<T> {
    void setPredecessor(IPullFilter<?> predecessor);
    T pull();
}
