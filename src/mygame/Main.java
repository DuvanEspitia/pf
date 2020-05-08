package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import de.lessvoid.nifty.Nifty;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication implements ActionListener{
    
    

    public static void main(String[] args) {
        Main app = new Main();
        app.setShowSettings(false);
        app.start();
    }
    
    

    boolean leftClick;
    boolean Avanzar = false;
    boolean Derecha= false;
    boolean Izquierda= false;
    boolean Atras= false;
    boolean shift = false;
    Node ninja;
    float angle = 90;
    Quaternion q = new Quaternion();
    AnimChannel channel = new AnimChannel();
    AnimControl control = new AnimControl();
    ChaseCamera chaseCam;
    Vector3f walkDirection = new Vector3f();
    CharacterControl personaje;
    private float airTime=0;
    PointLight lamp;
    float vel = 0.4f;
    
    
    @Override
    public void simpleInitApp() 
    {
        NiftyJmeDisplay niftyDisplay = NiftyJmeDisplay.newNiftyJmeDisplay(
                assetManager, inputManager, audioRenderer, guiViewPort);
        /**
         * Create a new NiftyGUI object
         */
        Nifty nifty = niftyDisplay.getNifty();
        /**
         * Read your XML and initialize your custom ScreenController
         */
        //nifty.fromXml("Interface/screen.xml", "start");
        nifty.fromXml("Interface/screen.xml", "start", new MyStartScreen(nifty,this));
        // attach the Nifty display to the gui view port as a processor
        guiViewPort.addProcessor(niftyDisplay);
        // disable the fly cam
        flyCam.setDragToRotate(true); 
        //nifty.addXml("Interface/mysecondscreen.xml");
        
        
        
        /// cargar el mapa ///
        Spatial teapot = assetManager.loadModel("Scenes/mapa.j3o");
        rootNode.attachChild(teapot);
        
        //// luz del mapa ////
        AmbientLight a = new AmbientLight();
        a.setColor(ColorRGBA.White);
        rootNode.addLight(a);
        
        ///// velocidad de cámara ////
        flyCam.setMoveSpeed(300f);
        //Tipo de cam
        
        
        ////////// Crear colisiones //////////////////
        BulletAppState bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        
        
        ////////// malla y colisiones del escenario ////////////////
        CollisionShape suelo = CollisionShapeFactory.createMeshShape(teapot);
        RigidBodyControl suelos = new RigidBodyControl(suelo, 0);
        teapot.addControl(suelos);
        bulletAppState.getPhysicsSpace().add(suelos);
        
         ////// importar personaje ////////
        ninja = (Node) assetManager.loadModel("Models/ogro/Sinbad.mesh.j3o");
        rootNode.attachChild(ninja);
        ninja.setLocalTranslation(new Vector3f(20f,100f,-15f));
        CapsuleCollisionShape p = new CapsuleCollisionShape(3f,4f);
        //RigidBodyControl personaje = new  RigidBodyControl(p);
        personaje = new  CharacterControl(p,1);
        //personaje.setMass(1);
        
        ninja.addControl(personaje);
        bulletAppState.getPhysicsSpace().add(personaje);
        personaje.setGravity(new Vector3f(0f, -10f, 0f));
        //personaje.setPhysicsLocation(new Vector3f(20f,100f,-15f));
        
        
        
        setupChaseCamera();

        
        //// keys ////
        setUpKeys();
        
        ////////////////////////////// animaciones  ///////////////
        control = ninja.getControl(AnimControl.class);
        channel = control.createChannel();

        
        //////// gravedad /////////////
        bulletAppState.getPhysicsSpace().setGravity(new Vector3f(0f, -10f, 0f));
        
        /////// lámpara ////////////////
        lamp = new PointLight();
        lamp.setRadius(30f);
        lamp.setColor(ColorRGBA.Blue);
        rootNode.addLight(lamp);
    }

    @Override
    public void simpleUpdate(float tpf) {
        //actualizarpersonaje();
       cambiarMovilidadCamara();
       Vector3f camDir = cam.getDirection().clone().multLocal(vel);
       Vector3f camLeft = cam.getLeft().clone().multLocal(vel);
       camDir.y = 0;
       camLeft.y = 0;
       walkDirection.set(0, 0, 0);
       
        //float anguloRadianes = FastMath.DEG_TO_RAD * angle;
        if(shift)
        {
           vel= 1f;
        }else
        {
           vel = 0.4f;
        }
        
        if (Avanzar) 
        {
             walkDirection.addLocal(camDir);
            // ninja.setLocalRotation(q.fromAngleAxis(anguloRadianes * 3, new Vector3f(0, 1, 0)));
             
        }

        if (Derecha) 
        {
              walkDirection.addLocal(camLeft.negate());
             //ninja.setLocalRotation(q.fromAngleAxis(anguloRadianes * 2, new Vector3f(0, 1, 0)));
            

        }
        if (Izquierda) 
        {
             walkDirection.addLocal(camLeft);
           //ninja.setLocalRotation(q.fromAngleAxis(anguloRadianes * 0, new Vector3f(0, 1, 0)));
            
        }
        if (Atras) 
        {
            walkDirection.addLocal(camDir.negate());
            //ninja.setLocalRotation(q.fromAngleAxis(anguloRadianes, new Vector3f(0, 1, 0)));
            
        }
        if (!personaje.onGround()) {
            airTime = airTime + tpf;
        } else {
            airTime = 0;
        }
        if (walkDirection.length() == 0) {
            if (!"IdleTop".equals(channel.getAnimationName())) {
                channel.setAnim("IdleTop", 1f);
            }
        } else {
            personaje.setViewDirection(walkDirection);
            if (airTime > .3f) {
                if (!"JumpLoop".equals(channel.getAnimationName())) {
                    channel.setAnim("JumpLoop");
                }
            } else if (!"RunBase".equals(channel.getAnimationName())) {
                channel.setAnim("RunBase", 0.7f);
            }
        }
        
       personaje.setWalkDirection(walkDirection);
       
       //// Movimiento de lámpara /////////
       lamp.setPosition(new Vector3f(ninja.getLocalTranslation().x+0.5f,ninja.getLocalTranslation().y-0.5f,ninja.getLocalTranslation().z+1f));
       
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
      
        if (name.equals("SHIFT")) {
            shift = isPressed;
           
            System.out.println("presionado");

        } else {
            //Avanzar = false;
            
        }

        if (name.equals("W")) {
            Avanzar = isPressed;
           
            System.out.println("presionado");

        } else {
            //Avanzar = false;
            
        }

        if (name.equals("A")) {
            Izquierda = isPressed ;
            System.out.println("presionado");
            //channel.setAnim("RunTop");

        } else {
            //Izquierda = false;
            
        }

        if (name.equals("S")) {
            Atras = isPressed;
            System.out.println("presionado");
            //channel.setAnim("RunTop");
        } else {
            //Atras = false;
            
        }

        if (name.equals("D")) {
            Derecha = isPressed;
            System.out.println("presionado");
           //channel.setAnim("RunTop");

        } else {
            //Derecha = false;
            
        }
        

        
//        if(shift&&Derecha)
//        {
//             channel.setAnim("RunBase");
//        }else
//        {
//        if(Avanzar)
//        {
//             channel.setAnim("RunTop");
//        }
//        }

    }
    
    private void setUpKeys() 
    {

        inputManager.addMapping("W", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addListener(this, "W");

        inputManager.addMapping("D", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addListener(this, "D");

        inputManager.addMapping("A", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addListener(this, "A");

        inputManager.addMapping("S", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addListener(this, "S");

        inputManager.addMapping("SHIFT", new KeyTrigger(KeyInput.KEY_LSHIFT));
        inputManager.addListener(this, "SHIFT");
    }
    
        private void cambiarMovilidadCamara() {

        inputManager.deleteMapping("FLYCAM_RotateDrag");
        flyCam.setDragToRotate(true);
        inputManager.addMapping("FLYCAM_RotateDrag", new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE));
        inputManager.addListener(flyCam, "FLYCAM_RotateDrag");
        
    }
        
        private void setupChaseCamera() {
        flyCam.setEnabled(false);
        chaseCam = new ChaseCamera(cam, ninja, inputManager);
        
    }
        
        
}
