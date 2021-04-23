package sample;

/**
 * Node made for the double and double circle lists
 * the only difference with the normal node is the
 * previous method
 * @param <T>
 */

public class DoubleNode<T>{
    private T element;
    private DoubleNode<T> next;
    private DoubleNode<T> previous;
    public DoubleNode(){
        this.element = null;
        this.next = null;
        this.previous = null;
    }
    public DoubleNode<T> getNext(){
        return next;
    }
    public void setNext(DoubleNode<T> next){
        this.next = next;
    }
    public DoubleNode<T> getPrevious(){
        return previous;
    }
    public void setPrevious(DoubleNode<T> previous){
        this.previous = previous;
    }
    public T getElement(){
        return element;
    }
    public void setElement(T element){
        this.element = element;
    }
}