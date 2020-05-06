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
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

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
    boolean Avanzar;
    boolean Derecha;
    boolean Izquierda;
    boolean Atras;
    Node ninja;
    float angle = 90;
    Quaternion q = new Quaternion();
    AnimChannel channel = new AnimChannel();
    AnimControl control = new AnimControl();
    ChaseCamera chaseCam;
     Vector3f walkDirection = new Vector3f();
    @Override
    public void simpleInitApp() {
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
        ninja = (Node) assetManager.loadModel("/Models/ninja/Ninja.mesh.j3o");
        rootNode.attachChild(ninja);
        ninja.setLocalTranslation(new Vector3f(20f,15f,-15f));
        CapsuleCollisionShape p = new CapsuleCollisionShape(3f,4f);
        RigidBodyControl personaje = new  RigidBodyControl(p);
        //CharacterControl personaje = new  CharacterControl(p,1);
        personaje.setMass(1);
        ninja.addControl(personaje);
        bulletAppState.getPhysicsSpace().add(personaje);
        
        ninja.setLocalScale((float) 0.1);
        
        setupChaseCamera();
        
        //////// gravedad /////////////
        bulletAppState.getPhysicsSpace().setGravity(new Vector3f(0f, -10f, 0f));
        
        //// keys ////
        setUpKeys();
        
        ////////////////////////////// animaciones  ///////////////
        control = ninja.getControl(AnimControl.class);
        channel = control.createChannel();

    }

    @Override
    public void simpleUpdate(float tpf) {
        //actualizarpersonaje();
        cambiarMovilidadCamara();
         
        float anguloRadianes = FastMath.DEG_TO_RAD * angle;


        if (Avanzar) 
        {
             ninja.setLocalRotation(q.fromAngleAxis(anguloRadianes * 3, new Vector3f(0, 1, 0)));
             
        }

        if (Derecha) 
        {
             ninja.setLocalRotation(q.fromAngleAxis(anguloRadianes * 2, new Vector3f(0, 1, 0)));
            

        }
        if (Izquierda) 
        {
           ninja.setLocalRotation(q.fromAngleAxis(anguloRadianes * 0, new Vector3f(0, 1, 0)));
            
        }
        if (Atras) 
        {
            ninja.setLocalRotation(q.fromAngleAxis(anguloRadianes, new Vector3f(0, 1, 0)));
            //angle = angle*2;
        }

    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
       Vector3f camDir = cam.getDirection().clone().multLocal(0.4f);
       Vector3f camLeft = cam.getLeft().clone().multLocal(0.4f);
       camDir.y = 0;
       camLeft.y = 0;
       walkDirection.set(0, 0, 0);


        if (name.equals("W")) {
            Avanzar = true;
            channel.setAnim("Walk");
             walkDirection.addLocal(camDir);
            System.out.println("presionado");

        } else {
            Avanzar = false;
            
        }

        if (name.equals("A")) {
            Izquierda = true;
            System.out.println("presionado");
            walkDirection.addLocal(camLeft);
            channel.setAnim("Walk");

        } else {
            Izquierda = false;
            
        }

        if (name.equals("S")) {
            Atras = true;
            System.out.println("presionado");
            walkDirection.addLocal(camDir.negate());
            channel.setAnim("Walk");
        } else {
            Atras = false;
            
        }

        if (name.equals("D")) {
            Derecha = true;
            System.out.println("presionado");
             walkDirection.addLocal(camLeft.negate());
             channel.setAnim("Walk");

        } else {
            Derecha = false;
            
        }

    }
    
    private void setUpKeys() {
        inputManager.addMapping("click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(this, "click");

        inputManager.addMapping("W", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addListener(this, "W");

        inputManager.addMapping("D", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addListener(this, "D");

        inputManager.addMapping("A", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addListener(this, "A");

        inputManager.addMapping("S", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addListener(this, "S");

    }
    
        private void cambiarMovilidadCamara() {

        inputManager.deleteMapping("FLYCAM_RotateDrag");
        flyCam.setDragToRotate(true);
        inputManager.addMapping("FLYCAM_RotateDrag", new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE));
        inputManager.addListener(flyCam, "FLYCAM_RotateDrag");
        //cont++;
    }
        
        private void setupChaseCamera() {
        flyCam.setEnabled(false);
        chaseCam = new ChaseCamera(cam, ninja, inputManager);
        
    }
        
        
}
