import java.util.*;

public class DLBmod<Value>
{
    private Node<Value> root;
    private Node<Value> n;
    private Node<Value> pointer;

    @SuppressWarnings("rawtypes")
    private static class Node<Value>
    {
        private Value val;
        private char key = (char)0;
        private Node<Value> next;
        private Node<Value> child;
    }

    public DLBmod()
    {
        root = new Node<>();
    }

    @SuppressWarnings("unchecked")
    public void put(StringBuilder s, Value val)
    {
        pointer = put(root, s, val, 0);
    }

    @SuppressWarnings("unchecked")
    private Node<Value> put(Node<Value> x, StringBuilder s, Value val, int d)
    {
        if (x == null) x = new Node<>();
        if (d == s.length())
        {
            x.val = val;
            return x;
        }

        char c = s.charAt(d);
        do
        {
            if (x.key == (char)0)  //new child
            {
                x.key = c; break;
            }
            else if (x.key == c)
            {
                break;
            }
            else if (x.next == null)
            {
                x.next = new Node<Value>();
                x.next.key = c;
                x = x.next; break;
            }

            x = x.next;
        }   while (x != null);


        if (x.child == null) x.child = put(x.child, s, val, d+1);
        else    put(x.child, s, val, d+1);
        return x;
    }

    public Value get(StringBuilder s)
    {
        n = root;
        return get(s, 0, s.length()-1);
    }

    @SuppressWarnings("unchecked")
    public Value get(StringBuilder s, int start, int end)
    {
        char c = s.charAt(start);

        if (n == null) return null;
        do
        {
            if (c == n.key) break;
            n = n.next;
        }   while (n != null);

        if (n == null) return null;
        if (start == end && n.child != null && n.child.val != null)
            return n.child.val;

        n = n.child;
        return get(s, start+1, end);
    }

    public boolean searchPrefix(StringBuilder s)
    {
        n = root;
        return searchPrefix(s, 0, s.length()-1);
    }

    @SuppressWarnings("unchecked")
    public boolean searchPrefix(StringBuilder s, int start, int end)
    {
        char c = s.charAt(start);

        if (n == null) return true;
        do
        {
            if (c == n.key) break;
            n = n.next;
        }   while (n != null);

        if (n == null && start == end) return true;
        if (start == end) return false;

        n = n.child;
        return searchPrefix(s, start+1, end);
    }
}
