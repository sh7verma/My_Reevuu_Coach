package com.myreevuuCoach.activities

import android.app.AlertDialog
import android.app.Dialog
import android.app.NotificationManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import com.google.firebase.database.*
import com.myreevuuCoach.R
import com.myreevuuCoach.adapters.ConversationAdapter
import com.myreevuuCoach.firebase.*
import com.myreevuuCoach.interfaces.InterConst
import com.myreevuuCoach.models.BaseSuccessModel
import com.myreevuuCoach.network.RetrofitClient
import com.myreevuuCoach.utils.RoundedTransformation
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_conversation.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by dev on 18/12/18.
 */

class ConversationActivity : BaseActivity(), FirebaseListeners.ChatDialogsListenerInterfaceForChat,
        FirebaseListeners.MessageListenerInterface {


    private var mConversationActivity: ConversationActivity? = null
    private var mConversationAdapter: ConversationAdapter? = null

    private var status = 0
    private var mOpponentUser: ProfileModel? = null
    private var mCurrentUser: ProfileModel? = null
    private var mPrivateChat: ChatsModel? = null
    var mParticpantIDS = ""
    var mOtherUserId = ""
    private var mOtherUserPic = ""
    private var mOtherUserName = ""
    private var myUserId = ""
    private var msgId = ""

    private var usersQuery: Query? = null
    private var mFirebaseConfigProfile = FirebaseDatabase.getInstance().reference.child(FirebaseChatConstants.USERS)
    private var mFirebaseConfigChats = FirebaseDatabase.getInstance().reference.child(FirebaseChatConstants.CHATS)
    private var mFirebaseConfigMessages = FirebaseDatabase.getInstance().reference.child(FirebaseChatConstants.MESSAGES)
    private var mFirebaseConfigNotifications = FirebaseDatabase.getInstance().reference.child(FirebaseChatConstants.NOTIFICATIONS)
    private var mFirebaseConfigReadStatus = FirebaseDatabase.getInstance().reference.child(FirebaseChatConstants.READ_STATUS)

    private var chat_date_format = SimpleDateFormat("dd-MM-yyyy", Locale.US)
    private var today_date_format = SimpleDateFormat("hh:mm aa", Locale.US)
    private var only_date_format = SimpleDateFormat("dd MMMM", Locale.US)
    private var mTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
    private var show_dateheader_format = SimpleDateFormat("dd MMM yyyy", Locale.US)

    internal var limit = 20
    private var setReadMessages: ArrayList<MessagesModel> = ArrayList()

    var mMessagesMap: HashMap<String, MessagesModel>? = null
    var mMessageIds: ArrayList<String> = ArrayList()

    var selectedPosition = 0
    var noResultsinPagination = false

    var clearTime = 0L

    override fun getContentView() = R.layout.activity_conversation

    override fun initUI() {
        mMessagesMap = HashMap()
        mMessageIds = ArrayList()
        mConversationActivity = this
        myUserId = mUtils.getInt(FirebaseChatConstants.user_id, -1).toString()

        if (intent.hasExtra(InterConst.NAME)) {
            mOtherUserName = intent.getStringExtra(InterConst.NAME)
            mOtherUserPic = intent.getStringExtra(InterConst.PROFILE_PIC)

            setProfileViewData()
        }
//        val send_pams = RelativeLayout.LayoutParams((mWidth * 0.12).toInt(), (mWidth * 0.12).toInt())
//        send_pams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
//        send_pams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
//        send_pams.setMargins(0, 0, mWidth / 48, mWidth / 48)

        edMessage.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim({ it <= ' ' }).isNotEmpty()) {
                    imgSendMessage.visibility = View.VISIBLE
                } else {
                    imgSendMessage.visibility = View.GONE
                }
            }
        })
    }


    override fun onCreateStuff() {
//        FirebaseListeners.getListenerClass(this)
//                .setProfileListener(mUtils!!.getInt(FirebaseChatConstants.user_id, -1).toString())

        setReadMessages = ArrayList()
        mCurrentUser = db!!.getProfile(mUtils!!.getInt(FirebaseChatConstants.user_id, -1).toString())
        if (mCurrentUser == null) {
            return
        }
        mCurrentUser!!.chat_dialog_ids = db!!.getDialogs(mCurrentUser!!.user_id)

        if (intent.hasExtra("participantIDs")) {
            mParticpantIDS = intent.getStringExtra("participantIDs")
            val particID = mParticpantIDS.split(",").toTypedArray()
            for (id in particID) {
                val userId = id.split("_").toTypedArray()
                if (!userId[0].trim().equals(mCurrentUser!!.user_id, ignoreCase = true)) {
                    mOtherUserId = userId[0].trim()
                    mOpponentUser = db!!.getProfile(userId[0].trim())
                    break
                }
            }
        } else {
            mParticpantIDS = mUtils!!.getString("participant_ids", "")
            val particID = mParticpantIDS.split(",").toTypedArray()
            for (id in particID) {
                val userId = id.split("_").toTypedArray()
                if (!userId[0].trim().equals(mCurrentUser!!.user_id, ignoreCase = true)) {
                    mOtherUserId = userId[0].trim()
                    mOpponentUser = db!!.getProfile(userId[0].trim())
                }
            }
            mUtils!!.setString("participant_ids", "")
            try {
                mOtherUserName = db.getChatDialog(mParticpantIDS, mCurrentUser!!.user_id, mOtherUserId)
                        .name
                        .get(mOtherUserId)
                        .toString()
                mOtherUserPic = db.getChatDialog(mParticpantIDS, mCurrentUser!!.user_id, mOtherUserId)
                        .profile_pic
                        .get(mOtherUserId)
                        .toString()

                setProfileViewData()
            } catch (e: Exception) {

            }

        }
        FirebaseListeners.setChatDialogListenerForChat(this, mParticpantIDS)
        FirebaseListeners.setMessageListener(this, mParticpantIDS)
        chatSetUp()

        if (intent.hasExtra(InterConst.FORWARD_MESSAGE)) {
            edMessage.setText(intent.getStringExtra(InterConst.FORWARD_MESSAGE).toString())
            if (connectedToInternet())
                sendTextMessage()
            else
                showInternetAlert(txtName)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        if (intent!!.hasExtra(InterConst.NAME)) {
            mOtherUserName = intent.getStringExtra(InterConst.NAME)
            mOtherUserPic = intent.getStringExtra(InterConst.PROFILE_PIC)

            setProfileViewData()
        }
        val parIds: String
        if (intent!!.hasExtra("participantIDs")) {
            parIds = intent.getStringExtra("participantIDs")
        } else {
            parIds = mUtils!!.getString("participant_ids", "")
            mUtils!!.setString("participant_ids", "")
        }

        if (parIds.equals(mParticpantIDS)) {
            //  same chat
            if (intent.hasExtra(InterConst.FORWARD_MESSAGE)) {
                edMessage.setText(intent.getStringExtra(InterConst.FORWARD_MESSAGE).toString())
                if (connectedToInternet()) {
                    sendTextMessage()
                } else {
                    hideKeyboard(mConversationActivity)
                    showInternetAlert(txtName)
                }
            }

        } else {
            // change chat data
            if (usersQuery != null) {
                usersQuery!!.removeEventListener(mOpponentUserListener)
            }
            llDefaultActionbar.visibility = View.VISIBLE
            llOptionActionbar.visibility = View.GONE
            edMessage.setText("")
            val inp = arrayOf<InputFilter>(InputFilter.LengthFilter(4000))
            edMessage.filters = inp
            selectedPosition = 0
            setReadMessages = ArrayList()
            mMessagesMap = HashMap()
            mMessageIds = ArrayList<String>()
            mCurrentUser!!.chat_dialog_ids = db!!.getDialogs(mCurrentUser!!.user_id)
            mParticpantIDS = parIds
            val particID = mParticpantIDS.split(",").toTypedArray()
            for (id in particID) {
                val userId = id.split("_").toTypedArray()
                if (!userId[0].trim().equals(mCurrentUser!!.user_id, ignoreCase = true)) {
                    mOtherUserId = userId[0].trim()
                    mOpponentUser = db!!.getProfile(userId[0].trim())
                }
            }
            FirebaseListeners.setChatDialogListenerForChat(this, mParticpantIDS)
            FirebaseListeners.setMessageListener(this, mParticpantIDS)
            chatSetUp()

            if (intent.hasExtra(InterConst.FORWARD_MESSAGE)) {
                edMessage.setText(intent.getStringExtra(InterConst.FORWARD_MESSAGE).toString())
                if (connectedToInternet()) {
                    sendTextMessage()
                } else {
                    hideKeyboard(mConversationActivity)
                    showInternetAlert(txtName)
                }
            }

            super.onNewIntent(intent)
        }
    }

    private fun setProfileViewData() {

        if (mOtherUserId.equals(InterConst.APP_ADMIN_ID) || mOtherUserName.equals(InterConst.APP_ADMIN_NAME)) {
            txtName.text = InterConst.APP_ADMIN_NAME
            Picasso.get()
                    .load(R.drawable.ic_contact)
                    .transform(RoundedTransformation(mHeight * 10, 0))
                    .resize((mHeight * 0.125).toInt(), (mHeight * 0.125).toInt())
                    .placeholder(R.drawable.ic_contact)
                    .centerCrop(Gravity.TOP)
                    .error(R.drawable.ic_contact).into(imgProfile)
        } else {
            txtName.text = mOtherUserName

            if (!TextUtils.isEmpty(mOtherUserPic)) {
                Picasso.get()
                        .load(mOtherUserPic)
                        .transform(RoundedTransformation(mHeight * 10, 0))
                        .resize((mHeight * 0.125).toInt(), (mHeight * 0.125).toInt())
                        .placeholder(R.mipmap.ic_profile)
                        .centerCrop(Gravity.TOP)
                        .error(R.mipmap.ic_profile).into(imgProfile)
            } else {
                Picasso.get()
                        .load(R.mipmap.ic_profile)
                        .transform(RoundedTransformation(mHeight * 10, 0))
                        .resize((mHeight * 0.125).toInt(), (mHeight * 0.125).toInt())
                        .placeholder(R.mipmap.ic_profile)
                        .centerCrop(Gravity.TOP)
                        .error(R.mipmap.ic_profile).into(imgProfile)
            }
        }
    }

    fun isOptionsVisible() {
        if (llDefaultActionbar.visibility == View.VISIBLE) {
            selectedPosition = 0
        } else {
            selectedPosition = 0
            llDefaultActionbar.visibility = View.VISIBLE
            llOptionActionbar.visibility = View.GONE
        }
    }

    fun makeOptionsVisible(pos: Int, messageId: String, type: Int) {
        selectedPosition = pos
        msgId = messageId

        if (type == 1) {
            imgCopy.visibility = View.VISIBLE
            if (mOtherUserId.equals(InterConst.APP_ADMIN_ID)) {
                imgForward.visibility = View.GONE
            }
        } else {
            imgCopy.visibility = View.GONE
            if (mOtherUserId.equals(InterConst.APP_ADMIN_ID)) {
                imgForward.visibility = View.GONE
            }
        }

        llDefaultActionbar.visibility = View.GONE
        llOptionActionbar.visibility = View.VISIBLE
    }

    override fun onResume() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()

        for (i in setReadMessages.indices) {
            val message = setReadMessages[i]
            if (!message.sender_id.equals(mCurrentUser!!.user_id)) {
                // message has been received by me
                if (message.message_status != FirebaseChatConstants.STATUS_MESSAGE_SEEN) {
                    mFirebaseConfigMessages.child(mPrivateChat!!.chat_dialog_id).child(message.message_id).child("message_status").setValue(FirebaseChatConstants.STATUS_MESSAGE_SEEN)
                    val msgHashMap = HashMap<String, Any>()
                    msgHashMap.put("message_id", message.message_id)
                    msgHashMap.put("message_status", FirebaseChatConstants.STATUS_MESSAGE_SEEN)
                    mFirebaseConfigReadStatus.child(message.message_id).setValue(msgHashMap)
                }
            }
        }
        if (mPrivateChat != null && mPrivateChat!!.unread_count.get(mCurrentUser!!.user_id) != 0) {
            mFirebaseConfigChats.child(mPrivateChat!!.chat_dialog_id).child("unread_count").child(mCurrentUser!!.user_id).setValue(0)
        }
        setReadMessages.clear()
        mUtils!!.setString("chat_dialog_id", mParticpantIDS)
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        mUtils!!.setString("chat_dialog_id", "")
    }

    fun chatSetUp() {
        if (mOpponentUser == null) {
            mOpponentUser = ProfileModel()
            mOpponentUser!!.user_id = mOtherUserId
        }

        mPrivateChat = db!!.getChatDialog("" + mParticpantIDS,
                mUtils!!.getInt(FirebaseChatConstants.user_id, -1).toString(), mOtherUserId)

        listenUser()

        if (mPrivateChat == null) {
            // no entry in database, check firebase
            val query = mFirebaseConfigChats.orderByChild("participant_ids").equalTo("" + mParticpantIDS)
            query.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onCancelled(firebaseError: DatabaseError) {
                    showAlertSnackBar(txtName, "" + firebaseError)
                    finish()
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.value == null) {
                        // no entry on firebase as well
                        createNewChat()
                    } else {
                        // add value to db
                        for (postSnapshot in dataSnapshot.children) {
                            val mChat = ChatsModel.parseChat(postSnapshot,
                                    mUtils!!.getInt(FirebaseChatConstants.user_id, -1).toString())
                            if (mChat != null) {
                                mChat.chat_dialog_id = postSnapshot.key
                                db!!.addNewChat(mChat, mUtils!!.getInt(FirebaseChatConstants.user_id, -1).toString(), mOtherUserId)
                                mPrivateChat = mChat
                            }
                        }
                        if (mPrivateChat == null) {
                            createNewChat()
                        } else {
                            setHeaderData()
                            addMessageListener()
                        }
                    }
                }
            })
        } else {

            mOtherUserId = mPrivateChat!!.opponent_user_id
            mOtherUserName = mPrivateChat!!.name.get(mOtherUserId).toString()
            mOtherUserPic = mPrivateChat!!.profile_pic.get(mOtherUserId).toString()
            setProfileViewData()
            setHeaderData()
            addMessageListener()
        }
    }

    private fun setHeaderData() {
        mOtherUserId = mPrivateChat!!.opponent_user_id
        mOtherUserName = mPrivateChat!!.name.get(mOtherUserId).toString()
        mOtherUserPic = mPrivateChat!!.profile_pic.get(mOtherUserId).toString()
        setProfileViewData()
    }

    internal fun createNewChat() {
        val mParticpantIDSList = ArrayList<String>()
        if (mOtherUserName.equals(InterConst.APP_ADMIN_NAME)) {
            mParticpantIDSList.add(mOtherUserId + "_" + FirebaseChatConstants.TYPE_ADMIN.toString())
        } else {
            mParticpantIDSList.add(mOtherUserId + "_" + FirebaseChatConstants.TYPE_ATHLETE.toString())
        }
        mParticpantIDSList.add(myUserId + "_" + FirebaseChatConstants.TYPE_COACH)

        mParticpantIDSList.sort()

        var mParticpantIDS = mParticpantIDSList.toString()
        var participants = mParticpantIDS.substring(1, mParticpantIDS.length - 1)
        participants = participants.replace(" ", "")
        val mChat = ChatsModel()
        mChat.chat_dialog_id = participants
        mChat.last_message = FirebaseChatConstants.DEFAULT_MESSAGE_REGEX


        mChat.last_message_sender_id = myUserId.toString()
        mChat.last_message_id = "0"
        mChat.participant_ids = participants
        val unread = HashMap<String, Int>()
        unread.put(myUserId.toString(), 0)
        unread.put(mOtherUserId.toString(), 0)
        mChat.unread_count = unread

        val name = HashMap<String, String>()
        name.put(myUserId, mSigUpModel.response.name)
        name.put(mOtherUserId, mOtherUserName)
        mChat.name = name


        val lastMessageType = HashMap<String, String>()
        lastMessageType.put(myUserId, "")
        lastMessageType.put(mOtherUserId, "")
        mChat.last_message_type = lastMessageType

        val lastMessageData = HashMap<String, String>()
        lastMessageData.put(myUserId, FirebaseChatConstants.DEFAULT_MESSAGE_REGEX)
        lastMessageData.put(mOtherUserId, FirebaseChatConstants.DEFAULT_MESSAGE_REGEX)
        mChat.last_message_data = lastMessageData

        val pic = HashMap<String, String>()
        pic.put(myUserId,
                mSigUpModel.response.profile_pic)
        pic.put(mOtherUserId, mOtherUserPic)
        mChat.profile_pic = pic

        // val time = ServerValue.TIMESTAMP
        val utcTime = FirebaseChatConstants.getLocalTime(Calendar.getInstance().timeInMillis)
        val deleteTime = HashMap<String, Long>()
        deleteTime.put(myUserId, utcTime)
        deleteTime.put(mOtherUserId, utcTime)
        mChat.delete_dialog_time = deleteTime

        val lastTime = HashMap<String, Long>()
        lastTime.put(myUserId, utcTime)
        lastTime.put(mOtherUserId, utcTime)
        mChat.last_message_time = lastTime

        val block = HashMap<String, String>()
        block.put(myUserId, "0")
        block.put(mOtherUserId, "0")
        mChat.block_status = block

        val rating = HashMap<String, String>()
        rating.put(myUserId, "0")
        rating.put(mOtherUserId, "0")
        mChat.rating = rating

        val message_rating_count = HashMap<String, Int>()
        message_rating_count.put(myUserId, 0)
        message_rating_count.put(mOtherUserId, 0)
        mChat.message_rating_count = message_rating_count

        val userType = HashMap<String, String>()
        userType.put(myUserId, FirebaseChatConstants.TYPE_COACH.toString())
        if (mOtherUserName.equals(InterConst.APP_ADMIN_NAME)) {
            mParticpantIDSList.add(mOtherUserId + "_" + FirebaseChatConstants.TYPE_ADMIN.toString())
        } else {
            mParticpantIDSList.add(mOtherUserId + "_" + FirebaseChatConstants.TYPE_ATHLETE.toString())
        }
        mChat.user_type = userType
        val deleteOuterDialog = HashMap<String, String>()
        deleteOuterDialog.put(myUserId, "0")
        deleteOuterDialog.put(mOtherUserId, "0")
        mChat.delete_outer_dialog = deleteOuterDialog

        val mFirebaseConfig = FirebaseDatabase.getInstance().reference.child(FirebaseChatConstants.CHATS)
        mFirebaseConfig.child(participants).setValue(mChat).addOnSuccessListener {
            mChat.opponent_user_id = mOtherUserId
            db!!.addNewChat(mChat, myUserId, mOtherUserId)

            mPrivateChat = mChat
            addMessageListener()

            val mFirebaseConfigChat: DatabaseReference
            mFirebaseConfigChat = FirebaseDatabase.getInstance().reference.child(FirebaseChatConstants.CHATS)
            mFirebaseConfigChat.child(participants).child("delete_dialog_time").child(
                    myUserId)
                    .setValue(ServerValue.TIMESTAMP)
            mFirebaseConfigChat.child(participants).child("delete_dialog_time").child(
                    mOtherUserId)
                    .setValue(ServerValue.TIMESTAMP)

            val mFirebaseConfigUser: DatabaseReference
            mFirebaseConfigUser = FirebaseDatabase.getInstance().reference.child(FirebaseChatConstants.USERS)
            mFirebaseConfigUser.child("id_$mOtherUserId").child("chat_dialog_ids").child(participants)
                    .setValue(participants)
            mFirebaseConfigUser.child("id_" + myUserId)
                    .child("chat_dialog_ids").child(participants).setValue(participants)

            if (mOtherUserId.equals(InterConst.APP_ADMIN_ID)) {
                mUtils!!.setString("chat_dialog_id", mConversationActivity!!.mParticpantIDS)
                sendFirstMessageFromAdmin()
            }

        }.addOnFailureListener {

        }
    }


    internal fun listenUser() {
        usersQuery = mFirebaseConfigProfile.orderByKey().equalTo("id_$mOtherUserId")
        usersQuery!!.addChildEventListener(mOpponentUserListener)
    }

    internal var mOpponentUserListener: ChildEventListener = object : ChildEventListener {

        override fun onChildAdded(dataSnapshot: com.google.firebase.database.DataSnapshot, s: String?) {
            val user = ProfileModel.parseProfile(dataSnapshot)
            if (mOtherUserId.contains(InterConst.APP_ADMIN_ID)) {
                user.user_id = InterConst.APP_ADMIN_ID
            }
            db!!.addProfile(user)
            mOpponentUser = db!!.getProfile(user.user_id)
            setOnlineFlag(user)

//            txtName.text = user.user_name
//            if (!user.user_pic.equals(mOpponentUser!!.user_pic)) {
//                Picasso.with(mContext).load(mOpponentUser!!.user_pic).placeholder(R.drawable.placeholder_image).into(imgProfileAvatar)
//            }
        }

        override fun onChildChanged(dataSnapshot: com.google.firebase.database.DataSnapshot, s: String?) {
            val user = ProfileModel.parseProfile(dataSnapshot)
            if (mOtherUserId.contains(InterConst.APP_ADMIN_ID)) {
                user.user_id = InterConst.APP_ADMIN_ID
            }
            db!!.addProfile(user)
            mOpponentUser = db!!.getProfile(user.user_id)
            setOnlineFlag(user)

//            txtName.text = user.user_name
//            if (!user.user_pic.equals(mOpponentUser!!.user_pic)) {
//                Picasso.with(mContext).load(mOpponentUser!!.user_pic).placeholder(R.drawable.placeholder_image).into(imgProfileAvatar)
//            }
        }

        override fun onChildRemoved(dataSnapshot: com.google.firebase.database.DataSnapshot) {

        }

        override fun onChildMoved(dataSnapshot: com.google.firebase.database.DataSnapshot, s: String?) {

        }

        override fun onCancelled(databaseError: DatabaseError) {

        }
    }

    internal fun setOnlineFlag(mUser: ProfileModel) {
        val online_format = SimpleDateFormat("HH:mm", Locale.US)
        if (mUser.online_status == FirebaseChatConstants.ONLINE_LONG) {

            if (mPrivateChat != null) {
                if (mPrivateChat!!.block_status.get(mOpponentUser!!.user_id).equals("1")) {
                    txtActive.text = ""
                } else {
                    if (mPrivateChat!!.block_status.get(mCurrentUser!!.user_id).equals("1")) {
                        txtActive.text = ""
                    } else {
                        txtActive.text = FirebaseChatConstants.ONLINE
                    }
                }
            } else {
                txtActive.text = FirebaseChatConstants.ONLINE
            }

        } else {
            var last_seen_status = ""
            try {
                val current = Calendar.getInstance()
                val current_time = current.time
                val today = chat_date_format.format(current_time)
                val time = FirebaseChatConstants.getLocalTime(mUser.online_status)
                val cl = Calendar.getInstance()
                cl.timeInMillis = time
                val dd = mTime.format(cl.time)
                val utc_date = mTime.parse(dd)
                val utc_create = Calendar.getInstance()
                utc_create.time = utc_date

                if (today.equals(chat_date_format.format(utc_create.time), ignoreCase = true)) {
                    //today
                    last_seen_status = resources.getString(R.string.Last_seen_today_at) + " " +
                            online_format.format(utc_create.time)
                } else {
                    current.add(Calendar.DATE, -1)
                    val yesterday = current.time
                    if (chat_date_format.format(yesterday).equals(chat_date_format.format(utc_create.time), ignoreCase = true)) {
                        //yesterday
                        last_seen_status = resources.getString(R.string.Last_seen_yesterday_at) + " " +
                                online_format.format(utc_create.time)
                    } else {
                        last_seen_status = resources.getString(R.string.Last_seen) + " " +
                                only_date_format.format(utc_create.time) + " " + "at" + " " + online_format.format(utc_create.time)
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (mPrivateChat != null) {
                if (mPrivateChat!!.block_status.get(mOpponentUser!!.user_id).equals("1")) {
                    txtActive.text = ""
                } else {
                    if (mPrivateChat!!.block_status.get(mCurrentUser!!.user_id).equals("1")) {
                        txtActive.text = ""
                    } else {
                        txtActive.text = "offline"
                    }
                }
            } else {
                txtActive.text = "offline"
            }
        }
    }

    override fun onDialogChanged(mChat: ChatsModel?, value: Int) {
        try {
            if (value == 0) {
                if (mPrivateChat!!.chat_dialog_id.equals(mChat!!.chat_dialog_id)) {
                    txtName.text = mPrivateChat!!.name.get(mOtherUserId)
                    if (!mPrivateChat!!.profile_pic.get(mOtherUserId).equals(mChat.profile_pic.get(mOtherUserId))) {
//                        Picasso.get().load(mChat.profile_pic.get(mOtherUserId)).placeholder(R.drawable.placeholder_image).into(imgProfileAvatar)
                    }
                    mPrivateChat = mChat
                }
            } else {
                Toast.makeText(mContext!!, mPrivateChat!!.name.get(mOtherUserId) + " " + "unmatch_message", Toast.LENGTH_SHORT).show()
                moveBack()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    internal fun addMessageListener() {
        FirebaseListeners.getListenerClass(applicationContext).setListener(mPrivateChat!!.chat_dialog_id)

        val deletetime = mPrivateChat!!.delete_dialog_time.get(mUtils!!.getInt(FirebaseChatConstants.user_id, -1).toString())

        mMessagesMap = db!!.getAllMessages(mPrivateChat!!.chat_dialog_id, mCurrentUser!!.user_id, limit,
                deletetime!!, mOtherUserId)

        makeHeaders()

        mConversationAdapter = ConversationAdapter(this, mConversationActivity, mWidth, mCurrentUser!!.user_id,
                mOtherUserId, mPrivateChat!!.participant_ids, mPrivateChat)

        mConversationAdapter!!.animationStatus(false)
        lvChatList.adapter = mConversationAdapter
    }

    internal fun makeHeaders() {
        mMessageIds = ArrayList()
        for (key in mMessagesMap!!.keys) {
            mMessageIds.add(key)
        }
        if (mMessagesMap!!.size <= 1) {
            mMessagesMap!!.clear()
            mMessageIds.clear()
        }
        for (i in 0 until mMessagesMap!!.size) {
            val message = mMessagesMap!![mMessageIds[i]]
            if (!message!!.is_header) {
//
                if (!message.sender_id.equals(mCurrentUser!!.user_id)) {
                    // message has been received by me
                    if (message.message_status != FirebaseChatConstants.STATUS_MESSAGE_SEEN) {
                        mFirebaseConfigMessages.child(message.chat_dialog_id).child(message.message_id).child("message_status").setValue(FirebaseChatConstants.STATUS_MESSAGE_SEEN)
                        val msgHashMap = HashMap<String, Any>()
                        msgHashMap.put("message_id", message.message_id)
                        msgHashMap.put("message_status", FirebaseChatConstants.STATUS_MESSAGE_SEEN)
                        mFirebaseConfigReadStatus.child(message.message_id).setValue(msgHashMap)
                    }
                }
            }
        }

        if (mPrivateChat!!.unread_count.get(mCurrentUser!!.user_id) != 0) {
            mFirebaseConfigChats.child(mPrivateChat!!.chat_dialog_id).child("unread_count").child(mCurrentUser!!.user_id).setValue(0)
        }
    }

    override fun initListener() {
        imgBackDefault.setOnClickListener(this)
        llViewProfile.setOnClickListener(this)
        imgOptions.setOnClickListener(this)
        imgForward.setOnClickListener(this)
        imgBackOption.setOnClickListener(this)
        imgDelete.setOnClickListener(this)
        imgCopy.setOnClickListener(this)
        imgSendMessage.setOnClickListener(this)

        lvChatList.setOnScrollListener(object : AbsListView.OnScrollListener {

            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
                // TODO Auto-generated method stub
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (lvChatList.firstVisiblePosition == 0 && mMessagesMap!!.size > 20 && !noResultsinPagination) {
                        loadMore()
                    }
                }
            }

            override fun onScroll(view: AbsListView, firstVisibleItem: Int,
                                  visibleItemCount: Int, totalItemCount: Int) {
                // TODO Auto-generated method stub
            }
        })

    }

    internal fun loadMore() {
        limit = mMessagesMap!!.size + FirebaseChatConstants.LOAD_MORE_VALUE
        val deletetime = mPrivateChat!!.delete_dialog_time.get(mUtils!!.getInt(FirebaseChatConstants.user_id, -1).toString())
        val local = db!!.getAllMessages(mPrivateChat!!.chat_dialog_id, mCurrentUser!!.user_id, limit, deletetime!!, mOtherUserId)
        if (local.size <= mMessagesMap!!.size) {
            // nothing new
            noResultsinPagination = true
        } else {
            noResultsinPagination = false
            val pos = mMessagesMap!!.size
            mMessagesMap = local
            makeHeaders()
            val pp = mMessagesMap!!.size - pos
            mConversationAdapter!!.animationStatus(false)
            mConversationAdapter!!.notifyDataSetChanged()
            lvChatList.setSelection(pp)
        }
    }

    override fun getContext() = this

    override fun onClick(v: View?) {
        when (v) {
            imgBackDefault -> {
                hideKeyboard(this)
                moveBack()
            }
            llViewProfile -> {
                if (!mOtherUserId.equals(InterConst.APP_ADMIN_ID)) {
                    val intent = Intent(mContext, OthersProfileActivity::class.java)

                    intent.putExtra(InterConst.PROFILE_PIC, mOtherUserPic)
                    intent.putExtra(InterConst.NAME, mOtherUserName)
                    intent.putExtra(InterConst.ID, Integer.parseInt(mOtherUserId))
                    startActivity(intent)
                }
            }
            imgOptions -> {
                optionDialog()
            }
            imgBackOption -> {
                selectedPosition = 0
                llOptionActionbar.visibility = View.GONE
                llDefaultActionbar.visibility = View.VISIBLE
                mConversationAdapter!!.remove_selection()
            }
            imgForward -> {
                val intent = Intent(mContext, ForwardChatActivity::class.java)
                intent.putExtra(InterConst.FORWARD_MESSAGE, mMessagesMap!![msgId]!!.message)
                startActivity(intent)
                selectedPosition = 0
                llDefaultActionbar.visibility = View.VISIBLE
                llOptionActionbar.visibility = View.GONE
                mConversationAdapter!!.remove_selection()
            }
            imgDelete -> {
                if (connectedToInternet()) {
                    if (mMessagesMap!![msgId]!!.message_status < FirebaseChatConstants.STATUS_MESSAGE_SEEN)
                        mFirebaseConfigMessages.child(mPrivateChat!!.chat_dialog_id).child(msgId).child("message_deleted").child(mCurrentUser!!.user_id).setValue("1")
                    if (msgId.equals(mMessageIds.get(mMessageIds.size - 1))) {
                        if (mMessageIds.size > 2) {
                            for (i in mMessageIds.size - 2 downTo 0) {
                                if (!mMessagesMap!![mMessageIds.get(i)]!!.is_header) {
                                    mFirebaseConfigChats.child(mPrivateChat!!.chat_dialog_id)
                                            .child("last_message_data").child(mCurrentUser!!.user_id)
                                            .setValue(mMessagesMap!![mMessageIds.get(i)]!!.message)

                                    mFirebaseConfigChats.child(mPrivateChat!!.chat_dialog_id)
                                            .child("last_message_type").child(mCurrentUser!!.user_id)
                                            .setValue(mMessagesMap!![mMessageIds.get(i)]!!.message_type)

                                    mFirebaseConfigChats.child(mPrivateChat!!.chat_dialog_id)
                                            .child("last_message_time").child(mCurrentUser!!.user_id)
                                            .setValue(mMessagesMap!![mMessageIds.get(i)]!!.firebase_message_time)
                                    break
                                }
                            }
                        }
                    }
                    hitDeleteMessageApi(msgId)
                    db!!.deleteSingleMessage(msgId)
                    mMessagesMap!!.remove(msgId)
                    mMessageIds.remove(msgId)
                    limit = mMessagesMap!!.size
                    val deletetime1 = mPrivateChat!!.delete_dialog_time.get(mCurrentUser!!.user_id)
                    mMessagesMap = db!!.getAllMessages(mPrivateChat!!.chat_dialog_id, mCurrentUser!!.user_id, limit, deletetime1!!, mOtherUserId)
                    makeHeaders()
                } else {
                    showInternetAlert(txtName)
                }
                selectedPosition = 0
                llDefaultActionbar.visibility = View.VISIBLE
                llOptionActionbar.visibility = View.GONE
                mConversationAdapter!!.remove_selection()
            }
            imgCopy -> {
                val clipboard = getSystemService(AppCompatActivity.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("simple text", mMessagesMap!![msgId]!!.message)
                clipboard.primaryClip = clip
                selectedPosition = 0
                llDefaultActionbar.visibility = View.VISIBLE
                llOptionActionbar.visibility = View.GONE
                mConversationAdapter!!.remove_selection()
                Toast.makeText(this, getString(R.string.text_copied), Toast.LENGTH_SHORT).show()
            }

            imgSendMessage -> {
                // Send Text Message
                if (connectedToInternet()) {
                    sendTextMessage()
                } else {
                    hideKeyboard(mConversationActivity)
                    showInternetAlert(txtName)
                }
            }
        }
    }

    override fun onBackPressed() {
        if (llOptionActionbar.visibility == View.VISIBLE) {
            selectedPosition = 0
            llOptionActionbar.visibility = View.GONE
            llDefaultActionbar.visibility = View.VISIBLE
            mConversationAdapter!!.remove_selection()
        } else {
            moveBack()
        }
    }

    private fun hitDeleteMessageApi(msgId: String) {
        showProgress()
        val call = RetrofitClient.getInstance().chat_delete_messages(
                mUtils.getString(InterConst.ACCESS_TOKEN, ""),
                msgId)
        call.enqueue(object : Callback<BaseSuccessModel> {
            override fun onResponse(call: Call<BaseSuccessModel>, response: Response<BaseSuccessModel>) {
                hideProgress()
                if (response.body().response != null) {

                } else {
                    toast(response.body().error.message)
                    if (response.body().error.code == InterConst.INVALID_ACCESS) {
                        moveToSplash()
                    }
                }
            }

            override fun onFailure(call: Call<BaseSuccessModel>, t: Throwable) {
                hideProgress()
            }
        })

    }

    private fun hitClearConverastionApi(delete_time: String) {
        val call = RetrofitClient.getInstance().clear_converation(
                mUtils.getString(InterConst.ACCESS_TOKEN, ""),
                mCurrentUser!!.chat_dialog_ids.toString(),
                delete_time,
                InterConst.CLEAR_CHAT)
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

    private fun moveBack() {
        finish()
    }


    private fun clearConversation() {
        db!!.clearConversation(mPrivateChat!!.chat_dialog_id)
        mFirebaseConfigChats.child(mPrivateChat!!.chat_dialog_id).child("last_message_data").child(mUtils!!.getInt(FirebaseChatConstants.user_id, -1).toString()).setValue(FirebaseChatConstants.DEFAULT_MESSAGE_REGEX)
        mFirebaseConfigChats.child(mPrivateChat!!.chat_dialog_id).child("unread_count").child(mUtils!!.getInt(FirebaseChatConstants.user_id, -1).toString()).setValue(0)

        mFirebaseConfigChats.child(mPrivateChat!!.chat_dialog_id).child("delete_dialog_time").child(mUtils!!.getInt(FirebaseChatConstants.user_id, -1).toString()).setValue(ServerValue.TIMESTAMP).addOnSuccessListener {

            hideProgress()
            mMessageIds.clear()
            mMessagesMap!!.clear()
            limit = 20
            val time = FirebaseChatConstants.getLocalTime(Calendar.getInstance().timeInMillis)
            mMessagesMap = db!!.getAllMessages(mPrivateChat!!.chat_dialog_id, mCurrentUser!!.user_id, limit, time, mOtherUserId)
            makeHeaders()
            mConversationAdapter!!.notifyDataSetChanged()

        }

        mFirebaseConfigChats.child(mPrivateChat!!.chat_dialog_id).child("delete_dialog_time").addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot != null) {
                            var delete_dialog_time: HashMap<String, Long>? = null
                            val gtDelete = object : GenericTypeIndicator<HashMap<String, Long>>() {
                            }
                            delete_dialog_time = dataSnapshot.getValue(gtDelete)
                            hitClearConverastionApi(delete_dialog_time!!.get(mUtils!!.getInt(FirebaseChatConstants.user_id, -1).toString()).toString())
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.e("delete_dialog_time", databaseError.toString())
                    }
                })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (usersQuery != null) {
            usersQuery!!.removeEventListener(mOpponentUserListener)
        }
        FirebaseListeners.setChatDialogListenerForChat(null, "")
        FirebaseListeners.setMessageListener(null, "")
    }

    fun sendTextMessage() {
        if (edMessage.text.toString().trim().length > 0) {
            if (mPrivateChat != null) {
                // 0 for send message, 1 for send lacal, 2 for unblock alert
                if (mPrivateChat!!.block_status.get(mCurrentUser!!.user_id).equals("1")) {
                    status = 2
                    ShowUnblockPrompt()
                } else if (mPrivateChat!!.block_status.get(mOtherUserId).equals("1")) {
                    status = 1
                } else {
                    status = 0
                }
                if (llDefaultActionbar.visibility != View.VISIBLE) {
                    selectedPosition = 0
                    llDefaultActionbar.visibility = View.VISIBLE
                    llOptionActionbar.visibility = View.GONE
                    mConversationAdapter!!.remove_selection()
                }

                if (status != 2) {
                    val mMessage = MessagesModel()
                    val messagePush = mFirebaseConfigMessages.push()
                    mMessage.message_id = messagePush.key
                    mMessage.message = edMessage.text.toString().trim()
                    mMessage.message_type = FirebaseChatConstants.TYPE_TEXT
                    mMessage.message_time = "" + (Calendar.getInstance()).timeInMillis
                    mMessage.firebase_message_time = (Calendar.getInstance()).timeInMillis
                    mMessage.chat_dialog_id = mPrivateChat!!.chat_dialog_id
                    mMessage.sender_id = mCurrentUser!!.user_id
                    mMessage.message_status = FirebaseChatConstants.STATUS_MESSAGE_SENT
                    mMessage.attachment_url = ""
                    mMessage.receiver_id = mOtherUserId

                    val delete = HashMap<String, String>()
                    delete.put(mCurrentUser!!.user_id, "0")
                    if (status == 1) {
                        delete.put(mOtherUserId, "1")
                    } else {
                        delete.put(mOtherUserId, "0")
                    }
                    mMessage.message_deleted = delete

                    val favourite = HashMap<String, String>()
                    favourite.put(mCurrentUser!!.user_id, "0")
                    favourite.put(mOtherUserId, "0")
                    mMessage.favourite_message = favourite

                    val msgHashMap = HashMap<String, Any>()
                    msgHashMap.put("message_id", mMessage.message_id)
                    msgHashMap.put("message", mMessage.message)
                    msgHashMap.put("message_type", mMessage.message_type)
                    msgHashMap.put("firebase_message_time", ServerValue.TIMESTAMP)
                    msgHashMap.put("chat_dialog_id", mMessage.chat_dialog_id)
                    msgHashMap.put("sender_id", mMessage.sender_id)
                    msgHashMap.put("message_status", mMessage.message_status)
                    msgHashMap.put("attachment_url", mMessage.attachment_url)
                    msgHashMap.put("message_deleted", mMessage.message_deleted)
                    msgHashMap.put("favourite_message", mMessage.favourite_message)
                    msgHashMap.put("receiver_id", mMessage.receiver_id)
                    msgHashMap.put("sender_name", mUtils!!.getString(InterConst.NAME, ""))

                    mFirebaseConfigChats.child(mParticpantIDS).child("delete_outer_dialog")
                            .child(myUserId).setValue("0")
                    mFirebaseConfigChats.child(mParticpantIDS).child("delete_outer_dialog")
                            .child(mOtherUserId).setValue("0")

//                    if (edMessage.text.toString().trim().length > FirebaseChatConstants.NOTIFICATION_TEXT_LENGTH) {
//                        val dialog1 = AlertDialog.Builder(this)
//                        dialog1.setMessage(getString(R.string.character_limit_message) + " " + FirebaseChatConstants.NOTIFICATION_TEXT_LENGTH + " " + getString(R.string.character_sent)).create()
//                        dialog1.setPositiveButton("yes") { dialog, which ->
//                            // TODO Auto-generated method stub
//                            mMessage.message = mMessage.message.substring(0, FirebaseChatConstants.NOTIFICATION_TEXT_LENGTH)
//                            db!!.addMessage(mMessage, mCurrentUser!!.user_id)
//                            addMessage(mMessage)
//
//                            val time = FirebaseChatConstants.getLocalTime((Calendar.getInstance()).timeInMillis)
//                        msgHashMap.put("message", mMessage.message)
//                            msgHashMap.put("message_time", "" + time)
//
//                            mFirebaseConfigMessages.child(mPrivateChat!!.chat_dialog_id).child(mMessage.message_id).setValue(msgHashMap)
//                            edMessage.setText("")
//                            dialog.dismiss()
//                        }
//                        dialog1.setNegativeButton("no", null)
//                        dialog1.show()
//                    }
                    db!!.addMessage(mMessage, mCurrentUser!!.user_id)
                    addMessage(mMessage)

                    val time = FirebaseChatConstants.getLocalTime((Calendar.getInstance()).timeInMillis)
                    msgHashMap.put("message_time", "" + time)

                    mFirebaseConfigMessages.child(mPrivateChat!!.chat_dialog_id).child(mMessage.message_id).setValue(msgHashMap)
                    edMessage.setText("")
//                    }
                    mFirebaseConfigNotifications.child(mMessage.message_id).setValue(msgHashMap)

                    if (status == 0) {

                        FirebaseDatabase.getInstance().reference.child(FirebaseChatConstants.USERS).child("id_$mOtherUserId").child("badge_count")
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        if (dataSnapshot != null) {
                                            var batch = 0
                                            if (dataSnapshot.getValue(String::class.java) != null) {
                                                Log.e("singleevent", dataSnapshot.getValue(String::class.java))
                                                batch = dataSnapshot.getValue(String::class.java)!!.toInt()
                                            }
                                            batch = batch!!.toInt() + 1
                                            FirebaseDatabase.getInstance().reference.child(FirebaseChatConstants.USERS)
                                                    .child("id_$mOtherUserId")
                                                    .child("badge_count")
                                                    .setValue(batch.toString())
                                        }
                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {
                                        Log.e("singleevent", databaseError.toString())
                                    }
                                })

                    }
                }
            }
        }
    }

    fun sendFirstMessageFromAdmin() {
        if (connectedToInternet()) {
            if (mPrivateChat != null) {
                // 0 for send message, 1 for send lacal, 2 for unblock alert
                if (mPrivateChat!!.block_status.get(mCurrentUser!!.user_id).equals("1")) {
                    status = 2
                    ShowUnblockPrompt()
                } else if (mPrivateChat!!.block_status.get(mOtherUserId).equals("1")) {
                    status = 1
                } else {
                    status = 0
                }
                if (llDefaultActionbar.visibility != View.VISIBLE) {
                    selectedPosition = 0
                    llDefaultActionbar.visibility = View.VISIBLE
                    llOptionActionbar.visibility = View.GONE
                    mConversationAdapter!!.remove_selection()
                }
                if (status != 2) {
                    val mMessage = MessagesModel()
                    val messagePush = mFirebaseConfigMessages.push()
                    mMessage.message_id = messagePush.key
                    mMessage.message = InterConst.APP_ADMIN_DEFAULT_MESSAGE
                    mMessage.message_type = FirebaseChatConstants.TYPE_TEXT
                    mMessage.message_time = "" + (Calendar.getInstance()).timeInMillis
                    mMessage.firebase_message_time = (Calendar.getInstance()).timeInMillis
                    mMessage.chat_dialog_id = mPrivateChat!!.chat_dialog_id
                    mMessage.sender_id = mOtherUserId
                    mMessage.message_status = FirebaseChatConstants.STATUS_MESSAGE_SENT
                    mMessage.attachment_url = ""
                    mMessage.receiver_id = mCurrentUser!!.user_id

                    val delete = HashMap<String, String>()
                    delete.put(mOtherUserId, "0")
                    if (status == 1) {
                        delete.put(mCurrentUser!!.user_id, "1")
                    } else {
                        delete.put(mCurrentUser!!.user_id, "0")
                    }
                    mMessage.message_deleted = delete

                    val favourite = HashMap<String, String>()
                    favourite.put(mCurrentUser!!.user_id, "0")
                    favourite.put(mOtherUserId, "0")
                    mMessage.favourite_message = favourite

                    val msgHashMap = HashMap<String, Any>()
                    msgHashMap.put("message_id", mMessage.message_id)
                    msgHashMap.put("message", mMessage.message)
                    msgHashMap.put("message_type", mMessage.message_type)
                    msgHashMap.put("firebase_message_time", ServerValue.TIMESTAMP)
                    msgHashMap.put("chat_dialog_id", mMessage.chat_dialog_id)
                    msgHashMap.put("sender_id", mMessage.sender_id)
                    msgHashMap.put("message_status", mMessage.message_status)
                    msgHashMap.put("attachment_url", mMessage.attachment_url)
                    msgHashMap.put("message_deleted", mMessage.message_deleted)
                    msgHashMap.put("favourite_message", mMessage.favourite_message)
                    msgHashMap.put("receiver_id", mMessage.receiver_id)
                    msgHashMap.put("sender_name", InterConst.APP_ADMIN_NAME)

                    /*   mFirebaseConfigChats.child(mParticpantIDS)
                               .child("delete_outer_dialog")
                               .child(myUserId).setValue("0")

                       mFirebaseConfigChats.child(mParticpantIDS)
                               .child("delete_outer_dialog")
                               .child(mOtherUserId).setValue("0")*/


                    db!!.addMessage(mMessage, mOtherUserId)
                    addMessage(mMessage)

                    val time = FirebaseChatConstants.getLocalTime((Calendar.getInstance()).timeInMillis)
                    msgHashMap.put("message_time", "" + time)

                    mFirebaseConfigMessages.child(mPrivateChat!!.chat_dialog_id).child(mMessage.message_id).setValue(msgHashMap)
                    edMessage.setText("")
                    mFirebaseConfigNotifications.child(mMessage.message_id).setValue(msgHashMap)

//                        }
                    if (status == 0) {
                        FirebaseDatabase.getInstance().reference
                                .child(FirebaseChatConstants.USERS).child("id_$mOtherUserId")
                                .child("badge_count")
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        if (dataSnapshot != null) {
                                            var batch = 0
                                            if (dataSnapshot.getValue(String::class.java) != null) {
                                                Log.e("singleevent", dataSnapshot.getValue(String::class.java))
                                                batch = dataSnapshot.getValue(String::class.java)!!.toInt()
                                            }
                                            batch = batch!!.toInt() + 1
                                            FirebaseDatabase.getInstance().reference.child(FirebaseChatConstants.USERS)
                                                    .child("id_$mOtherUserId")
                                                    .child("badge_count")
                                                    .setValue(batch.toString())
                                        }
                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {
                                        Log.e("singleevent", databaseError.toString())
                                    }
                                })

                    }


                }
            }
        } else {
            hideKeyboard(mConversationActivity)
            showInternetAlert(txtName)
        }
    }


    internal fun ShowUnblockPrompt() {
        val dialog1 = AlertDialog.Builder(this)
        dialog1.setMessage(getString(R.string.unblock_message)).create()
        dialog1.setPositiveButton(getString(R.string.unblock)) { dialog, which ->
            showProgress()
            // TODO Auto-generated method stub
            mFirebaseConfigChats.child(mPrivateChat!!.chat_dialog_id).child("block_status").child(mCurrentUser!!.user_id).setValue("0").addOnSuccessListener {
                mPrivateChat!!.block_status.put(mCurrentUser!!.user_id, "0")
                hideProgress()
                dialog.dismiss()
            }
        }
        dialog1.setNegativeButton("cancel", null)
        dialog1.show()
    }

    override fun onMessageAdd(message: MessagesModel?) {
        if (message != null) {
            if (mPrivateChat!!.chat_dialog_id.equals(message.chat_dialog_id)) {

                if (mPrivateChat!!.last_message_id != message.message_id) {
                    var lastMessageTime: Long
                    var newMessageTime: Long
                    try {
                        newMessageTime = message.firebase_message_time
                    } catch (e: Exception) {
                        newMessageTime = Calendar.getInstance().timeInMillis
                    }

                    try {
                        lastMessageTime = mPrivateChat!!.last_message_time.get(mCurrentUser!!.user_id)!!
                    } catch (e: Exception) {
                        lastMessageTime = newMessageTime - 1
                    }

                    if (lastMessageTime < newMessageTime && message.sender_id.equals(mCurrentUser!!.user_id)) {
                        // last message in chat dialog is less than current message
                        val mChatUpdate = HashMap<String, Any>()
                        mChatUpdate.put("last_message", message.message)

                        val lastTime = HashMap<String, Long>()
                        lastTime.put(mCurrentUser!!.user_id, message.firebase_message_time)
                        lastTime.put(mOtherUserId, message.firebase_message_time)

                        mChatUpdate.put("last_message_time", lastTime)
                        mChatUpdate.put("last_message_sender_id", message.sender_id)
                        mChatUpdate.put("last_message_id", message.message_id)

                        if (mPrivateChat!!.block_status.get(mOpponentUser!!.user_id).equals("0")) {
                            val lastMessage = HashMap<String, String>()
                            lastMessage.put(mCurrentUser!!.user_id, message.message)
                            lastMessage.put(mOtherUserId, message.message)
                            mChatUpdate.put("last_message_data", lastMessage)
                        } else {
                            mFirebaseConfigChats
                                    .child(mPrivateChat!!.chat_dialog_id)
                                    .child("last_message_data")
                                    .child(myUserId)
                                    .setValue(message.message)
                        }

                        val lastMessageType = HashMap<String, String>()
                        lastMessageType.put(mCurrentUser!!.user_id, message.message_type)
                        lastMessageType.put(mOtherUserId, message.message_type)
                        mChatUpdate.put("last_message_type", lastMessageType)

                        val opentID = mOpponentUser!!.user_id
                        val totalMessageCount = mPrivateChat!!.message_rating_count.get(mCurrentUser!!.user_id)!! +
                                mPrivateChat!!.message_rating_count.get(mOpponentUser!!.user_id)!!

                        if (totalMessageCount < 30) {
                            var ownMessageCount = mPrivateChat!!.message_rating_count.get(mCurrentUser!!.user_id)!!
                            ownMessageCount++

                            mFirebaseConfigChats.child(mPrivateChat!!.chat_dialog_id).child("message_rating_count")
                                    .child(mCurrentUser!!.user_id).setValue(ownMessageCount)
                        }

                        val unread = mPrivateChat!!.unread_count
                        if (unread != null && unread.containsKey(mOtherUserId)) {
                            var lastUnreadCount: Int = unread.get(mOtherUserId)!!
                            if (mPrivateChat!!.block_status.get(mOtherUserId).equals("0")) {
                                lastUnreadCount++
                            }
                            unread.put(mOtherUserId, lastUnreadCount)
                            unread.put(mCurrentUser!!.user_id, 0)
                            mChatUpdate.put("unread_count", unread)
                        }
                        mFirebaseConfigChats.child(mPrivateChat!!.chat_dialog_id)
                                .updateChildren(mChatUpdate)
                    }

                    if (mPrivateChat!!.chat_dialog_id.equals(message.chat_dialog_id)) {
                        if (mUtils!!.getInt("Background", 0) == 1) {
                            // Add message in list
                            setReadMessages.add(message)
                        } else {
                            if (!message.sender_id.equals(mCurrentUser!!.user_id)) {
                                // message has been received by me
                                if (message.message_status != FirebaseChatConstants.STATUS_MESSAGE_SEEN) {
                                    mFirebaseConfigMessages.child(mPrivateChat!!.chat_dialog_id).child(message.message_id).child("message_status").setValue(FirebaseChatConstants.STATUS_MESSAGE_SEEN)
                                    val msgHashMap = HashMap<String, Any>()
                                    msgHashMap.put("message_id", message.message_id)
                                    msgHashMap.put("message_status", FirebaseChatConstants.STATUS_MESSAGE_SEEN)
                                    mFirebaseConfigReadStatus.child(message.message_id).setValue(msgHashMap)
                                }
                            }
                        }
                    }

                    if (lastMessageTime < newMessageTime && message.sender_id.equals(mOtherUserId)) {
                        mFirebaseConfigChats.child(mPrivateChat!!.chat_dialog_id).child("unread_count").child(mCurrentUser!!.user_id).setValue(0)
                    }

//        if (!mMessageIds.contains(message!!.message_id)) {
//            if (mp != null) {
//                mp!!.stop()
//                mp!!.release()
//                mp = null
//            }
//            mp = MediaPlayer.create(applicationContext, R.raw.message_received)
//            mp!!.start()
//        }
                    createHeader(message)
                }

                if (mMessageIds.contains(message.message_id)) { // already
                    mMessagesMap!!.put(message.message_id, message)
                }
                mConversationAdapter!!.animationStatus(false)
                mConversationAdapter!!.notifyDataSetChanged()
                if (message.sender_id != mCurrentUser!!.user_id) {
                    if (message.message_status != FirebaseChatConstants.STATUS_MESSAGE_SEEN) {
                        mFirebaseConfigMessages.child(message.chat_dialog_id).child(message.message_id).child("message_status").setValue(FirebaseChatConstants.STATUS_MESSAGE_SEEN)
                        val msgHashMap = HashMap<String, Any>()
                        msgHashMap.put("message_id", message.message_id)
                        msgHashMap.put("message_status", FirebaseChatConstants.STATUS_MESSAGE_SEEN)
                        mFirebaseConfigReadStatus.child(message.message_id).setValue(msgHashMap)
                    }
                }

            }
        }

    }

    override fun onMessageChanged(message: MessagesModel?) {
        if (message != null) {
            if (mPrivateChat!!.chat_dialog_id.equals(message.chat_dialog_id)) {
                if (mMessageIds.contains(message.message_id)) { // already
                    mMessagesMap!!.put(message.message_id, message)
                }
                mConversationAdapter!!.animationStatus(false)
                mConversationAdapter!!.notifyDataSetChanged()
                if (message.sender_id != mCurrentUser!!.user_id) {
                    if (message.message_status != FirebaseChatConstants.STATUS_MESSAGE_SEEN) {
                        mFirebaseConfigMessages.child(message.chat_dialog_id).child(message.message_id).child("message_status").setValue(FirebaseChatConstants.STATUS_MESSAGE_SEEN)
                        val msgHashMap = HashMap<String, Any>()
                        msgHashMap.put("message_id", message.message_id)
                        msgHashMap.put("message_status", FirebaseChatConstants.STATUS_MESSAGE_SEEN)
                        mFirebaseConfigReadStatus.child(message.message_id).setValue(msgHashMap)
                    }
                }
            }
        }
    }

    internal fun createHeader(message: MessagesModel) {
        var lastMessageDate = ""
        var newMessageDate = ""
        val calNew = Calendar.getInstance()
        try {
            val timeinMillis1 = java.lang.Long.parseLong(message.message_time)
            calNew.timeInMillis = timeinMillis1
        } catch (e: Exception) {
            e.printStackTrace()
            calNew.timeInMillis = calNew.timeInMillis
        }
        newMessageDate = chat_date_format.format(calNew.time)

        if (mMessageIds.contains(message.message_id)) { // already
            mMessagesMap!!.put(message.message_id, message)
        } else {
            if (mMessageIds.size > 0) {
                val calDb = Calendar.getInstance()
                val timeinMillis: Long
                try {
                    timeinMillis = java.lang.Long.parseLong(mMessagesMap!![mMessageIds[mMessageIds.size - 1]]!!.message_time)
                    calDb.timeInMillis = timeinMillis
                } catch (e: Exception) {
                    e.printStackTrace()
                    calDb.timeInMillis = calDb.timeInMillis
                }

                lastMessageDate = chat_date_format.format(calDb.time)
            }
            if (!mMessageIds.contains(message.message_id)) {
                var state = 0
                if (message.sender_id.equals(mCurrentUser!!.user_id)) {
                    var newMessageTime: Long
                    try {
                        newMessageTime = message.message_time.toLong()
                    } catch (e: Exception) {
                        newMessageTime = Calendar.getInstance().timeInMillis
                    }

                    if (clearTime > newMessageTime) {
                        state = 1
                    }
                }

                if (state == 0) {
                    val cal = Calendar.getInstance()
                    val today = chat_date_format.format(cal.time)

                    val cal1 = Calendar.getInstance()
                    cal1.add(Calendar.DATE, -1)
                    val yesterday = chat_date_format.format(cal1.time)

                    if (!TextUtils.equals(lastMessageDate, newMessageDate)) {
                        val mMessage = MessagesModel()
                        mMessage.is_header = true
                        if (newMessageDate == today) {
                            mMessage.show_header_text = "Today"
                            mMessage.show_message_datetime = today_date_format.format(calNew.time)
                        } else if (newMessageDate == yesterday) {
                            mMessage.show_header_text = "Yesterday"
                            mMessage.show_message_datetime = today_date_format.format(calNew.time)
                        } else {
                            mMessage.show_header_text = show_dateheader_format.format(calNew.time)
                            mMessage.show_message_datetime = today_date_format.format(calNew.time)
                        }
                        mMessagesMap!!.put(newMessageDate, mMessage)
                        mMessageIds.add(newMessageDate)
                    }
                    mMessagesMap!!.put(message.message_id, message)
                    mMessageIds.add(message.message_id)
                }
            }
        }
        mConversationAdapter!!.animationStatus(true)
        mConversationAdapter!!.notifyDataSetChanged()
    }

    internal fun addMessage(mMessage: MessagesModel) {
        var lastMessageDate = ""
        var newMessageDate = ""
        val calNew = Calendar.getInstance()
        try {
            val timeinMillis1 = java.lang.Long.parseLong(mMessage.message_time)
            calNew.timeInMillis = timeinMillis1
        } catch (e: Exception) {
            e.printStackTrace()
            calNew.timeInMillis = calNew.timeInMillis
        }

        newMessageDate = chat_date_format.format(calNew.time)
        if (mMessageIds.size > 0) {
            val calDb = Calendar.getInstance()
            val timeinMillis: Long
            try {
                timeinMillis = java.lang.Long.parseLong(mMessagesMap!![mMessageIds[mMessageIds.size - 1]]!!.message_time)
                calDb.timeInMillis = timeinMillis
            } catch (e: Exception) {
                e.printStackTrace()
                calDb.timeInMillis = calDb.timeInMillis
            }
            lastMessageDate = chat_date_format.format(calDb.time)
        }

        val cal = Calendar.getInstance()
        val today = chat_date_format.format(cal.time)

        val cal1 = Calendar.getInstance()
        cal1.add(Calendar.DATE, -1)
        val yesterday = chat_date_format.format(cal1.time)

        if (!TextUtils.equals(lastMessageDate, newMessageDate)) {
            val mMessageLocal = MessagesModel()
            mMessageLocal.is_header = true
            if (newMessageDate == today) {
                mMessageLocal.show_header_text = "Today"
                mMessageLocal.show_message_datetime = today_date_format.format(calNew.time)
            } else if (newMessageDate == yesterday) {
                mMessageLocal.show_header_text = "Yesterday"
                mMessageLocal.show_message_datetime = today_date_format.format(calNew.time)
            } else {
                mMessageLocal.show_header_text = show_dateheader_format.format(calNew.time)
                mMessageLocal.show_message_datetime = today_date_format.format(calNew.time)
            }
            mMessagesMap!!.put(newMessageDate, mMessageLocal)
            mMessageIds.add(newMessageDate)
        }
        mMessage.is_header = false
        mMessage.show_message_datetime = today_date_format.format(calNew.time)

        mMessageIds.add(mMessage.message_id)
        mMessagesMap!!.put(mMessage.message_id, mMessage)
        mConversationAdapter!!.animationStatus(true)
        mConversationAdapter!!.notifyDataSetChanged()

        lvChatList.setSelection(lvChatList.count - 1)
        lvChatList.smoothScrollToPosition(lvChatList.count - 1)


    }

    private fun optionDialog() {

        val mDialog = Dialog(mContext)
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mDialog.setContentView(R.layout.dialog_chat_clear)
        val wmlp = mDialog.window!!.attributes
        wmlp.gravity = Gravity.TOP or Gravity.RIGHT
        mDialog.setCanceledOnTouchOutside(true)

        val phone_params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        phone_params.setMargins(mWidth / 32, mWidth / 32, mWidth / 32, mWidth / 32)

        val main_lay = mDialog.findViewById(R.id.ll_main) as LinearLayout
        main_lay.layoutParams = phone_params

        val llInner = mDialog.findViewById(R.id.ll_inner) as LinearLayout
        llInner.setPadding(mWidth / 32, mWidth / 32, mWidth / 32, mWidth / 32)

        val txtBlock = mDialog.findViewById(R.id.txt_block) as TextView

        val txtReport = mDialog.findViewById(R.id.txt_report) as TextView

        val txtClear = mDialog.findViewById(R.id.txt_clear) as TextView

        val v_line1 = mDialog.findViewById(R.id.v_line1) as View
        val v_line2 = mDialog.findViewById(R.id.v_line2) as View


        if (mOtherUserId.equals(InterConst.APP_ADMIN_ID)) {
            txtBlock.visibility = View.GONE
            txtReport.visibility = View.GONE
            v_line1.visibility = View.GONE
            v_line2.visibility = View.GONE
        }


        if (mPrivateChat!!.block_status.get(mCurrentUser!!.user_id).equals("0")) {
            txtBlock.text = "block"
        } else {
            txtBlock.text = "unblock"
        }

        txtClear.setOnClickListener {
            if (connectedToInternet(txtBlock)) {
                clearConversationDialog()
                mDialog.dismiss()
            }
        }
        txtBlock.setOnClickListener {
            if (connectedToInternet(txtBlock)) {
                blockDialog(mDialog)
                mDialog.dismiss()
            }
        }
        txtReport.setOnClickListener {
            if (connectedToInternet(txtBlock)) {
                showReportDialog()
                mDialog.dismiss()
            }
        }

        mDialog.show()
    }

    internal fun showReportDialog() {
        val dialog1 = android.support.v7.app.AlertDialog.Builder(this)
        dialog1.setMessage("Are you sure you want to report this User?").create()
        dialog1.setPositiveButton(getString(R.string.yes)) { dialog, which ->
            // TODO Auto-generated method stub
            hitReportApi()
            dialog.dismiss()
        }
        dialog1.setNegativeButton(
                resources.getString(R.string.no), null)
        dialog1.show()


    }

    fun hitReportApi() {
        showProgress()
        val call = RetrofitClient.getInstance().chatReport(
                mUtils.getString(InterConst.ACCESS_TOKEN, ""),
                mOtherUserId)
        call.enqueue(object : Callback<BaseSuccessModel> {
            override fun onResponse(call: Call<BaseSuccessModel>, response: Response<BaseSuccessModel>) {
                hideProgress()
                if (response.body().response != null) {

                } else {
                    toast(response.body().error.message)
                    if (response.body().error.code == InterConst.INVALID_ACCESS) {
                        moveToSplash()
                    }
                }
            }

            override fun onFailure(call: Call<BaseSuccessModel>, t: Throwable) {
                hideProgress()
            }
        })

    }


    private fun clearConversationDialog() {
        val dialog1 = android.support.v7.app.AlertDialog.Builder(this)
        dialog1.setMessage(getString(R.string.sure_to_clear_chat)).create()
        dialog1.setPositiveButton(getString(R.string.clear)) { dialog, which ->
            // TODO Auto-generated method stub
            showProgress()
            clearConversation()
            clearTime = Calendar.getInstance().timeInMillis
            dialog.dismiss()
        }
        dialog1.setNegativeButton(
                resources.getString(R.string.cancel), null)
        dialog1.show()
    }

    private fun blockDialog(mDialog: Dialog) {
        var status = mPrivateChat!!.block_status.get(mCurrentUser!!.user_id)
        if (status.equals("0")) {
            status = "1"
            val dialog1 = android.support.v7.app.AlertDialog.Builder(this)
            dialog1.setMessage(getString(R.string.sure_to_block)).create()
            dialog1.setPositiveButton(getString(R.string.block)) { dialog, which ->
                showProgress()
                // TODO Auto-generated method stub
                mFirebaseConfigChats.child(mPrivateChat!!.chat_dialog_id).child("block_status").child(mCurrentUser!!.user_id).setValue(status).addOnSuccessListener {
                    mPrivateChat!!.block_status.put(mCurrentUser!!.user_id, status)
                    hideProgress()
                    dialog.dismiss()
                    setOnlineFlag(mCurrentUser!!)

                }
            }
            dialog1.setNegativeButton(resources.getString(R.string.cancel), null)
            dialog1.show()
        } else {
            status = "0"
            val dialog1 = android.support.v7.app.AlertDialog.Builder(this)
            dialog1.setMessage(getString(R.string.sure_to_unblock)).create()
            dialog1.setPositiveButton(getString(R.string.unblock)) { dialog, which ->
                showProgress()
                // TODO Auto-generated method stub
                mFirebaseConfigChats.child(mPrivateChat!!.chat_dialog_id).child("block_status").child(mCurrentUser!!.user_id).setValue(status).addOnSuccessListener {
                    mPrivateChat!!.block_status.put(mCurrentUser!!.user_id, status)
                    hideProgress()
                    dialog.dismiss()
                    setOnlineFlag(mCurrentUser!!)

                }
            }

            dialog1.setNegativeButton(
                    resources.getString(R.string.cancel), null)
            dialog1.show()
        }
        mDialog.dismiss()
    }


}