import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import netP5.*; 
import oscP5.*; 
import spout.*; 
import gab.opencv.*; 
import processing.video.*; 
import java.awt.Rectangle; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class SpoutBlobTracking extends PApplet {






/**
 * MultipleColorTracking
 * Select 4 colors to track them separately
 *
 * It uses the OpenCV for Processing library by Greg Borenstein
 * https://github.com/atduskgreg/opencv-processing
 *
 * @author: Jordi Tost (@jorditost)
 * @url: https://github.com/jorditost/ImageFiltering/tree/master/MultipleColorTracking
 *
 * University of Applied Sciences Potsdam, 2014
 *
 * Instructions:
 * Press one numerical key [1-4] and click on one color to track it
 */
 




OpenCV opencv;
OscP5 oscp5;
NetAddress location;
PImage src;
ArrayList<Contour> touch;
ArrayList<Contour> hover;
Spout spout;
// <1> Set the range of Hue values for our filter
//ArrayList<Integer> colors;

int[] hues;
int[] colors;
int rangeWidth = 10;
PImage img; // Image to receive a texture
PImage[] outputs;

int colorToChange = -1;

public void setup() {
  opencv = new OpenCV(this, width, height);
  touch = new ArrayList<Contour>();
  hover = new ArrayList<Contour>();
  
  colors = new int[2];
  hues = new int[2];
  
  outputs = new PImage[2];
  
  
  
  // Array for detection colors
  colors[0] = color(47.0f, 55.0f, 82.0f);
  hues[0] = PApplet.parseInt(map(hue(color(47.0f, 55.0f, 82.0f)), 0, 255, 0, 180));
  colors[1] = color(164.0f, 247.0f, 171.0f);
  hues[1] = PApplet.parseInt(map(hue(color(164.0f, 247.0f, 171.0f)), 0, 255, 0, 180));
    spout = new Spout(this);
 oscp5 = new OscP5(this, 8888);
 location = new NetAddress("127.0.0.1", 8887);
  img = createImage(width, height, ARGB);

}

public void draw() {
  
  background(150);
   img = spout.receivePixels(img);
    image(img, 0, 0, width, height);

  // <2> Load the new frame of our movie in to OpenCV
  opencv.loadImage(img);
  
  // Tell OpenCV to use color information
  opencv.useColor();
  src = opencv.getSnapshot();
  
  // <3> Tell OpenCV to work in HSV color space.
  opencv.useColor(HSB);
  
  detectColors();
  
  // Show images
  image(src, 0, 0);
  
      image(outputs[0], width-src.width/4, src.height/4, src.width/4, src.height/4);
      image(outputs[1], width-src.width/4, src.height/4 + src.height/4, src.width/4, src.height/4);
      
      noStroke();
      fill(colors[0]);
      rect(src.width, src.height/4, 30, src.height/4);
      fill(colors[1]);
      rect(src.width, src.height/4 + src.height/4, 30, src.height/4);
    
  
  
  // Print text if new color expected
  textSize(20);
  stroke(255);
  fill(255);
  
  
  
  displayContoursBoundingBoxes();
  
  
}

//////////////////////
// Detect Functions
//////////////////////

public void detectColors() {
    
 
    
    //if (hues <= 0) continue;
    for(int i = 0; i < 2; i++){
    opencv.loadImage(src);
    opencv.useColor(HSB);
    
    // <4> Copy the Hue channel of our image into 
    //     the gray channel, which we process.
    opencv.setGray(opencv.getH().clone());
    
    int hueToDetect = hues[i];
    //println("index " + i + " - hue to detect: " + hueToDetect);
    
    // <5> Filter the image based on the range of 
    //     hue values that match the object we want to track.
    opencv.inRange(hueToDetect-rangeWidth/2, hueToDetect+rangeWidth/2);
    
    //opencv.dilate();
    opencv.erode();
    
    // TO DO:
    // Add here some image filtering to detect blobs better
    
    // <6> Save the processed image for reference.
    outputs[i] = opencv.getSnapshot();
  
  
  // <7> Find contours in our range image.
  //     Passing 'true' sorts them by descending area.
  if (outputs[i] != null) {
    
    opencv.loadImage(outputs[i]);
    if(i == 0)
      touch = opencv.findContours(true,true);
    else
      hover = opencv.findContours(true,true);
  }
  }
}

public void displayContoursBoundingBoxes() {
  OscMessage msg = new OscMessage("/screen/position");
  OscMessage msgcount = new OscMessage("/blob/count");
  OscMessage msg_hover = new OscMessage("/hover/position");
  OscMessage msgcount_hover = new OscMessage("/hover/count");
  int cnt = 0;
  int hcnt = 0;
  for (int i=0; i<touch.size(); i++) {
    
    Contour contour = touch.get(i);
    Rectangle r = contour.getBoundingBox();
    
    if (r.width < 30 || r.height < 30)
      continue;
    
    stroke(255, 0, 0);
    fill(255, 0, 0, 150);
    strokeWeight(2);
    rect(r.x, r.y, r.width, r.height);
    text((r.x + r.width/2) + "," +  (r.y + r.height/2), r.x + r.width/2, r.y + r.height/2);
    
    msg.add(((r.x + r.width/2.0f)/640.0f));
    msg.add(((r.y + r.height/2.0f)/480.0f));
    cnt++;
  }
  
  for (int i=0; i<hover.size(); i++) {
    
    Contour contour = hover.get(i);
    Rectangle r = contour.getBoundingBox();
    
    if (r.width < 30 || r.height < 30)
      continue;
    
    stroke(255, 0, 0);
    fill(255, 0, 0, 150);
    strokeWeight(2);
    rect(r.x, r.y, r.width, r.height);
    text((r.x + r.width/2) + "," +  (r.y + r.height/2), r.x + r.width/2, r.y + r.height/2);
    
    msg_hover.add(((r.x + r.width/2.0f)/640.0f));
    msg_hover.add(((r.y + r.height/2.0f)/480.0f));
    hcnt++;
  }
  msgcount.add(cnt);
  msgcount_hover.add(hcnt);
  oscp5.send(msg, location);
  oscp5.send(msgcount, location);
  oscp5.send(msg_hover, location);
  oscp5.send(msgcount_hover, location);
}

//////////////////////
// Keyboard / Mouse
//////////////////////

public void mousePressed() {
  int c = get(mouseX, mouseY);
    println("r: " + red(c) + " g: " + green(c) + " b: " + blue(c));
  if (mouseButton == RIGHT) {
    // Bring up a dialog to select a sender.
    // Spout installation required
    spout.selectSender();
  }
}
  public void settings() {  size(640, 480, P2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "SpoutBlobTracking" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
