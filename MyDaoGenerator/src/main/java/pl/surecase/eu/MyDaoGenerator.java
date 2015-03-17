package pl.surecase.eu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class MyDaoGenerator {

    public static void main(String args[]) throws Exception {

        Schema schema = new Schema(9, "greendao");

        //MessageTable
        Entity messageTable = schema.addEntity("Message");
        messageTable.addIdProperty().getProperty();
        messageTable.addStringProperty("Type");
        messageTable.addStringProperty("Campus");
        messageTable.addStringProperty("ClassRoom");
        messageTable.addStringProperty("CreatedBy");
        messageTable.addDateProperty("CreatedDate");
        messageTable.addStringProperty("Status");
        messageTable.addDateProperty("SentDate");
        messageTable.addStringProperty("MessageBody");

        //Receiver Table
        Entity receiverTable = schema.addEntity("Parent");
        receiverTable.addIdProperty();   //parent id
        receiverTable.addStringProperty("name");   // parent name

        /*
        * A Relation entity - this captures the join table
        * between messages and receivers
        */

        Entity messageReceivers = schema.addEntity("messageReceivers");
        messageReceivers.addIdProperty();


    /*
    * Add the messageId to the join table and setup the Foreign Key relationship
    */

        Property messageId = messageReceivers.addLongProperty("messageId").notNull().getProperty();
        ToMany messageToReceivers = messageTable.addToMany(messageReceivers, messageId);
        messageReceivers.addToOne(messageTable, messageId);
        messageToReceivers.setName("recipients");

    /*
    * Add the recipient ID (receiver ID) to the join table and setup the Foreign Key relationship
    */
        Property recipientId = messageReceivers.addLongProperty("parentId").notNull().getProperty();
        ToMany recipientsToMessage = receiverTable.addToMany(messageReceivers, recipientId);
        messageReceivers.addToOne(receiverTable, recipientId);
        recipientsToMessage.setName("messagesReceived");


        new DaoGenerator().generateAll(schema, args[0]);
    }
}
