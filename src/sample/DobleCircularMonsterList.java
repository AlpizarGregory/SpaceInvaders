package sample;

/**
 * The double circular linked list is going to be
 * used for the E class enemies
 * @param <T>
 */
class DoubleCircularMonsterList<T> implements List<T>{
    private DoubleNode<T> head;
    private int size;
    public DoubleCircularMonsterList(){
        head = null;
        size = 0;
    }
    @Override
    public void add(T value) {
        DoubleNode<T> newNode = new DoubleNode<T>();
        newNode.setElement(value);
        if (head == null){
            head = newNode;
            head.setNext(head);
            head.setPrevious(head);
            size++;
        }else {
            DoubleNode<T> current = head;
            while (current.getNext() != head){
                current = current.getNext();
            }
            current.setNext(newNode);
            newNode.setNext(head);
            newNode.setPrevious(current);
            size++;
        }
    }


    @Override
    public void removes(int index) {
        if (index == 0){
            head.getPrevious().setNext(head.getNext());
            head.getNext().setPrevious(head.getPrevious());
            head = head.getNext();
            size--;
            return;
        }else if (index <= size / 2){
            DoubleNode<T> current = head;
            for (int i = 0; i <= index; i++){
                current = current.getNext();
            }
            current.getPrevious().setNext(current.getNext());
            current.getNext().setPrevious(current.getPrevious());
        }else if (index > size / 2){
            DoubleNode<T> current = head;
            for (int i = size; i >= index; i--){
                current = current.getPrevious();
            }
            current.getPrevious().setNext(current.getNext());
            current.getNext().setPrevious(current.getPrevious());
        }
    }
    @Override
    public T get(int index) {
        if (index == 0){
            return head.getElement();
        }else if (index <= size / 2){
            DoubleNode<T> current = head;
            for (int i = 0; i <= index; i++){
                current = current.getNext();
            }
            return current.getElement();
        }else if (index > size / 2){
            DoubleNode<T> current = head;
            for (int i = 0; i >= index; i--){
                current = current.getPrevious();
            }
            return current.getElement();
        }else {
            return null;
        }
    }
    @Override
    public int size() {
        return size;
    }
    @Override
    public void clear() {
        head = null;
        //tail = null;
        size = 0;
    }
    @Override
    public void change(int i1, int i2) {
    }
}