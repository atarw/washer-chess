package washer.Test;

import org.junit.Assert;
import org.junit.Test;
import washer.engine.Node;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.*;

public class NodeTest {
    @Test
    public  void getNumberOfChildTest0() {
        Node<Integer> node = new Node<>();
        for (int i = 0; i < 10; i++) {
            assertEquals(i, node.getNumberOfChildren());
            node.addChild(i);
        }
    }
    @Test ( expected  = NullPointerException.class)
    public void toStringNoll(){
        Node<Integer> node = new Node<>();
        String s= new String("{,[]}") ;
        Assert.assertEquals(s, node.toString());
        Assert.assertFalse(node.hasChildren());
    }
    @Test
    public void toString1(){
        Node<Integer> node = new Node<>(1);
        String s= new String("{1,[]}") ;
        Assert.assertEquals(s, node.toString());
        Assert.assertFalse(node.hasChildren());
    }
    @Test
    public void toString12(){
        Node<Integer> node = new Node<>(1);
        node.addChild(2);
        String s= new String("{1,[2]}") ;
        Assert.assertEquals(s, node.toString());
        Assert.assertTrue(node.hasChildren());
    }
    @Test
    public void toString123(){
        Node<Integer> node = new Node<>(1);
        node.addChild(2);
        node.addChild(3);
        String s= new String("{1,[2,3]}") ;
        Assert.assertEquals(s, node.toString());
        Assert.assertTrue(node.hasChildren());
    }
}