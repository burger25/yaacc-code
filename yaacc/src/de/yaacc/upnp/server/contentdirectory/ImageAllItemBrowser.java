/*
 *
 * Copyright (C) 2014 www.yaacc.de 
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package de.yaacc.upnp.server.contentdirectory;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.Res;
import org.fourthline.cling.support.model.DIDLObject.Property.UPNP;
import org.fourthline.cling.support.model.container.Container;
import org.fourthline.cling.support.model.container.PhotoAlbum;
import org.fourthline.cling.support.model.item.Item;
import org.fourthline.cling.support.model.item.Photo;
import org.seamless.util.MimeType;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;
import de.yaacc.upnp.server.YaaccUpnpServerService;
/**
 * Browser  for an  image item.
 * 
 * 
 * @author openbit (Tobias Schoene)
 * 
 */
public class ImageAllItemBrowser extends ContentBrowser {

    public ImageAllItemBrowser(Context context) {
        super(context);
    }

    @Override
	public DIDLObject browseMeta(YaaccContentDirectory contentDirectory,
			String myId) {
		Item result = null;
		String[] projection = { MediaStore.Images.Media._ID,
				MediaStore.Images.Media.DISPLAY_NAME,
				MediaStore.Images.Media.MIME_TYPE, MediaStore.Images.Media.SIZE };
		String selection = MediaStore.Images.Media._ID + "= ?";
		String[] selectionArgs = new String[] { myId.substring(ContentDirectoryIDs.IMAGE_ALL_PREFIX.getId().length()) };
		Cursor mImageCursor = contentDirectory
				.getContext()
				.getContentResolver()
				.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						projection, selection, selectionArgs, null);

		if (mImageCursor != null) {
			mImageCursor.moveToFirst();
			String id = mImageCursor.getString(mImageCursor
					.getColumnIndex(MediaStore.Images.ImageColumns._ID));
			String name = mImageCursor
					.getString(mImageCursor
							.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME));
			Long size = Long.valueOf(mImageCursor.getString(mImageCursor
					.getColumnIndex(MediaStore.Images.ImageColumns.SIZE)));
			Log.d(getClass().getName(),
					"Mimetype: "
							+ mImageCursor.getString(mImageCursor
									.getColumnIndex(MediaStore.Images.ImageColumns.MIME_TYPE)));
			MimeType mimeType = MimeType
					.valueOf(mImageCursor.getString(mImageCursor
							.getColumnIndex(MediaStore.Images.ImageColumns.MIME_TYPE)));
			// file parameter only needed for media players which decide the
			// ability of playing a file by the file extension
			String uri = "http://" + contentDirectory.getIpAddress() + ":"
					+ YaaccUpnpServerService.PORT + "/?id=" + id + "&f=file." + MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType.toString());
			Res resource = new Res(mimeType, size, uri);
			result = new Photo(ContentDirectoryIDs.IMAGE_ALL_PREFIX.getId() + id,
					ContentDirectoryIDs.IMAGES_FOLDER.getId(), name, "", "",
					resource);
			URI albumArtUri = URI.create("http://"
					+ contentDirectory.getIpAddress() + ":"
					+ YaaccUpnpServerService.PORT + "/?thumb=" + id);
			result.replaceFirstProperty(new UPNP.ALBUM_ART_URI(
					albumArtUri));
			Log.d(getClass().getName(), "Image: " + id + " Name: " + name
					+ " uri: " + uri);
			mImageCursor.close();
		} else {
			Log.d(getClass().getName(), "Item " + myId + "  not found.");
		}

		return result;
	}

	@Override
	public List<Container> browseContainer(
			YaaccContentDirectory contentDirectory, String myId) {

		return new ArrayList<Container>();
	}

	@Override
	public List<Item> browseItem(YaaccContentDirectory contentDirectory,
			String myId) {
		List<Item> result = new ArrayList<Item>();
        result.add((Item)browseMeta(contentDirectory,myId));
		return result;

	}

}