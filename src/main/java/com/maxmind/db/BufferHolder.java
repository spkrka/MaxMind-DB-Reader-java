package com.maxmind.db;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import com.maxmind.db.Reader.FileMode;

final class BufferHolder {
    // DO NOT PASS OUTSIDE THIS CLASS. Doing so will remove thread safety.
    private final ByteBuffer buffer;
    private final boolean sync;

    BufferHolder(File database, FileMode mode, boolean sync) throws IOException {
        this.sync = sync;
        try (
                final RandomAccessFile file = new RandomAccessFile(database, "r");
                final FileChannel channel = file.getChannel()
        ) {
            if (mode == FileMode.MEMORY) {
                this.buffer = ByteBuffer.wrap(new byte[(int) channel.size()]);
                if (channel.read(this.buffer) != this.buffer.capacity()) {
                    throw new IOException("Unable to read "
                            + database.getName()
                            + " into memory. Unexpected end of stream.");
                }
            } else {
                this.buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
            }
        }
    }

    /**
     * Construct a ThreadBuffer from the provided URL.
     *
     * @param stream the source of my bytes.
     * @throws IOException          if unable to read from your source.
     * @throws NullPointerException if you provide a NULL InputStream
     */
    BufferHolder(InputStream stream, boolean sync) throws IOException {
        this.sync = sync;
        if (null == stream) {
            throw new NullPointerException("Unable to use a NULL InputStream");
        }
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final byte[] bytes = new byte[16 * 1024];
        int br;
        while (-1 != (br = stream.read(bytes))) {
            baos.write(bytes, 0, br);
        }
        this.buffer = ByteBuffer.wrap(baos.toByteArray());
    }

    /*
     * Returns a duplicate of the underlying ByteBuffer. The returned ByteBuffer
     * should not be shared between threads.
     */
    ByteBuffer get() {
        if (sync) {
            synchronized (this) {
                return this.buffer.duplicate();
            }
        } else {
            return this.buffer.duplicate();
        }
    }
}
