package search;

import java.util.ArrayList;
import java.util.List;

public class BinarySearch {

    public static int BS1(List<Integer> a, int x) {
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
    public static int BS2(List<Integer> a, int x, int l, int r) {
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
            return BS2(a,x,l,m);
        else
            // a[r] <= x && l < r
            return BS2(a,x,m+1,r);
    }
     public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        List<Integer> a = new ArrayList<>();
        for (int i = 1;i<args.length;i++)
            a.add(Integer.parseInt(args[i]));
       // System.out.println(BS1(a,x));
        System.out.println(BS2(a,x,0,a.size()-1));
    }
}
