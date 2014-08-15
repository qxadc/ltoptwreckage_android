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
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by YuanYu on 14-3-16.
 */
public class WreckageListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Wreckage> mWreckages;
    private LayoutInflater mInflater;

    public WreckageListAdapter(Context ctx, ArrayList<Wreckage> wreckages)
    {
        this.mContext = ctx;
        this.mWreckages = wreckages;
        mInflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount()
    {
        return mWreckages.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mWreckages.get(position);
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
            convertView = mInflater.inflate(R.layout.wreckage_list_item, null);
        }

        final Wreckage wreckage = mWreckages.get(position);

        TextView viewValue = (TextView) convertView.findViewById(R.id.textViewValue);
        final TextView viewMaxNum = (EditText) convertView.findViewById(R.id.editTextMaxNum);
        Button btnIncrease = (Button) convertView.findViewById(R.id.buttonIncrease);
        Button btnDecrease = (Button) convertView.findViewById(R.id.buttonDecrease);
        Button btnRemote = (Button) convertView.findViewById(R.id.buttonRemove);

        viewValue.setText(String.valueOf(wreckage.Value));
        viewMaxNum.setText(String.valueOf(wreckage.MaxNum));

        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wreckage.MaxNum < 32) {
                    wreckage.MaxNum++;
                    viewMaxNum.setText(String.valueOf(wreckage.MaxNum));
                }
            }
        });

        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wreckage.MaxNum > 0) {
                    wreckage.MaxNum--;
                    viewMaxNum.setText(String.valueOf(wreckage.MaxNum));
                }
            }
        });

        btnRemote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWreckages.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public Wreckage[] getSelectedWreckages()
    {
        ArrayList<Wreckage> lst = new ArrayList<Wreckage>();
        for (Wreckage wreckage : mWreckages)
        {
            if (wreckage.MaxNum > 0)
                lst.add(wreckage);
        }

        /*
        Collections.sort(lst, new Comparator<Wreckage>() {
            @Override
            public int compare(Wreckage w1, Wreckage w2) {
                if (w1.Value > w2.Value)
                    return -1;
                else if (w2.Value < w1.Value)
                    return 1;
                else
                    return 0;
            }
        });
        */

        Wreckage[] arr = new Wreckage[lst.size()];
        int i = 0;
        for (Wreckage wreckage : lst)
        {
            arr[i++] = wreckage;
        }

        return arr;
    }

    public void addWreckage(Wreckage w)
    {
        for (Wreckage wreckage : mWreckages)
        {
            if (wreckage.Value == w.Value)
                return;
        }

        mWreckages.add(w);

        Collections.sort(mWreckages, new Comparator<Wreckage>() {
            @Override
            public int compare(Wreckage w1, Wreckage w2) {
                if (w1.Value > w2.Value)
                    return -1;
                else if (w2.Value < w1.Value)
                    return 1;
                else
                    return 0;
            }
        });

        notifyDataSetChanged();
        return;
    }
}
