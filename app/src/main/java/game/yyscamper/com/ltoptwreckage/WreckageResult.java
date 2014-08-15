package game.yyscamper.com.ltoptwreckage;

/**
 * Created by Snowing on 2014/7/27.
 */
public class WreckageResult {
    public int[] Nums;
    public int Value;

    public WreckageResult(int[] nums, int val)
    {
        Nums = nums;
        Value = val;
    }

    public int GetTotalNumOfWreckage()
    {
        int n = 0;
        for (int i : Nums)
            n += i;
        return n;
    }
}
