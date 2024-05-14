package hw4;

public class Node<T> {
    private final T name;
    //Abstraction function: Represents non-null nodes
    public Node(T inputName) {
        this.name = inputName;
        //this.checkRep();
    }
    private void checkRep() {
        if (this.name == null){
            throw new RuntimeException("Rep check has failed - provide a name that is not null");
        }
    }
    public boolean equals(Node<T> altNode) {
        if (this == altNode)
            return true;
        if (altNode == null || getClass() != altNode.getClass())
            return false;
        return name.equals(altNode.name);
    }

    public T getName() {
        return this.name;
    }


}
