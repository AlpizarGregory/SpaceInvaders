package sample;


public interface List<T>{
    public void add(T value);
    public void removes(int index);
    public T get(int index);
    public int size();
    public void clear();
    public void change(int i1, int i2);

}
