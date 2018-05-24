package pl.dfs;

import sun.misc.IOUtils;
import sun.nio.ch.IOUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        try {
            int socketNumber = 4444;
            if(args.length>=1)
                socketNumber = Integer.parseInt(args[0]);

            ServerSocket serverSocket = null;
            serverSocket = new ServerSocket(socketNumber);
            Socket socket = null;
            socket = serverSocket.accept();

            socket.setTcpNoDelay(true);

            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
            BufferedInputStream bufferedInputStream = new BufferedInputStream(socket.getInputStream());

            String rootPath = System.getProperty("user.home");
            File dir = new File(rootPath + File.separator + "dfsDataNode" + String.valueOf(socketNumber));

            path = rootPath + File.separator + "dfsDataNode" + String.valueOf(socketNumber);

            if (!dir.exists())
                dir.mkdirs();
            
                byte[] bytes = new byte[100*1024*1024];
                byte[] finalBytes = new byte[100*1024*1024];
                int count;
                int finalCount;
                while ((count = bufferedInputStream.read(bytes)) > 0) {

                    finalCount=count;
                    System.arraycopy(bytes,0,finalBytes,0,count);
                    while(bytes[count-1]!=4) {
                        count = bufferedInputStream.read(bytes);
                        System.arraycopy(bytes,0,finalBytes,finalCount,count);
                        finalCount+=count;
                    }
                    try {
                        count = finalCount;
                        System.arraycopy(finalBytes,0,bytes,0,finalCount);
                        String input = new String(bytes).substring(0, count);
                        int i = 0;
                        int finalIndex;
                        while (input.charAt(i) != ' ') i++;
                        String command = input.substring(0, i);
                        finalIndex = i + 2;
                        input = input.substring(i + 2, input.length());

                        if (command.equals("save")) {
                            i = 0;
                            while (input.charAt(i) != '"') i++;
                            String name = input.substring(0, i);
                            input = input.substring(i + 2, input.length());
                            finalIndex += i + 2;
                            FileOutputStream fileOutputStream = new FileOutputStream(new File(path + File.separator + name));
                            fileOutputStream.write(bytes, finalIndex, count-1);
                            fileOutputStream.close();
                            String response = "success";
                            bufferedOutputStream.write(response.getBytes());
                            bufferedOutputStream.flush();

                        } else if(command.equals("delete")) {
                            i = 0;
                            while (input.charAt(i) != '"') i++;
                            String name = input.substring(0, i);
                            File toDelete = new File(path + File.separator + name);
                            toDelete.delete();
                            String response = "success";
                            bufferedOutputStream.write(response.getBytes());
                            bufferedOutputStream.flush();

                        } else if(command.equals("download")) {
                            i = 0;
                            while (input.charAt(i) != '"') i++;
                            String name = input.substring(0, i);
                            Path pathTofileToSend = Paths.get(path + File.separator + name);
                            byte[] bytesToSend = Files.readAllBytes(pathTofileToSend);
                            bufferedOutputStream.write(bytesToSend);
                            bufferedOutputStream.write(4);
                            bufferedOutputStream.flush();
                        } else if(command.equals("freespace")) {
                            String freeSpace = String.valueOf(dir.getUsableSpace());
                            bufferedOutputStream.write(freeSpace.getBytes());
                            bufferedOutputStream.write(4);
                            bufferedOutputStream.flush();
                        }
                    } catch (Exception e) {
                        String response = "failure";
                        bufferedOutputStream.write(response.getBytes());
                    }
                }


        }
        catch (IOException e1) {
            System.out.println("wyjatek");
        }

    }

    private static String path;
}
