package linkedListsTypes;

/**
 * This simple linked list is going to
 * be used for the basic and class A
 * enemies
 * @param <T>
 */
public class SimpleLinkedList<T> implements List<T> {
    private Node<T> head;
    private int size;

    public SimpleLinkedList(){
        head = null;
        size = 0;
    }
    @Override
    public void add(T value) {
        Node<T> newNode = new Node<T>();
        newNode.setElement(value);
        if (head == null){
            head = newNode;
            size++;
        }else{
            Node<T> current = head;
            while (current.getNext() != null){
                current = current.getNext();
            }
            current.setNext(newNode);
            size++;
        }
    }

    @Override
    public void remove(int index) {
        if (index == 0 && index < size){
            head = head.getNext();
            size--;
            return;
        }
        Node<T> current = head;
        int counter = 0;
        while (counter < index - 1 && current.getNext() != null){
            current = current.getNext();
            counter++;
        }
        if (counter == size - 2){
            current.setNext(null);
            size--;
        }else {
            current.setNext(current.getNext().getNext());
            size--;
        }
    }

    @Override
    public T get(int index) {
        if (index > size - 1)
            return null;
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
        T content1 = this.get(i1);
        T content2 = this.get(i2);
        Node<T> current = head;
        for (int i = 0; i < this.size(); i++){
            if (this.get(i) == content1){
                current.setElement(content2);
            }else if (this.get(i) == content2){
                current.setElement(content1);
            }
            current = current.getNext();
        }
    }
}
