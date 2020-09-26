package Client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ChatClient {

    private final String hostAddress;
    private final int port;

    public ChatClient(String hostAddress, int port) {
        this.hostAddress = hostAddress;
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        //Run the ChatClient on localhost and port 8000
        new ChatClient("localhost", 8000).run();
    }

    //Run method for Client Behavior
    public void run() throws InterruptedException, IOException {
        //Create a new instance using the default number of threads
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            //Setup the Channel
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    //Let this class handle the Client
                    .handler(new ChatClientInitializer());

            //Create a connection to the server through the bootstrap channel
            Channel channel = bootstrap.connect(hostAddress, port).sync().channel();

            //Capture user input from Console
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                //Takes users input from console and write it to the server
                channel.writeAndFlush(input.readLine() + "\r\n");
            }
        } finally {
            //If for whatever reason the connection is closed or no longer active, shutdown the connection.
            group.shutdownGracefully();
        }
    }
}
