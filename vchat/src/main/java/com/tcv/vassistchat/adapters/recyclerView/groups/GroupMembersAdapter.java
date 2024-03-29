/*
 * *
 *  * Created by Salman Saleem on 6/21/18 12:43 AM
 *  * Copyright (c) 2018 . All rights reserved.
 *  * Last modified 6/21/18 12:34 AM
 *
 */

package com.tcv.vassistchat.adapters.recyclerView.groups;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.tcv.vassistchat.R;
import com.tcv.vassistchat.activities.messages.MessagesActivity;
import com.tcv.vassistchat.api.APIGroups;
import com.tcv.vassistchat.api.APIService;
import com.tcv.vassistchat.app.AppConstants;
import com.tcv.vassistchat.app.EndPoints;
import com.tcv.vassistchat.app.WhatsCloneApplication;
import com.tcv.vassistchat.helpers.AppHelper;
import com.tcv.vassistchat.helpers.Files.cache.ImageLoader;
import com.tcv.vassistchat.helpers.Files.cache.MemoryCache;
import com.tcv.vassistchat.helpers.PreferenceManager;
import com.tcv.vassistchat.helpers.UtilsPhone;
import com.tcv.vassistchat.helpers.UtilsString;
import com.tcv.vassistchat.models.groups.GroupResponse;
import com.tcv.vassistchat.models.groups.MembersGroupModel;
import com.tcv.vassistchat.models.users.Pusher;
import com.tcv.vassistchat.ui.ColorGenerator;
import com.tcv.vassistchat.ui.RecyclerViewFastScroller;
import com.tcv.vassistchat.ui.TextDrawable;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import io.realm.Realm;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Salman Saleem on 20/02/2016.
 *
 */
public class GroupMembersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements RecyclerViewFastScroller.BubbleTextGetter {
    protected Activity mActivity;
    private List<MembersGroupModel> mContactsModel;
    private APIService mApiService;
    private Realm realm;
    private boolean isAdmin;

    private MemoryCache memoryCache;

    public GroupMembersAdapter(@NonNull Activity mActivity, boolean isAdmin) {
        this.mActivity = mActivity;
        this.isAdmin = isAdmin;
        this.mApiService  = new APIService(mActivity);
        this.realm = WhatsCloneApplication.getRealmDatabaseInstance();
        this.memoryCache = new MemoryCache();
        this.mContactsModel = new ArrayList<>();
    }

    public void setContacts(List<MembersGroupModel> contactsModelList) {
        this.mContactsModel = contactsModelList;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mActivity).inflate(R.layout.row_group_members, parent, false);
        return new ContactsViewHolder(itemView);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ContactsViewHolder contactsViewHolder = (ContactsViewHolder) holder;
        final MembersGroupModel membersGroupModel = this.mContactsModel.get(position);
        try {

            if (membersGroupModel.getUserId() == PreferenceManager.getID(mActivity)) {
                contactsViewHolder.itemView.setEnabled(false);
            }
            if (membersGroupModel.getUsername() != null) {
                if (membersGroupModel.getUserId() == PreferenceManager.getID(mActivity)) {
                    contactsViewHolder.setUsername(mActivity.getString(R.string.you));
                } else {
                    contactsViewHolder.setUsername(membersGroupModel.getUsername());
                }

            } else {
                try {
                    if (membersGroupModel.getUserId() == PreferenceManager.getID(mActivity)) {
                        contactsViewHolder.setUsername(mActivity.getString(R.string.you));
                    } else {
                        String name = UtilsPhone.getContactName(membersGroupModel.getPhone());
                        if (name != null) {
                            contactsViewHolder.setUsername(name);
                        } else {
                            contactsViewHolder.setUsername(membersGroupModel.getPhone());
                        }

                    }
                } catch (Exception e) {
                    AppHelper.LogCat(" " + e.getMessage());
                }

            }

            if (membersGroupModel.getStatus() != null) {
                contactsViewHolder.setStatus(membersGroupModel.getStatus());
            } else {
                contactsViewHolder.setStatus(membersGroupModel.getPhone());
            }
            if (membersGroupModel.getRole().equals("member")) {
                contactsViewHolder.hideAdmin();
            } else {
                contactsViewHolder.showAdmin();
            }

            contactsViewHolder.setUserImage(membersGroupModel.getImage(), membersGroupModel.getId(),membersGroupModel.getUsername());

        } catch (Exception e) {
            AppHelper.LogCat("Exception" + e.getMessage());
        }
        contactsViewHolder.setOnClickListener(view -> {

            if (isAdmin) {
                String TheName;
                String name = UtilsPhone.getContactName(membersGroupModel.getPhone());
                if (name != null) {
                    TheName = name;
                } else {
                    TheName = membersGroupModel.getPhone();
                }
                CharSequence options[] = new CharSequence[0];
                if (membersGroupModel.isAdmin()) {
                    options = new CharSequence[]{mActivity.getString(R.string.message_group_option) + TheName + "", mActivity.getString(R.string.view_group_option) + TheName + "", mActivity.getString(R.string.make_group_option) + " " + TheName + " " + mActivity.getString(R.string.make_member_group_option), mActivity.getString(R.string.remove_group_option) + TheName + ""};
                } else {
                    options = new CharSequence[]{mActivity.getString(R.string.message_group_option) + TheName + "", mActivity.getString(R.string.view_group_option) + TheName + "", mActivity.getString(R.string.make_group_option) + " " + TheName + " " + mActivity.getString(R.string.make_admin_group_option), mActivity.getString(R.string.remove_group_option) + TheName + ""};
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            Intent messagingIntent = new Intent(mActivity, MessagesActivity.class);
                            messagingIntent.putExtra("conversationID", 0);
                            messagingIntent.putExtra("recipientID", membersGroupModel.getUserId());
                            messagingIntent.putExtra("isGroup", false);
                            mActivity.startActivity(messagingIntent);
                            mActivity.finish();
                            break;
                        case 1:
                            contactsViewHolder.viewContact(membersGroupModel.getPhone());
                            break;
                        case 2:
                            if (membersGroupModel.isAdmin()) {
                                contactsViewHolder.RemoveMemberAsAdmin(membersGroupModel.getUserId(), membersGroupModel.getGroupID());
                            } else {
                                contactsViewHolder.MakeMemberAsAdmin(membersGroupModel.getUserId(), membersGroupModel.getGroupID());
                            }
                            break;
                        case 3:
                            AlertDialog.Builder builderDelete = new AlertDialog.Builder(mActivity);
                            builderDelete.setMessage(mActivity.getString(R.string.remove_from_group) + TheName + mActivity.getString(R.string.from_group))
                                    .setPositiveButton(mActivity.getString(R.string.ok), (dialog1, which1) -> {
                                        AppHelper.showDialog(mActivity, mActivity.getString(R.string.deleting_group));
                                        contactsViewHolder.RemoveMemberFromGroup(membersGroupModel.getUserId(), membersGroupModel.getGroupID());
                                    }).setNegativeButton(mActivity.getString(R.string.cancel), null).show();
                            break;
                    }

                });
                builder.show();

            }
            return true;
        });


    }


    @Override
    public int getItemCount() {
        if (mContactsModel != null) return mContactsModel.size();
        return 0;
    }

    @Override
    public String getTextToShowInBubble(int pos) {
        try {
            return mContactsModel.size() > pos ? Character.toString(mContactsModel.get(pos).getUsername().charAt(0)) : null;
        } catch (Exception e) {
            AppHelper.LogCat(e.getMessage());
            return e.getMessage();
        }

    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.user_image)
        ImageView userImage;
        @BindView(R.id.username)
        TextView username;
        @BindView(R.id.status)
        EmojiconTextView status;
        @BindView(R.id.admin)
        TextView admin;

        @BindView(R.id.member)
        TextView member;

        ContactsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            status.setSelected(true);
            setTypeFaces();
        }


        private void setTypeFaces() {
            if (AppConstants.ENABLE_FONTS_TYPES) {
                status.setTypeface(AppHelper.setTypeFace(mActivity, "Futura"));
                username.setTypeface(AppHelper.setTypeFace(mActivity, "Futura"));
                member.setTypeface(AppHelper.setTypeFace(mActivity, "Futura"));
                admin.setTypeface(AppHelper.setTypeFace(mActivity, "Futura"));
            }
        }

        TextDrawable textDrawable(String name) {
            if (name == null) {
                name = mActivity.getString(R.string.app_name);
            }
            ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
            // generate random color
            int color = generator.getColor(name);
            String c = String.valueOf(name.toUpperCase().charAt(0));
            return TextDrawable.builder().buildRound(c, color);


        }

        @SuppressLint("StaticFieldLeak")
        void setUserImage(String ImageUrl, int recipientId, String username) {
            TextDrawable drawable = textDrawable(username);
            new AsyncTask<Void, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(Void... params) {
                    return ImageLoader.GetCachedBitmapImage(memoryCache, ImageUrl, mActivity, recipientId, AppConstants.USER, AppConstants.ROW_PROFILE);
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    super.onPostExecute(bitmap);
                    if (bitmap != null) {
                        ImageLoader.SetBitmapImage(bitmap, userImage);
                    } else {


                        BitmapImageViewTarget target = new BitmapImageViewTarget(userImage) {
                            @Override
                            public void onResourceReady(final Bitmap bitmap, GlideAnimation anim) {
                                super.onResourceReady(bitmap, anim);
                                userImage.setImageBitmap(bitmap);

                            }

                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                super.onLoadFailed(e, errorDrawable);
                                userImage.setImageDrawable(errorDrawable);
                            }

                            @Override
                            public void onLoadStarted(Drawable placeHolderDrawable) {
                                super.onLoadStarted(placeHolderDrawable);
                                userImage.setImageDrawable(placeHolderDrawable);
                            }
                        };
                        Glide.with(mActivity.getApplicationContext())
                                .load(EndPoints.ROWS_IMAGE_URL + ImageUrl)
                                .asBitmap()
                                .centerCrop()
                                .transform(new CropCircleTransformation(mActivity.getApplicationContext()))
                                .placeholder(drawable)
                                .error(drawable)
                                .override(AppConstants.ROWS_IMAGE_SIZE, AppConstants.ROWS_IMAGE_SIZE)
                                .into(target);
                    }
                }
            }.execute();
        }

        void hideAdmin() {
            admin.setVisibility(View.GONE);
            member.setVisibility(View.VISIBLE);
        }

        void showAdmin() {
            admin.setVisibility(View.VISIBLE);
            member.setVisibility(View.GONE);
        }

        void setUsername(String Username) {
            username.setText(Username);
        }

        void setStatus(String Status) {
            String statu = UtilsString.unescapeJava(Status);
            status.setText(statu);
        }


        void setOnClickListener(View.OnLongClickListener listener) {
            itemView.setOnLongClickListener(listener);
        }

        void viewContact(String phone) {
            long ContactID = UtilsPhone.getContactID(mActivity, phone);
            try {
                if (ContactID != 0) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, ContactID));
                    mActivity.startActivity(intent);
                }
            } catch (Exception e) {
                AppHelper.LogCat("Error view contact  Exception" + e.getMessage());
            }
        }


        void MakeMemberAsAdmin(int id, int groupID) {
            APIGroups mApiGroups = mApiService.RootService(APIGroups.class, EndPoints.BACKEND_BASE_URL);
            Call<GroupResponse> CreateGroupCall = mApiGroups.makeAdmin(groupID, id);
            CreateGroupCall.enqueue(new Callback<GroupResponse>() {
                @Override
                public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().isSuccess()) {
                            AppHelper.Snackbar(mActivity, mActivity.findViewById(R.id.containerProfile), response.body().getMessage(), AppConstants.MESSAGE_COLOR_SUCCESS, AppConstants.TEXT_COLOR);
                            EventBus.getDefault().post(new Pusher(AppConstants.EVENT_BUS_CREATE_GROUP));
                            EventBus.getDefault().post(new Pusher(AppConstants.EVENT_BUS_ADD_MEMBER, groupID));
                        } else {
                            AppHelper.Snackbar(mActivity, mActivity.findViewById(R.id.containerProfile), response.body().getMessage(), AppConstants.MESSAGE_COLOR_ERROR, AppConstants.TEXT_COLOR);
                        }
                    } else {
                        AppHelper.Snackbar(mActivity, mActivity.findViewById(R.id.containerProfile), response.message(), AppConstants.MESSAGE_COLOR_ERROR, AppConstants.TEXT_COLOR);

                    }
                }

                @Override
                public void onFailure(Call<GroupResponse> call, Throwable t) {
                    AppHelper.Snackbar(mActivity, mActivity.findViewById(R.id.containerProfile), mActivity.getString(R.string.failed_to_make_member_as_admin), AppConstants.MESSAGE_COLOR_ERROR, AppConstants.TEXT_COLOR);
                }
            });


        }

        void RemoveMemberAsAdmin(int id, int groupID) {
            APIGroups mApiGroups = mApiService.RootService(APIGroups.class, EndPoints.BACKEND_BASE_URL);
            Call<GroupResponse> CreateGroupCall = mApiGroups.removeAdmin(groupID, id);
            CreateGroupCall.enqueue(new Callback<GroupResponse>() {
                @Override
                public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().isSuccess()) {
                            AppHelper.Snackbar(mActivity, mActivity.findViewById(R.id.containerProfile), response.body().getMessage(), AppConstants.MESSAGE_COLOR_SUCCESS, AppConstants.TEXT_COLOR);
                            EventBus.getDefault().post(new Pusher(AppConstants.EVENT_BUS_CREATE_GROUP));
                            EventBus.getDefault().post(new Pusher(AppConstants.EVENT_BUS_ADD_MEMBER, groupID));
                        } else {
                            AppHelper.Snackbar(mActivity, mActivity.findViewById(R.id.containerProfile), response.body().getMessage(), AppConstants.MESSAGE_COLOR_ERROR, AppConstants.TEXT_COLOR);
                        }
                    } else {
                        AppHelper.Snackbar(mActivity, mActivity.findViewById(R.id.containerProfile), response.message(), AppConstants.MESSAGE_COLOR_ERROR, AppConstants.TEXT_COLOR);

                    }
                }

                @Override
                public void onFailure(Call<GroupResponse> call, Throwable t) {
                    AppHelper.Snackbar(mActivity, mActivity.findViewById(R.id.containerProfile), mActivity.getString(R.string.failed_to_make_member_as_admin), AppConstants.MESSAGE_COLOR_ERROR, AppConstants.TEXT_COLOR);
                }
            });


        }

        void RemoveMemberFromGroup(int id, int groupID) {
            APIGroups mApiGroups = mApiService.RootService(APIGroups.class, EndPoints.BACKEND_BASE_URL);
            Call<GroupResponse> CreateGroupCall = mApiGroups.removeMember(groupID, id);
            CreateGroupCall.enqueue(new Callback<GroupResponse>() {
                @Override
                public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                    if (response.isSuccessful()) {
                        AppHelper.hideDialog();
                        if (response.body().isSuccess()) {
                            AppHelper.Snackbar(mActivity, mActivity.findViewById(R.id.containerProfile), response.body().getMessage(), AppConstants.MESSAGE_COLOR_SUCCESS, AppConstants.TEXT_COLOR);
                            realm.executeTransaction(realm1 -> {
                                MembersGroupModel membersGroupModel = realm1.where(MembersGroupModel.class).equalTo("userId", id).equalTo("groupID", groupID).findFirst();
                                membersGroupModel.deleteFromRealm();
                            });
                            EventBus.getDefault().post(new Pusher(AppConstants.EVENT_BUS_CREATE_GROUP));
                            EventBus.getDefault().post(new Pusher(AppConstants.EVENT_BUS_ADD_MEMBER, groupID));
                            AppHelper.reloadActivity(mActivity);
                        } else {
                            AppHelper.hideDialog();
                            AppHelper.Snackbar(mActivity, mActivity.findViewById(R.id.containerProfile), response.body().getMessage(), AppConstants.MESSAGE_COLOR_ERROR, AppConstants.TEXT_COLOR);
                        }
                    } else {
                        AppHelper.hideDialog();
                        AppHelper.Snackbar(mActivity, mActivity.findViewById(R.id.containerProfile), response.message(), AppConstants.MESSAGE_COLOR_ERROR, AppConstants.TEXT_COLOR);

                    }
                }

                @Override
                public void onFailure(Call<GroupResponse> call, Throwable t) {
                    AppHelper.hideDialog();
                    AppHelper.Snackbar(mActivity, mActivity.findViewById(R.id.containerProfile), mActivity.getString(R.string.failed_to_remove_member), AppConstants.MESSAGE_COLOR_ERROR, AppConstants.TEXT_COLOR);
                }
            });


        }

    }


}
