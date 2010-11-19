package com.toni.android.menstruation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver
{
  private String mcdate_value = "";
  private String period_value = "";
  private boolean isExit = true;

  @Override
  public void onReceive(Context arg0, Intent arg1)
  {
    // TODO Auto-generated method stub
    getFileDate(arg0);
    if (isExit)
    {
      String nDate = DateUtil.getNextDate(mcdate_value, Integer
          .parseInt(period_value), "yyyy/MM/dd");
      int days = DateUtil.computerDiffDate(DateUtil
          .getDateTime(DateUtil.getNextDate(mcdate_value, Integer
              .parseInt(period_value), "yyyyMMdd")), System
          .currentTimeMillis());
      String msg = arg0.getResources().getString(
          R.string.strMessage2);
      msg += " "+nDate;
      msg += "\n";
      if (days >= 0)
      {
        msg += arg0.getResources().getString(R.string.strMessage5);
        msg += " "+days+" ";
        msg += arg0.getResources().getString(R.string.strMessage7);
      } else
      {
        msg += arg0.getResources().getString(R.string.strMessage8);
        msg += " "+Math.abs(days)+" ";
        msg += arg0.getResources().getString(R.string.strMessage9);
      }

      showToast(arg0, msg);
    }
  }

  private void getFileDate(Context arg0)
  {
    String msg = "";
    Properties p = new Properties();
    try
    {
      p.load(arg0.openFileInput(MenstrualRem.fileName));
      mcdate_value = p.getProperty(MenstrualRem.mcdate_key);
      period_value = p.getProperty(MenstrualRem.period_key);
    } catch (FileNotFoundException e)
    {
      isExit = false;
      msg = e.getMessage();
      e.printStackTrace();
    } catch (IOException e)
    {
      isExit = false;
      msg = e.getMessage();
      e.printStackTrace();
    }
    if (msg.length() > 0)
    {
      showToast(arg0, msg);
    }
  }

  private void showToast(Context arg0, String msg)
  {
    Toast.makeText(arg0, msg, Toast.LENGTH_LONG).show();
  }
}
