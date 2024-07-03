package io.tomneh.canvengine.entity.samples;

import io.tomneh.canvengine.components.physics.MvBodyRigidness;
import io.tomneh.canvengine.res.assets.ImageAsset;
import io.tomneh.canvengine.res.ImgProperties;
import io.tomneh.canvengine.ui.awt.cmd.DrawCommand;
import io.tomneh.canvengine.ui.awt.cmd.DrawInstruction;
import io.tomneh.canvengine.components.render.RenderLayer;
import io.tomneh.canvengine.entity.template.Mobile;
import io.tomneh.canvengine.util.SceneManager;
import io.tomneh.canvengine.util.math.Float2D;
import io.tomneh.canvengine.util.math.Long2D;
import io.tomneh.canvengine.Consts;
import io.tomneh.canvengine.components.physics.collider.BallCollider;
import io.tomneh.canvengine.components.physics.collider.Collider;
import io.tomneh.canvengine.ui.awt.compos.Drawable;

public class SlawcioBall extends Mobile implements MvBodyRigidness, Drawable {

    // Instance consts
    final ImageAsset image;
    final long physicRadius, onScreenRadius;

    // Instance variables
    protected Float2D physicSpeed;
    protected Float2D tempForce= new Float2D(0,0);

    // Constructors
    public SlawcioBall(Long2D initPhysicCords, long physicRadius) {
        this(initPhysicCords, physicRadius, Float2D.zero());
    }
    public SlawcioBall(Long2D initPhysicCords, long physicRadius,
                       Float2D initPhysicSpeed) {
        // Seed/Calc. rendering params
        super(initPhysicCords);
        this.physicRadius = physicRadius;
        this.onScreenRadius = this.physicRadius >> Consts.TO_SCREEN_METRIC_SH_R;
        // Physics
        this.physicSpeed = initPhysicSpeed;
        // Asset
        this.image= new ImageAsset( new ImgProperties(
                "dynamics/CircularSławcio",
                (int)this.onScreenRadius << 1, (int) this.onScreenRadius << 1, // radius * 2
                0
        ) );
    }

    // Accessors, getters, calculators.

    // Value getters.

    @Override
    public RenderLayer getLayer() {
        return RenderLayer.NORMAL;
    }

    @Override
    public float mass() {
        return 85;
    }

    // Reference getters

    @Override
    public Float2D accessPhysicSpeed() {
        return this.physicSpeed;
    }

    // Uncategorized getters
    @Override
    public Float2D tempForce() {
        return this.tempForce;
    }

    @Override
    public Collider collider() {
        return new BallCollider() {
            @Override
            public long physicRadius() {
                return physicRadius;
            }

            @Override
            public Long2D physicCords() {
                return physicCords;
            }
        };
    }

    // “Processing” commands.

    @Override
    public void update(SceneManager ctx){
        this.physicBasedUpdate(ctx);
        // Zerofy the “temporary force” object so that contained value is not used again by accident.
        this.zerofyTempForce();
    }

    @Override
    public DrawCommand drawCommand() {
        var cords= this.onScreenCords();
        int x = (int) (cords.x - this.onScreenRadius);
        int y = (int) (cords.y - this.onScreenRadius);
        return new DrawInstruction(this.image.getRefToCachedImg(), x, y);
    }
}
