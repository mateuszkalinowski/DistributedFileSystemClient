package pl.dfs;

import sun.misc.IOUtils;
import sun.nio.ch.IOUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = null;
            serverSocket = new ServerSocket(4444);
            Socket socket = null;
            socket = serverSocket.accept();

            socket.setTcpNoDelay(true);

            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
            BufferedInputStream bufferedInputStream = new BufferedInputStream(socket.getInputStream());


                byte[] bytes = new byte[10*1024*1024];
                int count;
                while ((count = bufferedInputStream.read(bytes)) > 0) {
                    try {
                        String input = new String(bytes).substring(0, count);
                        int i = 0;
                        int finalIndex;
                        while (input.charAt(i) != ' ') i++;

                        String command = input.substring(0, i);
                        finalIndex = i + 1;

                        input = input.substring(i + 1, input.length());

                        if (command.equals("save")) {
                            i = 0;
                            while (input.charAt(i) != ' ') i++;
                            String name = input.substring(0, i);
                            input = input.substring(i + 1, input.length());
                            finalIndex += i + 1;
                            FileOutputStream fileOutputStream = new FileOutputStream(new File(System.getProperty("user.home") + "/" + name));
                            fileOutputStream.write(bytes, finalIndex, count);
                            fileOutputStream.close();
                            String response = "success";
                            bufferedOutputStream.write(response.getBytes());
                            bufferedOutputStream.flush();
                        } else {

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
}
