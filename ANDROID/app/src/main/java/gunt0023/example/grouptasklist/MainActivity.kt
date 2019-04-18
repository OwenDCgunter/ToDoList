package gunt0023.example.grouptasklist

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.migration.Migration
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import gunt0023.example.grouptasklist.TodoListDBContract.DATABASE_NAME


import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), NewTaskDialogFragment.NewTaskDialogListener {

    private val fm = supportFragmentManager

    private var todoListItems = ArrayList<Task>()
    private var listView: ListView? = null
    private var listAdapter: TaskListAdapter? = null
    private var showMenuItems: Boolean = false
    private var selectedItem: Int = -1
    private var database: AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        fab.setOnClickListener {showNewTaskUI() }
        listView = findViewById(R.id.list_view)

        database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, DATABASE_NAME)
            .addMigrations(object : Migration(TodoListDBContract.DATABASE_VERSION - 1, TodoListDBContract.DATABASE_VERSION){
            override fun migrate(database: SupportSQLiteDatabase){

            }
        }).build()






        populateListView()

        listView?.onItemClickListener =
                AdapterView.OnItemClickListener(function =
                { parent, view, position, id ->
                    showUpdateTaskUI(
                        position
                    )
                })

    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.task_list_menu, menu)
        val editItem = menu!!.findItem(R.id.edit_item)
        val deleteItem = menu!!.findItem(R.id.delete_item)
        val completeItem =menu!!.findItem(R.id.mark_as_done)
        if(showMenuItems){
            editItem.isVisible = true
            deleteItem.isVisible = true
            completeItem.isVisible = true
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(-1 != selectedItem) {
            if(R.id.edit_item == item?.itemId) {
                val updateFragment =
                    NewTaskDialogFragment.newInstance(R.string.update_task_dialog_title,
                        todoListItems[selectedItem].taskDetails)
                updateFragment.show(fm, "updatetask")
                hideMenu()
            } else if (R.id.delete_item == item?.itemId) {
                val selectedTask = todoListItems[selectedItem]
                DeleteTaskAsyncTask(database, selectedTask).execute()

                todoListItems.removeAt(selectedItem)
                listAdapter?.notifyDataSetChanged()
                selectedItem = -1
                hideMenu()
                Snackbar.make(fab, "Task deleted successfully",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show()
            }
            else if(R.id.mark_as_done == item?.itemId){


                val selectedTask = todoListItems[selectedItem]
                todoListItems[selectedItem].completed = true

                UpdateTaskAsyncTask(database, selectedTask).execute()

                listAdapter?.notifyDataSetChanged()
                selectedItem = -1
                hideMenu()
                Snackbar.make(fab, "Task has been marked as completed",
                    Snackbar.LENGTH_LONG).setAction("Action", null).show()
            }
        }

        return super.onOptionsItemSelected(item)

    }

    private class RetrieveTasksAsyncTask(private val database: AppDatabase?) : AsyncTask<Void, Void, List<Task>>() {
        override fun doInBackground(vararg params: Void): List<Task>?
        {
            return database?.taskDao()?.retrieveTaskList()
        }
    }

    private class AddTaskAsyncTask(private val database: AppDatabase?, private val newTask: Task) : AsyncTask<Void, Void, Long>()
    {
        override fun doInBackground(vararg params: Void): Long?
        {
            return database?.taskDao()?.addNewTask(newTask)
        }
    }

    private class UpdateTaskAsyncTask(private val database: AppDatabase?, private val selectedTask: Task) : AsyncTask<Void, Void, Unit>()
    {
        override fun doInBackground(vararg params: Void)
        {
             database?.taskDao()?.updateTask(selectedTask)
        }
    }

    private class DeleteTaskAsyncTask(private val database: AppDatabase?, private val selectedTask: Task) : AsyncTask<Void, Void, Unit>() {
        override fun doInBackground(vararg params: Void)
        {
            database?.taskDao()?.deleteTask(selectedTask)
        }
    }


    fun showNewTaskUI(){

        val newFragment = NewTaskDialogFragment.newInstance(R.string.add_new_task_dialog_title,null)
        newFragment.show(fm, "newtask")
    }
    override fun onDialogPositiveClick(dialog: DialogFragment, taskDetails:String) {


        if("newtask" == dialog.tag) {
            //todoListItems.add(Task(taskDetails, null))
            var addNewTask = Task(taskDetails)
            addNewTask.taskId = AddTaskAsyncTask(database, addNewTask).execute().get()
            todoListItems.add(addNewTask)

            listAdapter?.notifyDataSetChanged()


            Snackbar.make(fab, "Task Added Successfully", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }
        else if("updatetask" == dialog.tag){

            val selectedTask = todoListItems[selectedItem]

            UpdateTaskAsyncTask(database, selectedTask).execute()

            todoListItems[selectedItem].taskDetails = taskDetails

            listAdapter?.notifyDataSetChanged()
            selectedItem = -1
            Snackbar.make(fab, "Task Updated Successfully",
                Snackbar.LENGTH_LONG).setAction("Action", null).show()

        }
        /* if(todoListItems.isEmpty())
               {
                   Log.d("success123","success1234")
               }
               else
               {
                   Log.d("failer432", "failer4321")
               }*/
    }

    private fun hideMenu(){
        showMenuItems = false
        invalidateOptionsMenu()
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        Snackbar.make(fab, "Task cancelled and menu items removed", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        hideMenu()
    }

  private fun populateListView(){
        todoListItems = RetrieveTasksAsyncTask(database).execute().get() as ArrayList<Task>
        listAdapter = TaskListAdapter(this,todoListItems)
        listView?.adapter = listAdapter


    }

    private fun showUpdateTaskUI(selected: Int){
        selectedItem = selected
        showMenuItems = true
        invalidateOptionsMenu()

    }
}
