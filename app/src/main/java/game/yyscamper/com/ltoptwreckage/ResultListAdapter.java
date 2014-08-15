package game.yyscamper.com.ltoptwreckage;


import android.app.Notification;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by YuanYu on 14-3-16.
 */
public class ResultListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<WreckageResult> mResults;
    private Wreckage[] mWreckages;
    private int mTarget;
    private LayoutInflater mInflater;
    private View.OnClickListener mBtnClickListener;

    public ResultListAdapter(Context ctx, View.OnClickListener listener)
    {
        this.mContext = ctx;
        mResults = ResultRecorder.Results;
        mWreckages = ResultRecorder.Wreckages;
        mTarget = ResultRecorder.Target;
        mInflater = LayoutInflater.from(ctx);
        mBtnClickListener = listener;
    }

    @Override
    public int getCount()
    {
        return mResults.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mResults.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public int getViewTypeCount()
    {
        return 1;
    }

    @Override
    public int getItemViewType(int position)
    {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.result_list_item, null);
        }

        final WreckageResult result = mResults.get(position);

        TextView viewDelta = (TextView) convertView.findViewById(R.id.textViewDelta);
        TextView viewTotalNum = (TextView) convertView.findViewById(R.id.textViewTotalNum);
        TextView viewResultString = (TextView) convertView.findViewById(R.id.textViewResult);
        TextView viewSeq = (TextView) convertView.findViewById(R.id.textViewSeq);
        Button viewSelect = (Button) convertView.findViewById(R.id.buttonSelect);

        viewDelta.setText("多余经验: " + String.valueOf(result.Value - mTarget));
        viewTotalNum.setText("残骸总数: " + String.valueOf(result.GetTotalNumOfWreckage()));
        viewResultString.setText(WreckageCalculator.GetResultString(result, mWreckages, mTarget));
        viewSeq.setText("" + String.valueOf(position + 1));
        viewSelect.setOnClickListener(mBtnClickListener);

        viewSelect.setTag(new Integer(position));

        return convertView;
    }
}
