package com.sandrios.sandriosCamera.internal.ui.view;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sandrios.sandriosCamera.R;
import com.sandrios.sandriosCamera.internal.SandriosCamera;
import com.sandrios.sandriosCamera.internal.configuration.CameraConfiguration;
import com.sandrios.sandriosCamera.internal.ui.BaseSandriosActivity;
import com.sandrios.sandriosCamera.internal.ui.model.GalleryModel;
import com.sandrios.sandriosCamera.internal.utils.VideoRequestHandler;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static com.sandrios.sandriosCamera.internal.ui.viewpager.Utils.path;

/**
 * Created by Arpit Gandhi
 */
public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.GalleryViewHolder> {

    ArrayList<PickerTile> pickerTiles;
    ArrayList<GalleryModel> galleryModels = new ArrayList<>();
    Context context;
    OnItemClickListener onItemClickListener;
    String TAG = "Adapter file";
    VideoRequestHandler videoRequestHandler;
    Picasso picassoInstance;
    public ImageGalleryAdapter(Context context, int type, ArrayList<GalleryModel> galleryMod) {
        this.context = context;
        pickerTiles = new ArrayList<>();
        if (galleryMod != null) {
            galleryModels = galleryMod;
            for (int i = 0; i < galleryMod.size(); i++) {
                if (!TextUtils.isEmpty(galleryMod.get(i).getPath())) {// to use for gallery images and videos
                    copyFIle(galleryMod.get(i).getPath());
//            File fileFallery = new File(pathGalery);
//            pickerTiles.add(0,new PickerTile(Uri.fromFile(fileFallery)));
                }
            }
        }


        if (type == CameraConfiguration.VIDEO) {
            Cursor videoCursor = null;
            try {
                File file = new File(path);
                File[] listFile;
                if (file.exists()) {
                    listFile = file.listFiles();
                    if (listFile != null){

                        for (int i = 0; i < listFile.length; i++) {
                            pickerTiles.add(0, new PickerTile(Uri.fromFile(listFile[i])));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (videoCursor != null && !videoCursor.isClosed()) {
                    videoCursor.close();
                }
            }
        } else {
            File file = new File(path);
            File[] listFile;
            if (file.exists()) {
                listFile = file.listFiles();
                if (listFile != null) {
//                System.out.println("FilePathImage::: " + Uri.fromFile(listFile[0]));
                    for (int i = 0; i < listFile.length; i++) {
                        pickerTiles.add(0, new PickerTile(Uri.fromFile(listFile[i])));
                    }
                }
            }
        }

        for (int i = 0; i < pickerTiles.size(); i++) {

        }
    }

    public void copyFIle(String filePath) {
        String sdCard = path;

        // the file to be moved or copied
        File sourceLocation = new File(filePath);
        File targetLocation;
        if (filePath.endsWith(".mp4")) {
            targetLocation = new File(sdCard + "/" + SystemClock.currentThreadTimeMillis() + ".mp4");
        } else {

            targetLocation = new File(sdCard + "/" + SystemClock.currentThreadTimeMillis() + ".jpg");
        }
        // make sure your target location folder exists!

        // just to take note of the location sources
        Log.v(TAG, "sourceLocation: " + sourceLocation);
        Log.v(TAG, "targetLocation: " + targetLocation);

        try {
            // 1 = move the file, 2 = copy the file
            int actionChoice = 2;
            // moving the file to another directory
            if (actionChoice == 1) {
                if (sourceLocation.renameTo(targetLocation)) {
                    Log.v(TAG, "Move file successful.");
                } else {
                    Log.v(TAG, "Move file failed.");
                }
            }
            // we will copy the file
            else {
                // make sure the target file exists
                if (sourceLocation.exists()) {

                    InputStream in = new FileInputStream(sourceLocation);
                    OutputStream out = new FileOutputStream(targetLocation);

                    // Copy the bits from instream to outstream
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();
                    Log.v(TAG, "Copy file successful.");
                } else {
                    Log.v(TAG, "Copy file failed. Source file missing.");
                }
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // gallery images all
    public ImageGalleryAdapter(Context context, int video) {
        this.context = context;
        pickerTiles = new ArrayList<>();
        Cursor imageCursor = null;


        try {
            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.ImageColumns.ORIENTATION};
            final String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";
            imageCursor = context.getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
            //imageCursor = sContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
            if (imageCursor != null) {
                int count = 0;
                while (imageCursor.moveToNext()) {
                    String imageLocation = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    File imageFile = new File(imageLocation);
                    pickerTiles.add(new PickerTile(Uri.fromFile(imageFile)));
                    count++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (imageCursor != null && !imageCursor.isClosed()) {
                imageCursor.close();
            }
        }
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.image_item, null);
        return new GalleryViewHolder(view);
    }

    public ArrayList<PickerTile> getAllItems() {
        return pickerTiles;
    }

    @Override
    public void onBindViewHolder(final GalleryViewHolder holder, final int position) {

        final PickerTile pickerTile = getItem(position);

        Uri uri = pickerTile.getImageUri();

        if (uri != null) {
            int type = BaseSandriosActivity.getMimeType(context, uri.toString());

            System.out.println("TYPE OF FILE: " + type);

            if (type == SandriosCamera.MediaType.PHOTO) {
                holder.videoIndicator.setVisibility(View.GONE);
            } else {
                holder.videoIndicator.setVisibility(View.VISIBLE);
            }
            /*Glide.with(context)
                    .load(uri)
                    .thumbnail(0.1f)
                    .dontAnimate()
                    .centerCrop()
                    .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_gallery))
                    .error(ContextCompat.getDrawable(context, R.drawable.ic_error))
                    .into(holder.iv_thumbnail);*/

            String pathhhh = uri.getPath();
            videoRequestHandler = new VideoRequestHandler();
            picassoInstance = new Picasso.Builder(context.getApplicationContext())
                    .addRequestHandler(videoRequestHandler)
                    .build();
            picassoInstance.load(VideoRequestHandler.SCHEME_VIDEO + ":" + pathhhh).into(holder.iv_thumbnail);

            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        System.out.println("Fkjdfjkdfjdkfjla" + pickerTile.getImageUri());
                        ArrayList<String> media = new ArrayList<String>();
                        String video = "";
                        String videoPosition = "";
                        for (int i = 0; i < pickerTiles.size(); i++) {
                            media.add(pickerTiles.get(i).getImageUri().toString());
                            if (pickerTiles.get(i).getImageUri().toString().endsWith(".mp4")) {
                                video = pickerTiles.get(i).getImageUri().toString();
                                videoPosition = pickerTiles.get(i).getImageUri().toString();
                            }
                        }
                        Intent in = null;
                        try {
                            in = new Intent(context, Class.forName("com.app.oryxreuserside.activities.FullViewActivity"));
                            in.putExtra("display", pickerTiles.get(position).getImageUri().toString());
                            in.putStringArrayListExtra("paths", media);
                            in.putExtra("video", video);
                            in.putExtra("type", 1);
                            in.putExtra("video_position", videoPosition);
                            context.startActivity(in);
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }/*
                        Intent intent = new Intent(context, ActivityImagesViewpager.class);
                        intent.putParcelableArrayListExtra("ImagesArr", pickerTiles);
                        intent.putExtra("position", position);
                        context.startActivity(intent);*/
                        // onItemClickListener.onItemClick(holder.itemView, position);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return pickerTiles.size();
    }

    public PickerTile getItem(int position) {
        return pickerTiles.get(position);
    }

    public void setOnItemClickListener(
            OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public static class PickerTile implements Parcelable {

        public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
            public PickerTile createFromParcel(Parcel in) {
                return new PickerTile(in);
            }

            public PickerTile[] newArray(int size) {
                return new PickerTile[size];
            }
        };

        final Uri imageUri;

        PickerTile(@NonNull Uri imageUri) {
            this.imageUri = imageUri;
        }

        // Parcelling part
        public PickerTile(Parcel in) {
            Uri.Builder builder = new Uri.Builder();
            this.imageUri = builder.encodedPath(in.readString()).build();
        }

        @Nullable
        public Uri getImageUri() {
            return imageUri;
        }

        @Override
        public String toString() {
            return "ImageTile: " + imageUri;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(imageUri.toString());
        }
    }

    class GalleryViewHolder extends RecyclerView.ViewHolder {
        View videoIndicator;
        ImageView iv_thumbnail;

        GalleryViewHolder(View view) {
            super(view);
            videoIndicator = view.findViewById(R.id.video_indicator);
            iv_thumbnail = (ImageView) view.findViewById(R.id.image);
        }
    }
}