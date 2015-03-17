package example.isuru.com.greendaoforandroidstudio.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import greendao.Message;
import greendao.messageReceivers;
import example.isuru.com.greendaoforandroidstudio.ui.activity.EditMessageActivity;
import parentapp.oac.com.greendaoforandroidstudio.R;

public class DbItemsAdapter extends ArrayAdapter<Message> {

    private LayoutInflater inflater;
    private Context context;
    private String orien;
    private int[] dims;
    TextView tv;

    public DbItemsAdapter(Context context) {
        super(context, 0);
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dims = calculateScreenDimensions();
        if(dims[0]<dims[1]){
            orien = "portrait";
        }else{
            orien = "landscape";
        }
        Toast.makeText(context,orien,Toast.LENGTH_SHORT).show();
    }

    public void updateData(List<Message> messageList) {
        this.clear();
        for (Message aMessageList : messageList) {
            add(aMessageList);
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_message, null);
            viewHolder = new ViewHolder();
            viewHolder.root = (LinearLayout) convertView.findViewById(R.id.boxItem);
            viewHolder.imgIcon = (ImageView)convertView.findViewById(R.id.imgIcon);
            viewHolder.txtCampus = (TextView)convertView.findViewById(R.id.txtCampus);
            viewHolder.txtRoom = (TextView)convertView.findViewById(R.id.txtRoom);
            viewHolder.txtCreatedBy = (TextView)convertView.findViewById(R.id.txtCreatedBy);
            viewHolder.txtCreatedDate = (TextView)convertView.findViewById(R.id.txtCreatedDate);
            viewHolder.txtDateSent = (TextView)convertView.findViewById(R.id.txtDateSent);
            viewHolder.txtStatus = (TextView)convertView.findViewById(R.id.txtStatus);
            viewHolder.btnEdit = (Button)convertView.findViewById(R.id.btnEdit);
            viewHolder.txtMsgBody = (TextView)convertView.findViewById(R.id.txtMsgBody);
            viewHolder.gridAttachments = (GridLayout)convertView.findViewById(R.id.gridAttachments);
            viewHolder.receiverList = (LinearLayout)convertView.findViewById(R.id.parentLayout);
            viewHolder.receiverListParent = (HorizontalScrollView)convertView.findViewById(R.id.parentLayoutOuter);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        editBackground(position, viewHolder);
        fillViewWithData(position, viewHolder);

        onButtonClick(position, viewHolder);

        return convertView;
    }

    public void onButtonClick(final int position, ViewHolder viewHolder){
        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editItemIntent = new Intent(context, EditMessageActivity.class);
                context.startActivity(editItemIntent);

            }
        });
    }

    private void editBackground(int position, ViewHolder viewHolder) {
            viewHolder.root.setBackgroundColor(context.getResources().getColor(R.color.white));
            viewHolder.root.setBackgroundResource(R.drawable.bg_parent_rounded_corner);

    }

    private void fillViewWithData(int position, ViewHolder viewHolder) {

//        if(orien.equals("portrait")) {
//            viewHolder.receiverListParent.getLayoutParams().height = HorizontalScrollView.LayoutParams.WRAP_CONTENT;
//            viewHolder.receiverListParent.getLayoutParams().width = 620;
//        }else if(orien.equals("landscape")){
//            viewHolder.receiverListParent.getLayoutParams().height = HorizontalScrollView.LayoutParams.WRAP_CONTENT;
//            viewHolder.receiverListParent.getLayoutParams().width = 1480;
//        }

        //change image icon according to msg type
        if(getItem(position).getType().equals("Grow Story")||getItem(position).getType().equals("grow story")){
            viewHolder.imgIcon.setBackgroundResource(R.drawable.grow_story);
        }else if(getItem(position).getType().equals("Grow Snapshot")||getItem(position).getType().equals("grow snapshot")){
            viewHolder.imgIcon.setBackgroundResource(R.drawable.grow_snapshot);
        }else if(getItem(position).getType().equals("Daily Diary")||getItem(position).getType().equals("daily diary")){
            viewHolder.imgIcon.setBackgroundResource(R.drawable.daily_diary);
        }else if(getItem(position).getType().equals("Newsletter")||getItem(position).getType().equals("newsletter")){
            viewHolder.imgIcon.setBackgroundResource(R.drawable.newsletter);
        }else{
            viewHolder.imgIcon.setBackgroundResource(R.drawable.icon_default);
        }
        viewHolder.txtCampus.setText("Campus: "+getItem(position).getCampus());
        viewHolder.txtRoom.setText("Room: "+getItem(position).getClassRoom());
        viewHolder.txtCreatedBy.setText("CreatedBy: "+getItem(position).getCreatedBy());
        viewHolder.txtCreatedDate.setText("CreatedDate: "+getItem(position).getCreatedDate());
        viewHolder.txtDateSent.setText("Date Sent: "+getItem(position).getSentDate());
        viewHolder.txtStatus.setText("Status: "+getItem(position).getStatus());


        List<messageReceivers> newReceiverListItems = getItem(position).getRecipients();

        String recist ="";

        for(int i=0; i<newReceiverListItems.size(); i++){
            recist = recist + " " + newReceiverListItems.get(i).getParent().getName().toString();
        }



        final LinearLayout.LayoutParams lin_lay = new LinearLayout.LayoutParams(
                                           LinearLayout.LayoutParams.WRAP_CONTENT,
                                           LinearLayout.LayoutParams.WRAP_CONTENT);

        String[] textArray = new String[newReceiverListItems.size()];

        for(int i =0; i<newReceiverListItems.size();i++){
            textArray[i] = newReceiverListItems.get(i).getParent().getName().toString();
        }

        int length=textArray.length;

        viewHolder.txtMsgBody.setText("size= "+newReceiverListItems.size()+"  "+recist+ "  length= "+length);

        for(int i=0;i<length;i++){
            tv=new TextView(context);
            tv.setLayoutParams(lin_lay);
            tv.setText(textArray[i]);
            tv.setPadding(17, 11,17, 11);
            tv.setBackgroundResource(R.drawable.bg_bubble_rounded_corner);
            viewHolder.receiverList.addView(tv);
            viewHolder.receiverList.invalidate();
        }

        if(viewHolder.receiverList.getChildCount() > newReceiverListItems.size()){
//            viewHolder.receiverList.removeViews(newReceiverListItems.size(),(viewHolder.receiverList.getChildCount()-newReceiverListItems.size()));

            viewHolder.receiverList.removeAllViews();

            for(int i=0;i<length;i++){
                tv=new TextView(context);
                tv.setLayoutParams(lin_lay);
                tv.setText(textArray[i]);
                tv.setPadding(17, 9,17, 9);
                tv.setBackgroundResource(R.drawable.bg_bubble_rounded_corner);
                viewHolder.receiverList.addView(tv);
                viewHolder.receiverList.invalidate();
            }
        }



    }


    private int[] calculateScreenDimensions() {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        float one_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, this.context.getResources().getDisplayMetrics());
        int dimension[] = new int[2];
        dimension[0] = size.x;  //width
        dimension[1] = size.y;  //height
        return dimension;
    }

    static class ViewHolder {
        LinearLayout root;

        //Left Pane
        ImageView imgIcon;
        TextView txtCampus;
        TextView txtRoom;
        TextView txtCreatedBy;
        TextView txtCreatedDate;
        TextView txtDateSent;
        TextView txtStatus;
        Button btnEdit;

        //Right Pane
        TextView txtMsgBody;
        GridLayout gridAttachments;
        LinearLayout receiverList;
        HorizontalScrollView receiverListParent;
    }


}
