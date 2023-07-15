package io.github.itzispyder.clickcrystals.client;

import io.github.itzispyder.clickcrystals.data.Frequency;
import io.github.itzispyder.clickcrystals.scheduler.ArrayQueue;
import io.github.itzispyder.clickcrystals.scheduler.Queue;
import io.github.itzispyder.clickcrystals.util.ChatUtils;
import io.github.itzispyder.clickcrystals.util.MathUtils;
import net.minecraft.network.packet.Packet;

public class PacketInfo {

    private final String name;
    private int lastSeen, count, attemptedPrints;
    private final Queue<Frequency> frequencies;
    private final Status status;

    public PacketInfo(Packet<?> packet) {
        this.name = PacketMapper.getNameOf(packet);
        this.lastSeen = 64;
        this.count = 0;
        this.frequencies = new ArrayQueue<>(10);
        this.status = Status.fromPacketName(name);
    }

    public void onLogged() {
        frequencies.enqueue(determineFrequency(lastSeen));
        incrementCount();
        lastSeen = 0;
    }

    public String getDetails() {
        return "[" + MathUtils.getSystemLogTime() + "] [Packet-" + status.name() + "] " + name;
    }

    public void print() {
        if (++attemptedPrints >= getFrequency().getValue()) {
            String details = getDetails();
            if (attemptedPrints > 1) {
                details = details.concat(" (x" + attemptedPrints + ")");
            }

            ChatUtils.sendMessage(details);
            attemptedPrints = 0;
        }
    }

    private Frequency determineFrequency(int lastSeen) {
        if (lastSeen <= 1) {
            return Frequency.HIGHEST;
        }
        else if (lastSeen <= 5) {
            return Frequency.HIGHER;
        }
        else if (lastSeen <= 8) {
            return Frequency.HIGH;
        }
        else if (lastSeen <= 11) {
            return Frequency.NORMAL;
        }
        else if (lastSeen <= 14) {
            return Frequency.LOW;
        }
        else if (lastSeen <= 17) {
            return Frequency.LOWER;
        }
        else {
            return Frequency.LOWEST;
        }
    }

    public void incrementCount() {
        count ++;
    }

    public void decrementCount() {
        count --;
    }

    public void incrementTime() {
        lastSeen ++;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(int lastSeen) {
        this.lastSeen = lastSeen;
    }

    public Frequency getFrequency() {
        return Frequency.fromList(frequencies.getElements());
    }

    public String getName() {
        return name;
    }

    public Status getStatus() {
        return status;
    }

    public enum Status {
        SENT,
        RECEIVED,
        UNMAPPED;

        public static Status fromPacketName(String name) {
            if (PacketMapper.getC2SNames().contains(name)) {
                return SENT;
            }
            else if (PacketMapper.getS2CNames().contains(name)) {
                return RECEIVED;
            }
            else {
                return UNMAPPED;
            }
        }
    }
}