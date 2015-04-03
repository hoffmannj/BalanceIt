package com.hofi.game.balance;

import org.jbox2d.common.Vec2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.hofi.game.balance.interfaces.InputListener;
import com.hofi.game.balance.interfaces.NewButtonHandler;

public class NewButton extends UIObject implements InputListener {

	private int textSize = 32;
	private Sprite up;
	private Sprite down;
	private Sprite current;
	private Object owner;
	private NewButtonHandler handler;

	private Paint textPaint;
	private Bitmap bitmap;
	private Canvas canvas;

	public NewButton(Object owner, Vec2 position, String text) {
		init(owner, position, text);
	}

	private void init(Object owner, Vec2 position, String text) {
		this.owner = owner;
		handler = null;

		textPaint = new Paint();
		textPaint.setTextSize(textSize);
		textPaint.setAntiAlias(true);
		textPaint.setARGB(0xff, 0xff, 0xff, 0xff);
		float w = textPaint.measureText(text);
		int bw = (int) (w + 32);
		int bh = textSize + 32;

		this.position.set(position);
		this.width = (int) (w + 32);
		this.height = textSize + 32;

		Bitmap bup = Common.assetManager.getImage("btnup.png");
		bitmap = Bitmap.createBitmap((int) (w + 32), textSize + 32,
				Bitmap.Config.ARGB_4444);
		canvas = new Canvas(bitmap);
		bitmap.eraseColor(0x00000000);
		canvas.drawBitmap(bup, new Rect(0, 0, 15, 15), new Rect(0, 0, 15, 15),
				null);
		canvas.drawBitmap(bup, new Rect(0, 31 - 15, 15, 31), new Rect(0,
				bh - 16, 15, bh - 1), null);
		canvas.drawBitmap(bup, new Rect(31 - 15, 0, 31, 15), new Rect(bw - 16,
				0, bw - 1, 15), null);
		canvas.drawBitmap(bup, new Rect(31 - 15, 31 - 15, 31, 31), new Rect(
				bw - 16, bh - 16, bw - 1, bh - 1), null);
		canvas.drawBitmap(bup, new Rect(0, 15, 15, 16), new Rect(0, 15, 15,
				bh - 16), null);
		canvas.drawBitmap(bup, new Rect(31 - 15, 15, 31, 16), new Rect(bw - 16,
				15, bw - 1, bh - 16), null);
		canvas.drawBitmap(bup, new Rect(15, 0, 16, 15), new Rect(15, 0,
				bw - 16, 15), null);
		canvas.drawBitmap(bup, new Rect(15, 31 - 15, 16, 31), new Rect(15,
				bh - 16, bw - 16, bh - 1), null);
		canvas.drawBitmap(bup, new Rect(15, 15, 16, 16), new Rect(15, 15,
				bw - 16, bh - 16), null);

		canvas.drawText(text, 16, bh / 2 + 10, textPaint);

		if (up != null)
			up.cleanUp();
		up = new Sprite(bitmap, "btnup_" + text);
		up.setPosition(position);
		canvas = null;
		bitmap.recycle();
		bup.recycle();

		Bitmap bdown = Common.assetManager.getImage("btndown.png");
		bitmap = Bitmap.createBitmap((int) (w + 32), textSize + 32,
				Bitmap.Config.ARGB_4444);
		canvas = new Canvas(bitmap);
		bitmap.eraseColor(0x00000000);
		canvas.drawBitmap(bdown, new Rect(0, 0, 15, 15),
				new Rect(0, 0, 15, 15), null);
		canvas.drawBitmap(bdown, new Rect(0, 31 - 15, 15, 31), new Rect(0,
				bh - 16, 15, bh - 1), null);
		canvas.drawBitmap(bdown, new Rect(31 - 15, 0, 31, 15), new Rect(
				bw - 16, 0, bw - 1, 15), null);
		canvas.drawBitmap(bdown, new Rect(31 - 15, 31 - 15, 31, 31), new Rect(
				bw - 16, bh - 16, bw - 1, bh - 1), null);
		canvas.drawBitmap(bdown, new Rect(0, 15, 15, 16), new Rect(0, 15, 15,
				bh - 16), null);
		canvas.drawBitmap(bdown, new Rect(31 - 15, 15, 31, 16), new Rect(
				bw - 16, 15, bw - 1, bh - 16), null);
		canvas.drawBitmap(bdown, new Rect(15, 0, 16, 15), new Rect(15, 0,
				bw - 16, 15), null);
		canvas.drawBitmap(bdown, new Rect(15, 31 - 15, 16, 31), new Rect(15,
				bh - 16, bw - 16, bh - 1), null);
		canvas.drawBitmap(bdown, new Rect(15, 15, 16, 16), new Rect(15, 15,
				bw - 16, bh - 16), null);

		textPaint.setARGB(0xff, 0x00, 0x00, 0x00);
		canvas.drawText(text, 16, bh / 2 + 10, textPaint);

		if (down != null)
			down.cleanUp();
		down = new Sprite(bitmap, "btndown_" + text);
		down.setPosition(position);

		setCurrent(up);
	}

	private void setCurrent(Sprite sprite) {
		up.setVisible(false);
		down.setVisible(false);
		current = sprite;
		current.setVisible(true);
	}

	public NewButton setHandler(NewButtonHandler buttonHandler) {
		handler = buttonHandler;
		return this;
	}

	@Override
	public void draw() {
		current.draw();
	}

	@Override
	public void cleanUp() {

	}

	@Override
	public void TouchStart() {
		setCurrent(down);
	}

	@Override
	public void TouchEnd() {
		Vec2 tp = Common.input.getPosition();
		if (this.isHit(tp.x, tp.y)) {

			if (handler != null)
				handler.handle(this, owner);

			setCurrent(up);
		}
	}

	@Override
	public void TouchMove() {
		Vec2 tp = Common.input.getPosition();
		if (this.isHit(tp.x, tp.y)) {
			setCurrent(down);
		} else {
			setCurrent(up);
		}
	}

}
