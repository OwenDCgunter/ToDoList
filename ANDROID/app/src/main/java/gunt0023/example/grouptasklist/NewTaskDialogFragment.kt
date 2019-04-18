package gunt0023.example.grouptasklist

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.EditText


class NewTaskDialogFragment: DialogFragment() {

    interface NewTaskDialogListener{
        fun onDialogPositiveClick(dialog: DialogFragment, taskDetails: String)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    var newTaskDialogListener: NewTaskDialogListener? = null

    companion object{
        fun newInstance(title: Int, selected: String?): NewTaskDialogFragment {

            val newTaskDialogFragment = NewTaskDialogFragment()
            val args = Bundle()
            args.putInt("dialog_title", title)
            args.putString("selected_item", selected)
            newTaskDialogFragment.arguments = args

            return newTaskDialogFragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title = arguments!!.getInt("dialog_title")
        val builder = AlertDialog.Builder(this.activity!!)
        builder.setTitle(title)
        val dialogView = activity!!.layoutInflater.inflate(R.layout.dialog_new_task,null)
        val task = dialogView.findViewById<EditText>(R.id.task)
        val selectedText = arguments!!.getString("selected_item")
        task.setText(selectedText)

        builder.setView(dialogView).setPositiveButton(R.string.save) {dialog, id ->
            newTaskDialogListener?.onDialogPositiveClick(this,
                task.text.toString())}

        builder.setView(dialogView).setNegativeButton(android.R.string.cancel) {dialog, id ->
            newTaskDialogListener?.onDialogNegativeClick(this)}

        return builder.create()
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        try {
            newTaskDialogListener = activity as NewTaskDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() +
                    " must implement NewTaskDialogListener")
        }
    }


}