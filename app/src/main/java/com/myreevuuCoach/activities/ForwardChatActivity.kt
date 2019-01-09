package com.myreevuuCoach.activities

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.google.firebase.database.FirebaseDatabase
import com.myreevuuCoach.R
import com.myreevuuCoach.adapters.ChatsForwardAdapter
import com.myreevuuCoach.firebase.ChatsModel
import com.myreevuuCoach.firebase.FirebaseChatConstants
import com.myreevuuCoach.firebase.FirebaseListeners
import com.myreevuuCoach.firebase.ProfileModel
import com.myreevuuCoach.interfaces.InterConst
import com.myreevuuCoach.utils.AlertDialogs
import kotlinx.android.synthetic.main.fragment_chat.*
import java.util.*

/**
 * Created by dev on 20/12/18.
 */

class ForwardChatActivity : BaseKotlinActivity(), FirebaseListeners.ChatDialogsListenerInterface, View.OnClickListener {
    private var mChatsAdapter: ChatsForwardAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null

    var mChats: LinkedHashMap<String, ChatsModel>? = null
    var mKeys: ArrayList<String> = ArrayList()
    var mCurrentUser: ProfileModel? = null

    override fun initUI() {
        llToolbarSearch.visibility = View.VISIBLE
        llToolbarChats.visibility = View.GONE
        imgCancel.visibility = View.GONE
    }

    override fun initListener() {
        FirebaseListeners.setForwardChatDialogListener(this)

        imgSearch.setOnClickListener(this)
        imgBackSearch.setOnClickListener(this)
        imgCancel.setOnClickListener(this)

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
                            mChatsAdapter = ChatsForwardAdapter(mContext, ForwardChatActivity(), mWidth, mChats!!, mKeys,
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
                            mChatsAdapter = ChatsForwardAdapter(mContext, ForwardChatActivity(), mWidth, localChat, localKeys,
                                    mCurrentUser!!.user_id, true)
                            rvChats.adapter = mChatsAdapter
                            if (localChat != null && localChat.size > 0) {
                                llNoChats.visibility = View.GONE
                            } else {
                                llNoChats.visibility = View.VISIBLE
                                txtNoChat.text = getString(R.string.no_result_found)
                            }
                        }
                    }
                }
            }
        })

    }

    override fun getContext(): Context {
        return this
    }


    override fun getContentView(): Int {
        return R.layout.fragment_chat
    }

    override fun onCreateStuff() {
        mCurrentUser = mDb!!.getProfile(mUtils.getInt(FirebaseChatConstants.user_id, -1).toString())

        mLayoutManager = LinearLayoutManager(this)
        rvChats!!.layoutManager = mLayoutManager

        if (mCurrentUser != null) {
            mCurrentUser!!.chat_dialog_ids = mDb!!.getDialogs(mCurrentUser!!.user_id)
            mChats = mDb!!.getAllChats(mCurrentUser!!.user_id)
            mKeys = ArrayList()
            for (key in mChats!!.keys) {
                mKeys.add(key)
            }
            mChatsAdapter = ChatsForwardAdapter(mContext, this, mWidth, mChats, mKeys,
                    mCurrentUser!!.user_id, false)
            rvChats!!.adapter = mChatsAdapter
            setViewOnNotify()
        }
    }


    fun openConversationActivity(chat: ChatsModel) {
        val intent = Intent(this, ConversationActivity::class.java)
        intent.putExtra(InterConst.PROFILE_PIC, chat.profile_pic[chat.opponent_user_id])
        intent.putExtra(InterConst.FORWARD_MESSAGE, getIntent().getStringExtra(InterConst.FORWARD_MESSAGE))
        intent.putExtra(InterConst.NAME, chat.name[chat.opponent_user_id])
        intent.putExtra("participantIDs", chat.participant_ids)
        startActivity(intent)
        finish()
    }


    override fun onClick(v: View?) {
        when (v) {
            imgSearch -> {
                llToolbarChats.visibility = View.GONE
                llToolbarSearch.visibility = View.VISIBLE
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    edSearch.focusable
                }
            }
            imgBackSearch -> {
                edSearch.setText("")
                hideKeyboard(this)
                llToolbarSearch.visibility = View.GONE
                llToolbarChats.visibility = View.VISIBLE
            }
            imgCancel -> {
                edSearch.setText("")
            }
        }
    }

    internal fun showAlert(chat: ChatsModel) {
        AlertDialogs.confirmYesNoDialog(this!!, "Are you sure you want to delete this conversation?", object : AlertDialogs.DialogClick {
            override fun yes(dialog: DialogInterface) {
                FirebaseDatabase.getInstance().reference
                        .child(FirebaseChatConstants.CHATS)
                        .child(chat.participant_ids).child("delete_outer_dialog")
                        .child(mCurrentUser!!.user_id).setValue("1")

            }

            override fun no(dialog: DialogInterface) {
                dialog.dismiss()
            }
        })
    }

    override fun onDialogAdd(mChat: ChatsModel) {
        if (mChat.delete_outer_dialog.get(mUtils!!.getInt(FirebaseChatConstants.user_id, -1).toString()).equals("0")) {
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
        mChatsAdapter!!.notifyDataSetChanged()
        setViewOnNotify()
    }

    override fun onDialogChanged(mChat: ChatsModel, value: Int) {
        if (value == 0) {
            if (mChat!!.user_type.containsKey(mCurrentUser!!.user_id)) {
                if (!mKeys.contains(mChat.chat_dialog_id)) {
                    mKeys.add(0, mChat.chat_dialog_id)
                    val newmap = mChats!!.clone() as LinkedHashMap<String, ChatsModel>
                    mChats!!.clear()
                    mChats!!.put(mChat.chat_dialog_id, mChat)
                    mChats!!.putAll(newmap)
                } else {
                    val msgTime = mChats!![mChat.chat_dialog_id]!!.last_message_time.get(mUtils!!.getInt(FirebaseChatConstants.user_id, -1).toString())
                    if (mChat.last_message_time.get(mUtils!!.getInt(FirebaseChatConstants.user_id, -1).toString())!! > msgTime!!) {
                        mChats!!.remove(mChat.chat_dialog_id)
                        val newmap = mChats!!.clone() as LinkedHashMap<String, ChatsModel>
                        mChats!!.clear()
                        mChats!!.put(mChat.chat_dialog_id, mChat)
                        mChats!!.putAll(newmap)
                        mKeys.remove(mChat.chat_dialog_id)
                        mKeys.add(0, mChat.chat_dialog_id)
                    } else {
                        if (mChat.delete_outer_dialog.get(mUtils!!.getInt(FirebaseChatConstants.user_id, -1).toString()).equals("1")) {
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
            mChats!!.remove(mChat!!.chat_dialog_id)
            mKeys.remove(mChat.chat_dialog_id)
        }

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

    override fun onDialogRemoved(dialogId: String) {
        mChats!!.remove(dialogId)
        mKeys.remove(dialogId)
        mChatsAdapter!!.notifyDataSetChanged()
        setViewOnNotify()
    }

    override fun onDestroy() {
        super.onDestroy()
        FirebaseListeners.setForwardChatDialogListener(null)
    }

}