package im.vector.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import fr.gouv.tchap.a.R;
import im.vector.view.ActiveWidgetsBanner;
import im.vector.view.NotificationAreaView;
import im.vector.view.VectorAutoCompleteTextView;
import im.vector.view.VectorOngoingConferenceCallView;
import im.vector.view.VectorPendingCallView;

public class VectorRoomActivity_ViewBinding extends VectorAppCompatActivity_ViewBinding {
    private VectorRoomActivity target;
    private View view7f0900c8;
    private View view7f090140;
    private View view7f09029a;
    private View view7f0902a2;
    private View view7f0902b2;
    private View view7f0902c1;
    private View view7f0902ca;
    private View view7f0902cb;
    private View view7f0902cc;

    public VectorRoomActivity_ViewBinding(VectorRoomActivity vectorRoomActivity) {
        this(vectorRoomActivity, vectorRoomActivity.getWindow().getDecorView());
    }

    public VectorRoomActivity_ViewBinding(final VectorRoomActivity vectorRoomActivity, View view) {
        super(vectorRoomActivity, view);
        this.target = vectorRoomActivity;
        View findRequiredView = Utils.findRequiredView(view, R.id.room_sending_message_layout, "field 'mSendingMessagesLayout' and method 'onSendingMessageLayoutClick'");
        vectorRoomActivity.mSendingMessagesLayout = findRequiredView;
        this.view7f0902cb = findRequiredView;
        findRequiredView.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                vectorRoomActivity.onSendingMessageLayoutClick();
            }
        });
        View findRequiredView2 = Utils.findRequiredView(view, R.id.room_send_message_icon, "field 'mSendMessageView' and method 'onSendClick'");
        vectorRoomActivity.mSendMessageView = (ImageView) Utils.castView(findRequiredView2, R.id.room_send_message_icon, "field 'mSendMessageView'", ImageView.class);
        this.view7f0902ca = findRequiredView2;
        findRequiredView2.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                vectorRoomActivity.onSendClick();
            }
        });
        View findRequiredView3 = Utils.findRequiredView(view, R.id.room_attached_files_icon, "field 'mSendAttachedFileView' and method 'onSendFileClick'");
        vectorRoomActivity.mSendAttachedFileView = (ImageView) Utils.castView(findRequiredView3, R.id.room_attached_files_icon, "field 'mSendAttachedFileView'", ImageView.class);
        this.view7f09029a = findRequiredView3;
        findRequiredView3.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                vectorRoomActivity.onSendFileClick();
            }
        });
        View findRequiredView4 = Utils.findRequiredView(view, R.id.editText_messageBox, "field 'mEditText' and method 'onEditTextClick'");
        vectorRoomActivity.mEditText = (VectorAutoCompleteTextView) Utils.castView(findRequiredView4, R.id.editText_messageBox, "field 'mEditText'", VectorAutoCompleteTextView.class);
        this.view7f0900c8 = findRequiredView4;
        findRequiredView4.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                vectorRoomActivity.onEditTextClick();
            }
        });
        vectorRoomActivity.mBottomSeparator = Utils.findRequiredView(view, R.id.bottom_separator, "field 'mBottomSeparator'");
        vectorRoomActivity.mCanNotPostTextView = Utils.findRequiredView(view, R.id.room_cannot_post_textview, "field 'mCanNotPostTextView'");
        vectorRoomActivity.mBottomLayout = Utils.findRequiredView(view, R.id.room_bottom_layout, "field 'mBottomLayout'");
        vectorRoomActivity.mE2eImageView = (ImageView) Utils.findRequiredViewAsType(view, R.id.room_encrypted_image_view, "field 'mE2eImageView'", ImageView.class);
        View findRequiredView5 = Utils.findRequiredView(view, R.id.room_start_call_image_view, "field 'mStartCallLayout' and method 'onStartCallClick'");
        vectorRoomActivity.mStartCallLayout = findRequiredView5;
        this.view7f0902cc = findRequiredView5;
        findRequiredView5.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                vectorRoomActivity.onStartCallClick();
            }
        });
        View findRequiredView6 = Utils.findRequiredView(view, R.id.room_end_call_image_view, "field 'mStopCallLayout' and method 'onStopCallClick'");
        vectorRoomActivity.mStopCallLayout = findRequiredView6;
        this.view7f0902b2 = findRequiredView6;
        findRequiredView6.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                vectorRoomActivity.onStopCallClick();
            }
        });
        vectorRoomActivity.mActionBarCustomTitle = (TextView) Utils.findRequiredViewAsType(view, R.id.room_action_bar_title, "field 'mActionBarCustomTitle'", TextView.class);
        vectorRoomActivity.mActionBarCustomTopic = (TextView) Utils.findRequiredViewAsType(view, R.id.room_action_bar_topic, "field 'mActionBarCustomTopic'", TextView.class);
        vectorRoomActivity.mActionBarRoomInfo = (TextView) Utils.findRequiredViewAsType(view, R.id.room_action_bar_room_info, "field 'mActionBarRoomInfo'", TextView.class);
        vectorRoomActivity.mNotificationsArea = (NotificationAreaView) Utils.findRequiredViewAsType(view, R.id.room_notifications_area, "field 'mNotificationsArea'", NotificationAreaView.class);
        vectorRoomActivity.closeReply = Utils.findRequiredView(view, R.id.close_reply, "field 'closeReply'");
        vectorRoomActivity.mRoomPreviewLayout = Utils.findRequiredView(view, R.id.room_preview_info_layout, "field 'mRoomPreviewLayout'");
        vectorRoomActivity.mRoomReplyArea = Utils.findRequiredView(view, R.id.room_reply_area, "field 'mRoomReplyArea'");
        vectorRoomActivity.mReplySenderName = (TextView) Utils.findRequiredViewAsType(view, R.id.reply_to_sender, "field 'mReplySenderName'", TextView.class);
        vectorRoomActivity.mReplyMessage = (TextView) Utils.findRequiredViewAsType(view, R.id.room_reply_message, "field 'mReplyMessage'", TextView.class);
        View findRequiredView7 = Utils.findRequiredView(view, R.id.room_pending_call_view, "field 'mVectorPendingCallView' and method 'onPendingCallClick'");
        vectorRoomActivity.mVectorPendingCallView = (VectorPendingCallView) Utils.castView(findRequiredView7, R.id.room_pending_call_view, "field 'mVectorPendingCallView'", VectorPendingCallView.class);
        this.view7f0902c1 = findRequiredView7;
        findRequiredView7.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                vectorRoomActivity.onPendingCallClick();
            }
        });
        vectorRoomActivity.mVectorOngoingConferenceCallView = (VectorOngoingConferenceCallView) Utils.findRequiredViewAsType(view, R.id.room_ongoing_conference_call_view, "field 'mVectorOngoingConferenceCallView'", VectorOngoingConferenceCallView.class);
        vectorRoomActivity.mActiveWidgetsBanner = (ActiveWidgetsBanner) Utils.findRequiredViewAsType(view, R.id.room_pending_widgets_view, "field 'mActiveWidgetsBanner'", ActiveWidgetsBanner.class);
        vectorRoomActivity.mBackProgressView = Utils.findRequiredView(view, R.id.loading_room_paginate_back_progress, "field 'mBackProgressView'");
        vectorRoomActivity.mForwardProgressView = Utils.findRequiredView(view, R.id.loading_room_paginate_forward_progress, "field 'mForwardProgressView'");
        vectorRoomActivity.mMainProgressView = Utils.findRequiredView(view, R.id.main_progress_layout, "field 'mMainProgressView'");
        vectorRoomActivity.invitationTextView = (TextView) Utils.findRequiredViewAsType(view, R.id.room_preview_invitation_textview, "field 'invitationTextView'", TextView.class);
        vectorRoomActivity.subInvitationTextView = (TextView) Utils.findRequiredViewAsType(view, R.id.room_preview_subinvitation_textview, "field 'subInvitationTextView'", TextView.class);
        vectorRoomActivity.mSyncInProgressView = Utils.findRequiredView(view, R.id.room_sync_in_progress, "field 'mSyncInProgressView'");
        View findRequiredView8 = Utils.findRequiredView(view, R.id.header_texts_container, "method 'onTextsContainerClick'");
        this.view7f090140 = findRequiredView8;
        findRequiredView8.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                vectorRoomActivity.onTextsContainerClick();
            }
        });
        View findRequiredView9 = Utils.findRequiredView(view, R.id.room_button_margin_right, "method 'onMarginRightClick'");
        this.view7f0902a2 = findRequiredView9;
        findRequiredView9.setOnClickListener(new DebouncingOnClickListener() {
            public void doClick(View view) {
                vectorRoomActivity.onMarginRightClick();
            }
        });
    }

    public void unbind() {
        VectorRoomActivity vectorRoomActivity = this.target;
        if (vectorRoomActivity != null) {
            this.target = null;
            vectorRoomActivity.mSendingMessagesLayout = null;
            vectorRoomActivity.mSendMessageView = null;
            vectorRoomActivity.mSendAttachedFileView = null;
            vectorRoomActivity.mEditText = null;
            vectorRoomActivity.mBottomSeparator = null;
            vectorRoomActivity.mCanNotPostTextView = null;
            vectorRoomActivity.mBottomLayout = null;
            vectorRoomActivity.mE2eImageView = null;
            vectorRoomActivity.mStartCallLayout = null;
            vectorRoomActivity.mStopCallLayout = null;
            vectorRoomActivity.mActionBarCustomTitle = null;
            vectorRoomActivity.mActionBarCustomTopic = null;
            vectorRoomActivity.mActionBarRoomInfo = null;
            vectorRoomActivity.mNotificationsArea = null;
            vectorRoomActivity.closeReply = null;
            vectorRoomActivity.mRoomPreviewLayout = null;
            vectorRoomActivity.mRoomReplyArea = null;
            vectorRoomActivity.mReplySenderName = null;
            vectorRoomActivity.mReplyMessage = null;
            vectorRoomActivity.mVectorPendingCallView = null;
            vectorRoomActivity.mVectorOngoingConferenceCallView = null;
            vectorRoomActivity.mActiveWidgetsBanner = null;
            vectorRoomActivity.mBackProgressView = null;
            vectorRoomActivity.mForwardProgressView = null;
            vectorRoomActivity.mMainProgressView = null;
            vectorRoomActivity.invitationTextView = null;
            vectorRoomActivity.subInvitationTextView = null;
            vectorRoomActivity.mSyncInProgressView = null;
            this.view7f0902cb.setOnClickListener(null);
            this.view7f0902cb = null;
            this.view7f0902ca.setOnClickListener(null);
            this.view7f0902ca = null;
            this.view7f09029a.setOnClickListener(null);
            this.view7f09029a = null;
            this.view7f0900c8.setOnClickListener(null);
            this.view7f0900c8 = null;
            this.view7f0902cc.setOnClickListener(null);
            this.view7f0902cc = null;
            this.view7f0902b2.setOnClickListener(null);
            this.view7f0902b2 = null;
            this.view7f0902c1.setOnClickListener(null);
            this.view7f0902c1 = null;
            this.view7f090140.setOnClickListener(null);
            this.view7f090140 = null;
            this.view7f0902a2.setOnClickListener(null);
            this.view7f0902a2 = null;
            super.unbind();
            return;
        }
        throw new IllegalStateException("Bindings already cleared.");
    }
}
