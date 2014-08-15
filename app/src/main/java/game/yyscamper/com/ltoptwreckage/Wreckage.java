package game.yyscamper.com.ltoptwreckage;

import java.util.Comparator;

/**
 * Created by Snowing on 2014/7/27.
 */
public class Wreckage
{
    public int Value;
    public int MaxNum;
    public int CurrNum;

    public Wreckage(int val, int max)
    {
        Value = val;
        MaxNum = max;
        CurrNum = 0;
    }

    public Wreckage(int value)
    {
        Value = value;
        CurrNum = 0;

        if (value == 400 || value == 9600 || value == 4800 || value == 28800)
        {
            MaxNum = 2;
        }
        else if (value == 5328)
        {
            MaxNum = 16;
        }
        else if (value == 6024 || value == 8440 || value == 9648)
        {
            MaxNum = 6;
        }
        else if (value < 4000 || value > 20000)
        {
            MaxNum = 1;
        }
        else
        {
            MaxNum = 10;
        }
    }

    public static Comparator<Wreckage> WreckageComparator = new Comparator<Wreckage>() {
        @Override
        public int compare(Wreckage w1, Wreckage w2) {
            if (w1.Value > w2.Value)
                return -1;
            else if (w2.Value < w1.Value)
                return 1;
            else
                return 0;
        }
    };
}

