import java.util.List;
import java.util.ArrayList;

public class Tree <T> {
  
  private Node <T> root;
  
  public Tree () {
    super ();
    root = new Node <T> ();
  }
  
  public Node <T> getNode () {
    return this.root;
  }
  
  public void setNode (Node <T> root) {
    this.root = root;
  }
  
  public List <Node <T>> toList () {
    List <Node <T>> list = new ArrayList <Node <T>> ();
    walk (root, list);
    return list;
  }
  
  public String toString () {
    return toList ().toString ();
  }
  
  /**
   * Walks the Tree in pre-order style. This is a recursive method, and is
   * called from the toList() method with the root element as the first
   * argument. It appends to the second argument, which is passed by reference     * as it recurses down the tree.
   * @param element the starting element.
   * @param list the output of the walk.
   */
  
  private void walk (Node<T> element, List <Node <T>> list) {
    list.add (element);
    for (Node <T> data : element.getChildren ()) {
      walk (data, list);
    }
  }
}