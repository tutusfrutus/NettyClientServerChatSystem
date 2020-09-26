package Server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ChatServer {

    public static void main(String[] args) {
        //Run the ChatServer on localhost and port 8000 and listen for incoming connections.
        new ChatServer(8000).run();
    }

    private final int port;

    public ChatServer(int port) {
        this.port = port;
    }

    public void run() {
        //Listen to incoming connections and hand them off for processing,
        // mainGroup accepts incoming connections as they arrive, then sends them to the workers to process them.
        EventLoopGroup mainGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //Setup a channel
            ServerBootstrap bootstrap = new ServerBootstrap()
                    //Specify which groups have to be used
                    .group(mainGroup, workerGroup)
                    //Sockets to use for communication
                    .channel(NioServerSocketChannel.class)
                    //Class that handles incoming messages
                    .childHandler(new ChatServerInitializer());

            //bind to the specified port and listen for incoming connections
            bootstrap.bind(port).sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            System.out.println("Connection Interrupted.");
            e.printStackTrace();
        } finally {
            //Clean up the used eventgroup loops.
            mainGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
