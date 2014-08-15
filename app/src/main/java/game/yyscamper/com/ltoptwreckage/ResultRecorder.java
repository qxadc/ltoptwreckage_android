
package game.yyscamper.com.ltoptwreckage;

        import java.lang.reflect.Array;
        import java.util.ArrayList;
        import java.util.Collections;
        import java.util.HashSet;
        import java.util.Iterator;
        import java.util.Set;

/**
 * Created by Snowing on 2014/7/27.
 */
public class ResultRecorder {
    public static ArrayList<WreckageResult> Results;
    public static int Target;
    public static Wreckage[] Wreckages;
    public static int AllowedError;
    public static int SelectedResultIndex = -1;

    private static int parseInt(String str, int startIndex, int endIndex)
    {
        int val = 0;
        for (int i = startIndex; i < endIndex; i++)
        {
            val = val * 10 + (str.charAt(i) - '0');
        }
        return val;
    }

    public static ArrayList<Wreckage> parseSelectedWreckagesSet(Set<String> items) {
        ArrayList<Wreckage> lst = new ArrayList<Wreckage>();
        Iterator<String> iters = items.iterator();
        while(iters.hasNext())
        {
            String str = iters.next();
            int idx = str.indexOf(':');
            int val = parseInt(str, 0, idx);
            int num = parseInt(str, idx + 1, str.length());
            lst.add(new Wreckage(val, num));
        }

        Collections.sort(lst, Wreckage.WreckageComparator);
        return lst;
    }

    public static HashSet<String> convertSelectedWreckagesSet(Wreckage[] wreckages) {
        HashSet<String> items = new HashSet<String>();
        for (Wreckage w : wreckages)
        {
            items.add(w.Value + ":" + w.MaxNum);
        }
        return items;
    }

}