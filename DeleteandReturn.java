import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.*;

public class DeleteandReturn {

   public File generateFile() {
       ExecutorService executor = Executors.newSingleThreadExecutor();

       File sampleFile = new File("./sampleFile.txt");

       Future<?> makeFile = executor.submit(() -> {
           try {
               FileOutputStream is = new FileOutputStream(sampleFile);
               OutputStreamWriter osw = new OutputStreamWriter(is);
               Writer w = new BufferedWriter(osw);
               w.write("sample file text");
               w.write("\ntesting new line");
               w.write("\nanother line");
               w.close();
           } catch (IOException e) {
               System.err.println("Problem writing to the file samplefile.txt");
           }

           String line = null;

           try {
               FileReader fileReader = new FileReader(sampleFile);
               BufferedReader bufferedReader = new BufferedReader(fileReader);
               while((line = bufferedReader.readLine()) != null) {
                   System.out.println(line);
               }
               // do not close fileReader
               // leave it open so that we can delete the file and still return it
           } catch (FileNotFoundException ex) {
               System.out.println("Unable to open file");
           } catch (IOException ex) {
               System.out.println("Error reading file");
           }
       });

       try {
           makeFile.get(2, TimeUnit.SECONDS);
           sampleFile.delete();
           return sampleFile.getAbsoluteFile();
       } catch (InterruptedException | ExecutionException e) {
           e.printStackTrace();
           return sampleFile.getAbsoluteFile();
       } catch (TimeoutException e) {
           return sampleFile.getAbsoluteFile();
       }

   }

   public static void main(String[] args) {
       DeleteandReturn myFile = new DeleteandReturn();
       myFile.generateFile();
   }
}