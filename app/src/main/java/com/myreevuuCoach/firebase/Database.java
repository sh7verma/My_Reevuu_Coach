package com.myreevuuCoach.firebase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.myreevuuCoach.interfaces.InterConst;
import com.myreevuuCoach.utils.Utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by dev on 17/12/18.
 */

public class Database extends SQLiteOpenHelper {

    //    public static final String DATABASE = "my_reevuu_coach_local";
//    /// Events Table
//    static final String POSTS_TABLE = "posts_table";
//    static final String POST_ID = "post_id";
//    static final String POST_TYPE = "post_type";
//    static final String TITLE = "title";
//    static final String DESCRIPTION = "description";
//    static final String PROFESSION_ID = "profession_id";
//    static final String ADDRESS = "address";
//    static final String LATITUDE = "latitude";
//    static final String LONGITUDE = "longitude";
//    static final String DATE_TIME = "date_time";
//    static final String URL = "url";
//    static final String LIKE_COUNT = "like_count";
//    static final String COMMENT_COUNT = "comment_count";
//    static final String GOING = "going";
//    static final String INTRESTED = "interested";
//    static final String IS_BOOKMARKED = "is_bookmarked";
//    static final String LIKED = "liked";
//    static final String IS_GOING = "is_going";
//    static final String SHAREABLE_LINK = "shareable_link";
//    /// Images Table
//    static final String IMAGES_TABLE = "images_table";
//    static final String IMAGE_ID = "image_id";
//    static final String IMAGE_URL = "image_url";
//    static final String THUMBNAIL_URL = "thumnamil_url";
//    /// Going Table
//    static final String GOING_TABLE = "going_table";
//    static final String FULL_NAME = "full_name";
//    static final String AVATAR = "avatar";
//    static final String USER_ID = "user_id";
//    /// NOtes Table
//    static final String NOTES_TABLE = "notes_table";
//    static final String NOTES_ID = "notes_id";
//    static final String NOTES_NAME = "notes_name";
//    static final String NOTES_TITLE = "notes_title";
//    static final String NOTES_DESC = "notes_desc";
//    static final String NOTES_URL = "notes_url";
//    static final String NOTES_TYPE = "notes_type";
//    static final String NOTES_CREATED = "notes_created";
//    static final String NOTES_UPDATED = "notes_updated";
//    static final String NOTES_USER_ID = "note_user_id";
//    static final String NOTES_USER_NAME = "note_user_name";
//    static final String NOTES_NEW_TITLE = "notes_new_title";
//    /// Profile Table
//    static final String ID = "id";
//    static final String PROFILE_TABLE = "profile_table";
//    static final String ONLINE_STATUS = "online_status";
//    static final String ACCESS_TOKEN = "access_token";
//    static final String USER_NAME = "user_name";
//    static final String USER_PIC = "user_pic";
//    /// Dialogs Table
//    static final String DIALOGS_TABLE = "dialogs_table";
//    static final String CHAT_DIALOG_ID = "chat_dialog_id";
//    /// Chats Table
//    static final String CHATS_TABLE = "chats_table";
//    static final String LAST_MESSAGE = "last_message";
//    static final String LAST_MESSAGE_TIME = "last_message_time";
//    static final String LAST_MESSAGE_SENDER_ID = "last_message_sender_id";
//    static final String LAST_MESSAGE_ID = "last_message_id";
//    static final String LAST_MESSAGE_TYPE = "last_message_type";
//    static final String LAST_MESSAGE_DATA = "last_message_data";
//    static final String PARTICIPANT_IDS = "participant_id";
//    static final String NAME = "name";
//    static final String PROFILE_PIC = "profile_pic";
//    static final String DELETE_DIALOG_TIME = "delete_dialog_time";
//    static final String USER_TYPE = "user_type";
//    static final String OPPONENT_USER_TYPE = "opponent_user_type";
//    static final String OPPONENT_USER_ID = "opponent_user_id";
//    static final String RATING = "rating";
//    static final String OWN_MESSAGE_RATING_COUNT = "own_message_rating_count";
//    static final String DELETE_OUTER_DIALOG = "DELETE_OUTER_DIALOG";
//    static final String OTHER_MESSAGE_RATING_COUNT = "other_message_rating_count";
//    /// Unread Count Table
//    static final String UNREAD_COUNT_TABLE = "unread_count_table";
//    static final String UNREAD_COUNT = "unread_count";
//    /// Block Table
//    static final String BLOCK_TABLE = "block_table";
//    static final String BLOCK_STATUS = "block_status";
//    /// Messages Table
//    static final String MESSAGES_TABLE = "messages_table";
//    static final String MESSAGE_ID = "message_id";
//    static final String MESSAGE = "message";
//    static final String MESSAGE_TYPE = "message_type";
//    static final String MESSAGE_TIME = "message_time";
//    static final String FIRBASE_MESSAGE_TIME = "firebase_message_time";
//    static final String SENDER_ID = "sender_id";
//    static final String MESSAGE_STATUS = "message_status";
//    static final String ATTACHMENT_URL = "attachment_data";
//    static final String MESSAGE_DELETED = "message_deleted";
//    static final String FAVOURITE_MESSAGE = "favourite_message";
//    static final String ATTACHMENT_PROGRESS = "attachment_progress";
//    static final String ATTACHMENT_PATH = "attachment_path"; //local path
//    static final String ATTACHMENT_STATUS = "attachment_status"; // uploading; error; success;
//    static final String CUSTOM_DATA = "custom_data";
//    static final String RECEIVER_ID = "receiver_id";
//    private static final int dbversion = 1;
//    SimpleDateFormat today_date_format = new SimpleDateFormat("hh:mm aa", Locale.US);
//    private Utils utils;
//
//    public Database(Context context) {
//        super(context, DATABASE, null, dbversion);
//        utils = new Utils(context);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        createTables(db);
//    }
//
//    private void createTables(SQLiteDatabase db) {
//        String postsQuery = "create table if not exists " + POSTS_TABLE
//                + " (" + POST_ID + " TEXT ,"
//                + POST_TYPE + " TEXT ,"
//                + TITLE + " TEXT ,"
//                + DESCRIPTION + " TEXT ,"
//                + PROFESSION_ID + " TEXT ,"
//                + ADDRESS + " TEXT ,"
//                + LATITUDE + " TEXT ,"
//                + LONGITUDE + " TEXT ,"
//                + DATE_TIME + " TEXT ,"
//                + URL + " TEXT ,"
//                + LIKE_COUNT + " TEXT ,"
//                + COMMENT_COUNT + " TEXT ,"
//                + GOING + " TEXT ,"
//                + INTRESTED + " TEXT ,"
//                + IS_BOOKMARKED + " TEXT ,"
//                + LIKED + " TEXT ,"
//                + IS_GOING + " TEXT ,"
//                + SHAREABLE_LINK + " TEXT )";
//        db.execSQL(postsQuery);
//
//        String imagesQuery = "create table if not exists " + IMAGES_TABLE
//                + " (" + IMAGE_ID + " TEXT ,"
//                + IMAGE_URL + " TEXT ,"
//                + THUMBNAIL_URL + " TEXT ,"
//                + POST_ID + " TEXT ,"
//                + POST_TYPE + " TEXT )";
//        db.execSQL(imagesQuery);
//
//        String goingQuery = "create table if not exists " + GOING_TABLE
//                + " (" + FULL_NAME + " TEXT ,"
//                + AVATAR + " TEXT ,"
//                + USER_ID + " TEXT ,"
//                + POST_ID + " TEXT )";
//        db.execSQL(goingQuery);
//
//        String notesQuery = "create table if not exists " + NOTES_TABLE
//                + " (" + NOTES_ID + " TEXT ,"
//                + NOTES_NAME + " TEXT ,"
//                + NOTES_TITLE + " TEXT ,"
//                + NOTES_DESC + " TEXT ,"
//                + NOTES_URL + " TEXT ,"
//                + NOTES_TYPE + " TEXT ,"
//                + NOTES_CREATED + " TEXT ,"
//                + NOTES_UPDATED + " TEXT ,"
//                + NOTES_USER_ID + " TEXT ,"
//                + NOTES_USER_NAME + " TEXT ,"
//                + NOTES_NEW_TITLE + " TEXT )";
//        db.execSQL(notesQuery);
//
//        String profileQuery = "create table if not exists " + PROFILE_TABLE
//                + " (" + ID + " integer primary key AUTOINCREMENT ,"
//                + USER_ID + " TEXT ,"
//                + ONLINE_STATUS + " TEXT ,"
//                + ACCESS_TOKEN + " TEXT ,"
//                + USER_NAME + " TEXT ,"
//                + USER_PIC + " TEXT )";
//        db.execSQL(profileQuery);
//
//        String dialogQuery = "create table if not exists " + DIALOGS_TABLE
//                + " (" + ID + " integer primary key AUTOINCREMENT ,"
//                + USER_ID + " TEXT ,"
//                + CHAT_DIALOG_ID + " TEXT )";
//        db.execSQL(dialogQuery);
//
//        String chatsQuery = "create table if not exists " + CHATS_TABLE
//                + " (" + ID + " integer primary key AUTOINCREMENT ,"
//                + CHAT_DIALOG_ID + " TEXT ,"
//                + LAST_MESSAGE + " TEXT ,"
//                + LAST_MESSAGE_TIME + " TEXT ,"
//                + LAST_MESSAGE_SENDER_ID + " TEXT ,"
//                + LAST_MESSAGE_ID + " TEXT ,"
//                + LAST_MESSAGE_TYPE + " TEXT ,"
//                + PARTICIPANT_IDS + " TEXT ,"
//                + NAME + " TEXT ,"
//                + PROFILE_PIC + " TEXT ,"
//                + DELETE_DIALOG_TIME + " TEXT ,"
//                + USER_TYPE + " TEXT ,"
//                + OPPONENT_USER_TYPE + " TEXT ,"
//                + OPPONENT_USER_ID + " TEXT ,"
//                + RATING + " TEXT ,"
//                + OWN_MESSAGE_RATING_COUNT + " TEXT ,"
//                + OTHER_MESSAGE_RATING_COUNT + " TEXT ,"
//                + DELETE_OUTER_DIALOG + " TEXT ,"
//                + LAST_MESSAGE_DATA + " TEXT )";
//        db.execSQL(chatsQuery);
//
//        String unreadCountQuery = "create table if not exists " + UNREAD_COUNT_TABLE
//                + " (" + ID + " integer primary key AUTOINCREMENT ,"
//                + CHAT_DIALOG_ID + " TEXT ,"
//                + USER_ID + " TEXT ,"
//                + UNREAD_COUNT + " integer )";
//        db.execSQL(unreadCountQuery);
//
//        String blockQuery = "create table if not exists " + BLOCK_TABLE
//                + " (" + ID + " integer primary key AUTOINCREMENT ,"
//                + CHAT_DIALOG_ID + " TEXT ,"
//                + USER_ID + " TEXT ,"
//                + BLOCK_STATUS + " TEXT )";
//        db.execSQL(blockQuery);
//
//        String messageQuery = "create table if not exists " + MESSAGES_TABLE
//                + " (" + ID + " integer primary key AUTOINCREMENT ,"
//                + MESSAGE_ID + " TEXT ,"
//                + MESSAGE + " TEXT ,"
//                + MESSAGE_TYPE + " TEXT ,"
//                + MESSAGE_TIME + " TEXT ,"
//                + FIRBASE_MESSAGE_TIME + " TEXT ,"
//                + CHAT_DIALOG_ID + " TEXT ,"
//                + SENDER_ID + " TEXT ,"
//                + MESSAGE_STATUS + " integer ,"
//                + ATTACHMENT_URL + " TEXT ,"
//                + MESSAGE_DELETED + " TEXT ,"
//                + FAVOURITE_MESSAGE + " TEXT ,"
//                + ATTACHMENT_PROGRESS + " TEXT ,"
//                + ATTACHMENT_PATH + " TEXT ,"
//                + ATTACHMENT_STATUS + " TEXT ,"
//                + CUSTOM_DATA + " TEXT ,"
//                + RECEIVER_ID + " TEXT )";
//        db.execSQL(messageQuery);
//
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//
//    }
//
//
//    public void deleteNotes(int noteId) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.beginTransaction();
//        try {
//            String unShortlist = "Delete from " + NOTES_TABLE + " where " + NOTES_ID + " = '" + noteId + "'";
//            db.execSQL(unShortlist);
//            db.setTransactionSuccessful();
//        } catch (Exception e) {
//            Log.e("Exception = ", e + "");
//        } finally {
//            db.endTransaction();
//        }
//    }
//
//    public void updateGoingStatusEvents(int postId, int goingStatus, int interested) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.beginTransaction();
//        try {
//            ContentValues cv = new ContentValues();
//            cv.put(IS_GOING, goingStatus);
//            cv.put(INTRESTED, interested);
//            db.update(POSTS_TABLE, cv, POST_ID + " = '" + postId + "'", null);
//            db.setTransactionSuccessful();
//        } catch (Exception e) {
//            Log.e("Exception = ", e + "");
//        } finally {
//            db.endTransaction();
//        }
//    }
//
//
//    public void updateLikeStatus(int postId, int likeStatus, int likeCount) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.beginTransaction();
//        try {
//            ContentValues cv = new ContentValues();
//            cv.put(LIKED, likeStatus);
//            cv.put(LIKE_COUNT, likeCount);
//            db.update(POSTS_TABLE, cv, POST_ID + " = '" + postId + "'", null);
//            db.setTransactionSuccessful();
//        } catch (Exception e) {
//            Log.e("Exception = ", e + "");
//        } finally {
//            db.endTransaction();
//        }
//    }
//
//    public void updateBookmarkStatus(int postId, int bookmarkStatus) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.beginTransaction();
//        try {
//            ContentValues cv = new ContentValues();
//            cv.put(IS_BOOKMARKED, bookmarkStatus);
//            db.update(POSTS_TABLE, cv, POST_ID + " = '" + postId + "'", null);
//            db.setTransactionSuccessful();
//        } catch (Exception e) {
//            Log.e("Exception = ", e + "");
//        } finally {
//            db.endTransaction();
//        }
//    }
//
//    public void updateCommentCount(int postId, int commentCount) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.beginTransaction();
//        try {
//            ContentValues cv = new ContentValues();
//            cv.put(COMMENT_COUNT, commentCount);
//            db.update(POSTS_TABLE, cv, POST_ID + " = '" + postId + "'", null);
//            db.setTransactionSuccessful();
//        } catch (Exception e) {
//            Log.e("Exception = ", e + "");
//        } finally {
//            db.endTransaction();
//        }
//    }
//
//    public void removeGoingUser(int userId) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.beginTransaction();
//        try {
//            String unShortlist = "Delete from " + GOING_TABLE + " where " + USER_ID + " = '" + userId + "'";
//            db.execSQL(unShortlist);
//            db.setTransactionSuccessful();
//        } catch (Exception e) {
//            Log.e("Exception = ", e + "");
//        } finally {
//            db.endTransaction();
//        }
//    }
//
//    public void deleteEventData(int postType) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.beginTransaction();
//        try {
//            String dropEvents = "Delete from  " + POSTS_TABLE + " where " + POST_TYPE + " = '" + postType + "'";
//            db.execSQL(dropEvents);
//
//            String dropImages = "Delete from  " + IMAGES_TABLE + " where " + POST_TYPE + " = '" + postType + "'";
//            db.execSQL(dropImages);
//
//            String dropGoingUsers = "Delete from  " + GOING_TABLE;
//            db.execSQL(dropGoingUsers);
//
//            db.setTransactionSuccessful();
//        } catch (Exception e) {
//            Log.e("Exception = ", e + "");
//        } finally {
//            db.endTransaction();
//        }
//    }
//
//    public void deleteCommunityData(int postType) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.beginTransaction();
//        try {
//            String dropEvents = "Delete from  " + POSTS_TABLE + " where " + POST_TYPE + " = '" + postType + "'";
//            db.execSQL(dropEvents);
//
//            String dropImages = "Delete from  " + IMAGES_TABLE + " where " + POST_TYPE + " = '" + postType + "'";
//            db.execSQL(dropImages);
//
//            db.setTransactionSuccessful();
//        } catch (Exception e) {
//            Log.e("Exception = ", e + "");
//        } finally {
//            db.endTransaction();
//        }
//    }
//
//
//    public void deletePostById(int postId) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.beginTransaction();
//        try {
//            String dropEvents = "Delete from  " + POSTS_TABLE + " where " + POST_ID + " = '" + postId + "'";
//            db.execSQL(dropEvents);
//
//            String dropImages = "Delete from  " + IMAGES_TABLE + " where " + POST_ID + " = '" + postId + "'";
//            db.execSQL(dropImages);
//
//            String dropGoingUsers = "Delete from  " + GOING_TABLE + " where " + POST_ID + " = '" + postId + "'";
//            db.execSQL(dropGoingUsers);
//
//            db.setTransactionSuccessful();
//        } catch (Exception e) {
//            Log.e("Exception = ", e + "");
//        } finally {
//            db.endTransaction();
//        }
//    }
//
//    public void deleteAllTables() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.beginTransaction();
//        try {
//            String dropEvents = "Delete from  " + POSTS_TABLE;
//            db.execSQL(dropEvents);
//
//            String dropImages = "Delete from  " + IMAGES_TABLE;
//            db.execSQL(dropImages);
//
//            String dropGoingUsers = "Delete from  " + GOING_TABLE;
//            db.execSQL(dropGoingUsers);
//
//            String dropNotes = "Delete from  " + NOTES_TABLE;
//            db.execSQL(dropNotes);
//
//            String dropProfile = "Delete from  " + PROFILE_TABLE;
//            db.execSQL(dropProfile);
//
//            String dropDialogs = "Delete from  " + DIALOGS_TABLE;
//            db.execSQL(dropDialogs);
//
//            String dropChats = "Delete from  " + CHATS_TABLE;
//            db.execSQL(dropChats);
//
//            String dropUnread = "Delete from  " + UNREAD_COUNT_TABLE;
//            db.execSQL(dropUnread);
//
//            String dropBlock = "Delete from  " + BLOCK_TABLE;
//            db.execSQL(dropBlock);
//
//            String dropMessages = "Delete from  " + MESSAGES_TABLE;
//            db.execSQL(dropMessages);
//
//            db.setTransactionSuccessful();
//        } catch (Exception e) {
//            Log.e("Exception = ", e + "");
//        } finally {
//            db.endTransaction();
//        }
//    }
//
//    ///////////////////////////////////////////////////////////////////////
//
//    public void addProfile(ProfileModel model) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.beginTransaction();
//        Cursor data = null;
//        try {
//            ContentValues values = new ContentValues();
//            values.put(ONLINE_STATUS, "" + model.online_status);
//            values.put(ACCESS_TOKEN, "" + model.access_token);
//            values.put(USER_NAME, "" + model.user_name);
//            values.put(USER_PIC, "" + model.user_pic);
//            data = getReadableDatabase().rawQuery("Select * from " + PROFILE_TABLE + " where "
//                    + USER_ID + " = '" + model.user_id + "'", null);
//            if (data.getCount() > 0) {
//                db.update(PROFILE_TABLE, values, USER_ID + " = '" + model.user_id + "'", null);
//            } else {
//                values.put(USER_ID, "" + model.user_id);
//                db.insertOrThrow(PROFILE_TABLE, null, values);
//            }
//            db.setTransactionSuccessful();
//        } catch (Exception e) {
//            Log.e("Exception", "is " + e);
//        } finally {
//            if (data != null && !data.isClosed()) {
//                data.close();
//            }
//            db.endTransaction();
//        }
//    }
//
//    public ProfileModel getProfile(String userId) {
//        SQLiteDatabase db_read = this.getReadableDatabase();
//        Cursor cur = null;
//        ProfileModel mUser = null;
//        try {
//            String qry = "select * from " + PROFILE_TABLE + " where " + USER_ID + " = '" + userId + "'";
//            cur = db_read.rawQuery(qry, null);
//            cur.moveToFirst();
//            while (!cur.isAfterLast()) {
//                mUser = new ProfileModel();
//                mUser.user_id = cur.getString(1);
//                mUser.online_status = Long.parseLong(cur.getString(2));
//                mUser.access_token = cur.getString(3);
//                mUser.user_name = cur.getString(4);
//                mUser.user_pic = cur.getString(5);
//                mUser.chat_dialog_ids = new HashMap<String, String>();
//                cur.moveToNext();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cur != null && !cur.isClosed()) {
//                cur.close();
//            }
//        }
//        return mUser;
//    }
//
//    public synchronized void addDialogs(HashMap<String, String> list, String userId) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.beginTransaction();
//        Cursor data = null;
//        try {
//            for (String userUniqueKey : list.keySet()) {
//                ContentValues values = new ContentValues();
//
//                values.put(USER_ID, userId);
//                values.put(CHAT_DIALOG_ID, userUniqueKey);
//
//                data = getReadableDatabase().rawQuery(
//                        "Select * from " + DIALOGS_TABLE + " where "
//                                + USER_ID + " = '" + userId + "' and " + CHAT_DIALOG_ID + " = '" + userUniqueKey + "'", null);
//                if (data.getCount() > 0) {
//                } else {
//                    db.insertOrThrow(DIALOGS_TABLE, null, values);
//                }
//            }
//            db.setTransactionSuccessful();
//        } catch (Exception e) {
//            Log.e("Exception", "is " + e);
//        } finally {
//            if (data != null && !data.isClosed()) {
//                data.close();
//            }
//            db.endTransaction();
//        }
//    }
//
//    public HashMap<String, String> getDialogs(String userId) {
//        SQLiteDatabase db_read = this.getReadableDatabase();
//        Cursor cur = null;
//        HashMap<String, String> list = new HashMap<String, String>();
//        try {
//            String qry = "select * from " + DIALOGS_TABLE + " where " + USER_ID + " = '" + userId + "'";
//            cur = db_read.rawQuery(qry, null);
//            cur.moveToFirst();
//            while (!cur.isAfterLast()) {
//                list.put(cur.getString(2), cur.getString(2));
//                cur.moveToNext();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cur != null && !cur.isClosed()) {
//                cur.close();
//            }
//        }
//        return list;
//    }
//
//    public void addNewChat(ChatsModel chat, String userId, String otherUserId) {
//        SQLiteDatabase db_write = this.getWritableDatabase();
//        db_write.beginTransaction();
//        try {
//            ContentValues values = new ContentValues();
//            values.put(CHAT_DIALOG_ID, chat.chat_dialog_id);
//            values.put(LAST_MESSAGE, chat.last_message);
//            values.put(LAST_MESSAGE_TIME, "" + chat.last_message_time.get(userId));
//            values.put(LAST_MESSAGE_SENDER_ID, chat.last_message_sender_id);
//            values.put(LAST_MESSAGE_ID, chat.last_message_id);
//            values.put(LAST_MESSAGE_TYPE, chat.last_message_type.get(userId));
//            values.put(LAST_MESSAGE_DATA, chat.last_message_data.get(userId));
//            values.put(PARTICIPANT_IDS, chat.participant_ids);
//            values.put(NAME, chat.name.get(otherUserId));
//            values.put(PROFILE_PIC, chat.profile_pic.get(otherUserId));
//            values.put(DELETE_DIALOG_TIME, "" + chat.delete_dialog_time.get(userId));
//            values.put(USER_TYPE, chat.user_type.get(userId));
//            values.put(OPPONENT_USER_TYPE, chat.user_type.get(otherUserId));
//            values.put(OPPONENT_USER_ID, otherUserId);
//            values.put(RATING, chat.rating.get(userId));
//
//
//            values.put(OWN_MESSAGE_RATING_COUNT, chat.message_rating_count.get(userId));
//            values.put(DELETE_OUTER_DIALOG, chat.delete_outer_dialog.get(userId));
//
//            values.put(OTHER_MESSAGE_RATING_COUNT, chat.message_rating_count.get(otherUserId));
//
//            db_write.insertOrThrow(CHATS_TABLE, null, values);
//
//            addUnreadCount(chat.unread_count, chat.chat_dialog_id);
//
//            addBlockStatus(chat.block_status, chat.chat_dialog_id);
//
//            db_write.setTransactionSuccessful();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            db_write.endTransaction();
//        }
//    }
//
//    public void addUpateChat(ChatsModel chat, String userId, String otherUserId) {
//        SQLiteDatabase db_write = this.getWritableDatabase();
//        SQLiteDatabase db_read = this.getReadableDatabase();
//        db_write.beginTransaction();
//        Cursor cur = null;
//        try {
//            ContentValues values = new ContentValues();
//            values.put(LAST_MESSAGE, chat.last_message);
//            values.put(LAST_MESSAGE_TIME, "" + chat.last_message_time.get(userId));
//            values.put(LAST_MESSAGE_SENDER_ID, chat.last_message_sender_id);
//            values.put(LAST_MESSAGE_ID, chat.last_message_id);
//            values.put(LAST_MESSAGE_TYPE, chat.last_message_type.get(userId));
//            values.put(LAST_MESSAGE_DATA, chat.last_message_data.get(userId));
//            values.put(NAME, chat.name.get(otherUserId));
//            values.put(PROFILE_PIC, chat.profile_pic.get(otherUserId));
//            values.put(DELETE_DIALOG_TIME, "" + chat.delete_dialog_time.get(userId));
//            values.put(RATING, chat.rating.get(userId));
//
//
//            values.put(OWN_MESSAGE_RATING_COUNT, chat.message_rating_count.get(userId));
//            values.put(DELETE_OUTER_DIALOG, chat.delete_outer_dialog.get(userId));
//
//            values.put(OTHER_MESSAGE_RATING_COUNT, chat.message_rating_count.get(otherUserId));
//
//            addUnreadCount(chat.unread_count, chat.chat_dialog_id);
//
//            addBlockStatus(chat.block_status, chat.chat_dialog_id);
//
//            String qry = "select * from " + CHATS_TABLE + " where " + CHAT_DIALOG_ID
//                    + " = '" + chat.chat_dialog_id + "'";
//            cur = db_read.rawQuery(qry, null);
//            if (cur.getCount() > 0) {
//                db_write.update(CHATS_TABLE, values, CHAT_DIALOG_ID + " ='" + chat.chat_dialog_id + "'", null);
//            } else {
//                values.put(CHAT_DIALOG_ID, chat.chat_dialog_id);
//                values.put(PARTICIPANT_IDS, chat.participant_ids);
//                values.put(USER_TYPE, chat.user_type.get(userId));
//                values.put(OPPONENT_USER_TYPE, chat.user_type.get(otherUserId));
//                values.put(OPPONENT_USER_ID, otherUserId);
//                db_write.insertOrThrow(CHATS_TABLE, null, values);
//            }
//
//            db_write.setTransactionSuccessful();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cur != null && !cur.isClosed()) {
//                cur.close();
//            }
//            db_write.endTransaction();
//        }
//    }
//
//    public void addUnreadCount(HashMap<String, Integer> unreadCount, String dialogId) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.beginTransaction();
//        Cursor data = null;
//        try {
//            for (String userUniqueKey : unreadCount.keySet()) {
//                data = getReadableDatabase().rawQuery(
//                        "Select * from " + UNREAD_COUNT_TABLE + " where "
//                                + CHAT_DIALOG_ID + " = '" + dialogId + "' and " + USER_ID + " = '" + userUniqueKey + "'", null);
//                ContentValues values = new ContentValues();
//                values.put(UNREAD_COUNT, unreadCount.get(userUniqueKey));
//                if (data.getCount() > 0) {
//                    db.update(UNREAD_COUNT_TABLE, values, CHAT_DIALOG_ID + " = '" +
//                            dialogId + "' and " + USER_ID + " = '" + userUniqueKey + "'", null);
//                } else {
//                    values.put(CHAT_DIALOG_ID, dialogId);
//                    values.put(USER_ID, userUniqueKey);
//                    db.insertOrThrow(UNREAD_COUNT_TABLE, null, values);
//                }
//            }
//            db.setTransactionSuccessful();
//        } catch (Exception e) {
//            Log.e("Exception", "is " + e);
//        } finally {
//            if (data != null && !data.isClosed()) {
//                data.close();
//            }
//            db.endTransaction();
//        }
//    }
//
//    public void addBlockStatus(HashMap<String, String> blockStatus, String dialogId) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.beginTransaction();
//        Cursor data = null;
//        try {
//            for (String userUniqueKey : blockStatus.keySet()) {
//                data = getReadableDatabase().rawQuery(
//                        "Select * from " + BLOCK_TABLE + " where "
//                                + CHAT_DIALOG_ID + " = '" + dialogId + "' and " + USER_ID + " = '" + userUniqueKey + "'", null);
//                ContentValues values = new ContentValues();
//                values.put(BLOCK_STATUS, blockStatus.get(userUniqueKey));
//                if (data.getCount() > 0) {
//                    db.update(BLOCK_TABLE, values, CHAT_DIALOG_ID + " = '" +
//                            dialogId + "' and " + USER_ID + " = '" + userUniqueKey + "'", null);
//                } else {
//                    values.put(CHAT_DIALOG_ID, dialogId);
//                    values.put(USER_ID, userUniqueKey);
//                    db.insertOrThrow(BLOCK_TABLE, null, values);
//                }
//            }
//            db.setTransactionSuccessful();
//        } catch (Exception e) {
//            Log.e("Exception", "is " + e);
//        } finally {
//            if (data != null && !data.isClosed()) {
//                data.close();
//            }
//            db.endTransaction();
//        }
//    }
//
//    public LinkedHashMap<String, ChatsModel> getAllChats(String userId) {
//        SQLiteDatabase db_read = this.getReadableDatabase();
//        Cursor cur = null;
//        LinkedHashMap<String, ChatsModel> mChatList = new LinkedHashMap<>();
//        try {
//            String qry = "Select * from " + CHATS_TABLE + " order by " + LAST_MESSAGE_TIME + " desc";
//            cur = db_read.rawQuery(qry, null);
//            cur.moveToFirst();
//            while (!cur.isAfterLast()) {
////                if (!TextUtils.isEmpty(cur.getString(6))) {
//                ChatsModel mChats = new ChatsModel();
//                mChats.chat_dialog_id = cur.getString(1);
//                mChats.last_message = cur.getString(2);
//                mChats.last_message_sender_id = cur.getString(4);
//                mChats.last_message_id = cur.getString(5);
//                mChats.participant_ids = cur.getString(7);
//                mChats.opponent_user_id = cur.getString(13);
//                String otherUserId = cur.getString(13);
//                String[] particID = mChats.participant_ids.split(",");
//                for (String id : particID) {
//                    String[] userIds = id.split("_");
//                    if (!(userIds[0].trim()).equalsIgnoreCase(userId)) {
//                        otherUserId = userIds[0].trim();
//                        break;
//                    }
//                }
//
//                mChats.last_message_type = new HashMap<>();
//                mChats.last_message_type.put(userId, cur.getString(6));
//                mChats.last_message_type.put(otherUserId, cur.getString(6));
//
//                mChats.last_message_data = new HashMap<>();
//                mChats.last_message_data.put(userId, cur.getString(18));
//                mChats.last_message_data.put(otherUserId, cur.getString(18));
//
//                mChats.last_message_time = new HashMap<>();
//                mChats.last_message_time.put(otherUserId, Long.parseLong(cur.getString(3)));
//                mChats.last_message_time.put(userId, Long.parseLong(cur.getString(3)));
//
//                mChats.name = new HashMap<>();
//                mChats.name.put(otherUserId, cur.getString(8));
//                mChats.name.put(userId, "");
//
//                mChats.profile_pic = new HashMap<>();
//                mChats.profile_pic.put(otherUserId, cur.getString(9));
//                mChats.profile_pic.put(userId, "");
//
//                mChats.delete_dialog_time = new HashMap<>();
//                mChats.delete_dialog_time.put(otherUserId, Long.parseLong(cur.getString(10)));
//                mChats.delete_dialog_time.put(userId, Long.parseLong(cur.getString(10)));
//
//                mChats.user_type = new HashMap<>();
//                mChats.user_type.put(userId, cur.getString(11));
//                mChats.user_type.put(otherUserId, cur.getString(12));
//
//                mChats.rating = new HashMap<>();
//                mChats.rating.put(userId, cur.getString(14));
//                mChats.rating.put(otherUserId, "0");
//
//                mChats.message_rating_count = new HashMap<>();
//                mChats.message_rating_count.put(userId, Integer.parseInt(cur.getString(15)));
//                mChats.message_rating_count.put(otherUserId, Integer.parseInt(cur.getString(16)));
//
//                mChats.delete_outer_dialog = new HashMap<>();
//                mChats.delete_outer_dialog.put(userId, cur.getString(17));
//                mChats.delete_outer_dialog.put(otherUserId, "0");
//
//                mChats.unread_count = getUnreadCount(cur.getString(1));
//                mChats.block_status = getBlockStatus(cur.getString(1));
//
////                int status = 0;
////                if (type.equals(FirebaseChatConstants.TYPE_ATHLETE)) {
////                    if (mChats.user_type.get(otherUserId).equals(FirebaseChatConstants.TYPE_ATHLETE)) {
////                        status = 1;
////                    }
////                } else if (type.equals(FirebaseChatConstants.TYPE_COACH)) {
////                    if (mChats.user_type.get(otherUserId).equals(FirebaseChatConstants.TYPE_COACH)) {
////                        status = 1;
////                    }
////                } else {
////                    status = 1;
////                }
////                if (status == 1) {
//
////                    long messageTime = Long.parseLong(cur.getString(3));
////                    if (mChats.delete_dialog_time.containsKey(userId)) {
////                        long deletetime = mChats.delete_dialog_time.get(userId);
////                        if (messageTime >= deletetime) {
////                            mChatList.put(cur.getString(1), mChats);
////                        }
//                if (!mChats.last_message.equals(FirebaseChatConstants.DEFAULT_MESSAGE_REGEX)) {
////                    mChatList.put(cur.getString(1), mChats);
//                    if (cur.getString(17).equals("0"))
//                        mChatList.put(cur.getString(1), mChats);
//                }
////                    }
////                }
//                cur.moveToNext();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cur != null && !cur.isClosed()) {
//                cur.close();
//            }
//        }
//        return mChatList;
//    }
//
//    public ChatsModel getChatDialog(String participants, String userId, String otherUserId) {
//        SQLiteDatabase db_read = this.getReadableDatabase();
//        Cursor cur = null;
//        ChatsModel mChats = null;
//        try {
//            String qry = "Select * from " + CHATS_TABLE + " where " + PARTICIPANT_IDS + " = '" + participants + "'";
//            cur = db_read.rawQuery(qry, null);
//            if (cur.getCount() > 0) {
//                cur.moveToFirst();
//                while (!cur.isAfterLast()) {
//                    mChats = new ChatsModel();
//                    mChats.chat_dialog_id = cur.getString(1);
//                    mChats.last_message = cur.getString(2);
//
//                    mChats.last_message_time = new HashMap<>();
//                    mChats.last_message_time.put(otherUserId, Long.parseLong(cur.getString(3)));
//                    mChats.last_message_time.put(userId, Long.parseLong(cur.getString(3)));
//
//                    mChats.last_message_sender_id = cur.getString(4);
//                    mChats.last_message_id = cur.getString(5);
//                    mChats.participant_ids = cur.getString(7);
//
//                    mChats.name = new HashMap<>();
//                    mChats.name.put(otherUserId, cur.getString(8));
//                    mChats.name.put(userId, "");
//
//                    mChats.last_message_type = new HashMap<>();
//                    mChats.last_message_type.put(userId, cur.getString(6));
//                    mChats.last_message_type.put(otherUserId, cur.getString(6));
//
//                    mChats.last_message_data = new HashMap<>();
//                    mChats.last_message_data.put(userId, cur.getString(18));
//                    mChats.last_message_data.put(otherUserId, cur.getString(18));
//
//                    mChats.profile_pic = new HashMap<>();
//                    mChats.profile_pic.put(otherUserId, cur.getString(9));
//                    mChats.profile_pic.put(userId, "");
//
//                    mChats.delete_dialog_time = new HashMap<>();
//                    mChats.delete_dialog_time.put(otherUserId, Long.parseLong(cur.getString(10)));
//                    mChats.delete_dialog_time.put(userId, Long.parseLong(cur.getString(10)));
//
//                    mChats.user_type = new HashMap<>();
//                    mChats.user_type.put(userId, cur.getString(11));
//                    mChats.user_type.put(otherUserId, cur.getString(12));
//
//                    mChats.rating = new HashMap<>();
//                    mChats.rating.put(userId, cur.getString(14));
//                    mChats.rating.put(otherUserId, "0");
//
//                    mChats.opponent_user_id = cur.getString(13);
//
//                    mChats.unread_count = getUnreadCount(cur.getString(1));
//                    mChats.block_status = getBlockStatus(cur.getString(1));
//
//                    mChats.message_rating_count = new HashMap<>();
//                    mChats.message_rating_count.put(userId, Integer.parseInt(cur.getString(15)));
//                    mChats.message_rating_count.put(otherUserId, Integer.parseInt(cur.getString(16)));
//
//                    mChats.delete_outer_dialog = new HashMap<>();
//                    mChats.delete_outer_dialog.put(userId, cur.getString(17));
//                    mChats.delete_outer_dialog.put(otherUserId, "0");
//
//                    cur.moveToNext();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cur != null && !cur.isClosed()) {
//                cur.close();
//            }
//        }
//        return mChats;
//    }
//
//    public HashMap<String, Integer> getUnreadCount(String dialogId) {
//        SQLiteDatabase db_read = this.getReadableDatabase();
//        Cursor cur = null;
//        HashMap<String, Integer> list = new HashMap<String, Integer>();
//        try {
//            String qry = "select * from " + UNREAD_COUNT_TABLE + " where " + CHAT_DIALOG_ID + " = '" + dialogId + "'";
//            cur = db_read.rawQuery(qry, null);
//            cur.moveToFirst();
//            while (!cur.isAfterLast()) {
//                list.put(cur.getString(2), cur.getInt(3));
//                cur.moveToNext();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cur != null && !cur.isClosed()) {
//                cur.close();
//            }
//        }
//        return list;
//    }
//
//    public HashMap<String, String> getBlockStatus(String dialogId) {
//        SQLiteDatabase db_read = this.getReadableDatabase();
//        Cursor cur = null;
//        HashMap<String, String> list = new HashMap<String, String>();
//        try {
//            String qry = "select * from " + BLOCK_TABLE + " where " + CHAT_DIALOG_ID + " = '" + dialogId + "'";
//            cur = db_read.rawQuery(qry, null);
//            cur.moveToFirst();
//            while (!cur.isAfterLast()) {
//                list.put(cur.getString(2), cur.getString(3));
//                cur.moveToNext();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cur != null && !cur.isClosed()) {
//                cur.close();
//            }
//        }
//        return list;
//    }
//
//    public String getUserBlockStatus(String dialogId) {
//        SQLiteDatabase db_read = this.getReadableDatabase();
//        Cursor cur = null;
//        String list = "";
//        try {
//            String qry = "select * from " + BLOCK_TABLE + " where " + CHAT_DIALOG_ID + " = '" + dialogId + "'";
//            cur = db_read.rawQuery(qry, null);
//            cur.moveToFirst();
//            while (!cur.isAfterLast()) {
//                if (!utils.getString("user_id", "").equals(cur.getString(2))) {
//                    list = cur.getString(3);
//                    break;
//                }
//                cur.moveToNext();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cur != null && !cur.isClosed()) {
//                cur.close();
//            }
//        }
//        return list;
//    }
//
//    public LinkedHashMap<String, MessagesModel> getAllMessages(String chatDialogId, String userId, int limit, long dialogDeleteTime, String otherUserId) {
//        SQLiteDatabase db_read = this.getReadableDatabase();
//        Cursor cur = null;
//        SimpleDateFormat today_date_format = new SimpleDateFormat("hh:mm aa", Locale.US);
//        LinkedHashMap<String, MessagesModel> mMessageList = new LinkedHashMap<>();
//        List<MessagesModel> localMessage = new ArrayList<>();
//        try {
//            String qry = "select * from " + MESSAGES_TABLE + " where " + CHAT_DIALOG_ID
//                    + " = '" + chatDialogId + "' order by " + ID + " desc limit " + limit;
//            cur = db_read.rawQuery(qry, null);
//            cur.moveToFirst();
//            while (!cur.isAfterLast()) {
//                if (!("" + cur.getString(10)).equals("1")) {
//                    Calendar calDb = Calendar.getInstance();
//                    long timeinMillis;
//                    try {
//                        timeinMillis = Long.parseLong(cur.getString(4));
//                    } catch (Exception e) {
//                        timeinMillis = calDb.getTimeInMillis();
//                    }
//                    calDb.setTimeInMillis(timeinMillis);
//
//                    MessagesModel mMessage = new MessagesModel();
//                    mMessage.message_id = cur.getString(1);
//                    mMessage.message = cur.getString(2);
//                    mMessage.message_type = cur.getString(3);
//                    mMessage.message_time = cur.getString(4);
//                    mMessage.firebase_message_time = Long.parseLong(cur.getString(5));
//                    mMessage.chat_dialog_id = cur.getString(6);
//                    mMessage.sender_id = cur.getString(7);
//                    mMessage.message_status = cur.getInt(8);
//                    mMessage.attachment_url = cur.getString(9);
//
//                    mMessage.message_deleted = new HashMap<>();
//                    mMessage.message_deleted.put(userId, cur.getString(10));
//                    mMessage.message_deleted.put(otherUserId, "");
//
//                    mMessage.favourite_message = new HashMap<>();
//                    mMessage.favourite_message.put(userId, cur.getString(11));
//                    mMessage.favourite_message.put(otherUserId, "");
//
//                    mMessage.is_header = false;
//                    mMessage.attachment_progress = cur.getString(12);
//                    mMessage.attachment_path = cur.getString(13);
//                    mMessage.attachment_status = cur.getString(14);
//                    mMessage.show_message_datetime = today_date_format.format(calDb.getTime());
//                    mMessage.custom_data = cur.getString(15);
//                    mMessage.receiver_id = cur.getString(16);
//
//                    if (!mMessage.message_deleted.get(userId).equals("1")) {
//                        long messageTime = mMessage.firebase_message_time;
//                        if (messageTime >= dialogDeleteTime) {
//                            localMessage.add(mMessage);
//                        }
//                    }
//                }
//                cur.moveToNext();
//            }
//            Collections.reverse(localMessage);
//            mMessageList = createHeader(localMessage);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cur != null && !cur.isClosed()) {
//                cur.close();
//            }
//        }
//        return mMessageList;
//    }
//
//    public List<MessagesModel> getAllMessagesArray(String chatDialogId, String userId, String type,
//                                                   long dialogDeleteTime, String otherUserId) {
//        SQLiteDatabase db_read = this.getReadableDatabase();
//        Cursor cur = null;
//        SimpleDateFormat today_date_format = new SimpleDateFormat("hh:mm aa", Locale.US);
//        List<MessagesModel> localMessage = new ArrayList<>();
//        try {
//
//            String qry = "select * from " + MESSAGES_TABLE + " where " + CHAT_DIALOG_ID
//                    + " = '" + chatDialogId + "' and " + MESSAGE_TYPE + " = '" + type + "'";
//            cur = db_read.rawQuery(qry, null);
//            cur.moveToFirst();
//            while (!cur.isAfterLast()) {
//                if (!("" + cur.getString(10)).equals("1")) {
//                    Calendar calDb = Calendar.getInstance();
//                    long timeinMillis;
//                    try {
//                        timeinMillis = Long.parseLong(cur.getString(4));
//                    } catch (Exception e) {
//                        timeinMillis = calDb.getTimeInMillis();
//                    }
//                    calDb.setTimeInMillis(timeinMillis);
//
//                    MessagesModel mMessage = new MessagesModel();
//                    mMessage.message_id = cur.getString(1);
//                    mMessage.message = cur.getString(2);
//                    mMessage.message_type = cur.getString(3);
//                    mMessage.message_time = cur.getString(4);
//                    mMessage.firebase_message_time = Long.parseLong(cur.getString(5));
//                    mMessage.chat_dialog_id = cur.getString(6);
//                    mMessage.sender_id = cur.getString(7);
//                    mMessage.message_status = cur.getInt(8);
//                    mMessage.attachment_url = cur.getString(9);
//
//                    mMessage.message_deleted = new HashMap<>();
//                    mMessage.message_deleted.put(userId, cur.getString(10));
//                    mMessage.message_deleted.put(otherUserId, "");
//
//                    mMessage.favourite_message = new HashMap<>();
//                    mMessage.favourite_message.put(userId, cur.getString(11));
//                    mMessage.favourite_message.put(otherUserId, "");
//
//                    mMessage.is_header = false;
//                    mMessage.attachment_progress = cur.getString(12);
//                    mMessage.attachment_path = cur.getString(13);
//                    mMessage.attachment_status = cur.getString(14);
//                    mMessage.show_message_datetime = today_date_format.format(calDb.getTime());
//                    mMessage.custom_data = cur.getString(15);
//                    mMessage.receiver_id = cur.getString(16);
//
//                    if (!mMessage.message_deleted.get(userId).equals("1")) {
//                        long messageTime = mMessage.firebase_message_time;
//                        if (messageTime >= dialogDeleteTime) {
//                            localMessage.add(mMessage);
//                        }
//                    }
//                }
//                cur.moveToNext();
//            }
//            Collections.reverse(localMessage);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cur != null && !cur.isClosed()) {
//                cur.close();
//            }
//        }
//        return localMessage;
//    }
//
//
//    public LinkedHashMap<String, MessagesModel> getAllFavouriteMessages(String chatDialogId, String userId, long dialogDeleteTime, String otherUserId, String status) {
//        SQLiteDatabase db_read = this.getReadableDatabase();
//        Cursor cur = null;
//        SimpleDateFormat today_date_format = new SimpleDateFormat("hh:mm aa", Locale.US);
//        LinkedHashMap<String, MessagesModel> mMessageList = new LinkedHashMap<>();
//        List<MessagesModel> localMessage = new ArrayList<>();
//        try {
//            String qry = "select * from " + MESSAGES_TABLE + " where "
//                    + CHAT_DIALOG_ID + " = '" + chatDialogId + "' and "
//                    + FAVOURITE_MESSAGE + " = '" + status
//                    + "' order by " + ID;
//            cur = db_read.rawQuery(qry, null);
//            cur.moveToFirst();
//            while (!cur.isAfterLast()) {
//                if (!("" + cur.getString(10)).equals("1")) {
//                    Calendar calDb = Calendar.getInstance();
//                    long timeinMillis;
//                    try {
//                        timeinMillis = Long.parseLong(cur.getString(4));
//                    } catch (Exception e) {
//                        timeinMillis = calDb.getTimeInMillis();
//                    }
//                    calDb.setTimeInMillis(timeinMillis);
//
//                    MessagesModel mMessage = new MessagesModel();
//                    mMessage.message_id = cur.getString(1);
//                    mMessage.message = cur.getString(2);
//                    mMessage.message_type = cur.getString(3);
//                    mMessage.message_time = cur.getString(4);
//                    mMessage.firebase_message_time = Long.parseLong(cur.getString(5));
//                    mMessage.chat_dialog_id = cur.getString(6);
//                    mMessage.sender_id = cur.getString(7);
//                    mMessage.message_status = cur.getInt(8);
//                    mMessage.attachment_url = cur.getString(9);
//
//                    mMessage.message_deleted = new HashMap<>();
//                    mMessage.message_deleted.put(userId, cur.getString(10));
//                    mMessage.message_deleted.put(otherUserId, "");
//
//                    mMessage.favourite_message = new HashMap<>();
//                    mMessage.favourite_message.put(userId, cur.getString(11));
//                    mMessage.favourite_message.put(otherUserId, "");
//
//                    mMessage.is_header = false;
//                    mMessage.attachment_progress = cur.getString(12);
//                    mMessage.attachment_path = cur.getString(13);
//                    mMessage.attachment_status = cur.getString(14);
//                    mMessage.show_message_datetime = today_date_format.format(calDb.getTime());
//                    mMessage.custom_data = cur.getString(15);
//                    mMessage.receiver_id = cur.getString(16);
//
//                    if (!mMessage.message_deleted.get(userId).equals("1")) {
//                        long messageTime = mMessage.firebase_message_time;
//                        if (messageTime >= dialogDeleteTime) {
//                            localMessage.add(mMessage);
//                        }
//                    }
//                }
//                cur.moveToNext();
//            }
//            mMessageList = createHeader(localMessage);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cur != null && !cur.isClosed()) {
//                cur.close();
//            }
//        }
//        return mMessageList;
//    }
//
//    LinkedHashMap<String, MessagesModel> createHeader(List<MessagesModel> messages) {
//        LinkedHashMap<String, MessagesModel> mMessageList = new LinkedHashMap<>();
//        SimpleDateFormat chat_date_format = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
//        SimpleDateFormat today_date_format = new SimpleDateFormat("hh:mm aa", Locale.US);
//        SimpleDateFormat show_dateheader_format = new SimpleDateFormat("dd MMM yyyy", Locale.US);
//
//        Calendar cal = Calendar.getInstance();
//        String today = chat_date_format.format(cal.getTime());
//
//        Calendar cal1 = Calendar.getInstance();
//        cal1.add(Calendar.DATE, -1);
//        String yesterday = chat_date_format.format(cal1.getTime());
//
//        String lastHeader = "";
//
//        for (MessagesModel message : messages) {
//            Calendar calDb = Calendar.getInstance();
//            long timeinMillis;
//            try {
//                timeinMillis = Long.parseLong(message.message_time);
//            } catch (Exception e) {
//                timeinMillis = calDb.getTimeInMillis();
//            }
//            calDb.setTimeInMillis(timeinMillis);
//            String dbDate = chat_date_format.format(calDb.getTime());
//
//            if (!TextUtils.equals(lastHeader, dbDate)) {
//                lastHeader = dbDate;
//
//                MessagesModel mMessage = new MessagesModel();
//                mMessage.is_header = true;
//                if (dbDate.equals(today)) {
//                    mMessage.show_header_text = "Today";
//                    mMessage.show_message_datetime = today_date_format.format(calDb.getTime());
//                } else if (dbDate.equals(yesterday)) {
//                    mMessage.show_header_text = "Yesterday";
//                    mMessage.show_message_datetime = today_date_format.format(calDb.getTime());
//                } else {
//                    mMessage.show_header_text = show_dateheader_format.format(calDb.getTime());
//                    mMessage.show_message_datetime = today_date_format.format(calDb.getTime());
//                }
//                mMessageList.put(lastHeader, mMessage);
//            }
//            MessagesModel mMessage = message;
//            mMessage.is_header = false;
//            mMessage.show_message_datetime = today_date_format.format(calDb.getTime());
//            mMessageList.put(message.message_id, mMessage);
//        }
//        return mMessageList;
//    }
//
//    public void addMessage(MessagesModel msg, String userId) {
//        SQLiteDatabase db_write = this.getWritableDatabase();
//        SQLiteDatabase db_read = this.getReadableDatabase();
//        db_write.beginTransaction();
//        Cursor cur = null;
//        try {
//            ContentValues values = new ContentValues();
//            values.put(MESSAGE, msg.message);
//            values.put(MESSAGE_TYPE, msg.message_type);
//            if (msg.sender_id.equals(userId)) {
//                values.put(FIRBASE_MESSAGE_TIME, msg.firebase_message_time);
//            }
//            values.put(CHAT_DIALOG_ID, msg.chat_dialog_id);
//            values.put(SENDER_ID, msg.sender_id);
//            values.put(MESSAGE_STATUS, msg.message_status);
//            values.put(ATTACHMENT_URL, msg.attachment_url);
//            values.put(RECEIVER_ID, msg.receiver_id);
//
//            String qry = "select * from " + MESSAGES_TABLE + " where " + MESSAGE_ID + " = '" + msg.message_id + "'";
//            cur = db_read.rawQuery(qry, null);
//            if (cur.getCount() > 0) {
//                db_write.update(MESSAGES_TABLE, values, MESSAGE_ID + " = '" + msg.message_id + "'", null);
//            } else {
//                values.put(MESSAGE_DELETED, msg.message_deleted.get(userId));
//                values.put(FAVOURITE_MESSAGE, msg.favourite_message.get(userId));
//                values.put(MESSAGE_ID, msg.message_id);
//                if (msg.sender_id.equals(userId)) {
//                    values.put(MESSAGE_TIME, "" + msg.message_time);
//                } else {
//                    values.put(MESSAGE_TIME, "" + (Calendar.getInstance()).getTimeInMillis()); // comment for show local time
//                }
//                values.put(FIRBASE_MESSAGE_TIME, msg.firebase_message_time);
//                if (TextUtils.isEmpty(msg.attachment_path)) {
//                    values.put(ATTACHMENT_PATH, "");
//                } else {
//                    values.put(ATTACHMENT_PATH, msg.attachment_path);
//                }
//                if (TextUtils.isEmpty(msg.attachment_status)) {
//                    values.put(ATTACHMENT_STATUS, FirebaseChatConstants.FILE_EREROR);
//                } else {
//                    values.put(ATTACHMENT_STATUS, msg.attachment_status);
//                }
//                if (TextUtils.isEmpty(msg.attachment_progress)) {
//                    values.put(ATTACHMENT_PROGRESS, "0");
//                } else {
//                    values.put(ATTACHMENT_PROGRESS, msg.attachment_progress);
//                }
//                values.put(CUSTOM_DATA, msg.custom_data);
//                db_write.insertOrThrow(MESSAGES_TABLE, null, values);
//            }
//            db_write.setTransactionSuccessful();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cur != null && !cur.isClosed()) {
//                cur.close();
//            }
//            db_write.endTransaction();
//        }
//    }
//
//    public void updateMessage(MessagesModel msg, String userId) {
//        SQLiteDatabase db_write = this.getWritableDatabase();
//        SQLiteDatabase db_read = this.getReadableDatabase();
//        db_write.beginTransaction();
//        Cursor cur = null;
//        try {
//            ContentValues values = new ContentValues();
//            values.put(MESSAGE, msg.message);
//            values.put(MESSAGE_TYPE, msg.message_type);
//            if (msg.sender_id.equals(userId)) {
//                values.put(FIRBASE_MESSAGE_TIME, msg.firebase_message_time);
//            }
//            values.put(CHAT_DIALOG_ID, msg.chat_dialog_id);
//            values.put(SENDER_ID, msg.sender_id);
//            values.put(MESSAGE_STATUS, msg.message_status);
//            values.put(ATTACHMENT_URL, msg.attachment_url);
//            values.put(RECEIVER_ID, msg.receiver_id);
//            values.put(MESSAGE_DELETED, msg.message_deleted.get(userId));
//            values.put(FAVOURITE_MESSAGE, msg.favourite_message.get(userId));
//            values.put(FIRBASE_MESSAGE_TIME, msg.firebase_message_time);
//
//            String qry = "select * from " + MESSAGES_TABLE + " where " + MESSAGE_ID + " = '" + msg.message_id + "'";
//            cur = db_read.rawQuery(qry, null);
//            if (cur.getCount() > 0) {
//                db_write.update(MESSAGES_TABLE, values, MESSAGE_ID + " = '" + msg.message_id + "'", null);
//            }
//            db_write.setTransactionSuccessful();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cur != null && !cur.isClosed()) {
//                cur.close();
//            }
//            db_write.endTransaction();
//        }
//    }
//
//    public void addMessagesHistory(List<MessageHistoryModel.ResponseBean> messages, String userId) {
//        SQLiteDatabase db_write = this.getWritableDatabase();
//        SQLiteDatabase db_read = this.getReadableDatabase();
//        db_write.beginTransaction();
//        Cursor cur = null;
//        try {
//            for (int i = 0; i < messages.size(); i++) {
//                MessageHistoryModel.ResponseBean msg = messages.get(i);
//                ContentValues values = new ContentValues();
//                values.put(MESSAGE, msg.getMessage());
//                values.put(MESSAGE_TYPE, msg.getMessage_type());
//                values.put(MESSAGE_TIME, msg.getMessage_time()); // comment for show local time
//                values.put(FIRBASE_MESSAGE_TIME, msg.getFirebase_message_time());
//                values.put(CHAT_DIALOG_ID, msg.getChat_dialog_id());
//                values.put(SENDER_ID, msg.getSender_id());
//                values.put(MESSAGE_STATUS, msg.getMessage_status());
//                values.put(ATTACHMENT_URL, msg.getAttachment_url());
//                values.put(RECEIVER_ID, msg.getReceiver_id());
//
//                String qry = "select * from " + MESSAGES_TABLE + " where " + MESSAGE_ID + " = '" + msg.getMessage_id() + "'";
//                cur = db_read.rawQuery(qry, null);
//                if (cur.getCount() > 0) {
//                    db_write.update(MESSAGES_TABLE, values, MESSAGE_ID + " = '" + msg.getMessage_id() + "'", null);
//                } else {
//                    for (int j = 0; j < msg.getMessage_deleted().size(); j++) {
//                        if (msg.getMessage_deleted().get(j).getKey().equals(userId)) {
//                            values.put(MESSAGE_DELETED, msg.getMessage_deleted().get(j).getValue());
//                            break;
//                        }
//                    }
////                    for (int j = 0; j < msg.getFavourite_message().size(); j++) {
////                        if (msg.getFavourite_message().get(j).getKey().equals(userId)) {
////                            values.put(FAVOURITE_MESSAGE, msg.getFavourite_message().get(j).getValue());
////                            break;
////                        }
////                    }
//                    values.put(MESSAGE_ID, msg.getMessage_id());
//                    values.put(ATTACHMENT_PATH, "");
//                    values.put(ATTACHMENT_STATUS, FirebaseChatConstants.FILE_EREROR);
//                    values.put(ATTACHMENT_PROGRESS, "0");
//                    values.put(CUSTOM_DATA, "");
//                    db_write.insertOrThrow(MESSAGES_TABLE, null, values);
//                }
//            }
//            db_write.setTransactionSuccessful();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cur != null && !cur.isClosed()) {
//                cur.close();
//            }
//            db_write.endTransaction();
//        }
//    }
//
//    public MessagesModel getSingleMessage(String msgId, String userId) {
//        SQLiteDatabase db_read = this.getReadableDatabase();
//        Cursor cur = null;
//        MessagesModel mMessage = null;
//        try {
//            String qry = "select * from " + MESSAGES_TABLE + " where " + MESSAGE_ID + " = '" + msgId + "'";
//            cur = db_read.rawQuery(qry, null);
//            cur.moveToFirst();
//            while (!cur.isAfterLast()) {
//                if (!("" + cur.getString(10)).equals("1")) {
//                    Calendar calDb = Calendar.getInstance();
//                    long timeinMillis;
//                    try {
//                        timeinMillis = Long.parseLong(cur.getString(4));
//                    } catch (Exception e) {
//                        timeinMillis = calDb.getTimeInMillis();
//                    }
//                    calDb.setTimeInMillis(timeinMillis);
//
//                    mMessage = new MessagesModel();
//                    mMessage.message_id = cur.getString(1);
//                    mMessage.message = cur.getString(2);
//                    mMessage.message_type = cur.getString(3);
//                    mMessage.message_time = cur.getString(4);
//                    mMessage.firebase_message_time = Long.parseLong(cur.getString(5));
//                    mMessage.chat_dialog_id = cur.getString(6);
//                    mMessage.sender_id = cur.getString(7);
//                    mMessage.message_status = cur.getInt(8);
//                    mMessage.attachment_url = cur.getString(9);
//
//                    String otherUserId = "";
//                    String[] particID = cur.getString(6).split(",");
//                    for (String id : particID) {
//                        String[] userIds = id.split("_");
//                        if (!(userIds[0].trim()).equalsIgnoreCase(userId)) {
//                            otherUserId = userIds[0].trim();
//                            break;
//                        }
//                    }
//
//                    mMessage.message_deleted = new HashMap<>();
//                    mMessage.message_deleted.put(userId, cur.getString(10));
//                    mMessage.message_deleted.put(otherUserId, "");
//
//                    mMessage.favourite_message = new HashMap<>();
//                    mMessage.favourite_message.put(userId, cur.getString(11));
//                    mMessage.favourite_message.put(otherUserId, "");
//
//                    mMessage.is_header = false;
//                    mMessage.attachment_progress = cur.getString(12);
//                    mMessage.attachment_path = cur.getString(13);
//                    mMessage.attachment_status = cur.getString(14);
//                    mMessage.show_message_datetime = today_date_format.format(calDb.getTime());
//                    mMessage.custom_data = cur.getString(15);
//                    mMessage.receiver_id = cur.getString(16);
//                }
//                cur.moveToNext();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cur != null && !cur.isClosed()) {
//                cur.close();
//            }
//        }
//        return mMessage;
//    }
//
//    public MessagesModel getPendingUploads(String attachment_path, String userId) {
//        SQLiteDatabase db_read = this.getReadableDatabase();
//        Cursor cur = null;
//        MessagesModel mMessage = null;
//        try {
//            String qry = "select * from " + MESSAGES_TABLE + " where "
//                    + ATTACHMENT_PATH + " = '" + attachment_path + "' and "
//                    + ATTACHMENT_STATUS + " = '" + FirebaseChatConstants.FILE_EREROR + "' and "
//                    + SENDER_ID + " = '" + userId + "' limit 1";
//            cur = db_read.rawQuery(qry, null);
//
//            cur.moveToFirst();
//            while (!cur.isAfterLast()) {
//                if (!("" + cur.getString(10)).equals("1")) {
//                    Calendar calDb = Calendar.getInstance();
//                    long timeinMillis;
//                    try {
//                        timeinMillis = Long.parseLong(cur.getString(4));
//                    } catch (Exception e) {
//                        timeinMillis = calDb.getTimeInMillis();
//                    }
//                    calDb.setTimeInMillis(timeinMillis);
//
//                    mMessage = new MessagesModel();
//                    mMessage.message_id = cur.getString(1);
//                    mMessage.message = cur.getString(2);
//                    mMessage.message_type = cur.getString(3);
//                    mMessage.message_time = cur.getString(4);
//                    mMessage.firebase_message_time = Long.parseLong(cur.getString(5));
//                    mMessage.chat_dialog_id = cur.getString(6);
//                    mMessage.sender_id = cur.getString(7);
//                    mMessage.message_status = cur.getInt(8);
//                    mMessage.attachment_url = cur.getString(9);
//
//                    String otherUserId = "";
//                    String[] particID = cur.getString(6).split(",");
//                    for (String id : particID) {
//                        String[] userIds = id.split("_");
//                        if (!(userIds[0].trim()).equalsIgnoreCase(userId)) {
//                            otherUserId = userIds[0].trim();
//                            break;
//                        }
//                    }
//
//                    mMessage.message_deleted = new HashMap<>();
//                    mMessage.message_deleted.put(userId, cur.getString(10));
//                    mMessage.message_deleted.put(otherUserId, "");
//
//                    mMessage.favourite_message = new HashMap<>();
//                    mMessage.favourite_message.put(userId, cur.getString(11));
//                    mMessage.favourite_message.put(otherUserId, "");
//
//                    mMessage.is_header = false;
//                    mMessage.attachment_progress = cur.getString(12);
//                    mMessage.attachment_path = cur.getString(13);
//                    mMessage.attachment_status = cur.getString(14);
//                    mMessage.show_message_datetime = today_date_format.format(calDb.getTime());
//                    mMessage.custom_data = cur.getString(15);
//                    mMessage.receiver_id = cur.getString(16);
//                }
//                cur.moveToNext();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cur != null && !cur.isClosed()) {
//                cur.close();
//            }
//        }
//        return mMessage;
//    }
//
//    public Long getMessageTime(String chatDialogId) {
//        SQLiteDatabase db_read = this.getReadableDatabase();
//        Cursor cur = null;
//        String status = "0";
//        Long messageTime = 0L;
//        try {
//            String qry = "select * from " + MESSAGES_TABLE + " where "
//                    + CHAT_DIALOG_ID + " = '" + chatDialogId + "' and "
//                    + MESSAGE_DELETED + " = '" + status
//                    + "' order by " + FIRBASE_MESSAGE_TIME + " desc limit 1";
//            cur = db_read.rawQuery(qry, null);
//
//            cur.moveToFirst();
//            while (!cur.isAfterLast()) {
//                if (!("" + cur.getString(10)).equals("1")) {
//                    messageTime = Long.parseLong(cur.getString(4));
//                }
//                cur.moveToNext();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cur != null && !cur.isClosed()) {
//                cur.close();
//            }
//        }
//        return messageTime;
//    }
//
//    public MessagesModel getPendingUploads(String userId) {
//        SQLiteDatabase db_read = this.getReadableDatabase();
//        Cursor cur = null;
//        MessagesModel mMessage = null;
//        try {
//            String qry = "select * from " + MESSAGES_TABLE + " where "
//                    + ATTACHMENT_STATUS + " = '" + FirebaseChatConstants.FILE_UPLOADING + "' and "
//                    + SENDER_ID + " = '" + userId
//                    + "' order by " + MESSAGE_TIME + " desc limit 1";
//            cur = db_read.rawQuery(qry, null);
//
//            cur.moveToFirst();
//            while (!cur.isAfterLast()) {
//                if (!("" + cur.getString(10)).equals("1")) {
//                    Calendar calDb = Calendar.getInstance();
//                    long timeinMillis;
//                    try {
//                        timeinMillis = Long.parseLong(cur.getString(4));
//                    } catch (Exception e) {
//                        timeinMillis = calDb.getTimeInMillis();
//                    }
//                    calDb.setTimeInMillis(timeinMillis);
//
//                    mMessage = new MessagesModel();
//                    mMessage.message_id = cur.getString(1);
//                    mMessage.message = cur.getString(2);
//                    mMessage.message_type = cur.getString(3);
//                    mMessage.message_time = cur.getString(4);
//                    mMessage.firebase_message_time = Long.parseLong(cur.getString(5));
//                    mMessage.chat_dialog_id = cur.getString(6);
//                    mMessage.sender_id = cur.getString(7);
//                    mMessage.message_status = cur.getInt(8);
//                    mMessage.attachment_url = cur.getString(9);
//
//                    String otherUserId = "";
//                    String[] particID = cur.getString(6).split(",");
//                    for (String id : particID) {
//                        String[] userIds = id.split("_");
//                        if (!(userIds[0].trim()).equalsIgnoreCase(userId)) {
//                            otherUserId = userIds[0].trim();
//                            break;
//                        }
//                    }
//
//                    mMessage.message_deleted = new HashMap<>();
//                    mMessage.message_deleted.put(userId, cur.getString(10));
//                    mMessage.message_deleted.put(otherUserId, "");
//
//                    mMessage.favourite_message = new HashMap<>();
//                    mMessage.favourite_message.put(userId, cur.getString(11));
//                    mMessage.favourite_message.put(otherUserId, "");
//
//                    mMessage.is_header = false;
//                    mMessage.attachment_progress = cur.getString(12);
//                    mMessage.attachment_path = cur.getString(13);
//                    mMessage.attachment_status = cur.getString(14);
//                    mMessage.show_message_datetime = today_date_format.format(calDb.getTime());
//                    mMessage.custom_data = cur.getString(15);
//                    mMessage.receiver_id = cur.getString(16);
//                }
//                cur.moveToNext();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cur != null && !cur.isClosed()) {
//                cur.close();
//            }
//        }
//        return mMessage;
//    }
//
//    public int deleteSingleMessage(String messageId) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.beginTransaction();
//        int rowCount = 0;
//        Cursor data = null;
//        try {
//            rowCount = db.delete(MESSAGES_TABLE, MESSAGE_ID + " = '" + messageId + "'", null);
//            db.setTransactionSuccessful();
//        } catch (Exception e) {
//            Log.e("Exception", "is " + e);
//        } finally {
//            if (data != null && !data.isClosed()) {
//                data.close();
//            }
//            db.endTransaction();
//        }
//        return rowCount;
//    }
//
//    public void changeUploadStatus(String messageId, String attachmentStatus, int messageStatus) {
//        SQLiteDatabase db_write = this.getWritableDatabase();
//        db_write.beginTransaction();
//        try {
//            ContentValues values = new ContentValues();
//            values.put(ATTACHMENT_STATUS, attachmentStatus);
//            values.put(MESSAGE_STATUS, messageStatus);
//            db_write.update(MESSAGES_TABLE, values, MESSAGE_ID + " = '" + messageId + "'", null);
//            db_write.setTransactionSuccessful();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            db_write.endTransaction();
//        }
//    }
//
//    public void changeDownloadStatus(String messageID, String attachmentStatus, String downloadPath) {
//        SQLiteDatabase db_write = this.getWritableDatabase();
//        db_write.beginTransaction();
//        try {
//            ContentValues values = new ContentValues();
//            values.put(ATTACHMENT_STATUS, attachmentStatus);
//            values.put(ATTACHMENT_PATH, downloadPath);
//
//            db_write.update(MESSAGES_TABLE, values, MESSAGE_ID + " = '" + messageID + "'", null);
//
//            db_write.setTransactionSuccessful();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            db_write.endTransaction();
//        }
//    }
//
//    public void changeDownloadStatus(String messageID, String upload_status, String downloadPath, String thumbPath) {
//        SQLiteDatabase db_write = this.getWritableDatabase();
//        db_write.beginTransaction();
//        try {
//            ContentValues values = new ContentValues();
//            values.put(ATTACHMENT_STATUS, upload_status);
//            values.put(ATTACHMENT_PATH, downloadPath);
//            values.put(CUSTOM_DATA, thumbPath);
//
//            db_write.update(MESSAGES_TABLE, values, MESSAGE_ID + " = '" + messageID + "'", null);
//
//            db_write.setTransactionSuccessful();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            db_write.endTransaction();
//        }
//    }
//
//    public void changProgress(String messageId, String progress) {
//        SQLiteDatabase db_write = this.getWritableDatabase();
//        db_write.beginTransaction();
//        try {
//            ContentValues values = new ContentValues();
//            values.put(ATTACHMENT_PROGRESS, progress);
//
//            db_write.update(MESSAGES_TABLE, values, MESSAGE_ID + " = '" + messageId + "'", null);
//
//            db_write.setTransactionSuccessful();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            db_write.endTransaction();
//        }
//    }
//
//    public void changDeleteStatus(String messageId, String status) {
//        SQLiteDatabase db_write = this.getWritableDatabase();
//        db_write.beginTransaction();
//        try {
//            ContentValues values = new ContentValues();
//            values.put(MESSAGE_DELETED, status);
//
//            db_write.update(MESSAGES_TABLE, values, MESSAGE_ID + " = '" + messageId + "'", null);
//
//            db_write.setTransactionSuccessful();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            db_write.endTransaction();
//        }
//    }
//
//    public void changFavouriteStatus(String messageId, String status) {
//        SQLiteDatabase db_write = this.getWritableDatabase();
//        db_write.beginTransaction();
//        try {
//            ContentValues values = new ContentValues();
//            values.put(FAVOURITE_MESSAGE, status);
//
//            db_write.update(MESSAGES_TABLE, values, MESSAGE_ID + " = '" + messageId + "'", null);
//
//            db_write.setTransactionSuccessful();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            db_write.endTransaction();
//        }
//    }
//
//    public ArrayList<String> getImageVideoAttachments(String mDialogID) {
//        SQLiteDatabase db_read = this.getReadableDatabase();
//        Cursor cur = null;
//        ArrayList<String> mAttachmentList = new ArrayList<String>();
//        try {
//
//            String qryAttachment = "Select " + ATTACHMENT_PATH + ", " + ATTACHMENT_URL + ", " + MESSAGE_TYPE + " from " + MESSAGES_TABLE
//                    + " where " + CHAT_DIALOG_ID + " ='" + mDialogID + "'";
//            cur = db_read.rawQuery(qryAttachment, null);
//
//            cur.moveToFirst();
//            while (!cur.isAfterLast()) {
//                if (!TextUtils.isEmpty(cur.getString(0)) && !TextUtils.isEmpty(cur.getString(1))
//                        && (cur.getString(2).equals(FirebaseChatConstants.TYPE_IMAGE) || cur.getString(2).equals(FirebaseChatConstants.TYPE_VIDEO))) {
//                    File ff = new File(cur.getString(0).trim());
//                    if (ff.exists()) {
//                        mAttachmentList.add(cur.getString(0).trim());
//                    }
//                }
//                cur.moveToNext();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cur != null && !cur.isClosed()) {
//                cur.close();
//            }
//        }
//        return mAttachmentList;
//    }
//
//    public ArrayList<String> getFavouriteImageVideoAttachments(String mDialogID) {
//        SQLiteDatabase db_read = this.getReadableDatabase();
//        String value = "1";
//        Cursor cur = null;
//        ArrayList<String> mAttachmentList = new ArrayList<String>();
//        try {
//
//            String qryAttachment = "Select " + ATTACHMENT_PATH + ", " + ATTACHMENT_URL + ", " + MESSAGE_TYPE + " from " + MESSAGES_TABLE
//                    + " where " + CHAT_DIALOG_ID + " ='" + mDialogID + "' and " + FAVOURITE_MESSAGE + " = '" + value + "'";
//            cur = db_read.rawQuery(qryAttachment, null);
//
//            cur.moveToFirst();
//            while (!cur.isAfterLast()) {
//                if (!TextUtils.isEmpty(cur.getString(0)) && !TextUtils.isEmpty(cur.getString(1))
//                        && (cur.getString(2).equals(FirebaseChatConstants.TYPE_IMAGE) || cur.getString(2).equals(FirebaseChatConstants.TYPE_VIDEO))) {
//                    File ff = new File(cur.getString(0).trim());
//                    if (ff.exists()) {
//                        mAttachmentList.add(cur.getString(0).trim());
//                    }
//                }
//                cur.moveToNext();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (cur != null && !cur.isClosed()) {
//                cur.close();
//            }
//        }
//        return mAttachmentList;
//    }
//
//
//    public void clearConversation(String dialogId) {
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.beginTransaction();
//        try {
//
//            String qry3 = "DELETE FROM " + UNREAD_COUNT_TABLE + " where "
//                    + CHAT_DIALOG_ID + " = '" + dialogId + "'";
//            db.execSQL(qry3);
//
//            String qry4 = "DELETE FROM " + MESSAGES_TABLE + " where "
//                    + CHAT_DIALOG_ID + " = '" + dialogId + "'";
//            db.execSQL(qry4);
//
//            db.setTransactionSuccessful();
//        } catch (Exception e) {
//            Log.e("Exception", "is " + e);
//        } finally {
//            db.endTransaction();
//        }
//
//    }
//
//    public void deleteDialogData(String dialogId) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.beginTransaction();
//        try {
//            String chatQuery = "DELETE FROM " + CHATS_TABLE + " where "
//                    + CHAT_DIALOG_ID + " = '" + dialogId + "'";
//            db.execSQL(chatQuery);
//
//            String deleteQuery = "DELETE FROM " + DIALOGS_TABLE + " where "
//                    + CHAT_DIALOG_ID + " = '" + dialogId + "'";
//            db.execSQL(deleteQuery);
//
//            String unreadQuery = "DELETE FROM " + UNREAD_COUNT_TABLE + " where "
//                    + CHAT_DIALOG_ID + " = '" + dialogId + "'";
//            db.execSQL(unreadQuery);
//
//            String blockQuery = "DELETE FROM " + BLOCK_TABLE + " where "
//                    + CHAT_DIALOG_ID + " = '" + dialogId + "'";
//            db.execSQL(blockQuery);
//
//            String messageQuery = "DELETE FROM " + MESSAGES_TABLE + " where "
//                    + CHAT_DIALOG_ID + " = '" + dialogId + "'";
//            db.execSQL(messageQuery);
//
//            db.setTransactionSuccessful();
//        } catch (Exception e) {
//            Log.e("Exception", "is " + e);
//        } finally {
//            db.endTransaction();
//        }
//    }
    public static final String DATABASE = "my_reevuu_coach_local";
    /// Events Table
    static final String POSTS_TABLE = "posts_table";
    static final String POST_ID = "post_id";
    static final String POST_TYPE = "post_type";
    static final String TITLE = "title";
    static final String DESCRIPTION = "description";
    static final String PROFESSION_ID = "profession_id";
    static final String ADDRESS = "address";
    static final String LATITUDE = "latitude";
    static final String LONGITUDE = "longitude";
    static final String DATE_TIME = "date_time";
    static final String URL = "url";
    static final String LIKE_COUNT = "like_count";
    static final String COMMENT_COUNT = "comment_count";
    static final String GOING = "going";
    static final String INTRESTED = "interested";
    static final String IS_BOOKMARKED = "is_bookmarked";
    static final String LIKED = "liked";
    static final String IS_GOING = "is_going";
    static final String SHAREABLE_LINK = "shareable_link";
    /// Images Table
    static final String IMAGES_TABLE = "images_table";
    static final String IMAGE_ID = "image_id";
    static final String IMAGE_URL = "image_url";
    static final String THUMBNAIL_URL = "thumnamil_url";
    /// Going Table
    static final String GOING_TABLE = "going_table";
    static final String FULL_NAME = "full_name";
    static final String AVATAR = "avatar";
    static final String USER_ID = "user_id";
    /// NOtes Table
    static final String NOTES_TABLE = "notes_table";
    static final String NOTES_ID = "notes_id";
    static final String NOTES_NAME = "notes_name";
    static final String NOTES_TITLE = "notes_title";
    static final String NOTES_DESC = "notes_desc";
    static final String NOTES_URL = "notes_url";
    static final String NOTES_TYPE = "notes_type";
    static final String NOTES_CREATED = "notes_created";
    static final String NOTES_UPDATED = "notes_updated";
    static final String NOTES_USER_ID = "note_user_id";
    static final String NOTES_USER_NAME = "note_user_name";
    static final String NOTES_NEW_TITLE = "notes_new_title";
    /// Profile Table
    static final String ID = "id";
    static final String PROFILE_TABLE = "profile_table";
    static final String ONLINE_STATUS = "online_status";
    static final String ACCESS_TOKEN = "access_token";
    static final String USER_NAME = "user_name";
    static final String USER_PIC = "user_pic";
    /// Dialogs Table
    static final String DIALOGS_TABLE = "dialogs_table";
    static final String CHAT_DIALOG_ID = "chat_dialog_id";
    /// Chats Table
    static final String CHATS_TABLE = "chats_table";
    static final String LAST_MESSAGE = "last_message";
    static final String LAST_MESSAGE_TIME = "last_message_time";
    static final String LAST_MESSAGE_SENDER_ID = "last_message_sender_id";
    static final String LAST_MESSAGE_ID = "last_message_id";
    static final String LAST_MESSAGE_TYPE = "last_message_type";
    static final String LAST_MESSAGE_DATA = "last_message_data";
    static final String PARTICIPANT_IDS = "participant_id";
    static final String NAME = "name";
    static final String PROFILE_PIC = "profile_pic";
    static final String DELETE_DIALOG_TIME = "delete_dialog_time";
    static final String USER_TYPE = "user_type";
    static final String OPPONENT_USER_TYPE = "opponent_user_type";
    static final String OPPONENT_USER_ID = "opponent_user_id";
    static final String RATING = "rating";
    static final String OWN_MESSAGE_RATING_COUNT = "own_message_rating_count";
    static final String DELETE_OUTER_DIALOG = "DELETE_OUTER_DIALOG";
    static final String OTHER_MESSAGE_RATING_COUNT = "other_message_rating_count";
    /// Unread Count Table
    static final String UNREAD_COUNT_TABLE = "unread_count_table";
    static final String UNREAD_COUNT = "unread_count";
    /// Block Table
    static final String BLOCK_TABLE = "block_table";
    static final String BLOCK_STATUS = "block_status";
    /// Messages Table
    static final String MESSAGES_TABLE = "messages_table";
    static final String MESSAGE_ID = "message_id";
    static final String MESSAGE = "message";
    static final String MESSAGE_TYPE = "message_type";
    static final String MESSAGE_TIME = "message_time";
    static final String FIRBASE_MESSAGE_TIME = "firebase_message_time";
    static final String SENDER_ID = "sender_id";
    static final String MESSAGE_STATUS = "message_status";
    static final String ATTACHMENT_URL = "attachment_data";
    static final String MESSAGE_DELETED = "message_deleted";
    static final String FAVOURITE_MESSAGE = "favourite_message";
    static final String ATTACHMENT_PROGRESS = "attachment_progress";
    static final String ATTACHMENT_PATH = "attachment_path"; //local path
    static final String ATTACHMENT_STATUS = "attachment_status"; // uploading; error; success;
    static final String CUSTOM_DATA = "custom_data";
    static final String RECEIVER_ID = "receiver_id";
    private static final int dbversion = 1;
    SimpleDateFormat today_date_format = new SimpleDateFormat("hh:mm aa", Locale.US);
    private Utils utils;

    public Database(Context context) {
        super(context, DATABASE, null, dbversion);
        utils = new Utils(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    private void createTables(SQLiteDatabase db) {
        String postsQuery = "create table if not exists " + POSTS_TABLE
                + " (" + POST_ID + " TEXT ,"
                + POST_TYPE + " TEXT ,"
                + TITLE + " TEXT ,"
                + DESCRIPTION + " TEXT ,"
                + PROFESSION_ID + " TEXT ,"
                + ADDRESS + " TEXT ,"
                + LATITUDE + " TEXT ,"
                + LONGITUDE + " TEXT ,"
                + DATE_TIME + " TEXT ,"
                + URL + " TEXT ,"
                + LIKE_COUNT + " TEXT ,"
                + COMMENT_COUNT + " TEXT ,"
                + GOING + " TEXT ,"
                + INTRESTED + " TEXT ,"
                + IS_BOOKMARKED + " TEXT ,"
                + LIKED + " TEXT ,"
                + IS_GOING + " TEXT ,"
                + SHAREABLE_LINK + " TEXT )";
        db.execSQL(postsQuery);

        String imagesQuery = "create table if not exists " + IMAGES_TABLE
                + " (" + IMAGE_ID + " TEXT ,"
                + IMAGE_URL + " TEXT ,"
                + THUMBNAIL_URL + " TEXT ,"
                + POST_ID + " TEXT ,"
                + POST_TYPE + " TEXT )";
        db.execSQL(imagesQuery);

        String goingQuery = "create table if not exists " + GOING_TABLE
                + " (" + FULL_NAME + " TEXT ,"
                + AVATAR + " TEXT ,"
                + USER_ID + " TEXT ,"
                + POST_ID + " TEXT )";
        db.execSQL(goingQuery);

        String notesQuery = "create table if not exists " + NOTES_TABLE
                + " (" + NOTES_ID + " TEXT ,"
                + NOTES_NAME + " TEXT ,"
                + NOTES_TITLE + " TEXT ,"
                + NOTES_DESC + " TEXT ,"
                + NOTES_URL + " TEXT ,"
                + NOTES_TYPE + " TEXT ,"
                + NOTES_CREATED + " TEXT ,"
                + NOTES_UPDATED + " TEXT ,"
                + NOTES_USER_ID + " TEXT ,"
                + NOTES_USER_NAME + " TEXT ,"
                + NOTES_NEW_TITLE + " TEXT )";
        db.execSQL(notesQuery);

        String profileQuery = "create table if not exists " + PROFILE_TABLE
                + " (" + ID + " integer primary key AUTOINCREMENT ,"
                + USER_ID + " TEXT ,"
                + ONLINE_STATUS + " TEXT ,"
                + ACCESS_TOKEN + " TEXT ,"
                + USER_NAME + " TEXT ,"
                + USER_PIC + " TEXT )";
        db.execSQL(profileQuery);

        String dialogQuery = "create table if not exists " + DIALOGS_TABLE
                + " (" + ID + " integer primary key AUTOINCREMENT ,"
                + USER_ID + " TEXT ,"
                + CHAT_DIALOG_ID + " TEXT )";
        db.execSQL(dialogQuery);

        String chatsQuery = "create table if not exists " + CHATS_TABLE
                + " (" + ID + " integer primary key AUTOINCREMENT ,"
                + CHAT_DIALOG_ID + " TEXT ,"
                + LAST_MESSAGE + " TEXT ,"
                + LAST_MESSAGE_TIME + " TEXT ,"
                + LAST_MESSAGE_SENDER_ID + " TEXT ,"
                + LAST_MESSAGE_ID + " TEXT ,"
                + LAST_MESSAGE_TYPE + " TEXT ,"
                + PARTICIPANT_IDS + " TEXT ,"
                + NAME + " TEXT ,"
                + PROFILE_PIC + " TEXT ,"
                + DELETE_DIALOG_TIME + " TEXT ,"
                + USER_TYPE + " TEXT ,"
                + OPPONENT_USER_TYPE + " TEXT ,"
                + OPPONENT_USER_ID + " TEXT ,"
                + RATING + " TEXT ,"
                + OWN_MESSAGE_RATING_COUNT + " TEXT ,"
                + OTHER_MESSAGE_RATING_COUNT + " TEXT ,"
                + DELETE_OUTER_DIALOG + " TEXT ,"
                + LAST_MESSAGE_DATA + " TEXT )";
        db.execSQL(chatsQuery);

        String unreadCountQuery = "create table if not exists " + UNREAD_COUNT_TABLE
                + " (" + ID + " integer primary key AUTOINCREMENT ,"
                + CHAT_DIALOG_ID + " TEXT ,"
                + USER_ID + " TEXT ,"
                + UNREAD_COUNT + " integer )";
        db.execSQL(unreadCountQuery);

        String blockQuery = "create table if not exists " + BLOCK_TABLE
                + " (" + ID + " integer primary key AUTOINCREMENT ,"
                + CHAT_DIALOG_ID + " TEXT ,"
                + USER_ID + " TEXT ,"
                + BLOCK_STATUS + " TEXT )";
        db.execSQL(blockQuery);

        String messageQuery = "create table if not exists " + MESSAGES_TABLE
                + " (" + ID + " integer primary key AUTOINCREMENT ,"
                + MESSAGE_ID + " TEXT ,"
                + MESSAGE + " TEXT ,"
                + MESSAGE_TYPE + " TEXT ,"
                + MESSAGE_TIME + " TEXT ,"
                + FIRBASE_MESSAGE_TIME + " TEXT ,"
                + CHAT_DIALOG_ID + " TEXT ,"
                + SENDER_ID + " TEXT ,"
                + MESSAGE_STATUS + " integer ,"
                + ATTACHMENT_URL + " TEXT ,"
                + MESSAGE_DELETED + " TEXT ,"
                + FAVOURITE_MESSAGE + " TEXT ,"
                + ATTACHMENT_PROGRESS + " TEXT ,"
                + ATTACHMENT_PATH + " TEXT ,"
                + ATTACHMENT_STATUS + " TEXT ,"
                + CUSTOM_DATA + " TEXT ,"
                + RECEIVER_ID + " TEXT )";
        db.execSQL(messageQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void deleteNotes(int noteId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            String unShortlist = "Delete from " + NOTES_TABLE + " where " + NOTES_ID + " = '" + noteId + "'";
            db.execSQL(unShortlist);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("Exception = ", e + "");
        } finally {
            db.endTransaction();
        }
    }

    public void updateGoingStatusEvents(int postId, int goingStatus, int interested) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(IS_GOING, goingStatus);
            cv.put(INTRESTED, interested);
            db.update(POSTS_TABLE, cv, POST_ID + " = '" + postId + "'", null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("Exception = ", e + "");
        } finally {
            db.endTransaction();
        }
    }


    public void updateLikeStatus(int postId, int likeStatus, int likeCount) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(LIKED, likeStatus);
            cv.put(LIKE_COUNT, likeCount);
            db.update(POSTS_TABLE, cv, POST_ID + " = '" + postId + "'", null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("Exception = ", e + "");
        } finally {
            db.endTransaction();
        }
    }

    public void updateBookmarkStatus(int postId, int bookmarkStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(IS_BOOKMARKED, bookmarkStatus);
            db.update(POSTS_TABLE, cv, POST_ID + " = '" + postId + "'", null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("Exception = ", e + "");
        } finally {
            db.endTransaction();
        }
    }

    public void updateCommentCount(int postId, int commentCount) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(COMMENT_COUNT, commentCount);
            db.update(POSTS_TABLE, cv, POST_ID + " = '" + postId + "'", null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("Exception = ", e + "");
        } finally {
            db.endTransaction();
        }
    }

    public void removeGoingUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            String unShortlist = "Delete from " + GOING_TABLE + " where " + USER_ID + " = '" + userId + "'";
            db.execSQL(unShortlist);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("Exception = ", e + "");
        } finally {
            db.endTransaction();
        }
    }

    public void deleteEventData(int postType) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            String dropEvents = "Delete from  " + POSTS_TABLE + " where " + POST_TYPE + " = '" + postType + "'";
            db.execSQL(dropEvents);

            String dropImages = "Delete from  " + IMAGES_TABLE + " where " + POST_TYPE + " = '" + postType + "'";
            db.execSQL(dropImages);

            String dropGoingUsers = "Delete from  " + GOING_TABLE;
            db.execSQL(dropGoingUsers);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("Exception = ", e + "");
        } finally {
            db.endTransaction();
        }
    }

    public void deleteCommunityData(int postType) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            String dropEvents = "Delete from  " + POSTS_TABLE + " where " + POST_TYPE + " = '" + postType + "'";
            db.execSQL(dropEvents);

            String dropImages = "Delete from  " + IMAGES_TABLE + " where " + POST_TYPE + " = '" + postType + "'";
            db.execSQL(dropImages);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("Exception = ", e + "");
        } finally {
            db.endTransaction();
        }
    }


    public void deletePostById(int postId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            String dropEvents = "Delete from  " + POSTS_TABLE + " where " + POST_ID + " = '" + postId + "'";
            db.execSQL(dropEvents);

            String dropImages = "Delete from  " + IMAGES_TABLE + " where " + POST_ID + " = '" + postId + "'";
            db.execSQL(dropImages);

            String dropGoingUsers = "Delete from  " + GOING_TABLE + " where " + POST_ID + " = '" + postId + "'";
            db.execSQL(dropGoingUsers);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("Exception = ", e + "");
        } finally {
            db.endTransaction();
        }
    }

    public void deleteAllTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            String dropEvents = "Delete from  " + POSTS_TABLE;
            db.execSQL(dropEvents);

            String dropImages = "Delete from  " + IMAGES_TABLE;
            db.execSQL(dropImages);

            String dropGoingUsers = "Delete from  " + GOING_TABLE;
            db.execSQL(dropGoingUsers);

            String dropNotes = "Delete from  " + NOTES_TABLE;
            db.execSQL(dropNotes);

            String dropProfile = "Delete from  " + PROFILE_TABLE;
            db.execSQL(dropProfile);

            String dropDialogs = "Delete from  " + DIALOGS_TABLE;
            db.execSQL(dropDialogs);

            String dropChats = "Delete from  " + CHATS_TABLE;
            db.execSQL(dropChats);

            String dropUnread = "Delete from  " + UNREAD_COUNT_TABLE;
            db.execSQL(dropUnread);

            String dropBlock = "Delete from  " + BLOCK_TABLE;
            db.execSQL(dropBlock);

            String dropMessages = "Delete from  " + MESSAGES_TABLE;
            db.execSQL(dropMessages);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("Exception = ", e + "");
        } finally {
            db.endTransaction();
        }
    }

    ///////////////////////////////////////////////////////////////////////

    public void addProfile(ProfileModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        Cursor data = null;
        try {
            ContentValues values = new ContentValues();
            values.put(ONLINE_STATUS, "" + model.online_status);
            values.put(ACCESS_TOKEN, "" + model.access_token);
            values.put(USER_NAME, "" + model.user_name);
            values.put(USER_PIC, "" + model.user_pic);
            data = getReadableDatabase().rawQuery("Select * from " + PROFILE_TABLE + " where "
                    + USER_ID + " = '" + model.user_id + "'", null);
            if (data.getCount() > 0) {
                db.update(PROFILE_TABLE, values, USER_ID + " = '" + model.user_id + "'", null);
            } else {
                values.put(USER_ID, "" + model.user_id);
                db.insertOrThrow(PROFILE_TABLE, null, values);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("Exception", "is " + e);
        } finally {
            if (data != null && !data.isClosed()) {
                data.close();
            }
            db.endTransaction();
        }
    }

    public ProfileModel getProfile(String userId) {
        SQLiteDatabase db_read = this.getReadableDatabase();
        Cursor cur = null;
        ProfileModel mUser = null;
        try {
            String qry = "select * from " + PROFILE_TABLE + " where " + USER_ID + " = '" + userId + "'";
            cur = db_read.rawQuery(qry, null);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                mUser = new ProfileModel();
                mUser.user_id = cur.getString(1);
                mUser.online_status = Long.parseLong(cur.getString(2));
                mUser.access_token = cur.getString(3);
                mUser.user_name = cur.getString(4);
                mUser.user_pic = cur.getString(5);
                mUser.chat_dialog_ids = new HashMap<String, String>();
                cur.moveToNext();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cur != null && !cur.isClosed()) {
                cur.close();
            }
        }
        return mUser;
    }

    public synchronized void addDialogs(HashMap<String, String> list, String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        Cursor data = null;
        try {
            for (String userUniqueKey : list.keySet()) {
                ContentValues values = new ContentValues();

                values.put(USER_ID, userId);
                values.put(CHAT_DIALOG_ID, userUniqueKey);

                data = getReadableDatabase().rawQuery(
                        "Select * from " + DIALOGS_TABLE + " where "
                                + USER_ID + " = '" + userId + "' and " + CHAT_DIALOG_ID + " = '" + userUniqueKey + "'", null);
                if (data.getCount() > 0) {
                } else {
                    db.insertOrThrow(DIALOGS_TABLE, null, values);
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("Exception", "is " + e);
        } finally {
            if (data != null && !data.isClosed()) {
                data.close();
            }
            db.endTransaction();
        }
    }

    public HashMap<String, String> getDialogs(String userId) {
        SQLiteDatabase db_read = this.getReadableDatabase();
        Cursor cur = null;
        HashMap<String, String> list = new HashMap<String, String>();
        try {
            String qry = "select * from " + DIALOGS_TABLE + " where " + USER_ID + " = '" + userId + "'";
            cur = db_read.rawQuery(qry, null);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                list.put(cur.getString(2), cur.getString(2));
                cur.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cur != null && !cur.isClosed()) {
                cur.close();
            }
        }
        return list;
    }

    public void addNewChat(ChatsModel chat, String userId, String otherUserId) {
        SQLiteDatabase db_write = this.getWritableDatabase();
        db_write.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(CHAT_DIALOG_ID, chat.chat_dialog_id);
            values.put(LAST_MESSAGE, chat.last_message);
            values.put(LAST_MESSAGE_TIME, "" + chat.last_message_time.get(userId));
            values.put(LAST_MESSAGE_SENDER_ID, chat.last_message_sender_id);
            values.put(LAST_MESSAGE_ID, chat.last_message_id);
            values.put(LAST_MESSAGE_TYPE, chat.last_message_type.get(userId));
            values.put(LAST_MESSAGE_DATA, chat.last_message_data.get(userId));
            values.put(PARTICIPANT_IDS, chat.participant_ids);
            values.put(NAME, chat.name.get(otherUserId));
            values.put(PROFILE_PIC, chat.profile_pic.get(otherUserId));
            values.put(DELETE_DIALOG_TIME, "" + chat.delete_dialog_time.get(userId));
            values.put(USER_TYPE, chat.user_type.get(userId));
            values.put(OPPONENT_USER_TYPE, chat.user_type.get(otherUserId));
            values.put(OPPONENT_USER_ID, otherUserId);
            values.put(RATING, chat.rating.get(userId));


            values.put(OWN_MESSAGE_RATING_COUNT, chat.message_rating_count.get(userId));
            values.put(DELETE_OUTER_DIALOG, chat.delete_outer_dialog.get(userId));

            values.put(OTHER_MESSAGE_RATING_COUNT, chat.message_rating_count.get(otherUserId));

            db_write.insertOrThrow(CHATS_TABLE, null, values);

            addUnreadCount(chat.unread_count, chat.chat_dialog_id);

            addBlockStatus(chat.block_status, chat.chat_dialog_id);

            db_write.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db_write.endTransaction();
        }
    }

    public void addUpateChat(ChatsModel chat, String userId, String otherUserId) {
        SQLiteDatabase db_write = this.getWritableDatabase();
        SQLiteDatabase db_read = this.getReadableDatabase();
        db_write.beginTransaction();
        Cursor cur = null;
        try {
            ContentValues values = new ContentValues();
            values.put(LAST_MESSAGE, chat.last_message);
            values.put(LAST_MESSAGE_TIME, "" + chat.last_message_time.get(userId));
            values.put(LAST_MESSAGE_SENDER_ID, chat.last_message_sender_id);
            values.put(LAST_MESSAGE_ID, chat.last_message_id);
            values.put(LAST_MESSAGE_TYPE, chat.last_message_type.get(userId));
            values.put(LAST_MESSAGE_DATA, chat.last_message_data.get(userId));
            values.put(NAME, chat.name.get(otherUserId));
            values.put(PROFILE_PIC, chat.profile_pic.get(otherUserId));
            values.put(DELETE_DIALOG_TIME, "" + chat.delete_dialog_time.get(userId));
            values.put(RATING, chat.rating.get(userId));


            values.put(OWN_MESSAGE_RATING_COUNT, chat.message_rating_count.get(userId));
            values.put(DELETE_OUTER_DIALOG, chat.delete_outer_dialog.get(userId));

            values.put(OTHER_MESSAGE_RATING_COUNT, chat.message_rating_count.get(otherUserId));

            addUnreadCount(chat.unread_count, chat.chat_dialog_id);

            addBlockStatus(chat.block_status, chat.chat_dialog_id);

            String qry = "select * from " + CHATS_TABLE + " where " + CHAT_DIALOG_ID
                    + " = '" + chat.chat_dialog_id + "'";
            cur = db_read.rawQuery(qry, null);
            if (cur.getCount() > 0) {
                db_write.update(CHATS_TABLE, values, CHAT_DIALOG_ID + " ='" + chat.chat_dialog_id + "'", null);
            } else {
                values.put(CHAT_DIALOG_ID, chat.chat_dialog_id);
                values.put(PARTICIPANT_IDS, chat.participant_ids);
                values.put(USER_TYPE, chat.user_type.get(userId));
                values.put(OPPONENT_USER_TYPE, chat.user_type.get(otherUserId));
                values.put(OPPONENT_USER_ID, otherUserId);
                db_write.insertOrThrow(CHATS_TABLE, null, values);
            }

            db_write.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cur != null && !cur.isClosed()) {
                cur.close();
            }
            db_write.endTransaction();
        }
    }

    public void addUnreadCount(HashMap<String, Integer> unreadCount, String dialogId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        Cursor data = null;
        try {
            for (String userUniqueKey : unreadCount.keySet()) {
                data = getReadableDatabase().rawQuery(
                        "Select * from " + UNREAD_COUNT_TABLE + " where "
                                + CHAT_DIALOG_ID + " = '" + dialogId + "' and " + USER_ID + " = '" + userUniqueKey + "'", null);
                ContentValues values = new ContentValues();
                values.put(UNREAD_COUNT, unreadCount.get(userUniqueKey));
                if (data.getCount() > 0) {
                    db.update(UNREAD_COUNT_TABLE, values, CHAT_DIALOG_ID + " = '" +
                            dialogId + "' and " + USER_ID + " = '" + userUniqueKey + "'", null);
                } else {
                    values.put(CHAT_DIALOG_ID, dialogId);
                    values.put(USER_ID, userUniqueKey);
                    db.insertOrThrow(UNREAD_COUNT_TABLE, null, values);
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("Exception", "is " + e);
        } finally {
            if (data != null && !data.isClosed()) {
                data.close();
            }
            db.endTransaction();
        }
    }

    public void addBlockStatus(HashMap<String, String> blockStatus, String dialogId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        Cursor data = null;
        try {
            for (String userUniqueKey : blockStatus.keySet()) {
                data = getReadableDatabase().rawQuery(
                        "Select * from " + BLOCK_TABLE + " where "
                                + CHAT_DIALOG_ID + " = '" + dialogId + "' and " + USER_ID + " = '" + userUniqueKey + "'", null);
                ContentValues values = new ContentValues();
                values.put(BLOCK_STATUS, blockStatus.get(userUniqueKey));
                if (data.getCount() > 0) {
                    db.update(BLOCK_TABLE, values, CHAT_DIALOG_ID + " = '" +
                            dialogId + "' and " + USER_ID + " = '" + userUniqueKey + "'", null);
                } else {
                    values.put(CHAT_DIALOG_ID, dialogId);
                    values.put(USER_ID, userUniqueKey);
                    db.insertOrThrow(BLOCK_TABLE, null, values);
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("Exception", "is " + e);
        } finally {
            if (data != null && !data.isClosed()) {
                data.close();
            }
            db.endTransaction();
        }
    }

    public LinkedHashMap<String, ChatsModel> getAllChats(String userId) {
        SQLiteDatabase db_read = this.getReadableDatabase();
        Cursor cur = null;
        LinkedHashMap<String, ChatsModel> mChatList = new LinkedHashMap<>();
        try {
            String qry = "Select * from " + CHATS_TABLE + " order by " + LAST_MESSAGE_TIME + " desc";
            cur = db_read.rawQuery(qry, null);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
//                if (!TextUtils.isEmpty(cur.getString(6))) {
                String chat_dialog_id = "";
                ChatsModel mChats = new ChatsModel();
                chat_dialog_id = cur.getString(1);
                mChats.chat_dialog_id = chat_dialog_id;
                mChats.last_message = cur.getString(2);
                mChats.last_message_sender_id = cur.getString(4);
                mChats.last_message_id = cur.getString(5);
                mChats.participant_ids = cur.getString(7);
                mChats.opponent_user_id = cur.getString(13);
                String otherUserId = cur.getString(13);
                String[] particID = mChats.participant_ids.split(",");
                for (String id : particID) {
                    String[] userIds = id.split("_");
                    if (!(userIds[0].trim()).equalsIgnoreCase(userId)) {
                        otherUserId = userIds[0].trim();
                        break;
                    }
                }

                mChats.last_message_type = new HashMap<>();
                mChats.last_message_type.put(userId, cur.getString(6));
                mChats.last_message_type.put(otherUserId, cur.getString(6));

                mChats.last_message_data = new HashMap<>();
                mChats.last_message_data.put(userId, cur.getString(18));
                mChats.last_message_data.put(otherUserId, cur.getString(18));

                mChats.last_message_time = new HashMap<>();
                mChats.last_message_time.put(otherUserId, Long.parseLong(cur.getString(3)));
                mChats.last_message_time.put(userId, Long.parseLong(cur.getString(3)));

                mChats.name = new HashMap<>();
                mChats.name.put(otherUserId, cur.getString(8));
                mChats.name.put(userId, "");

                mChats.profile_pic = new HashMap<>();
                mChats.profile_pic.put(otherUserId, cur.getString(9));
                mChats.profile_pic.put(userId, "");

                mChats.delete_dialog_time = new HashMap<>();
                mChats.delete_dialog_time.put(otherUserId, Long.parseLong(cur.getString(10)));
                mChats.delete_dialog_time.put(userId, Long.parseLong(cur.getString(10)));

                mChats.user_type = new HashMap<>();
                mChats.user_type.put(userId, cur.getString(11));
                mChats.user_type.put(otherUserId, cur.getString(12));

                mChats.rating = new HashMap<>();
                mChats.rating.put(userId, cur.getString(14));
                mChats.rating.put(otherUserId, "0");

                mChats.message_rating_count = new HashMap<>();
                mChats.message_rating_count.put(userId, Integer.parseInt(cur.getString(15)));
                mChats.message_rating_count.put(otherUserId, Integer.parseInt(cur.getString(16)));

                mChats.delete_outer_dialog = new HashMap<>();
                mChats.delete_outer_dialog.put(userId, cur.getString(17));
                mChats.delete_outer_dialog.put(otherUserId, "0");

                mChats.unread_count = getUnreadCount(cur.getString(1));
                mChats.block_status = getBlockStatus(cur.getString(1));

//                int status = 0;
//                if (type.equals(FirebaseChatConstants.TYPE_ATHLETE)) {
//                    if (mChats.user_type.get(otherUserId).equals(FirebaseChatConstants.TYPE_ATHLETE)) {
//                        status = 1;
//                    }
//                } else if (type.equals(FirebaseChatConstants.TYPE_COACH)) {
//                    if (mChats.user_type.get(otherUserId).equals(FirebaseChatConstants.TYPE_COACH)) {
//                        status = 1;
//                    }
//                } else {
//                    status = 1;
//                }
//                if (status == 1) {
                if (cur.getString(17).equals("0")) {
                    mChatList.put(cur.getString(1), mChats);
                    if (!chat_dialog_id.contains(InterConst.APP_ADMIN_ID)) {
                        mChatList.put(cur.getString(1), mChats);
                    }
                }
//                    long messageTime = Long.parseLong(cur.getString(3));
//                    if (mChats.delete_dialog_time.containsKey(userId)) {
//                        long deletetime = mChats.delete_dialog_time.get(userId);
//                        if (messageTime >= deletetime) {
//                            mChatList.put(cur.getString(1), mChats);
//                        }
//                        if (mChats.last_message.equals(FirebaseChatConstants.DEFAULT_MESSAGE_REGEX)) {
//                            mChatList.put(cur.getString(1), mChats);
//                        }
//                    }
//                }
                cur.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cur != null && !cur.isClosed()) {
                cur.close();
            }
        }
        return mChatList;
    }

    public ChatsModel getChatDialog(String participants, String userId, String otherUserId) {
        SQLiteDatabase db_read = this.getReadableDatabase();
        Cursor cur = null;
        ChatsModel mChats = null;
        try {
            String qry = "Select * from " + CHATS_TABLE + " where " + PARTICIPANT_IDS + " = '" + participants + "'";
            cur = db_read.rawQuery(qry, null);
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    mChats = new ChatsModel();
                    mChats.chat_dialog_id = cur.getString(1);
                    mChats.last_message = cur.getString(2);

                    mChats.last_message_time = new HashMap<>();
                    mChats.last_message_time.put(otherUserId, Long.parseLong(cur.getString(3)));
                    mChats.last_message_time.put(userId, Long.parseLong(cur.getString(3)));

                    mChats.last_message_sender_id = cur.getString(4);
                    mChats.last_message_id = cur.getString(5);
                    mChats.participant_ids = cur.getString(7);

                    mChats.name = new HashMap<>();
                    mChats.name.put(otherUserId, cur.getString(8));
                    mChats.name.put(userId, "");

                    mChats.last_message_type = new HashMap<>();
                    mChats.last_message_type.put(userId, cur.getString(6));
                    mChats.last_message_type.put(otherUserId, cur.getString(6));

                    mChats.last_message_data = new HashMap<>();
                    mChats.last_message_data.put(userId, cur.getString(18));
                    mChats.last_message_data.put(otherUserId, cur.getString(18));

                    mChats.profile_pic = new HashMap<>();
                    mChats.profile_pic.put(otherUserId, cur.getString(9));
                    mChats.profile_pic.put(userId, "");

                    mChats.delete_dialog_time = new HashMap<>();
                    mChats.delete_dialog_time.put(otherUserId, Long.parseLong(cur.getString(10)));
                    mChats.delete_dialog_time.put(userId, Long.parseLong(cur.getString(10)));

                    mChats.user_type = new HashMap<>();
                    mChats.user_type.put(userId, cur.getString(11));
                    mChats.user_type.put(otherUserId, cur.getString(12));

                    mChats.rating = new HashMap<>();
                    mChats.rating.put(userId, cur.getString(14));
                    mChats.rating.put(otherUserId, "0");

                    mChats.opponent_user_id = cur.getString(13);

                    mChats.unread_count = getUnreadCount(cur.getString(1));
                    mChats.block_status = getBlockStatus(cur.getString(1));

                    mChats.message_rating_count = new HashMap<>();
                    mChats.message_rating_count.put(userId, Integer.parseInt(cur.getString(15)));
                    mChats.message_rating_count.put(otherUserId, Integer.parseInt(cur.getString(16)));

                    mChats.delete_outer_dialog = new HashMap<>();
                    mChats.delete_outer_dialog.put(userId, cur.getString(17));
                    mChats.delete_outer_dialog.put(otherUserId, "0");

                    cur.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cur != null && !cur.isClosed()) {
                cur.close();
            }
        }
        return mChats;
    }

    public HashMap<String, Integer> getUnreadCount(String dialogId) {
        SQLiteDatabase db_read = this.getReadableDatabase();
        Cursor cur = null;
        HashMap<String, Integer> list = new HashMap<String, Integer>();
        try {
            String qry = "select * from " + UNREAD_COUNT_TABLE + " where " + CHAT_DIALOG_ID + " = '" + dialogId + "'";
            cur = db_read.rawQuery(qry, null);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                list.put(cur.getString(2), cur.getInt(3));
                cur.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cur != null && !cur.isClosed()) {
                cur.close();
            }
        }
        return list;
    }

    public HashMap<String, String> getBlockStatus(String dialogId) {
        SQLiteDatabase db_read = this.getReadableDatabase();
        Cursor cur = null;
        HashMap<String, String> list = new HashMap<String, String>();
        try {
            String qry = "select * from " + BLOCK_TABLE + " where " + CHAT_DIALOG_ID + " = '" + dialogId + "'";
            cur = db_read.rawQuery(qry, null);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                list.put(cur.getString(2), cur.getString(3));
                cur.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cur != null && !cur.isClosed()) {
                cur.close();
            }
        }
        return list;
    }

    public String getUserBlockStatus(String dialogId) {
        SQLiteDatabase db_read = this.getReadableDatabase();
        Cursor cur = null;
        String list = "";
        try {
            String qry = "select * from " + BLOCK_TABLE + " where " + CHAT_DIALOG_ID + " = '" + dialogId + "'";
            cur = db_read.rawQuery(qry, null);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                if (!utils.getString("user_id", "").equals(cur.getString(2))) {
                    list = cur.getString(3);
                    break;
                }
                cur.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cur != null && !cur.isClosed()) {
                cur.close();
            }
        }
        return list;
    }

    public LinkedHashMap<String, MessagesModel> getAllMessages(String chatDialogId, String userId, int limit, long dialogDeleteTime, String otherUserId) {
        SQLiteDatabase db_read = this.getReadableDatabase();
        Cursor cur = null;
        SimpleDateFormat today_date_format = new SimpleDateFormat("hh:mm aa", Locale.US);
        LinkedHashMap<String, MessagesModel> mMessageList = new LinkedHashMap<>();
        List<MessagesModel> localMessage = new ArrayList<>();
        try {
            String qry = "select * from " + MESSAGES_TABLE + " where " + CHAT_DIALOG_ID
                    + " = '" + chatDialogId + "' order by " + ID + " desc limit " + limit;
            cur = db_read.rawQuery(qry, null);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                if (!("" + cur.getString(10)).equals("1")) {
                    Calendar calDb = Calendar.getInstance();
                    long timeinMillis;
                    try {
                        timeinMillis = Long.parseLong(cur.getString(4));
                    } catch (Exception e) {
                        timeinMillis = calDb.getTimeInMillis();
                    }
                    calDb.setTimeInMillis(timeinMillis);

                    MessagesModel mMessage = new MessagesModel();
                    mMessage.message_id = cur.getString(1);
                    mMessage.message = cur.getString(2);
                    mMessage.message_type = cur.getString(3);
                    mMessage.message_time = cur.getString(4);
                    mMessage.firebase_message_time = Long.parseLong(cur.getString(5));
                    mMessage.chat_dialog_id = cur.getString(6);
                    mMessage.sender_id = cur.getString(7);
                    mMessage.message_status = cur.getInt(8);
                    mMessage.attachment_url = cur.getString(9);

                    mMessage.message_deleted = new HashMap<>();
                    mMessage.message_deleted.put(userId, cur.getString(10));
                    mMessage.message_deleted.put(otherUserId, "");

                    mMessage.favourite_message = new HashMap<>();
                    mMessage.favourite_message.put(userId, cur.getString(11));
                    mMessage.favourite_message.put(otherUserId, "");

                    mMessage.is_header = false;
                    mMessage.attachment_progress = cur.getString(12);
                    mMessage.attachment_path = cur.getString(13);
                    mMessage.attachment_status = cur.getString(14);
                    mMessage.show_message_datetime = today_date_format.format(calDb.getTime());
                    mMessage.custom_data = cur.getString(15);
                    mMessage.receiver_id = cur.getString(16);

                    if (!mMessage.message_deleted.get(userId).equals("1")) {
                        long messageTime = mMessage.firebase_message_time;
                        if (messageTime >= dialogDeleteTime) {
                            localMessage.add(mMessage);
                        }
                    }
                }
                cur.moveToNext();
            }
            Collections.reverse(localMessage);
            mMessageList = createHeader(localMessage);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cur != null && !cur.isClosed()) {
                cur.close();
            }
        }
        return mMessageList;
    }

    public List<MessagesModel> getAllMessagesArray(String chatDialogId, String userId, String type,
                                                   long dialogDeleteTime, String otherUserId) {
        SQLiteDatabase db_read = this.getReadableDatabase();
        Cursor cur = null;
        SimpleDateFormat today_date_format = new SimpleDateFormat("hh:mm aa", Locale.US);
        List<MessagesModel> localMessage = new ArrayList<>();
        try {

            String qry = "select * from " + MESSAGES_TABLE + " where " + CHAT_DIALOG_ID
                    + " = '" + chatDialogId + "' and " + MESSAGE_TYPE + " = '" + type + "'";
            cur = db_read.rawQuery(qry, null);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                if (!("" + cur.getString(10)).equals("1")) {
                    Calendar calDb = Calendar.getInstance();
                    long timeinMillis;
                    try {
                        timeinMillis = Long.parseLong(cur.getString(4));
                    } catch (Exception e) {
                        timeinMillis = calDb.getTimeInMillis();
                    }
                    calDb.setTimeInMillis(timeinMillis);

                    MessagesModel mMessage = new MessagesModel();
                    mMessage.message_id = cur.getString(1);
                    mMessage.message = cur.getString(2);
                    mMessage.message_type = cur.getString(3);
                    mMessage.message_time = cur.getString(4);
                    mMessage.firebase_message_time = Long.parseLong(cur.getString(5));
                    mMessage.chat_dialog_id = cur.getString(6);
                    mMessage.sender_id = cur.getString(7);
                    mMessage.message_status = cur.getInt(8);
                    mMessage.attachment_url = cur.getString(9);

                    mMessage.message_deleted = new HashMap<>();
                    mMessage.message_deleted.put(userId, cur.getString(10));
                    mMessage.message_deleted.put(otherUserId, "");

                    mMessage.favourite_message = new HashMap<>();
                    mMessage.favourite_message.put(userId, cur.getString(11));
                    mMessage.favourite_message.put(otherUserId, "");

                    mMessage.is_header = false;
                    mMessage.attachment_progress = cur.getString(12);
                    mMessage.attachment_path = cur.getString(13);
                    mMessage.attachment_status = cur.getString(14);
                    mMessage.show_message_datetime = today_date_format.format(calDb.getTime());
                    mMessage.custom_data = cur.getString(15);
                    mMessage.receiver_id = cur.getString(16);

                    if (!mMessage.message_deleted.get(userId).equals("1")) {
                        long messageTime = mMessage.firebase_message_time;
                        if (messageTime >= dialogDeleteTime) {
                            localMessage.add(mMessage);
                        }
                    }
                }
                cur.moveToNext();
            }
            Collections.reverse(localMessage);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cur != null && !cur.isClosed()) {
                cur.close();
            }
        }
        return localMessage;
    }


    public LinkedHashMap<String, MessagesModel> getAllFavouriteMessages(String chatDialogId, String userId, long dialogDeleteTime, String otherUserId, String status) {
        SQLiteDatabase db_read = this.getReadableDatabase();
        Cursor cur = null;
        SimpleDateFormat today_date_format = new SimpleDateFormat("hh:mm aa", Locale.US);
        LinkedHashMap<String, MessagesModel> mMessageList = new LinkedHashMap<>();
        List<MessagesModel> localMessage = new ArrayList<>();
        try {
            String qry = "select * from " + MESSAGES_TABLE + " where "
                    + CHAT_DIALOG_ID + " = '" + chatDialogId + "' and "
                    + FAVOURITE_MESSAGE + " = '" + status
                    + "' order by " + ID;
            cur = db_read.rawQuery(qry, null);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                if (!("" + cur.getString(10)).equals("1")) {
                    Calendar calDb = Calendar.getInstance();
                    long timeinMillis;
                    try {
                        timeinMillis = Long.parseLong(cur.getString(4));
                    } catch (Exception e) {
                        timeinMillis = calDb.getTimeInMillis();
                    }
                    calDb.setTimeInMillis(timeinMillis);

                    MessagesModel mMessage = new MessagesModel();
                    mMessage.message_id = cur.getString(1);
                    mMessage.message = cur.getString(2);
                    mMessage.message_type = cur.getString(3);
                    mMessage.message_time = cur.getString(4);
                    mMessage.firebase_message_time = Long.parseLong(cur.getString(5));
                    mMessage.chat_dialog_id = cur.getString(6);
                    mMessage.sender_id = cur.getString(7);
                    mMessage.message_status = cur.getInt(8);
                    mMessage.attachment_url = cur.getString(9);

                    mMessage.message_deleted = new HashMap<>();
                    mMessage.message_deleted.put(userId, cur.getString(10));
                    mMessage.message_deleted.put(otherUserId, "");

                    mMessage.favourite_message = new HashMap<>();
                    mMessage.favourite_message.put(userId, cur.getString(11));
                    mMessage.favourite_message.put(otherUserId, "");

                    mMessage.is_header = false;
                    mMessage.attachment_progress = cur.getString(12);
                    mMessage.attachment_path = cur.getString(13);
                    mMessage.attachment_status = cur.getString(14);
                    mMessage.show_message_datetime = today_date_format.format(calDb.getTime());
                    mMessage.custom_data = cur.getString(15);
                    mMessage.receiver_id = cur.getString(16);

                    if (!mMessage.message_deleted.get(userId).equals("1")) {
                        long messageTime = mMessage.firebase_message_time;
                        if (messageTime >= dialogDeleteTime) {
                            localMessage.add(mMessage);
                        }
                    }
                }
                cur.moveToNext();
            }
            mMessageList = createHeader(localMessage);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cur != null && !cur.isClosed()) {
                cur.close();
            }
        }
        return mMessageList;
    }

    LinkedHashMap<String, MessagesModel> createHeader(List<MessagesModel> messages) {
        LinkedHashMap<String, MessagesModel> mMessageList = new LinkedHashMap<>();
        SimpleDateFormat chat_date_format = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        SimpleDateFormat today_date_format = new SimpleDateFormat("hh:mm aa", Locale.US);
        SimpleDateFormat show_dateheader_format = new SimpleDateFormat("dd MMM yyyy", Locale.US);

        Calendar cal = Calendar.getInstance();
        String today = chat_date_format.format(cal.getTime());

        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.DATE, -1);
        String yesterday = chat_date_format.format(cal1.getTime());

        String lastHeader = "";

        for (MessagesModel message : messages) {
            Calendar calDb = Calendar.getInstance();
            long timeinMillis;
            try {
                timeinMillis = Long.parseLong(message.message_time);
            } catch (Exception e) {
                timeinMillis = calDb.getTimeInMillis();
            }
            calDb.setTimeInMillis(timeinMillis);
            String dbDate = chat_date_format.format(calDb.getTime());

            if (!TextUtils.equals(lastHeader, dbDate)) {
                lastHeader = dbDate;

                MessagesModel mMessage = new MessagesModel();
                mMessage.is_header = true;
                if (dbDate.equals(today)) {
                    mMessage.show_header_text = "Today";
                    mMessage.show_message_datetime = today_date_format.format(calDb.getTime());
                } else if (dbDate.equals(yesterday)) {
                    mMessage.show_header_text = "Yesterday";
                    mMessage.show_message_datetime = today_date_format.format(calDb.getTime());
                } else {
                    mMessage.show_header_text = show_dateheader_format.format(calDb.getTime());
                    mMessage.show_message_datetime = today_date_format.format(calDb.getTime());
                }
                mMessageList.put(lastHeader, mMessage);
            }
            MessagesModel mMessage = message;
            mMessage.is_header = false;
            mMessage.show_message_datetime = today_date_format.format(calDb.getTime());
            mMessageList.put(message.message_id, mMessage);
        }
        return mMessageList;
    }

    public void addMessage(MessagesModel msg, String userId) {
        SQLiteDatabase db_write = this.getWritableDatabase();
        SQLiteDatabase db_read = this.getReadableDatabase();
        db_write.beginTransaction();
        Cursor cur = null;
        try {
            ContentValues values = new ContentValues();
            values.put(MESSAGE, msg.message);
            values.put(MESSAGE_TYPE, msg.message_type);
            if (msg.sender_id.equals(userId)) {
                values.put(FIRBASE_MESSAGE_TIME, msg.firebase_message_time);
            }
            values.put(CHAT_DIALOG_ID, msg.chat_dialog_id);
            values.put(SENDER_ID, msg.sender_id);
            values.put(MESSAGE_STATUS, msg.message_status);
            values.put(ATTACHMENT_URL, msg.attachment_url);
            values.put(RECEIVER_ID, msg.receiver_id);

            String qry = "select * from " + MESSAGES_TABLE + " where " + MESSAGE_ID + " = '" + msg.message_id + "'";
            cur = db_read.rawQuery(qry, null);
            if (cur.getCount() > 0) {
                db_write.update(MESSAGES_TABLE, values, MESSAGE_ID + " = '" + msg.message_id + "'", null);
            } else {
                values.put(MESSAGE_DELETED, msg.message_deleted.get(userId));
                values.put(FAVOURITE_MESSAGE, msg.favourite_message.get(userId));
                values.put(MESSAGE_ID, msg.message_id);
                if (msg.sender_id.equals(userId)) {
                    values.put(MESSAGE_TIME, "" + msg.message_time);
                } else {
                    values.put(MESSAGE_TIME, "" + (Calendar.getInstance()).getTimeInMillis()); // comment for show local time
                }
                values.put(FIRBASE_MESSAGE_TIME, msg.firebase_message_time);
                if (TextUtils.isEmpty(msg.attachment_path)) {
                    values.put(ATTACHMENT_PATH, "");
                } else {
                    values.put(ATTACHMENT_PATH, msg.attachment_path);
                }
                if (TextUtils.isEmpty(msg.attachment_status)) {
                    values.put(ATTACHMENT_STATUS, FirebaseChatConstants.FILE_EREROR);
                } else {
                    values.put(ATTACHMENT_STATUS, msg.attachment_status);
                }
                if (TextUtils.isEmpty(msg.attachment_progress)) {
                    values.put(ATTACHMENT_PROGRESS, "0");
                } else {
                    values.put(ATTACHMENT_PROGRESS, msg.attachment_progress);
                }
                values.put(CUSTOM_DATA, msg.custom_data);
                db_write.insertOrThrow(MESSAGES_TABLE, null, values);
            }
            db_write.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cur != null && !cur.isClosed()) {
                cur.close();
            }
            db_write.endTransaction();
        }
    }

    public void updateMessage(MessagesModel msg, String userId) {
        SQLiteDatabase db_write = this.getWritableDatabase();
        SQLiteDatabase db_read = this.getReadableDatabase();
        db_write.beginTransaction();
        Cursor cur = null;
        try {
            ContentValues values = new ContentValues();
            values.put(MESSAGE, msg.message);
            values.put(MESSAGE_TYPE, msg.message_type);
            if (msg.sender_id.equals(userId)) {
                values.put(FIRBASE_MESSAGE_TIME, msg.firebase_message_time);
            }
            values.put(CHAT_DIALOG_ID, msg.chat_dialog_id);
            values.put(SENDER_ID, msg.sender_id);
            values.put(MESSAGE_STATUS, msg.message_status);
            values.put(ATTACHMENT_URL, msg.attachment_url);
            values.put(RECEIVER_ID, msg.receiver_id);
            values.put(MESSAGE_DELETED, msg.message_deleted.get(userId));
            values.put(FAVOURITE_MESSAGE, msg.favourite_message.get(userId));
            values.put(FIRBASE_MESSAGE_TIME, msg.firebase_message_time);

            String qry = "select * from " + MESSAGES_TABLE + " where " + MESSAGE_ID + " = '" + msg.message_id + "'";
            cur = db_read.rawQuery(qry, null);
            if (cur.getCount() > 0) {
                db_write.update(MESSAGES_TABLE, values, MESSAGE_ID + " = '" + msg.message_id + "'", null);
            }
            db_write.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cur != null && !cur.isClosed()) {
                cur.close();
            }
            db_write.endTransaction();
        }
    }

    public void addMessagesHistory(List<MessageHistoryModel.ResponseBean> messages, String userId) {
        SQLiteDatabase db_write = this.getWritableDatabase();
        SQLiteDatabase db_read = this.getReadableDatabase();
        db_write.beginTransaction();
        Cursor cur = null;
        try {
            for (int i = 0; i < messages.size(); i++) {
                MessageHistoryModel.ResponseBean msg = messages.get(i);
                ContentValues values = new ContentValues();
                values.put(MESSAGE, msg.getMessage());
                values.put(MESSAGE_TYPE, msg.getMessage_type());
                values.put(MESSAGE_TIME, msg.getMessage_time()); // comment for show local time
                values.put(FIRBASE_MESSAGE_TIME, msg.getFirebase_message_time());
                values.put(CHAT_DIALOG_ID, msg.getChat_dialog_id());
                values.put(SENDER_ID, msg.getSender_id());
                values.put(MESSAGE_STATUS, msg.getMessage_status());
                values.put(ATTACHMENT_URL, msg.getAttachment_url());
                values.put(RECEIVER_ID, msg.getReceiver_id());

                String qry = "select * from " + MESSAGES_TABLE + " where " + MESSAGE_ID + " = '" + msg.getMessage_id() + "'";
                cur = db_read.rawQuery(qry, null);
                if (cur.getCount() > 0) {
                    db_write.update(MESSAGES_TABLE, values, MESSAGE_ID + " = '" + msg.getMessage_id() + "'", null);
                } else {
                    for (int j = 0; j < msg.getMessage_deleted().size(); j++) {
                        if (msg.getMessage_deleted().get(j).getKey().equals(userId)) {
                            values.put(MESSAGE_DELETED, msg.getMessage_deleted().get(j).getValue());
                            break;
                        }
                    }
//                    for (int j = 0; j < msg.getFavourite_message().size(); j++) {
//                        if (msg.getFavourite_message().get(j).getKey().equals(userId)) {
//                            values.put(FAVOURITE_MESSAGE, msg.getFavourite_message().get(j).getValue());
//                            break;
//                        }
//                    }
                    values.put(MESSAGE_ID, msg.getMessage_id());
                    values.put(ATTACHMENT_PATH, "");
                    values.put(ATTACHMENT_STATUS, FirebaseChatConstants.FILE_EREROR);
                    values.put(ATTACHMENT_PROGRESS, "0");
                    values.put(CUSTOM_DATA, "");
                    db_write.insertOrThrow(MESSAGES_TABLE, null, values);
                }
            }
            db_write.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cur != null && !cur.isClosed()) {
                cur.close();
            }
            db_write.endTransaction();
        }
    }

    public MessagesModel getSingleMessage(String msgId, String userId) {
        SQLiteDatabase db_read = this.getReadableDatabase();
        Cursor cur = null;
        MessagesModel mMessage = null;
        try {
            String qry = "select * from " + MESSAGES_TABLE + " where " + MESSAGE_ID + " = '" + msgId + "'";
            cur = db_read.rawQuery(qry, null);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                if (!("" + cur.getString(10)).equals("1")) {
                    Calendar calDb = Calendar.getInstance();
                    long timeinMillis;
                    try {
                        timeinMillis = Long.parseLong(cur.getString(4));
                    } catch (Exception e) {
                        timeinMillis = calDb.getTimeInMillis();
                    }
                    calDb.setTimeInMillis(timeinMillis);

                    mMessage = new MessagesModel();
                    mMessage.message_id = cur.getString(1);
                    mMessage.message = cur.getString(2);
                    mMessage.message_type = cur.getString(3);
                    mMessage.message_time = cur.getString(4);
                    mMessage.firebase_message_time = Long.parseLong(cur.getString(5));
                    mMessage.chat_dialog_id = cur.getString(6);
                    mMessage.sender_id = cur.getString(7);
                    mMessage.message_status = cur.getInt(8);
                    mMessage.attachment_url = cur.getString(9);

                    String otherUserId = "";
                    String[] particID = cur.getString(6).split(",");
                    for (String id : particID) {
                        String[] userIds = id.split("_");
                        if (!(userIds[0].trim()).equalsIgnoreCase(userId)) {
                            otherUserId = userIds[0].trim();
                            break;
                        }
                    }

                    mMessage.message_deleted = new HashMap<>();
                    mMessage.message_deleted.put(userId, cur.getString(10));
                    mMessage.message_deleted.put(otherUserId, "");

                    mMessage.favourite_message = new HashMap<>();
                    mMessage.favourite_message.put(userId, cur.getString(11));
                    mMessage.favourite_message.put(otherUserId, "");

                    mMessage.is_header = false;
                    mMessage.attachment_progress = cur.getString(12);
                    mMessage.attachment_path = cur.getString(13);
                    mMessage.attachment_status = cur.getString(14);
                    mMessage.show_message_datetime = today_date_format.format(calDb.getTime());
                    mMessage.custom_data = cur.getString(15);
                    mMessage.receiver_id = cur.getString(16);
                }
                cur.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cur != null && !cur.isClosed()) {
                cur.close();
            }
        }
        return mMessage;
    }

    public MessagesModel getPendingUploads(String attachment_path, String userId) {
        SQLiteDatabase db_read = this.getReadableDatabase();
        Cursor cur = null;
        MessagesModel mMessage = null;
        try {
            String qry = "select * from " + MESSAGES_TABLE + " where "
                    + ATTACHMENT_PATH + " = '" + attachment_path + "' and "
                    + ATTACHMENT_STATUS + " = '" + FirebaseChatConstants.FILE_EREROR + "' and "
                    + SENDER_ID + " = '" + userId + "' limit 1";
            cur = db_read.rawQuery(qry, null);

            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                if (!("" + cur.getString(10)).equals("1")) {
                    Calendar calDb = Calendar.getInstance();
                    long timeinMillis;
                    try {
                        timeinMillis = Long.parseLong(cur.getString(4));
                    } catch (Exception e) {
                        timeinMillis = calDb.getTimeInMillis();
                    }
                    calDb.setTimeInMillis(timeinMillis);

                    mMessage = new MessagesModel();
                    mMessage.message_id = cur.getString(1);
                    mMessage.message = cur.getString(2);
                    mMessage.message_type = cur.getString(3);
                    mMessage.message_time = cur.getString(4);
                    mMessage.firebase_message_time = Long.parseLong(cur.getString(5));
                    mMessage.chat_dialog_id = cur.getString(6);
                    mMessage.sender_id = cur.getString(7);
                    mMessage.message_status = cur.getInt(8);
                    mMessage.attachment_url = cur.getString(9);

                    String otherUserId = "";
                    String[] particID = cur.getString(6).split(",");
                    for (String id : particID) {
                        String[] userIds = id.split("_");
                        if (!(userIds[0].trim()).equalsIgnoreCase(userId)) {
                            otherUserId = userIds[0].trim();
                            break;
                        }
                    }

                    mMessage.message_deleted = new HashMap<>();
                    mMessage.message_deleted.put(userId, cur.getString(10));
                    mMessage.message_deleted.put(otherUserId, "");

                    mMessage.favourite_message = new HashMap<>();
                    mMessage.favourite_message.put(userId, cur.getString(11));
                    mMessage.favourite_message.put(otherUserId, "");

                    mMessage.is_header = false;
                    mMessage.attachment_progress = cur.getString(12);
                    mMessage.attachment_path = cur.getString(13);
                    mMessage.attachment_status = cur.getString(14);
                    mMessage.show_message_datetime = today_date_format.format(calDb.getTime());
                    mMessage.custom_data = cur.getString(15);
                    mMessage.receiver_id = cur.getString(16);
                }
                cur.moveToNext();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cur != null && !cur.isClosed()) {
                cur.close();
            }
        }
        return mMessage;
    }

    public Long getMessageTime(String chatDialogId) {
        SQLiteDatabase db_read = this.getReadableDatabase();
        Cursor cur = null;
        String status = "0";
        Long messageTime = 0L;
        try {
            String qry = "select * from " + MESSAGES_TABLE + " where "
                    + CHAT_DIALOG_ID + " = '" + chatDialogId + "' and "
                    + MESSAGE_DELETED + " = '" + status
                    + "' order by " + FIRBASE_MESSAGE_TIME + " desc limit 1";
            cur = db_read.rawQuery(qry, null);

            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                if (!("" + cur.getString(10)).equals("1")) {
                    messageTime = Long.parseLong(cur.getString(4));
                }
                cur.moveToNext();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cur != null && !cur.isClosed()) {
                cur.close();
            }
        }
        return messageTime;
    }

    public MessagesModel getPendingUploads(String userId) {
        SQLiteDatabase db_read = this.getReadableDatabase();
        Cursor cur = null;
        MessagesModel mMessage = null;
        try {
            String qry = "select * from " + MESSAGES_TABLE + " where "
                    + ATTACHMENT_STATUS + " = '" + FirebaseChatConstants.FILE_UPLOADING + "' and "
                    + SENDER_ID + " = '" + userId
                    + "' order by " + MESSAGE_TIME + " desc limit 1";
            cur = db_read.rawQuery(qry, null);

            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                if (!("" + cur.getString(10)).equals("1")) {
                    Calendar calDb = Calendar.getInstance();
                    long timeinMillis;
                    try {
                        timeinMillis = Long.parseLong(cur.getString(4));
                    } catch (Exception e) {
                        timeinMillis = calDb.getTimeInMillis();
                    }
                    calDb.setTimeInMillis(timeinMillis);

                    mMessage = new MessagesModel();
                    mMessage.message_id = cur.getString(1);
                    mMessage.message = cur.getString(2);
                    mMessage.message_type = cur.getString(3);
                    mMessage.message_time = cur.getString(4);
                    mMessage.firebase_message_time = Long.parseLong(cur.getString(5));
                    mMessage.chat_dialog_id = cur.getString(6);
                    mMessage.sender_id = cur.getString(7);
                    mMessage.message_status = cur.getInt(8);
                    mMessage.attachment_url = cur.getString(9);

                    String otherUserId = "";
                    String[] particID = cur.getString(6).split(",");
                    for (String id : particID) {
                        String[] userIds = id.split("_");
                        if (!(userIds[0].trim()).equalsIgnoreCase(userId)) {
                            otherUserId = userIds[0].trim();
                            break;
                        }
                    }

                    mMessage.message_deleted = new HashMap<>();
                    mMessage.message_deleted.put(userId, cur.getString(10));
                    mMessage.message_deleted.put(otherUserId, "");

                    mMessage.favourite_message = new HashMap<>();
                    mMessage.favourite_message.put(userId, cur.getString(11));
                    mMessage.favourite_message.put(otherUserId, "");

                    mMessage.is_header = false;
                    mMessage.attachment_progress = cur.getString(12);
                    mMessage.attachment_path = cur.getString(13);
                    mMessage.attachment_status = cur.getString(14);
                    mMessage.show_message_datetime = today_date_format.format(calDb.getTime());
                    mMessage.custom_data = cur.getString(15);
                    mMessage.receiver_id = cur.getString(16);
                }
                cur.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cur != null && !cur.isClosed()) {
                cur.close();
            }
        }
        return mMessage;
    }

    public int deleteSingleMessage(String messageId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        int rowCount = 0;
        Cursor data = null;
        try {
            rowCount = db.delete(MESSAGES_TABLE, MESSAGE_ID + " = '" + messageId + "'", null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("Exception", "is " + e);
        } finally {
            if (data != null && !data.isClosed()) {
                data.close();
            }
            db.endTransaction();
        }
        return rowCount;
    }

    public void changeUploadStatus(String messageId, String attachmentStatus, int messageStatus) {
        SQLiteDatabase db_write = this.getWritableDatabase();
        db_write.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(ATTACHMENT_STATUS, attachmentStatus);
            values.put(MESSAGE_STATUS, messageStatus);
            db_write.update(MESSAGES_TABLE, values, MESSAGE_ID + " = '" + messageId + "'", null);
            db_write.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db_write.endTransaction();
        }
    }

    public void changeDownloadStatus(String messageID, String attachmentStatus, String downloadPath) {
        SQLiteDatabase db_write = this.getWritableDatabase();
        db_write.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(ATTACHMENT_STATUS, attachmentStatus);
            values.put(ATTACHMENT_PATH, downloadPath);

            db_write.update(MESSAGES_TABLE, values, MESSAGE_ID + " = '" + messageID + "'", null);

            db_write.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db_write.endTransaction();
        }
    }

    public void changeDownloadStatus(String messageID, String upload_status, String downloadPath, String thumbPath) {
        SQLiteDatabase db_write = this.getWritableDatabase();
        db_write.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(ATTACHMENT_STATUS, upload_status);
            values.put(ATTACHMENT_PATH, downloadPath);
            values.put(CUSTOM_DATA, thumbPath);

            db_write.update(MESSAGES_TABLE, values, MESSAGE_ID + " = '" + messageID + "'", null);

            db_write.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db_write.endTransaction();
        }
    }

    public void changProgress(String messageId, String progress) {
        SQLiteDatabase db_write = this.getWritableDatabase();
        db_write.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(ATTACHMENT_PROGRESS, progress);

            db_write.update(MESSAGES_TABLE, values, MESSAGE_ID + " = '" + messageId + "'", null);

            db_write.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db_write.endTransaction();
        }
    }

    public void changDeleteStatus(String messageId, String status) {
        SQLiteDatabase db_write = this.getWritableDatabase();
        db_write.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(MESSAGE_DELETED, status);

            db_write.update(MESSAGES_TABLE, values, MESSAGE_ID + " = '" + messageId + "'", null);

            db_write.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db_write.endTransaction();
        }
    }

    public void changFavouriteStatus(String messageId, String status) {
        SQLiteDatabase db_write = this.getWritableDatabase();
        db_write.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(FAVOURITE_MESSAGE, status);

            db_write.update(MESSAGES_TABLE, values, MESSAGE_ID + " = '" + messageId + "'", null);

            db_write.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db_write.endTransaction();
        }
    }

    public ArrayList<String> getImageVideoAttachments(String mDialogID) {
        SQLiteDatabase db_read = this.getReadableDatabase();
        Cursor cur = null;
        ArrayList<String> mAttachmentList = new ArrayList<String>();
        try {

            String qryAttachment = "Select " + ATTACHMENT_PATH + ", " + ATTACHMENT_URL + ", " + MESSAGE_TYPE + " from " + MESSAGES_TABLE
                    + " where " + CHAT_DIALOG_ID + " ='" + mDialogID + "'";
            cur = db_read.rawQuery(qryAttachment, null);

            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                if (!TextUtils.isEmpty(cur.getString(0)) && !TextUtils.isEmpty(cur.getString(1))
                        && (cur.getString(2).equals(FirebaseChatConstants.TYPE_IMAGE) || cur.getString(2).equals(FirebaseChatConstants.TYPE_VIDEO))) {
                    File ff = new File(cur.getString(0).trim());
                    if (ff.exists()) {
                        mAttachmentList.add(cur.getString(0).trim());
                    }
                }
                cur.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cur != null && !cur.isClosed()) {
                cur.close();
            }
        }
        return mAttachmentList;
    }

    public ArrayList<String> getFavouriteImageVideoAttachments(String mDialogID) {
        SQLiteDatabase db_read = this.getReadableDatabase();
        String value = "1";
        Cursor cur = null;
        ArrayList<String> mAttachmentList = new ArrayList<String>();
        try {

            String qryAttachment = "Select " + ATTACHMENT_PATH + ", " + ATTACHMENT_URL + ", " + MESSAGE_TYPE + " from " + MESSAGES_TABLE
                    + " where " + CHAT_DIALOG_ID + " ='" + mDialogID + "' and " + FAVOURITE_MESSAGE + " = '" + value + "'";
            cur = db_read.rawQuery(qryAttachment, null);

            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                if (!TextUtils.isEmpty(cur.getString(0)) && !TextUtils.isEmpty(cur.getString(1))
                        && (cur.getString(2).equals(FirebaseChatConstants.TYPE_IMAGE) || cur.getString(2).equals(FirebaseChatConstants.TYPE_VIDEO))) {
                    File ff = new File(cur.getString(0).trim());
                    if (ff.exists()) {
                        mAttachmentList.add(cur.getString(0).trim());
                    }
                }
                cur.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cur != null && !cur.isClosed()) {
                cur.close();
            }
        }
        return mAttachmentList;
    }


    public void clearConversation(String dialogId) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {

            String qry3 = "DELETE FROM " + UNREAD_COUNT_TABLE + " where "
                    + CHAT_DIALOG_ID + " = '" + dialogId + "'";
            db.execSQL(qry3);

            String qry4 = "DELETE FROM " + MESSAGES_TABLE + " where "
                    + CHAT_DIALOG_ID + " = '" + dialogId + "'";
            db.execSQL(qry4);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("Exception", "is " + e);
        } finally {
            db.endTransaction();
        }

    }

    public void deleteDialogData(String dialogId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            String chatQuery = "DELETE FROM " + CHATS_TABLE + " where "
                    + CHAT_DIALOG_ID + " = '" + dialogId + "'";
            db.execSQL(chatQuery);

            String deleteQuery = "DELETE FROM " + DIALOGS_TABLE + " where "
                    + CHAT_DIALOG_ID + " = '" + dialogId + "'";
            db.execSQL(deleteQuery);

            String unreadQuery = "DELETE FROM " + UNREAD_COUNT_TABLE + " where "
                    + CHAT_DIALOG_ID + " = '" + dialogId + "'";
            db.execSQL(unreadQuery);

            String blockQuery = "DELETE FROM " + BLOCK_TABLE + " where "
                    + CHAT_DIALOG_ID + " = '" + dialogId + "'";
            db.execSQL(blockQuery);

            String messageQuery = "DELETE FROM " + MESSAGES_TABLE + " where "
                    + CHAT_DIALOG_ID + " = '" + dialogId + "'";
            db.execSQL(messageQuery);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("Exception", "is " + e);
        } finally {
            db.endTransaction();
        }
    }
}
