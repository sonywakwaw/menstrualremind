package com.toni.android.menstruation;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

public class AlertManager extends Activity
{
  private String mcdate_value = "";
  private String period_value = "";
  private String remind_value = "";
  private int mYear;
  private int mMonth;
  private int mDay;
  private DatePicker DatePicker01;
  private EditText EditText01;
  private Button Button02;
  private TimePicker TimePicker01;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_1);

    DatePicker01 = (DatePicker) findViewById(R.id.DatePicker01);
    EditText01 = (EditText) this.findViewById(R.id.EditText01);
    Button02 = (Button) this.findViewById(R.id.Button02);
    TimePicker01 = (TimePicker) findViewById(R.id.TimePicker01);

    Bundle bunde = this.getIntent().getExtras();
    mcdate_value = bunde.getString(MenstrualRem.mcdate_key);
    period_value = bunde.getString(MenstrualRem.period_key);
    remind_value = bunde.getString(MenstrualRem.remind_key);

    EditText01.setText(period_value);

    Calendar calendar = Calendar.getInstance();
    if (mcdate_value != null)
    {
      mYear = Integer.parseInt(mcdate_value.substring(0, 4));
      mMonth = Integer.parseInt(mcdate_value.substring(4, 6)) - 1;
      mDay = Integer.parseInt(mcdate_value.substring(6, 8));
    } else
    {
      mYear = calendar.get(Calendar.YEAR);
      mMonth = calendar.get(Calendar.MONTH);
      mDay = calendar.get(Calendar.DAY_OF_MONTH);
    }
    DatePicker01.init(mYear, mMonth, mDay, null);

    if (remind_value != null && remind_value.length() == 4)
    {
      TimePicker01.setCurrentHour(Integer.parseInt(remind_value
          .substring(0, 2)));
      TimePicker01.setCurrentMinute(Integer.parseInt(remind_value
          .substring(2, 4)));
    }
    Button02.setOnClickListener(new Button.OnClickListener()
    {

      @Override
      public void onClick(View arg0)
      {

        FileOutputStream fos;
        try
        {
          int m = DatePicker01.getMonth() + 1;
          String strM = m >= 10 ? "" + m : "0" + m;
          int d = DatePicker01.getDayOfMonth();
          String strD = d >= 10 ? "" + d : "0" + d;
          mcdate_value = "" + DatePicker01.getYear() + "" + strM
              + "" + strD;

          period_value = EditText01.getText().toString();

          int h = TimePicker01.getCurrentHour();
          String strH = h >= 10 ? "" + h : "0" + h;
          int mu = TimePicker01.getCurrentMinute();
          String strMu = mu >= 10 ? "" + mu : "0" + mu;
          remind_value = strH + strMu;

          fos = openFileOutput(MenstrualRem.fileName,
              MODE_WORLD_WRITEABLE);
          BufferedOutputStream bos = new BufferedOutputStream(fos);

          String txt = MenstrualRem.mcdate_key + "=" + mcdate_value;
          bos.write(txt.getBytes());

          bos.write(new String("\n").getBytes());
          txt = MenstrualRem.period_key + "=" + period_value;
          bos.write(txt.getBytes());

          bos.write(new String("\n").getBytes());
          txt = MenstrualRem.remind_key + "=" + remind_value;
          bos.write(txt.getBytes());

          bos.close();
          fos.close();
        } catch (FileNotFoundException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (IOException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }

        Intent receiverIntent = new Intent(AlertManager.this,
            AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(
        		AlertManager.this, 1, receiverIntent, 0);
        AlarmManager am;
        am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(sender);

        int times = 24 * 60 * 60 * 1000;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, TimePicker01
            .getCurrentHour());
        calendar.set(Calendar.MINUTE, TimePicker01
            .getCurrentMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long triggerTime = calendar.getTimeInMillis();

        am.setRepeating(AlarmManager.RTC_WAKEUP, triggerTime,
            times, sender);

        Intent intent = new Intent();
        intent.setClass(AlertManager.this, MenstrualRem.class);
        startActivity(intent);
        finish();
      }
    });
  }

}
