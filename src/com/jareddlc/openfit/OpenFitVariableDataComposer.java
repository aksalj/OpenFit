package com.jareddlc.openfit;

import android.graphics.Bitmap;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OpenFitVariableDataComposer {
    private static final String LOG_TAG = "OpenFit:OpenFitVariableDataComposer";
    private final List<IVariableData> mDataList = new ArrayList();
    private int mPayloadSize;

    static final ByteOrder BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;
    static final Charset DEFAULT_CHARSET = Charset.forName("UCS-2");

    private void addData(IVariableData paramIVariableData) {
      this.mDataList.add(paramIVariableData);
      this.mPayloadSize += paramIVariableData.getByteSize();
    }

    protected int getPayloadSize() {
      return this.mPayloadSize;
    }

    public void reset() {
      this.mDataList.clear();
      this.mPayloadSize = 0;
    }

    public void toByteArray(byte[] pArrayOfByte, int paramInt) {
      ByteBuffer paramArrayOfByte = ByteBuffer.wrap(pArrayOfByte, paramInt, this.mPayloadSize);
      paramArrayOfByte.order(BYTE_ORDER);
      int i = this.mDataList.size();
      paramInt = 0;
      while(paramInt < i) {
        ((IVariableData)this.mDataList.get(paramInt)).writeTo(paramArrayOfByte);
        paramInt += 1;
      }
    }

    public byte[] toByteArray() {
      byte[] arrayOfByte = new byte[this.mPayloadSize];
      toByteArray(arrayOfByte, 0);
      return arrayOfByte;
    }

    public String toString() {
      return "VariableDataComposer : Payload(" + this.mPayloadSize + ")";
    }

    public void writeBoolean(boolean paramBoolean) {
      addData(new BooleanData(paramBoolean));
    }

    public void writeByte(byte paramByte) {
      addData(new ByteData(paramByte));
    }

    public void writeBytes(byte[] paramArrayOfByte) {
      addData(new ByteArrayData(paramArrayOfByte));
    }

    public void writeBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
      addData(new ByteArrayData(paramArrayOfByte, paramInt1, paramInt2));
    }

    public void writeDouble(double paramDouble) {
      addData(new DoubleData(paramDouble));
    }

    public void writeFloat(float paramFloat) {
      addData(new FloatData(paramFloat));
    }

    public void writeImage(Bitmap paramBitmap) {
      throw new UnsupportedOperationException("Not impelemented!");
    }

    public void writeInt(int paramInt) {
      addData(new IntData(paramInt));
    }

    public void writeLong(long paramLong) {
      addData(new LongData(paramLong));
    }

    public void writeShort(short paramShort) {
      addData(new ShortData(paramShort));
    }

    public void writeString(String paramString) {
      writeBytes(paramString.getBytes(DEFAULT_CHARSET));
    }

    private static class BooleanData implements OpenFitVariableDataComposer.IVariableData {
      private final boolean mValue;

      BooleanData(boolean paramBoolean) {
        this.mValue = paramBoolean;
      }

      public int getByteSize() {
        return 1;
      }

      public void writeTo(ByteBuffer paramByteBuffer) {
        if(this.mValue) {}
        for(int i = 1;; i = 0) {
          paramByteBuffer.put((byte)i);
          return;
        }
      }
    }

    private static class ByteArrayData implements OpenFitVariableDataComposer.IVariableData {
      private final byte[] mCopied;

      ByteArrayData(byte[] paramArrayOfByte) {
        this.mCopied = Arrays.copyOf(paramArrayOfByte, paramArrayOfByte.length);
      }

      ByteArrayData(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        if(paramArrayOfByte.length < paramInt1 + paramInt2) {
          throw new IndexOutOfBoundsException();
        }
        this.mCopied = Arrays.copyOfRange(paramArrayOfByte, paramInt1, paramInt1 + paramInt2);
      }

      public int getByteSize() {
        return this.mCopied.length;
      }

      public void writeTo(ByteBuffer paramByteBuffer) {
        paramByteBuffer.put(this.mCopied);
      }
    }

    private static class ByteData implements OpenFitVariableDataComposer.IVariableData {
      final byte mByte;

      ByteData(byte paramByte) {
        this.mByte = paramByte;
      }

      public int getByteSize() {
        return 1;
      }

      public void writeTo(ByteBuffer paramByteBuffer) {
        paramByteBuffer.put(this.mByte);
      }
    }

    private static class DoubleData implements OpenFitVariableDataComposer.IVariableData {
      private final double mValue;

      DoubleData(double paramDouble) {
        this.mValue = paramDouble;
      }

      public int getByteSize() {
        return 8;
      }

      public void writeTo(ByteBuffer paramByteBuffer) {
        paramByteBuffer.putDouble(this.mValue);
      }
    }

    private static class FloatData implements OpenFitVariableDataComposer.IVariableData {
      private final float mValue;

      FloatData(float paramFloat) {
        this.mValue = paramFloat;
      }

      public int getByteSize() {
        return 4;
      }

      public void writeTo(ByteBuffer paramByteBuffer) {
        paramByteBuffer.putFloat(this.mValue);
      }
    }

    private static abstract interface IVariableData {
      public abstract int getByteSize();
      public abstract void writeTo(ByteBuffer paramByteBuffer);
    }

    private static class IntData implements OpenFitVariableDataComposer.IVariableData {
      private final int mValue;

      IntData(int paramInt) {
        this.mValue = paramInt;
      }

      public int getByteSize() {
        return 4;
      }

      public void writeTo(ByteBuffer paramByteBuffer) {
        paramByteBuffer.putInt(this.mValue);
      }
    }

    private static class LongData implements OpenFitVariableDataComposer.IVariableData {
      private final long mValue;

      LongData(long paramLong) {
        this.mValue = paramLong;
      }

      public int getByteSize() {
        return 8;
      }

      public void writeTo(ByteBuffer paramByteBuffer) {
        paramByteBuffer.putLong(this.mValue);
      }
    }

    private static class ShortData implements OpenFitVariableDataComposer.IVariableData {
      private final short mValue;

      ShortData(short paramShort) {
        this.mValue = paramShort;
      }

      public int getByteSize() {
        return 2;
      }

      public void writeTo(ByteBuffer paramByteBuffer) {
        paramByteBuffer.putShort(this.mValue);
      }
    }
}