package com.hofi.game.balance;

import java.util.ArrayList;
import java.util.Hashtable;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

import com.hofi.game.balance.interfaces.ContactHandler;

public class MainContactHandler implements ContactListener {

	private Hashtable<String, Hashtable<String, ArrayList<ContactHandler>>> handlers;

	public MainContactHandler() {
		handlers = new Hashtable<String, Hashtable<String, ArrayList<ContactHandler>>>();
	}

	public void addHandler(String objNameA, String objNameB,
			ContactHandler handler) {
		if (!handlers.containsKey(objNameA))
			handlers.put(objNameA,
					new Hashtable<String, ArrayList<ContactHandler>>());
		Hashtable<String, ArrayList<ContactHandler>> q = handlers.get(objNameA);
		if (!q.containsKey(objNameB))
			q.put(objNameB, new ArrayList<ContactHandler>());
		q.get(objNameB).add(handler);
	}

	public void removeHandler(String objNameA, String objNameB) {
		if (!handlers.containsKey(objNameA) && !handlers.containsKey(objNameB))
			return;
		if (handlers.containsKey(objNameA)) {
			Hashtable<String, ArrayList<ContactHandler>> q = handlers
					.get(objNameA);
			if (q.containsKey(objNameB)) {
				q.get(objNameB).clear();
				q.remove(objNameB);
			}
		}
		if (handlers.containsKey(objNameB)) {
			Hashtable<String, ArrayList<ContactHandler>> q = handlers
					.get(objNameB);
			if (q.containsKey(objNameA)) {
				q.get(objNameA).clear();
				q.remove(objNameA);
			}
		}
	}

	public void clearHandlers() {
		handlers.clear();
	}

	@Override
	public void beginContact(Contact arg0) {
		/*
		 * if (!arg0.getFixtureA().isSensor() && !arg0.getFixtureB().isSensor())
		 * return;
		 */
		DrawablePhysicsObject a = (DrawablePhysicsObject) ((PhysicsObject) arg0
				.getFixtureA().getBody().getUserData()).getUserData();
		DrawablePhysicsObject b = (DrawablePhysicsObject) ((PhysicsObject) arg0
				.getFixtureB().getBody().getUserData()).getUserData();
		String nA = Common.gameObjects.getObjectNameForDPO(a);
		String nB = Common.gameObjects.getObjectNameForDPO(b);
		if (!handlers.containsKey(nA) && !handlers.containsKey(nB))
			return;
		if (handlers.containsKey(nA)) {
			Hashtable<String, ArrayList<ContactHandler>> q = handlers.get(nA);
			if (q.containsKey(nB)) {
				ArrayList<ContactHandler> al = q.get(nB);
				for (ContactHandler contactHandler : al) {
					contactHandler.handleEnter(a, arg0.getFixtureA(), b,
							arg0.getFixtureB());
				}
			}
		}
		if (handlers.containsKey(nB)) {
			Hashtable<String, ArrayList<ContactHandler>> q = handlers.get(nB);
			if (q.containsKey(nA)) {
				ArrayList<ContactHandler> al = q.get(nA);
				for (ContactHandler contactHandler : al) {
					contactHandler.handleEnter(b, arg0.getFixtureB(), a,
							arg0.getFixtureA());
				}
			}
		}
	}

	@Override
	public void endContact(Contact arg0) {
		/*
		 * if (!arg0.getFixtureA().isSensor() && !arg0.getFixtureB().isSensor())
		 * return;
		 */
		DrawablePhysicsObject a = (DrawablePhysicsObject) ((PhysicsObject) arg0
				.getFixtureA().getBody().getUserData()).getUserData();
		DrawablePhysicsObject b = (DrawablePhysicsObject) ((PhysicsObject) arg0
				.getFixtureB().getBody().getUserData()).getUserData();
		String nA = Common.gameObjects.getObjectNameForDPO(a);
		String nB = Common.gameObjects.getObjectNameForDPO(b);
		if (!handlers.containsKey(nA) && !handlers.containsKey(nB))
			return;
		if (handlers.containsKey(nA)) {
			Hashtable<String, ArrayList<ContactHandler>> q = handlers.get(nA);
			if (q.containsKey(nB)) {
				ArrayList<ContactHandler> al = q.get(nB);
				for (ContactHandler contactHandler : al) {
					contactHandler.handleLeave(a, arg0.getFixtureA(), b,
							arg0.getFixtureB());
				}
			}
		}
		if (handlers.containsKey(nB)) {
			Hashtable<String, ArrayList<ContactHandler>> q = handlers.get(nB);
			if (q.containsKey(nA)) {
				ArrayList<ContactHandler> al = q.get(nA);
				for (ContactHandler contactHandler : al) {
					contactHandler.handleLeave(b, arg0.getFixtureB(), a,
							arg0.getFixtureA());
				}
			}
		}
	}

	@Override
	public void postSolve(Contact arg0, ContactImpulse arg1) {

	}

	@Override
	public void preSolve(Contact arg0, Manifold arg1) {

	}
}
