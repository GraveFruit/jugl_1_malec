package org.yourorghere;
import javax.media.opengl.*;



public class Koparka {
    
    public float kat1=0;
    public float kierunek1=-1;
    public float stan1=0;
    public float kat2=0;
    public float kierunek2=1;
    public float stan2=0;
    public float kat3=0;
    public float kierunek3=1;
    public float stan3=0;
    public float kat4=0;
    public float kierunek4=1;
    public float stan4=1;
    
    public void ZmienKat1(float kat1){
        this.kat1+=kat1;
        if(this.kat1>30.0f){
            this.kierunek1*=-1;
            this.kat1=30.0f;}
        if(this.kat1<-80.0f)
            this.kat1=-80.0f;
        
    }

    
    public void ZmienKat2(float kat2){
        this.kat2+=kat2;
        if(this.kat2>30.0f)
            this.kat2=30.0f;
        if(this.kat2<-100.0f)
            this.kat2=-100.0f;
    }
        
    public void ZmienKat3(float kat3){
        this.kat3+=kat3;
        if(this.kat3>30.0f)
            this.kat3=30.0f;
        if(this.kat3<-100.0f)
            this.kat3=-100.0f;
    }
    
    public void ZmienKat4(float kat4){
        this.kat4+=kat4;
        if(this.kat4>45.0f)
            this.kat4=45.0f;
        if(this.kat4<-45.0f)
            this.kat4=-45.0f;
    }
    
    public void Rysuj(GL gl)
    {
        //ciagnik
        gl.glColor3f(1.0f,1.0f,0.0f);
        Prostopadloscian(gl,-2.0f,-1.0f,-1.0f,4.0f,1.0f,2.0f);
        gl.glColor3f(0.15f,0.15f,0.15f);
        Walec(gl,0.5f,0.5f,-1.5f,-1.0f,-1.25f);
        Walec(gl,0.5f,0.5f,-1.5f,-1.0f,0.75f);
        Walec(gl,0.5f,0.5f,1.5f,-1.0f,-1.25f);
        Walec(gl,0.5f,0.5f,1.5f,-1.0f,0.75f);
        gl.glColor3f(1.0f,1.0f,0.0f);
        Prostopadloscian(gl,-0.5f,0.0f,-1.0f,2.0f,0.5f,2.0f);
        Prostopadloscian(gl,-0.5f,0.5f,-1.0f,0.1f,1.0f,0.1f);
        Prostopadloscian(gl,-0.5f,0.5f,0.9f,0.1f,1.0f,0.1f);
        Prostopadloscian(gl,1.4f,0.5f,-1.0f,0.1f,1.0f,0.1f);
        Prostopadloscian(gl,1.4f,0.5f,0.9f,0.1f,1.0f,0.1f);
        Prostopadloscian(gl,-0.5f,1.5f,-1.0f,2.0f,0.1f,2.0f);
        
        gl.glRotatef(0.0f+kat4,0.0f,1.0f,0.0f);
        //ramie 1
        gl.glTranslatef(1.5f,0.0f,0.0f);
        gl.glRotatef(45.0f+kat1,0.0f,0.0f,1.0f);
        Prostopadloscian(gl,0.0f,0.0f,0.0f,3.0f,0.3f,0.3f);
        //ramie 2
        gl.glTranslatef(2.7f,0.0f,0.0f);
        gl.glRotatef(-45.0f+kat2,0.0f,0.0f,1.0f);
        Prostopadloscian(gl,0.0f,0.0f,0.0f,1.5f,0.3f,0.3f);
        //lyzka
        gl.glTranslatef(1.2f,0.1f,0.0f);
        gl.glRotatef(-45.0f+kat3,0.0f,0.0f,1.0f);
        Lyzka(gl);
    }

    private void Prostopadloscian(GL gl, float x0, float y0, float z0,
    float dx, float dy, float dz)
        {
        float x1=x0+dx;
        float y1=y0+dy;
        float z1=z0+dz;
        gl.glBegin(GL.GL_QUADS);
        //sciana przednia
        gl.glNormal3f(0.0f,0.0f,1.0f);
        gl.glVertex3f(x0,y0,z1);
        gl.glVertex3f(x1,y0,z1);
        gl.glVertex3f(x1,y1,z1);
        gl.glVertex3f(x0,y1,z1);
        //sciana tylnia
        gl.glNormal3f(0.0f,0.0f,-1.0f);
        gl.glVertex3f(x0,y1,z0);
        gl.glVertex3f(x1,y1,z0);
        gl.glVertex3f(x1,y0,z0);
        gl.glVertex3f(x0,y0,z0);
        //sciana lewa
        gl.glNormal3f(-1.0f,0.0f,0.0f);
        gl.glVertex3f(x0,y0,z0);
        gl.glVertex3f(x0,y0,z1);
        gl.glVertex3f(x0,y1,z1);
        gl.glVertex3f(x0,y1,z0);
        //sciana prawa
        gl.glNormal3f(1.0f,0.0f,0.0f);
        gl.glVertex3f(x1,y1,z0);
        gl.glVertex3f(x1,y1,z1);
        gl.glVertex3f(x1,y0,z1);
        gl.glVertex3f(x1,y0,z0);
        //sciana dolna
        gl.glNormal3f(0.0f,-1.0f,0.0f);
        gl.glVertex3f(x0,y0,z1);
        gl.glVertex3f(x0,y0,z0);
        gl.glVertex3f(x1,y0,z0);
        gl.glVertex3f(x1,y0,z1);
        //sciana gorna
        gl.glNormal3f(0.0f,1.0f,0.0f);
        gl.glVertex3f(x1,y1,z1);
        gl.glVertex3f(x1,y1,z0);
        gl.glVertex3f(x0,y1,z0);
        gl.glVertex3f(x0,y1,z1);
        gl.glEnd();
    }

    private void Walec(GL gl, float promien, float dlugosc,
    float px, float py, float pz)
    {
        float x=0.0f,y=0.0f,kat=0.0f;
        gl.glBegin(GL.GL_QUAD_STRIP);
        for(kat = 0.0f; kat < (2.0f*Math.PI); kat += (Math.PI/32.0f))
        {
            x = px + promien*(float)Math.sin(kat);
            y = py + promien*(float)Math.cos(kat);
            gl.glNormal3f((float)Math.sin(kat),(float)Math.cos(kat),0.0f);
            gl.glVertex3f(x, y, pz);
            gl.glVertex3f(x, y, pz+dlugosc);
        }
        gl.glEnd();
        gl.glNormal3f(0.0f,0.0f,-1.0f);
        x=y=kat=0.0f;
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glVertex3f(px, py, pz); //srodek kola
        for(kat = 0.0f; kat < (2.0f*Math.PI); kat += (Math.PI/32.0f))
        {
            x = px + promien*(float)Math.sin(kat);
            y = py + promien*(float)Math.cos(kat);
            gl.glVertex3f(x, y, pz);
        }
        gl.glEnd();
        gl.glNormal3f(0.0f,0.0f,1.0f);
        x=y=kat=0.0f;
        gl.glBegin(GL.GL_TRIANGLE_FAN);
        gl.glVertex3f(px, py, pz+dlugosc); //srodek kola
        for(kat = 2.0f*(float)Math.PI; kat > 0.0f ; kat -= (Math.PI/32.0f))
        {
            x = px + promien*(float)Math.sin(kat);
            y = py + promien*(float)Math.cos(kat);
            gl.glVertex3f(x, y, pz+dlugosc);
        }
        gl.glEnd();
    }

    private void Lyzka(GL gl)
    {
        gl.glDisable(GL.GL_CULL_FACE);
        gl.glBegin(GL.GL_TRIANGLES);
        //prawa
        gl.glNormal3f(0.0f,0.0f,1.0f);
        gl.glVertex3f(0.0f,0.0f,0.5f);
        gl.glVertex3f(0.5f,0.5f,0.5f);
        gl.glVertex3f(0.0f,0.5f,0.5f);
        //lewa
        gl.glNormal3f(0.0f,0.0f,-1.0f);
        gl.glVertex3f(0.0f,0.0f,-0.2f);
        gl.glVertex3f(0.0f,0.5f,-0.2f);
        gl.glVertex3f(0.5f,0.5f,-0.2f);
        gl.glEnd();
        gl.glDisable(GL.GL_CULL_FACE);
        gl.glBegin(GL.GL_QUADS);
        gl.glNormal3f(-1.0f,0.0f,0.0f);
        gl.glVertex3f(0.0f,0.0f,0.5f);
        gl.glVertex3f(0.0f,0.5f,0.5f);
        gl.glVertex3f(0.0f,0.5f,-0.2f);
        gl.glVertex3f(0.0f,0.0f,-0.2f);
        gl.glNormal3f(0.0f,1.0f,0.0f);
        gl.glVertex3f(0.0f,0.5f,0.5f);
        gl.glVertex3f(0.5f,0.5f,0.5f);
        gl.glVertex3f(0.5f,0.5f,-0.2f);
        gl.glVertex3f(0.0f,0.5f,-0.2f);
        gl.glEnd();
        gl.glEnable(GL.GL_CULL_FACE);
    }
}