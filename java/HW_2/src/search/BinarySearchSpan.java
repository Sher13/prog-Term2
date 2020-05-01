package search;

import java.util.ArrayList;
import java.util.List;

public class BinarySearchSpan {

    public static int BS1(List<Integer> a, int x) {
        // Pred: a[i] >= a[j], i < j
        // Post: r - min : a[r] <= x
        int l = 0;
        int r = a.size()-1;
        // l = 0 && r = a.size()-1
        if (a.size() == 0||a.get(r) > x)
            return r+1;
        // l = 0 && x >= a[r] && a.size() > 0
        //  x >= a[r] && l <= r
        while(l!=r) {
            int m = (l+r)/2;
            // m = (l+r)/2 && l <= r && x >= a[r] && m < r && m >= l
            if (a.get(m) <= x)
                // a[m] <= x
                r = m;
                // r' = m && a[m] <= x => a[r'] <= x && l' <= r'
            else
                // a[m] > x
                l = m+1;
            // l' = m+1 && l' <= r' && a[r'] <= x
        }
        //
        return r;
    }
    public static int BS2(List<Integer> a, int x) {
        // Pred: a[i] >= a[j], i < j
        // Post: r - min : a[r] < x
        int l = 0;
        int r = a.size()-1;
        // l = 0 && r = a.size()-1
        if (a.size() == 0||a.get(r) >= x)
            return r+1;
        // l = 0 && x > a[r] && a.size() > 0
        //  x > a[r] && l <= r
        while(l!=r) {
            int m = (l+r)/2;
            // m = (l+r)/2 && l <= r && x > a[r] && m < r && m >= l
            if (a.get(m) < x)
                // a[m] <= x
                r = m;
                // r' = m && a[m] < x => a[r'] < x && l' <= r'
            else
                // a[m] >= x
                l = m+1;
            // l' = m+1 && l' <= r' && a[r'] < x
        }
        //
        return r;
    }
    public static int recBS1(List<Integer> a, int x, int l, int r) {
        // Pred: a[i] >= a[j], i < j && l \in [0,a.length] && l < r
        // Post: r - min : a[r] <= x
        if (r < 0||a.get(r) > x)
            return r+1;
        // a[r] <= x && l <= r
        if (l == r)
            return l;
        // a[r] <= x && l < r
        int m = (l+r)/2;
        // m >= l && m < r
        if (a.get(m) <= x)
            // a[m] <= x -> a[r] <= x && l < r
            return recBS1(a,x,l,m);
        else
            // a[r] <= x && l < r
            return recBS1(a,x,m+1,r);
    }
    public static int recBS2(List<Integer> a, int x, int l, int r) {
        // Pred: a[i] >= a[j], i < j
        // Post: r - min : a[r] < x
        if (r < 0||a.get(r) >= x)
            return r+1;
        // a[r] < x && l <= r
        if (l == r)
            return l;
        // a[r] < x && l < r
        int m = (l+r)/2;
        // m >= l && m < r
        if (a.get(m) < x)
            // a[m] < x -> a[r] < x && l < r
            return recBS2(a,x,l,m);
        else
            // a[r] < x && l < r
            return recBS2(a,x,m+1,r);
    }
    public static void main(String[] args) {
        // Pred: \all i args[i].parseInt == int without exception && args.length > 0
        // Post: return i and l : \all e \in [i,l] == x (args[0])
        int x = Integer.parseInt(args[0]);
        List<Integer> a = new ArrayList<>();
        for (int i = 1;i<args.length;i++)
            a.add(Integer.parseInt(args[i]));
         /*int z = BS1(a,x);
         System.out.print(z);
         System.out.print(" ");
         System.out.print(BS2(a,x)-z);*/
         int s = recBS1(a,x,0,a.size()-1);
         System.out.print(s);
         System.out.print(" ");
         System.out.print(recBS2(a,x,0,a.size()-1)-s);
    }
}
