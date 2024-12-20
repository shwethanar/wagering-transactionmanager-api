package simpleprograms;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextBox;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;

import java.io.File;
import java.io.FileOutputStream;
import java.awt.Rectangle;

public class CreateTemplate1 {

    public static void main(String[] args) {

        // Hardcoded slide data
        String[] placeholderComments = {
            "This is a comment for slide 1",
            "This is a comment for slide 2",
            "This is a comment for slide 3",
            "This is a comment for slide 4"
        };

        String[] placeholderImages = {
            "Image for slide 1",
            "Image for slide 2",
            "Image for slide 3",
            "Image for slide 4"
        };

        // Hardcoded positions and sizes for the comment and image boxes (x, y, width, height)
        int[][] commentPositions = {
            {50, 50, 620, 80},   // Slide 1
            {500, 50, 180, 440}, // Slide 2
            {290, 50, 70, 200},  // Slide 3 - top left
            {620, 50, 70, 200},  // Slide 3 - top right
            {290, 290, 70, 200}, // Slide 3 - bottom left
            {620, 290, 70, 200}, // Slide 3 - bottom right
            {510, 60, 150, 420}  // Slide 4 (overlay - comment over image)
        };

        int[][] imagePositions = {
            {50, 150, 620, 350},   // Slide 1
            {50, 50, 420, 440},    // Slide 2
            {50, 50, 210, 200},    // Slide 3 - top left
            {390, 50, 210, 200},   // Slide 3 - top right
            {50, 290, 210, 200},   // Slide 3 - bottom left
            {390, 290, 210, 200},  // Slide 3 - bottom right
            {50, 50, 620, 440}     // Slide 4 (overlay - comment over image)
        };

        // Create the PowerPoint presentation
        XMLSlideShow ppt = new XMLSlideShow();

        // Loop through each slide's data and create the corresponding slide
        for (int i = 0; i < 4; i++) {
            // Create a new slide
            XSLFSlide slide = ppt.createSlide();

            // Create a text box for placeholder_comments
            XSLFTextBox commentsBox = slide.createTextBox();
            commentsBox.setAnchor(new Rectangle(commentPositions[i][0], commentPositions[i][1],
                    commentPositions[i][2], commentPositions[i][3]));
            XSLFTextParagraph commentsParagraph = commentsBox.addNewTextParagraph();
            XSLFTextRun commentsRun = commentsParagraph.addNewTextRun();
            commentsRun.setText(placeholderComments[i]);
            commentsRun.setFontSize(18.0);

            // Create a text box for placeholder_image
            XSLFTextBox imageBox = slide.createTextBox();
            imageBox.setAnchor(new Rectangle(imagePositions[i][0], imagePositions[i][1],
                    imagePositions[i][2], imagePositions[i][3]));
            XSLFTextParagraph imageParagraph = imageBox.addNewTextParagraph();
            XSLFTextRun imageRun = imageParagraph.addNewTextRun();
            imageRun.setText(placeholderImages[i]);
            imageRun.setFontSize(18.0);
        }

        // Write the PowerPoint file to disk
        try (FileOutputStream out = new FileOutputStream(new File("template_deck_hardcoded.pptx"))) {
            ppt.write(out);
            System.out.println("PowerPoint template created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
