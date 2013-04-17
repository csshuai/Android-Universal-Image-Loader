package com.nostra13.universalimageloader.core.assist;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public class ImageStream implements Closeable {
    private InputStream mInputStream;
    private long mLength;

    public ImageStream(InputStream inputStream, long length) {
        this.mInputStream = inputStream;
        this.mLength = length;
    }

    public InputStream getInputStream() {
        return mInputStream;
    }

    public void setInputStream(InputStream mInputStream) {
        this.mInputStream = mInputStream;
    }

    public long getLength() {
        return mLength;
    }

    public void setLength(long mLength) {
        this.mLength = mLength;
    }

    @Override
    public void close() throws IOException {
        if (mInputStream != null) {
            mInputStream.close();
        }
    }
}
