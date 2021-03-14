package com.alrex.parcool.common.capability;

import com.alrex.parcool.ParCool;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ICrawl {
    @OnlyIn(Dist.CLIENT)
    public boolean canCrawl(ClientPlayerEntity player);
    @OnlyIn(Dist.CLIENT)
    public boolean canSliding(ClientPlayerEntity player);
    public boolean isCrawling();
    public void setCrawling(boolean crawling);
    public boolean isSliding();
    public void setSliding(boolean sliding);
    @OnlyIn(Dist.CLIENT)
    public void updateSlidingTime(ClientPlayerEntity player);

    public static class CrawlStorage implements Capability.IStorage<ICrawl>{
        @Override
        public void readNBT(Capability<ICrawl> capability, ICrawl instance, Direction side, INBT nbt) { }
        @Nullable @Override
        public INBT writeNBT(Capability<ICrawl> capability, ICrawl instance, Direction side) {
            return null;
        }
    }

    public static class CrawlProvider implements ICapabilityProvider {
        @CapabilityInject(ICrawl.class)
        public static final Capability<ICrawl> CRAWL_CAPABILITY = null;
        public static final ResourceLocation CAPABILITY_LOCATION=new ResourceLocation(ParCool.MOD_ID,"capability.parcool.crawl");

        private LazyOptional<ICrawl> instance=LazyOptional.of(CRAWL_CAPABILITY::getDefaultInstance);

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            return cap == CRAWL_CAPABILITY ? instance.cast() : LazyOptional.empty();
        }
        @Nonnull @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
            return cap == CRAWL_CAPABILITY ? instance.cast() : LazyOptional.empty();
        }
    }

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class CrawlRegistry{
        @SubscribeEvent
        public static void register(FMLCommonSetupEvent event){
            CapabilityManager.INSTANCE.register(ICrawl.class,new ICrawl.CrawlStorage(),Crawl::new);
        }
    }
}