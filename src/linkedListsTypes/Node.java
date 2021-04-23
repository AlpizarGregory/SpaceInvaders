package linkedListsTypes;

/**
 * Node used in the simple and circular lists
 * @param <T>
 */
public class Node<T> {
    private T element;
    private Node next;

    public Node(){
        element = null;
        next = null;
    }

    public T getElement(){
        return element;
    }

    public void setElement(T element){
        this.element = element;
    }

    public Node<T> getNext(){
        return next;
    }

    public void setNext(Node<T> next){
        this.next = next;
    }

}
