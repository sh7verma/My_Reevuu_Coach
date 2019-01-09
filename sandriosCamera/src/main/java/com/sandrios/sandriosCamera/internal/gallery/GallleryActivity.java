package com.sandrios.sandriosCamera.internal.gallery;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sandrios.sandriosCamera.R;
import com.sandrios.sandriosCamera.internal.manager.listener.OnGalleryClick;
import com.sandrios.sandriosCamera.internal.ui.model.GalleryModel;
import com.sandrios.sandriosCamera.internal.ui.view.ImageGalleryAdapter;
import com.sandrios.sandriosCamera.internal.utils.ImageFilePath;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class GallleryActivity extends AppCompatActivity implements OnGalleryClick {

	ArrayList<GalleryModel> list = new ArrayList<>();
	ArrayList<GalleryModel> selectedlist = new ArrayList<>();
	GalleryAdapter adapter;
	RecyclerView recyclerView;
	TextView selected,done;
	ImageGalleryAdapter imageGalleryAdapter;
	ArrayList<ImageGalleryAdapter.PickerTile> mdata;
	int count=0;
	private int mWidth =0;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		selected = (TextView) findViewById(R.id.txt_selected);
		done = (TextView) findViewById(R.id.txt_done);
		recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		recyclerView.setLayoutManager(new GridLayoutManager(this,3, LinearLayoutManager.VERTICAL,false));
		imageGalleryList();
		getDefaults();
		adapter = new GalleryAdapter(this,list,mWidth);
		recyclerView.setAdapter(adapter);
		done.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent =new Intent();
				intent.putParcelableArrayListExtra("galleryList",selectedlist);
				setResult(RESULT_OK,intent);
                finish();
			}
		});
	}
	protected void getDefaults() {
		DisplayMetrics display = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(display);
		mWidth = display.widthPixels;
	}
	@Override
	protected void onResume() {
		super.onResume();
		if (getIntent().hasExtra("list")){
			mdata = getIntent().getParcelableArrayListExtra("list");
		}
		for (int i = 0; i < mdata.size(); i++) {
			//check jpg images count
			if (mdata.get(i).getImageUri().getPath().contains(".jpg")) {
				count++;
			}
		}
		GalleryAdapter.setInterface(this);
	}

	@Override
	public void onclick(int pos) {
		if (list.get(pos).getCheck() == 1){
			list.get(pos).setCheck(0);
			for (int i = 0; i < selectedlist.size(); i++){
				if (selectedlist.get(i).getPosition()==pos){
					selectedlist.remove(i);
					count--;
				}
			}
		}
		else {
			if (count > 4){

				Toast.makeText(this, R.string.photo_limit,Toast.LENGTH_SHORT).show();
			}
			else {

				if (count > 4) {//add jpg image

					list.get(pos).setCheck(0);
					list.get(pos).setPosition(pos);
					selectedlist.remove(selectedlist.size() - 1);
//					Toast.makeText(this, R.string.photo_limit, Toast.LENGTH_SHORT).show();
				} else {

					list.get(pos).setCheck(1);
					list.get(pos).setPosition(pos);
					selectedlist.add(list.get(pos));
					count++;
				}
			}


		}
		selected.setText("Selected "+selectedlist.size());
		adapter.notifyDataSetChanged();

	}

	//videos
	public void galleryVideos() {

		int int_position = 0;
		Uri uri;
		Cursor cursor;
		int column_index_data, column_index_folder_name,column_id,thum;

		String absolutePathOfImage = null;
		uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

		String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME,MediaStore.Video.Media._ID,MediaStore.Video.Thumbnails.DATA};

		final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
		cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");

		column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
		column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
		column_id = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
		thum = cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);

		while (cursor.moveToNext()) {
			absolutePathOfImage = cursor.getString(column_index_data);
			String thumnail = save_video_thumbnail(absolutePathOfImage);
			Log.e("Column", absolutePathOfImage);
//			Log.e("Folder", cursor.getString(column_index_folder_name));
//			Log.e("column_id", cursor.getString(column_id));
			Log.e("thum", cursor.getString(thum));

			GalleryModel obj_model = new GalleryModel();
			obj_model.setCheck(0);
			obj_model.setPath(ImageFilePath.getPath(this, Uri.parse(thumnail)));
			list.add(obj_model);

		}

	}
	String save_video_thumbnail(String pathFile) {
		String saved_path=null;
		if (TextUtils.isEmpty(saved_path)) {
			try {
//				final Uri uri = Uri.parse(pathFile);
//				final String path = FileUtils.getPath(this, uri);
				File file = new File(pathFile);
				long length = file.length();
				if ((length / 1024) > (1024 * 110)) {//more than 100MB
//					Toast.makeText(getApplicationContext(), getResources().getString(R.string.file_size_exceeds), Toast.LENGTH_SHORT).show();
//					return;
				}
				File ff = new File(pathFile);
				Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(ff.getAbsolutePath(), MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
                saved_path= savePicture(bitmap,"ve");
			} catch (Exception e) {
				Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
			}
		}
		return saved_path;
	}
	String save_video_(String pathFile) {
		String saved_path=null;
		if (!TextUtils.isEmpty(pathFile)) {
			try {
				Bitmap pic = ThumbnailUtils.createVideoThumbnail(pathFile,
						MediaStore.Video.Thumbnails.MINI_KIND);
				 saved_path = savePicture(pic,"title");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return saved_path;
	}
	private String savePicture(Bitmap bm, String imgName)
	{
		OutputStream fOut = null;
		String pathd = null;
		String strDirectory = Environment.getExternalStorageDirectory().toString();

		File f = new File(strDirectory, imgName);
		try {
			fOut = new FileOutputStream(f);

			/**Compress image**/
			bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
			fOut.flush();
			fOut.close();

			/**Update image to gallery**/
			  pathd = MediaStore.Images.Media.insertImage(getContentResolver(),f.getAbsolutePath(), f.getName(), f.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pathd;
	}
	// gallery images all
	public void imageGalleryList() {
		list = new ArrayList<>();
		Cursor imageCursor = null;
		try {
			final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.ImageColumns.ORIENTATION};
			final String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";
			imageCursor = getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
			//imageCursor = sContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
			if (imageCursor != null) {
				int count = 0;
				while (imageCursor.moveToNext()) {
					GalleryModel galleryModel = new GalleryModel();
					String imageLocation = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
					File imageFile = new File(imageLocation);
					String path = ImageFilePath.getPath(this, Uri.fromFile(imageFile));
					galleryModel.setPath(path);
					list.add(galleryModel);
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
}
