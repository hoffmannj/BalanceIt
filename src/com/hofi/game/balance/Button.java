package com.hofi.game.balance;

import org.jbox2d.common.Vec2;

import com.hofi.game.balance.interfaces.ButtonHandler;
import com.hofi.game.balance.interfaces.InputListener;

public class Button extends UIObject implements InputListener {

	private Sprite up;
	private Sprite down;
	private Sprite current;
	private Object owner;
	private ButtonHandler handler;

	public Button(Object owner, Vec2 position, String upTexture,
			String downTexture) {
		init(owner, position, upTexture, downTexture);
	}

	private void init(Object owner, Vec2 position, String upTexture,
			String downTexture) {
		this.owner = owner;
		handler = null;

		this.position.set(position);
		Vec2 sImage = Common.assetManager.getImageSize(upTexture + ".png");
		this.width = sImage.x;
		this.height = sImage.y;

		if (up != null)
			up.cleanUp();
		up = new Sprite(upTexture);
		up.setPosition(position);

		if (down != null)
			down.cleanUp();
		down = new Sprite(downTexture);
		down.setPosition(position);

		setCurrent(up);
	}

	private void setCurrent(Sprite sprite) {
		up.setVisible(false);
		down.setVisible(false);
		current = sprite;
		current.setVisible(true);
	}

	public Button setHandler(ButtonHandler buttonHandler) {
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
