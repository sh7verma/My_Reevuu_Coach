package com.myreevuuCoach.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.google.firebase.database.*
import com.myreevuuCoach.R
import com.myreevuuCoach.activities.ConversationActivity
import com.myreevuuCoach.activities.LandingActivity
import com.myreevuuCoach.adapters.ChatsAdapter
import com.myreevuuCoach.firebase.ChatsModel
import com.myreevuuCoach.firebase.FirebaseChatConstants
import com.myreevuuCoach.firebase.FirebaseListeners
import com.myreevuuCoach.firebase.ProfileModel
import com.myreevuuCoach.interfaces.InterConst
import com.myreevuuCoach.models.BaseSuccessModel
import com.myreevuuCoach.network.RetrofitClient
import com.myreevuuCoach.utils.AlertDialogs
import com.myreevuuCoach.utils.Constants
import kotlinx.android.synthetic.main.fragment_chat.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * Created by dev on 12/11/18.
 */

class ChatFragment : BaseFragment(), FirebaseListeners.ChatDialogsListenerInterface, View.OnClickListener {

    private var mChatsAdapter: ChatsAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null

    var mChats: LinkedHashMap<String, ChatsModel>? = null
    var mKeys: ArrayList<String> = ArrayList()
    var mCurrentUser: ProfileModel? = null

    override fun getContentView(): Int {
        return R.layout.fragment_chat
    }

    override fun onCreateStuff() {
        mCurrentUser = mDb!!.getProfile(utils.getInt(FirebaseChatConstants.user_id, -1).toString())

        mLayoutManager = LinearLayoutManager(activity)
        rvChats!!.layoutManager = mLayoutManager

        if (mCurrentUser != null) {
            mCurrentUser!!.chat_dialog_ids = mDb!!.getDialogs(mCurrentUser!!.user_id)
            mChats = mDb!!.getAllChats(mCurrentUser!!.user_id)
            mKeys = ArrayList()
            for (key in mChats!!.keys) {
                mKeys.add(key)
            }
            mChatsAdapter = ChatsAdapter(activity, mWidth, mChats, mKeys,
                    mCurrentUser!!.user_id, false)
            rvChats!!.adapter = mChatsAdapter
            setViewOnNotify()
        }
    }

    override fun initListeners() {

        FirebaseListeners.setChatDialogListener(this)

        imgSearch.setOnClickListener(this)
        imgBackSearch.setOnClickListener(this)
        imgCancel.setOnClickListener(this)
        imgNoti.setOnClickListener(this)
        edSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val searString = s.toString().toLowerCase()
                if (mCurrentUser != null) {
                    if (mChats!!.size > 0) {
                        if (searString.trim().length == 0) {
                            mChatsAdapter = ChatsAdapter(activity!!, mWidth, mChats!!, mKeys,
                                    mCurrentUser!!.user_id, false)
                            rvChats.adapter = mChatsAdapter
                            llNoChats.visibility = View.GONE
                        } else {
                            val localChat = LinkedHashMap<String, ChatsModel>()
                            val localKeys = ArrayList<String>()
                            for (i in 0 until mChats!!.size) {
                                val ch = mChats!![mKeys[i]]
                                if (("" + ch!!.name.get(ch.opponent_user_id)).toLowerCase().contains(searString.trim())) {
                                    localChat.put(mKeys[i], ch)
                                    localKeys.add(mKeys[i])
                                }
                            }
                            mChatsAdapter = ChatsAdapter(activity!!, mWidth, localChat, localKeys,
                                    mCurrentUser!!.user_id, true)
                            rvChats.adapter = mChatsAdapter
                            if (localChat != null && localChat.size > 0) {
                                llNoChats.visibility = View.GONE
                            } else {
                                llNoChats.visibility = View.VISIBLE

                            }
                        }
                    }
                }
            }
        })

    }

    fun openConversationActivity(chat: ChatsModel) {
        val intent = Intent(activity, ConversationActivity::class.java)
        intent.putExtra(InterConst.PROFILE_PIC, chat.profile_pic[chat.opponent_user_id])
        intent.putExtra(InterConst.NAME, chat.name[chat.opponent_user_id])
        intent.putExtra("participantIDs", chat.participant_ids)
        startActivity(intent)
    }

    fun deleteConversation(chat: ChatsModel) {
        showAlert(chat)
    }

    override fun onClick(v: View?) {
        when (v) {
            imgSearch -> {
                showKeyboard(activity)
                llToolbarChats.visibility = View.GONE
                llToolbarSearch.visibility = View.VISIBLE

                edSearch.requestFocus()

            }
            imgBackSearch -> {
                edSearch.setText("")
                hideKeyboard(activity)
                llToolbarSearch.visibility = View.GONE
                llToolbarChats.visibility = View.VISIBLE
            }
            imgCancel -> {
                edSearch.setText("")
            }

            imgNoti -> {
                hideUnreadNotificationDot()
                edSearch.setText("")
                (activity as LandingActivity).openNotificationCenterPage()
            }
        }
    }
    private fun hitClearConverastionApi(time: String) {
        val call = RetrofitClient.getInstance().clear_converation(
                utils.getString(InterConst.ACCESS_TOKEN, ""),
                mCurrentUser!!.chat_dialog_ids.toString(),
                time,
                InterConst.DELETE_DIALOG)
        call.enqueue(object : Callback<BaseSuccessModel> {
            override fun onResponse(call: Call<BaseSuccessModel>, response: Response<BaseSuccessModel>) {
                if (response.body().response != null) {

                } else {
                    toast(response.body().error.message)
                    if (response.body().error.code == InterConst.INVALID_ACCESS) {
                        moveToSplash()
                    }
                }
            }

            override fun onFailure(call: Call<BaseSuccessModel>, t: Throwable) {
            }
        })

    }
    internal fun showAlert(chat: ChatsModel) {
        AlertDialogs.confirmYesNoDialog(activity!!, getString(R.string.delete_conversation),
                object : AlertDialogs.DialogClick {
                    override fun yes(dialog: DialogInterface) {

                        FirebaseDatabase.getInstance().reference
                                .child(FirebaseChatConstants.CHATS)
                                .child(chat.participant_ids).child("delete_outer_dialog")
                                .child(mCurrentUser!!.user_id).setValue("1")

                        mDb!!.clearConversation(chat.participant_ids)

                        FirebaseDatabase.getInstance().reference.child(FirebaseChatConstants.CHATS)
                                .child(chat.participant_ids).child("last_message_data")
                                .child(utils!!.getInt(FirebaseChatConstants.user_id, -1).toString())
                                .setValue(FirebaseChatConstants.DEFAULT_MESSAGE_REGEX)

                        FirebaseDatabase.getInstance().reference.child(FirebaseChatConstants.CHATS)
                                .child(chat.participant_ids).child("unread_count")
                                .child(utils!!.getInt(FirebaseChatConstants.user_id, -1).toString()).setValue(0)

                        FirebaseDatabase.getInstance().reference.child(FirebaseChatConstants.CHATS)
                                .child(chat.participant_ids).child("delete_dialog_time")
                                .child(utils!!.getInt(FirebaseChatConstants.user_id, -1).toString())
                                .setValue(ServerValue.TIMESTAMP)

                        FirebaseDatabase.getInstance().reference.child(FirebaseChatConstants.CHATS)
                                .child(chat.participant_ids).child("delete_dialog_time").addListenerForSingleValueEvent(
                                object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        if (dataSnapshot != null) {
                                            var delete_dialog_time: HashMap<String, Long>? = null
                                            val gtDelete = object : GenericTypeIndicator<HashMap<String, Long>>() {
                                            }
                                            delete_dialog_time = dataSnapshot.getValue(gtDelete)
                                            hitClearConverastionApi(delete_dialog_time!!.get(utils!!.getInt(FirebaseChatConstants.user_id, -1).toString()).toString())
                                        }
                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {
                                        Log.e("delete_dialog_time", databaseError.toString())
                                    }
                                })

                    }

                    override fun no(dialog: DialogInterface) {
                        dialog.dismiss()
                    }
                })
    }

    override fun onDialogAdd(mChat: ChatsModel) {
        if (!mChat.chat_dialog_id.contains(InterConst.APP_ADMIN_ID)) {
            if (mChat.delete_outer_dialog.get(utils!!.getInt(FirebaseChatConstants.user_id, -1).toString()).equals("0")) {
                if (mKeys.contains(mChat!!.chat_dialog_id)) { // already
                    mChats!!.put(mChat.chat_dialog_id, mChat)
                } else {
                    mKeys.add(0, mChat.chat_dialog_id)
                    val newmap = mChats!!.clone() as LinkedHashMap<String, ChatsModel>
                    mChats!!.clear()
                    mChats!!.put(mChat.chat_dialog_id, mChat)
                    mChats!!.putAll(newmap)
                }
            }
        }
        mChatsAdapter!!.setBatch(0)
        mChatsAdapter!!.notifyDataSetChanged()
        setViewOnNotify()
    }

    override fun onDialogChanged(mChat: ChatsModel, value: Int) {
        if (value == 0) {
            if (!mChat!!.chat_dialog_id.contains(InterConst.APP_ADMIN_ID)) {
                if (mChat!!.user_type.containsKey(mCurrentUser!!.user_id)) {
                    if (!mKeys.contains(mChat.chat_dialog_id)) {
                        mKeys.add(0, mChat.chat_dialog_id)
                        val newmap = mChats!!.clone() as LinkedHashMap<String, ChatsModel>
                        mChats!!.clear()
                        mChats!!.put(mChat.chat_dialog_id, mChat)
                        mChats!!.putAll(newmap)
                    } else {
                        val msgTime = mChats!![mChat.chat_dialog_id]!!.last_message_time.get(utils!!.getInt(FirebaseChatConstants.user_id, -1).toString())
                        if (mChat.last_message_time.get(utils!!.getInt(FirebaseChatConstants.user_id, -1).toString())!! > msgTime!!) {
                            mChats!!.remove(mChat.chat_dialog_id)
                            val newmap = mChats!!.clone() as LinkedHashMap<String, ChatsModel>
                            mChats!!.clear()
                            mChats!!.put(mChat.chat_dialog_id, mChat)
                            mChats!!.putAll(newmap)
                            mKeys.remove(mChat.chat_dialog_id)
                            mKeys.add(0, mChat.chat_dialog_id)
                        } else {
                            if (mChat.delete_outer_dialog.get(utils!!.getInt(FirebaseChatConstants.user_id, -1).toString()).equals("1")) {
                                mChats!!.remove(mChat.chat_dialog_id)
                                mKeys.remove(mChat.chat_dialog_id)
                            } else {
                                mChats!!.put(mChat.chat_dialog_id, mChat)
                            }
                        }
                    }
                } else {
                    mChats!!.remove(mChat.chat_dialog_id)
                    mKeys.remove(mChat.chat_dialog_id)
                }
            } else {
                utils.setInt(InterConst.APP_ADMIN_UNREAD_COUNT, mChat.unread_count.get(mCurrentUser!!.user_id)!!)
                LocalBroadcastManager.getInstance(activity!!).sendBroadcast(Intent(Constants.NEW_MESSAGE_FROM_ADMIN))
                mChat.unread_count.get(mCurrentUser!!.user_id) != 0
                mChats!!.remove(mChat!!.chat_dialog_id)
                mKeys.remove(mChat.chat_dialog_id)
            }

            if (mKeys.contains(mChat.chat_dialog_id)) {
                if (mChat.delete_outer_dialog.get(utils!!.getInt(FirebaseChatConstants.user_id, -1).toString()).equals("1")) {
                    mChats!!.remove(mChat!!.chat_dialog_id)
                    mKeys.remove(mChat.chat_dialog_id)
                }
            }
            mChatsAdapter!!.setBatch(0)
            mChatsAdapter!!.notifyDataSetChanged()
            setViewOnNotify()
        }
    }

    /*  override fun onDialogChanged(mChat: ChatsModel, value: Int) {
          if (value == 0) {
              if (!mChat!!.chat_dialog_id.contains(InterConst.APP_ADMIN_ID)) {
                  if (mChat!!.user_type.containsKey(mCurrentUser!!.user_id)) {
                      if (!mKeys.contains(mChat.chat_dialog_id)) {
                          mKeys.add(0, mChat.chat_dialog_id)
                          val newmap = mChats!!.clone() as LinkedHashMap<String, ChatsModel>
                          mChats!!.clear()
                          mChats!!.put(mChat.chat_dialog_id, mChat)
                          mChats!!.putAll(newmap)
                      } else {
                          val msgTime = mChats!![mChat.chat_dialog_id]!!.last_message_time.get(utils!!.getInt(FirebaseChatConstants.user_id, -1).toString())
                          if (mChat.last_message_time.get(utils!!.getInt(FirebaseChatConstants.user_id, -1).toString())!! > msgTime!!) {
                              mChats!!.remove(mChat.chat_dialog_id)
                              val newmap = mChats!!.clone() as LinkedHashMap<String, ChatsModel>
                              mChats!!.clear()
                              mChats!!.put(mChat.chat_dialog_id, mChat)
                              mChats!!.putAll(newmap)
                              mKeys.remove(mChat.chat_dialog_id)
                              mKeys.add(0, mChat.chat_dialog_id)
                          } else {
                              if (mChat.delete_outer_dialog.get(utils!!.getInt(FirebaseChatConstants.user_id, -1).toString()).equals("1")) {
                                  mChats!!.remove(mChat.chat_dialog_id)
                                  mKeys.remove(mChat.chat_dialog_id)
                              } else {
                                  mChats!!.put(mChat.chat_dialog_id, mChat)
                              }
                          }
                      }
                  } else {
                      mChats!!.remove(mChat.chat_dialog_id)
                      mKeys.remove(mChat.chat_dialog_id)
                  }
              } else {
                  utils.setInt(InterConst.APP_ADMIN_UNREAD_COUNT, mChat.unread_count.get(mCurrentUser!!.user_id)!!)
                  LocalBroadcastManager.getInstance(activity!!).sendBroadcast(Intent(Constants.NEW_MESSAGE_FROM_ADMIN))
                  mChat.unread_count.get(mCurrentUser!!.user_id) != 0
                  mChats!!.remove(mChat!!.chat_dialog_id)
                  mKeys.remove(mChat.chat_dialog_id)
              }

          } else {
              mChats!!.remove(mChat!!.chat_dialog_id)
              mKeys.remove(mChat.chat_dialog_id)
          }
          if (mKeys.contains(mChat.chat_dialog_id)) {
              if (mChat.delete_outer_dialog.get(utils!!.getInt(FirebaseChatConstants.user_id, -1).toString()).equals("1")) {
                  mChats!!.remove(mChat!!.chat_dialog_id)
                  mKeys.remove(mChat.chat_dialog_id)
              }
          }

          mChatsAdapter!!.setBatch(0)
          mChatsAdapter!!.notifyDataSetChanged()
          setViewOnNotify()
      }*/

    override fun onDialogRemoved(dialogId: String) {
//        var otherUserType = ""
//        for (userUniqueKey in ch!!.user_type.keys) {
//            if (!userUniqueKey.equals(mUtils!!.setString("user_id", ""))) {
//                otherUserType = ch!!.user_type.get(userUniqueKey)!!
//                break
//            }
//        }
        mChats!!.remove(dialogId)
        mKeys.remove(dialogId)
        mChatsAdapter!!.setBatch(0)
        mChatsAdapter!!.notifyDataSetChanged()

        setViewOnNotify()
    }

    private fun setViewOnNotify() {
        if (mChats != null && mChats!!.size > 0) {
            llNoChats.visibility = View.GONE
        } else {
            llNoChats.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        FirebaseListeners.setChatDialogListener(null)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: ChatFragment

        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context

        fun newInstance(context: Context): ChatFragment {
            instance = ChatFragment()
            mContext = context
            return instance
        }
    }


    internal fun hideUnreadNotificationDot() {
        utils.setInt(InterConst.UNREAD_NOTIFICATION_DOT, 0)
        img_unread_dot.setVisibility(View.GONE)
    }

    internal fun showUnreadNotificationDot() {
        img_unread_dot.setVisibility(View.VISIBLE)

    }

    fun checkUnreadNotification() {
        Log.e("checkUnreadNotification", "ChatFragment")
        if (utils.getInt(InterConst.UNREAD_NOTIFICATION_DOT, 0) == 0) {
            hideUnreadNotificationDot()
        } else {
            showUnreadNotificationDot()
        }
    }
}