package org.sangraama.gameLogic;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

public class CollisionDetector implements ContactListener {

    CollisionManager sanCollisionManager;

    public CollisionDetector() {
        sanCollisionManager = CollisionManager.INSTANCE;
    }

    @Override
    public void beginContact(Contact contact) {
        sanCollisionManager.addToCollisionList(contact);
    }

    @Override
    public void endContact(Contact contact) {
        // TODO Auto-generated method stub

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // TODO Auto-generated method stub

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // TODO Auto-generated method stub

    }

}
