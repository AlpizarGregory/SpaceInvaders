package linkedListsTypes;

/**
 * The circular linked list is going
 * to be used for the C and D class enemies
 * @param <T>
 */
public class CircleList<T> implements List<T>{
    private Node<T> head;
    private int size;

    public CircleList(){
        head = null;
        size = 0;
    }

    @Override
    public void add(T value) {
        Node<T> newNode = new Node<T>();
        newNode.setElement(value);
        if (head == null){
            head = newNode;
            head.setNext(head);
            ++size;
        }else {
            Node<T> current = head;
            while (current.getNext() != head){
                current = current.getNext();
            }
            current.setNext(newNode);
            newNode.setNext(head);
            ++size;
        }

    }

    @Override
    public void remove(int index) {
        if (index == 0 && index < size){
            head = head.getNext();
            --size;
        }else {
            Node<T> current = head;
            int counter = 0;
            while (counter < index - 1){
                current = current.getNext();
                counter++;
            }
            current.setNext(current.getNext().getNext());
            --size;
        }
    }

    @Override
    public T get(int index) {
        Node<T> current = head;
        for (int i = 0; i < index; i++){
            current = current.getNext();
        }
        return current.getElement();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        this.head = null;
        this.size = 0;
    }

    @Override
    public void change(int i1, int i2) {
    }
}
