package com.hofi.game.balance;

import java.util.concurrent.ConcurrentHashMap;

import android.graphics.Bitmap;

public class Textures {
	private ConcurrentHashMap<String, Integer> textures = new ConcurrentHashMap<String, Integer>();

	public Textures() {
	}

	public void loadTexture(String textureName) {
		if (textures.containsKey(textureName))
			return;
		int ht = loadTextureBitmap(textureName + ".png");
		textures.put(textureName, ht);
	}

	public void loadTexture(Bitmap bitmap, String textureName) {
		if (textures.containsKey(textureName)) {
			System.err.println("texture already loaded");
			return;
		}
		int ht = loadBitmapTexture(bitmap);
		if (ht == 0) {
			throw new RuntimeException("Error loading texture.");
		}
		textures.put(textureName, ht);
	}

	public int getTexture(String textureName) {
		if (!textures.containsKey(textureName))
			return -1;
		return textures.get(textureName);
	}

	public void removeTexture(String textureName) {
		if (!textures.containsKey(textureName))
			return;
		int id = textures.get(textureName);
		deleteTexture(id);
		textures.remove(textureName);
	}

	public void clearTextures() {
		int id;
		String key;
		while (textures.size() > 0) {
			key = textures.keys().nextElement();
			id = textures.get(key);
			deleteTexture(id);
			textures.remove(key);
		}
		textures.clear();
	}

	private int loadBitmapTexture(Bitmap bitmap) {
		int textureHandle = GLHelper.getNewTextureHandle();

		if (textureHandle != 0) {
			GLHelper.bindBitmapToTextureHandle(bitmap, textureHandle);
		}

		return textureHandle;
	}

	private int loadTextureBitmap(final String texName) {
		Bitmap bmp = Common.assetManager.getImage(texName);
		int id = loadBitmapTexture(bmp);
		bmp.recycle();
		if (id == 0) {
			throw new RuntimeException("Error loading texture.");
		}

		return id;
	}

	private void deleteTexture(int texId) {
		GLHelper.deleteTexture(texId);
	}

}
