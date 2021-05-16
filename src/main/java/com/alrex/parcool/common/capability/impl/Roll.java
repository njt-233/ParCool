package com.alrex.parcool.common.capability.impl;

import com.alrex.parcool.ParCoolConfig;
import com.alrex.parcool.client.input.KeyBindings;
import com.alrex.parcool.common.capability.ICrawl;
import com.alrex.parcool.common.capability.IRoll;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraftforge.common.util.LazyOptional;

public class Roll implements IRoll {
	private boolean rollReady = false;
	private boolean rolling = false;
	private int rollingTime = 0;

	@Override
	public boolean canContinueRollReady(ClientPlayerEntity player) {
		ICrawl crawl;
		{
			LazyOptional<ICrawl> crawlOptional = player.getCapability(ICrawl.CrawlProvider.CRAWL_CAPABILITY);
			if (!crawlOptional.isPresent()) return false;
			crawl = crawlOptional.orElseThrow(NullPointerException::new);
		}
		return rollReady && ParCoolConfig.CONFIG_CLIENT.canRoll.get() && !crawl.isCrawling() && !crawl.isSliding() && KeyBindings.getKeyRoll().isKeyDown() && !rolling && !player.isInWaterOrBubbleColumn();
	}

	@Override
	public boolean canRollReady(ClientPlayerEntity player) {
		ICrawl crawl;
		{
			LazyOptional<ICrawl> crawlOptional = player.getCapability(ICrawl.CrawlProvider.CRAWL_CAPABILITY);
			if (!crawlOptional.isPresent()) return false;
			crawl = crawlOptional.orElseThrow(NullPointerException::new);
		}
		return !player.collidedVertically && ParCoolConfig.CONFIG_CLIENT.canRoll.get() && player.getMotion().getY() < -0.25 && !crawl.isCrawling() && !crawl.isSliding() && KeyBindings.getKeyRoll().isKeyDown() && !player.isInWaterOrBubbleColumn();
	}

	@Override
	public boolean isRollReady() {
		return rollReady;
	}

	@Override
	public boolean isRolling() {
		return rolling;
	}

	@Override
	public void setRollReady(boolean rollReady) {
		this.rollReady = rollReady;
	}

	@Override
	public void setRolling(boolean rolling) {
		this.rolling = rolling;
		if (!rolling) rollingTime = 0;
	}

	@Override
	public void updateRollingTime() {
		if (rolling) {
			rollingTime++;
			if (rollingTime > getRollAnimateTime()) {
				setRolling(false);
			}
		} else rollingTime = 0;
	}

	@Override
	public int getRollingTime() {
		return rollingTime;
	}

	@Override
	public int getStaminaConsumption() {
		return 50;
	}

	@Override
	public int getRollAnimateTime() {
		return 10;
	}

}
