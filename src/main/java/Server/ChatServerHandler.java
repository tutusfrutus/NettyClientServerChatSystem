package Server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        //Identify the Client
        Channel incoming = ctx.channel();
        for (Channel channel : channels) {
            //Tell the other Clients who joined.
                channel.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " has joined!\n");
        }
        //Add the Client
        channels.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        //Identify the Client
        Channel incoming = ctx.channel();
        for (Channel channel : channels) {
            //Tell the other Clients who left.
            channel.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " has left!\n");
        }
        //Remove the Client
        channels.remove(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) {
        //Identify who sent a msg to the server
        Channel incoming = channelHandlerContext.channel();
        for (Channel channel : channels) {
            //Send a message to the channels containing the incoming message except to the incoming one.
            if (channel != incoming) {
                channel.writeAndFlush("[" + incoming.remoteAddress() + "] " + s + "\n");
            }
        }
    }
}
