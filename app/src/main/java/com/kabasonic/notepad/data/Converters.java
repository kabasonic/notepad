package com.kabasonic.notepad.data;

import androidx.room.TypeConverter;

import java.util.Date;

public class Converters {
    @TypeConverter
    public Date fromTimestamp( Long value) {
        if(value == null)
            return null;
        else{
            return new Date(value);
        }

    }

    @TypeConverter
    public Long dateToTimestamp(Date date) {
        if (date == null) {
            return null;
        } else {
            return date.getTime();
        }
    }



//    @TypeConverter
//    public byte[] toBitmap(Bitmap bitmap){
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
//        return stream.toByteArray();
//    }
//
//    @TypeConverter
//    public Bitmap fromBitmap(byte[] bytes){
//        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//    }

//    @TypeConverter
//    public ArrayList<byte[]> fromBitmap(ArrayList<Bitmap> bitmapArrayList){
//        ArrayList<byte[]> byteArrayList = new ArrayList<>();
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        for(int i=0;i<bitmapArrayList.size();i++){
//            bitmapArrayList.get(i).compress(Bitmap.CompressFormat.PNG,100,outputStream);
//            byteArrayList.add(i,outputStream.toByteArray());
//        }
//        return byteArrayList;
//    }
//
//    @TypeConverter
//    public ArrayList<Bitmap> toBitmap(ArrayList<byte[]> byteArrayList){
//        ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
//        for(int i=0;i<byteArrayList.size();i++){
//            bitmapArrayList.add(i, BitmapFactory.decodeByteArray(byteArrayList.get(i),0,byteArrayList.get(i).length));
//        }
//        return bitmapArrayList;
//    }


//    @TypeConverter
//    public byte[] fromBitmap(Bitmap bitmap){
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
//        return outputStream.toByteArray();
//    }
//
//    @TypeConverter
//    public Bitmap toBitmap(byte[] bytes){
//        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//    }
}
