package sample;

/**
 * This interface will be implemented by all the lists
 * add : adds node to list
 * remove: removes node of list
 * get: gets the index of a node in the list
 * size: amount of nodes in the list
 * clear: resets the lists
 * change: change the index of the lists
 * @param <T>
 */


public interface List<T>{
    public void add(T value);
    public void removes(int index);
    public T get(int index);
    public int size();
    public void clear();
    public void change(int i1, int i2);

}
