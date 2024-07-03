package io.tomneh.canvengine.components.physics;

import io.tomneh.canvengine.components.logic.Updating;
import io.tomneh.canvengine.util.SceneManager;
import io.tomneh.canvengine.util.math.Float2D;

public interface MvBodyRigidness extends Movable, Updating, BodyRigidness{

    // Getters

    /** @return mass of the rigid body entity
     * <br><br>
     * Value getter that can be implemented in many ways.
     * */
    float mass();

    /** @return Temporary force which is valid only until the end of this frame.
     * <br><br>
     * Uncategorized getter that can be implemented in many ways.
     * */
    Float2D tempForce();

    /** @return reference to physical velocity value
     * <br><br>
     * Warning: This should never be implemented as a value getter or value calculator.
     *  It must be a reference getter that can be used to modify entity's speed.
     *  You can just return reference to field where you store velocity for your entity.
     * */
    Float2D accessPhysicSpeed();


    // Updaters

    /** Equivalent to <code>BodyRigidness.update(this, sceneManager);</code>
     * <br><br>
     * This override of {@link Updating#update(SceneManager)} will make every object that extends
     *  {@link BodyRigidness} work out of the box as a rigid body.
     * However, if you override this method (e.g. you want to add some update logic to your entity),
     *  you should add <code>BodyRigidness.update(this, sceneManager);</code>
     *  to your override of {@link Updating#update(SceneManager)} or some equivalent instructions.
     * */
    @Override
    default void update(SceneManager sceneManager){
        this.physicBasedUpdate(sceneManager);
    }

    /** Performs a physic based update.
     * <br><br>
     * <h3> How it approximately works? </h3> <ol>
         * <li>Gets data using value getters.</li>
         * <li>Transforms it (physic calculations).</li>
         * <li>Uses setters and reference getters to save the output of calculations.</li>
     * </ol>
     * <br><br>
     * <h3> Example implementation. </h3> <ol>
         * <li>Gets force and mass.</li>
         * <li>Calculates a change in speed (positive or negative).</li>
         * <li>Adds it to the velocity object referenced by {@link #accessPhysicSpeed()}</li>
     * </ol>
     * */
    default void physicBasedUpdate(SceneManager ctx){
        this.forceBasedUpdate(ctx);
        this.speedBasedUpdate(ctx);
    }

    /** Performs a {@link #tempForce()} based update. */
    default void forceBasedUpdate(SceneManager ctx) {
        // 1. Bindings
        var tempForce= this.tempForce();
        var velocity = this.accessPhysicSpeed();

        // 2. Calculations based on formula `a = F/m`.
        // “Acc“ is a velocity change during this frame.
        float xAcc= tempForce.x / this.mass() * ctx.getTDeltaInSec();
        float yAcc= tempForce.y / this.mass() * ctx.getTDeltaInSec();

        // 3. Increment velocity object by “Acc”s.
        velocity.x += xAcc;
        velocity.y += yAcc;
    }

    /** Zerofies the this.{@link #tempForce()} object to mark it as consumed.
     *  <br><br>
     *  So that contained value is not used again by accident.
     *  This makes sense only if this.{@link #tempForce()} is a reference getter.
     *  */
    default void zerofyTempForce(){
        var tempForce= this.tempForce();
        tempForce.x= 0; tempForce.y= 0;
    }

    /** Performs a {@link #accessPhysicSpeed()} based update. */
    default void speedBasedUpdate(SceneManager ctx){
        // Bindings
        var velocity= this.accessPhysicSpeed();
        var physicCords= this.physicCords();

        // Calculations
        physicCords.x +=  velocity.x * ctx.getTDeltaInSec();
        physicCords.y +=  velocity.y * ctx.getTDeltaInSec();
    }
}
