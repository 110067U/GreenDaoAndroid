package example.isuru.com.greendaoforandroidstudio.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import greendao.Message;
import greendao.Parent;
import greendao.messageReceivers;
import example.isuru.com.greendaoforandroidstudio.backend.repositories.MessageReceiversRepository;
import example.isuru.com.greendaoforandroidstudio.backend.repositories.MessageRepository;
import example.isuru.com.greendaoforandroidstudio.backend.repositories.ParentRepository;
import parentapp.oac.com.greendaoforandroidstudio.R;

public class EditMessageActivity extends ActionBarActivity {

    private Button btnSave;
    private EditText etBoxCampus;
    private EditText etBoxRoom;
    private EditText etBoxType;
    private EditText etBoxCreatedBy;
    private EditText etBoxMsgBody;
    private EditText etBoxR1;
    private long messageId;
    private Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_edit_message);

        btnSave     = (Button) findViewById(R.id.btnSave);

        etBoxCampus     = (EditText) findViewById(R.id.etBoxCampus);
        etBoxRoom       = (EditText) findViewById(R.id.etBoxRoom);
        etBoxType       = (EditText) findViewById(R.id.etBoxType);
        etBoxCreatedBy  = (EditText) findViewById(R.id.etBoxCreatedBy);
        etBoxMsgBody    = (EditText) findViewById(R.id.etBoxMsgBody);
        etBoxR1         = (EditText) findViewById(R.id.etBoxR1);

        if (getIntent() != null && getIntent().getExtras() != null) {
            messageId = getIntent().getExtras().getLong("messageId");
            message = MessageRepository.getMessageForId(EditMessageActivity.this, messageId);
        }

        setupButtons();
        fillViewWithData();
    }

    private void setupButtons() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (message == null) {
                        message = new Message();
                    } else {
                        message.setId(messageId);
                    }
                    message.setCampus(etBoxCampus.getText().toString());
                    message.setClassRoom(etBoxRoom .getText().toString());
                    message.setType(etBoxType.getText().toString());
                    message.setCreatedBy(etBoxCreatedBy.getText().toString());
                    message.setMessageBody( etBoxMsgBody.getText().toString());
                    message.setStatus("Draft");
                    message.setCreatedDate(null);
                    message.setSentDate(null);


                    MessageRepository.insertOrUpdate(EditMessageActivity.this, message);

                   //get parent list into a string array
                    String parentsString = etBoxR1.getText().toString();

                    int commacount=0;

                    for(int i=0; i<parentsString.length(); i++){
                        if(parentsString.charAt(i)==' '){
                            commacount++;
                        }
                    }

                    String[] parentList = new String[commacount+1];
                    String temp="";
                    int itr = 0;

                    for(int i=0; i<parentsString.length(); i++){

                        if(parentsString.charAt(i)==' '){
                            parentList[itr] = temp;
                            temp = "";
                            itr++;
                        }else{
                            temp = temp +parentsString.charAt(i);
                        }
                    }
                       parentList[commacount] = temp;

                //create parent and messageReceiver entries
                    for(int i=0; i<parentList.length; i++){
                        Parent p = new Parent();
                        p.setName(parentList[i]);
                        ParentRepository.insertOrUpdate(EditMessageActivity.this, p);
                        messageReceivers mr = new messageReceivers();
                        mr.setMessageId(message.getId());
                        mr.setParentId(p.getId());
                        MessageReceiversRepository.insertOrUpdate(EditMessageActivity.this, mr);
                    }

                   finish();
            }
        });
    }

//    private boolean validateFields() {

//        if (etBoxName.getText().length() == 0) {
//            etBoxName.setError(getString(R.string.error_cannot_be_empty));
//            return false;
//        }
//        if (etBoxSlots.getText().length() == 0) {
//            etBoxSlots.setError(getString(R.string.error_cannot_be_empty));
//            return false;
//        }
//        try {
//            Integer.parseInt(etBoxSlots.getText().toString());
//        } catch (Exception e) {
//            etBoxSlots.setError(getString(R.string.error_must_be_number));
//            return false;
//        }
//
//        etBoxName.setError(null);
//        etBoxSlots.setError(null);
//        return  true;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (message != null) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.edit_item_menu, menu);
            return true;
        } else {
            return super.onCreateOptionsMenu(menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_item:
                deleteItem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteItem() {
        new AlertDialog.Builder(EditMessageActivity.this)
                .setTitle(getString(R.string.dialog_delete_item_title))
                .setMessage(R.string.dialog_delete_item_content)
                .setCancelable(false)
                .setPositiveButton(R.string.dialog_delete_items_confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MessageRepository.deleteMessageWithId(EditMessageActivity.this, messageId);
                        //how to delete row in message_parent table with "message_id" = "messageID"

//                        ArrayList<Integer> receiverIds = new ArrayList<Integer>();
//
//                        List<messageReceivers> receivers  = message.getRecipients();
//
//                        for(int i=0; i<receivers.size(); i++){
//                            receiverIds.add(receivers.get(i).getId().intValue());
//                        }
//
//                        for(int i=0; i<receiverIds.size(); i++){
//                            MessageReceiversRepository.deleteMessage_ParentWithId(EditMessageActivity.this, receiverIds.get(i));
//                        }


                        dialog.cancel();
                        finish();
                    }
                })
                .setNegativeButton(R.string.dialog_delete_items_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).create().show();
    }

    private void fillViewWithData() {
        if (message != null) {

            etBoxCampus.setText(message.getCampus());
            etBoxRoom.setText(message.getClassRoom());
            etBoxType.setText(message.getType());
            etBoxCreatedBy.setText(message.getCreatedBy());
            etBoxMsgBody.setText(message.getMessageBody());

        }

    }

}
