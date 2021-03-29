package application;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

import org.opencv.imgcodecs.Imgcodecs; // imread, imwrite		-gal
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

public class JavaCVPrjt01 {
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static int detect = 1; // gal
	  static JTextField textfield1;

	static Mat imag = null;
	private static int cnt = 0;
	

	public static void main(String[] args) {
		
		
	FirstWindow.main(args);
		
	
    
		JFrame jframe = new JFrame("THEIF  DETECTOR ");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	

		JLabel vidpanel = new JLabel();
		jframe.add(vidpanel);
		
	    JButton btnAddFlight = new JButton("Add Flight");		/// gal need to delete it
	    btnAddFlight.setLocation(0,0);
	    jframe.add(btnAddFlight);
        
		jframe.setContentPane(vidpanel);
		jframe.setSize(640, 480);
		jframe.setResizable(false);
		
		
		jframe.setVisible(true);

   
        
		Mat frame = new Mat();
		Mat outerBox = new Mat();
		Mat diff_frame = null;
		Mat tempon_frame = null;
		ArrayList<Rect> array = new ArrayList<Rect>();
		VideoCapture camera = new VideoCapture(0);

		Size sz = new Size(640, 480);
		int i = 0;

		while (true) {
			if (camera.read(frame)) {

				Imgproc.resize(frame, frame, sz);
				imag = frame.clone();
				outerBox = new Mat(frame.size(), CvType.CV_8UC1);
				Imgproc.cvtColor(frame, outerBox, Imgproc.COLOR_BGR2GRAY);
				Imgproc.GaussianBlur(outerBox, outerBox, new Size(3, 3), 0);

				if (i == 0) {
					jframe.setSize(frame.width(), frame.height());
					diff_frame = new Mat(outerBox.size(), CvType.CV_8UC1);
					tempon_frame = new Mat(outerBox.size(), CvType.CV_8UC1);
					diff_frame = outerBox.clone();
				}

				if (i == 1) {

					Core.subtract(outerBox, tempon_frame, diff_frame);
					Imgproc.adaptiveThreshold(diff_frame, diff_frame, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C,
							Imgproc.THRESH_BINARY_INV, 5, 2);
					array = detection_contours(diff_frame);

					if (array.size() > 0) {

						Iterator<Rect> it2 = array.iterator();
						while (it2.hasNext()) {
							Rect obj = it2.next();
							// Core.rectangle(imag, obj.br(), obj.tl(), new Scalar(0, 255, 0), 1); /// gal
							// need to delete
							Imgproc.rectangle(imag, obj.br(), obj.tl(), new Scalar(0, 255, 0), 1);
						}

						if (detect == 1) {
							cnt++;

							detect = 0;
							// System.out.println("Detect" +cnt );

							Thread t1 = new Thread(new Runnable() {
								@Override
								public void run() {

									try {
										if (Thread.interrupted()) {
											CameraSnapshotJavaFX.main(args); // Take a picture and relase the camera
											try {
												Thread.sleep(100);
											} catch (InterruptedException e2) {
												// TODO Auto-generated catch block
												e2.printStackTrace();
											}
											camera.open(0); // open the camera aain

											detect = 0;

											try {
												Thread.sleep(900);

											} catch (InterruptedException e1) {
												// TODO Auto-generated catch block
												e1.printStackTrace();
											}

											SendMail.sendmail(FirstWindow.filePathPic		,FirstWindow.emailAdress
, "Detect Theif",
													"Hey This is the theif! ");
											try {
												Thread.sleep(30000); // sleep about 30 seconds

												cnt = 0; // start to check again
											} catch (InterruptedException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											System.out.println("finish");

										}

									} catch (MessagingException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							});

							t1.start();

							if (cnt == 10) { // if there are 10 inerpuuptions
								t1.interrupt();
								try {
									Thread.sleep(500);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

						}

					}
				}

				i = 1;

				ImageIcon image = new ImageIcon(Mat2bufferedImage(imag));
				vidpanel.setIcon(image);
				vidpanel.repaint();
				tempon_frame = outerBox.clone();

			}

		}}

		//System.out.println("heyy");

	//}

	public static BufferedImage Mat2bufferedImage(Mat image) {
		MatOfByte bytemat = new MatOfByte();
		// Highgui.imencode(".jpg", image, bytemat);

		Imgcodecs.imencode(".jpg", image, bytemat); // gal

		byte[] bytes = bytemat.toArray();
		InputStream in = new ByteArrayInputStream(bytes);
		BufferedImage img = null;
		try {
			img = ImageIO.read(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return img;
	}

	public static ArrayList<Rect> detection_contours(Mat outmat) {
		Mat v = new Mat();
		Mat vv = outmat.clone();
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Imgproc.findContours(vv, contours, v, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

		double maxArea = 100;
		int maxAreaIdx = -1;
		Rect r = null;
		ArrayList<Rect> rect_array = new ArrayList<Rect>();

		for (int idx = 0; idx < contours.size(); idx++) {
			Mat contour = contours.get(idx);
			double contourarea = Imgproc.contourArea(contour);
			if (contourarea > maxArea) {
				// maxArea = contourarea;
				maxAreaIdx = idx;
				r = Imgproc.boundingRect(contours.get(maxAreaIdx));
				rect_array.add(r);
				Imgproc.drawContours(imag, contours, maxAreaIdx, new Scalar(0, 0, 255));

				System.out.println("contours.size : " + contours.size()); // gal

				if (contours.size() > 2000) { // sesativy
					System.out.println("detect");
					detect = 1;

				}

			}

		}

		v.release();
	
		return rect_array;

	}
	
}
