package net.net63.codearcade.LSD.world;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import net.net63.codearcade.LSD.components.*;
import net.net63.codearcade.LSD.managers.Assets;
import net.net63.codearcade.LSD.utils.Constants;

/**
 * A utility class that contains code to generate entities
 * in the world and engine
 *
 * Created by Basim on 10/07/15.
 */
public class WorldBuilder {

    // --------------- Constants ---------------------

    private static final float MAX_PARTICLE_SIZE = 0.09f;
    private static final float MIN_PARTICLE_SIZE = 0.05f;

    // --------------- State Vars --------------------

    private static Engine engine;
    private static World world;
    private static LevelDescriptor levelDescriptor;
    private static TiledMap currentMap;
    private static Rectangle bounds;

    /**
     * Required before using, set all the states
     * of the World Builder
     *
     * @param engine The engine for the entities
     * @param world The Box2d world
     * @param levelDescriptor A level descriptor to hold level data
     */
    public static void setup(Engine engine, World world, LevelDescriptor levelDescriptor) {
        WorldBuilder.engine = engine;
        WorldBuilder.world = world;
        WorldBuilder.levelDescriptor = levelDescriptor;

        bounds = new Rectangle();
    }

    /**
     * Create all entities corresponding to the given
     * TiledMap as walls, sensors and the player position
     *
     * @param map The TiledMap to load from
     */
    public static void loadFromMap(TiledMap map) {
        createWorld();

        WorldBuilder.currentMap = map;

        loadMeta(map.getLayers().get("meta"));
        loadSensors(map.getLayers().get("sensors"));
        loadWalls(map.getLayers().get("walls"));
        loadMovingSensors(map.getLayers().get("moving-sensors"));

        bounds.setPosition(bounds.x - Constants.BOUNDS_BUFFER_X, bounds.y - Constants.BOUNDS_BUFFER_Y);
        bounds.setSize(bounds.width + Constants.BOUNDS_BUFFER_X, bounds.height + Constants.BOUNDS_BUFFER_Y);
        levelDescriptor.setWorldBounds(bounds);
    }

    /**
     * Load the meta details from the map
     *
     * @param metaLayer The meta map layer
     */
    private static void loadMeta(MapLayer metaLayer) {

        //Load the player position
        MapObject playerPosition = metaLayer.getObjects().get("player-position");
        float[] dimensions = getDimensions(playerPosition);
        float posX = dimensions[0] + dimensions[2] / 2.0f;
        float posY = dimensions[1] + dimensions[3] / 2.0f;
        createPlayer(posX, posY);
    }

    /**
     * Load all the sensors from the given layer
     *
     * @param sensorLayer The MapLayer containing sensors
     */
    private static void loadSensors(MapLayer sensorLayer) {

        int sensorCount = levelDescriptor.getSensorCount();

        //Iterate over each object and create a new sensor
        for (MapObject sensor: sensorLayer.getObjects()) {
            float[] dimensions = getDimensions(sensor);

            if (dimensions != null) {
                createSensor(dimensions[0], dimensions[1], dimensions[2], dimensions[3]);
                sensorCount++;
            }
        }

        levelDescriptor.setSensorCount(sensorCount);
    }

    /**
     * Load all the moving sensors in the given layer
     *
     * @param movingSensorLayer The MapLayer containing the moving sensors
     */
    private static void loadMovingSensors(MapLayer movingSensorLayer) {

        int sensorCount = levelDescriptor.getSensorCount();

        //Iterate over each map object in this layer
        for (MapObject movingSensor: movingSensorLayer.getObjects()) {

            //Retrieve dimensions
            float[] dimensions = getDimensions(movingSensor);
            if (dimensions == null) continue;

            MapProperties properties = movingSensor.getProperties();
            String[] neededKeys = {"delay", "speed", "nodeX", "nodeY"};

            //Check if all the needed values are present, otherwise skip this object
            boolean valid = true;
            for (String key: neededKeys) if (!properties.containsKey(key)) valid = false;
            if (!valid) continue;

            //Retrieve the properties
            float delay = Float.parseFloat(properties.get("delay", String.class));
            float speed = Float.parseFloat(properties.get("speed", String.class));
            String[] nodeX = properties.get("nodeX", String.class).split(",");
            String[] nodeY = properties.get("nodeY", String.class).split(",");

            //Create the node array
            if (nodeX.length != nodeY.length) continue;
            Vector2[] nodes = new Vector2[nodeX.length + 1];

            //Set the initial node to the position
            nodes[0] = new Vector2(dimensions[0] + dimensions[2] / 2, dimensions[1] + dimensions[3] / 2);

            //Store the map height for use with each node
            float mapHeight = currentMap.getProperties().get("height", Integer.class);
            mapHeight *= currentMap.getProperties().get("tileheight", Integer.class);
            mapHeight *= Constants.PIXEL_TO_METRE;

            //Set each successive node as specified in nodeX and nodeY
            for (int i = 1; i < nodes.length; i++) {
                nodes[i] = new Vector2(Float.parseFloat(nodeX[i - 1]), Float.parseFloat(nodeY[i - 1]));

                //Convert to world space
                nodes[i].scl(Constants.PIXEL_TO_METRE);

                //Transform the y-coordinate from right-down to right-up which is normally done by the loader
                nodes[i].y = mapHeight - nodes[i].y - dimensions[3];

                //Make sure object is centralized at each node
                nodes[i].add(dimensions[2] / 2, dimensions[3] / 2);
            }

            //Create a new entity with all the extracted values
            sensorCount++;
            createMovingSensor(dimensions[0], dimensions[1], dimensions[2], dimensions[3],
                                nodes, delay, speed);
        }

        levelDescriptor.setSensorCount(sensorCount);
    }

    /**
     * Load all the walls from the given layer
     *
     * @param wallLayer The MapLayer containing all the walls
     */
    private static void loadWalls(MapLayer wallLayer) {

        //Iterate over each object and create a new wall
        for (MapObject wallObject: wallLayer.getObjects()) {
            float[] dimensions = getDimensions(wallObject);

            if (dimensions != null) {
                createWall(dimensions[0], dimensions[1], dimensions[2], dimensions[3]);
            }
        }

    }

    /**
     * Update the level bounds to contain the given position
     * and dimensions
     *
     * @param x The x-position
     * @param y The y-position
     * @param width The width
     * @param height The height
     */
    private static void addBoundedBody(float x, float y, float width, float height) {
        if (bounds.width == 0) bounds.set(x, y, width, height);
        else bounds.merge(x, y).merge(x + width, y + height);
    }

    /**
     * Obtain dimensions in world space with a given
     * object
     *
     * @param mapObject The object to get dimensions from
     * @return A 4-length array as [x, y, width, height]
     */
    private static float[] getDimensions(MapObject mapObject) {
        MapProperties properties = mapObject.getProperties();

        //The properties to extract
        String[] keys = { "x", "y", "width", "height" };
        float[] values = new float[keys.length];

        //Iterate over each property and convert it to world space
        for (int i = 0; i < keys.length; i++) {
            if (! properties.containsKey(keys[i])) return null;
            values[i] = Constants.PIXEL_TO_METRE * properties.get(keys[i], Float.class).floatValue();
        }

        return values;
    }

    /**
     * Create the world entity and Box2d world
     *
     * @return The world entity
     */
    public static Entity createWorld() {
        WorldComponent worldComponent = new WorldComponent();
        worldComponent.world = world;

        return createEntityFrom(worldComponent);
    }

    /**
     * Create a sensor at the specified position
     * with the given dimensions
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    public static Entity createSensor(float x, float y, float width, float height) {

        addBoundedBody(x, y, width, height);

        SensorComponent sensorComponent = new SensorComponent();
        RenderComponent renderComponent = new RenderComponent();
        BodyComponent bodyComponent = new BodyComponent();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.fixedRotation = true;
        bodyDef.position.set((width / 2) + x, (height / 2) + y);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.restitution = 0.0f;
        fixtureDef.shape = new PolygonShape();
        fixtureDef.filter.categoryBits = Constants.CategoryBits.SENSOR;

        ((PolygonShape) fixtureDef.shape).setAsBox(width / 2, height / 2);

        bodyComponent.body = world.createBody(bodyDef);
        bodyComponent.body.createFixture(fixtureDef);

        renderComponent.texture = new TextureRegion(Assets.getAsset(Assets.Images.SENSOR_TILE, Texture.class));
        renderComponent.tileWidth = renderComponent.texture.getRegionWidth();
        renderComponent.tileHeight = renderComponent.texture.getRegionHeight();
        renderComponent.tileToSize = true;

        return createEntityFrom(sensorComponent, bodyComponent, renderComponent);
    }

    public static Entity createMovingSensor(float x, float y, float width, float height, Vector2[] nodes, float delay, float speed) {

        Entity sensor = createSensor(x, y, width, height);

        NodeMovementComponent movementComponent = new NodeMovementComponent();

        movementComponent.hasStarted = false;
        movementComponent.timeRemaining = delay / 1000f;

        movementComponent.speed = speed;
        movementComponent.movingForward = true;
        movementComponent.nextNode = 1;
        movementComponent.nodes = nodes;

        movementComponent.distanceToNext = nodes[0].dst(nodes[1]);

        Body body = sensor.getComponent(BodyComponent.class).body;
        body.setType(BodyDef.BodyType.KinematicBody);
        body.setLinearVelocity(nodes[1].cpy().sub(nodes[0]).nor().scl(speed));

        sensor.add(movementComponent);

        return sensor;
    }

    /**
     * Create a wall at the specified position with
     * the given dimensions
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    public static Entity createWall(float x, float y, float width, float height) {

        addBoundedBody(x, y, width, height);

        WallComponent wallComponent = new WallComponent();
        RenderComponent renderComponent = new RenderComponent();
        BodyComponent bodyComponent = new BodyComponent();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.fixedRotation = true;
        bodyDef.position.set((width / 2) + x, (height / 2) + y);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.restitution = 0.0f;
        fixtureDef.shape = new PolygonShape();
        fixtureDef.filter.categoryBits = Constants.CategoryBits.WALL;

        ((PolygonShape) fixtureDef.shape).setAsBox(width / 2, height / 2);

        bodyComponent.body = world.createBody(bodyDef);
        bodyComponent.body.createFixture(fixtureDef);

        renderComponent.texture = new TextureRegion(Assets.getAsset(Assets.Images.WALL_TILE, Texture.class));
        renderComponent.tileWidth = renderComponent.texture.getRegionWidth();
        renderComponent.tileHeight = renderComponent.texture.getRegionHeight();
        renderComponent.tileToSize = true;

        return createEntityFrom(wallComponent, bodyComponent, renderComponent);

    }

    /**
     * Create the player component at the given position
     *
     * @param x
     * @param y
     * @return
     */
    public static Entity createPlayer(float x, float y) {

        PlayerComponent playerComponent = new PlayerComponent();
        StateComponent stateComponent = new StateComponent();
        AnimationComponent animationComponent = new AnimationComponent();
        RenderComponent renderComponent = new RenderComponent();
        BodyComponent bodyComponent = new BodyComponent();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        bodyDef.position.set(x, y);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.restitution = 0.0f;
        fixtureDef.shape = new CircleShape();
        fixtureDef.shape.setRadius(Constants.PLAYER_WIDTH / 2.0f);
        fixtureDef.filter.maskBits = Constants.MaskBits.PLAYER;
        fixtureDef.filter.categoryBits = Constants.CategoryBits.PLAYER;

        Animation still = Assets.getAnimation(Assets.Animations.PLAYER_STILL);
        Animation jumping = Assets.getAnimation(Assets.Animations.PLAYER_JUMPING);
        Animation falling = Assets.getAnimation(Assets.Animations.PLAYER_FALLING);

        still.setPlayMode(Animation.PlayMode.LOOP);
        jumping.setPlayMode(Animation.PlayMode.LOOP);
        falling.setPlayMode(Animation.PlayMode.LOOP);

        still.setFrameDuration(0.1f);
        jumping.setFrameDuration(0.1f);
        falling.setFrameDuration(0.1f);

        animationComponent.animations.put(PlayerComponent.STATE_INIT, falling);
        animationComponent.animations.put(PlayerComponent.STATE_STILL, still);
        animationComponent.animations.put(PlayerComponent.STATE_AIMING, still);
        animationComponent.animations.put(PlayerComponent.STATE_DEAD, still);
        animationComponent.animations.put(PlayerComponent.STATE_JUMPING, jumping);
        animationComponent.animations.put(PlayerComponent.STATE_FALLING, falling);

        stateComponent.set(PlayerComponent.STATE_INIT);
        renderComponent.texture = animationComponent.animations.get(stateComponent.get()).getKeyFrame(0);

        bodyComponent.body = world.createBody(bodyDef);
        bodyComponent.body.createFixture(fixtureDef);

        return createEntityFrom(playerComponent, bodyComponent, stateComponent, animationComponent, renderComponent);
    }

    public static Entity createParticleEffect(Vector2[] positions, Color[] colors, float time) {
        return createParticleEffect(positions, colors, time, 20, 20);
    }

    public static Entity createParticleEffect(Vector2[] positions, Color[] colors, float time, float vX, float vY) {

        int size = Math.min(positions.length, colors.length);

        ParticleComponent particleComponent = new ParticleComponent();
        particleComponent.particles = new Body[size];
        particleComponent.colors = new Color[size];
        particleComponent.currentTime = 0;
        particleComponent.finalTime = time;
        particleComponent.numParticles = size;

        for (int i = 0; i < size; i++) {
            Body particle = createParticle(positions[i].x, positions[i].y);

            particle.applyLinearImpulse(new Vector2(MathUtils.random(-vX, vX), MathUtils.random(-vY, vY)), particle.getWorldCenter(), true);
            particle.setAngularVelocity(MathUtils.random(-10, 10));

            particleComponent.particles[i] = particle;
            particleComponent.colors[i] = colors[i];
        }

        return createEntityFrom(particleComponent);
    }

    /**
     * Create a new particle body at the given position
     *
     * @param x
     * @param y
     * @return
     */
    private static Body createParticle(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();

        bodyDef.position.set(x, y);
        bodyDef.fixedRotation = false;
        bodyDef.gravityScale = 0.8f;
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        float size = MathUtils.random(MIN_PARTICLE_SIZE, MAX_PARTICLE_SIZE);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(size / 2, size / 2);
        fixtureDef.restitution = 0.6f;
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = Constants.CategoryBits.PARTICLE;
        fixtureDef.filter.maskBits = Constants.MaskBits.PARTICLE;

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);

        return body;
    }

    /**
     * Utility method to create and add an entity given an array of components,
     * this also adds it to the global entity Engine
     *
     * @param components The components to add
     * @return The entity containing all the components
     */
    private static Entity createEntityFrom(Component... components) {
        Entity entity = new Entity();

        for (Component component: components) {
            entity.add(component);

            if (component instanceof BodyComponent) {
                ((BodyComponent) component).body.setUserData(entity);
            }
        }

        engine.addEntity(entity);

        return entity;
    }
}
