package com.hofi.game.balance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jbox2d.common.Vec2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

public class MyAssetManager {

	private ObjectMapper mapper = new ObjectMapper();

	private final static String strJSon = ".json";

	public Bitmap getImage(String assetName) {
		try {
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inScaled = false;
			return BitmapFactory.decodeStream(Common.mainActivity.getAssets()
					.open(assetName), null, options);
		} catch (IOException ex) {
			return null;
		}
	}

	public Bitmap getImage(int resourceId) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inScaled = false;
		return BitmapFactory.decodeResource(Common.mainActivity.getResources(),
				resourceId, options);
	}

	public Vec2 getImageSize(String assetName) {
		final Bitmap bitmap = getImage(assetName);
		if (bitmap == null)
			return null;
		Vec2 s = new Vec2(bitmap.getWidth(), bitmap.getHeight());
		bitmap.recycle();
		return s;
	}

	public InputStream GetAssetInputStream(String assetName) {
		try {
			return Common.mainActivity.getAssets().open(assetName);
		} catch (IOException ex) {
			return null;
		}
	}

	public <T> T readJSonData(Class<T> type, String name) {
		ObjectReader reader = mapper.reader(type);
		try {
			return reader.readValue(GetAssetInputStream(name + strJSon));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<String> readTextLines(String assetName) {
		try {
			InputStreamReader isr = new InputStreamReader(
					GetAssetInputStream(assetName));
			BufferedReader br = new BufferedReader(isr);
			ArrayList<String> re = new ArrayList<String>();
			while (br.ready()) {
				re.add(br.readLine());
			}
			return re;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
