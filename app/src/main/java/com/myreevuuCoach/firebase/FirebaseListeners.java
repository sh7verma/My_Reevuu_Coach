package com.myreevuuCoach.firebase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.myreevuuCoach.MainApplication;
import com.myreevuuCoach.interfaces.InterConst;
import com.myreevuuCoach.utils.ConnectivityReceiver;
import com.myreevuuCoach.utils.Utils;

import java.io.File;
import java.util.HashMap;

/**
 * Created by dev on 17/12/18.
 */

public class FirebaseListeners implements ConnectivityReceiver.ConnectivityReceiverListener {

    public static String chatDialogId = "";
    public static MessageListenerInterface mMessageInterface;
    public static ChatDialogsListenerInterface mChatDialogInterface;
    public static ChatDialogsListenerInterface mForwardChatDialogInterface;
    public static ChatDialogsListenerInterfaceForChat mChatDialogInterfaceForChat;
    public static ProfileListenerInterface mProfileInterface;
    static FirebaseListeners mListener;
    DatabaseReference mFirebaseConfigMessages = FirebaseDatabase.getInstance().getReference().child(FirebaseChatConstants.MESSAGES);
    DatabaseReference mFirebaseConfigChat = FirebaseDatabase.getInstance().getReference().child(FirebaseChatConstants.CHATS);
    DatabaseReference mFirebaseConfigProfile = FirebaseDatabase.getInstance().getReference().child(FirebaseChatConstants.USERS);
    DatabaseReference mFirebaseConfigReadStatus = FirebaseDatabase.getInstance().getReference().child(FirebaseChatConstants.READ_STATUS);
    HashMap<String, ChildEventListener> messageListener = new HashMap<>();
    HashMap<String, ChildEventListener> chatsListener = new HashMap<>();
    HashMap<String, ChildEventListener> profileListener = new HashMap<>();
    HashMap<String, Query> messageQuery = new HashMap<>();
    HashMap<String, Query> chatQuery = new HashMap<>();
    HashMap<String, Query> profileQuery = new HashMap<>();
    Utils util;
    Database mDb;
    Query messageQry;
    Query chatsQuery;
    Query usersQuery;


    ////////////////////  MessagesModel Listener  //////////////////////
    Context con;
    ChildEventListener mMessageChildListener = new ChildEventListener() {


        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            if (!TextUtils.isEmpty(util.getString(InterConst.ACCESS_TOKEN, ""))) {
                Log.e("message added", "is " + dataSnapshot + ", " + s);
                MessagesModel message = MessagesModel.parseMessage(dataSnapshot);
                if (message != null) {
                    if (message.message_deleted.get(String.valueOf(util.getInt(FirebaseChatConstants.user_id, -1))).equals("0")) {
                        if (message.message_type == FirebaseChatConstants.TYPE_NOTES || message.message_type == FirebaseChatConstants.TYPE_TEXT) {
                            mDb.addMessage(message, String.valueOf(util.getInt(FirebaseChatConstants.user_id, -1)));
                        } else {
                            if (message.sender_id.equals(String.valueOf(util.getInt(FirebaseChatConstants.user_id, -1)))) {
                                mDb.updateMessage(message, String.valueOf(util.getInt(FirebaseChatConstants.user_id, -1)));
                            } else {
                                mDb.addMessage(message, String.valueOf(util.getInt(FirebaseChatConstants.user_id, -1)));
                            }
                        }

                        if (!message.sender_id.equals(String.valueOf(util.getInt(FirebaseChatConstants.user_id, -1)))) {
                            if (chatDialogId.equals(message.chat_dialog_id)) {
                                if (mMessageInterface != null) {
                                    MessagesModel msg = mDb.getSingleMessage(message.message_id, String.valueOf(util.getInt(FirebaseChatConstants.user_id, -1)));
                                    mMessageInterface.onMessageAdd(msg);
                                } else {
                                    if (!message.sender_id.equals(String.valueOf(util.getInt(FirebaseChatConstants.user_id, -1)))) {
                                        if (message.message_status == FirebaseChatConstants.STATUS_MESSAGE_SENT) {
                                            mFirebaseConfigMessages.child(message.chat_dialog_id).child(message.message_id).child("message_status").setValue(FirebaseChatConstants.STATUS_MESSAGE_DELIVERED);
                                            HashMap<String, Object> val = new HashMap<>();
                                            val.put(FirebaseChatConstants.message_id, message.message_id);
                                            val.put(FirebaseChatConstants.message_status, FirebaseChatConstants.STATUS_MESSAGE_DELIVERED);
                                            mFirebaseConfigReadStatus.child(message.message_id).setValue(val);
                                        }
                                    }
                                }
                            } else {
                                if (!message.sender_id.equals(String.valueOf(util.getInt(FirebaseChatConstants.user_id, -1)))) {
                                    if (message.message_status == FirebaseChatConstants.STATUS_MESSAGE_SENT) {
                                        mFirebaseConfigMessages.child(message.chat_dialog_id).child(message.message_id).child("message_status").setValue(FirebaseChatConstants.STATUS_MESSAGE_DELIVERED);
                                        HashMap<String, Object> val = new HashMap<>();
                                        val.put(FirebaseChatConstants.message_id, message.message_id);
                                        val.put(FirebaseChatConstants.message_status, FirebaseChatConstants.STATUS_MESSAGE_DELIVERED);
                                        mFirebaseConfigReadStatus.child(message.message_id).setValue(val);
                                    }
                                }
                            }
                        }
                        if (message.sender_id.equals(String.valueOf(util.getInt(FirebaseChatConstants.user_id, -1)))) {
                            if (message.message_status == FirebaseChatConstants.STATUS_MESSAGE_SEEN) {
                                mFirebaseConfigMessages.child(message.chat_dialog_id).child(message.message_id).removeValue();
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            if (!TextUtils.isEmpty(util.getString(InterConst.ACCESS_TOKEN, ""))) {
                Log.e("message changed", "is " + dataSnapshot + ", " + s);
                MessagesModel message = MessagesModel.parseMessage(dataSnapshot);
                if (message != null) {
                    if (message.message_deleted.get(String.valueOf(util.getInt(FirebaseChatConstants.user_id, -1))).equals("0")) {
                        if (message.message_type == FirebaseChatConstants.TYPE_NOTES || message.message_type == FirebaseChatConstants.TYPE_TEXT) {
                            mDb.addMessage(message, String.valueOf(util.getInt(FirebaseChatConstants.user_id, -1)));
                        } else {
                            if (message.sender_id.equals(String.valueOf(util.getInt(FirebaseChatConstants.user_id, -1)))) {
                                mDb.updateMessage(message, String.valueOf(util.getInt(FirebaseChatConstants.user_id, -1)));
                            } else {
                                mDb.addMessage(message, String.valueOf(util.getInt(FirebaseChatConstants.user_id, -1)));
                            }
                        }
                        if (chatDialogId.equals(message.chat_dialog_id)) {
                            if (mMessageInterface != null) {
                                MessagesModel msg = mDb.getSingleMessage(message.message_id, String.valueOf(util.getInt(FirebaseChatConstants.user_id, -1)));
                                if (message.sender_id.equals(String.valueOf(util.getInt(FirebaseChatConstants.user_id, -1)))) {
                                    mMessageInterface.onMessageAdd(msg);
                                } else {
                                    mMessageInterface.onMessageChanged(msg);
                                }
                            } else {
                                if (!message.sender_id.equals(String.valueOf(util.getInt(FirebaseChatConstants.user_id, -1)))) {
                                    if (message.message_status == FirebaseChatConstants.STATUS_MESSAGE_SENT) {
                                        mFirebaseConfigMessages.child(message.chat_dialog_id).child(message.message_id).child("message_status").setValue(FirebaseChatConstants.STATUS_MESSAGE_DELIVERED);
                                        HashMap<String, Object> val = new HashMap<>();
                                        val.put(FirebaseChatConstants.message_id, message.message_id);
                                        val.put(FirebaseChatConstants.message_status, FirebaseChatConstants.STATUS_MESSAGE_DELIVERED);
                                        mFirebaseConfigReadStatus.child(message.message_id).setValue(val);
                                    }
                                }
                            }
                        } else {
                            if (!message.sender_id.equals(String.valueOf(util.getInt(FirebaseChatConstants.user_id, -1)))) {
                                if (message.message_status == FirebaseChatConstants.STATUS_MESSAGE_SENT) {
                                    mFirebaseConfigMessages.child(message.chat_dialog_id).child(message.message_id).child("message_status").setValue(FirebaseChatConstants.STATUS_MESSAGE_DELIVERED);
                                    HashMap<String, Object> val = new HashMap<>();
                                    val.put(FirebaseChatConstants.message_id, message.message_id);
                                    val.put(FirebaseChatConstants.message_status, FirebaseChatConstants.STATUS_MESSAGE_DELIVERED);
                                    mFirebaseConfigReadStatus.child(message.message_id).setValue(val);
                                }
                            }
                        }
                        if (message.sender_id.equals(String.valueOf(util.getInt(FirebaseChatConstants.user_id, -1)))) {
                            if (message.message_status == FirebaseChatConstants.STATUS_MESSAGE_SEEN) {
                                mFirebaseConfigMessages.child(message.chat_dialog_id).child(message.message_id).removeValue();
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    ChildEventListener mChatChildListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            if (!TextUtils.isEmpty(util.getString(InterConst.ACCESS_TOKEN, ""))) {
                Log.e("chat added", "is " + dataSnapshot + ", " + s);
                ChatsModel chat = ChatsModel.parseChat(dataSnapshot, String.valueOf(util.getInt(FirebaseChatConstants.user_id, -1)));
                if (chat != null) {
                    if (!TextUtils.isEmpty(chat.chat_dialog_id)) {
                        if (chat.user_type.containsKey(String.valueOf(util.getInt(FirebaseChatConstants.user_id, -1)))) {
                            setListener(chat.chat_dialog_id);
//                    String otherUserID = "";
//                    for (String userId : chat.user_type.keySet()) {
//                        if (!userId.equals( String.valueOf(util.getInt( FirebaseChatConstants.user_id,-1))  )) {
//                            otherUserID = userId;
//                            break;
//                        }
//                    }
                            mDb.addUpateChat(chat, String.valueOf(util.getInt(FirebaseChatConstants.user_id, -1)),
                                    chat.opponent_user_id);

                            if (mChatDialogInterface != null) {
                                mChatDialogInterface.onDialogAdd(chat);
                            }
                            if (mForwardChatDialogInterface != null) {
                                mForwardChatDialogInterface.onDialogAdd(chat);
                            }
                        }
                    } else {
                        mFirebaseConfigProfile.child("id_" + String.valueOf(util.getInt(FirebaseChatConstants.user_id, -1))).child("chat_dialog_ids").child(dataSnapshot.getKey());
                        mFirebaseConfigProfile.removeValue();
                        removeMessageListener(dataSnapshot.getKey());
                        removeChatsListener(dataSnapshot.getKey());
                    }
                }
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            if (!TextUtils.isEmpty(util.getString(InterConst.ACCESS_TOKEN, ""))) {
                Log.e("chat changed", "is " + dataSnapshot + ", " + s);
                ChatsModel chat = ChatsModel.parseChat(dataSnapshot, String.valueOf(util.getInt(FirebaseChatConstants.user_id, -1)));
                if (chat != null) {
                    if (!TextUtils.isEmpty(chat.chat_dialog_id)) {
                        if (chat.user_type.containsKey(String.valueOf(util.getInt(FirebaseChatConstants.user_id, -1)))) {
                            setListener(chat.chat_dialog_id);
//                    String otherUserID = "";
//                    for (String userId : chat.user_type.keySet()) {
//                        if (!userId.equals( String.valueOf(util.getInt( FirebaseChatConstants.user_id,-1))  )) {
//                            otherUserID = userId;
//                            break;
//                        }
//                    }
                            mDb.addUpateChat(chat, String.valueOf(util.getInt(FirebaseChatConstants.user_id, -1)), chat.opponent_user_id);
                            if (mChatDialogInterface != null) {
                                mChatDialogInterface.onDialogChanged(chat, 0);
                            }
                            if (mForwardChatDialogInterface != null) {
                                mForwardChatDialogInterface.onDialogChanged(chat, 0);
                            }
                            if (mChatDialogInterfaceForChat != null) {
                                if (chatDialogId.equals(chat.chat_dialog_id)) {
                                    mChatDialogInterfaceForChat.onDialogChanged(chat, 0);
                                }
                            }
                        }
                    } else {
                        mDb.deleteDialogData(dataSnapshot.getKey());
                        mFirebaseConfigProfile.child("id_" + String.valueOf(util.getInt(FirebaseChatConstants.user_id, -1))).child("chat_dialog_ids").child(dataSnapshot.getKey());
                        mFirebaseConfigProfile.removeValue();
                        removeMessageListener(dataSnapshot.getKey());
                        removeChatsListener(dataSnapshot.getKey());
                        if (mChatDialogInterface != null) {
                            mChatDialogInterface.onDialogChanged(chat, 1);
                        }
                        if (mForwardChatDialogInterface != null) {
                            mForwardChatDialogInterface.onDialogChanged(chat, 1);
                        }
                        if (mChatDialogInterfaceForChat != null) {
                            if (chatDialogId.equals(chat.chat_dialog_id)) {
                                mChatDialogInterfaceForChat.onDialogChanged(chat, 1);
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            if (!TextUtils.isEmpty(util.getString(InterConst.ACCESS_TOKEN, ""))) {
                mDb.deleteDialogData(dataSnapshot.getKey());
                removeMessageListener(dataSnapshot.getKey());
                removeChatsListener(dataSnapshot.getKey());
                if (mChatDialogInterface != null) {
                    mChatDialogInterface.onDialogRemoved(dataSnapshot.getKey());
                }
                if (mForwardChatDialogInterface != null) {
                    mForwardChatDialogInterface.onDialogRemoved(dataSnapshot.getKey());
                }
                if (mChatDialogInterfaceForChat != null) {
                    if (chatDialogId.equals(dataSnapshot.getKey())) {
                        mChatDialogInterfaceForChat.onDialogChanged(null, 1);
                    }
                }
            }
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    ChildEventListener mUserListener = new ChildEventListener() {

        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            if (!TextUtils.isEmpty(util.getString(InterConst.ACCESS_TOKEN, ""))) {
                ProfileModel user = ProfileModel.parseProfile(dataSnapshot);
                if (!TextUtils.isEmpty(user.access_token)) {
                    if (user.online_status != FirebaseChatConstants.ONLINE_LONG) {
                        if (util.getInt(InterConst.BACKGROUND, InterConst.APP_OFFLINE) == InterConst.APP_ONLINE) {
                            mFirebaseConfigProfile.child("id_" + user.user_id).child("online_status")
                                    .setValue(FirebaseChatConstants.ONLINE_LONG);
                        }
                    }

                    HashMap<String, String> dialogs = user.chat_dialog_ids;
                    if (dialogs != null && dialogs.size() > 0) {
                        HashMap<String, String> ids = mDb.getDialogs(String.valueOf(util.getInt(FirebaseChatConstants.user_id, -1)));
                        mDb.addDialogs(dialogs, String.valueOf(util.getInt(FirebaseChatConstants.user_id, -1)));
                        for (String userUniqueKey : dialogs.keySet()) {
                            ids.remove(userUniqueKey);
                            setChatsListener(userUniqueKey);
                        }
                        for (String userUniqueKey : ids.keySet()) {
                            removeChatsListener(userUniqueKey);
                            mDb.deleteDialogData(userUniqueKey);
                        }
                    }
                    if (!user.access_token.equals(util.getString(InterConst.ACCESS_TOKEN, ""))) {
                        if (mProfileInterface != null) {
                            mProfileInterface.onProfileChanged("");
                        }
                    }
                }
            }
//            mDb.addBlockedByMeIds(user.blocked_by_me);
//            mDb.addBlockedByOtherIds(user.blocked_from_others);
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            if (!TextUtils.isEmpty(util.getString(InterConst.ACCESS_TOKEN, ""))) {
                ProfileModel user = ProfileModel.parseProfile(dataSnapshot);
                if (user.online_status != FirebaseChatConstants.ONLINE_LONG) {
                    if (util.getInt(InterConst.BACKGROUND, InterConst.APP_OFFLINE) == InterConst.APP_ONLINE) {
                        mFirebaseConfigProfile.child("id_" + user.user_id).child("online_status")
                                .setValue(FirebaseChatConstants.ONLINE_LONG);
                    }
                }
                HashMap<String, String> dialogs = user.chat_dialog_ids;
                if (dialogs != null && dialogs.size() > 0) {
                    HashMap<String, String> ids = mDb.getDialogs(String.valueOf(util.getInt(FirebaseChatConstants.user_id, -1)));
                    mDb.addDialogs(dialogs, String.valueOf(util.getInt(FirebaseChatConstants.user_id, -1)));
                    for (String userUniqueKey : dialogs.keySet()) {
                        ids.remove(userUniqueKey);
                        setChatsListener(userUniqueKey);
                    }
                    for (String userUniqueKey : ids.keySet()) {
                        removeChatsListener(userUniqueKey);
                        mDb.deleteDialogData(userUniqueKey);
                    }
                }

                if (!user.access_token.equals(util.getString(InterConst.ACCESS_TOKEN, ""))) {
                    if (mProfileInterface != null) {
                        mProfileInterface.onProfileChanged(user.user_id);
                    }
                }
//            mDb.addBlockedByMeIds(user.blocked_by_me);
//            mDb.addBlockedByOtherIds(user.blocked_from_others);
            }
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public static void initListener(Context con) {
        if (mListener == null)
            mListener = new FirebaseListeners();
        mListener.initDefaults(con);
    }

    public static FirebaseListeners getListenerClass(Context con) {
        if (mListener != null)
            return mListener;
        else {
            mListener = new FirebaseListeners();
            mListener.initDefaults(con);
            return mListener;
        }
    }

    /////////////////////  Dialog Listener  //////////////////////

    public static void setMessageListener(MessageListenerInterface listsner, String dialogId) {
        mMessageInterface = listsner;
        chatDialogId = dialogId;
    }

    public static void setChatDialogListener(ChatDialogsListenerInterface listsner) {
        mChatDialogInterface = listsner;
    }

    public static void setForwardChatDialogListener(ChatDialogsListenerInterface listsner) {
        mForwardChatDialogInterface = listsner;
    }

    public static void setChatDialogListenerForChat(ChatDialogsListenerInterfaceForChat listsner, String dialogId) {
        mChatDialogInterfaceForChat = listsner;
        chatDialogId = dialogId;
    }

    public static void setProfileDataListener(ProfileListenerInterface listsner) {
        mProfileInterface = listsner;
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public static void getTime(final Utils util) {
        DatabaseReference offsetRef = FirebaseDatabase.getInstance().getReference(".info/serverTimeOffset");
        offsetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
//                SimpleDateFormat today_date_format = new SimpleDateFormat("hh:mm aa", Locale.US);
                double offset = snapshot.getValue(Double.class);
                util.setString("offset", "" + offset);
//                double estimatedServerTimeMs = System.currentTimeMillis() + offset;
//                Log.e("offset", "" + offset);
//                Log.e("time", "" + estimatedServerTimeMs);
//                String a = offset + "";
//                String[] aa = a.split("\\.");
//                Calendar cal = Calendar.getInstance();
//                cal.setTimeInMillis(cal.getTimeInMillis() + Long.parseLong(aa[0]));
//                Log.e("date", "" + today_date_format.format(cal.getTime()));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });
    }

    void initDefaults(Context context) {
        MainApplication.getInstance().setConnectivityListener(FirebaseListeners.this);
        util = new Utils(context);
        mDb = new Database(context);
        con = context;
    }

    public void removeMessageListener(String dialogId) {
        dialogId = dialogId.trim();
        Query mq = messageQuery.get(dialogId);
        if (mq != null && messageListener.containsKey(dialogId)) {
            mq.removeEventListener(messageListener.get(dialogId));
            messageListener.remove(dialogId);
            messageQuery.remove(dialogId);
        }
    }

    public void setListener(String dialogId) {
        dialogId = dialogId.trim();
        if (!messageListener.containsKey(dialogId)) {
            messageListener.put(dialogId, mMessageChildListener);
            messageQry = mFirebaseConfigMessages.child(dialogId);
            messageQry.addChildEventListener(mMessageChildListener);
            messageQuery.put(dialogId, messageQry);
        }
    }

    //////////////////////  Profile Listener  ////////////////////////

    public void removeChatsListener(String dialogId) {
        dialogId = dialogId.trim();
        Query mq = chatQuery.get(dialogId);
        if (mq != null && chatsListener.containsKey(dialogId)) {
            mq.removeEventListener(chatsListener.get(dialogId));
            chatsListener.remove(dialogId);
            chatQuery.remove(dialogId);
        }
    }

    public void setChatsListener(String dialogId) {
        dialogId = dialogId.trim();
        if (!chatsListener.containsKey(dialogId)) {
            chatsQuery = mFirebaseConfigChat.orderByKey().equalTo(dialogId);
            chatsQuery.addChildEventListener(mChatChildListener);
            chatsListener.put(dialogId, mChatChildListener);
            chatQuery.put(dialogId, chatsQuery);
        }
    }

    public void setProfileListener(String userId) {
        if (!profileListener.containsKey("id_" + userId)) {
            usersQuery = mFirebaseConfigProfile.orderByKey().equalTo("id_" + userId);
            usersQuery.addChildEventListener(mUserListener);
            profileListener.put(userId, mUserListener);
            profileQuery.put(userId, usersQuery);
        }
    }

    public void RemoveAllListeners() {
        for (String ids : chatsListener.keySet()) {
            Query chat = chatQuery.get(ids.trim());
            chat.removeEventListener(chatsListener.get(ids.trim()));
        }
        chatsListener.clear();
        chatQuery.clear();

        for (String ids : messageListener.keySet()) {
            Query message = messageQuery.get(ids.trim());
            message.removeEventListener(messageListener.get(ids.trim()));
        }

        messageListener.clear();
        messageQuery.clear();

        for (String ids : profileListener.keySet()) {
            Query user = profileQuery.get(ids.trim());
            user.removeEventListener(profileListener.get(ids.trim()));
        }
        profileListener.clear();
        profileQuery.clear();

    }

    public void clearApplicationData(Context mCon) {
        mCon.deleteDatabase(Database.DATABASE);
        File cache = mCon.getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "************** File /data/data/APP_PACKAGE/"
                            + s + " DELETED *****************");
                }
            }
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            if (!TextUtils.isEmpty(util.getString("access_token", "")) && util.getInt("profile_status", 0) == 2) {
                DatabaseReference mFirebaseConfig = FirebaseDatabase.getInstance().getReference().child("Users");
                if (util.getInt(InterConst.BACKGROUND, InterConst.APP_OFFLINE) == InterConst.APP_ONLINE) {
                    mFirebaseConfig.child("id_" + String.valueOf(util.getInt(FirebaseChatConstants.user_id, -1)))
                            .child("online_status").setValue(ServerValue.TIMESTAMP);
                } else {
                    mFirebaseConfig.child("id_" + String.valueOf(util.getInt(FirebaseChatConstants.user_id, -1)))
                            .child("online_status").setValue(FirebaseChatConstants.ONLINE_LONG);
                }
            }
        }
    }

    public interface MessageListenerInterface {
        void onMessageAdd(MessagesModel message);

        void onMessageChanged(MessagesModel message);
    }

    //// Chat Fragment
    public interface ChatDialogsListenerInterface {
        void onDialogAdd(ChatsModel chat);

        void onDialogChanged(ChatsModel chat, int val);

        void onDialogRemoved(String dialogId);
    }

    //// Chat Activity
    public interface ChatDialogsListenerInterfaceForChat {
        void onDialogChanged(ChatsModel chat, int val);
    }

    public interface ProfileListenerInterface {
        void onProfileChanged(String value);
    }

}