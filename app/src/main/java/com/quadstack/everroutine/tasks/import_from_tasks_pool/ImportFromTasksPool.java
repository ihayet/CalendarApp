package com.quadstack.everroutine.tasks.import_from_tasks_pool;

import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quadstack.everroutine.R;
import com.quadstack.everroutine.Utility;
import com.quadstack.everroutine.database.DBMeta;
import com.quadstack.everroutine.database.DBModule;
import com.quadstack.everroutine.double_drawer.DoubleDrawerActivity;
import com.quadstack.everroutine.tasks.TaskAddEdit;
import com.quadstack.everroutine.tasks_pool.*;
import com.quadstack.everroutine.tasks_pool.Adapter;
import com.quadstack.everroutine.tasks_pool.DataModel;

public class ImportFromTasksPool extends DoubleDrawerActivity
{
    private View tasksPoolView;
    private Adapter tasksPoolAdapter;

    private ImageView title;
    private ListView importListView;
    private TextView listEmpty;

    public static String taskName;
    public static int taskCategory, taskPriority, taskAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        tasksPoolAdapter = new Adapter(ImportFromTasksPool.this, R.layout.tasks_pool_row);

        tasksPoolView = super.setContentLayout(R.layout.activity_import_from_tasks_pool, R.layout.custom_actionbar_layout);

        title = (ImageView)tasksPoolView.findViewById(R.id.importTasksPoolTitle);
        importListView = (ListView)tasksPoolView.findViewById(R.id.importTasksPoolListView);
        listEmpty = (TextView)tasksPoolView.findViewById(R.id.importTasksPoolListEmpty);

        importListView.setAdapter(tasksPoolAdapter);

        addData();

        setDynamicLayout();
        addListeners();

        checkEmptyList();

        taskName="";
        taskCategory=0;
        taskPriority=1;
        taskAlarm=0;
    }

    @Override
    protected void setDynamicLayout()
    {
        title.setImageBitmap(Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.import_title, Utility.titleWidth, Utility.titleHeight));

        //------------------------------------------------------------------------------------------------------------------------------------------------------

        listEmpty.getLayoutParams().width = Utility.rowWidth;
        listEmpty.getLayoutParams().height = Utility.rowHeight;
        ((RelativeLayout.LayoutParams)listEmpty.getLayoutParams()).setMargins(Utility.rowLeft, ((Utility.screenHeight/2) - Utility.rowHeight), 0, 0);

        listEmpty.setBackground(new BitmapDrawable(getResources(), Utility.decodeSampledBitmapFromResource(getResources(), R.drawable.torn_paper, Utility.rowWidth, Utility.rowHeight)));
        listEmpty.setPadding(100, 0, 100, 0);
        listEmpty.setTextSize(Utility.fontSize);
    }

    @Override
    protected void addListeners()
    {
        importListView.setOnItemClickListener(new ListView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                com.quadstack.everroutine.tasks_pool.DataModel task = (DataModel)tasksPoolAdapter.getItem(position);

                taskName = task.getItemName();
                taskCategory = task.getItemCategory();
                taskPriority = task.getTaskPriority();
                taskAlarm = (task.isTaskAlarm()?1:0);

                TaskAddEdit.importFromTasksPool(taskName, taskCategory, taskPriority, taskAlarm);

                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    private void addData()
    {
        int id, taskCategory, taskPriority, taskAlarm;
        String taskName;

        Cursor cursor = new DBModule(getApplicationContext()).getTasksPool();

        if(cursor.moveToFirst())
        {
            do
            {
                id = cursor.getInt(cursor.getColumnIndex(DBMeta.TasksPool.id));
                taskCategory = cursor.getInt(cursor.getColumnIndex(DBMeta.TasksPool.task_category));
                taskPriority = cursor.getInt(cursor.getColumnIndex(DBMeta.TasksPool.task_priority));
                taskAlarm = cursor.getInt(cursor.getColumnIndex(DBMeta.TasksPool.task_alarm));
                taskName = cursor.getString(cursor.getColumnIndex(DBMeta.TasksPool.task_name));

                tasksPoolAdapter.add(new com.quadstack.everroutine.tasks_pool.DataModel(id, taskName, taskCategory, taskPriority, (taskAlarm==1?true:false), getImgResource(taskCategory)));
            }
            while(cursor.moveToNext());
        }
    }

    private static int getImgResource(int taskCategory)
    {
        switch(taskCategory)
        {
            case 0:
                return R.drawable.official;
            case 1:
                return R.drawable.academic;
            case 2:
                return R.drawable.miscellaneous;
            default:
                break;
        }

        return 0;
    }

    protected void checkEmptyList()
    {
        if(tasksPoolAdapter.getCount() == 0)
        {
            listEmpty.setVisibility(View.VISIBLE);
            importListView.setVisibility(View.INVISIBLE);
        }
        else
        {
            listEmpty.setVisibility(View.INVISIBLE);
            importListView.setVisibility(View.VISIBLE);
        }
    }
}
