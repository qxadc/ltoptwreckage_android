package game.yyscamper.com.ltoptwreckage;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RemoteViews;

import game.yyscamper.com.ltoptwreckage.R;

public class ResultActivity extends Activity {
    private ListView mListViewResults;
    private ResultListAdapter mListResultAdapter;
    private int mCurrPostion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        mListResultAdapter = new ResultListAdapter(this, new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                try {
                    mCurrPostion = ((Integer) v.getTag()).intValue();
                }
                catch (Exception err)
                {
                    return;
                }
                sendToNotifyBar();
            }
        });
        mListViewResults = (ListView)findViewById(R.id.listView);
        mListViewResults.setAdapter(mListResultAdapter);

        this.registerForContextMenu(mListViewResults);

        mListViewResults.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrPostion = position;
                return false;
            }
        });

        mListViewResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrPostion = position;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v == mListViewResults)
        {
            menu.add(0, 1, Menu.NONE, "选择并添加到通知栏");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId())
        {
            case 1: //Edit pay entry
                sendToNotifyBar();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void sendToNotifyBar()
    {
        if (mCurrPostion < 0)
            return;

        ResultRecorder.SelectedResultIndex = mCurrPostion;

        WreckageResult result = ResultRecorder.Results.get(mCurrPostion);
        String strResult = WreckageCalculator.GetResultString(result, ResultRecorder.Wreckages, ResultRecorder.Target);

        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification n = new Notification(R.drawable.icon, strResult, System.currentTimeMillis());
        n.flags = Notification.FLAG_AUTO_CANCEL;
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        //PendingIntent
        PendingIntent contentIntent = PendingIntent.getActivity(
                this,
                R.string.app_name,
                i,
                PendingIntent.FLAG_UPDATE_CURRENT);

        /*
        n.setLatestEventInfo(
                this,
                "雷霆残骸优化",
                strResult + strResult + strResult,
                contentIntent);
                */

        RemoteViews rv = new RemoteViews(this.getPackageName(), R.layout.notification_item_view);
        rv.setTextViewText(R.id.textViewDelta, " 多余经验: " + String.valueOf(result.Value - ResultRecorder.Target));
        rv.setTextViewText(R.id.textViewTarget, "目标经验: " + String.valueOf(ResultRecorder.Target));
        rv.setTextViewText(R.id.textViewResult, " " + strResult);
        rv.setImageViewResource(R.id.imageView, R.drawable.icon);
        n.contentView = rv;
        n.contentIntent = contentIntent;

        nm.notify(R.string.app_name, n);
    }
}
