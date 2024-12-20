package simpleprograms;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileOutputStream;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTable;
import org.apache.poi.xslf.usermodel.XSLFTableCell;
import org.apache.poi.xslf.usermodel.XSLFTableRow;
import org.apache.poi.xslf.usermodel.XSLFTextBox;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;

public class CreateTemplate {

    public static void main(String[] args) {
        // Define the output file path
        String outputFilePath = "template_with_table.pptx";

        try (XMLSlideShow ppt = new XMLSlideShow()) {

            // Create a slide
            XSLFSlide slide = ppt.createSlide();

            // Create Table (above the images and text boxes)
            XSLFTable table = slide.createTable();

            // Set table position and size
            table.setAnchor(new Rectangle(50, 50, 600, 150));

            // Create the header row
            XSLFTableRow headerRow = table.addRow();
            headerRow.setHeight(40);  // Control row height

            // Add header cells and customize each one
            addCellWithStyle(headerRow, "Agency", Color.WHITE, new Color(0, 102, 204), 14.0);
            addCellWithStyle(headerRow, "MROJA Name", Color.WHITE, new Color(0, 102, 204), 14.0);
            addCellWithStyle(headerRow, "Exam Name", Color.WHITE, new Color(0, 102, 204), 14.0);
            addCellWithStyle(headerRow, "Business/Function", Color.WHITE, new Color(0, 102, 204), 14.0);
            addCellWithStyle(headerRow, "Owner", Color.WHITE, new Color(0, 102, 204), 14.0);
            addCellWithStyle(headerRow, "Proposed Business Target Date", Color.WHITE, new Color(0, 102, 204), 14.0);
            addCellWithStyle(headerRow, "Response Letter Due Date", Color.WHITE, new Color(0, 102, 204), 14.0);

            // Add a data row (just placeholders for now)
            XSLFTableRow dataRow = table.addRow();
            dataRow.setHeight(30);  // Control row height

            // Add placeholder data cells
            for (int i = 0; i < 7; i++) {
                addCellWithStyle(dataRow, " ", Color.BLACK, Color.WHITE, 12.0);  // Default black text on white background
            }

            // Define image placeholders (left column)
            XSLFTextBox imageBox1 = slide.createTextBox();
            imageBox1.setAnchor(new Rectangle(50, 210, 150, 100)); // Image 1
            XSLFTextParagraph imagePara1 = imageBox1.addNewTextParagraph();
            imagePara1.addNewTextRun().setText("Image 1 Placeholder");

            XSLFTextBox imageBox2 = slide.createTextBox();
            imageBox2.setAnchor(new Rectangle(50, 340, 150, 100)); // Image 2
            XSLFTextParagraph imagePara2 = imageBox2.addNewTextParagraph();
            imagePara2.addNewTextRun().setText("Image 2 Placeholder");

            XSLFTextBox imageBox3 = slide.createTextBox();
            imageBox3.setAnchor(new Rectangle(50, 470, 150, 100)); // Image 3
            XSLFTextParagraph imagePara3 = imageBox3.addNewTextParagraph();
            imagePara3.addNewTextRun().setText("Image 3 Placeholder");

            // Define text box placeholders (right column)
            XSLFTextBox textBox1 = slide.createTextBox();
            textBox1.setAnchor(new Rectangle(220, 210, 300, 100)); // Textbox 1
            XSLFTextParagraph para1 = textBox1.addNewTextParagraph();
            para1.addNewTextRun().setText("Textbox 1 Placeholder");

            XSLFTextBox textBox2 = slide.createTextBox();
            textBox2.setAnchor(new Rectangle(220, 340, 300, 100)); // Textbox 2
            XSLFTextParagraph para2 = textBox2.addNewTextParagraph();
            para2.addNewTextRun().setText("Textbox 2 Placeholder");

            XSLFTextBox textBox3 = slide.createTextBox();
            textBox3.setAnchor(new Rectangle(220, 470, 300, 100)); // Textbox 3
            XSLFTextParagraph para3 = textBox3.addNewTextParagraph();
            para3.addNewTextRun().setText("Textbox 3 Placeholder");

            // Save the PowerPoint file
            try (FileOutputStream out = new FileOutputStream(new File(outputFilePath))) {
                ppt.write(out);
                System.out.println("PowerPoint template with table created successfully!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to add a styled cell
    private static void addCellWithStyle(XSLFTableRow row, String text, Color textColor, Color bgColor, Double fontSize) {
        XSLFTableCell cell = row.addCell();
        cell.setText(text);

        // Set background color
        cell.setFillColor(bgColor);

        // Create a text run and set font color and size
        XSLFTextParagraph p = cell.getTextParagraphs().get(0);
        XSLFTextRun run = p.getTextRuns().get(0);
        run.setFontColor(textColor);
        run.setFontSize(fontSize);  // Use Double for font size
    }
}

