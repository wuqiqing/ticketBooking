import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.BufferedImage;

public class main {
	
	public static String[] types = new String[64];
	public static String maxValue;
public static void decreaseColor(int[] pixel){
	//降低色階
	int i =  0;
	while(i < pixel.length){
	int value = pixel[i];
		if (value <= 84){
			pixel[i] = 0;
		}
		else if (value > 84 && value <= 168 ){
			pixel[i] = 128;
		}
		else {
			pixel[i] = 255;
		}
		i++;	
	}
}
public static boolean convertFormat(String inputImagePath,
        String outputImagePath, String formatName) throws IOException {
	//轉檔jpegToJpg
    FileInputStream inputStream = new FileInputStream(inputImagePath);
    FileOutputStream outputStream = new FileOutputStream(outputImagePath);
     
    BufferedImage inputImage = ImageIO.read(inputStream);
     
    boolean result = ImageIO.write(inputImage, formatName, outputStream);
     
    outputStream.close();
    inputStream.close();
     
    return result;
}
public static void colorSum(int[] pixel){
	//計算最多的顏色
	int max = 0,count = 0;
	int[] times = new int[64];
	
    String r = Integer.toHexString(pixel[0])+Integer.toHexString(pixel[1])
    		+Integer.toHexString(pixel[2]);

    for (int i = 0;i < types.length;i++) {
    	if(types[i] == r ){
    		times[i]++;
    		break;
    	}
    	types[count] = r;
    	times[count]++;
    	count++;
    }

    int temp = 0;
    for (int i = 0;i < types.length;i++) {
    	if(times[i] > max){
    		max = times[i];
    		temp = i;
    	}
    }	
    maxValue = types[temp];
}
public static String rmColor(int x,int y,int[] pixel){
	//移除顏色
	String r = Integer.toHexString(pixel[0])+Integer.toHexString(pixel[1])
			+Integer.toHexString(pixel[2]);
	while(r.equals("a0c2c2")){
		return r;
	}
	return "aa";
}
public static void getColorAround(int x, int y,BufferedImage img){
	//偵測附近顏色
	int [] array = new int[8];
	int count = 0;
	
	array[0] = img.getRGB(x-1, y-1);
	array[1] = img.getRGB(x, y-1);
	array[2] = img.getRGB(x+1, y-1);
	array[3] = img.getRGB(x-1, y);
	array[4] = img.getRGB(x+1, y);
	array[5] = img.getRGB(x+1, y-1);
	array[6] = img.getRGB(x, y+1);
	array[7] = img.getRGB(x+1, y+1);
	
	for (int arrs : array){
		if (arrs == Integer.decode("0xffffff")){
			count++;
		}		
	}
	System.out.println(count);
}

public static void main(String[] args) {
    String convertInputImage = "/Users/Mac/Downloads/ImageOut.jsp.jpeg";
    String convertOutputImage = "/Users/Mac/Downloads/image.jpg";
    String formatName = "jpg";
    
    try {
        boolean result = main.convertFormat(convertInputImage,
                convertOutputImage, formatName);
        if (result) {
            System.out.println("Image converted successfully.");
        } else {
            System.out.println("Could not convert image.");
        }
    } catch (IOException ex) {
        System.out.println("Error during converting image.");
        ex.printStackTrace();
    }//convert
	
    try {
    	File f = new File(convertOutputImage);
    	BufferedImage bi = ImageIO.read(f);
    	BufferedImage img = new BufferedImage(bi.getWidth(), bi.getHeight(),
    			BufferedImage.TYPE_INT_RGB);

    	int[] pixel;
    	int temp = 0;
    	
    	for (int y = 0; y < bi.getHeight(); y++) {
    	    for (int x = 0; x < bi.getWidth(); x++) {
    	        pixel = bi.getRaster().getPixel(x, y, new int[3]);
    	        decreaseColor(pixel);
    	        String r1 = Integer.toHexString(pixel[0])+Integer.toHexString(pixel[1])
    	        		+Integer.toHexString(pixel[2]);
    	        int intr = Integer.valueOf(r1,16); 
    	        img.setRGB(x,y,intr);
    	    }
    	}
    	System.out.println("Lower level Complete.");//lower level
    	
    	for (int y = 0; y < img.getHeight(); y++) {
    	    for (int x = 0; x < img.getWidth(); x++) {
    	        pixel = img.getRaster().getPixel(x, y, new int[3]);
    	        colorSum(pixel);
    	    }
    	}//find most
    	System.out.println("Remove Color :"+maxValue);
    	
    	/*for (int y = 0; y < img.getHeight(); y++) {
    	    for (int x = 0; x < img.getWidth(); x++) {
    	        pixel = img.getRaster().getPixel(x, y, new int[3]);
    	        while(rmColor(x,y,pixel).equals("a0c2c2")){ 
    	        	int num = Integer.decode("0xffffff");
    	            img.setRGB(x,y,num);
    	            break;
    	        }
    	    }
    	}*/
    	
    	//getColorAround(2,2,img);
    	
    	f = new File("/Users/Mac/Desktop/Output.jpg");
    	ImageIO.write(img, "jpg", f);
    	System.out.println("Done!");
    } catch (IOException e) {
        e.printStackTrace();
	    }
	}
}

