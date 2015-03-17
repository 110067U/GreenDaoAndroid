package example.isuru.com.greendaoforandroidstudio.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import greendao.Message;
import parentapp.oac.com.greendaoforandroidstudio.R;
import example.isuru.com.greendaoforandroidstudio.backend.repositories.MessageReceiversRepository;
import example.isuru.com.greendaoforandroidstudio.backend.repositories.MessageRepository;
import example.isuru.com.greendaoforandroidstudio.backend.repositories.ParentRepository;
import example.isuru.com.greendaoforandroidstudio.ui.adapter.DbItemsAdapter;

public class MessageListActivity extends ActionBarActivity {

    private ListView lvItemList;
    private DbItemsAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_message_list);

        lvItemList = (ListView) this.findViewById(R.id.lvItemList);
        messageAdapter = new DbItemsAdapter(MessageListActivity.this);
        lvItemList.setAdapter(messageAdapter);

        setupButtons();
    }

    private void setupButtons() {
        lvItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent editItemIntent = new Intent(MessageListActivity.this, EditMessageActivity.class);
                Message clickedMessage = messageAdapter.getItem(position);
                editItemIntent.putExtra("messageId", clickedMessage.getId());
                startActivity(editItemIntent);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        messageAdapter.updateData(MessageRepository.getAllMessages(MessageListActivity.this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_item:
                createItem();
                return true;

            case R.id.delete_items:
                clearAllItems();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createItem() {
        Intent addMessageActivityIntent = new Intent(MessageListActivity.this, EditMessageActivity.class);
        startActivity(addMessageActivityIntent);
    }

    private void clearAllItems() {
        if (messageAdapter.getCount() == 0) {
            Toast.makeText(MessageListActivity.this, getString(R.string.toast_no_items_to_delete), Toast.LENGTH_SHORT).show();
        } else {
            new AlertDialog.Builder(MessageListActivity.this)
                    .setTitle(getString(R.string.dialog_delete_items_title))
                    .setMessage(R.string.dialog_delete_items_content)
                    .setCancelable(false)
                    .setPositiveButton(R.string.dialog_delete_items_confirm, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MessageRepository.clearMessages(MessageListActivity.this);
                            MessageReceiversRepository.clearMessage_Receivers(MessageListActivity.this);
                            ParentRepository.clearParents(MessageListActivity.this);
                            messageAdapter.updateData(MessageRepository.getAllMessages(MessageListActivity.this));
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton(R.string.dialog_delete_items_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    }).create().show();
        }
    }
}
