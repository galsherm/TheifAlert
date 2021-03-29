package application;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;


import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

public class CameraSnapshotJavaFX  {
   Mat matrix = null;

   public WritableImage capureSnapShot() {
      WritableImage WritableImage = null;
		System.out.println("take pic"); // gal

      // Loading the OpenCV core library
      System.loadLibrary( Core.NATIVE_LIBRARY_NAME );

      // Instantiating the VideoCapture class (camera:: 0)
      VideoCapture capture = new VideoCapture(0);

      // Reading the next video frame from the camera
      Mat matrix = new Mat();
      capture.read(matrix);

      // If camera is opened
      if( capture.isOpened()) {
         // If there is next video frame
         if (capture.read(matrix)) {
            // Creating BuffredImage from the matrix
            BufferedImage image = new BufferedImage(matrix.width(), 
               matrix.height(), BufferedImage.TYPE_3BYTE_BGR);
            
            WritableRaster raster = image.getRaster();
            DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
            byte[] data = dataBuffer.getData();
            matrix.get(0, 0, data);
            this.matrix = matrix;
            
            // Creating the Writable Image
            WritableImage = SwingFXUtils.toFXImage(image, null);
            capture.release();
            
            
         }
      }
      return WritableImage;
   }
   public void saveImage() {
      // Saving the Image
     // String file = "E:\\Program files\\openCV\\Pictures\\pic.JPG";
		
	     String file = FirstWindow.filePathPic+"\\pic.JPG";
	  // String file = "E:\\picc.jpg";
      // Saving it again 
      Imgcodecs.imwrite(file, matrix);
   }
   public static void main(String args[]) {
	   CameraSnapshotJavaFX obj = new CameraSnapshotJavaFX();
	   
	  //  @SuppressWarnings("unused")
		WritableImage writableImage = obj.capureSnapShot();

	      // Saving the image
	      obj.saveImage();
	      
     // launch(args);
   }
}
