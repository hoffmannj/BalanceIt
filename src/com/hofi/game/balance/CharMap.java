package com.hofi.game.balance;

import org.jbox2d.common.Vec2;

public class CharMap {

	private final String characters = "0123456789!\"£$%&*()+-=[]{};'#:@~,./<>?abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private int iIndex;
	private CharDataList chars;

	private Sprite map;
	private Vec2 tl, br;

	public CharMap() {
		iIndex = characters.indexOf('I');

		chars = new CharDataList();
		chars = Common.assetManager.readJSonData(CharDataList.class, "charmap");

		tl = new Vec2();
		br = new Vec2();

		map = new Sprite("charmap");
		map.setVisible(false);
	}

	public void draw(char c, float x, float y) {
		draw(c, x, y, 1, 1, 1, 1);
	}

	public void draw(char c, float x, float y, float r, float g, float b,
			float a) {
		draw(c, 20, x, y, r, g, b, a);
	}

	public void draw(char c, float size, float x, float y, float r, float g,
			float b, float a) {
		float m = size / 20.0f;
		int idx = characters.indexOf(c);
		CharData cd = chars.get(idx);
		tl.x = cd.topLeft.x;
		tl.y = cd.topLeft.y;
		br.x = cd.bottomRight.x;
		br.y = cd.bottomRight.y;
		map.setTexCoords(tl, br);
		map.setPosition(x, y);
		map.setScale(cd.width * m, cd.height * m);
		map.setColor(r, g, b, a);
		map.setVisible(true);
		map.draw();
		map.setVisible(false);
	}

	public void drawString(String s, float x, float y) {
		drawString(s, x, y, 1, 1, 1, 1);
	}

	public void drawString(String s, float x, float y, float r, float g,
			float b, float a) {
		drawString(s, 20, x, y, r, g, b, a);
	}

	public void drawString(String s, float size, float x, float y, float r,
			float g, float b, float a) {
		float m = size / 20.0f;
		float xx = x;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			int idx = characters.indexOf(c);
			if (c == ' ' || idx < 0) {
				xx += chars.get(iIndex).width * m * 0.9f;
				continue;
			} else {
				CharData cd = chars.get(idx);
				draw(c, size, xx, y, r, g, b, a);
				xx += cd.width * m * 0.9f;
			}
		}
	}

	public float measureString(String s) {
		return measureString(s, 20);
	}

	public float measureString(String s, float size) {
		float m = size / 20.0f;
		float xx = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == ' ') {
				xx += chars.get(iIndex).width * m * 0.9f;
				continue;
			} else {
				int idx = characters.indexOf(c);
				CharData cd = chars.get(idx);
				xx += cd.width * m * 0.9f;
			}
		}
		return xx;
	}
}
