package com.example.practice_ptv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PracticePtvApplication {

    public static void main(String[] args) {
        SpringApplication.run(PracticePtvApplication.class, args);
    }

} package com.side;
 
 import org.bytedeco.ffmpeg.global.avcodec;
 import org.bytedeco.javacv.FFmpegFrameRecorder;
 import org.bytedeco.javacv.Frame;
 import org.bytedeco.javacv.Java2DFrameConverter;
 import org.bytedeco.javacv.OpenCVFrameConverter;
 import org.bytedeco.opencv.global.opencv_core;
 import org.bytedeco.opencv.global.opencv_imgcodecs;
 import org.bytedeco.opencv.global.opencv_imgproc;
 import org.bytedeco.opencv.opencv_core.Mat;
 import org.bytedeco.opencv.opencv_core.Size;
 
 
 import javax.imageio.ImageIO;
 import java.awt.image.BufferedImage;
 import java.io.File;
 import java.io.IOException;
 import java.util.ArrayList;
 import java.util.List;
 
 
 class Main {
     private static final int TRANSITION_FRAMES = 30; // Number of frames for transition
     private static final int DISPLAY_FRAMES = 60; // Number of frames to display each image
     private static final int FRAME_RATE = 30; // Frames per second
 
     /**
      * Creates a video slideshow from a list of image paths.
      */
     public static void createSlideshow(List<String> imagePaths, String outputVideoPath) throws Exception {
         if (imagePaths == null || imagePaths.size() < 2) {
             throw new IllegalArgumentException("At least two images are required to create a slideshow.");
         }
 
         Mat firstImage = opencv_imgcodecs.imread(imagePaths.get(0));
         int width = firstImage.cols();
         int height = firstImage.rows();
 
         try (FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputVideoPath, width, height)) {
             recorder.setFrameRate(FRAME_RATE);
             recorder.setVideoCodec(org.bytedeco.ffmpeg.global.avcodec.AV_CODEC_ID_H264);
             recorder.start();
 
             OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();
 
             for (int i = 0; i < imagePaths.size() - 1; i++) {
                 Mat image1 = opencv_imgcodecs.imread(imagePaths.get(i));
                 Mat image2 = opencv_imgcodecs.imread(imagePaths.get(i + 1)); 
 
                 // Resize images to match the first image's dimensions
                 Mat resizedImage1 = new Mat();
                 Mat resizedImage2 = new Mat();
                 opencv_imgproc.resize(image1, resizedImage1, new Size(width, height));
                 opencv_imgproc.resize(image2, resizedImage2, new Size(width, height));
 
                 // Display the first image for a set duration
                 for (int j = 0; j < DISPLAY_FRAMES; j++) {
                     recorder.record(converter.convert(resizedImage1));
                 }
 
                 // Apply fade transition between images
                 for (int j = 0; j <= TRANSITION_FRAMES; j++) {
                     double alpha = (double) j / TRANSITION_FRAMES;
                     Mat blended = new Mat();
                     opencv_core.addWeighted(resizedImage1, 1.0 - alpha, resizedImage2, alpha, 0.0, blended);
                     recorder.record(converter.convert(blended));
                 }
             }
 
             // Display the last image for a set duration
             Mat lastImage = opencv_imgcodecs.imread(imagePaths.get(imagePaths.size() - 1));
             Mat resizedLastImage = new Mat();
             opencv_imgproc.resize(lastImage, resizedLastImage, new Size(width, height));
             for (int j = 0; j < DISPLAY_FRAMES; j++) {
                 recorder.record(converter.convert(resizedLastImage));
             }
 
             recorder.stop();
     }
 }
 
     public static void main(String[] args) throws Exception {
         //Initialize FFmpeg
         String outputVideoPath = "output.mp4";
         List<String> imageFilePaths = new ArrayList<>();
         imageFilePaths.add("C:\\Users\\63200307\\Documents\\work_space\\untitled\\src\\main\\resources\\images\\image.png");
         imageFilePaths.add("C:\\Users\\63200307\\Documents\\work_space\\untitled\\src\\main\\resources\\images\\image1.png");
 
         createSlideshow(imageFilePaths, outputVideoPath);
 //
 //        try (FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputVideoPath, 2560, 1440);
 //             Java2DFrameConverter converter = new Java2DFrameConverter()
 //        ) {
 //            recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
 //            recorder.setFormat("mp4");
 //            recorder.setFrameRate(30); // 1 frame per second
 //            recorder.start();
 //
 //            // Loop through images and add them to the video
 //            for (String imageFile : imageFilePaths) {
 //                BufferedImage image = ImageIO.read(new File(imageFile));
 //                Frame frame = converter.convert(image);
 //                recorder.record(frame);
 //            }
 //
 //            recorder.stop();
 //            System.out.println("Slideshow created successfully.");
 //        } catch (IOException e) {
 //            e.printStackTrace();
 //        }
     }
 }

