package com.siyka.omron.fins.master;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.siyka.omron.fins.FinsFrame;
import com.siyka.omron.fins.FinsResponse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

public class FinsMasterHandler extends SimpleChannelInboundHandler<FinsFrame<FinsResponse>> {

	final static Logger logger = LoggerFactory.getLogger(FinsMasterHandler.class);

	private final Map<Byte, CompletableFuture<FinsFrame<FinsResponse>>> futures;

	public FinsMasterHandler(final Map<Byte, CompletableFuture<FinsFrame<FinsResponse>>> futures) {
		this.futures = futures;
	}

	@Override
	protected void channelRead0(final ChannelHandlerContext context, final FinsFrame<FinsResponse> frame) throws Exception {
		Optional.ofNullable(this.futures.get(frame.getHeader().getServiceAddress())).ifPresent(future -> future.complete(frame));
		ReferenceCountUtil.release(frame);
	}

}