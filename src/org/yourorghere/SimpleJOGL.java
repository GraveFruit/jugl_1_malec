package org.yourorghere;

import com.sun.opengl.util.Animator;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 * SimpleJOGL.java <BR>
 * author: Brian Paul (converted to Java by Ron Cemer and Sven Goethel)
 * <P>
 *
 * This version is equal to Brian Paul's version 1.2 1999/10/21
 */
public class SimpleJOGL implements GLEventListener {

    //statyczne pola okre�laj�ce rotacj� wok� osi X i Y
    private static float xrot = 0.0f, yrot = 0.0f;
    public static float ambientLight[] = {0.3f, 0.3f, 0.3f, 1.0f};//swiat�o otaczaj�ce
    public static float diffuseLight[] = {0.7f, 0.7f, 0.7f, 1.0f};//?wiat�o rozproszone
    public static float specular[] = {1.0f, 1.0f, 1.0f, 1.0f}; //?wiat�o odbite
    public static float lightPos[] = {-50.0f, 150.0f, 150.0f, 1.0f};//pozycja ?wiat�a
    public static float lightPos2[] = {-150.0f, -150.0f, 50.0f, 1.0f};//pozycja ?wiat�a
    static Koparka koparka;
    static BufferedImage image1 = null, image2 = null;
    static BufferedImage niebo = null, trawa = null, bok = null;
    static Texture t1 = null, t2 = null;
    static Texture tniebo = null, tbok = null, ttrawa = null;
    static float iks = 0.0f, zet = 0.0f, kat = 0.0f;

    public static void main(String[] args) {

        Frame frame = new Frame("Simple JOGL Application");
        GLCanvas canvas = new GLCanvas();
        canvas.addGLEventListener(new SimpleJOGL());
        frame.add(canvas);
        frame.setSize(640, 480);
        final Animator animator = new Animator(canvas);
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                // Run this on another thread than the AWT event queue to
                // make sure the call to Animator.stop() completes before
                // exiting
                new Thread(new Runnable() {

                    public void run() {
                        animator.stop();
                        System.exit(0);
                    }
                }).start();
            }
        });
        //Obs�uga klawiszy strza�ek
        frame.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    przesun(1.0f);
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    przesun(-1.0f);
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    kat += 1.0f;
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    kat -= 1.0f;
                }
                if (e.getKeyChar() == 'q') {
                    ambientLight = new float[]{ambientLight[0] - 0.1f, ambientLight[0] - 0.1f, ambientLight[0] - 0.1f, 1};
                }
                if (e.getKeyChar() == 'w') {
                    ambientLight = new float[]{ambientLight[0] + 0.1f, ambientLight[0] + 0.1f, ambientLight[0] + 0.1f, 1};
                }
                if (e.getKeyChar() == 'a') {
                    diffuseLight = new float[]{diffuseLight[0] - 0.1f, diffuseLight[0] - 0.1f, diffuseLight[0] - 0.1f, 1};
                }
                if (e.getKeyChar() == 's') {
                    diffuseLight = new float[]{diffuseLight[0] + 0.1f, diffuseLight[0] + 0.1f, diffuseLight[0] + 0.1f, 1};
                }
                if (e.getKeyChar() == 'z') {
                    specular = new float[]{specular[0] - 0.1f, specular[0] - 0.1f, specular[0] - 0.1f, 1};
                }
                if (e.getKeyChar() == 'x') {
                    specular = new float[]{specular[0] + 0.1f, specular[0] + 0.1f, specular[0] + 0.1f, 1};
                }
                if (e.getKeyChar() == 'n') {
                    lightPos[3] = 0;
                }
                if (e.getKeyChar() == 'm') {
                    lightPos[3] = 1;
                }
            }

            public void keyReleased(KeyEvent e) {
            }

            public void keyTyped(KeyEvent e) {
            }
        });
        // Center frame
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        animator.start();
    }

    public void init(GLAutoDrawable drawable) {
        // Use debug pipeline
        // drawable.setGL(new DebugGL(drawable.getGL()));

        //koparka = new Koparka();
        GL gl = drawable.getGL();
        System.err.println("INIT GL IS: " + gl.getClass().getName());

        // Enable VSync
        gl.setSwapInterval(1);

        //warto?ci sk�adowe o?wietlenia i koordynaty ?r�d�a ?wiat�a
        //(czwarty parametr okre?la odleg�o?� ?r�d�a:
        //0.0f-niesko�czona; 1.0f-okre?lona przez pozosta�e parametry)
        gl.glEnable(GL.GL_LIGHTING); //uaktywnienie o?wietlenia
        //ustawienie parametr�w ?r�d�a ?wiat�a nr. 0
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, ambientLight, 0); //swiat�o otaczaj�ce
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, diffuseLight, 0); //?wiat�o rozproszone
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, specular, 0); //?wiat�o odbite
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, lightPos, 0); //pozycja ?wiat�a
        gl.glEnable(GL.GL_LIGHT0); //uaktywnienie ?r�d�a ?wiat�a nr. 0

        gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, ambientLight, 0); //swiat�o otaczaj�ce
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, diffuseLight, 0); //?wiat�o rozproszone
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_SPECULAR, specular, 0); //?wiat�o odbite
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, lightPos2, 0); //pozycja ?wiat�a
        gl.glEnable(GL.GL_LIGHT1); //uaktywnienie ?r�d�a ?wiat�a nr. 0

        gl.glEnable(GL.GL_COLOR_MATERIAL); //uaktywnienie ?ledzenia kolor�w
        //kolory b�d� ustalane za pomoc� glColor
        gl.glColorMaterial(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE);
        //Ustawienie jasno?ci i odblaskowo?ci obiekt�w
        float specref[] = {1.0f, 1.0f, 1.0f, 1.0f}; //parametry odblaskowo?ci
        gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, specref, 0);

        gl.glMateriali(GL.GL_FRONT, GL.GL_SHININESS, 128);

        gl.glEnable(GL.GL_DEPTH_TEST);
        // Setup the drawing area and shading mode
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glShadeModel(GL.GL_SMOOTH); // try setting this to GL_FLAT and see what happens.

        try {
            image1 = ImageIO.read(getClass().getResourceAsStream("/pokemon.jpg"));
            image2 = ImageIO.read(getClass().getResourceAsStream("/android.jpg"));
            niebo = ImageIO.read(getClass().getResourceAsStream("/niebo.jpg"));
            trawa = ImageIO.read(getClass().getResourceAsStream("/trawa.jpg"));
            bok = ImageIO.read(getClass().getResourceAsStream("/bok.jpg"));
        } catch (Exception exc) {
            JOptionPane.showMessageDialog(null, exc.toString());
            return;
        }

        t1 = TextureIO.newTexture(image1, false);
        t2 = TextureIO.newTexture(image2, false);
        tniebo = TextureIO.newTexture(niebo, false);
        ttrawa = TextureIO.newTexture(trawa, false);
        tbok = TextureIO.newTexture(bok, false);

        gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_BLEND | GL.GL_MODULATE);
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);

    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL gl = drawable.getGL();
        GLU glu = new GLU();

        if (height <= 0) { // avoid a divide by zero error!

            height = 1;
        }
        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(90.0f, h, 0.1f, 300.0f);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();

        float ilor;
        if (width <= height) {
            ilor = height / width;
            gl.glOrtho(-10.0f, 10.0f, -10.0f * ilor, 10.0f * ilor, -10.0f, 10.0f);
        } else {
            ilor = width / height;
            gl.glOrtho(-10.0f * ilor, 10.0f * ilor, -10.0f, 10.0f, -10.0f, 10.0f);
        }

    }

    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();

        gl.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, ambientLight, 0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, diffuseLight, 0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, specular, 0);
        gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, lightPos, 0);

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        //gl.glTranslatef(0.0f, 0.0f, -6.0f); //przesuni?cie o 6 jednostek
        //gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f); //rotacja wok�? osi X
        //gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f); //rotacja wok�? osi Y

        gl.glFlush();

        Rysuj(gl, tbok, ttrawa, tniebo);

        gl.glBindTexture(GL.GL_TEXTURE_2D, t1.getTextureObject());
        gl.glBegin(GL.GL_QUADS);
//?ciana przednia
        //gl.glColor3f(0.8f, 0.0f, 0.0f);
        gl.glNormal3f(0.0f, 0.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(-1.0f, -1.0f, 1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(1.0f, -1.0f, 1.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(-1.0f, 1.0f, 1.0f);

//sciana tylniaa
        //gl.glColor3f(0.8f, 0.0f, 0.0f);
        gl.glNormal3f(0.0f, 0.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
//?ciana lewa
        //gl.glColor3f(0.8f, 0.0f, 0.0f);
        gl.glNormal3f(-1.0f, 0.0f, 0.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, -1.0f, 1.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glBindTexture(GL.GL_TEXTURE_2D, t2.getTextureObject());
        gl.glBegin(GL.GL_QUADS);
//?ciana prawa
        //gl.glColor3f(0.8f, 0.0f, 0.0f);
        gl.glNormal3f(1.0f, 0.0f, 0.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
//?ciana dolna
        //gl.glColor3f(0.8f, 0.0f, 0.0f);
        gl.glNormal3f(0.0f, -1.0f, 0.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(-1.0f, -1.0f, 1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, 1.0f);
//sciana gorna
        //gl.glColor3f(0.8f, 0.0f, 0.0f);
        gl.glNormal3f(0.0f, 1.0f, 0.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(1.0f, 1.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glEnd();

    }

    void walec(GL gl) {
//wywo�ujemy automatyczne normalizowanie normalnych
//bo operacja skalowania je zniekszta�ci
        gl.glEnable(GL.GL_NORMALIZE);
        float x, y, kat;
        gl.glBegin(GL.GL_QUAD_STRIP);

        gl.glColor3f(0.2f, 0.2f, 0.0f);
        for (kat = 0.0f; kat < (2.0f * Math.PI); kat += (Math.PI / 32.0f)) {
            x = 0.5f * (float) Math.sin(kat);
            y = 0.5f * (float) Math.cos(kat);
            gl.glNormal3f((float) Math.sin(kat), (float) Math.cos(kat), 0.0f);
            gl.glVertex3f(x, y, -1.0f);
            gl.glVertex3f(x, y, 0.0f);
        }
        gl.glEnd();
        gl.glNormal3f(0.0f, 0.0f, -1.0f);
        x = y = kat = 0.0f;
        gl.glBegin(GL.GL_TRIANGLE_FAN);

        gl.glColor3f(0.2f, 0.2f, 0.0f);
        gl.glVertex3f(0.0f, 0.0f, -1.0f); //srodek kola
        for (kat = 0.0f; kat < (2.0f * Math.PI); kat += (Math.PI / 32.0f)) {
            x = 0.5f * (float) Math.sin(kat);
            y = 0.5f * (float) Math.cos(kat);
            gl.glVertex3f(x, y, -1.0f);
        }
        gl.glEnd();
        gl.glNormal3f(0.0f, 0.0f, 1.0f);
        x = y = kat = 0.0f;
        gl.glBegin(GL.GL_TRIANGLE_FAN);

        gl.glColor3f(0.2f, 0.2f, 0.0f);
        gl.glVertex3f(0.0f, 0.0f, 0.0f); //srodek kola
        for (kat = 2.0f * (float) Math.PI; kat > 0.0f; kat -= (Math.PI / 32.0f)) {
            x = 0.5f * (float) Math.sin(kat);
            y = 0.5f * (float) Math.cos(kat);
            gl.glVertex3f(x, y, 0.0f);
        }
        gl.glEnd();
    }

    void stozek(GL gl) {
//wywo�ujemy automatyczne normalizowanie normalnych
        gl.glEnable(GL.GL_NORMALIZE);
        float x, y, kat;
        gl.glBegin(GL.GL_TRIANGLE_FAN);

        gl.glColor3f(0.0f, 0.3f, 0.0f);
        gl.glVertex3f(0.0f, 0.0f, -2.0f); //wierzcholek stozka
        for (kat = 0.0f; kat < (2.0f * Math.PI); kat += (Math.PI / 32.0f)) {
            x = (float) Math.sin(kat);
            y = (float) Math.cos(kat);
            gl.glNormal3f((float) Math.sin(kat), (float) Math.cos(kat), -2.0f);
            gl.glVertex3f(x, y, 0.0f);
        }
        gl.glEnd();
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glNormal3f(0.0f, 0.0f, 1.0f);
        gl.glColor3f(0.0f, 0.15f, 0.0f);
        gl.glVertex3f(0.0f, 0.0f, 0.0f); //srodek kola
        for (kat = 2.0f * (float) Math.PI; kat > 0.0f; kat -= (Math.PI / 32.0f)) {
            x = (float) Math.sin(kat);
            y = (float) Math.cos(kat);
            gl.glVertex3f(x, y, 0.0f);
        }
        gl.glEnd();
    }

    void drzewo(GL gl) {
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0.0f, -1.0f);
        gl.glScalef(0.4f, 0.4f, 0.4f);
        stozek(gl);
        gl.glTranslatef(0.0f, 0.0f, +1.8f);
        gl.glScalef(1.4f, 1.4f, 1.4f);
        stozek(gl);
        gl.glTranslatef(0.0f, 0.0f, +1.3f);
        gl.glScalef(1.2f, 1.2f, 1.2f);
        stozek(gl);
        gl.glTranslatef(0.0f, 0.0f, 1.0f);
        gl.glScalef(0.6f, 0.6f, 1.0f);
        walec(gl);
        gl.glPopMatrix();
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    void Rysuj(GL gl, Texture t1, Texture t2, Texture t3) {
        gl.glRotatef(kat, 0.0f, 1.0f, 0.0f);
        gl.glTranslatef(iks, 96.0f, 99.0f+zet);
//szescian
        gl.glColor3f(1.0f, 1.0f, 1.0f);
//za�adowanie tekstury wczytanej wcze�niej z pliku krajobraz.bmp
        gl.glBindTexture(GL.GL_TEXTURE_2D, t1.getTextureObject());
        gl.glBegin(GL.GL_QUADS);
//�ciana przednia
        gl.glNormal3f(0.0f, 0.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(-100.0f, 100.0f, 100.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(100.0f, 100.0f, 100.0f);
        gl.glTexCoord2f(0.0f, 0.85f);
        gl.glVertex3f(100.0f, -100.0f, 100.0f);
        gl.glTexCoord2f(1.0f, 0.85f);
        gl.glVertex3f(-100.0f, -100.0f, 100.0f);
//�ciana tylnia
        gl.glNormal3f(0.0f, 0.0f, 1.0f);
        gl.glTexCoord2f(1.0f, 0.85f);
        gl.glVertex3f(-100.0f, -100.0f, -100.0f);
        gl.glTexCoord2f(0.0f, 0.85f);
        gl.glVertex3f(100.0f, -100.0f, -100.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(100.0f, 100.0f, -100.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(-100.0f, 100.0f, -100.0f);
//�ciana lewa
        gl.glNormal3f(1.0f, 0.0f, 0.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-100.0f, 100.0f, -100.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(-100.0f, 100.0f, 100.0f);
        gl.glTexCoord2f(1.0f, 0.85f);
        gl.glVertex3f(-100.0f, -100.0f, 100.0f);
        gl.glTexCoord2f(0.0f, 0.85f);
        gl.glVertex3f(-100.0f, -100.0f, -100.0f);
//�ciana prawa
        gl.glNormal3f(-1.0f, 0.0f, 0.0f);
        gl.glTexCoord2f(0.0f, 0.85f);
        gl.glVertex3f(100.0f, -100.0f, -100.0f);
        gl.glTexCoord2f(1.0f, 0.85f);
        gl.glVertex3f(100.0f, -100.0f, 100.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(100.0f, 100.0f, 100.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(100.0f, 100.0f, -100.0f);
        gl.glEnd();

//�ciana dolna
//za�adowanie tekstury wczytanej wcze�niej z pliku niebo.bmp
        gl.glBindTexture(GL.GL_TEXTURE_2D, t2.getTextureObject());
        //ustawienia aby tekstura si� powiela�a
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
        gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
        gl.glBegin(GL.GL_QUADS);
        gl.glNormal3f(0.0f, 1.0f, 0.0f);
        //koordynaty ustawienia 16 x 16 kwadrat�w powielonej tekstury na �cianie dolnej
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(100.0f, -100.0f, 100.0f);
        gl.glTexCoord2f(0.0f, 16.0f);
        gl.glVertex3f(100.0f, -100.0f, -100.0f);
        gl.glTexCoord2f(16.0f, 16.0f);
        gl.glVertex3f(-100.0f, -100.0f, -100.0f);
        gl.glTexCoord2f(16.0f, 0.0f);
        gl.glVertex3f(-100.0f, -100.0f, 100.0f);
        gl.glEnd();

        //�ciana gorna
//za�adowanie tekstury wczytanej wcze�niej z pliku trawa.bmp
        gl.glBindTexture(GL.GL_TEXTURE_2D, t3.getTextureObject());
        gl.glBegin(GL.GL_QUADS);
        gl.glNormal3f(0.0f, -1.0f, 0.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-100.0f, 100.0f, 100.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(-100.0f, 100.0f, -100.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(100.0f, 100.0f, -100.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(100.0f, 100.0f, 100.0f);
        gl.glEnd();
    }
    
    public static void przesun(float d){
        iks-=d*Math.sin(kat*(3.14f/180.0f));
        zet+=d*Math.cos(kat*(3.14f/180.0f));
    }
}
