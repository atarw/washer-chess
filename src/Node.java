import java.util.List;
import java.util.ArrayList;

public class Node <T> {
  
  public T data;
  public List <Node <T>> children;
  
  public T getData () {
    return this.data;
  }
  
  public void setData (T data) {
    this.data = data;
  }
  
  public Node <T> getChild (int index) {
    if (!this.hasChildren ()) {
      throw new IllegalArgumentException ("No children");
    }
    return children.get (index);
  }
  
  public List <Node <T>> getChildren () {
    if (this.children == null) {
      return new ArrayList <Node <T>> ();
    }
    return this.children;
  }
  
  public boolean hasChildren () {
    return getNumberOfChildren () != 0;
  }
  
  public void setChildren (List <Node <T>> children) {
    this.children = children;
  }
  
  public int getNumberOfChildren () {
    if (children == null) {
      return 0;
    }
    return children.size ();
  }
  
  public void addChildren (List <T> children) {
    for (T i : children) {
      this.addChild (i);
    }
  }
  
  public void addChild (T child) {
    addChild (new Node <T> (child));
  }
  
  public void addChild (Node <T> child) {
    if (children == null) {
      children = new ArrayList <Node <T>> ();
    }
    children.add (child);
  }
  
  public void addChildAt (int index, T child) {
    addChildAt (index, new Node <T> (child));
  }
  
  public void addChildAt (int index, Node <T> child) throws IndexOutOfBoundsException {
    if (index == getNumberOfChildren ()) {
      // this is really an append
      addChild (child);
      return;
    }
    else {
      children.get (index); //just to throw the exception, and stop here
      children.add (index, child);
    }
  }
  
  public void removeChildAt (int index) throws IndexOutOfBoundsException {
    children.remove (index);
  }
  
  @Override
  public String toString () {
    StringBuilder sb = new StringBuilder ();
    sb.append ("{").append (getData ().toString ()).append (",[");
    
    int i = 0;
    
    for (Node <T> e : getChildren ()) {
      if (i > 0) {
        sb.append (",");
      }
      sb.append (e.getData ().toString ());
      i++;
    }
    sb.append ("]").append ("}");
    
    return sb.toString ();
  }
  
  public Node () {
    super ();
  }
  
  public Node (T data) {
    this ();
    setData (data);
  }
}