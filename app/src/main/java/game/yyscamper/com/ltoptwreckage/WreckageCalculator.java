package game.yyscamper.com.ltoptwreckage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by Snowing on 2014/7/27.
 */
public class WreckageCalculator {

    public static String GetResultString(WreckageResult result, Wreckage[] wreckages, int target)
    {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < wreckages.length; i++)
        {
            if (result.Nums[i] == 1)
            {
                //sb.Append(wreckages[i].Value + " + ");
                sb.append(String.format("%d*%d + ", wreckages[i].Value, result.Nums[i]));
            }
            else if (result.Nums[i] >= 2)
            {
                sb.append(String.format("%d*%d + ", wreckages[i].Value, result.Nums[i]));
            }
        }

        sb.delete(sb.length() - 3, sb.length());
        return sb.toString();
    }

    private static void SortResult(ArrayList<WreckageResult> results)
    {
        Collections.sort(results, new Comparator<WreckageResult>() {
            @Override
            public int compare(WreckageResult r1, WreckageResult r2) {
                if (r1.Value < r2.Value)
                    return -1;
                else if (r1.Value > r2.Value)
                    return 1;
                else
                {
                    if (r1.GetTotalNumOfWreckage() < r2.GetTotalNumOfWreckage())
                    {
                        return -1;
                    }
                    else if (r1.GetTotalNumOfWreckage() > r2.GetTotalNumOfWreckage())
                    {
                        return 1;
                    }
                    else
                    {
                        return 0;
                    }
                }
            }
        });
    }

    public static ArrayList<WreckageResult> DoCalc(Wreckage[] wreckages, int target, int allowedError)
    {
        int calValue = 0;
        ArrayList<WreckageResult> results = new ArrayList<WreckageResult>();
        int upLimit = target + allowedError;
        int temp;
        int[] maxNums = new int[wreckages.length];

        for (int i = 0; i < wreckages.length; i++)
        {
            wreckages[i].CurrNum = 0;

            temp = upLimit / wreckages[i].Value;
            if (wreckages[i].MaxNum > temp)
                maxNums[i] = temp;
            else
                maxNums[i] = wreckages[i].MaxNum;
        }

        while (true)
        {
            next_round:
            if (wreckages[wreckages.length - 1].CurrNum > maxNums[wreckages.length - 1]) //finished all scan
            {
                SortResult(results);
                return results;
            }

            while (wreckages[0].CurrNum <= maxNums[0])
            {
                calValue = 0;
                for (int x = 0; x < wreckages.length; x++)
                {
                    calValue += (wreckages[x].CurrNum * wreckages[x].Value);
                }

                if (calValue >= target && calValue < target + allowedError)
                {
                    int[] nums = new int[wreckages.length];
                    for (int y = 0; y < wreckages.length; y++)
                    {
                        nums[y] = wreckages[y].CurrNum;
                    }
                    results.add(new WreckageResult(nums, calValue));
                }

                wreckages[0].CurrNum++;
            }

            if (wreckages.length == 1)
            {
                SortResult(results);
                return results;
            }

            wreckages[0].CurrNum = 0;

            boolean flagExit = false;
            for (int i = 1; i < wreckages.length; i++)
            {
                if (++wreckages[i].CurrNum > maxNums[i])
                {
                    //shift right one
                    if (i != (wreckages.length - 1))
                        wreckages[i].CurrNum = 0;

                    for (int j = i + 1; j < wreckages.length; j++)
                    {
                        if (++wreckages[j].CurrNum <= maxNums[j])
                        {
                            flagExit = true;
                            break;
                        }
                        else
                        {
                            if (j != (wreckages.length - 1))
                                wreckages[j].CurrNum = 0;
                        }
                    }

                    if (flagExit)
                        break;
                }
                else
                {
                    break;
                }
            }
        }
    }

}
