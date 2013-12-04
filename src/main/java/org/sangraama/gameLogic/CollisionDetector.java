package org.sangraama.gameLogic;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

/**
 * This class detects collision of the game world. After detecting collisions send the collisions to
 * CollisionManager to apply game logic on collisions.
 * 
 * @author Dileepa Rajaguru
 * 
 */

public class CollisionDetector implements ContactListener {

    CollisionManager sanCollisionManager;

    /**
     * Identify the collisions when two bodies in the world start to collide.Then pass that
     * collision to the CollisionManager.
     */
    public CollisionDetector() {
        sanCollisionManager = CollisionManager.INSTANCE;
    }

    @Override
    public void beginContact(Contact contact) {
        sanCollisionManager.addToCollisionList(contact);
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

}
