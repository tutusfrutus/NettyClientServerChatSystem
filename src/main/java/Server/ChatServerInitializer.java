package Server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ChatServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        //Pipeline system, this basically tells how we organize the connection/information
        ChannelPipeline pipeline = socketChannel.pipeline();

        //We expect frames of max size 8192 limited by the Line Endings.
        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        //Decode the received bytes into Strings
        pipeline.addLast("decoder", new StringDecoder());
        //Encode the sent Strings into bytes
        pipeline.addLast("encoder", new StringEncoder());

        //Class to handle the incoming decoded Strings from the client
        pipeline.addLast("handler", new ChatServerHandler());
    }
}
