package game.yyscamper.com.ltoptwreckage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.prefs.PreferenceChangeEvent;


public class MainActivity extends Activity {

    WreckageListAdapter mWreckageListAdapter = null;
    AutoCompleteTextView mViewTarget;
    AutoCompleteTextView mViewAllowedError;
    Spinner mViewWreckages;
    Button mButtonAddWreckage;
    ListView mListViewSelectedWreckages;
    Button mButtonPresetTarget;
    Button mButtonPresetAllowedError;
    Switch mSwitchAutoDecrease;

    static final String KEY_TARGET = "target";
    static final String KEY_ALLOWED_ERROR = "allowedError";
    static final String KEY_AUTO_DECREASE = "autoDecrease";
    static final String KEY_SELECTED_WRECKAGES = "selectedWreckages";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListViewSelectedWreckages = (ListView)findViewById(R.id.listView);
        mViewTarget = (AutoCompleteTextView)findViewById(R.id.autoCompTarget);
        mViewAllowedError = (AutoCompleteTextView)findViewById(R.id.autoCompAllowedError);
        mViewWreckages = (Spinner)findViewById(R.id.spinnerWreckages);
        mButtonAddWreckage = (Button)findViewById(R.id.buttonAddWreckage);
        mButtonPresetTarget = (Button)findViewById(R.id.buttonPresetTarget);
        mButtonPresetAllowedError = (Button)findViewById(R.id.buttonPresetAllowedError);
        mSwitchAutoDecrease = (Switch)findViewById(R.id.switchAutoDecrease);

        mViewTarget.setSelection(0, mViewTarget.getText().length());

        SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(this);
        int target = setting.getInt(KEY_TARGET, 59160);
        int allowedError = setting.getInt(KEY_ALLOWED_ERROR, 200);
        boolean isAutoDecrease = setting.getBoolean(KEY_AUTO_DECREASE, true);
        Set<String> selectWreckages = setting.getStringSet(KEY_SELECTED_WRECKAGES, null);

        mViewTarget.setText(String.valueOf(target));
        mViewAllowedError.setText(String.valueOf(allowedError));
        mSwitchAutoDecrease.setChecked(isAutoDecrease);

        if (selectWreckages == null)
        {
            ArrayList<Wreckage> defaultWreckages = new ArrayList<Wreckage>();
            defaultWreckages.add(new Wreckage(9600));
            defaultWreckages.add(new Wreckage(6024));
            defaultWreckages.add(new Wreckage(5996));
            defaultWreckages.add(new Wreckage(5328));
            defaultWreckages.add(new Wreckage(4660));
            mWreckageListAdapter = new WreckageListAdapter(this, defaultWreckages);
        }
        else
        {
            mWreckageListAdapter = new WreckageListAdapter(this, ResultRecorder.parseSelectedWreckagesSet(selectWreckages));
        }

        mListViewSelectedWreckages.setAdapter(mWreckageListAdapter);

        Resources res = getResources();
        final String[] predefined_targets = res.getStringArray(R.array.target_pre_defined);
        String[] predefined_wreckages = res.getStringArray(R.array.wreckages_list);
        final String[] predefined_allowedErrors = res.getStringArray(R.array.allowed_errors);

        ArrayAdapter<String> adapterTarget = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, predefined_targets);
        mViewTarget.setAdapter(adapterTarget);

        ArrayAdapter<String> adapterWreckages = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, predefined_wreckages);
        mViewWreckages.setAdapter(adapterWreckages);

        ArrayAdapter<String> adapterAllowedError = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, predefined_allowedErrors);
        mViewAllowedError.setAdapter(adapterAllowedError);

        mButtonAddWreckage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str = mViewWreckages.getSelectedItem().toString();

                if (!str.equalsIgnoreCase("自定义")) {
                    int value = Integer.parseInt(str);
                    mWreckageListAdapter.addWreckage(new Wreckage(value));
                }
                else
                {
                    final EditText editText = new EditText(MainActivity.this);
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("输入自定义经验值")
                            .setView(editText)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int value = Integer.parseInt(editText.getText().toString());
                                    mWreckageListAdapter.addWreckage(new Wreckage(value));
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();

                }
            }
        });

        mButtonPresetTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("选择目标经验")
                       .setIcon(R.drawable.icon32)
                       .setCancelable(true)
                       .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {

                           }
                       })
                       .setItems(predefined_targets, new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               mViewTarget.setText(predefined_targets[which]);
                               mViewTarget.clearFocus();
                               mViewWreckages.requestFocus();
                           }
                       });
                 builder.create().show();
            }
        });

        mButtonPresetAllowedError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dg = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("选择最大允许多余经验")
                        .setIcon(R.drawable.icon32)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setItems(predefined_allowedErrors, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mViewAllowedError.setText(predefined_allowedErrors[which]);
                            }
                        })
                        .create();
                dg.show();
            }
        });
    }

    @Override
    protected void onResume() {
        mViewTarget.setSelection(0, mViewTarget.getText().length());

        if (mSwitchAutoDecrease.isChecked() && ResultRecorder.SelectedResultIndex >= 0)
        {
            WreckageResult result = ResultRecorder.Results.get(ResultRecorder.SelectedResultIndex);
            for (int i = 0; i < ResultRecorder.Wreckages.length; i++)
            {
                ResultRecorder.Wreckages[i].MaxNum -= result.Nums[i];
                if (ResultRecorder.Wreckages[i].MaxNum < 0)
                {
                    ResultRecorder.Wreckages[i].MaxNum = 0;
                }
            }

            ResultRecorder.SelectedResultIndex = -1;
            mWreckageListAdapter.notifyDataSetChanged();
        }

        super.onResume();
    }

    @Override
    protected void onPause() {
        savePreference();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_calc) {
            doCalc();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAlertDialog(String message, String title)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton("OK", new AlertDialog.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void doCalc()
    {
        Wreckage[] wreckages = mWreckageListAdapter.getSelectedWreckages();
        if (wreckages.length <= 0)
        {
            showAlertDialog("你需要选择至少一种残骸！", "错误");
            return;
        }

        int target = Integer.parseInt(mViewTarget.getText().toString());
        if (target <= 0)
        {
            showAlertDialog("你输入的目标经验错误！这必须是一个大于零的整数！", "输入错误");
            return;
        }
        int allowedError = Integer.parseInt(mViewAllowedError.getText().toString());
        if (allowedError  < 0) {
            showAlertDialog("你输入的允许最大多余经验错误！这必须是一个大于或等于零的整数！", "输入错误");
            return;
        }

        ArrayList<WreckageResult> results = WreckageCalculator.DoCalc(wreckages, target, allowedError);
        if (results.size() <= 0)
        {
            showAlertDialog("未能找到合适的残骸组合，建议你增大\"最大多余经验\"或者添加更多数量的残骸！", "提示");
            return;
        }

        ResultRecorder.Results = results;
        ResultRecorder.Target = target;
        ResultRecorder.Wreckages = wreckages;
        ResultRecorder.AllowedError = allowedError;
        ResultRecorder.SelectedResultIndex = -1;
        savePreference(target, allowedError, wreckages);

        Intent intent = new Intent(this, ResultActivity.class);
        startActivity(intent);
    }

    private void savePreference()
    {
        savePreference(Integer.parseInt(mViewTarget.getText().toString()),
                Integer.parseInt(mViewAllowedError.getText().toString()),
                mWreckageListAdapter.getSelectedWreckages());
    }

    private void savePreference(int target, int allowedError, Wreckage[] wreckages)
    {
        SharedPreferences prefer = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefer.edit();
        editor.putInt(KEY_TARGET, target);
        editor.putInt(KEY_ALLOWED_ERROR, allowedError);
        editor.putStringSet(KEY_SELECTED_WRECKAGES, ResultRecorder.convertSelectedWreckagesSet(wreckages));
        editor.putBoolean(KEY_AUTO_DECREASE, mSwitchAutoDecrease.isChecked());
        editor.commit();
    }
}
