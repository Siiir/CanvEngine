package io.tomneh.canvengine.util;

import io.tomneh.canvengine.components.logic.Updating;
import io.tomneh.canvengine.components.physics.collider.Collider;
import io.tomneh.canvengine.entity.Entity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;

/**
 * Game logic happens here.
 * <br><br>
 * `this` doesn't do image rendering.
 */
public class Scene extends Entity implements Updating {

    // Instance variables

    /** This is a flag for {@linkplain io.tomneh.canvengine.ui.awt.compos.Scene} manager,
     *   which informs it whether it should execute logic update.
     *  <br><br>
     *  When <code>{@link #paused}==true</code>,
     *   {@linkplain io.tomneh.canvengine.ui.awt.compos.Scene} manager, depending on its implementation,
     *    may choose to still perform graphical update.
     *   Manager may also choose to do other things with scene, while not updating scene.
     *    It can, for example, execute user commands, which may finally lead to unpausing scene
     *    via <code>this.{@link #paused}= false</code>.
     * */
    public boolean paused= false;

    /*
        Adding new component types to scene requires you to add
        1. new `transient protected ChosenCollection<YourComponentType> yourComponents;`
        2. entry in `initTransients`
        3. entry in `slowlyRegisterCompos`
        4. entry in `slowlyUnregisterCompos`
        *. if it is an active component type you need to find a way to process its instances.
    */

    // Passive elements

    /** Stores information about, which entity is present and a reference to it.
     * <br><br>
     * Entity references and values are primarily used for: <ol>
     *     <li>entity registration, which happens e.g. during deserialization and spawning</li>
     *     <li>entity deregistration, which happens e.g. despawning</li>
     * </ol>
     * */
    transient protected HashSet<Entity> entities; // Serialized

    /** Stores all registered entity's colliders so that they can be detected by moving entities.
     * <br>
     * <br>
     * Example situation: <br>
     * Bird entity is moving from A to B.
     * On each logic update it checks whether it hasn't bumped on other collider entity.
     * Check can be performed by checking whether bird's collider
     *  collides with any collider in <code>this.{@linkplain #colliders}</code>.
     *  <br>
     * If bird's collider collides with, for example, stone's collider,
     *  both entities will exchange some messages and choose what to do.
     * Bird can, for example, die while stone will get red/bloody.
     * */
    transient protected HashSet<Collider> colliders;

    // Regularly processed components

    /** Stores all registered {@link Updating} components so that their {@link Updating#update(SceneManager)}
     *   methods can be sequentially called on each logic update.*/
    transient protected HashSet<Updating> updatings;

    // Init methods

    /** Initializes all transient fields of <code>this</code>.
     * <br><br>
     * This method can be used by all constructors and initializers.
     * Both by parametrized-method constructors and deserializers.
     * */
    protected void initTransients() {
        // Elements
        {
            // Passive
            this.entities = new HashSet<>();
        }
        // Components
        {
            // Passively processed
            this.colliders = new HashSet<>();
            // Regularly processed
            this.updatings = new HashSet<>();
        }
    }

    /** Construct a new empty {@linkplain io.tomneh.canvengine.ui.awt.compos.Scene}. */
    public Scene(){
        super();
        this.initTransients();
    }

    /** Registration method that allows entities to choose the way they register their components. */
    public void registerEntity(Entity entity) {
        this.entities.add(entity);
        entity.registerComposOn(this);
    }

    /** Slow and strict method that registers all components that entity extends. */
    public void slowlyRegCompos(Entity entity) {
        // Passive
        if (entity instanceof Collider collider){
            this.colliders.add(collider);
        }
        // Active
        if (entity instanceof Updating updating){
            this.updatings.add(updating);
        }
    }

    /** Deregistration method that allows entities to choose the way they deregister their components. */
    public boolean unregisterEntity(Entity entity) {
        boolean entityWasPresent = this.entities.remove(entity);
        if (entityWasPresent){
            entity.unregisterComposFrom(this);
        }
        return entityWasPresent;
    }

    /** Slow and strict method that unregisters all components given entity extends. */
    public void slowlyUnregCompos(Entity entity) {
        // Passive
        if (entity instanceof Collider collider){
            this.colliders.remove(collider);
        }
        // Active
        if (entity instanceof Updating updating){
            this.updatings.remove(updating);
        }
    }

    // Processing collections

    /** Logical update that updates all <code>this.{@link #updatings}</code>
     *   unless the game is paused.
     *   */
    @Override
    public void update(SceneManager ctx) {
        if (this.paused){
            // Don't do logic update.
        }else{
            for (Updating updating : this.updatings) {
                updating.update(ctx);
            }
        }
    }


    // Serialization methods

    /** Note: This doesn't serialize components as they can be retrieved using {@link Entity#registerComposOn(Scene)}. */
    private void writeObject(ObjectOutputStream oOS) throws IOException {
        oOS.defaultWriteObject();

        // Writing entities
        {
            oOS.writeInt(this.entities.size());
            for (Entity entity :
                    this.entities) {
                oOS.writeObject(entity);
            }
        }
    }

    /** Note: This doesn't serialize components.
     *  They are retrieved using {@link Entity#registerComposOn(Scene)}. */
    private void readObject(ObjectInputStream oIS) throws IOException, ClassNotFoundException {
        oIS.defaultReadObject();

        // Reading entities
        {
            int entitiesSize= oIS.readInt();
            this.initTransients();
            for (int i = 0; i < entitiesSize; i++) {
                Entity entity= (Entity) oIS.readObject();
                this.registerEntity(entity);
            }
        }
    }
}
