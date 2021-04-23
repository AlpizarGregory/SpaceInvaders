package sample;

/**
 * The double linked list is going to
 * be used for the B class enemies
 * @param <T>
 */

public class DoubleMonsterList<T> implements List<T> {
    private DoubleNode<T> head;
    private DoubleNode<T> tail;
    private int size;
    public DoubleMonsterList(){
        head = null;
        tail = null;
        size = 0;
    }
    @Override
    public void add(T value) {
        DoubleNode<T> newNode = new DoubleNode<T>();
        newNode.setElement(value);
        if (head == null){
            head = tail = newNode;
            head.setPrevious(null);
            head.setNext(null);
            size++;
        }else {
            tail.setNext(newNode);
            head.setPrevious(tail);
            tail = newNode;
            tail.setNext(null);
            size++;
        }
    }

    @Override
    public void removes(int index) {
        if (index == 0 && index < size){
            head = head.getNext();
            size--;
            return;
        }
        DoubleNode<T> current = head;
        int counter = 0;
        while (counter < index - 1 && current.getNext() != null){
            current = current.getNext();
            counter++;
        }
        if (counter == size - 2){
            current.setNext(null);
            size--;
            return;
        }else {
            current.setNext(current.getNext().getNext());
            size--;
            return;
        }
    }



    @Override
    public T get(int index) {
        if (index > size - 1)
            return null;
        DoubleNode<T> current = head;
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
        head = null;
        tail = null;
        size = 0;
    }
    @Override
    public void change(int i1, int i2) {
        T content1 = this.get(i1);
        T content2 = this.get(i2);
        DoubleNode<T> current = head;
        for (int i = 0; i < size; i++){
            if (this.get(i) == content1){
                current.setElement(content2);
            }else if (this.get(i) == content2){
                current.setElement(content1);
            }
            current = current.getNext();
        }
    }
}