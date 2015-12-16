import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class PlyTable {
  
  private Map <Integer, Set <Ply>> table;
  private List <Ply> list;
  
  public Map <Integer, Set <Ply>> getTable () {
    return table;
  }
  
  public List <Ply> getList () {
    return list;
  }
  
  public Set <Ply> get (int key) {
    return table.get (key);
  }
  
  public int size () {
    return list.size ();
  }
  
  public boolean isEmpty () {
    return list.isEmpty ();
  }
  
  public void clear () {
    table.clear ();
    list.clear ();
  }
  
  public void put (int start, Set <Ply> moves) {
    table.put (start, moves);
    list.addAll (moves);
    reorder ();
  }
  
  public void remove (Ply ply) {
    table.get (ply.getStart ()).remove (ply);
    list.remove (ply);
  }
  
  private void reorder () {
    Collections.sort (list);
  }
  
  @Override
  public String toString () {
    return table.toString ();
  }
  
  public PlyTable () {
    this.table = new HashMap <Integer, Set <Ply>> ();
    this.list = new ArrayList <Ply> ();
  }
  
  public PlyTable (Map <Integer, Set <Ply>> table) {
    this.table = table;
    this.list = new ArrayList <Ply> ();
    
    for (Set <Ply> i : table.values ()) {
      list.addAll (i);
    }
    reorder ();
  }
  
  public PlyTable (PlyTable plyTable) {
    this.table = new HashMap <Integer, Set <Ply>> (plyTable.getTable ());
    this.list = new ArrayList <Ply> (plyTable.getList ());
  }
}